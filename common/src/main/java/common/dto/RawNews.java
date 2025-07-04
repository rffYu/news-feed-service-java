package common.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.EqualsAndHashCode;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class RawNews extends TimeSensitiveInformationDto {

    public RawNews(String title, String infoId, String content,
                   String source, LocalDateTime dt, String link, List<String> cat) {
        super("news", title, infoId, content, source, dt, link, cat);
    }
}
