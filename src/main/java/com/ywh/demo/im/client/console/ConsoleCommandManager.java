package com.ywh.demo.im.client.console;

import com.ywh.demo.im.util.SessionUtil;
import io.netty.channel.Channel;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import static com.ywh.demo.im.constant.Constant.*;

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
        consoleCommandMap.put(CMD_SEND_TO_USER, new SendToUserConsoleCommand());
        consoleCommandMap.put(CMD_LOGOUT, new LogoutConsoleCommand());
        consoleCommandMap.put(CMD_CREATE_GROUP, new CreateGroupConsoleCommand());
        consoleCommandMap.put(CMD_JOIN_GROUP, new JoinGroupConsoleCommand());
        consoleCommandMap.put(CMD_QUIT_GROUP, new QuitGroupConsoleCommand());
        consoleCommandMap.put(CMD_LIST_GROUP_MEMBERS, new ListGroupMembersConsoleCommand());
    }

    @Override
    public void exec(Scanner scanner, Channel channel) {

        // 获取第一个指令
        System.out.print("请输入需要执行的命令：");
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
