package com.ywh.file.server;

import com.alibaba.fastjson.JSON;
import com.ywh.file.domain.*;
import com.ywh.file.util.FileUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.socket.SocketChannel;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static com.ywh.file.constant.FileStatus.BEGIN;
import static com.ywh.file.constant.FileStatus.COMPLETE;
import static com.ywh.file.constant.TransferType.INSTRUCT;

public class MyServerHandler extends ChannelInboundHandlerAdapter {

    private static Map<String, FileBurstInstruct> burstDataMap = new ConcurrentHashMap<>();

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        // 数据格式验证
        if (!(msg instanceof FileTransferProtocol)) {
            return;
        }

        FileTransferProtocol fileTransferProtocol = (FileTransferProtocol) msg;
        switch (fileTransferProtocol.getTransferType()) {
            case 0:
                handleRequest(ctx, fileTransferProtocol);
                break;
            case 2:
                handleData(ctx, fileTransferProtocol);
                break;
            default:
                break;
        }
    }

    /**
     *
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        ctx.close();
        System.out.println("异常信息：\r\n" + cause.getMessage());
    }

    /**
     *
     * @param ctx
     * @param fileTransferProtocol
     */
    private void handleData(ChannelHandlerContext ctx, FileTransferProtocol fileTransferProtocol) throws IOException {
        FileBurstData fileBurstData = (FileBurstData) fileTransferProtocol.getTransferObj();
        FileBurstInstruct fileBurstInstruct = FileUtil.writeFile("E://", fileBurstData);

        // 保存断点续传信息
        burstDataMap.put(fileBurstData.getFileName(), fileBurstInstruct);
        fileTransferProtocol.setTransferType(INSTRUCT);
        fileTransferProtocol.setTransferObj(fileBurstInstruct);
        ctx.writeAndFlush(fileTransferProtocol);
        System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) + " 接收客户端传输文件数据。" + JSON.toJSONString(fileBurstData));

        //传输完成删除断点信息
        if (fileBurstInstruct.getStatus() == COMPLETE) {
            burstDataMap.remove(fileBurstData.getFileName());
        }
    }

    /**
     *
     *
     * @param ctx
     * @param fileTransferProtocol
     */
    private void handleRequest(ChannelHandlerContext ctx, FileTransferProtocol fileTransferProtocol) {
        FileDescInfo fileDescInfo = (FileDescInfo) fileTransferProtocol.getTransferObj();

        // 断点续传信息，实际应用中需要将断点续传信息保存到数据库中
        FileBurstInstruct fileBurstInstructOld = burstDataMap.get(fileDescInfo.getFileName());
        if (null != fileBurstInstructOld) {
            // 传输完成删除断点信息
            if (fileBurstInstructOld.getStatus() == COMPLETE) {
                burstDataMap.remove(fileDescInfo.getFileName());
            }
            System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) + " 接收客户端传输文件请求[断点续传]。" + JSON.toJSONString(fileBurstInstructOld));
            fileTransferProtocol.setTransferType(INSTRUCT);
            fileTransferProtocol.setTransferObj(fileBurstInstructOld);
            ctx.writeAndFlush(fileTransferProtocol);
            return;
        }

        FileBurstInstruct fileBurstInstruct = new FileBurstInstruct();
        fileBurstInstruct.setStatus(BEGIN);
        fileBurstInstruct.setClientFileUrl(fileDescInfo.getFileUrl());
        fileBurstInstruct.setReadPosition(0);
        fileTransferProtocol.setTransferType(INSTRUCT);
        fileTransferProtocol.setTransferObj(fileBurstInstruct);
        ctx.writeAndFlush(fileTransferProtocol);
        System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) + " 接收客户端传输文件请求。" + JSON.toJSONString(fileDescInfo));
    }

}
