package PayPal.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class PatchRequestItem{

	@JsonProperty
	private String op;

	@JsonProperty
	private String path;

	@JsonProperty
	private Value value ;

}