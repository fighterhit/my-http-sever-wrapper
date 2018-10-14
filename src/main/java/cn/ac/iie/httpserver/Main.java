package cn.ac.iie.httpserver;

import cn.ac.iie.httpserver.handler.HandlerInterface;
import cn.ac.iie.httpserver.server.HttpServer;
import org.eclipse.jetty.server.Request;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Fighter Created on 2018/10/14.
 */
public class Main {
    public static void main(String[] args) throws Exception {
        HttpServer server = new HttpServer("0.0.0.0", 8888, 32);

        //fixme 注册环境后若想成功调用回调函数，则 url 必须是 /hello 下
        server.registerContext("/hello");

        //fixme，而此处设置当路径为 /image/push => PushHandler，/image/pull => PullHandler，不带 /hello 会返回空 handler
        server.registerHandler("/image", "/push", new PushHandler());
        server.registerHandler("/image", "/pull", new PullHandler());
        server.startServer();
        server.join();
    }
}

class PushHandler implements HandlerInterface {
    @Override
    public void execute(Request baseRequest, HttpServletRequest request, HttpServletResponse response) throws Exception {
        baseRequest.setHandled(true);
        System.out.println("httpServletRequest.getContextPath: " + request.getContextPath());
        System.out.println("httpServletRequest.getPathInfo: " + request.getPathInfo());
        System.out.println("httpServletRequest.getServletPath: " + request.getServletPath());
        response.setContentType("text/html");
        response.setStatus(HttpServletResponse.SC_OK);
        response.getWriter().println("<h1>Push image</h1>");
    }
}

class PullHandler implements HandlerInterface {
    @Override
    public void execute(Request baseRequest, HttpServletRequest request, HttpServletResponse response) throws Exception {
        baseRequest.setHandled(true);
        System.out.println("httpServletRequest.getContextPath: " + request.getContextPath());
        System.out.println("httpServletRequest.getPathInfo: " + request.getPathInfo());
        System.out.println("httpServletRequest.getServletPath: " + request.getServletPath());
        response.setContentType("text/html");
        response.setStatus(HttpServletResponse.SC_OK);
        response.getWriter().println("<h1>Pull image</h1>");
    }
}
