package com.ywh.example.bio;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @author ywh
 * @since 26/02/2020
 */
public class Server {
    public static void main(String[] args) throws Exception {

        ServerSocket serverSocket = new ServerSocket(8000);

        // 接收新连接线程
        new Thread(() -> {
            while (true) {
                try {
                    // 阻塞获取新的连接
                    Socket socket = serverSocket.accept();

                    // 每一个新的连接都创建一个线程用于读取数据
                    new Thread(() -> {
                        try {
                            int len;
                            byte[] data = new byte[1024];
                            InputStream inputStream = socket.getInputStream();
                            // 按字节流方式读取数据
                            while ((len = inputStream.read(data)) != -1) {
                                System.out.println(new String(data, 0, len));
                            }
                        } catch (IOException e) {
                        }
                    }).start();

                } catch (IOException e) {
                }

            }
        }).start();
    }
}
