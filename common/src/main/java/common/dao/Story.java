package common.dao;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Story extends NotTimeSensitiveInformationDao {

    public Story(String title, String infoId, String content,
                String source, String link, List<String> cat) {
        super("story", title, infoId, content, source, link, cat);
    }
}
