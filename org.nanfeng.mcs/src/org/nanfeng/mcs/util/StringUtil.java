package org.nanfeng.mcs.util;

/**
 * Static utility methods for manipulating string
 */
public class StringUtil {
    /**
     * <code>NEW_LINE</code>, system new line
     */
    public static final String NEW_LINE = System.getProperty("line.separator");

    /**
     * Check if the given string is null or "" and "   "
     * 
     * @param str
     * @return true if it is empty, otherwise false
     */
    public static boolean isEmpty(String str) {
        return (str == null) || str.trim().isEmpty();
    }
}
