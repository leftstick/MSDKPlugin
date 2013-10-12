package org.nanfeng.mcs.js.impl;

import java.util.List;

import org.nanfeng.mcs.js.Constraint;
import org.nanfeng.mcs.js.Module;
import org.nanfeng.mcs.js.ModuleDefinition;
import org.nanfeng.mcs.js.Type;
import org.nanfeng.mcs.util.StringUtil;

/**
 * Default implementation of <code>ModuleDefinition</code>
 */
public class ModuleDefinitionImpl implements ModuleDefinition {

    private String moduleID;
    private int[] version;
    private Type type;
    private List<Module> implementing;
    private List<Module> dependencies;
    private List<Constraint> constraints;

    @Override
    public Type getType() {
        return type;
    }

    @Override
    public void setType(Type type) {
        this.type = type;
    }

    @Override
    public int[] getVersion() {
        return version;
    }

    @Override
    public void setVersion(int[] version) {
        this.version = version;
    }

    @Override
    public void setConstraints(List<Constraint> cons) {
        this.constraints = cons;
    }

    @Override
    public String getModuleID() {
        return this.moduleID;
    }

    @Override
    public void setModuleID(String moduleID) {
        this.moduleID = moduleID;
    }

    @Override
    public List<Constraint> getConstraints() {
        return this.constraints;
    }

    @Override
    public void setImplementing(List<Module> impls) {
        this.implementing = impls;
    }

    @Override
    public List<Module> getImplementing() {
        return this.implementing;
    }

    @Override
    public void setDependency(List<Module> depends) {
        this.dependencies = depends;
    }

    @Override
    public List<Module> getDependencies() {
        return this.dependencies;
    }

    @Override
    public String toJavaScript() {
        StringBuffer js = new StringBuffer();
        // module header
        js.append("var module = {");
        // id
        js.append("id: \"" + this.getModuleID() + "\",");
        // version
        StringBuffer ver = new StringBuffer();
        ver.append("[");
        ver.append(this.getVersion()[0] + "," + this.getVersion()[1]);
        ver.append("]");
        js.append("version:" + ver.toString() + ",");
        // type
        js.append("type:\"" + this.getType().getType() + "\"");
        // implementing
        if ((this.getImplementing() != null) && !this.getImplementing().isEmpty()) {
            js.append(",");
            js.append(StringUtil.NEW_LINE);
            js.append("implementing: {");
            for (int i = 0; i < this.getImplementing().size(); i++) {
                Module m = this.getImplementing().get(i);
                if (i == (this.getImplementing().size() - 1)) {
                    js.append(m.toJavaScript());
                } else {
                    js.append(m.toJavaScript() + ",");
                }
            }
            js.append("}");
        }
        // dependency
        if ((this.getDependencies() != null) && !this.getDependencies().isEmpty()) {
            js.append(",");
            js.append(StringUtil.NEW_LINE);
            js.append("dependencies: {");
            for (int i = 0; i < this.getDependencies().size(); i++) {
                Module m = this.getDependencies().get(i);
                if (i == (this.getDependencies().size() - 1)) {
                    js.append(m.toJavaScript());
                } else {
                    js.append(m.toJavaScript() + ",");
                }
            }
            js.append("}");
        }
        // constraint
        if ((this.getConstraints() != null) && !this.getConstraints().isEmpty()) {
            js.append(",");
            js.append(StringUtil.NEW_LINE);
            js.append("constraints: {");
            for (int i = 0; i < this.getConstraints().size(); i++) {
                Constraint c = this.getConstraints().get(i);
                if (i == (this.getDependencies().size() - 1)) {
                    js.append("\"" + c.getKey() + "\":\"" + c.getValue() + "\"");
                } else {
                    js.append("\"" + c.getKey() + "\":\"" + c.getValue() + "\",");
                }
            }
            js.append("}");
        }
        js.append("};");
        return js.toString();
    }
}
