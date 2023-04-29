package PayPal.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Builder;

@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class Value{

	private String countryCode;
	private String adminArea1;
	private String addressLine1;
	private String adminArea2;
	private String addressLine2;
	private String postalCode;
}