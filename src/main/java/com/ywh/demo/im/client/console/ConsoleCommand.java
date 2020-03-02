package com.ywh.demo.im.client.console;

import io.netty.channel.Channel;

import java.util.Scanner;

/**
 * 输入命令
 *
 * @author ywh
 * @since 24/12/2019
 */
public interface ConsoleCommand {
    /**
     * 执行命令
     *
     * @param scanner
     * @param channel
     */
    void exec(Scanner scanner, Channel channel);
}
