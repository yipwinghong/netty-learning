package com.ywh.demo.im.client.console;

import com.ywh.demo.im.protocol.request.CreateGroupRequestPacket;
import io.netty.channel.Channel;

import java.util.Arrays;
import java.util.Scanner;

/**
 * 创建群组命令
 *
 * @author ywh
 * @since 24/12/2019
 */
public class CreateGroupConsoleCommand implements ConsoleCommand {

    private static final String USER_ID_SPLITER = ",";

    @Override
    public void exec(Scanner scanner, Channel channel) {
        CreateGroupRequestPacket createGroupRequestPacket = new CreateGroupRequestPacket();

        System.out.print("【拉人群聊】输入 userName 列表，userName 之间英文逗号隔开：");
        String userNames = scanner.next();
        createGroupRequestPacket.setUserNameList(Arrays.asList(userNames.split(USER_ID_SPLITER)));
        channel.writeAndFlush(createGroupRequestPacket);
    }

}
