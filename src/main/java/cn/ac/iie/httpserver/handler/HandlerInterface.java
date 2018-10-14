package cn.ac.iie.httpserver.handler;

import org.eclipse.jetty.server.Request;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 用户自定义 handler都实现 HandlerInterface 接口即可
 *
 * @author Fighter Created on 2018/10/14.
 */
public interface HandlerInterface {
    void execute(Request baseRequest, HttpServletRequest request, HttpServletResponse response) throws Exception;
}
