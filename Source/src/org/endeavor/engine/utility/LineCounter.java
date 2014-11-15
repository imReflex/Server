package org.endeavor.engine.utility;

import java.io.File;
import java.io.FileReader;
import java.io.LineNumberReader;
import java.util.LinkedList;
import java.util.List;

public class LineCounter {

	public static void run() {
		List<File> files = listRecursive(new File("./src/"), new Filter<File>() {
			@Override
			public boolean accept(File t) {
				return t.getName().endsWith(".java");
			}
		});

		int lineCount = 0;
		for (File file : files) {
			lineCount += lineCount(file);
		}

		System.out.println("Lines of code: " + lineCount + " in " + files.size() + " files");
	}

	public interface Filter<T> {
		public boolean accept(T t);
	}

	public static LinkedList<File> listRecursive(File file, Filter<File> filter) {
		LinkedList<File> files = new LinkedList<File>();
		for (File f : file.listFiles()) {
			if (f.isDirectory()) {
				files.addAll(listRecursive(f, filter));
			} else {
				if (filter.accept(f))
					files.add(f);
			}
		}
		return files;
	}

	public static int lineCount(File file) {
		int count = 0;
		try {
			LineNumberReader ln = new LineNumberReader(new FileReader(file));
			while (true) {
				String line = ln.readLine();
				if (line == null)
					break;
				if (!line.trim().equals(""))
					count++;
			}
			ln.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return count;
	}
}
