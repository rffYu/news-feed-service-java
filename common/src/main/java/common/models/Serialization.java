package common.models;

import java.util.Map;
import java.util.HashMap;

public class Serialization {

    private static final Map<String, Class<? extends Information>> infoTypeMap = new HashMap<>();

    static {
        infoTypeMap.put("news", RawNews.class);
        infoTypeMap.put("story", RawStory.class);
    }

    public static Class<? extends Information> getDataTypeClass(String infoType) {
        return infoTypeMap.get(infoType);
    }
}
