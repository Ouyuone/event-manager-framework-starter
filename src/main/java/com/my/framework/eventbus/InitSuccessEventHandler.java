package com.my.framework.eventbus;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author <a href="mailto:yu.ou@alpha-ess.com">ouyu</a>
 * @date: 2024/02/04 16:26:13
 */
@Slf4j
public class InitSuccessEventHandler implements EventHandler<InitEventManagerSuccessEvent> {
    /**
     * @param event 事件
     */
    @Override
    public void handleEvent(InitEventManagerSuccessEvent event) {
        event.printEventMapping();
        log.info("Initialize The Event Manager");
    }
}
