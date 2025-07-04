package common.dao;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public abstract class NotTimeSensitiveInformationDao extends InformationDao {

    public NotTimeSensitiveInformationDao(String infoType, String title, String infoId, String content,
                                          String source, String link, List<String> cat) {
        this.setInfoType(infoType);
        this.setTitle(title);
        this.setInfoId(infoId);
        this.setContent(content);
        this.setSource(source);
        this.setLink(link);
        this.setCat(cat != null ? cat : List.of());
    }
}
