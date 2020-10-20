package com.ywh.im.client.console;

import com.ywh.im.common.protocol.request.ListGroupMembersRequestPacket;
import io.netty.channel.Channel;

import java.util.Scanner;

/**
 * 查看群组成员命令
 *
 * @author ywh
 * @since 24/12/2019
 */
public class ListGroupMembersConsoleCommand implements ConsoleCommand {

    @Override
    public void exec(Scanner scanner, Channel channel) {
        ListGroupMembersRequestPacket listGroupMembersRequestPacket = new ListGroupMembersRequestPacket();
        System.out.print("输入组名获取群成员：");
        listGroupMembersRequestPacket.setGroupName(scanner.next());
        channel.writeAndFlush(listGroupMembersRequestPacket);
    }
}
