package com.ywh.im.client.console;

import com.ywh.im.protocol.request.MessageRequestPacket;
import io.netty.channel.Channel;

import java.util.Scanner;

/**
 * 发送用户消息命令
 *
 * @author ywh
 * @since 24/12/2019
 */
public class SendToUserConsoleCommand implements ConsoleCommand {
    @Override
    public void exec(Scanner scanner, Channel channel) {
        System.out.print("请输入接收消息的用户名：");
        String toUserName = scanner.next();
        System.out.print("请输入发送的消息：");
        String message = scanner.next();
        channel.writeAndFlush(new MessageRequestPacket(toUserName, message));
    }
}
