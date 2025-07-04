package common.dao;

import lombok.*;
import java.time.LocalDateTime;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
public class News extends TimeSensitiveInformationDao {

    public News(String title, String infoId, String content,
                String source, LocalDateTime dt, String link, List<String> cat) {
        super("new", title, infoId, content, source, dt, link, cat);
    }
}
