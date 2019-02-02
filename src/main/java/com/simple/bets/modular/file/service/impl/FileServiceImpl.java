package com.simple.bets.modular.file.service.impl;

import com.simple.bets.config.BetsProperties;
import com.simple.bets.core.service.impl.ServiceImpl;
import com.simple.bets.modular.file.model.FileModel;
import com.simple.bets.modular.file.model.UploadResult;
import com.simple.bets.modular.file.service.FileService;
import com.simple.bets.modular.file.utils.UploadMergeRunnable;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @ProjectName: bets
 * @Package: com.simple.bets.modular.file.service.impl
 * @ClassName: FileServiceImpl
 * @Author: wangdingfeng
 * @Description: ${description}
 * @Date: 2019/1/14 18:10
 * @Version: 1.0
 */
@Service
public class FileServiceImpl extends ServiceImpl<FileModel> implements FileService {

    @Autowired
    BetsProperties betsProperties;

    /**
     * 表单字段常量
     */
    public static final String FORM_FIELDS = "form_fields";
    /**
     * 文件域常量
     */
    public static final String FILE_FIELDS = "file_fields";

    public static final String UP_FILE = "upload";

    public static final String TEMP_PATH = "temp";

    private Map<String, List<String>> chunkInfoMap = new ConcurrentHashMap<String, List<String>>();

    private static Object lock = new Object();


