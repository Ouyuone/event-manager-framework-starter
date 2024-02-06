package com.my.framework.eventbus;

import com.my.framework.eventbus.support.KafkaEventManagerSupport;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.kafka.KafkaAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.core.KafkaTemplate;

/**
 * @author <a href="mailto:yu.ou@alpha-ess.com">ouyu</a>
 * @date: 2024/02/04 17:51:51
 */
@ConditionalOnClass({KafkaTemplate.class})
@AutoConfigureAfter({EventBusAutoConfiguration.class, KafkaAutoConfiguration.class})
public class KafkaEventBusConfiguration {

    @Value(KafkaEventManagerSupport.TOPIC_NAME_EL)
    private String topic;


    @Value("k.unique.%s.event-bus")
    private String uniqueKeyTemplate;

    @Value("#{'k.unique.${spring.application.name}.event-bus'}")
    private String uniqueKey;

    @Value("k.shared.event-bus")
    private String sharedKey;

    @Bean
    @ConditionalOnBean(KafkaTemplate.class)
    public KafkaProducer kafkaProducer(KafkaTemplate<String, String> kafkaTemplate){
        return new KafkaProducer(kafkaTemplate);
    }

    @Bean
    @ConditionalOnBean({KafkaProducer.class,KafkaTemplate.class})
    public KafkaEventManagerSupport kafkaEventManagerSupport(
            KafkaProducer kafkaProducer, EventMappings eventMappings) {
        return new KafkaEventManagerSupport(
                topic,
                uniqueKey,
                sharedKey,
                uniqueKeyTemplate,
                kafkaProducer,
                eventMappings.getMappings()
        );
    }
}
