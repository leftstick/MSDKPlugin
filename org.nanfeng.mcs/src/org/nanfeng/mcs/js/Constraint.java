package org.nanfeng.mcs.js;

/**
 * Constraint bean
 */
public class Constraint {
    private String key;
    private String value;

    /**
     * @param key key of this constraint
     * @param value value of this constraint
     * 
     */
    public Constraint(String key, String value) {
        this.key = key;
        this.value = value;
    }

    /**
     * empty-argument constructor of <code>Constraint</code>. The key,
     * value of this <code>Constraint</code> must be set later on
     * before using them.
     */
    public Constraint() {
        this(null, null);
    }

    /**
     * @return key of this constraint
     */
    public String getKey() {
        return key;
    }

    /**
     * @param key
     */
    public void setKey(String key) {
        this.key = key;
    }

    /**
     * @return value of this constraint
     */
    public String getValue() {
        return value;
    }

    /**
     * @param value
     */
    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Constraint)) {
            return false;
        }
        Constraint con = (Constraint) obj;
        if ((con.getKey() == null) || (this.key == null) || (con.getValue() == null) || (this.value == null)) {
            return false;
        }
        if (!con.getKey().equals(this.key)) {
            return false;
        }
        if (!con.getValue().equals(this.value)) {
            return false;
        }
        return true;
    }
}
