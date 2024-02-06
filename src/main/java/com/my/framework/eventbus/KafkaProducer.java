package com.my.framework.eventbus;

import com.my.framework.JsonUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.kafka.core.KafkaOperations;
import org.springframework.kafka.core.KafkaTemplate;

import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 * @author <a href="mailto:yu.ou@alpha-ess.com">ouyu</a>
 * 2024/1/18 11:40
 */
@Slf4j
//@Component
@RequiredArgsConstructor
public class KafkaProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;

    public void asyncSendMessage(String topic, Object message) {
        asyncSendMessage(topic, null, message, null);
    }

    public void asyncSendMessage(String topic, Object message, Map<String, String> headers) {
        asyncSendMessage(topic, null, message, headers);
    }

    public void asyncSendMessage(String topic, String key, Object message) {
        asyncSendMessage(topic, key, message, null);
    }

    public void asyncSendMessage(String topic, String key, Object message, Map<String, String> headers) {
        String obj2String = JsonUtil.toJsonString(message);
        log.debug("Preparing to send message to Topic {}: {}", topic, obj2String);
        ProducerRecord<String, String> producerRecord = new ProducerRecord<>(topic, key, obj2String);
        if (ObjectUtils.isNotEmpty(headers)) {
            headers.forEach((name, value) -> producerRecord.headers().add(name, value.getBytes(StandardCharsets.UTF_8)));
        }
        this.kafkaTemplate.send(producerRecord).completable().whenCompleteAsync((sendResult,throwable)->{
            if (ObjectUtils.isNotEmpty(throwable)) {
                log.error("Sending message to Topic {} failed: ", topic, throwable);
            }else{
                log.debug("Successfully sent message to topic {}, result: {}", topic, sendResult);
            }
        });
    }

    public void sendMessage(String topic, Object message) {
        sendMessage(topic, null, message, null);
    }

    public void sendMessage(String topic, Object message, Map<String, String> headers) {
        sendMessage(topic, null, message, headers);
    }

    public void sendMessage(String topic, String key, Object message) {
        sendMessage(topic, key, message, null);
    }

    public void sendMessage(String topic, String key, Object message, Map<String, String> headers) {
        String obj2String = JsonUtil.toJsonString(message);
        log.debug("Preparing to send message to Topic {}: {}", topic, obj2String);
        ProducerRecord<String, String> producerRecord = new ProducerRecord<>(topic, obj2String);
        if (ObjectUtils.isNotEmpty(headers)) {
            headers.forEach((name, value) -> producerRecord.headers().add(name, value.getBytes(StandardCharsets.UTF_8)));
        }
        this.kafkaTemplate.send(producerRecord).completable().join();
    }

    public <T> T executeInTransaction(KafkaOperations.OperationsCallback<String, String, T> callback) {
        return this.kafkaTemplate.executeInTransaction(callback);
    }

}
