package PayPal.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.cucumber.core.internal.com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Builder;

@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class ItemTotal {

    @JsonProperty("currency_code")
    public  String currencyCode;

    @JsonProperty("value")
    public  String value;
}
