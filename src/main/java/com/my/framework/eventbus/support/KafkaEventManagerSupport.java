package com.my.framework.eventbus.support;

import com.my.framework.JsonUtil;
import com.my.framework.eventbus.*;
import lombok.SneakyThrows;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.header.Header;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @author <a href="mailto:yu.ou@alpha-ess.com">ouyu</a>
 * @date: 2024/02/04 17:45:16
 */
public class KafkaEventManagerSupport extends LocalEventManagerSupport implements EventManagerSupport {

    private final KafkaProducer kafkaProducer;

    private static final String TYPE_ID = "__typeId";

    private final String topic;

    private final String uniqueKey;

    private final String sharedKey;

    private final String uniqueKeyTemplate;

    public static final String TOPIC_NAME_EL = "${event-bus.kafka.topic:au-vpp_framework_event_bus}";


    public static final String GROUP_NAME_EL = "${event-bus.kafka.group-id:${spring.application.name}}";


    public KafkaEventManagerSupport(String topic, String uniqueKey, String sharedKey,
                                    String uniqueKeyTemplate, KafkaProducer kafkaProducer, List<EventMapping<? extends Event>> mappings) {
        super(mappings);
        this.kafkaProducer = kafkaProducer;
        this.topic = topic;
        this.uniqueKey = uniqueKey;
        this.sharedKey = sharedKey;
        this.uniqueKeyTemplate = uniqueKeyTemplate;
    }

    /**
     * @param event
     * @return
     */
    @Override
    protected boolean supportEvent(Event event) {
        return true;
    }

    /**
     * @param event {@link Event}
     */
    @Override
    protected void publishInternal(Event event) {
        String[] servers = event.servers();
        if (event instanceof BroadcastEvent) {
            // 做广播
            publishKafkaEvent(event, topic, sharedKey);
        } else if (ArrayUtils.isEmpty(servers)) {
            // 发给自己
            publishKafkaEvent(event, topic, uniqueKey);
        } else {
            // 发给指定服务
            for (String server : servers) {
                publishKafkaEvent(event, topic, String.format(uniqueKeyTemplate, server));
            }
        }
    }

    /**
     * 发布Kafka事件
     */
    protected void publishKafkaEvent(Event event, String topic, String key) {
        try {
            String typeId = event.getClass().getName();
            String content = JsonUtil.toJsonString(event);
            kafkaProducer.sendMessage(topic, key, content, Map.of(TYPE_ID, typeId));
        } catch (Exception e) {
            throw new RuntimeException(String.format("发布事件到Kafka失败 topic: %s key: %s", topic, key), e);
        }
    }

    @SneakyThrows
    @KafkaListener(topics = TOPIC_NAME_EL, groupId = GROUP_NAME_EL)
    protected void handleEvent(ConsumerRecord<String, String> record, Acknowledgment ack) {
        String key = record.key();
        if (StringUtils.equalsAny(key, uniqueKey, sharedKey)) {
            Header header = record.headers().lastHeader(TYPE_ID);

            if (Objects.isNull(header)) {
                log.error("类型ID丢失, 事件处理失效!!");
                ack.acknowledge();
                return;
            }

            String typeId = new String(header.value(), StandardCharsets.UTF_8);
            String content = record.value();

            Event event = (Event) JsonUtil.convertToObject(content, Class.forName(typeId));

            publishLocalEvent(event, false);
        }

        ack.acknowledge();
    }

    /**
     * @return
     */
    @Override
    public PublishStrategy support() {
        return PublishStrategy.KAFKA;
    }
}
