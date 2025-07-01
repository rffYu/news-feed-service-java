package common.models;

import lombok.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

@Data
@NoArgsConstructor
public abstract class TimeSensitiveInformation extends Information {

    @Getter
    @JsonProperty("dt")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    protected LocalDateTime dt = LocalDateTime.now();

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
            this.infoId = generateInfoUid();
        }
    }

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    // generate info_id based on source, title and datetime
    protected String generateInfoUid() {
        String dtString = this.dt != null ? this.dt.format(FORMATTER) : "";
        return InfoUIDGenerator.generateInfoUID(this.source, this.title, dtString);
    }
}
