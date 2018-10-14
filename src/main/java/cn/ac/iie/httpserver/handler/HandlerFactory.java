package cn.ac.iie.httpserver.handler;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;

/**
 * @author Fighter Created on 2018/10/14.
 */
public class HandlerFactory {

    private Map<String, HandlerInterface> handlerInterfaceMap = new HashMap<>();
    private Map<String, Supplier<HandlerInterface>> handlerInterfaceSupplier = new HashMap<>();
    public void registerHandler(String contextPath, String pathInfo, HandlerInterface handler) {
        handlerInterfaceMap.put(contextPath + pathInfo, handler);
    }

    public void registerHandler(String contextPath, String pathInfo, Supplier<HandlerInterface> handler) {
        handlerInterfaceSupplier.put(contextPath + pathInfo, handler);
    }

    //fixme 同步？
    public HandlerInterface getHandler(String requestURI) {
        //先从 handlerInterfaceMap 获取 handler
        HandlerInterface handler = handlerInterfaceMap.get(requestURI);
        if (handler == null) {
            handler = Optional.ofNullable(handlerInterfaceSupplier.get(requestURI)).orElse(null).get();
            if (handler != null) {
                handlerInterfaceMap.put(requestURI, handler);
            }
        }
        return handler;
    }
}
