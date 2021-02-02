package com.heima.admin.kafka.listener;

import com.heima.admin.service.ArticleDeleteService;
import com.heima.common.message.NewsAutoScanConstants;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * @author cuichacha
 * @date 2/2/21 14:51
 */
@Component
public class ArticleDeleteListener {

    @Autowired
    private ArticleDeleteService articleDeleteService;

    @KafkaListener(topics = NewsAutoScanConstants.WM_NEWS_DELETE_TOPIC)
    public void deleteArticle(ConsumerRecord<?, ?> record) {
        Optional<? extends ConsumerRecord<?, ?>> optional = Optional.ofNullable(record);
        if (optional.isPresent()) {
            Object value = record.value();
            articleDeleteService.deleteArticles(Long.valueOf((String) value));
        }
    }
}
