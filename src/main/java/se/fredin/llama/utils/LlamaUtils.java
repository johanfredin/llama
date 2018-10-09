package se.fredin.llama.utils;

import org.apache.camel.Exchange;
import org.apache.camel.model.dataformat.JacksonXMLDataFormat;
import se.fredin.llama.bean.LlamaBean;
import se.fredin.llama.processor.Field;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

public class LlamaUtils {

    public static boolean isTrueAny(boolean... conditions) {
        return isTrue(Operator.OR, conditions);
    }

    public static boolean isTrueAll(boolean... conditions) {
        return isTrue(Operator.AND, conditions);
    }

    public static boolean isTrue(Operator operator, boolean... conditions) {
        var trueStatementFound = false;
        for (var condition : conditions) {
            if (!condition) {
                if (operator == Operator.AND) {
                    return false;
                }
            }
            trueStatementFound = true;
        }

        if (operator == Operator.OR) {
            return trueStatementFound;
        }
        return true;
    }


    @SuppressWarnings("unchecked")
    public static <T extends LlamaBean> List<T> asLlamaBeanList(Exchange e) {
        return new ArrayList<T>(e.getIn().getBody(List.class));
    }

    @SuppressWarnings("unchecked")
    public static List<Map<String, String>> asListOfMaps(Exchange e) {
        return new ArrayList<Map<String, String>>(e.getIn().getBody(List.class));
    }

    @SuppressWarnings("unchecked")
    public static List<Map<String, String>> asLinkedListOfMaps(Exchange e) {
        return new LinkedList<Map<String, String>>(e.getIn().getBody(List.class));
    }

    @SuppressWarnings("unchecked")
    public static <T> List<T> asTypedList(Exchange e) {
        return new ArrayList<T>(e.getIn().getBody(List.class));
    }

    public static <K, V> Map<K, V>asTypedMap(Exchange e) {
        return new HashMap<K, V>(e.getIn().getBody(Map.class));
    }

    /**
     * Creates a map where key = {@link LlamaBean#getId()} and value a List of llama beans.
     *
     * @param e   the exchange that is assumed to have a list of {@link LlamaBean} instances
     * @param <T> the Type of the bean (must extend Llamabean
     * @return a map of llama beans grouped by bean id.
     */
    @SuppressWarnings("unchecked")
    public static <T extends LlamaBean> Map<Serializable, List<T>> asLlamaBeanMap(Exchange e) {
        List<T> list = asLlamaBeanList(e);
        return list
                .stream()
                .collect(Collectors.groupingBy(T::getId));
    }

    public static String getTransformedUrl(String inUrl, String inUrlPrefix, String outputUrlPrefix) {
        var url = inUrlPrefix.toLowerCase().replace("\\", "/");
        inUrl = inUrl.toLowerCase().replace("\\", "/");
        var outUrl = outputUrlPrefix.toLowerCase().replace("\\", "/");

        return inUrl.replace(url, outUrl);
    }

    public static JacksonXMLDataFormat toXml(boolean prettyPrint) {
        var dataFormat = new JacksonXMLDataFormat();
        dataFormat.setPrettyPrint(prettyPrint);
        return dataFormat;
    }


    @SafeVarargs
    public static <K, V> Map<K, V> getMergedMap(Map<K, V>... maps) {
        return getMergedMap(Arrays.asList(maps), false);
    }

    public static <K, V> Map<K, V> getMergedMap(Collection<Map<K, V>> maps, boolean overrideDuplicates) {
        var result = new HashMap<K, V>();
        for (var mapToAdd : maps) {
            if (overrideDuplicates) {
                mapToAdd.forEach((key, value) -> {
                    if (!result.containsKey(key)) {
                        result.put(key, value);
                    }
                });
            } else {
                result.putAll(mapToAdd);
            }
        }
        return result;
    }

    public static List<Field> fields(Field... fields) {
        return Arrays.asList(fields);
    }

    public static Field field(String name) {
        return field(name, name);
    }

    public static Field field(String name, String outName) {
        return new Field(name, outName);
    }

    public static boolean withinRange(Object o, int min, int max) {
        return Integer.parseInt(o.toString()) > min && Integer.parseInt(o.toString()) < max;
    }
}
