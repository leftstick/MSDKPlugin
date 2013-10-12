package org.nanfeng.mcs.js;

/**
 */
public enum ArgClassType {
    /**
     * <code>ARRAY</code>, indicates that this argument is an array
     */
    ARRAY("array"),
    /**
     * <code>STRING</code>, indicates that this argument is a string
     */
    STRING("string"),
    /**
     * <code>NUMBER</code>, indicates that this argument is a number
     */
    NUMBER("number"),

    /**
     * <code>OBJECT</code>, indicates that this argument is a object
     */
    OBJECT("object"),
    /**
     * <code>BOOLEAN</code>, indicates that this argument is a boolean
     */
    BOOLEAN("boolean");

    private String type;

    private ArgClassType(String type) {
        this.type = type;
    }

    /**
     * @return String value of this Type
     */
    public String getType() {
        return this.type;
    }

    /**
     * @param type string value of a ArgClassType
     * @return converted ArgClassType
     */
    public static ArgClassType toType(String type) {
        for (ArgClassType t : ArgClassType.values()) {
            if (t.getType().equals(type)) {
                return t;
            }
        }
        throw new RuntimeException("No ArgClassType found.");
    }
}
