package org.nanfeng.mcs.builtin.resource;

import java.util.List;

import org.nanfeng.mcs.js.Module;

/**
 * A <code>ResourceLoader</code> defines the behavior of loading
 * modules from resource file
 */
public interface ResourceLoader {

    /**
     * retrieve the modules
     * 
     * @return list of module
     */
    public List<Module> loadMods();
}
