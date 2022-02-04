package com.vass.producer.controller;

import com.vass.producer.service.ProducerService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class CustomerController {

    private final ProducerService producerService;

    public CustomerController(ProducerService producerService) {
        this.producerService = producerService;
    }

    @GetMapping("/generateAvro")
    public String generateNewPaymentAvro(@RequestParam(required = false) Integer count) throws InterruptedException {
        producerService.produceAvro(count);
        return "OK";
    }

    @GetMapping("/generateProto")
    public String generateNewPaymentProto(@RequestParam(required = false) Integer count) throws InterruptedException {
        producerService.produceProto(count);
        return "OK";
    }

    @GetMapping("/generateJson")
    public String generateNewPaymentJson(@RequestParam(required = false) Integer count) throws InterruptedException {
        producerService.produceJson(count);
        return "OK";
    }
}
