package net.romvoid95.curseforge.util;

import java.util.concurrent.ThreadFactory;

import ch.qos.logback.classic.Logger;

public class ThreadBuilder {
	
	public static ThreadFactory newFactory(String threadName, Logger log) {
		return newFactory(threadName, log, true);
	}
	
    public static ThreadFactory newFactory(String threadName, Logger logger, boolean isdaemon)
    {
        return (r) ->
        {
            Thread t = new Thread(r, threadName);
            t.setDaemon(isdaemon);
            t.setUncaughtExceptionHandler((final Thread thread, final Throwable throwable) ->
                    logger.error("There was a uncaught exception in the {} threadpool", thread.getName(), throwable));
            return t;
        };
    }
}
