package PayPal.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

import java.util.List;

@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class PurchaseUnits {

    @JsonProperty("items")
    List<Items> itemsList;

    @JsonProperty("amount")
    Amount amount;

}
