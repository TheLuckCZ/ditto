package cz.luck.ditto.controller;

import cz.luck.ditto.config.RequestLoggingFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@RestController
public class CitFinController {

    private static final Logger log = LoggerFactory.getLogger(CitFinController.class);

    @RequestMapping(
            value = "/cz/api/rest/test",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_XML_VALUE
    )
    public ResponseEntity<String> test(
    ) {
        return produceXmlResponseEntity();
    }

    // GET - Statements
    @RequestMapping(
            value = "/cz/api/rest/by-id/{token}/{year}/{order_id}/statement.xml",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_XML_VALUE
    )
    public ResponseEntity<String> statementById(
            @PathVariable("token") String token,
            @PathVariable("year") int year,
            @PathVariable("order_id") int orderId
    ) {
        String params = "By Id statement: token=" + token + ", year=" + year + ", order_id=" + orderId;
        log.debug(params);
        return produceXmlResponseEntity();
    }

    @RequestMapping(
            value = "/cz/api/rest/last-statement/{token}/statement.xml",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_XML_VALUE
    )
    public ResponseEntity<String> statementLast(
            @PathVariable("token") String token // identifies one account with one currency
    ) {
        return produceXmlResponseEntity();
    }

    // PUT - Tokens
    @RequestMapping(
            value = "/cz/api/rest/set-last-id/{token}/{transaction_id}.xml",
            method = RequestMethod.PUT,
            produces = MediaType.APPLICATION_XML_VALUE
    )
    public ResponseEntity<String> setLastId(
            @PathVariable("token") String token,
            @PathVariable("transaction_id") int transactionId
    ) {
        return produceXmlResponseEntity();
    }

    @RequestMapping(
            value = "/cz/api/rest/set-last-date/{token}/{date}.xml",
            method = RequestMethod.PUT,
            produces = MediaType.APPLICATION_XML_VALUE
    )
    public ResponseEntity<String> setLastDate(
            @PathVariable("token") String token,
            @PathVariable("date") String date // Example: 2024-10-09
    ) {
        return produceXmlResponseEntity();
    }

    // GET - Transactions
    @RequestMapping(
            value = "/cz/api/rest/last/{token}/transactions.xml",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_XML_VALUE
    )
    public ResponseEntity<String> getLastTransactions(
            @PathVariable("token") String token
    ) {
        return produceXmlResponseEntity();
    }


    @RequestMapping(
            value = "/cz/api/rest/periods/{token}/{from}/{until}/transactions.xml",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_XML_VALUE
    )
    public ResponseEntity<String> getPeriodsTransactions(
            @PathVariable("token") String token,
            @PathVariable("from") String from,  // Example: 2024-10-09
            @PathVariable("until") String until // Example: 2024-10-09
    ) {
        return produceXmlResponseEntity();
    }


    // PRIVATE
    private ResponseEntity<String> produceXmlResponseEntity() {
        try {
            ClassPathResource resource = new ClassPathResource("citfin.xml");
            Path path = resource.getFile().toPath();
            String content = Files.readString(path);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_XML);

            return new ResponseEntity<>(content, headers, HttpStatus.OK);
        } catch (IOException e) {
            return new ResponseEntity<>("File not found", HttpStatus.NOT_FOUND);
        }
    }

}
