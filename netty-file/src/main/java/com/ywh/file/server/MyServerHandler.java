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

public class MyServerHandler extends ChannelInboundHandlerAdapter {

    private static Map<String, FileBurstInstruct> burstDataMap = new ConcurrentHashMap<>();

    /**
     * 当客户端主动链接服务端的链接后，这个通道就是活跃的了。也就是客户端与服务端建立了通信通道并且可以传输数据
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        SocketChannel channel = (SocketChannel) ctx.channel();
        System.out.println("链接报告开始");
        System.out.println("链接报告信息：有一客户端链接到本服务端。channelId：" + channel.id());
        System.out.println("链接报告IP:" + channel.localAddress().getHostString());
        System.out.println("链接报告Port:" + channel.localAddress().getPort());
        System.out.println("链接报告完毕");
    }

    /**
     * 当客户端主动断开服务端的链接后，这个通道就是不活跃的。也就是说客户端与服务端的关闭了通信通道并且不可以传输数据
     */
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("客户端断开链接" + ctx.channel().localAddress().toString());
    }

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
     * 抓住异常，当发生异常的时候，可以做一些相应的处理，比如打印日志、关闭链接
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
        fileTransferProtocol.setTransferType(Constants.TransferType.INSTRUCT);
        fileTransferProtocol.setTransferObj(fileBurstInstruct);
        ctx.writeAndFlush(fileTransferProtocol);
        System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) + " 服务端，接收客户端传输文件数据。" + JSON.toJSONString(fileBurstData));

        //传输完成删除断点信息
        if (fileBurstInstruct.getStatus() == Constants.FileStatus.COMPLETE) {
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
            if (fileBurstInstructOld.getStatus() == Constants.FileStatus.COMPLETE) {
                burstDataMap.remove(fileDescInfo.getFileName());
            }
            System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) + " 服务端，接收客户端传输文件请求[断点续传]。" + JSON.toJSONString(fileBurstInstructOld));
            fileTransferProtocol.setTransferType(Constants.TransferType.INSTRUCT);
            fileTransferProtocol.setTransferObj(fileBurstInstructOld);
            ctx.writeAndFlush(fileTransferProtocol);
            return;
        }

        FileBurstInstruct fileBurstInstruct = new FileBurstInstruct();
        fileBurstInstruct.setStatus(Constants.FileStatus.BEGIN);
        fileBurstInstruct.setClientFileUrl(fileDescInfo.getFileUrl());
        fileBurstInstruct.setReadPosition(0);
        fileTransferProtocol.setTransferType(Constants.TransferType.INSTRUCT);
        fileTransferProtocol.setTransferObj(fileBurstInstruct);
        ctx.writeAndFlush(fileTransferProtocol);
        System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) + " 服务端，接收客户端传输文件请求。" + JSON.toJSONString(fileDescInfo));
    }

}
