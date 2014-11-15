

public class IDK {

	public static void unpackConfig(CacheArchive streamLoader) {
		Stream stream = new Stream(streamLoader.getDataForName("idk.dat"));
		int length = stream.readUnsignedWord();
		System.out.println("Loaded: " + length + " IdentityKit definitions.");
		if (cache == null)
			cache = new IDK[length];
		for (int j = 0; j < length; j++) {
			if (cache[j] == null)
				cache[j] = new IDK();
			cache[j].readValues(stream);
		}
		if(Client.getOption("new_idk")) {
			changeIDK(true);
		}
	}

	
	public static void changeIDK(boolean newidk) {
		for(IDK idk : IDK.cache) {
			if(idk == null)
				return;
			for(int i = 0; i < idk.bodyModelIDs.length; i++) {
				if(newidk && idk.bodyModelIDs[i] <= 1500)
					idk.bodyModelIDs[i] = idk.bodyModelIDs[i] + 80000;
				else if(!newidk && idk.bodyModelIDs[i] >= 80000)
					idk.bodyModelIDs[i] = idk.bodyModelIDs[i] - 80000;
			}
			/*for(int i = 0; i < idk.headModelIDs.length; i++) {
				if(newidk && idk.headModelIDs[i] <= 1500)
					idk.headModelIDs[i] = idk.headModelIDs[i] + 80000;
				else if(!newidk && idk.headModelIDs[i] >= 80000)
					idk.headModelIDs[i] = idk.headModelIDs[i] - 80000;
			}*/
		}
	}
	
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
				for (int k = 0; k < modelCount; k++) {
					bodyModelIDs[k] = stream.readUnsignedWord();
					//System.out.println("[IDK] Body Part: " + bodyPartID + " Model ID: " + bodyModelIDs[k]);
					//if(bodyModelIDs[k] <= 1500)
						//FileOperations.copy(bodyModelIDs[k]);
				}
			} else if (opcode == 3)
				notSelectable = true;
			else if (opcode >= 40 && opcode < 50)
				recolourOriginal[opcode - 40] = stream.readUnsignedWord();
			else if (opcode >= 50 && opcode < 60)
				recolourTarget[opcode - 50] = stream.readUnsignedWord();
			else if (opcode >= 60 && opcode < 70) {
				headModelIDs[opcode - 60] = stream.readUnsignedWord();
				/*System.out.println("IDK head model: " + headModelIDs[opcode - 60]);
				if(headModelIDs[opcode - 60] <= 15000)
					FileOperations.copy(headModelIDs[opcode - 60]);*/
			} else
				System.out.println("Error unrecognised config code: " + opcode);
		} while (true);
	}

	public int MALE_HEAD = 0, MALE_JAW = 1, MALE_TORSO = 2, MALE_ARMS = 3,
			MALE_HANDS = 4, MALE_LEGS = 5, MALE_FEET = 6, FEMALE_HEAD = 7,
			FEMALE_JAW = 8, FEMALE_TORSO = 9, FEMALE_ARMS = 10,
			FEMALE_HANFS = 11, FEMALE_LEGS = 12, FEMALE_FEET = 13;

	public boolean bodyModelIsFetched() {
		if (bodyModelIDs == null)
			return true;
		boolean flag = true;
		for (int j = 0; j < bodyModelIDs.length; j++)
			if (!Model.modelIsFetched(bodyModelIDs[j]))
				flag = false;
		return flag;
	}

	public Model fetchBodyModel() {
		if (bodyModelIDs == null)
			return null;
		Model models[] = new Model[bodyModelIDs.length];
		for (int i = 0; i < bodyModelIDs.length; i++)
			models[i] = Model.fetchModel(bodyModelIDs[i]);
		Model model;
		if (models.length == 1)
			model = models[0];
		else
			model = new Model(models.length, models);
		for (int j = 0; j < 6; j++) {
			model.recolour(recolourOriginal[j], recolourTarget[j]);
		}
		model.recolour(55232, 6798);
		return model;
	}

	public boolean headModelFetched() {
		boolean flag1 = true;
		for (int i = 0; i < 5; i++)
			if (headModelIDs[i] != -1 && !Model.modelIsFetched(headModelIDs[i]))
				flag1 = false;
		return flag1;
	}

	public Model fetchHeadModel() {
		Model models[] = new Model[5];
		int j = 0;
		for (int k = 0; k < 5; k++)
			if (headModelIDs[k] != -1)
				models[j++] = Model.fetchModel(headModelIDs[k]);
		Model model = new Model(j, models);
		for (int l = 0; l < 6; l++) {
			model.recolour(recolourOriginal[l], recolourTarget[l]);
		}
		model.recolour(55232, 6798);
		return model;
	}

	public IDK() {
		bodyPartID = -1;
		recolourOriginal = new int[6];
		recolourTarget = new int[6];
		notSelectable = false;
	}
	public static IDK cache[];
	public int bodyPartID;
	protected int[] bodyModelIDs;
	protected int[] recolourOriginal;
	protected int[] recolourTarget;
	protected final int[] headModelIDs = {
	-1, -1, -1, -1, -1 };
	public boolean notSelectable;

}