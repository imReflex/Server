import java.awt.Font;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.util.zip.GZIPInputStream;


/**
 * @author Sam
 */
public class FontArchive {

	public static RealFont[] cache;
	
	public static void unpackConfig(CacheArchive archive) {
		try {
			DataInputStream in = new DataInputStream(new ByteArrayInputStream(archive.getDataForName("realfonts")));
			byte fontCount = in.readByte();
			cache = new RealFont[fontCount];
			for (int index = 0; index < fontCount; index++) {
				long length = in.readLong();
				byte[] fileData = new byte[(int) length];
				in.readFully(fileData);
				cache[index] = new RealFont(Client.instance, Font.createFont(Font.TRUETYPE_FONT, new ByteArrayInputStream(fileData)).deriveFont(23), true);
			}
			in.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
