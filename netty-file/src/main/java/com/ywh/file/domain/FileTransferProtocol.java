package com.ywh.file.domain;

import lombok.Data;

@Data
public class FileTransferProtocol {

    /**
     * 0 请求传输文件，1 文件传输指令，2 文件传输数据
     */
    private Integer transferType;

    /**
     * 数据对象，0 FileDescInfo，1 FileBurstInstruct，2 FileBurstData
     */
    private Object transferObj;

}
