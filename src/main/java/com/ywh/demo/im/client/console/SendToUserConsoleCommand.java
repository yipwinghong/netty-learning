package com.ywh.demo.im.client.console;

import com.ywh.demo.im.protocol.request.MessageRequestPacket;
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
        System.out.print("发送消息给某个某个用户：");

        String toUserId = scanner.next();
        String message = scanner.next();
        channel.writeAndFlush(new MessageRequestPacket(toUserId, message));
    }
}
