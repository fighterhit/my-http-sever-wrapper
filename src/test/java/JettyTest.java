import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.server.handler.AbstractHandler;
import org.eclipse.jetty.server.handler.ContextHandler;
import org.eclipse.jetty.server.handler.ContextHandlerCollection;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @author Fighter Created on 2018/10/13.
 */
public class JettyTest {
    public static void main(String[] args) throws Exception {
        Server server = new Server();
        ServerConnector connector1 = new ServerConnector(server);
        connector1.setPort(8080);
//        ServerConnector connector2 = new ServerConnector(server);
//        connector2.setPort(8889);

        //        ContextHandler context = new ContextHandler("/");
//        context.setContextPath("/");
//        context.setHandler(MyHandler.getInstance());

        ContextHandler contextFR = new ContextHandler();
        contextFR.setContextPath("hello/");
        contextFR.setHandler(new MyHandler());

//        ContextHandler contextIT = new ContextHandler("/it");
//        contextIT.setHandler(new HelloHandler("Bongiorno"));
//        ContextHandler contextV = new ContextHandler("/");
//        contextV.setVirtualHosts(new String[]{"127.0.0.2"});
//        contextV.setHandler(new HelloHandler("Virtual Hello"));
        ContextHandlerCollection contexts = new ContextHandlerCollection();
//        contexts.setHandlers(new Handler[]{context, contextFR, contextIT, contextV});
        contexts.setHandlers(new Handler[]{contextFR});
        server.setHandler(contexts);
        server.setConnectors(new ServerConnector[]{connector1});
        server.start();
        server.join();
    }
}

class MyHandler extends AbstractHandler {

    @Override
    public void handle(String s, Request request, HttpServletRequest httpServletRequest, HttpServletResponse response) throws IOException, ServletException {
        System.out.println("target: " + s);
        System.out.println("httpServletRequest.getContextPath: " + httpServletRequest.getContextPath());
        System.out.println("httpServletRequest.getPathInfo: " + httpServletRequest.getPathInfo());
        System.out.println("httpServletRequest.getServletPath: " + httpServletRequest.getServletPath());
        response.setContentType("text/html");
        response.setStatus(HttpServletResponse.SC_OK);
        response.getWriter().println("<h1>Hello Servletaaa</h1>");
        request.setHandled(true);
    }
}

class HelloHandler extends AbstractHandler {
    final String greeting;
    final String body;

    public HelloHandler() {
        this("Hello World");
    }

    public HelloHandler(String greeting) {
        this(greeting, null);
    }

    public HelloHandler(String greeting, String body) {
        this.greeting = greeting;
        this.body = body;
    }

    @Override
    public void handle(String target,
                       Request baseRequest,
                       HttpServletRequest request,
                       HttpServletResponse response) throws IOException,
            ServletException {
        response.setContentType("text/html; charset=utf-8");
        response.setStatus(HttpServletResponse.SC_OK);

        PrintWriter out = response.getWriter();

        out.println("<h1>" + greeting + "</h1>");
        if (body != null) {
            out.println(body);
        }

        baseRequest.setHandled(true);
    }
}
