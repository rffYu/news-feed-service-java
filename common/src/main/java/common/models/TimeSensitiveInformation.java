package common.models;

import lombok.*;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public abstract class TimeSensitiveInformation extends Information {

    private String title;
    private String infoId;
    private String content = "";
    private String source = "";
    private LocalDateTime dt = LocalDateTime.now();
    private String link = "";

    public TimeSensitiveInformation(String title, String infoId, String content,
                                    String source, LocalDateTime dt, String link) {
        super();
        this.title = title;
        this.infoId = infoId;
        this.content = content;
        this.source = source;
        this.dt = dt != null ? dt : LocalDateTime.now();
        this.link = link;

        if (this.infoId == null || this.infoId.isEmpty()) {
            this.infoId = generateInfoUid(this.source, this.title, this.dt);
        }
    }

    // Implement your info_id generator here, based on source, title and datetime
    protected static String generateInfoUid(String source, String title, LocalDateTime dt) {
        // TODO: Implement the UID generation logic
        return source + "-" + title + "-" + dt.toString();
    }
}

