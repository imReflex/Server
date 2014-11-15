


final class PlainTile
{

	public PlainTile(int colA, int colB, int colD, int colC, int texId, int rgbCol, boolean flat, int color)
	{
		isFlat = true;
		colourA = colA;
		colourB = colB;
		colourD = colD;
		colourC = colC;
		textureId = texId;
		rgbColour = rgbCol;
		isFlat = flat;
		this.color = color;
	}

	final int colourA;
	final int colourB;
	final int colourD;
	final int colourC;
	final int textureId;
	boolean isFlat;
	final int rgbColour;
	final int color;
}
