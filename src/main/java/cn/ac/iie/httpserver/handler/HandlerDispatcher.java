package cn.ac.iie.httpserver.handler;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.eclipse.jetty.http.HttpStatus;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * handler 调度器，单例
 *
 * @author Fighter Created on 2018/10/14.
 */
public class HandlerDispatcher extends AbstractHandler {

    Logger logger = LoggerFactory.getLogger(HandlerDispatcher.class);

    private static HandlerDispatcher handlerDispatcher;
    HandlerFactory handlerFactory;

    private HandlerDispatcher(HandlerFactory factory) {
        this.handlerFactory = factory;
    }

    public static HandlerDispatcher getInstance(HandlerFactory factory) {
        if (handlerDispatcher == null) {
            handlerDispatcher = new HandlerDispatcher(factory);
        }
        return handlerDispatcher;
    }

    /**
     * handler 调度逻辑，根据用户设置的 contextURI 和 PathInfo 获取指定 handler
     *
     * @param s
     * @param request
     * @param httpServletRequest
     * @param httpServletResponse
     * @throws IOException
     * @throws ServletException
     */
    @Override
    public void handle(String s, Request request, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws IOException, ServletException {
        //不加此行 返回404
        request.setHandled(true);
        String contextPath = request.getContextPath();
        String pathInfo = request.getPathInfo();
        String requestURI = contextPath + pathInfo;

        HandlerInterface handler = handlerFactory.getHandler(requestURI);

        try {
            handler.execute(request, httpServletRequest, httpServletResponse);
        } catch (Exception e) {
            logger.error("server internal error! {}", ExceptionUtils.getFullStackTrace(e));
            httpServletResponse.setStatus(HttpStatus.INTERNAL_SERVER_ERROR_500);
            httpServletResponse.getWriter().println(ExceptionUtils.getStackTrace(e));
        }
    }
}
