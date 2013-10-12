package org.nanfeng.mcs.create.preference.component;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.jface.dialogs.IInputValidator;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.preference.ListEditor;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Composite;
import org.nanfeng.mcs.util.StringUtil;

/**
 * A field editor for a include expression preference.
 */
public class IncludeFieldEditor extends ListEditor {

    private final Pattern forbidden_pattern = Pattern.compile("[^!@#%\\^$&\\[\\]\\{\\}|\\(\\)\\?=,_+]{1,}");

    private final String splitChar = "|";

    private Composite parent;

    /**
     * Creates a new path field editor
     */
    protected IncludeFieldEditor() {
    }

    /**
     * Creates a path field editor.
     * 
     * @param name the name of the preference this field editor works
     *            on
     * @param parent the parent of the field editor's control
     */
    public IncludeFieldEditor(String name, Composite parent) {
        init(name, "&Includes:");
        createControl(parent);
        this.parent = parent;
    }

    @Override
    protected String createList(String[] items) {
        StringBuffer preference = new StringBuffer();
        for (String s : items) {
            if (StringUtil.isEmpty(s)) {
                continue;
            }
            preference.append(s);
            preference.append(splitChar);
        }
        return preference.toString();
    }

    @Override
    protected String getNewInputObject() {

        InputDialog dialog = new InputDialog(
                getShell(),
                "Include Expression",
                "The include pattern used to select javascript files for processing."
                        + StringUtil.NEW_LINE
                        + StringUtil.NEW_LINE
                        + ("All '\\/' and '\' characters are replaced by File.separatorChar, so the separator used need not match File.separatorChar.When a pattern ends with a '\\/' or '\', \"**\" is appended."),
                null, new IInputValidator() {
                    @Override
                    public String isValid(String newText) {
                        Matcher m = forbidden_pattern.matcher(newText);
                        return m.matches() ? null : "Contains illegal characters:!@#%^$&[]{}|()?=,_+";
                    }
                });
        int res = dialog.open();
        if (Window.OK != res) {
            return null;
        }
        return dialog.getValue();
    }

    @Override
    protected String[] parseString(String stringList) {
        return stringList.split("\\|");
    }

    /**
     * @return list of Include
     */
    public List<String> getIncludes() {
        org.eclipse.swt.widgets.List listC = super.getListControl(parent);
        return Arrays.asList(listC.getItems());
    }
}
