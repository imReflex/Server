final class ObjectOnTile extends Animable {

	public Model getRotatedModel()
	{
		int anim = -1;
		if(animation != null)
		{
			int currentTime = Client.loopCycle - nextFrameTime;
			if(currentTime > 100 && animation.loopDelay > 0)
				currentTime = 100;
			while(currentTime > animation.getFrameLength(frame))
			{
				//up frame
				currentTime -= animation.getFrameLength(frame);
				frame++;
				if(frame < animation.frameCount)
					continue;
				//reset frame
				frame -= animation.loopDelay;
				if(frame >= 0 && frame < animation.frameCount)
					continue;
				animation = null;
				break;
			}
			nextFrameTime = Client.loopCycle - currentTime;
			if(animation != null)
				anim = animation.frameIDs[frame];
		}
		ObjectDef objectDef;
		if(objectConfigs != null)
			objectDef = getObjectForVarbitConfig();
		else
			objectDef = ObjectDef.forID(objectId);
		if(objectDef == null)
		{
			return null;
		} else
		{
			return objectDef.renderObject(type, orientation, a_y, b_y, d_y, c_y, animation, anim);
		}
	}

	private ObjectDef getObjectForVarbitConfig()
	{
		try {
		int i = -1;
		if(varbitIndex != -1)
		{
			VarBit varBit = VarBit.cache[varbitIndex];
			int k = varBit.configId;
			int l = varBit.leastSignificantBit;
			int i1 = varBit.mostSignificantBit;
			int j1 = Client.bit_mask[i1 - l];
			i = clientInstance.variousSettings[k] >> l & j1;
		} else
		if(configId != -1)
			i = clientInstance.variousSettings[configId];
		if(i < 0 || i >= objectConfigs.length || objectConfigs[i] == -1)
			return null;
		else
			return ObjectDef.forID(objectConfigs[i]);
		} catch(ArrayIndexOutOfBoundsException e) {return null;}
	}
	
	

	public ObjectOnTile(int i, int j, int k, int l, int i1, int j1,
						 int k1, int anim, boolean randomizeAnimStart)
	{
		objectId = i;
		type = k;
		orientation = j;
		a_y = j1;
		b_y = l;
		d_y = i1;
		c_y = k1;
		if(anim != -1)
		{
			animation = Animation.anims[anim];
			frame = 0;
			nextFrameTime = Client.loopCycle;
			if(randomizeAnimStart && animation.loopDelay != -1)
			{
				frame = (int)(Math.random() * (double) animation.frameCount);
				nextFrameTime -= (int)(Math.random() * (double) animation.getFrameLength(frame));
			}
		}
		ObjectDef object = ObjectDef.forID(objectId);
		varbitIndex = object.varbitIndex;
		configId = object.configID;
		objectConfigs = object.configObjectIDs;
	}

	private int frame;
	private final int[] objectConfigs;
	private final int varbitIndex;
	private final int configId;
	private final int a_y;
	private final int b_y;
	private final int d_y;
	private final int c_y;
	private Animation animation;
	private int nextFrameTime;
	public static Client clientInstance;
	private final int objectId;
	private final int type;
	private final int orientation;
}
