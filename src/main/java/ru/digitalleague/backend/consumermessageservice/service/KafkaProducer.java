package ru.digitalleague.backend.consumermessageservice.service;

import com.google.gson.Gson;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;
import ru.digitalleague.backend.consumermessageservice.model.Answer;

import java.util.concurrent.ExecutionException;

@Service
public class KafkaProducer {

    private Logger LOG = LogManager.getLogger(KafkaProducer.class);
    @Value(value = "${topicsOut}")
    private String TOPIC;
    private Gson gson;

    private  KafkaTemplate<String, String> template;
    @Autowired(required = false)
    public KafkaProducer(KafkaTemplate<String, String> template,Gson gson) {
        this.template = template;
        this.gson = gson;
    }


    public String sendMessages(Answer answer) throws ExecutionException, InterruptedException {
        String answerToJson = gson.toJson(answer);
        ListenableFuture<SendResult<String, String>> answer1 = this.template.send(new ProducerRecord<String, String>(TOPIC,0,"answer", answerToJson));
        LOG.info("Method sendMessages sending answer in topic={} with value={} and future={}",TOPIC,answerToJson,answer1.get().getProducerRecord());
        return "OK";
    }
}
