package com.my.framework.eventbus;


import com.my.framework.JsonUtil;
import com.my.framework.eventbus.support.AsyncEventManagerSupport;
import com.my.framework.eventbus.support.EventManagerSupport;
import com.my.framework.eventbus.support.LocalEventManagerSupport;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author <a href="mailto:yu.ou@alpha-ess.com">ouyu</a>
 * @date: 2024/02/04 15:36:19
 */
//@Configuration
@Import({InitSuccessEventHandler.class, JsonUtil.class})
public class EventBusAutoConfiguration implements ApplicationListener<ApplicationReadyEvent> {


    /**
     * @param event
     */
    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        ApplicationContext application = event.getApplicationContext();
        EventMappings mappings = application.getBean(EventMappings.class);
        for (EventHandler<? extends Event> eventHandler : application.getBeansOfType(EventHandler.class).values()) {
            mappings.addEventHandler(eventHandler);
        }
        application.getBean(EventManager.class).publish(new InitEventManagerSuccessEvent(mappings.getMappings()), PublishStrategy.LOCAL);
    }


    @Bean
    @ConditionalOnMissingBean(EventMappings.class)
    public EventMappings eventMappings() {
        List<EventHandler<? extends Event>> handlers = new ArrayList<>();
        return new EventMappings(handlers);
    }

    @Bean
    public LocalEventManagerSupport localEventManagerSupport(EventMappings eventMappings) {
        return new LocalEventManagerSupport(
                eventMappings.getMappings()
        );
    }

    @Bean
    public AsyncEventManagerSupport asyncEventManagerSupport(EventMappings eventMappings) {
        ThreadPoolTaskExecutor threadPoolTaskExecutor = new ThreadPoolTaskExecutor();
        threadPoolTaskExecutor.setCorePoolSize(5);
        threadPoolTaskExecutor.setMaxPoolSize(20);
        threadPoolTaskExecutor.setQueueCapacity(20);
        threadPoolTaskExecutor.setKeepAliveSeconds(300);
        threadPoolTaskExecutor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        threadPoolTaskExecutor.setThreadNamePrefix("event-async-pool");
        return new AsyncEventManagerSupport(
                threadPoolTaskExecutor,
                eventMappings.getMappings()
        );
    }

    @Bean
    @ConditionalOnMissingBean(EventManager.class)
    public DefaultBusEventManager eventManager(List<EventManagerSupport> supports) {
        return new DefaultBusEventManager(supports);
    }
}
