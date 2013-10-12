package org.nanfeng.mcs.create.wizard.wizardpage.component.constructor;

/**
 * <code>ColumnInfo</code> must be used in
 * <code>ModuleTableView</code>
 */
public class ColumnInfo {
    private String text;
    private int widthScale;

    /**
     * @param t header text of this column
     * @param scale width percentage of this column
     */
    public ColumnInfo(String t, int scale) {
        this.text = t;
        this.widthScale = scale;
    }

    /**
     * @return text
     */
    public String getText() {
        return text;
    }

    /**
     * @param text
     */
    public void setText(String text) {
        this.text = text;
    }

    /**
     * @return width percentage of this column
     */
    public int getWidthScale() {
        return widthScale;
    }

    /**
     * @param widthScale
     */
    public void setWidthScale(int widthScale) {
        this.widthScale = widthScale;
    }
}
