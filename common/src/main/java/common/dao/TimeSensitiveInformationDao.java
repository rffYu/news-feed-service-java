package common.dao;

import common.InfoUIDGenerator;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.Getter;
import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public abstract class TimeSensitiveInformationDao extends InformationDao {

    @Getter
    @JsonProperty("dt")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime dt = LocalDateTime.now();

    public TimeSensitiveInformationDao(String infoType, String title, String infoId, String content, String source, LocalDateTime dt, String link, List<String> cat) {
        this.setInfoType(infoType);
        this.setTitle(title);
        this.setInfoId(infoId);
        this.setContent(content);
        this.setSource(source);
        // Set the datetime, defaulting to now if null
        this.setDt(dt != null ? dt : LocalDateTime.now());
        this.setLink(link);
        this.setCat(cat != null ? cat : List.of());
    }
}
