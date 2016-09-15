package org.jdtsort;

import org.eclipse.core.runtime.ILog;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;

import static org.eclipse.core.runtime.IStatus.ERROR;
import static org.jdtsort.Activator.PLUGIN_ID;

public final class Logger {
    private static final ILog log = Activator.getDefault().getLog();

    public static void log(Throwable exception) {
        log.log(new Status(ERROR, PLUGIN_ID, exception.getMessage(), exception));
    }

    public static void log(int severity, String message) {
        log.log(new Status(severity, PLUGIN_ID, message));
    }

    public static void log(String message, Throwable exception) {
        log.log(new Status(ERROR, PLUGIN_ID, message, exception));
    }

    public static void log(int severity, int code, String message, Throwable exception) {
        log.log(new Status(severity, PLUGIN_ID, code, message, exception));
    }

    public static void logError(String message) {
        log.log(new Status(ERROR, PLUGIN_ID, message));
    }

    public static void logInfo(String message) {
        log.log(new Status(IStatus.INFO, PLUGIN_ID, message));
    }

    public static void logOK(String message) {
        log.log(new Status(IStatus.OK, PLUGIN_ID, message));
    }

    public static void logWarning(String message) {
        log.log(new Status(IStatus.WARNING, PLUGIN_ID, message));
    }

    private Logger() {}
}
