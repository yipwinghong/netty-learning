package com.ywh.demo.im.client.console;

import com.ywh.demo.im.util.SessionUtil;
import io.netty.channel.Channel;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/**
 * 输入命令管理器
 *
 * @author ywh
 * @since 24/12/2019
 */
public class ConsoleCommandManager implements ConsoleCommand {
    private Map<String, ConsoleCommand> consoleCommandMap;

    public ConsoleCommandManager() {
        consoleCommandMap = new HashMap<>();
        consoleCommandMap.put("sendToUser", new SendToUserConsoleCommand());
        consoleCommandMap.put("logout", new LogoutConsoleCommand());
        consoleCommandMap.put("createGroup", new CreateGroupConsoleCommand());
        consoleCommandMap.put("joinGroup", new JoinGroupConsoleCommand());
        consoleCommandMap.put("quitGroup", new QuitGroupConsoleCommand());
        consoleCommandMap.put("listGroupMembers", new ListGroupMembersConsoleCommand());
    }

    @Override
    public void exec(Scanner scanner, Channel channel) {

//        System.out.println();
//        System.out.print("请输入消息接收者：");
//        String toUserName = sc.next();
//        System.out.print("请输入消息：");
//        String message = sc.next();
//        channel.writeAndFlush(new MessageRequestPacket(toUserName, message));
//        System.out.println(new Date() + ": 输入消息已发送至服务端");

        //  获取第一个指令
        String command = scanner.next();

        if (!SessionUtil.hasLogin(channel)) {
            return;
        }

        ConsoleCommand consoleCommand = consoleCommandMap.get(command);

        if (consoleCommand != null) {
            consoleCommand.exec(scanner, channel);
        } else {
            System.err.println("无法识别 [" + command + "] 指令，请重新输入!");
        }
    }
}
