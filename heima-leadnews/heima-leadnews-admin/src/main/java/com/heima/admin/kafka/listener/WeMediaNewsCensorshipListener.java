package com.heima.admin.kafka.listener;

import com.heima.admin.feign.WeMediaFeign;
import com.heima.admin.service.WmNewsCensorshipService;
import com.heima.common.message.NewsAutoScanConstants;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * @author cuichacha
 * @date 1/31/21 13:37
 */
@Component
public class WeMediaNewsCensorshipListener {

    @Autowired
    private WmNewsCensorshipService wmNewsCensorshipService;

    @KafkaListener(topics = NewsAutoScanConstants.WM_NEWS_AUTO_SCAN_TOPIC)
    public void receiveMessage(ConsumerRecord<?, ?> record) {
        Optional<? extends ConsumerRecord<?, ?>> optional = Optional.ofNullable(record);
        if (optional.isPresent()) {
            Object value = record.value();
            wmNewsCensorshipService.censorByWmNewsId(Integer.valueOf((String) value));
        }
    }
}
