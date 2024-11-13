package cz.luck.ditto.model.citfin;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.*;

@Data
public class AccountInfo {

    @JsonProperty("accountId")
    private String accountId;

    @JsonProperty("bankId")
    private String bankId;

    @JsonProperty("currency")
    private String currency;

    @JsonProperty("iban")
    private String iban;

    @JsonProperty("bic")
    private String bic;

    @JsonProperty("openingBalance")
    private BigDecimal openingBalance;

    @JsonProperty("closingBalance")
    private BigDecimal closingBalance;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-ddXXX")
    @JsonProperty("dateStart")
    private OffsetDateTime dateStart;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-ddXXX")
    @JsonProperty("dateEnd")
    private OffsetDateTime dateEnd;

    @JsonProperty("yearList")
    private int yearList;

    @JsonProperty("idList")
    private int idList;

    @JsonProperty("idFrom")
    private int idFrom;

    @JsonProperty("idTo")
    private int idTo;

    @JsonProperty("idLastDownload")
    private int idLastDownload;

}
