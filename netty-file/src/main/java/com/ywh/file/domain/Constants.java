package com.ywh.file.domain;

public class Constants {

    /**
     * 文件状态：开始，中间，结尾，完成。
     */
    public static class FileStatus {
        public static int BEGIN = 0;
        public static int CENTER = 1;
        public static int END = 2;
        public static int COMPLETE = 3;
    }

    /**
     * 传送文件请求
     * 传送文件指令
     * 传送文件数据
     */
    public static class TransferType {
        public static int REQUEST = 0;
        public static int INSTRUCT = 1;
        public static int DATA = 2;
    }

}
