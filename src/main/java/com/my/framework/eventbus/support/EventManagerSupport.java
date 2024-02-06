package com.my.framework.eventbus.support;

import com.my.framework.eventbus.Event;
import com.my.framework.eventbus.PublishStrategy;
import org.jetbrains.annotations.NotNull;

/**
 * EventManagerSupport
 * @author <a href="mailto:yu.ou@alpha-ess.com">ouyu</a>
 * @date: 2024/02/04 15:15:34
 */
public interface EventManagerSupport {


    /**
     * 发布事件
     * @param event
     */
    void publish(@NotNull Event event);


    /**
     * 获取支持的策略
     * @return PublishStrategy
     */
    PublishStrategy support();
}
