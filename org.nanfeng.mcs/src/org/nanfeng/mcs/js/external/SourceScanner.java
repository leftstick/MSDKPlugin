package org.nanfeng.mcs.js.external;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.Set;

import org.apache.commons.io.FileUtils;
import org.codehaus.plexus.util.DirectoryScanner;
import org.eclipse.core.runtime.IProgressMonitor;
import org.mozilla.javascript.tools.shell.Main;
import org.nanfeng.mcs.builtin.resource.ResourceLoaderFactory;
import org.nanfeng.mcs.util.FileUtil;

/**
 * This scanner helps user to scan Source code information from
 * external project.
 */
public class SourceScanner extends Observable {
    private final static String jsdocDistFilename = "jsdoc_toolkit-1.4.0";
    private final static String jsdocDistFileExtension = ".zip";
    private final static String userHome = System.getProperty("user.home");
    private final static String externalModuleDBHome = userHome + "/" + ".externalModuleDB";

    private final static String jsdocBuildDir = externalModuleDBHome + "/" + jsdocDistFilename;

    private final static String outputDir = externalModuleDBHome;

    private final static String templateFolderName = "ModuleDB-template";
    private final static String templateFileName = "ModulesDB.tmpl";
    private final static String publishFileName = "publish.js";
    private final static String templateFolder = externalModuleDBHome + "/" + templateFolderName;
    private final static String moduleDBName = "ModulesDB.json";

    /**
     * External <code>moduleDB</code>
     */
    public final static String moduleDB = externalModuleDBHome + "/" + moduleDBName;

    private String baseDir;
    private List<String> includes;
    private boolean errorOccurs;
    private IProgressMonitor monitor;

    /**
     */
    public SourceScanner() {
        this.addObserver((Observer) ResourceLoaderFactory.getDependencyLoader());
        this.addObserver((Observer) ResourceLoaderFactory.getImplementLoader());
    }

    /**
     * @param monitor
     */
    public void setMonitor(IProgressMonitor monitor) {
        this.monitor = monitor;
    }

    private void setup() {

        File mdb = new File(moduleDB);
        if (mdb.exists()) {
            mdb.delete();
        }

        String targetZip = externalModuleDBHome + "/" + jsdocDistFilename + jsdocDistFileExtension;
        errorOccurs = !FileUtil.copyFileFromInsideToOutside("/libs/" + jsdocDistFilename + jsdocDistFileExtension,
                targetZip);
        if (errorOccurs) {
            return;
        }
        preformProcess();

        errorOccurs = !FileUtil.copyFileFromInsideToOutside("/" + templateFolderName + "/" + templateFileName,
                externalModuleDBHome + "/" + templateFolderName + "/" + templateFileName);
        if (errorOccurs) {
            return;
        }
        preformProcess();

        errorOccurs = !FileUtil.copyFileFromInsideToOutside("/" + templateFolderName + "/" + publishFileName,
                externalModuleDBHome + "/" + templateFolderName + "/" + publishFileName);
        if (errorOccurs) {
            return;
        }
        preformProcess();

        errorOccurs = !FileUtil.unZipFile(targetZip, externalModuleDBHome);
        if (errorOccurs) {
            return;
        }
        preformProcess();
    }

    /**
     * Do execute the scan process and generate the moduleDB.
     * 
     * @return true if executed successful. Otherwise false
     */
    public boolean execute() {
        beginProcess();
        setup();
        if (errorOccurs) {
            return errorOccurs;
        }
        try {
            System.setProperty("jsdoc.dir", jsdocBuildDir);
            List<String> args = new ArrayList<String>();
            args.add(jsdocBuildDir + "/app/run.js");
            args.add("-d=" + outputDir);
            args.add("-t=" + templateFolder);
            Set<File> jsfiles = getJavaScriptFiles();
            if (jsfiles.isEmpty()) {
                errorOccurs = true;
                tearDown();
                endProcess();
                return errorOccurs;
            }
            preformProcess();
            for (File element : jsfiles) {
                args.add(element.getAbsolutePath());
            }
            Main.exec(args.toArray(new String[args.size()]));
            preformProcess();
        } catch (Exception e) {
            e.printStackTrace();
            errorOccurs = true;
        }
        tearDown();
        endProcess();
        return errorOccurs;
    }

    /**
     * @return true if error occurs. Otherwise false
     */
    public boolean isErrorOccurs() {
        return errorOccurs;
    }

    private void tearDown() {
        try {
            File jdDocDist = new File(jsdocBuildDir);
            if (jdDocDist.exists()) {
                FileUtils.deleteDirectory(jdDocDist);
            }
            File jdDocDistZip = new File(jsdocBuildDir + jsdocDistFileExtension);
            if (jdDocDistZip.exists()) {
                jdDocDistZip.delete();
            }
            File tempFolder = new File(templateFolder);
            if (tempFolder.exists()) {
                FileUtils.deleteDirectory(tempFolder);
            }
            if (errorOccurs) {
                File mdb = new File(moduleDB);
                if (mdb.exists()) {
                    mdb.delete();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Set<File> getJavaScriptFiles() {
        DirectoryScanner ds = new DirectoryScanner();

        ds.setBasedir(this.getBaseDir());
        if (this.getIncludes().isEmpty()) {
            ds.setIncludes(new String[] { "**/*.js" });
        } else {
            ds.setIncludes(this.getIncludes().toArray(new String[this.getIncludes().size()]));
        }
        ds.addDefaultExcludes();

        ds.setCaseSensitive(true);
        ds.scan();

        Set<File> files = new HashSet<File>();

        String[] relPaths = ds.getIncludedFiles();
        for (String relPath : relPaths) {
            File file = new File(this.getBaseDir(), relPath);
            files.add(file);
        }
        return files;
    }

    private void preformProcess() {
        if (monitor == null) {
            return;
        }
        monitor.subTask("Collecting module informations...");
        monitor.worked(12);
    }

    private void beginProcess() {
        if (monitor == null) {
            return;
        }
        System.err.println("set begine");
        monitor.beginTask("", 100);
    }

    private void endProcess() {
        if (!errorOccurs) {
            super.setChanged();
            super.notifyObservers();
        }
        if (monitor == null) {
            return;
        }
        monitor.done();
    }

    /**
     * @return BaseDir
     */
    public String getBaseDir() {
        return baseDir;
    }

    /**
     * @param baseDir The base directory.
     */
    public void setBaseDir(String baseDir) {
        this.baseDir = baseDir;
    }

    /**
     * @return list of include pattern
     */
    public List<String> getIncludes() {
        return includes;
    }

    /**
     * @param includes The include pattern used to select JavaScript
     *            files for processing.
     */
    public void setIncludes(List<String> includes) {
        this.includes = includes;
    }
}
