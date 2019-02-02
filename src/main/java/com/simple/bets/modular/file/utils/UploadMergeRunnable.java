package com.simple.bets.modular.file.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * 合并分片文件
 * @author liuruijun
 * @version 2018-07-03
 */
public class UploadMergeRunnable implements Runnable {
	private Logger logger = LoggerFactory.getLogger(getClass());

	private long startPos;
	private String mergeFileName;
	private File partFile;

    public UploadMergeRunnable(long startPos, String mergeFileName, File partFile) {
        this.startPos = startPos;
        this.mergeFileName = mergeFileName;
        this.partFile = partFile;
    }

	@Override
	public void run() {
		RandomAccessFile rFile;
		FileInputStream fs = null;
		try {
			logger.info("合并文件chunk--" + (startPos/(10*1024*1024)));
			
			rFile = new RandomAccessFile(mergeFileName, "rw");
			rFile.seek(startPos);
			fs = new FileInputStream(partFile);
			byte[] b = new byte[fs.available()];
			fs.read(b);

			rFile.write(b);
			rFile.close();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				fs.close();
				fs = null;
				
				if(!partFile.delete()) {
					logger.info("删除分片文件chunk-- 失败----------");
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}
