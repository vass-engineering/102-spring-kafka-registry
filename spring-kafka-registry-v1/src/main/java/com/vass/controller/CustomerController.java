package com.vass.controller;

import com.vass.model.Customer;
import com.vass.producer.Producer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Slf4j
public class CustomerController {

    private final Producer producer;

    @PostMapping("/customers")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void sendCustomer(@RequestBody Customer customer) {
        log.info("Sending customer request: " + customer.toString());
        producer.send(customer);
    }
}
