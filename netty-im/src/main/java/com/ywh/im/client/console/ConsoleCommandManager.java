package com.ywh.im.client.console;

import com.ywh.im.common.session.SessionUtil;
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

    private static final String CMD_SEND_TO_USER = "sendToUser";

    private static final String CMD_LOGOUT = "logout";

    private static final String CMD_CREATE_GROUP = "createGroup";

    private static final String CMD_JOIN_GROUP = "joinGroup";

    private static final String CMD_QUIT_GROUP = "quitGroup";

    private static final String CMD_LIST_GROUP_MEMBERS = "listGroupMembers";

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
            System.err.println("无法识别 [" + command + "] 指令，请重新输入！");
        }
    }
}
