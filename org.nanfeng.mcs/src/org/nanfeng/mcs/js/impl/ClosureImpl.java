package org.nanfeng.mcs.js.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import org.nanfeng.mcs.js.ArgClassType;
import org.nanfeng.mcs.js.Closure;
import org.nanfeng.mcs.js.Module;
import org.nanfeng.mcs.js.ModuleDefinition;
import org.nanfeng.mcs.js.ReturnValue;
import org.nanfeng.mcs.js.Type;
import org.nanfeng.mcs.util.CollectionUtil;
import org.nanfeng.mcs.util.ObjectUtil;
import org.nanfeng.mcs.util.StringUtil;

/**
 * Default implementation of Closure
 */
public class ClosureImpl implements Closure {

    private static final String LOADING_ID = "com.zuohao.iptv.portal.fw.interfaces.LoadingIF";
    private static final String LOADING_LOAD = "load";

    private ModuleDefinition definition;
    private List<String> impls;
    private ReturnValue value;
    private List<String> variables;

    /**
     * <code>ClosureImpl</code>
     */
    public ClosureImpl() {
        impls = new ArrayList<String>();
        variables = new ArrayList<String>();
    }

    @Override
    public String getComments() {
        StringBuffer com = new StringBuffer();
        com.append("/*********************************************************************");
        com.append(StringUtil.NEW_LINE);
        com.append(" * COPYRIGHT zuohao " + Calendar.getInstance().get(Calendar.YEAR));
        com.append(StringUtil.NEW_LINE);
        com.append(" *");
        com.append(StringUtil.NEW_LINE);
        com.append(" * The copyright to the computer program(s) herein is the property of");
        com.append(StringUtil.NEW_LINE);
        com.append(" * zuohao Inc. The programs may be used and/or copied only with");
        com.append(StringUtil.NEW_LINE);
        com.append(" * written permission from zuohao Inc. or in accordance with the");
        com.append(StringUtil.NEW_LINE);
        com.append(" * terms and conditions stipulated in the agreement/contract under");
        com.append(StringUtil.NEW_LINE);
        com.append(" * which the program(s) have been supplied.");
        com.append(StringUtil.NEW_LINE);
        com.append(" ********************************************************************/");
        com.append(StringUtil.NEW_LINE);
        com.append(StringUtil.NEW_LINE);
        com.append("/**");
        com.append(StringUtil.NEW_LINE);
        com.append(" * ");
        com.append(StringUtil.NEW_LINE);
        com.append(" * @fileoverview");
        com.append(StringUtil.NEW_LINE);
        com.append(" * ");
        com.append(StringUtil.NEW_LINE);
        com.append(" * ");
        com.append(StringUtil.NEW_LINE);
        com.append(" */");
        com.append(StringUtil.NEW_LINE);
        return com.toString();
    }

    @Override
    public String getHeader() {
        return "(function() {";
    }

    @Override
    public void setModuleDefinition(ModuleDefinition definition) {
        this.definition = definition;
    }

    @Override
    public ModuleDefinition getModuleDefinition() {
        return this.definition;
    }

    @Override
    public void implementation() {
        if (ObjectUtil.isNull(this.getModuleDefinition())
                || CollectionUtil.isEmpty(this.getModuleDefinition().getImplementing())) {
            return;
        }
        impls.add(StringUtil.NEW_LINE);
        List<Module> ms = this.getModuleDefinition().getImplementing();

        for (Module m : ms) {
            if (CollectionUtil.isEmpty(m.getStatics())) {
                continue;
            }
            addStaticsToImplementation(m);
        }

        impls.add(StringUtil.NEW_LINE);
        for (Module m : ms) {
            if (CollectionUtil.isEmpty(m.getPublics())) {
                continue;
            }
            addPublicsToImplementation(m);
        }

    }

