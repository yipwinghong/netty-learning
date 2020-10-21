package com.ywh.file.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class FileBurstData {

    /**
     * 客户端文件地址
     */
    private String fileUrl;

    /**
     * 文件名称
     */
    private String fileName;

    /**
     * 开始位置
     */
    private Integer beginPos;

    /**
     * 结束位置
     */
    private Integer endPos;

    /**
     * 文件字节；实际应用中可以使用非对称加密，以保证传输信息安全
     */
    private byte[] bytes;

    /**
     * 0 开始、1 中间、2 结尾、3 完成
     */
    private Integer status;

    public FileBurstData(int fileStatus) {
        this.status = fileStatus;
    }
}
