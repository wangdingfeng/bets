package com.simple.bets.modular.file.web;

import cn.hutool.core.lang.UUID;
import com.simple.bets.core.common.lang.StringUtils;
import com.simple.bets.core.common.util.ToolUtils;
import com.simple.bets.core.base.controller.BaseController;
import com.simple.bets.core.base.model.ResponseResult;
import com.simple.bets.modular.file.model.FileModel;
import com.simple.bets.modular.file.model.UploadResult;
import com.simple.bets.modular.file.service.FileService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @ProjectName: bets
 * @Package: com.simple.bets.modular.file.web
 * @ClassName: FileController
 * @Author: wangdingfeng
 * @Description: ${description}
 * @Date: 2019/1/14 18:11
 * @Version: 1.0
 */
@Controller
@RequestMapping("file")
public class FileController extends BaseController {

    private Logger logger= LoggerFactory.getLogger(getClass());

    @Autowired
    private FileService fileService;

    @RequestMapping("list")
    public String list(Model model){
        String uuid = UUID.randomUUID().toString();
        model.addAttribute("uuid",uuid);
        return "file/file-form";
    }

    /**
     * @Author wangdingfeng
     * @Description //上传文件
     * @Date 10:25 2019/1/15
     **/
    @RequestMapping(value = "upload", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String upload(HttpServletRequest request) {
        List<UploadResult> result = fileService.uploadFile(request);
        for (UploadResult uploadResult : result) {
            logger.info("上传结果----uploadResult----" + uploadResult.toString());
            if (!uploadResult.err) {
                FileModel model = new FileModel();
                if (StringUtils.isBlank(uploadResult.chunk) || uploadResult.merged) {// 写入数据库
                    model.setFileName(uploadResult.fileName);
                    model.setFilePath(uploadResult.fileRltvPath);
                    model.setFileSize(ToolUtils.readableFileSize(uploadResult.fields.get("size")));
                    model.setStatus("1");
                    fileService.save(model);
                    return ResponseResult.ok("上传成功").toString();
                }

            }
        }
        return ResponseResult.error("上传失败").toString();
    }
}
