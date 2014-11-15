
public class OldIDK extends IDK {

	public static OldIDK cache[];
	
	public static void unpackConfig(CacheArchive streamLoader) {
		Stream stream = new Stream(streamLoader.getDataForName("idk2.dat"));
		int length = stream.readUnsignedWord();
		System.out.println("Loaded: " + length + "  Old IdentityKit definitions.");
		if (cache == null)
			cache = new OldIDK[length];
		for (int j = 0; j < length; j++) {
			if (cache[j] == null)
				cache[j] = new OldIDK();
			cache[j].readValues(stream);
		}
	}
	
	@Override
	public void readValues(Stream stream) {
		do {
			int opcode = stream.readUnsignedByte();
			if (opcode == 0)
				return;
			if (opcode == 1)
				bodyPartID = stream.readUnsignedByte();
			else if (opcode == 2) {
				int modelCount = stream.readUnsignedByte();
				bodyModelIDs = new int[modelCount];
				for (int k = 0; k < modelCount; k++)
					bodyModelIDs[k] = stream.readUnsignedWord();

			} else if (opcode == 3)
				notSelectable = true;
			else if (opcode >= 40 && opcode < 50)
				recolourOriginal[opcode - 40] = stream.readUnsignedWord();
			else if (opcode >= 50 && opcode < 60)
				recolourTarget[opcode - 50] = stream.readUnsignedWord();
			else if (opcode >= 60 && opcode < 70)
				headModelIDs[opcode - 60] = stream.readUnsignedWord();
			else
				System.out.println("Error unrecognised config code: " + opcode);
		} while (true);
	}
	
}
