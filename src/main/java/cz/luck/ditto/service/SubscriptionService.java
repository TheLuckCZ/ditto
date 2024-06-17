package cz.luck.ditto.service;

import cz.luck.ditto.controller.MainController;
import cz.luck.ditto.model.WebhookBody;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import java.net.ConnectException;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

@Service
public class SubscriptionService {

    private static final Logger log = LoggerFactory.getLogger(SubscriptionService.class);

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

    @Autowired
    private RestTemplate restTemplate;

    private final Map<String, String> addressWebhooksMap = new HashMap<>();

    public void addWebhook(String address, String webhookUrl) {
        addressWebhooksMap.put(address, webhookUrl);
    }

    @Scheduled(fixedRate = 10000)
    public void sendWebhooks() {
        addressWebhooksMap.forEach(
                (key, value) -> {
                    System.out.println(LocalDateTime.now().format(formatter) +
                            " - TATUM transaction webhook notification ('" + value + "') - ADDRESS: '" + key + "'");

                    fireWebhook(key, value);
                }
        );
    }

    private void fireWebhook(String walletAddress, String webhookUrl) {
        WebhookBody requestBody = generateWebhookBody(walletAddress);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<WebhookBody> request = new HttpEntity<>(requestBody, headers);

        log.info("Firing webhook : {}", request);

        // Call the API endpoint
        try {
            restTemplate.exchange(
                    webhookUrl, // URL from the addressWebhooksMap
                    HttpMethod.POST, // or another HTTP method like POST
                    request, // request entity, can contain headers and body
                    String.class);
        } catch (ResourceAccessException e) {
            log.error(e.getLocalizedMessage());
        }

    }

    private WebhookBody generateWebhookBody(String walletAddress) {
        WebhookBody webhookBody = new WebhookBody();
        webhookBody.setAddress(walletAddress);
        webhookBody.setAmount("0.001");
        webhookBody.setAsset("ETH");
        webhookBody.setBlockNumber((int) LocalDateTime.now().toEpochSecond(ZoneOffset.UTC));
        webhookBody.setCounterAddress("0x690B9A9E9aa1C9dB991C7721a92d351Db4FaC990");
        webhookBody.setTxId("0x062d236ccc044f68194a04008e98c3823271dc26160a4db9ae9303f9ecfc7bf6");
        webhookBody.setType("native");
        webhookBody.setChain("ethereum-testnet");
        webhookBody.setSubscriptionType("ADDRESS_EVENT");

        return webhookBody;
    }



}
