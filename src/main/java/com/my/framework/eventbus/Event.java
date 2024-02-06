package com.my.framework.eventbus;

/**
 * @author <a href="mailto:yu.ou@alpha-ess.com">ouyu</a>
 * @date: 2024/02/04 14:56:35
 */
public interface Event {

    /**
     * 返回需要通知的服务名称（如果没有一个名称，将会只通知当前服务）@{link com.alphaess.framework.core.eventbus.PublishStrategy.LOCAL}
     * & @{link com.alphaess.framework.core.eventbus.PublishStrategy.ASYNC}模式不支持该参数
     * @return spring.application.name
     */
    default String[] servers(){return null;}
}
