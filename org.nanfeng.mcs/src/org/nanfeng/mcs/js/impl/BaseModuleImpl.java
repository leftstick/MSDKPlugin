package org.nanfeng.mcs.js.impl;

import java.util.Map;

import org.nanfeng.mcs.js.Module;
import org.nanfeng.mcs.js.Type;

/**
 * Default implementation of Module
 */
public class BaseModuleImpl implements Module {

    private final String REG = "(-[0-9]\\.[0-9]){0,1}\\.js";

    private String artifact;
    private String id;
    private String name;
    private String namespace;
    private String file;
    private String alias;
    private int[] version;
    private Type type;
    private Map<String, Object> publics;
    private Map<String, Object> statics;
    private boolean optional;
    private boolean disableIfMissing;

    /**
     * @return Artifact
     */
    @Override
    public String getArtifact() {
        return artifact;
    }

    /**
     * @param artifact
     */
    public void setArtifact(String artifact) {
        this.artifact = artifact;
    }

    private String getModuleName() {
        String fileWithoutExtension = file.substring(0, file.length() - 3);
        String nameWithoutVer = id.substring(id.lastIndexOf(".") + 1);
        String moduleName = fileWithoutExtension.substring(fileWithoutExtension.indexOf(nameWithoutVer));
        return moduleName;
    }

    /**
     * @param id
     */
    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String getId() {
        return this.id;
    }

    @Override
    public String getName() {
        return name;
    }

    /**
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getNamespace() {
        return namespace;
    }

    /**
     * @param namespace
     */
    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }

    @Override
    public String getFile() {
        return file;
    }

    /**
     * @param file
     */
    public void setFile(String file) {
        this.file = file;
        this.setId(file.replaceAll(REG, ""));
        this.setName(getModuleName());
        this.setNamespace(id.substring(0, id.lastIndexOf(".")));
    }

    /**
     * @param alias
     */
    public void setAlias(String alias) {
        this.alias = alias;
    }

    @Override
    public String getAlias() {
        return this.alias;
    }

    /**
     * @param version
     */
    public void setVersion(int[] version) {
        this.version = version;
    }

    @Override
    public int[] getVersion() {
        return this.version;
    }

    @Override
    public Type getType() {
        return type;
    }

    /**
     * @param type
     */
    public void setType(String type) {
        this.type = Type.toType(type);
    }

    /**
     * @param publics
     */
    public void setPublics(Map<String, Object> publics) {
        this.publics = publics;
    }

    @Override
    public Map<String, Object> getPublics() {
        return this.publics;
    }

    /**
     * @param statics
     */
    public void setStatics(Map<String, Object> statics) {
        this.statics = statics;
    }

    @Override
    public Map<String, Object> getStatics() {
        return this.statics;
    }

    @Override
    public boolean getOptional() {
        return optional;
    }

    @Override
    public void setOptional(boolean optional) {
        this.optional = optional;
    }

    @Override
    public boolean getDisableIfMissing() {
        return disableIfMissing;
    }

    @Override
    public void setDisableIfMissing(boolean disableIfMissing) {
        this.disableIfMissing = disableIfMissing;
    }

    @Override
    public String toJavaScript() {
        return null;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof BaseModuleImpl)) {
            return false;
        }
        BaseModuleImpl o = (BaseModuleImpl) obj;
        return o.getFile().equals(this.file);
    }
}
