package se.fredin.llama.jobengine.task.join;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JoinUtils {

    public static final byte EXCHANGE_MAIN = 0;
    public static final byte EXCHANGE_JOINING = 1;

    public static Map<String, List<Map<String, String>>> groupCollection(List<JoinKey> joinKeys, byte exchange, List<Map<String, String>> list) {
        Map<String, List<Map<String, String>>> map = new HashMap<>();
        for (Map<String, String> m : list) {

            // First set the key
            String key = "";
            for (JoinKey joinKey : joinKeys) {
                key += m.get(exchange == EXCHANGE_MAIN ? joinKey.getKeyInMain() : joinKey.getKeyInJoining());
            }

            // Now group
            List<Map<String, String>> value = map.get(key);
            if (value == null) {
                value = new ArrayList<>();
            }

            // Add new entry
            value.add(m);

            // Update map
            map.put(key, value);
        }

        return map;
    }

    public static String keysAsString(List<JoinKey> joinKeys, byte exchange) {
        String key = "";
        for (JoinKey joinKey : joinKeys) {
            key += exchange == JoinUtils.EXCHANGE_MAIN ? joinKey.getKeyInMain() : joinKey.getKeyInJoining();
        }
        return key;
    }
}
