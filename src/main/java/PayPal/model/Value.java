package PayPal.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class Value{

	@JsonProperty("country_code")
	private String countryCode;
	@JsonProperty("admin_area_1")
	private String adminArea1;
	@JsonProperty("address_line_1")
	private String addressLine1;
	@JsonProperty("admin_area_2")
	private String adminArea2;
	@JsonProperty("address_line_2")
	private String addressLine2;
	@JsonProperty("postal_code")
	private String postalCode;
}