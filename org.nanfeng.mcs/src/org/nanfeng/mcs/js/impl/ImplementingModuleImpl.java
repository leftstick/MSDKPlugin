package org.nanfeng.mcs.js.impl;

import org.nanfeng.mcs.util.CollectionUtil;

/**
 * Default implementing module implementation within Module. It
 * generates different JavaScript code from
 * <code>DependencyModuleImpl</code>
 */
public class ImplementingModuleImpl extends BaseModuleImpl {
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
        // publics
        if (CollectionUtil.isNotEmpty(this.getPublics())) {
            js.append(",publics: {}");
        }
        // statics
        if (CollectionUtil.isNotEmpty(this.getStatics())) {
            js.append(",statics: {}");
        }
        // footer
        js.append("}");
        return js.toString();
    }
}
