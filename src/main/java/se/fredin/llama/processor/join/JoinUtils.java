package se.fredin.llama.processor.join;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JoinUtils {

    public static final byte EXCHANGE_MAIN = 0;
    public static final byte EXCHANGE_JOINING = 1;

    public static Map<String, List<Map<String, String>>> groupCollection(List<JoinKey> joinKeys, byte exchange, List<Map<String, String>> list) {
        var map = new HashMap<String, List<Map<String, String>>>();
        for (var m : list) {

            // First set the key
            var keyBuilder = new StringBuilder();
            for (var joinKey : joinKeys) {
                keyBuilder.append(m.get(exchange == EXCHANGE_MAIN ? joinKey.getKeyInMain() : joinKey.getKeyInJoining()));
            }
            var key = keyBuilder.toString();

            // Now group
            var value = map.get(key);
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
        var keyBuilder = new StringBuilder();
        for (var joinKey : joinKeys) {
            keyBuilder.append(exchange == JoinUtils.EXCHANGE_MAIN ? joinKey.getKeyInMain() : joinKey.getKeyInJoining());
        }
        return keyBuilder.toString();
    }
}