package common.models;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
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

    @JsonProperty("cat")
    @JsonDeserialize(using = StringListDeserializer.class)
    protected List<String> cat = new ArrayList<>();
}
