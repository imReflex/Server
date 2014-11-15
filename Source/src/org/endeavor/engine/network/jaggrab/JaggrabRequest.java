package org.endeavor.engine.network.jaggrab;

/**
 * 
 * @author Joshua Barry
 * 
 */
public class JaggrabRequest {

	public static final String[] PATHS = { "/crc", "/title", "/config", "/interface", "/media", "/versionlist",
			"/textures", "/wordenc", "/sounds" };
	public static final byte[] INDICES = { -2, 1, 2, 3, 4, 5, 6, 7, 8 };
	private String path;

	public JaggrabRequest(String path) {
		this.path = path;
	}

	public int getIndex() {
		for (int index = 0; index < PATHS.length; index++) {
			if (path.startsWith(PATHS[index])) {
				return INDICES[index];
			}
		}
		return 0;
	}
}
