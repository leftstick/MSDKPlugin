package org.nanfeng.mcs.builtin.resource;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import org.codehaus.jackson.type.TypeReference;
import org.nanfeng.mcs.js.Module;
import org.nanfeng.mcs.js.external.SourceScanner;
import org.nanfeng.mcs.js.impl.DependencyModuleImpl;
import org.nanfeng.mcs.util.ModuleUtil;

/**
 * A default implementation of ResourceLoader, to load modules by
 * using <code>DependencyModuleImpl</code> parser
 */
public class DependencyModLoader implements ResourceLoader, Observer {
    private List<Module> builtInList;
    private List<Module> modules;

    /**
     * 
     */
    public DependencyModLoader() {
        modules = new ArrayList<Module>();
        builtInList = readInternal();
        modules.addAll(builtInList);
        modules.addAll(readExternal());
    }

    private List<Module> readInternal() {
        InputStream is1 = this.getClass().getResourceAsStream("ModulesDB.json");
        List<DependencyModuleImpl> temp = JSONUtils.fromJSON(is1, new TypeReference<List<DependencyModuleImpl>>() {
            //
        });

        return ModuleUtil.wapperModuleSubset(temp);
    }

    @Override
    public List<Module> loadMods() {
        return modules;
    }

    @Override
    public void update(Observable o, Object arg) {
        modules.clear();
        modules.addAll(builtInList);
        modules.addAll(readExternal());
    }

    private List<Module> readExternal() {
        File exFile = new File(SourceScanner.moduleDB);
        if (!exFile.exists()) {
            return new ArrayList<Module>();
        }
        List<DependencyModuleImpl> temp;
        try {
            temp = JSONUtils.fromJSON(new FileInputStream(SourceScanner.moduleDB),
                    new TypeReference<List<DependencyModuleImpl>>() {
                        //
                    });

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return new ArrayList<Module>();
        }
        return ModuleUtil.wapperModuleSubset(temp);
    }

}
