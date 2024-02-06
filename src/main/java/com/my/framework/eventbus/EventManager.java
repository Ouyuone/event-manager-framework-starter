package com.my.framework.eventbus;


import org.jetbrains.annotations.NotNull;

/**
 * EventManager
 * @author <a href="mailto:yu.ou@alpha-ess.com">ouyu</a>
 * @date: 2024/02/04 14:52:24
 */
public interface EventManager {

    /**
     * 根据策略发布事件
     * @param event - 事件不能为空
     * @param strategy - 策略不能为空
     */
    void publish(@NotNull Event event, @NotNull PublishStrategy strategy);
}
