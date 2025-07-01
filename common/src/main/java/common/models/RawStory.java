package common.models;

import lombok.*;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class RawStory extends NotTimeSensitiveInformation {

    private LocalDateTime dt;
    private List<String> cat;

    public RawStory(String title, String infoId, String content,
                    String source, LocalDateTime dt, String link, List<String> cat) {
        super(title, infoId, content, source, link);
        this.dt = dt != null ? dt : LocalDateTime.now();
        this.cat = cat;
        super.setInfoType("story");
    }
}
