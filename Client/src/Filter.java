

final class Filter
{

	private float interpolateGain(int i, int j, float f)
	{
		float f1 = (float)lowpassSamples[i][0][j] + f * (float)(lowpassSamples[i][1][j] - lowpassSamples[i][0][j]);
			f1 *= 0.001525879F;
			return 1.0F - (float)Math.pow(10D, -f1 / 20F);
	}

	private float normalize(float octaves)
	{
		float f1 = 32.7032F * (float)Math.pow(2D, octaves);
		return (f1 * 3.141593F) / 11025F;
	}

	private float poleFreq(float f, int i, int j)
	{
		float f1 = (float)anIntArrayArrayArray666[j][0][i] + f * (float)(anIntArrayArrayArray666[j][1][i] - anIntArrayArrayArray666[j][0][i]);
		f1 *= 0.0001220703F;
		return normalize(f1);
	}

	public int method544(int i, float smoothing)
	{
		if(i == 0)
		{
			float f1 = (float)anIntArray668[0] + (float)(anIntArray668[1] - anIntArray668[0]) * smoothing;
			f1 *= 0.003051758F;
			attenuation = (float)Math.pow(0.10000000000000001D, f1 / 20F);
			inv_g0_fixedpt = (int)(attenuation * 65536F);
		}
		if(anIntArray665[i] == 0)
			return 0;
		float f2 = interpolateGain(i, 0, smoothing);
		coef[i][0] = -2F * f2 * (float)Math.cos(poleFreq(smoothing, 0, i));
		coef[i][1] = f2 * f2;
		for(int k = 1; k < anIntArray665[i]; k++)
		{
			float f3 = interpolateGain(i, k, smoothing);
			float f4 = -2F * f3 * (float)Math.cos(poleFreq(smoothing, k, i));
			float f5 = f3 * f3;
			coef[i][k * 2 + 1] = coef[i][k * 2 - 1] * f5;
			coef[i][k * 2] = coef[i][k * 2 - 1] * f4 + coef[i][k * 2 - 2] * f5;
			for(int j1 = k * 2 - 1; j1 >= 2; j1--)
				coef[i][j1] += coef[i][j1 - 1] * f4 + coef[i][j1 - 2] * f5;

			coef[i][1] += coef[i][0] * f4 + f5;
			coef[i][0] += f4;
		}

		if(i == 0)
		{
			for(int l = 0; l < anIntArray665[0] * 2; l++)
				coef[0][l] *= attenuation;

		}
		for(int i1 = 0; i1 < anIntArray665[i] * 2; i1++)
			anIntArrayArray670[i][i1] = (int)(coef[i][i1] * 65536F);

		return anIntArray665[i] * 2;
	}

	public void method545(Stream stream, Envelope class29)
	{
		int i = stream.readUnsignedByte();
		anIntArray665[0] = i >> 4;
		anIntArray665[1] = i & 0xf;
		if(i != 0)
		{
			anIntArray668[0] = stream.readUnsignedWord();
			anIntArray668[1] = stream.readUnsignedWord();
			int j = stream.readUnsignedByte();
			for(int k = 0; k < 2; k++)
			{
				for(int l = 0; l < anIntArray665[k]; l++)
				{
					anIntArrayArrayArray666[k][0][l] = stream.readUnsignedWord();
					lowpassSamples[k][0][l] = stream.readUnsignedWord();
				}

			}

			for(int i1 = 0; i1 < 2; i1++)
			{
				for(int j1 = 0; j1 < anIntArray665[i1]; j1++)
					if((j & 1 << i1 * 4 << j1) != 0)
					{
						anIntArrayArrayArray666[i1][1][j1] = stream.readUnsignedWord();
						lowpassSamples[i1][1][j1] = stream.readUnsignedWord();
					} else
					{
						anIntArrayArrayArray666[i1][1][j1] = anIntArrayArrayArray666[i1][0][j1];
						lowpassSamples[i1][1][j1] = lowpassSamples[i1][0][j1];
					}

			}

			if(j != 0 || anIntArray668[1] != anIntArray668[0])
				class29.decodeSegments(stream);
		} else
		{
			anIntArray668[0] = anIntArray668[1] = 0;
		}
	}

	public Filter()
	{
		anIntArray665 = new int[2];
		anIntArrayArrayArray666 = new int[2][2][4];
		lowpassSamples = new int[2][2][4];
		anIntArray668 = new int[2];
	}

	final int[] anIntArray665;
	private final int[][][] anIntArrayArrayArray666;
	private final int[][][] lowpassSamples;
	private final int[] anIntArray668;
	private static final float[][] coef = new float[2][8];
	static final int[][] anIntArrayArray670 = new int[2][8];
	private static float attenuation;
	static int inv_g0_fixedpt;

}
