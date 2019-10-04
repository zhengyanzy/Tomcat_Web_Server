package com.zy.server;

import jdk.internal.util.xml.impl.Input;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.HashMap;

public class MyHttpRequest {
    private String url;
    private InputStream inputStream;
    //封装请求参数
    private HashMap<String,String> hashMap = new HashMap<>();
    //解析参数
    public MyHttpRequest(InputStream inputStream) {
        byte[] bytes = new byte[1024*10];
        System.out.println("等待接收数据");
        int len = 0;
        try {
            len = inputStream.read(bytes);
        } catch (IOException e) {
            e.printStackTrace();
        }

        //如果在读取就会堵塞,原因可能没有读取到一个结束标志,未解决（采取一次性读取）
        //int read1 = inputStream.read();
        //System.out.println("read1-------"+read1);

        //浏览器会发送两个请求(其中有一个请求应该是/favicon.ico),这个/favicon.ico请求有时候读出不了数据返回-1（未解之谜）
        if(len!=-1){
            String s = new String(bytes, 0, len);
            String method = s.substring(0, s.indexOf(" "));
            url = s.substring(s.indexOf("/"), s.indexOf(" ",s.indexOf("/")));
            //表示有参数
            if (url.indexOf("?")!=-1){
                String parameter = url.substring(url.indexOf("?")+1, url.lastIndexOf(""));
                System.out.println(parameter);
                //parameter类似username=1&password=2可以将他存储为HashMap中
                String[] split1 = parameter.split("&");
                for (String str:split1){
                    String[] split = str.split("=");
                    String[] strings = Arrays.copyOf(split, 2);
                    hashMap.put(strings[0],strings[1]);
                }
                url = url.substring(0,url.indexOf("?"));
            }
//            System.out.println(method);
//            System.out.println(url);
        }
        System.out.println(len+"int");
    }
    public String  getParmater(String key){
        return hashMap.get(key);
    }
    public String getUrl(){
        return url;
    }
}
