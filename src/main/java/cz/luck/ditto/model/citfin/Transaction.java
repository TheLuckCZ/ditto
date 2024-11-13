package cz.luck.ditto.model.citfin;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.*;

@Data
public class Transaction {

    @JsonProperty("column0")
    private OffsetDateTime date;

    @JsonProperty("column1")
    private BigDecimal amount;

    @JsonProperty("column2")
    private String counterAccount;

    @JsonProperty("column3")
    private String bankCode;

    @JsonProperty("column5")
    private String variableSymbol;

    @JsonProperty("column8")
    private String type;

    @JsonProperty("column14")
    private String currency;

    @JsonProperty("column16")
    private String message;

    @JsonProperty("column22")
    private String transactionId;


}