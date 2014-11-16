

public class Configuration {
	
	//public final static String jaggrab = /*"127.0.0.1"*/"107.167.4.210";
	//public final static String server = /*"127.0.0.1"*/"107.167.4.210"; 
	public final static String jaggrab = "aphelion-rsps.no-ip.org";//
	public final static String server = "aphelion-rsps.no-ip.org"; 
	//public final static String server = "localhost";
	public final static int CHARACTERS_SEPARATOR_WIDTH = 110; 
	public final static double CLIENT_VERSION = 0.1;
	
	public final static String CLIENT_NAME = "Aphelion | BETA " + CLIENT_VERSION;
	
	public final static boolean LOAD_BACKGROUND = false;
	
	public final static boolean fog = false;
	
	public final static String portID = "43594";
	/**
	 * The NPC bits.
	 * 12 = 317/377
	 * 14 = 474+
	 * 16 = 600+
	 */
	public final static int NPC_BITS = 16;

	public static final boolean JAGCACHED_ENABLED = true;
	
	public static int[] ITEMS_WITH_BLACK = {
		560, 559, 1077, 1089, 1125, 1149, 1153, 1155, 1157, 1159, 1161, 1163,
		1165, 1279, 1313, 1327, 2349, 2351, 2353, 2355, 2357, 2359,
		2361, 2363, 2619, 2627, 2657, 2665, 2673, 3140, 3486, 6568, 11335, 12158, 1261, 
		12162, 12163, 12164, 12165, 12166, 12167, 12168, 15332, 15333, 15334, 15335,
		13675, 14479, 18510, 19337, 19342, 19347, 19407, 19437
	};
}
