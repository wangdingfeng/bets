package com.simple.bets.modular.file.service;

import com.simple.bets.core.service.IService;
import com.simple.bets.modular.file.model.FileModel;
import com.simple.bets.modular.file.model.UploadResult;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @ProjectName: bets
 * @Package: com.simple.bets.modular.file.service
 * @ClassName: FileService
 * @Author: wangdingfeng
 * @Description: 文件信息
 * @Date: 2019/1/14 18:09
 * @Version: 1.0
 */
public interface FileService extends IService<FileModel> {

    List<UploadResult> uploadFile(HttpServletRequest request);
}
