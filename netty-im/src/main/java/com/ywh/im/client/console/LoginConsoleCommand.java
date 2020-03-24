package com.ywh.im.client.console;

import com.ywh.im.protocol.request.LoginRequestPacket;
import io.netty.channel.Channel;

import java.util.Scanner;

/**
 * 登录命令
 *
 * @author ywh
 * @since 24/12/2019
 */
public class LoginConsoleCommand implements ConsoleCommand {

    @Override
    public void exec(Scanner sc, Channel channel) {
        LoginRequestPacket loginRequestPacket = new LoginRequestPacket();
        System.out.print("输入用户名登录：");
        loginRequestPacket.setUserName(sc.nextLine());

        // 密码使用默认的
        loginRequestPacket.setPassword("123456");

        // 发送登录数据包
        channel.writeAndFlush(loginRequestPacket);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException ignored) {
        }
    }
}
