package org.nanfeng.mcs.util.gui;

import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;

/**
 * Static utility methods for manipulating SWT/JFace GUI
 */
public class GUIUtil {

    /**
     * Create a <code>GridLayout</code> with given column number, and
     * its columns are equal width
     * 
     * @param colNum column number
     * @return GridLayout
     */
    public static GridLayout gridLayoutEqualWidth(int colNum) {
        GridLayout layout = new GridLayout();
        layout.marginWidth = 0;
        layout.marginHeight = 0;
        layout.makeColumnsEqualWidth = true;
        layout.numColumns = colNum;
        return layout;
    }

    /**
     * Create a <code>GridLayout</code> with given column number, and
     * its columns are not equal width
     * 
     * @param colNum column number
     * @return GridLayout
     */
    public static GridLayout gridLayoutNoEqualWidth(int colNum) {
        GridLayout layout = new GridLayout();
        layout.marginWidth = 0;
        layout.marginHeight = 0;
        layout.makeColumnsEqualWidth = false;
        layout.numColumns = colNum;
        return layout;
    }

    /**
     * Create a <code>GridData</code> with given horizontalSpan.
     * 
     * <pre>
     *   verticalAlignment = GridData.FILL;
     *   horizontalAlignment = GridData.FILL;
     *   horizontalSpan = horizontalSpan;
     *   grabExcessHorizontalSpace = true;
     * </pre>
     * 
     * @param horizontalSpan
     * @return GridData
     */
    public static GridData gridDataFillHorizontal(int horizontalSpan) {
        GridData data = new GridData(GridData.FILL_HORIZONTAL);
        data.verticalAlignment = GridData.FILL;
        data.horizontalAlignment = GridData.FILL;
        data.horizontalSpan = horizontalSpan;
        data.grabExcessHorizontalSpace = true;
        return data;
    }

    /**
     * Create a <code>GridData</code> with given horizontalSpan.
     * 
     * <pre>
     *   verticalAlignment = GridData.FILL;
     *   horizontalAlignment = GridData.FILL;
     *   horizontalSpan = horizontalSpan;
     *   grabExcessHorizontalSpace = true;
     * </pre>
     * 
     * @param horizontalSpan
     * @return GridData
     */
    public static GridData gridDataFillBoth(int horizontalSpan) {
        GridData data = new GridData(GridData.FILL_BOTH);
        data.verticalAlignment = GridData.FILL;
        data.horizontalAlignment = GridData.FILL;
        data.horizontalSpan = horizontalSpan;
        data.grabExcessHorizontalSpace = true;
        return data;
    }
}
