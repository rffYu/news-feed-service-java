package common.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import java.time.LocalDateTime;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
public class RawNews extends TimeSensitiveInformationDto {

    private List<String> cat;

    public RawNews(String title, String infoId, String content,
                   String source, LocalDateTime dt, String link, List<String> cat) {
        super("news", title, infoId, content, source, dt, link, cat);
    }
}
