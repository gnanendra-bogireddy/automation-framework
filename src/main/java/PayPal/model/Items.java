package PayPal.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class Items {

    @JsonProperty("name")
    public String name;

    @JsonProperty("description")
    public String description;

    @JsonProperty("quantity")
    public String quantity;

    @JsonProperty("unit_amount")
    public UnitAmount unitAmount;

}
