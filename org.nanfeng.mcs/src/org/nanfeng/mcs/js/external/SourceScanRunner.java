package org.nanfeng.mcs.js.external;

import java.util.List;

import org.eclipse.core.resources.WorkspaceJob;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;

/**
 * This is a runner executes the source scan in background
 */
public class SourceScanRunner {

    private static SourceScanRunner runner;

    private boolean busy;

    private WorkspaceJob job;

    private SourceScanner scanner;

    /**
     * @return instance of SourceScanRunner
     */
    public static SourceScanRunner getRunner() {
        if (runner == null) {
            runner = new SourceScanRunner();
        }
        return runner;
    }

    private SourceScanRunner() {
        scanner = new SourceScanner();
        job = new WorkspaceJob("Source Scan") {
            @Override
            public IStatus runInWorkspace(IProgressMonitor monitor) throws CoreException {
                scanner.setMonitor(monitor);
                boolean errorOccurs = scanner.execute();
                if (errorOccurs) {
                    setBusy(false);
                    return Status.CANCEL_STATUS;
                }
                setBusy(false);
                return Status.OK_STATUS;
            }
        };
    }

    /**
     * Set Base Dir to scanner. This must be called before run.
     * 
     * @param baseDir
     */
    public void setBaseDir(String baseDir) {
        scanner.setBaseDir(baseDir);
    }

    /**
     * Set include expressions to scanner. This must be called before
     * run.
     * 
     * @param includes
     */
    public void setIncludes(List<String> includes) {
        scanner.setIncludes(includes);
    }

    /**
     * Run a new scan. User must check busy first when you want to
     * run.
     */
    public void run() {
        setBusy(true);
        job.schedule();
    }

    /**
     * @return true if a job is running
     */
    public boolean isBusy() {
        return busy;
    }

    private void setBusy(boolean busy) {
        this.busy = busy;
    }

}
