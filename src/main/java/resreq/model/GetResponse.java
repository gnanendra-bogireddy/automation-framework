package resreq.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class GetResponse {

    @JsonProperty("data")
    private GetData data;

    @JsonProperty("support")
    private Support support;
}
