package com.my.framework.eventbus;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author <a href="mailto:yu.ou@alpha-ess.com">ouyu</a>
 * @date: 2024/02/04 16:25:03
 */
@Slf4j
@AllArgsConstructor
public class InitEventManagerSuccessEvent implements Event {

    private final List<EventMapping<? extends Event>> mappings;

    public void printEventMapping(){
        mappings.forEach(mapping->{
            Class<? extends Event> clazz = mapping.getClazz();
            List<String> handlers = mapping.handlers.stream().map(handler -> handler.getClass().getPackageName() + "." + handler.getClass().getSimpleName() +";").collect(Collectors.toList());
            handlers.set(handlers.size() -1,handlers.get(handlers.size() -1).substring(0,handlers.get(handlers.size() -1).length()-1));
            if (log.isDebugEnabled()) {
                log.debug("Local Event: {} -> handler: {}",clazz,handlers);
            }
        });
    }
}
