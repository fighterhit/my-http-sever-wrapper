package cn.ac.iie.httpserver.server;

import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;

/**
 * server 具体实现类
 *
 * @author Fighter Created on 2018/10/14.
 */
public class HttpServer extends AbstractServer {

    public HttpServer(String ip, int port, int parallelSize) throws Exception {
        super(ip, port, parallelSize);
    }

    @Override
    protected Connector[] getConnectors(Server server, String ip, int port) throws Exception {
        ServerConnector connector = new ServerConnector(server);
        connector.setHost(ip);
        connector.setPort(port);
        return new Connector[]{connector};
    }
}