    private void addStaticsToImplementation(Module m) {
        Set<Entry<String, Object>> pubs = m.getStatics().entrySet();
        for (Entry<String, Object> pub : pubs) {
            impls.add("module.implementing.");
            impls.add(m.getAlias() + ".");
            impls.add("statics.");
            impls.add(pub.getKey() + "=");
            if (pub.getValue() instanceof String) {
                addImplementationAsObject(pub);
            } else {
                impls.add("\"\";");
            }
        }
    }

    private void addPublicsToImplementation(Module m) {
        Set<Entry<String, Object>> pubs = m.getPublics().entrySet();
        for (Entry<String, Object> pub : pubs) {
            impls.add("module.implementing.");
            impls.add(m.getAlias() + ".");
            impls.add("publics.");
            impls.add(pub.getKey() + "=");
            if (pub.getValue() instanceof List) {
                addImplementationAsFunction(m, pub);
            } else if (pub.getValue() instanceof String) {
                addImplementationAsObject(pub);
            }
        }
    }

    private void addImplementationAsObject(Entry<String, Object> pub) {
        String classTypeStr = (String) pub.getValue();
        ArgClassType classType = ArgClassType.toType(classTypeStr);
        if (classType == ArgClassType.ARRAY) {
            impls.add("[];");
        } else if (classType == ArgClassType.STRING) {
            impls.add("\"\";");
        } else if (classType == ArgClassType.NUMBER) {
            impls.add("0;");
        } else if (classType == ArgClassType.BOOLEAN) {
            impls.add("false;");
        } else if (classType == ArgClassType.OBJECT) {
            impls.add("{};");
        }
    }

    private void addImplementationAsFunction(Module m, Entry<String, Object> pub) {
        @SuppressWarnings("unchecked")
        List<String> args = (List<String>) pub.getValue();
        impls.add("function(");
        for (int i = 0; i < args.size(); i++) {
            if (i == (args.size() - 1)) {
                impls.add(args.get(i));
            } else {
                impls.add(args.get(i) + ",");
            }
        }
        impls.add(") {");
        addLoadingImplementation(m, pub);
        impls.add("};");
    }

    private void addLoadingImplementation(Module m, Entry<String, Object> pub) {
        if (!m.getId().equals(LOADING_ID) || !pub.getKey().equals(LOADING_LOAD)) {
            return;
        }
        if ((ObjectUtil.isNull(definition)) || CollectionUtil.isEmpty(definition.getDependencies())) {
            return;
        }
        List<Module> depends = definition.getDependencies();
        variables.add(StringUtil.NEW_LINE);
        variables.add("//");
        variables.add(StringUtil.NEW_LINE);
        variables.add("//Modules");
        variables.add(StringUtil.NEW_LINE);
        variables.add("//");
        variables.add(StringUtil.NEW_LINE);
        for (Module dep : depends) {
            if (dep.getType() == Type.INTERFACE) {
                continue;
            }
            variables.add("var " + dep.getAlias() + ";");
            impls.add(dep.getAlias() + " = module.dependencies." + dep.getAlias() + ".handle;");
        }

    }

    @Override
    public void setReturnValue(ReturnValue value) {
        this.value = value;
    }

    @Override
    public ReturnValue getReturnValue() {
        return this.value;
    }

    @Override
    public String getFooter() {
        return "});";
    }

    @Override
    public String toJavaScript() {
        StringBuffer js = new StringBuffer();
        js.append(getComments());
        js.append(getHeader());
        if (ObjectUtil.isNotNull(this.getModuleDefinition())) {
            js.append(this.getModuleDefinition().toJavaScript());
        }
        this.implementation();
        for (String var : variables) {
            js.append(var);
        }
        for (String impl : impls) {
            js.append(impl);
        }
        if (ObjectUtil.isNotNull(this.getReturnValue())) {
            js.append(this.getReturnValue().toJavaScript());
        }

        js.append(getFooter());

        return js.toString();
    }

}
