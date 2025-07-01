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
    private String infoType;

    @JsonProperty("info_id")
    private String infoId;

    @JsonProperty("title")
    private String title;

    @JsonProperty("content")
    private String content;
}
