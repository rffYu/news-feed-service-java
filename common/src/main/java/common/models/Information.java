package common.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public abstract class Information {

    @JsonProperty("info_type")
    protected String infoType;

    @JsonProperty("info_id")
    protected String infoId;

    @JsonProperty("title")
    protected String title;

    @JsonProperty("content")
    protected String content = "";
    
    @JsonProperty("source")
    protected String source = "";

    @JsonProperty("link")
    protected String link = "";
}
