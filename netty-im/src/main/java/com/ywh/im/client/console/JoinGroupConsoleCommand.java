package com.ywh.im.client.console;

import com.ywh.im.common.protocol.request.JoinGroupRequestPacket;
import io.netty.channel.Channel;

import java.util.Scanner;

/**
 * 加入群组命令
 *
 * @author ywh
 * @since 24/12/2019
 */
public class JoinGroupConsoleCommand implements ConsoleCommand {
    @Override
    public void exec(Scanner scanner, Channel channel) {
        JoinGroupRequestPacket joinGroupRequestPacket = new JoinGroupRequestPacket();

        System.out.print("输入 groupName，加入群聊：");
        String groupName = scanner.next();

        joinGroupRequestPacket.setGroupName(groupName);
        channel.writeAndFlush(joinGroupRequestPacket);
    }
}
