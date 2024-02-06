package com.my.framework.eventbus;

import com.my.framework.eventbus.support.EventManagerSupport;
import com.my.framework.exception.EventHandleException;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @author <a href="mailto:yu.ou@alpha-ess.com">ouyu</a>
 * @date: 2024/02/04 15:13:38
 */
@Slf4j
public class DefaultBusEventManager implements EventManager{

    private final Map<PublishStrategy, EventManagerSupport> supports = new HashMap<>();

    public DefaultBusEventManager(List<EventManagerSupport> supports) {
        for (EventManagerSupport support : supports) {
            PublishStrategy strategy = support.support();
            if (Objects.nonNull(this.supports.put(strategy, support))) {
                throw new IllegalStateException("duplicate PublishStrategy:" + strategy);
            }
        }

        log.info("registered complete, current support strategy: {}", this.supports.keySet());
    }

    @Override
    public void publish(@NotNull Event event, @NotNull PublishStrategy strategy) {
        EventManagerSupport support = supports.get(strategy);

        if (Objects.isNull(support)) {
            throw new EventHandleException("当前策略不支持, 请检查是否引入对应依赖包: "+ strategy);
        }

        support.publish(event);
    }
}
