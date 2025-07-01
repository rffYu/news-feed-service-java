package common.models;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public abstract class NotTimeSensitiveInformation extends Information {

    public NotTimeSensitiveInformation(String title, String infoId, String content,
                                       String source, String link) {
        super();
        this.title = title;
        this.infoId = infoId;
        this.content = content;
        this.source = source;
        this.link = link;

        if (this.infoId == null || this.infoId.isEmpty()) {
            this.infoId = generateInfoUid();
        }
    }

    // generate info_id based on source, title and link
    protected String generateInfoUid() {
        return InfoUIDGenerator.generateInfoUID(this.source, this.title, this.link);
    }
}
