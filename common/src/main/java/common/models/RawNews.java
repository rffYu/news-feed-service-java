package common.models;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class RawNews extends TimeSensitiveInformation {

    private List<String> cat;

    public RawNews(String title, String infoId, String content,
                   String source, LocalDateTime dt, String link, List<String> cat) {
        super(title, infoId, content, source, dt, link);
        this.cat = cat;
        super.setInfoType("news");
    }
}
