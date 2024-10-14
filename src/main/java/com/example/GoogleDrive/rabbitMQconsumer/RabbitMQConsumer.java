package com.example.GoogleDrive.rabbitMQconsumer;

import com.example.GoogleDrive.controller.MessageController;
import com.example.GoogleDrive.dto.FileObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.GeneralSecurityException;

/**
 * @author hasin.raiyan
 * @since 9/18/23
 */
@Service
public class RabbitMQConsumer {

    private static final Logger LOGGER = LoggerFactory.getLogger(RabbitMQConsumer.class);

    @Autowired
    private MessageController messageController;

    @RabbitListener(queues = {"${rabbitmq.upload.queue.name}"})
    public void consumeMessage(FileObject fileObject) throws GeneralSecurityException, IOException {
        System.out.println("INSIDE CONSUMER: " + fileObject);
        LOGGER.info(String.format("Received  object -> %s", fileObject.toString()));

        messageController.uploadFileAndNotify(fileObject);
    }
}
