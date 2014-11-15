package org.endeavor.engine.utility;

public class ThreadUtility {

	private long[] total;
	private int[] amount;
	private long[] last;
	private long[] average;

	public static boolean PRINT_AVERAGE = false;

	public static boolean PRINT_CURRENT = true;

	public ThreadUtility(int count) {
		average = new long[count];
		total = new long[count];
		amount = new int[count];
		last = new long[count];
	}

	public void print() {
		System.out.println("packet thread average: " + average[1] + "ms");
		/*
		 * if (!PRINT_AVERAGE && !PRINT_CURRENT) { return; }
		 * 
		 * if (ready()) { String t = null; synchronized (average) { t = "[t] ";
		 * for (int i = 0; i < average.length; i++) t += "(" + i + ": " +
		 * (PRINT_AVERAGE ? "" + average[i] + "ms, " : "") + last[i] + "ms)"; }
		 * System.out.println(t); } else { System.out.println("[t] Waiting...");
		 * }
		 */
	}

	public boolean ready() {
		synchronized (average) {
			for (int i = 0; i < amount.length; i++)
				if (amount[i] == 0)
					return false;
		}
		return true;
	}

	public void benchmark(int id, long start, long end) {
		total[id] += (end - start);
		amount[id]++;
		last[id] = (end - start);
		average[id] = total[id] / amount[id];
	}
}
