package org.endeavor.engine.utility;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * @author Allen
 */
public class FileUtils {

	/**
	 * Copy a file.
	 * @param from
	 * @param to
	 */
	public static void copy(File from, File to) {
    	try {
			InputStream in = new FileInputStream(from);
			OutputStream out = new FileOutputStream(to);
			byte[] buf = new byte[1024];
			int len;
			while ((len = in.read(buf)) > 0) {
				out.write(buf, 0, len);
			}
			in.close();
			out.close();
    	} catch(Exception e) {
    		e.printStackTrace();
    	}
	}
	
}
