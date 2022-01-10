package ru.digitalleague.backend.consumermessageservice.service;

import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import ru.digitalleague.backend.consumermessageservice.model.Answer;

@Service
public class KafkaProducer {
    private static final String TOPIC = "vn-project-out";
    private Gson gson;

    private  KafkaTemplate<String, String> template;
    @Autowired(required = false)
    public KafkaProducer(KafkaTemplate<String, String> template,Gson gson) {
        this.template = template;
        this.gson = gson;
    }


    public String sendMessages(Answer answer){
        String answerToJson = gson.toJson(answer);
        this.template.send(TOPIC,"answer",answerToJson);
        System.out.println(answer);
        return "OK";
    }
}
