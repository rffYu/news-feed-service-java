package common.dto;

import common.InfoUIDGenerator;
import lombok.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public abstract class TimeSensitiveInformationDto extends InformationDto {
    private static final String DT_PATTERN = "yyyy-MM-dd HH:mm:ss";

    @Getter
    @JsonProperty("dt")
    @JsonFormat(pattern = DT_PATTERN)
    protected LocalDateTime dt = LocalDateTime.now();

    public TimeSensitiveInformationDto(String infoType, String title, String infoId, String content,
                                       String source, LocalDateTime dt, String link, List<String> cat) {
        this.setInfoType(infoType);
        this.setTitle(title);
        this.setInfoId(infoId != null && !infoId.isEmpty() ? infoId : generateInfoUid());
        this.setContent(content);
        this.setSource(source);
        // Set the datetime, defaulting to now if null
        this.setDt(dt != null ? dt : LocalDateTime.now());
        this.setLink(link);
        this.setCat(cat != null ? cat : List.of());
    }

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern(DT_PATTERN);

    // generate info_id based on source, title and datetime
    protected String generateInfoUid() {
        String dtString = this.dt != null ? this.dt.format(FORMATTER) : "";
        return InfoUIDGenerator.generateInfoUID(this.getSource(), this.getTitle(), dtString);
    }
}
