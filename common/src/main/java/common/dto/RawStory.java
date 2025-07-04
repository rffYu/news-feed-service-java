package common.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.EqualsAndHashCode;
import java.util.List;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class RawStory extends NotTimeSensitiveInformationDto {

    public RawStory(String title, String infoId, String content,
                    String source, String link, List<String> cat) {
        super("story", title, infoId, content, source, link, cat);
    }
}
