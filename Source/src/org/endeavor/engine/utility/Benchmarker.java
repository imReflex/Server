package org.endeavor.engine.utility;

import java.util.HashMap;
import java.util.Map;

public class Benchmarker {

	private static final Map<Integer, Long[]> benchmarks = new HashMap<Integer, Long[]>();

	private static long curr = 0;

	private static int timer = 0;

	private Benchmarker() {
	}

	public static void start() {
		curr = System.currentTimeMillis();
	}

	public static void stop(int id) {
		long next = System.currentTimeMillis() - curr;

		long am;
		long avg;
		long total;

		try {
			if (benchmarks.containsKey(id)) {
				am = benchmarks.get(id)[0] + 1;
				total = next + benchmarks.get(id)[2];
				avg = total / am;
			} else {
				am = 1;
				avg = next;
				total = next;
			}

			benchmarks.put(id, new Long[] { am, avg, total });
		} catch (Exception e) {
			e.printStackTrace();
			benchmarks.remove(id);
		}
	}

	public static void process() {
		timer++;

		if (timer == 20) {
			timer = 0;
			print();
		}
	}

	public static void print() {
		int id = 0;

		for (Long[] i : benchmarks.values()) {
			System.out.println("id: " + id + " avg: " + i[1] + " am: " + i[0] + " total: " + i[2]);
			id++;
		}
	}

}
