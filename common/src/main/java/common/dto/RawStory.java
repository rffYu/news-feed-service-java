package common.dto;

import lombok.*;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
public class RawStory extends NotTimeSensitiveInformationDto {

    public RawStory(String title, String infoId, String content,
                    String source, String link, List<String> cat) {
        super("story", title, infoId, content, source, link, cat);
    }
}
