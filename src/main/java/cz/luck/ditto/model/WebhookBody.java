package cz.luck.ditto.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class WebhookBody {

    private String address;
    private String amount;
    private String asset;
    @JsonProperty("blockNumber")
    private int blockNumber;
    @JsonProperty("counterAddress")
    private String counterAddress;
    @JsonProperty("txId")
    private String txId;
    private String type;
    private String chain;
    @JsonProperty("subscriptionType")
    private String subscriptionType;

}