package com.zy.servlet;

import com.zy.server.MyHttpRequest;
import com.zy.server.MyHttpResponse;
import com.zy.server.MyServlet;
import com.zy.server.MyServlet;

public class RegisterServlet implements MyServlet {
    @Override
    public void service(MyHttpRequest myHttpRequest, MyHttpResponse myHttpReponser) {
        myHttpReponser.print("<!DOCTYPE html>");
        myHttpReponser.print("<html lang=\"en\">");
        myHttpReponser.print("<head>");
        myHttpReponser.print("<meta charset=\"UTF-8\">");
        myHttpReponser.print("<title>title</title>");
        myHttpReponser.print("</head>");
        myHttpReponser.print("<body>");
        myHttpReponser.print("<h1>注册成功</h1>");
        myHttpReponser.print("</body>");
        myHttpReponser.print("</html>");
    }
}
