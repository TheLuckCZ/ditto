package cz.luck.ditto.controller;

import cz.luck.ditto.model.CreateSubscriptionNotification;
import cz.luck.ditto.service.SubscriptionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MainController {

    @Autowired
    private SubscriptionService subService;

    private static final Logger log = LoggerFactory.getLogger(MainController.class);

    @GetMapping("/test")
    public String testEndpoint() {
        return "Test endpoint is working!";
    }

    @PostMapping("/subscription")
    public ResponseEntity<String> createSubscription(@RequestBody CreateSubscriptionNotification notification) {
        log.info("Received subscription: {}", notification);
        subService.addWebhook(notification.getAttr().getAddress(), notification.getAttr().getUrl());
        return ResponseEntity.ok("Subscription created successfully");
    }

}