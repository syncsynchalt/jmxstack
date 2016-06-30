package net.ulfheim.jmxstack;

import javax.management.MBeanServerConnection;
import java.io.IOException;
import java.io.PrintStream;
import java.lang.management.ManagementFactory;
import java.lang.management.ThreadInfo;
import java.lang.management.ThreadMXBean;

public class StackPrinter {
    private ThreadMXBean threadMXBean;
    private PrintStream out;

    public StackPrinter(MBeanServerConnection connection, PrintStream outputStream) throws IOException {
        this.threadMXBean = ManagementFactory.newPlatformMXBeanProxy(connection,
                ManagementFactory.THREAD_MXBEAN_NAME, ThreadMXBean.class);
        this.out = outputStream;
    }

    public void doPrint() {
        long[] threadIds = threadMXBean.getAllThreadIds();
        ThreadInfo[] threadInfo = threadMXBean.getThreadInfo(threadIds, Integer.MAX_VALUE);
        for (ThreadInfo ti : threadInfo) {
            printThreadInfo(ti);
        }

    }

    private void printThreadInfo(ThreadInfo ti) {
        printThreadHeader(ti);

        StackTraceElement[] stacktrace = ti.getStackTrace();
        for (StackTraceElement ste : stacktrace) {
            out.println("\tat " + ste.toString());
        }
        out.println();
    }

    private void printThreadHeader(ThreadInfo ti) {
        out.print("\"" + ti.getThreadName() + "\"" + " id=" + ti.getThreadId() + " in " + ti.getThreadState());
        if (ti.getLockName() != null) {
            out.print(" on lock=" + ti.getLockName());
        }
        if (ti.isSuspended()) {
            out.print(" (suspended)");
        }
        if (ti.isInNative()) {
            out.print(" (running in native)");
        }
        out.println("");
        if (ti.getLockOwnerName() != null) {
            out.println("\t  owned by " + ti.getLockOwnerName() + " id=" + ti.getLockOwnerId());
        }
    }
}
