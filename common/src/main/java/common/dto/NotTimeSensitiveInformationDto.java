package common.dto;

import common.InfoUIDGenerator;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public abstract class NotTimeSensitiveInformationDto extends InformationDto {

    public NotTimeSensitiveInformationDto(String infoType, String title, String infoId, String content,
                                          String source, String link, List<String> cat) {
        this.setInfoType(infoType);
        this.setTitle(title);
        this.setInfoId(infoId != null && !infoId.isEmpty() ? infoId : generateInfoUid());
        this.setContent(content);
        this.setSource(source);
        this.setLink(link);
        this.setCat(cat != null ? cat : List.of());
    }

    // generate info_id based on source, title and link
    protected String generateInfoUid() {
        return InfoUIDGenerator.generateInfoUID(this.getSource(), this.getTitle(), this.getLink());
    }
}
