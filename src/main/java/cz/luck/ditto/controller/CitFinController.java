package cz.luck.ditto.controller;

import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.module.*;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import cz.luck.ditto.model.citfin.*;
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
import java.time.*;
import java.time.format.*;
import java.util.*;

@RestController
public class CitFinController {

    private static final Logger log = LoggerFactory.getLogger(CitFinController.class);

    private static boolean flip = true;

    private final ObjectMapper objectMapper;

    public CitFinController() {
        this.objectMapper = new ObjectMapper();
        configureObjectMapper(this.objectMapper);
    }

    private void configureObjectMapper(ObjectMapper objectMapper) {
        // Add your custom configuration here
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.registerModule(new SimpleModule().addDeserializer(OffsetDateTime.class, new OffsetDateTimeDeserializer()));
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
    }

    @RequestMapping(
            value = "/cz/api/rest/test",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_XML_VALUE
    )
    public ResponseEntity<String> test() {

        String responseBody = DateTimeFormatter.ofPattern("yyyy-MM-dd").format(LocalDate.now().minusDays(1));

        if (flip) {
            responseBody += "+02:00";
        } else {
            responseBody += "-02:00";
        }
        flip = !flip;

        // Parse the string into OffsetDateTime
        OffsetDateTime offsetDateTime = OffsetDateTime.parse(responseBody, DateTimeFormatter.ISO_OFFSET_DATE.withResolverStyle(ResolverStyle.SMART));

        // Convert OffsetDateTime to Instant and then to Date
        Date date = Date.from(offsetDateTime.toInstant());

        return new ResponseEntity<>(responseBody, HttpStatus.OK);

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
        return produceResponseEntityBody("citfin.xml");
    }

    @RequestMapping(
            value = "/cz/api/rest/last-statement/{token}/statement.xml",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_XML_VALUE
    )
    public ResponseEntity<String> statementLast(
            @PathVariable("token") String token // identifies one account with one currency
    ) {
        return produceResponseEntityBody("citfin.xml");
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
        return produceResponseEntityBody("citfin.xml");
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
        return produceResponseEntityBody("citfin.xml");
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
        return produceResponseEntityBody("citfin.xml");
    }


    @RequestMapping(
            value = "/cz/api/rest/periods/{token}/{from}/{until}/transactions.{format}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_XML_VALUE
    )
    public ResponseEntity<String> getPeriodsTransactions(
            @PathVariable("token") String token,
            @PathVariable("from") String from,  // Example: 2024-10-09
            @PathVariable("until") String until, // Example: 2024-10-09
            @PathVariable("format") String format // Example: xml, json
    ) throws Exception {
        if (Objects.equals(format, "xml")) {
            return produceResponseEntityBody("periods_10-29.xml");
        } else if (Objects.equals(format, "json")) {
            return produceResponseEntityBody("periods_10-29.json");
        }
        throw new Exception("Format parameter has to be  'xml' or 'json'");
    }


    // PRIVATE
    private ResponseEntity<String> produceResponseEntityBody(String fileName) {
        try {
            ClassPathResource resource = new ClassPathResource(fileName);
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
