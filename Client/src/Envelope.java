

final class Envelope
{

	public void decode(Stream stream)
	{
		form = stream.readUnsignedByte();
			anInt538 = stream.readDWord();
			anInt539 = stream.readDWord();
			decodeSegments(stream);
	}

	public void decodeSegments(Stream stream)
	{
		segmentCount = stream.readUnsignedByte();
		segmentDuration = new int[segmentCount];
		segmentPeak = new int[segmentCount];
		for(int i = 0; i < segmentCount; i++)
		{
			segmentDuration[i] = stream.readUnsignedWord();
			segmentPeak[i] = stream.readUnsignedWord();
		}

	}

	void resetValues()
	{
		checkpoint = 0;
		segmentPtr = 0;
		step = 0;
		amplitude = 0;
		tick = 0;
	}

	int evaluate(int i)
	{
		if(tick >= checkpoint)
		{
			amplitude = segmentPeak[segmentPtr++] << 15;
			if(segmentPtr >= segmentCount)
				segmentPtr = segmentCount - 1;
			checkpoint = (int)(((double)segmentDuration[segmentPtr] / 65536D) * (double)i);
			if(checkpoint > tick)
				step = ((segmentPeak[segmentPtr] << 15) - amplitude) / (checkpoint - tick);
		}
		amplitude += step;
		tick++;
		return amplitude - step >> 15;
	}

	public Envelope()
	{
	}

	private int segmentCount;
	private int[] segmentDuration;
	private int[] segmentPeak;
	int anInt538;
	int anInt539;
	int form;
	private int checkpoint;
	private int segmentPtr;
	private int step;
	private int amplitude;
	private int tick;
	//public static int anInt546;
}
