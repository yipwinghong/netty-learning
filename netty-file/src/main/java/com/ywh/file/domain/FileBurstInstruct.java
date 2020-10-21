package com.ywh.file.domain;

import lombok.Data;

/**
 *
 */
@Data
public class FileBurstInstruct {

    /**
     * 0 开始、1 中间、2 结尾、3 完成
     */
    private Integer status;

    /**
     * 客户端文件URL
     */
    private String clientFileUrl;

    /**
     * 读取位置
     */
    private Integer readPosition;

    public FileBurstInstruct(){}

    public FileBurstInstruct(Integer status) {
        this.status = status;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getClientFileUrl() {
        return clientFileUrl;
    }

    public void setClientFileUrl(String clientFileUrl) {
        this.clientFileUrl = clientFileUrl;
    }

    public Integer getReadPosition() {
        return readPosition;
    }

    public void setReadPosition(Integer readPosition) {
        this.readPosition = readPosition;
    }
}
