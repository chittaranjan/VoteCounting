import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class Util {


    public static <K,V> Map<K,V> of(K k1, V v1,
                                    K k2, V v2,
                                    K k3, V v3,
                                    K k4, V v4) {
        Map<K, V> newMap = new HashMap<>();
        newMap.put(k1, v1);
        newMap.put(k2, v2);
        newMap.put(k3, v3);
        newMap.put(k4, v4);
        return Collections.unmodifiableMap(newMap);
    }

    public static <K,V> Map<K,V> of(K k1, V v1,
                                    K k2, V v2,
                                    K k3, V v3) {
        Map<K, V> newMap = new HashMap<>();
        newMap.put(k1, v1);
        newMap.put(k2, v2);
        newMap.put(k3, v3);
        return Collections.unmodifiableMap(newMap);
    }

    public static <K,V> Map<K,V> of(K k1, V v1,
                                    K k2, V v2) {
        Map<K, V> newMap = new HashMap<>();
        newMap.put(k1, v1);
        newMap.put(k2, v2);
        return Collections.unmodifiableMap(newMap);
    }

    public static <K,V> Map<K,V> of(K k1, V v1) {
        Map<K, V> newMap = new HashMap<>();
        newMap.put(k1, v1);
        return Collections.unmodifiableMap(newMap);
    }

}
