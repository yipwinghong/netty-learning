package com.ywh.im.client.console;

import com.ywh.im.common.protocol.request.QuitGroupRequestPacket;
import io.netty.channel.Channel;

import java.util.Scanner;

/**
 * 退出群组命令
 *
 * @author ywh
 * @since 24/12/2019
 */
public class QuitGroupConsoleCommand implements ConsoleCommand {
    @Override
    public void exec(Scanner scanner, Channel channel) {
        QuitGroupRequestPacket quitGroupRequestPacket = new QuitGroupRequestPacket();
        System.out.print("输入组名退出群聊：");
        quitGroupRequestPacket.setGroupName(scanner.next());
        channel.writeAndFlush(quitGroupRequestPacket);
    }
}
