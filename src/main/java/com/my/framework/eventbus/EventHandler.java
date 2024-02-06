package com.my.framework.eventbus;

/**
 * 事件处理
 * @param <E>
 */
public interface EventHandler<E extends Event> {

    /**
     * 处理事件(父类型E可以处理所有E类型以下的事件类型)
     *
     * @param event 事件
     */
    void handleEvent(E event);

}