package org.nanfeng.mcs.util;

/**
 * Static utility methods for manipulating object
 */
public class ObjectUtil {
    /**
     * Check if the given Object is null
     * 
     * @param obj
     * @return true if it is null, otherwise false
     */
    public static boolean isNull(Object obj) {
        return obj == null;
    }

    /**
     * Check if the given Object is not null
     * 
     * @param obj
     * @return true if it is not null, otherwise false
     */
    public static boolean isNotNull(Object obj) {
        return !isNull(obj);
    }
}
