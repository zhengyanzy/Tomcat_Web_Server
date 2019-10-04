package com.zy.server;


import java.io.*;
import java.util.Date;

public class MyHttpResponse {
    private StringBuilder content = new StringBuilder();
    private StringBuilder header = new StringBuilder();
    //正文长度一定要对,浏览器根据这个大小获得对应的数据.
    private int len;

    public void createHeader(int code) throws IOException {
        switch (code) {
            case 200: {
                header.append("HTTP/1.1 200 OK\r\n");
                break;
            }
            case 404: {
                header.append("HTTP/1.0 404 NOT FOUND\r\n");
                InputStream resourceAsStream = this.getClass().getClassLoader().getResourceAsStream("404.html");
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(resourceAsStream));
                String msg;
                while ((msg = bufferedReader.readLine())!=null){
                    System.out.println(msg);
                    content.append(msg);
                }
                len = content.toString().getBytes().length;
                break;
            }
            case 500: {
                header.append("HTTP/1.1 500 SERVER ERROR\r\n");
                break;
            }
        }
        //以下消息不是必须的,如果下面的这些信息不写,上面就必须写成stringBuilder.append("HTTP/1.1 200 ok\r\n\n");
        header.append("Date:").append(new Date()).append("\r\n");
        header.append("Server:").append("Test Server/0.0.1;charset=GBK").append("\r\n");
        header.append("Content-type:").append("text/html").append("\r\n");
        header.append("Content-length:").append(len).append("\r\n").append("\r\n"); //和正文之间必须有两个换行
    }

    public void print(String str) {
        content.append(str);
        len += str.getBytes().length;
    }

    public void sendMessage(BufferedWriter bufferedWriter, int code) {
        try {
            this.createHeader(code);
            bufferedWriter.write(header.toString());
            bufferedWriter.write(content.toString());
            bufferedWriter.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (bufferedWriter != null) {
                try {
                    bufferedWriter.close();
                    System.out.println("bufferedWriter 已关闭");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}