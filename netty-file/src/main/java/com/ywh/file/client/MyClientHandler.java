package com.ywh.file.client;

import com.ywh.file.domain.Constants;
import com.ywh.file.domain.FileBurstData;
import com.ywh.file.domain.FileBurstInstruct;
import com.ywh.file.domain.FileTransferProtocol;
import com.ywh.file.util.FileUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.socket.SocketChannel;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MyClientHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        SocketChannel channel = (SocketChannel) ctx.channel();
        System.out.println("链接报告开始");
        System.out.println("链接报告信息：本客户端链接到服务端。channelId：" + channel.id());
        System.out.println("链接报告IP:" + channel.localAddress().getHostString());
        System.out.println("链接报告Port:" + channel.localAddress().getPort());
        System.out.println("链接报告完毕");
    }

    /**
     * 当客户端主动断开服务端的链接后，这个通道就是不活跃的。也就是说客户端与服务端的关闭了通信通道并且不可以传输数据
     */
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("断开链接" + ctx.channel().localAddress().toString());
        super.channelInactive(ctx);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws IOException {
        // 数据格式验证
        if (!(msg instanceof FileTransferProtocol)) {
            return;
        }

        FileTransferProtocol fileTransferProtocol = (FileTransferProtocol) msg;
        // 0 传输文件请求，1 文件传输指令，2 文件传输数据
        if (fileTransferProtocol.getTransferType() == 1) {
            handleCmd(ctx, fileTransferProtocol);
        }

        // 模拟断开连接
//        ctx.flush();
//        ctx.close();
//        System.exit(-1);
    }

    /**
     * 抓住异常，当发生异常的时候，可以做一些相应的处理，比如打印日志、关闭链接
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        ctx.close();
        System.out.println("异常信息：\r\n" + cause.getMessage());
    }

    private void handleCmd(ChannelHandlerContext ctx, FileTransferProtocol fileTransferProtocol) throws IOException {
        FileBurstInstruct fileBurstInstruct = (FileBurstInstruct) fileTransferProtocol.getTransferObj();
        if (Constants.FileStatus.COMPLETE == fileBurstInstruct.getStatus()) {
            ctx.flush();
            ctx.close();
            System.exit(-1);
            return;
        }
        FileBurstData fileBurstData = FileUtil.readFile(fileBurstInstruct.getClientFileUrl(), fileBurstInstruct.getReadPosition());
        fileTransferProtocol.setTransferType(Constants.TransferType.DATA);
        fileTransferProtocol.setTransferObj(fileBurstData);
        ctx.writeAndFlush(fileTransferProtocol);
        System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) + " 客户端传输文件信息。 FILE：" + fileBurstData.getFileName() + " SIZE(byte)：" + (fileBurstData.getEndPos() - fileBurstData.getBeginPos()));
    }
}
