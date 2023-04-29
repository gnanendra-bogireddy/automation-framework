package PayPal.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class ExperienceContext {

    @JsonProperty("payment_method_preference")
    private  String paymentMethodPreference;

    @JsonProperty("payment_method_selected")
    private  String paymentMethodSelected;

    @JsonProperty("brand_name")
    private  String brandName;

    @JsonProperty("locale")
    private  String locale;

    @JsonProperty("landing_page")
    private  String landingPage;

    @JsonProperty("shipping_preference")
    private  String shippingPreference;

    @JsonProperty("user_action")
    private  String userAction;

    @JsonProperty("return_url")
    private  String returnUrl;

    @JsonProperty("cancel_url")
    private  String cancelUrl;

}
