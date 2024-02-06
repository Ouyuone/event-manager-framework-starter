package com.my.framework.eventbus.support;

import com.my.framework.JsonUtil;
import com.my.framework.eventbus.*;
import com.my.framework.exception.EventHandleException;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;
import java.util.List;

/**
 * LocalEventManagerSupport
 * @author <a href="mailto:yu.ou@alpha-ess.com">ouyu</a>
 * @date: 2024/02/04 15:47:46
 */
public class LocalEventManagerSupport  extends AbstractEventManagerSupport implements EventManagerSupport{

    /**
     * @param mappings
     */
    public LocalEventManagerSupport(List<EventMapping<? extends Event>> mappings) {
        super(mappings);
    }

    /**
     * @param event
     * @return
     */
    @Override
    protected boolean supportEvent(@NotNull Event event) {
        return !(event instanceof BroadcastEvent);
    }

    /**
     * @param event {@link Event}
     */
    @Override
    protected void publishInternal(@NotNull Event event) {
        publishLocalAndIgnoreException(event);
    }

    /**
     * 发布事件且忽略所有异常
     */
    protected void publishLocalAndIgnoreException(Event event) {
        publishLocalEvent(event, true);
    }

    /**
     * 发布本地事件
     */
    protected void publishLocalEvent(Event event, boolean ignoreException) {
        for (EventMapping<Event> mapping : findMappings(event)) {
            Iterator<EventHandler<Event>> itr = mapping.iterator();
            while (itr.hasNext()) {
                try {
                    itr.next().handleEvent(event);
                } catch (Exception e) {
                    if (ignoreException) {
                        log.error("事件处理失败 event: " + JsonUtil.toJsonString(event), e);
                    } else {
                        log.error("事件处理失败", e);
                        throw new EventHandleException("事件处理失败", e);
                    }
                }
            }
        }
    }

    /**
     * @return
     */
    @Override
    public PublishStrategy support() {
        return PublishStrategy.LOCAL;
    }
}
