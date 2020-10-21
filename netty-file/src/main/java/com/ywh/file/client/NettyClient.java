package com.ywh.file.client;

import com.ywh.file.codec.ObjDecoder;
import com.ywh.file.codec.ObjEncoder;
import com.ywh.file.domain.FileDescInfo;
import com.ywh.file.domain.FileTransferProtocol;
import com.ywh.file.util.SerializationUtil;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.MessageToByteEncoder;

import java.io.File;

public class NettyClient {

    private EventLoopGroup workerGroup = new NioEventLoopGroup();

    private Channel channel;

    public ChannelFuture connect(String inetHost, int inetPort) {
        ChannelFuture channelFuture = null;
        try {
            Bootstrap b = new Bootstrap()
            .group(workerGroup)
            .channel(NioSocketChannel.class)
            .option(ChannelOption.AUTO_READ, true)
            .handler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel ch) {
                    ch.pipeline()
                        .addLast(new ObjDecoder(FileTransferProtocol.class))
                        .addLast(new ObjEncoder(FileTransferProtocol.class))
                        .addLast(new MyClientHandler());
                }
            });
            channelFuture = b.connect(inetHost, inetPort).syncUninterruptibly();
            this.channel = channelFuture.channel();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (null != channelFuture && channelFuture.isSuccess()) {
                System.out.println("Client 启动成功！");
            } else {
                System.out.println("Client 启动失败！");
            }
        }
        return channelFuture;
    }

    public void destroy() {
        if (null == channel) {
            return;
        }
        channel.close();
        workerGroup.shutdownGracefully();
    }

    public static void main(String[] args) {

        //启动客户端
        ChannelFuture channelFuture = new NettyClient().connect("127.0.0.1", 7397);

        //文件信息{文件大于1024kb方便测试断点续传}
        File file = new File("C:\\Users\\Administrator\\Desktop\\XshellXftp6Portable.zip");
        FileDescInfo fileDescInfo = new FileDescInfo();
        fileDescInfo.setFileUrl(file.getAbsolutePath());
        fileDescInfo.setFileName(file.getName());
        fileDescInfo.setFileSize(file.length());

        FileTransferProtocol fileTransferProtocol = new FileTransferProtocol();
        fileTransferProtocol.setTransferType(0);
        fileTransferProtocol.setTransferObj(fileDescInfo);

        //发送信息；请求传输文件
        channelFuture.channel().writeAndFlush(fileTransferProtocol);

    }
}
