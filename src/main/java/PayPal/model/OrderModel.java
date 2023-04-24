package PayPal.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

import java.util.List;

@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class OrderModel {

    @JsonProperty("intent")
    public String intent;

    @JsonProperty("purchase_units")
    List<PurchaseUnits> purchageUnitsList;

    @JsonProperty("application_context")
    public ApplicationContext applicationContext;
}
