package com.zy.server;

import com.sun.deploy.net.HttpRequest;
import com.zy.servlet.LoginServlet;
import org.xml.sax.SAXException;

import javax.servlet.Servlet;
import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;


public class Server {
    public static void main(String[] args) {
        try {
            ServerSocket server = new ServerSocket(8080);
            System.out.println("服务器启动成功");
            //解析web.xml
            ParseWebXml.parse();
            System.out.println("开始解析web.xml");
            while (true){
                Socket socket = server.accept();
                System.out.println(socket);
                System.out.println("有一个客户端连接....");
                new Thread(new ServerThread(socket)).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }
}
class ServerThread implements Runnable{
    private Socket socket;
    private BufferedWriter bufferedWriter;
    private InputStream inputStream;
    public ServerThread(Socket socket) throws IOException {
        this.socket = socket;
        bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        //bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        inputStream = socket.getInputStream();
    }
    @Override
    public void run() {
        try {
            //处理请求,封装请求参数
            MyHttpRequest httpRequest = new MyHttpRequest(inputStream);
            MyHttpResponse httpResponse = new MyHttpResponse();
            String url = httpRequest.getUrl();

            // 浏览器会自动请求/favicon.ico，我们给他返回一个图片
            if("/favicon.ico".equals(url)){
                this.fileReponse();
                return;
            }
            //通过反射创建Servlet对象
            String clz = WebContext.getClz(url);
            if (clz!=null){
                MyServlet servlet = (MyServlet) Class.forName(clz).getConstructor().newInstance();
                servlet.service(httpRequest,httpResponse);
            }else {
                //return 404
                httpResponse.sendMessage(bufferedWriter,404);
                return;
            }
            //正文,响应给浏览器的
            httpResponse.sendMessage(bufferedWriter,200);
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                if (inputStream!=null){
                    inputStream.close();
                    System.out.println("inputStream 已关闭");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }
    public void fileReponse(){
        //获取图片大小
        String fileName = this.getClass().getClassLoader().getResource("favicon.ico").getPath();//获取文件路径
        long length = new File(fileName).length();
        //使用字节输出流
        OutputStream outputStream = null;
        try {
            outputStream = socket.getOutputStream();
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("HTTP/1.1 200 OK\r\n");
            stringBuilder.append("Date:").append(new Date()).append("\r\n");
            stringBuilder.append("Server:").append("Test Server/0.0.1;charset=GBK").append("\r\n");
            stringBuilder.append("Content-type:").append("bytes").append("\r\n");
            stringBuilder.append("accept-ranges:").append("image/x-icon").append("\r\n");
            stringBuilder.append("Content-length:").append(length).append("\r\n").append("\r\n");
            outputStream.write(stringBuilder.toString().getBytes());

            InputStream fileInputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("favicon.ico");
            int len = -1;
            byte[] bytes = new byte[1024];
            while ((len = fileInputStream.read(bytes))!=-1){
                outputStream.write(bytes,0,len);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if (outputStream!=null){
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}