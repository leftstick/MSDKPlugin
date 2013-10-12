package org.nanfeng.mcs.js.impl;

/**
 * Default dependency module implementation within Module. It
 * generates different JavaScript code from
 * <code>ImplementingModuleImpl</code>
 */
public class DependencyModuleImpl extends BaseModuleImpl {
    @Override
    public String toJavaScript() {
        StringBuffer js = new StringBuffer();
        // alias
        js.append(getAlias() + ":{");
        // id
        js.append("id:\"" + getId() + "\",");
        // version
        StringBuffer ver = new StringBuffer();
        ver.append("[");
        ver.append(this.getVersion()[0] + "," + this.getVersion()[1]);
        ver.append("]");
        js.append("version:" + ver.toString() + "");
        // optional
        if (this.getOptional()) {
            js.append(",optional: " + this.getOptional());
        }
        // DisableIfMissing
        if (this.getDisableIfMissing()) {
            js.append(",disableIfMissing: " + this.getDisableIfMissing());
        }
        // footer
        js.append("}");
        return js.toString();
    }
}
