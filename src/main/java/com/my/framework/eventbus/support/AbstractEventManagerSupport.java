package com.my.framework.eventbus.support;

import com.my.framework.eventbus.Event;
import com.my.framework.eventbus.EventMapping;
import com.my.framework.exception.EventHandleException;
import lombok.AllArgsConstructor;

import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.NonNull;

import java.util.ArrayList;
import java.util.List;

/**
 * @author <a href="mailto:yu.ou@alpha-ess.com">ouyu</a>
 * @date: 2024/02/04 15:48:04
 */
@AllArgsConstructor
public abstract class AbstractEventManagerSupport implements EventManagerSupport {
    private final List<EventMapping<? extends Event>> mappings;

    protected final Logger log = LoggerFactory.getLogger(getClass());
    /**
     * @param event
     */
    @Override
    public void publish(@NotNull Event event) {
        if(!supportEvent(event)){
            throw new EventHandleException("无法进行发布的消息类型: " + event.getClass().getName());
        }
        publishInternal(event);
    }


    @SuppressWarnings({"unchecked"})
    protected <T extends Event> List<EventMapping<T>> findMappings(@NonNull T event) {
        List<EventMapping<T>> result = new ArrayList<>();

        Class<? extends Event> clazz = event.getClass();
        for (EventMapping<? extends Event> mapping : this.mappings) {
            if (!clazz.isAssignableFrom(mapping.getClazz())) {
                continue;
            }

            result.add((EventMapping<T>) mapping);
        }

        return result;
    }

    /**
     * 是否支持事件类型
     * @param event
     * @return
     */
    protected abstract boolean supportEvent(@NotNull Event event);

    /**
     * 发布事件
     *
     * @param event {@link Event}
     */
    protected abstract void publishInternal(@NotNull Event event);
}
