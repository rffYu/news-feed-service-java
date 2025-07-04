package common.dto;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import common.InfoUIDGenerator;
import lombok.*;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

class FlexibleLocalDateTimeDeserializer extends JsonDeserializer<LocalDateTime> {

    private static final DateTimeFormatter[] SUPPORTED_FORMATS = new DateTimeFormatter[] {
        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSSSS"),
        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"),
        DateTimeFormatter.ISO_LOCAL_DATE_TIME
    };

    @Override
    public LocalDateTime deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        String text = p.getText();
        for (DateTimeFormatter formatter : SUPPORTED_FORMATS) {
            try {
                return LocalDateTime.parse(text, formatter);
            } catch (Exception e) {
                // Try next formatter
            }
        }
        throw ctxt.weirdStringException(text, LocalDateTime.class, "Unsupported date format");

    }
}

@Data
@EqualsAndHashCode(callSuper = true)
public abstract class TimeSensitiveInformationDto extends InformationDto {

    @JsonProperty("dt")
    @JsonDeserialize(using = FlexibleLocalDateTimeDeserializer.class)
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

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    // generate info_id based on source, title and datetime
    protected String generateInfoUid() {
        String dtString = this.dt != null ? this.dt.format(FORMATTER) : "";
        return InfoUIDGenerator.generateInfoUID(this.getSource(), this.getTitle(), dtString);
    }
}
