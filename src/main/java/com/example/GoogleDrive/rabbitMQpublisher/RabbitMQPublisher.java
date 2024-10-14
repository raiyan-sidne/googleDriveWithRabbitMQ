package com.example.GoogleDrive.rabbitMQpublisher;

import com.example.GoogleDrive.dto.FileObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * @author hasin.raiyan
 * @since 10/22/23
 */
@Service
public class RabbitMQPublisher {

    private static final Logger LOGGER = LoggerFactory.getLogger(RabbitMQPublisher.class);

    @Value("${rabbitmq.upload.exchange.name}")
    private String uploadExchange;

    @Value("${rabbitmq.notify.exchange.name}")
    private String notifyExchange;

    @Value("${rabbitmq.upload.routing.key}")
    private String uploadRoutingKey;

    @Value("${rabbitmq.notify.routing.key}")
    private String notifyRoutingKey;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    public void publishMessageToNotify(FileObject fileObject) {
        rabbitTemplate.convertAndSend(notifyExchange, notifyRoutingKey, fileObject);
    }

    public void sendingTestUploadCommand(FileObject fileObject) {
        rabbitTemplate.convertAndSend(uploadExchange, uploadRoutingKey, fileObject);
    }
}
