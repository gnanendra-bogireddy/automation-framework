package PayPal.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class UnitAmount {

    @JsonProperty("currency_code")
    public  String currencyCode;

    @JsonProperty("value")
    public  String value;

}
