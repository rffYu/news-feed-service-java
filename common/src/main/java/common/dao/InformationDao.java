package common.dao;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME,
    include = JsonTypeInfo.As.PROPERTY,
    property = "info_type"
)
@JsonSubTypes({
    @JsonSubTypes.Type(value = News.class, name = "news"),
    @JsonSubTypes.Type(value = Story.class, name = "story")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
public abstract class InformationDao {

    @JsonProperty("info_type")
    private String infoType;

    @JsonProperty("info_id")
    private String infoId;

    @JsonProperty("title")
    private String title;

    @JsonProperty("content")
    private String content = "";

    @JsonProperty("source")
    private String source = "";

    @JsonProperty("link")
    private String link = "";

    @JsonProperty("cat")
    private List<String> cat = new ArrayList<>();
}
