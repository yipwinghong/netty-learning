package com.ywh.demo.io.bio;

import java.io.IOException;
import java.net.Socket;
import java.util.Date;

/**
 * @author ywh
 * @since 26/02/2020
 */
public class Client {

    public static void main(String[] args) {
        new Thread(() -> {
            try {
                Socket socket = new Socket("127.0.0.1", 8000);
                while (true) {
                    try {
                        socket.getOutputStream().write((new Date() + ": hello world").getBytes());
                        Thread.sleep(2000);
                    } catch (Exception ignored) {
                    }
                }
            } catch (IOException e) {
            }
        }).start();
    }
}
