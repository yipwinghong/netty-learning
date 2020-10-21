package com.ywh.file.client;

import com.ywh.file.domain.FileBurstData;
import com.ywh.file.domain.FileBurstInstruct;
import com.ywh.file.domain.FileTransferProtocol;
import com.ywh.file.util.FileUtil;
import static com.ywh.file.constant.TransferType.DATA;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static com.ywh.file.constant.FileStatus.COMPLETE;

public class MyClientHandler extends ChannelInboundHandlerAdapter {


    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws IOException {
        // 数据格式验证
        if (!(msg instanceof FileTransferProtocol)) {
            return;
        }

        FileTransferProtocol fileTransferProtocol = (FileTransferProtocol) msg;
        // 接收到 1 指令，表示可以开始向服务端发送文件。
        if (fileTransferProtocol.getTransferType() == 1) {
            handleCmd(ctx, fileTransferProtocol);
        }

        // 模拟断开连接
//        ctx.flush();
//        ctx.close();
//        System.exit(-1);
    }

    /**
     *
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        ctx.close();
        System.out.println("异常信息：\r\n" + cause.getMessage());
    }

    private void handleCmd(ChannelHandlerContext ctx, FileTransferProtocol fileTransferProtocol) throws IOException {
        FileBurstInstruct fileBurstInstruct = (FileBurstInstruct) fileTransferProtocol.getTransferObj();
        if (COMPLETE == fileBurstInstruct.getStatus()) {
            ctx.flush();
            ctx.close();
            System.exit(-1);
            return;
        }
        FileBurstData fileBurstData = FileUtil.readFile(fileBurstInstruct.getClientFileUrl(), fileBurstInstruct.getReadPosition());
        fileTransferProtocol.setTransferType(DATA);
        fileTransferProtocol.setTransferObj(fileBurstData);
        ctx.writeAndFlush(fileTransferProtocol);
        System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) + " 客户端传输文件信息。 FILE：" + fileBurstData.getFileName() + " SIZE(byte)：" + (fileBurstData.getEndPos() - fileBurstData.getBeginPos()));
    }
}
