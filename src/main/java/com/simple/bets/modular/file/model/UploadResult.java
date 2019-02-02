package com.simple.bets.modular.file.model;

import java.util.Map;

/**
 * @ProjectName: bets
 * @Package: com.simple.bets.modular.file.model
 * @ClassName: UploadResult
 * @Author: wangdingfeng
 * @Description: ${description}
 * @Date: 2019/1/15 11:37
 * @Version: 1.0
 */
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
