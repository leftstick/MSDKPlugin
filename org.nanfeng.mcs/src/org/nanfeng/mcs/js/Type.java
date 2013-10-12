package org.nanfeng.mcs.js;

/**
 * Type of a module, consist of interface, abstraction, code.
 */
public enum Type {

    /**
     * <code>CODE</code>
     */
    CODE("code"),

    /**
     * <code>INTERFACE</code>
     */
    INTERFACE("interface"),

    /**
     * <code>ABSTRACTION</code>
     */
    ABSTRACTION("abstraction"),

    /**
     * <code>PACKAGE</code>
     */
    PACKAGE("package");

    private String type;

    private Type(String type) {
        this.type = type;
    }

    /**
     * @return String value of this Type
     */
    public String getType() {
        return this.type;
    }

    /**
     * @param type string value of a Type
     * @return converted Type
     */
    public static Type toType(String type) {
        for (Type t : Type.values()) {
            if (t.getType().equals(type)) {
                return t;
            }
        }
        throw new RuntimeException("No type found.");
    }
}
