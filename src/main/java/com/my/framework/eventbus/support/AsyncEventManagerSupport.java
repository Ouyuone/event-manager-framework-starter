package com.my.framework.eventbus.support;
import com.my.framework.eventbus.BroadcastEvent;
import com.my.framework.eventbus.Event;
import com.my.framework.eventbus.EventMapping;
import com.my.framework.eventbus.PublishStrategy;
import org.jetbrains.annotations.NotNull;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.List;

/**
 * @author <a href="mailto:yu.ou@alpha-ess.com">ouyu</a>
 * @date: 2024/02/04 17:12:19
 */
public class AsyncEventManagerSupport extends LocalEventManagerSupport  implements EventManagerSupport{

    private final ThreadPoolTaskExecutor asyncExecutorService;

    public AsyncEventManagerSupport(ThreadPoolTaskExecutor asyncExecutorService, List<EventMapping<? extends Event>> mappings) {
        super(mappings);
        this.asyncExecutorService =asyncExecutorService;
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
        asyncExecutorService.execute(()->this.publishLocalAndIgnoreException(event));

    }

    /**
     * @return
     */
    @Override
    public PublishStrategy support() {
        return PublishStrategy.ASYNC;
    }
}
