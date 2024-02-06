package com.my.framework.eventbus;

import lombok.Getter;
import org.springframework.util.CollectionUtils;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

public class EventMappings {

    private static final String EVENT_HANDLE_METHOD_NAME = "handleEvent";

    @Getter
    private final List<EventMapping<? extends Event>> mappings = new ArrayList<>();

    public void addEventHandler(EventHandler<? extends Event> handler) {
        handlerMethod(handler);
    }

    public EventMappings(List<EventHandler<? extends Event>> handlers) {
        init(handlers);
    }

    public void init(List<EventHandler<? extends Event>> handlers) {
        if (CollectionUtils.isEmpty(handlers)) {
            return;
        }

        for (EventHandler<? extends Event> handler : handlers) {
            handlerMethod(handler);
        }
    }

    private void handlerMethod(EventHandler<? extends Event> handler) {
        Class<?> clazz = handler.getClass();
        for (Method method : clazz.getDeclaredMethods()) {
            // @formatter:off
            if (!EVENT_HANDLE_METHOD_NAME.equals(method.getName())) {
                continue;
            }
            if(!Modifier.isPublic(method.getModifiers())){
                continue;
            }
            // @formatter:on

            handlerMethod(method, handler);
        }
    }

    @SuppressWarnings({"rawtypes"})
    private void handlerMethod(Method method, EventHandler<? extends Event> handler) {
        Class<?>[] parameters = method.getParameterTypes();
        if (parameters.length == 1) {
            Class<?> parameterType = parameters[0];
            boolean added = false;

            if (!Event.class.isAssignableFrom(parameterType)) {
                return;
            }

            if (Event.class.equals(parameterType)) {
                return;
            }

            for (EventMapping mapping : mappings) {
                if (mapping.getClazz().equals(parameterType)) {
                    added = mapping.handlers.add(handler);
                    break;
                }
            }

            if (!added) {
                EventMapping mapping = new EventMapping(parameterType);
                mapping.handlers.add(handler);
                mappings.add(mapping);
            }
        }

    }
}