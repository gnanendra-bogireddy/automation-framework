package PayPal.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class Name {

    @JsonProperty("given_name")
    private String givenName;

    @JsonProperty("surname")
    private String surname;
}
