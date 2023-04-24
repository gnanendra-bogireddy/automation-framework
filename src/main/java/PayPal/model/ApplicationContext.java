package PayPal.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class ApplicationContext {

    @JsonProperty("return_url")
    public String returnUrl;

    @JsonProperty("cancel_url")
    public String cancelUrl;
}
