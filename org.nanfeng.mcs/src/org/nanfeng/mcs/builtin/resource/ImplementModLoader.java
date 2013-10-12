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
import org.nanfeng.mcs.js.Type;
import org.nanfeng.mcs.js.external.SourceScanner;
import org.nanfeng.mcs.js.impl.ImplementingModuleImpl;
import org.nanfeng.mcs.util.ModuleUtil;

/**
 * A default implementation of ResourceLoader, to load modules by
 * using <code>ImplementingModuleImpl</code> parser
 */
public class ImplementModLoader implements ResourceLoader, Observer {
    private List<Module> builtInList;
    private List<Module> modules;

    /**
     */
    public ImplementModLoader() {
        modules = new ArrayList<Module>();
        builtInList = readInternal();
        modules.addAll(builtInList);
        modules.addAll(readExternal());
    }

    private List<Module> readInternal() {
        InputStream is1 = this.getClass().getResourceAsStream("ModulesDB.json");
        List<ImplementingModuleImpl> temp = JSONUtils.fromJSON(is1, new TypeReference<List<ImplementingModuleImpl>>() {
            //
        });
        List<ImplementingModuleImpl> filter = filterCode(temp);
        return ModuleUtil.wapperModuleSubset(filter);
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
        List<ImplementingModuleImpl> filter;
        try {
            List<ImplementingModuleImpl> temp = JSONUtils.fromJSON(new FileInputStream(SourceScanner.moduleDB),
                    new TypeReference<List<ImplementingModuleImpl>>() {
                        //
                    });
            filter = filterCode(temp);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return new ArrayList<Module>();
        }
        return ModuleUtil.wapperModuleSubset(filter);
    }

    private List<ImplementingModuleImpl> filterCode(List<ImplementingModuleImpl> list) {
        List<ImplementingModuleImpl> filtered = new ArrayList<ImplementingModuleImpl>();
        for (ImplementingModuleImpl m : list) {
            if (m.getType() != Type.CODE) {
                filtered.add(m);
            }
        }
        return filtered;
    }
}
