package resreq.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class CreatedResponse {

    @JsonProperty("id")
    private String id;

    @JsonProperty("createdAt")
    private  String createdAt;
}
