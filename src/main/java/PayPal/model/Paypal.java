package PayPal.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class Paypal {

    @JsonProperty
    private Name name;

    @JsonProperty("email_address")
    private String emailAddress;

    @JsonProperty("experience_context")
    private ExperienceContext experienceContext;

}
