package com.ywh.im.client.console;

import com.ywh.im.common.protocol.request.CreateGroupRequestPacket;
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
        System.out.print("输入用户名列表创建群组，用户名之间使用英文逗号隔开：");
        String userNames = scanner.next();
        createGroupRequestPacket.setUserNameList(Arrays.asList(userNames.split(USER_ID_SPLITER)));
        channel.writeAndFlush(createGroupRequestPacket);
    }

}