    /**
     * 文件上传
     *
     * @param request
     * @return infos info[0] 上传文件错误信息 info[1] savePath info[2] saveUrl info[3] fileUrl
     */
    @Override
    public List<UploadResult> uploadFile(HttpServletRequest request) {
        //判断上传路径是否存在
        String tempPath = betsProperties.getFilePath()+UP_FILE+ File.separator+TEMP_PATH;
        File tempFile  = new File(tempPath);
        if(!tempFile.exists()){
            tempFile.mkdirs();
        }

        List<UploadResult> resultList = new ArrayList<UploadResult>();
        // 初始化表单元素
        Map<String, Object> fieldsMap = new HashMap<String, Object>();
        try {
            fieldsMap = initFields(request);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        // 上传
        List<FileItem> fiList = (List<FileItem>) fieldsMap.get(this.FILE_FIELDS);

        Map<String, String> fields = (Map<String, String>) fieldsMap.get(this.FORM_FIELDS);

        if (fiList != null) {
            for (FileItem item : fiList) {
                resultList.add(saveFile(item, fields));
            }
        }

        return resultList;
    }

    /**
     * 处理上传内容
     *
     * @param request
     * @return
     * @throws UnsupportedEncodingException
     */
    private Map<String, Object> initFields(HttpServletRequest request) throws UnsupportedEncodingException {

        // 存储表单字段和非表单字段
        Map<String, Object> map = new HashMap<String, Object>();

        // 第一步：判断request
        boolean isMultipart = ServletFileUpload.isMultipartContent(request);
        // 第二步：解析request
        if (isMultipart) {
            DiskFileItemFactory factory = new DiskFileItemFactory();

            // 阀值,超过这个值才会写到临时目录,否则在内存中
            factory.setSizeThreshold(1024 * 1024 * 10);
            String tempPath = betsProperties.getFilePath()+UP_FILE+ File.separator+TEMP_PATH;
            factory.setRepository(new File(tempPath));
            ServletFileUpload upload = new ServletFileUpload(factory);
            upload.setHeaderEncoding("UTF-8");
            // 最大上传限制
//			upload.setSizeMax(maxSize);
            List<FileItem> items = null;
            try {
                items = upload.parseRequest(request);
            } catch (FileUploadException e) {
                e.printStackTrace();
            }

            // 第3步：处理uploaded items
            if (items != null && items.size() > 0) {
                Iterator<FileItem> iter = items.iterator();
                // 文件域对象
                List<FileItem> list = new ArrayList<FileItem>();
                // 表单字段
                Map<String, String> fields = new HashMap<String, String>();
                while (iter.hasNext()) {
                    FileItem item = iter.next();
                    // 处理所有表单元素和文件域表单元素
                    if (item.isFormField()) { // 表单元素
                        String name = item.getFieldName();
                        String value = item.getString("UTF-8");
                        fields.put(name, value);
                    } else {
                        // 文件域表单元素
                        list.add(item);
                    }
                }
                map.put(FORM_FIELDS, fields);
                map.put(FILE_FIELDS, list);
            }
        }
        return map;
    }

    /**
     * 保存文件
     * @param item
     * @return
     */
    private UploadResult saveFile(FileItem item, Map<String, String> fields) {
        UploadResult result = new UploadResult();

        String ymd = new SimpleDateFormat("yyyyMMdd").format(new Date());
        String rltvPath = fields.get("dirName") + File.separator + ymd + File.separator;
        String basePath = betsProperties.getFilePath()+UP_FILE+ File.separator;
        String savePath = basePath + rltvPath;
        File dirFile = new File(savePath);
        if (!dirFile.exists()) {
            dirFile.mkdirs();
        }
        String fileName = item.getName();
        String fileRltvPath = rltvPath + fileName;

        String newFileName = fileName.substring(0, fileName.lastIndexOf("."));//去后缀的文件名
        String fileExt = fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();//后缀名
        if(fields.containsKey("chunks")) {// 是否切片
            fileName = newFileName + ("_" + fields.get("chunk") + ".part");
        }

        try {
            String tempPath = betsProperties.getFilePath()+UP_FILE+ File.separator+TEMP_PATH;
            String dir = fields.containsKey("chunks") ? tempPath : savePath;
            File uploadedFile = new File(dir, fileName);

            item.write(uploadedFile);

            if(fields.containsKey("chunks")) {// 是否切片
                result.chunk = fields.get("chunk");
                mergeFile(newFileName, fileExt, fields.get("chunk"), fields.get("chunks"), savePath, result);
            }

        } catch (Exception e) {
            result.err = true;
            e.printStackTrace();
        }
        result.fileName = newFileName + "." + fileExt;
        result.fileRltvPath = fileRltvPath;
        result.fields = fields;

        return result;
    }

    /**
     * 文件合并
     * @param fileExt 指定合并文件
     * @param chunk 分割前的文件名
     */
    public void mergeFile(String newFileName, String fileExt, String chunk, String chunks, String savePath, UploadResult result) {
        int chunkCount = Integer.parseInt(chunks);
        synchronized(lock) {
            if(chunkInfoMap.get(newFileName) == null) {
                chunkInfoMap.put(newFileName, new ArrayList<String>());
            }

            chunkInfoMap.get(newFileName).add(chunk);

            if(chunkInfoMap.get(newFileName).size() != chunkCount) {
                logger.info("chunkInfoMap--get(" + newFileName + ")-size=" + chunkInfoMap.get(newFileName).size());
                return;
            }
        }
        logger.info("开始合并------------------------" + chunkInfoMap.get(newFileName).size());
        ExecutorService service = null;
        String tempPath = betsProperties.getFilePath()+UP_FILE+ File.separator+TEMP_PATH+ File.separator;
        try {
            String fileName = savePath + newFileName + "." + fileExt;
            service = Executors.newFixedThreadPool(5);
            // 开始合并文件，对应切片的二进制文件
            for (int i = 0; i < chunkCount; i++) {
                String chunkFile = (tempPath + newFileName + "_" + i + ".part");
                service.submit(new UploadMergeRunnable(i*10*1024*1024, fileName, new File(chunkFile)));
            }
        } catch (Exception e) {
            e.printStackTrace();
            result.err = true;
        } finally {
            service.shutdown();
            result.merged = true;
            logger.info("合并完成------------------------关闭线程池");
        }
    }

}
