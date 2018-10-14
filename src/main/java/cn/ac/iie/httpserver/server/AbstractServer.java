package cn.ac.iie.httpserver.server;

import cn.ac.iie.httpserver.handler.HandlerDispatcher;
import cn.ac.iie.httpserver.handler.HandlerFactory;
import cn.ac.iie.httpserver.handler.HandlerInterface;
import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.ContextHandler;
import org.eclipse.jetty.server.handler.ContextHandlerCollection;
import org.eclipse.jetty.util.thread.QueuedThreadPool;

import java.util.function.Supplier;

/**
 * server 抽象类，包括初始化、启动、关闭等
 *
 * @author Fighter Created on 2018/10/14.
 * @see <a href="http://www.eclipse.org/jetty/documentation/9.4.12.v20180830/embedding-jetty.html#_embedding_contexts">Embedding Contexts</a>
 */
public abstract class AbstractServer {

    String ip;
    int port = -1;
    int parallelSize = -1;

    boolean ready, started;

    private Server server;
    private HandlerFactory factory;
    private ContextHandlerCollection contexts;


    public AbstractServer(String ip, int port, int parallelSize) throws Exception {

        this.ip = ip;
        this.port = port;
        this.parallelSize = parallelSize;

        server = new Server(new QueuedThreadPool(parallelSize));
        contexts = new ContextHandlerCollection();
        factory = new HandlerFactory();
    }

    public void startServer() throws Exception {
        if (ready) {
            if (!started) {
                init();
                server.start();
                started = true;
            }
        } else {
            throw new Exception("server cannot start because context is empty!");
        }
    }

    /**
     * server 初始化
     *
     * @throws Exception
     */
    private void init() throws Exception {
        if (ip == null || ip.trim().isEmpty()) {
            throw new Exception("server ip is illegal!");
        }
        if (port == -1) {
            throw new Exception("server port is illegal!");
        }
        if (parallelSize < 32) {
            throw new Exception("parallel size is illegal or less than 32!");
        }
        server.setConnectors(getConnectors(server, ip, port));
        server.setHandler(contexts);
    }

    protected abstract Connector[] getConnectors(Server server, String ip, int port) throws Exception;

    //fixme，注册上下文环境，后面用户 registerHandler 必须在此 contextPath 路径下，否则无法访问；故可直接注册为 "/"
    public void registerContext(String contextPath) throws Exception {
        HandlerDispatcher handlerDispatcher = HandlerDispatcher.getInstance(factory);
        if (handlerDispatcher == null) {
            throw new Exception("HandlerDispatcher init error!");
        }
        ContextHandler contextHandler = new ContextHandler(contextPath);
        contextHandler.setHandler(handlerDispatcher);
        contexts.addHandler(contextHandler);
        ready = true;
    }

    public void registerHandler(String contextPath, String pathInfo, Supplier<HandlerInterface> handlerSupplier) {
        contextPath = formatPath(contextPath);
        pathInfo = formatPath(pathInfo);
        factory.registerHandler(contextPath, pathInfo, handlerSupplier);
    }

    public void registerHandler(String contextPath, String pathInfo, HandlerInterface handlerInterface) {
        contextPath = formatPath(contextPath);
        pathInfo = formatPath(pathInfo);
        factory.registerHandler(contextPath, pathInfo, handlerInterface);
    }

    //去除 contextPath 和 pathInfo 尾部 "/"，即 "/hello" => "/hello/"
    public String formatPath(String path) {
        if (path.endsWith("/")) {
            String tmp = path.substring(0, path.length() - 1);
            path = tmp.length() == 0 ? "/" : tmp;
        }
        return path;
    }

    public void join() throws InterruptedException {
        server.join();
    }

    //fixme 根据 ready 判断则需要重新 registerContext
    public void stop() throws Exception {
        if (started) {
            try {
                server.stop();
            } catch (Exception e) {
                throw new Exception("server stop failed!");
            }
            started = false;

        }
    }

    //返回原生 jetty server
    public Server getJettyServer() {
        return server;
    }

}
