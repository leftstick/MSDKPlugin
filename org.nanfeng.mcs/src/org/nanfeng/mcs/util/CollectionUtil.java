package org.nanfeng.mcs.util;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Static utility methods for manipulating Collection
 */
public class CollectionUtil {
    /**
     * Check if the given Collection is null or empty
     * 
     * @param col
     * @return true if it is empty or null, otherwise false
     */
    public static boolean isEmpty(Collection<?> col) {
        return (col == null) || col.isEmpty();
    }

    /**
     * Check if the given Map is null or empty
     * 
     * @param map
     * @return true if it is empty or null, otherwise false
     */
    public static boolean isEmpty(Map<?, ?> map) {
        return (map == null) || map.isEmpty();
    }

    /**
     * Check if the given Map is not null and not empty
     * 
     * @param map
     * @return true if it is not empty and not null, otherwise false
     */
    public static boolean isNotEmpty(Map<String, Object> map) {
        return !isEmpty(map);
    }

    /**
     * Check if the given Collection is not null and not empty
     * 
     * @param col
     * @return true if it is not empty and not null, otherwise false
     */
    public static boolean isNotEmpty(List<String> col) {
        return !isEmpty(col);
    }
}
