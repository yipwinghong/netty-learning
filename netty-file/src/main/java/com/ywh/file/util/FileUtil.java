package com.ywh.file.util;

import com.ywh.file.domain.Constants;
import com.ywh.file.domain.FileBurstData;
import com.ywh.file.domain.FileBurstInstruct;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

public class FileUtil {

    public static FileBurstData readFile(String fileUrl, Integer readPosition) throws IOException {
        File file = new File(fileUrl);
        RandomAccessFile randomAccessFile = new RandomAccessFile(file, "r");
        randomAccessFile.seek(readPosition);
        byte[] bytes = new byte[1024 * 100];
        int readSize = randomAccessFile.read(bytes);
        if (readSize <= 0) {
            randomAccessFile.close();
            return new FileBurstData(Constants.FileStatus.COMPLETE);
        }
        FileBurstData fileInfo = new FileBurstData();
        fileInfo.setFileUrl(fileUrl);
        fileInfo.setFileName(file.getName());
        fileInfo.setBeginPos(readPosition);
        fileInfo.setEndPos(readPosition + readSize);
        if (readSize < 1024 * 100) {
            byte[] copy = new byte[readSize];
            System.arraycopy(bytes, 0, copy, 0, readSize);
            fileInfo.setBytes(copy);
            fileInfo.setStatus(Constants.FileStatus.END);
        } else {
            fileInfo.setBytes(bytes);
            fileInfo.setStatus(Constants.FileStatus.CENTER);
        }
        randomAccessFile.close();
        return fileInfo;
    }

    public static FileBurstInstruct writeFile(String baseUrl, FileBurstData fileBurstData) throws IOException {

        if (Constants.FileStatus.COMPLETE == fileBurstData.getStatus()) {
            return new FileBurstInstruct(Constants.FileStatus.COMPLETE);
        }

        File file = new File(baseUrl + "/" + fileBurstData.getFileName());
        RandomAccessFile randomAccessFile = new RandomAccessFile(file, "rw");
        randomAccessFile.seek(fileBurstData.getBeginPos());
        randomAccessFile.write(fileBurstData.getBytes());
        randomAccessFile.close();

        if (Constants.FileStatus.END == fileBurstData.getStatus()) {
            return new FileBurstInstruct(Constants.FileStatus.COMPLETE);
        }

        //文件分片传输指令
        FileBurstInstruct fileBurstInstruct = new FileBurstInstruct();
        fileBurstInstruct.setStatus(Constants.FileStatus.CENTER);
        fileBurstInstruct.setClientFileUrl(fileBurstData.getFileUrl());
        fileBurstInstruct.setReadPosition(fileBurstData.getEndPos() + 1);

        return fileBurstInstruct;
    }

}
