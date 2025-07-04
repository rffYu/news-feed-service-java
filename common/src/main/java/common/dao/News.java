package common.dao;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class News extends TimeSensitiveInformationDao {

    public News(String title, String infoId, String content,
                String source, LocalDateTime dt, String link, List<String> cat) {
        super("news", title, infoId, content, source, dt, link, cat);
    }
}
