package com.simple.bets.core.common.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import sun.misc.BASE64Decoder;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

/**
 * @Author wangdingfeng
 * @Description //base64转换工具
 * @Date 13:09 2019/1/17
 **/
@Component
public class ImageBase64Util {

    private static Logger logger = LoggerFactory.getLogger(ImageBase64Util.class);

    public static String filePath;

    @Value("${bets.filePath}")
    public void setFilePath(String filePath) {
        ImageBase64Util.filePath = filePath;
    }

    /**
     * base64 转换图片
     * @param imgStr base64
     * @param fileName 文件名称
     * @param savePath 保存路径
     * @return
     */
    public static String generateImage(String imgStr,String fileName,String savePath) throws Exception {   //对字节数组字符串进行Base64解码并生成图片
        if (imgStr == null) //图像数据为空  
            return null;
        BASE64Decoder decoder = new BASE64Decoder();  
        try
        {
            logger.info("base64转图片保存路径="+savePath);
        	 imgStr = imgStr.split(",")[1];
        	 byte[] b = decoder.decodeBuffer(imgStr);
            String basePath = savePath + File.separator;
            File file = new File(filePath+File.separator+basePath);
    		if (!file.exists()) {
    			file.mkdirs();
    		}
    		String saveImagePath = basePath+fileName;
            //生成图片  
            OutputStream out = new FileOutputStream(filePath+File.separator+saveImagePath);
            out.write(b);  
            out.flush();  
            out.close();  
            return savePath+File.separator+fileName;
        }   
        catch (Exception e) {
            logger.error("base64转换图片失败",e);
            throw e ;
        }  
    }  
    
}
