package common.models;

import lombok.*;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public abstract class NotTimeSensitiveInformation extends Information {

    private String title;
    private String infoId;
    private String content = "";
    private String source = "";
    private String link = "";

    public NotTimeSensitiveInformation(String title, String infoId, String content,
                                       String source, String link) {
        super();
        this.title = title;
        this.infoId = infoId;
        this.content = content;
        this.source = source;
        this.link = link;

        if (this.infoId == null || this.infoId.isEmpty()) {
            this.infoId = generateInfoUid(this.source, this.title, null);
        }
    }

    protected static String generateInfoUid(String source, String title, LocalDateTime dt) {
        // TODO: Implement the UID generation logic for not time sensitive info
        return source + "-" + title;
    }
}

