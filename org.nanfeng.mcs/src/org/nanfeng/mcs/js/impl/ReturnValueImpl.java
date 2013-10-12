package org.nanfeng.mcs.js.impl;

import org.nanfeng.mcs.js.ReturnValue;

/**
 * Default implementation of <code>ReturnValue</code>
 */
public class ReturnValueImpl implements ReturnValue {

    @Override
    public String toJavaScript() {
        return "return module;";
    }

}
