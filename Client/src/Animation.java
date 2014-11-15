
public final class Animation {
	
    public static void unpackConfig(CacheArchive streamLoader)
    {
       Stream stream = new Stream(streamLoader.getDataForName("seq.dat"));
        int length = stream.readUnsignedWord();
        System.out.println("Loaded: " + length + " animation sequence definitions.");
        if(anims == null)
            anims = new Animation[length];
        for(int j = 0; j < length; j++)
        {
            if(anims[j] == null)
                anims[j] = new Animation();
            anims[j].readValues(stream);
        }
    }

    public int getFrameLength(int i)
    {
    	if(i > delays.length)
    		return 1;
        int j = delays[i];
        if(j == 0)
        {
            FrameReader reader = FrameReader.forID(frameIDs[i]);
            if(reader != null)
                j = delays[i] = reader.displayLength;
        }
        if(j == 0)
            j = 1;
        return j;
    }

	public void readValues(Stream stream)
	{
		do {
			int i = stream.readUnsignedByte();
			if(i == 0)
				break;
			if(i == 1) {
				frameCount = stream.readUnsignedWord();
				frameIDs = new int[frameCount];
				frameIDs2 = new int[frameCount];
				delays = new int[frameCount];
				for(int i_ = 0; i_ < frameCount; i_++){
					frameIDs[i_] = stream.readDWord();
					frameIDs2[i_] = -1;
				}
				for(int i_ = 0; i_ < frameCount; i_++)
					delays[i_] = stream.readUnsignedByte();
			}
			else if(i == 2)
				loopDelay = stream.readUnsignedWord();
			else if(i == 3) {
				int k = stream.readUnsignedByte();
				animationFlowControl = new int[k + 1];
				for(int l = 0; l < k; l++)
					animationFlowControl[l] = stream.readUnsignedByte();
				animationFlowControl[k] = 0x98967f;
			}
			else if(i == 4)
				oneSquareAnimation = true;
			else if(i == 5)
				forcedPriority = stream.readUnsignedByte();
			else if(i == 6)
				leftHandItem = stream.readUnsignedWord();
			else if(i == 7)
				rightHandItem = stream.readUnsignedWord();
			else if(i == 8)
				frameStep = stream.readUnsignedByte();
			else if(i == 9)
				resetWhenWalk = stream.readUnsignedByte();
			else if(i == 10)
				priority = stream.readUnsignedByte();
			else if(i == 11)
				delayType = stream.readUnsignedByte();
			else 
				System.out.println("Unrecognized seq.dat config code: "+i);
		} while(true);
		if(frameCount == 0)
		{
			frameCount = 1;
			frameIDs = new int[1];
			frameIDs[0] = -1;
			frameIDs2 = new int[1];
			frameIDs2[0] = -1;
			delays = new int[1];
			delays[0] = -1;
		}
		if(resetWhenWalk == -1)
			if(animationFlowControl != null)
				resetWhenWalk = 2;
			else
				resetWhenWalk = 0;
		if(priority == -1)
		{
			if(animationFlowControl != null)
			{
				priority = 2;
				return;
			}
			priority = 0;
		}
	}

    private Animation()
    {
        loopDelay = -1;
        oneSquareAnimation = false;
        forcedPriority = 5;
        leftHandItem = -1;
        rightHandItem = -1;
        frameStep = 99;
        resetWhenWalk = -1;
        priority = -1;
        delayType = 2;
    }
    public static Animation anims[];
    public int frameCount;
    public int frameIDs[];
    public int frameIDs2[];
    public int[] delays;
    public int loopDelay;
    public int animationFlowControl[];
    public boolean oneSquareAnimation;
    public int forcedPriority;
    public int leftHandItem;
    public int rightHandItem;
    public int frameStep;
    public int resetWhenWalk;
    public int priority;
    public int delayType;
    public static int anInt367;
}