package com.my.framework.eventbus;

import lombok.AllArgsConstructor;

/**
 * @author <a href="mailto:yu.ou@alpha-ess.com">ouyu</a>
 * @date: 2024/02/04 14:52:51
 */
@AllArgsConstructor
public enum PublishStrategy {

    /**
     * 本地事件发布，除了参数错误外不会抛出错误
     */
    LOCAL,

    /**
     * 本地异步时间发布，除了参数错误外不会抛出错误
     */
    ASYNC,

    /**
     * KAFKA 交换事件
     * 分布式事件发布，事件可以进行重试消费，消费者注意幂等性
     * 需要引入framework-kafka包才能用
     */
    KAFKA
}
