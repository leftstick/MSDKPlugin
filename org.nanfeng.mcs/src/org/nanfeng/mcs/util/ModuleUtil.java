package org.nanfeng.mcs.util;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import org.eclipse.jface.text.Document;
import org.eclipse.jface.text.IDocument;
import org.eclipse.text.edits.TextEdit;
import org.eclipse.wst.jsdt.core.JavaScriptCore;
import org.eclipse.wst.jsdt.core.ToolFactory;
import org.eclipse.wst.jsdt.core.formatter.CodeFormatter;
import org.nanfeng.mcs.js.Module;
import org.nanfeng.mcs.js.Type;

/**
 * Static utility methods for manipulating module
 */
public class ModuleUtil {

    private static final String MODULEID_PATTERN = "com.zuohao.iptv.portal(\\.[a-zA-Z0-9]+)+";

    /**
     * @param version consist of a major version and minor version
     * @return string, e.g. [1 , 0]
     */
    public static String versionToStr(int[] version) {
        StringBuffer ver = new StringBuffer();
        ver.append("[ ");
        ver.append(version[0]);
        ver.append(" , ");
        ver.append(version[1]);
        ver.append(" ]");
        return ver.toString();
    }

    /**
     * @param major a number between 1 to 3
     * @param minor a number between 0 to 9
     * @return array e.g. [1,0]
     */
    public static int[] stringToVersion(String major, String minor) {
        int[] version = new int[2];
        version[0] = Integer.parseInt(major);
        version[1] = Integer.parseInt(minor);
        return version;
    }

    /**
     * Parse an module id to path. e.g.
     * 
     * <pre>
     * com.zuohao.iptv.portal.br.logic.ReminderLogic [1,0] => com/zuohao/iptv/portal/br/logic/ReminderLogic.js
     * com.zuohao.iptv.portal.fw.device.interfaces.AsyncDiscoveryIF [1,2] => com/zuohao/iptv/portal/fw/device/interfaces/AsyncDiscoveryIF-1.2.js
     * </pre>
     * 
     * @param id id of a module
     * @param vers version of a module
     * @param type type of a module
     * @return the path
     */
    public static String moduleIdToPath(String id, int[] vers, Type type) {
        String[] ids = id.split("\\.");
        StringBuffer path = new StringBuffer();
        path.append("src/main/js/");
        for (int i = 0; i < (ids.length - 1); i++) {
            path.append(ids[i] + "/");
        }
        if ((vers[1] > 0) && (type == Type.INTERFACE)) {
            path.append(ids[ids.length - 1] + "-" + vers[0] + "." + vers[1] + ".js");
        } else {
            path.append(ids[ids.length - 1] + ".js");
        }
        return path.toString();
    }

    /**
     * Format the JavaScript code string to a formatted style
     * 
     * @param js string of JavaScript code
     * @return formatted JavaScript code
     */
    public static String formatCode(String js) {
        Hashtable<?, ?> options = JavaScriptCore.getOptions();
        CodeFormatter formatter = ToolFactory.createCodeFormatter(options, ToolFactory.M_FORMAT_NEW);
        TextEdit edit = formatter.format(CodeFormatter.K_JAVASCRIPT_UNIT, js, 0, js.length(), 0, StringUtil.NEW_LINE);
        if (edit == null) {
            return js;
        }
        IDocument doc = new Document(js);
        try {
            edit.apply(doc);
        } catch (Exception e) {
            e.printStackTrace();
            return js;
        }
        return doc.get();
    }

    /**
     * Wrapper the given list of subclass of Module to a Module list
     * 
     * @param sub list of subclass of Module
     * @return list of Module
     */
    public static List<Module> wapperModuleSubset(List<? extends Module> sub) {
        List<Module> wapper = new ArrayList<Module>();
        wapper.addAll(sub);
        return wapper;
    }

    /**
     * Check if the given idOrPath is a valid ModuleId or ModulePath
     * 
     * @param idOrPath
     * @return true if it is valid, otherwise false
     */
    public static boolean isValidModuleIdOrPath(String idOrPath) {
        return idOrPath.matches(MODULEID_PATTERN);
    }

}
