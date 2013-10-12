package org.nanfeng.mcs.builtin.resource;

/**
 * ResourceLoaderFactory to create instance of resource loader
 */
public class ResourceLoaderFactory {
    private static ResourceLoader impl;
    private static ResourceLoader depen;

    /**
     * get the ImplementModLoader
     * 
     * @return ResourceLoader
     */
    public static ResourceLoader getImplementLoader() {
        if (impl == null) {
            impl = new ImplementModLoader();
        }
        return impl;
    }

    /**
     * get the DependencyModLoader
     * 
     * @return ResourceLoader
     */
    public static ResourceLoader getDependencyLoader() {
        if (depen == null) {
            depen = new DependencyModLoader();
        }
        return depen;
    }
}
