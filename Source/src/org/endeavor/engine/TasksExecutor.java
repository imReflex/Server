package org.endeavor.engine;

import java.util.Timer;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

public class TasksExecutor {

	protected static volatile boolean shutdown;
	public static Timer fastExecutor;
	public static ScheduledExecutorService slowExecutor;
	
	public static void init() {
		int availableProcessors = Runtime.getRuntime().availableProcessors();
		fastExecutor = new Timer("Fast Executor");
		slowExecutor = availableProcessors >= 6 ? Executors.newScheduledThreadPool(availableProcessors >= 12 ? 4 : 2, new SlowThreadFactory()) : 
						Executors.newSingleThreadScheduledExecutor(new SlowThreadFactory());
	}
	
	public static void shutdown() {
		fastExecutor.cancel();
		slowExecutor.shutdown();
		shutdown = true;
	}
	
}
