/*
package com.simple.bets.modular.file.utils;

import com.simple.bets.config.BetsProperties;
import com.simple.bets.core.common.util.SpringContextUtils;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

*/
/**
 * 文件分片上传相关实现重构，参考了原代码 UploadUtils
 * @author liuruijun
 * @version 2018-07-05
 *//*


public class UploadUtils {
	private Logger logger = LoggerFactory.getLogger(getClass());
	*/
/**
	 * 表单字段常量
	 *//*

	public static final String FORM_FIELDS = "form_fields";
	*/
/**
	 * 文件域常量
	 *//*

	public static final String FILE_FIELDS = "file_fields";

	private BetsProperties betsProperties = SpringContextUtils.getBean(BetsProperties.class);

	// 文件保存目录相对路径
	private  String upPath = "upload";
	// 上传临时路径
	private  final String TEMP_PATH = "temp";
	private  String tempPath = upPath + File.separator + TEMP_PATH;

	//	 文件保存目录路径
	private  String basePath;
	// 文件保存目录url
	private  String baseUrl;

	private Map<String, List<String>> chunkInfoMap;

	private static Object lock = new Object();
	
	private static class SingletonHolder {
		private static final UploadUtils INSTANCE = new UploadUtils();
	}

	private UploadUtils() {
		chunkInfoMap = new ConcurrentHashMap<String, List<String>>();
		// 获取上传临时路径
		tempPath =  betsProperties.getFilePath()+ tempPath + File.separator;
		File file = new File(tempPath);
		if (!file.exists()) {
			file.mkdirs();
		}
		
		// 文件保存目录路径
		basePath =betsProperties.getFilePath()+ upPath + File.separator;
		// 文件保存目录URL
		baseUrl = betsProperties.getFilePath()+ File.separator + upPath + File.separator;
		
		File baseDirFile = new File(basePath);
		if (!baseDirFile.exists()) {
			baseDirFile.mkdirs();
		}
	}

	public static final UploadUtils getInstance() {
		return SingletonHolder.INSTANCE;
	}

	*/
/**
	 * 文件上传
	 *
	 * @param request
	 * @return infos info[0] 上传文件错误信息 info[1] savePath info[2] saveUrl info[3] fileUrl
	 *//*

	@SuppressWarnings("unchecked")
	public List<UploadResult> uploadFile(HttpServletRequest request) {
		List<UploadResult> resultList = new ArrayList<UploadResult>();
		// 初始化表单元素
		Map<String, Object> fieldsMap = new HashMap<String, Object>();
		try {
			fieldsMap = initFields(request);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		// 上传
		List<FileItem> fiList = (List<FileItem>) fieldsMap.get(UploadUtils.FILE_FIELDS);
		Map<String, String> fields = (Map<String, String>) fieldsMap.get(UploadUtils.FORM_FIELDS);

		if (fiList != null) {
			for (FileItem item : fiList) {
				resultList.add(saveFile(item, fields));
			}
		}

		return resultList;
	}
	
	*/
/**
	 * 处理上传内容
	 * 
	 * @param request
	 * @return
	 * @throws UnsupportedEncodingException 
	 *//*

	public Map<String, Object> initFields(HttpServletRequest request) throws UnsupportedEncodingException {

		// 存储表单字段和非表单字段
		Map<String, Object> map = new HashMap<String, Object>();

		// 第一步：判断request
		boolean isMultipart = ServletFileUpload.isMultipartContent(request);
		// 第二步：解析request
		if (isMultipart) {
			DiskFileItemFactory factory = new DiskFileItemFactory();

			// 阀值,超过这个值才会写到临时目录,否则在内存中
			factory.setSizeThreshold(1024 * 1024 * 10);
			factory.setRepository(new File(tempPath));

			// Create a new file upload handler
			ServletFileUpload upload = new ServletFileUpload(factory);

			upload.setHeaderEncoding("UTF-8");

			// 最大上传限制
//			upload.setSizeMax(maxSize);

			*/
/* FileItem *//*

			List<FileItem> items = null;
			// Parse the request
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
					} else { // 文件域表单元素
						list.add(item);
					}
				}
				map.put(FORM_FIELDS, fields);
				map.put(FILE_FIELDS, list);
			}
		}
		return map;
	}

	*/
/**
	 * 保存文件
	 * @param item
	 * @return
	 *//*

	private UploadResult saveFile(FileItem item, Map<String, String> fields) {
		UploadResult result = new UploadResult();
		
		String ymd = new SimpleDateFormat("yyyyMMdd").format(new Date());
		String rltvPath = fields.get("dirName") + File.separator + ymd + File.separator;
		String savePath = basePath + rltvPath;
//		String saveUrl = baseUrl + fields.get("dirName") + File.separator + ymd + File.separator;
		
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
	
	*/
/**
     * 文件合并  
     * @param fileExt 指定合并文件
     * @param chunk 分割前的文件名
     *//*

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
	
	public class UploadResult {
		public boolean err = false;
		public String fileName;
		public String fileRltvPath;// 相对路径
		
		public boolean merged;
		public String chunk;
		public Map<String, String> fields;
		
		@Override
		public String toString() {
			return "UploadResult [err=" + err + ", fileName=" + fileName + ", fileRltvPath=" + fileRltvPath + ", merged=" + merged
					+ ", chunk=" + chunk + "]";
		}
	}
	
	
	*/
/**
	 * @Author wangdingfeng
	 * @Description 下载文件
	 * @Date 10:18 2019/1/15
	 **//*


	public void downloadFile(String filepath, HttpServletRequest request, HttpServletResponse response) {
		File file = new File(basePath + filepath);
		setHeader(request, response, file.getName());
		response.setContentType("application/pdf;charset=utf-8");
		response.setCharacterEncoding("utf-8");

		BufferedInputStream in = null;
		BufferedOutputStream out = null;
		try {
			in = new BufferedInputStream(new FileInputStream(file));
			out = new BufferedOutputStream(response.getOutputStream());

			byte[] buffer = new byte[1024];
			int len = 0;
			while ((len = in.read(buffer)) != -1) {
				out.write(buffer, 0, len);
			}
			out.flush();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				in.close();
				out.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}
	*/
/**
	 * @Author wangdingfeng
	 * @Description 设置浏览器请求头
	 * @Date 10:18 2019/1/15
	 **//*


	private void setHeader(HttpServletRequest request, HttpServletResponse response, String fileName) {
		String userAgent = request.getHeader("USER-AGENT");
		try {
			String finalFileName = null;
			if (StringUtils.contains(userAgent, "MSIE") || StringUtils.contains(userAgent, "Trident")) {// IE浏览器
				finalFileName = URLEncoder.encode(fileName, "UTF8");
			} else if (StringUtils.contains(userAgent, "Chrome") || StringUtils.contains(userAgent, "Firefox")
					|| StringUtils.contains(userAgent, "Safari")) {// google,火狐浏览器
				finalFileName = new String(fileName.getBytes("UTF-8"), "ISO8859-1");
			} else {
				finalFileName = URLEncoder.encode(fileName, "UTF8");// 其他浏览器
			}
			response.addHeader("Content-Disposition", "attachment;filename=\"" + finalFileName + "\"");// 这里设置一下让浏览器弹出下载提示框，而不是直接在浏览器中打开
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}
}
*/
