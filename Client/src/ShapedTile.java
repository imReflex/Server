final class ShapedTile {

	int[] displayColor;
	public ShapedTile(int y, int colourA_, int colourC_, int l, int overlay_texture, int colourD_, int rot, int l1, int colourRGB_, int j2, int k2, int l2, int i3, int shape_, int k3, int l3, int i4, int x, int colourRGBA_, int overlay_color, int underlay_texture,
			   int underlay_color) {
		flat = !(i3 != l2 || i3 != l || i3 != k2);
		shape = shape_;
		rotation = rot;
		colourRGB = colourRGB_;
		colourRGBA = colourRGBA_;
		char c = '\200';
		int i5 = c / 2;
		int j5 = c / 4;
		int k5 = (c * 3) / 4;
		int vertexes[] = shapedTilePointData[shape_];
		int vertexCount = vertexes.length;
		origVertexX = new int[vertexCount];
		origVertexY = new int[vertexCount];
		origVertexZ = new int[vertexCount];
		int ai1[] = new int[vertexCount];
		int ai2[] = new int[vertexCount];
		int i6 = x * c;
		int j6 = y * c;
		for (int k6 = 0; k6 < vertexCount; k6++) {
			int l6 = vertexes[k6];
			if ((l6 & 1) == 0 && l6 <= 8)
				l6 = (l6 - rot - rot - 1 & 7) + 1;
			if (l6 > 8 && l6 <= 12)
				l6 = (l6 - 9 - rot & 3) + 9;
			if (l6 > 12 && l6 <= 16)
				l6 = (l6 - 13 - rot & 3) + 13;
			int i7;
			int k7;
			int i8;
			int k8;
			int j9;
			if (l6 == 1) {
				i7 = i6;
				k7 = j6;
				i8 = i3;
				k8 = l1;
				j9 = colourA_;
			} else if (l6 == 2) {
				i7 = i6 + i5;
				k7 = j6;
				i8 = i3 + l2 >> 1;
				k8 = l1 + i4 >> 1;
				j9 = colourA_ + l3 >> 1;
			} else if (l6 == 3) {
				i7 = i6 + c;
				k7 = j6;
				i8 = l2;
				k8 = i4;
				j9 = l3;
			} else if (l6 == 4) {
				i7 = i6 + c;
				k7 = j6 + i5;
				i8 = l2 + l >> 1;
				k8 = i4 + j2 >> 1;
				j9 = l3 + colourD_ >> 1;
			} else if (l6 == 5) {
				i7 = i6 + c;
				k7 = j6 + c;
				i8 = l;
				k8 = j2;
				j9 = colourD_;
			} else if (l6 == 6) {
				i7 = i6 + i5;
				k7 = j6 + c;
				i8 = l + k2 >> 1;
				k8 = j2 + colourC_ >> 1;
				j9 = colourD_ + k3 >> 1;
			} else if (l6 == 7) {
				i7 = i6;
				k7 = j6 + c;
				i8 = k2;
				k8 = colourC_;
				j9 = k3;
			} else if (l6 == 8) {
				i7 = i6;
				k7 = j6 + i5;
				i8 = k2 + i3 >> 1;
				k8 = colourC_ + l1 >> 1;
				j9 = k3 + colourA_ >> 1;
			} else if (l6 == 9) {
				i7 = i6 + i5;
				k7 = j6 + j5;
				i8 = i3 + l2 >> 1;
				k8 = l1 + i4 >> 1;
				j9 = colourA_ + l3 >> 1;
			} else if (l6 == 10) {
				i7 = i6 + k5;
				k7 = j6 + i5;
				i8 = l2 + l >> 1;
				k8 = i4 + j2 >> 1;
				j9 = l3 + colourD_ >> 1;
			} else if (l6 == 11) {
				i7 = i6 + i5;
				k7 = j6 + k5;
				i8 = l + k2 >> 1;
				k8 = j2 + colourC_ >> 1;
				j9 = colourD_ + k3 >> 1;
			} else if (l6 == 12) {
				i7 = i6 + j5;
				k7 = j6 + i5;
				i8 = k2 + i3 >> 1;
				k8 = colourC_ + l1 >> 1;
				j9 = k3 + colourA_ >> 1;
			} else if (l6 == 13) {
				i7 = i6 + j5;
				k7 = j6 + j5;
				i8 = i3;
				k8 = l1;
				j9 = colourA_;
			} else if (l6 == 14) {
				i7 = i6 + k5;
				k7 = j6 + j5;
				i8 = l2;
				k8 = i4;
				j9 = l3;
			} else if (l6 == 15) {
				i7 = i6 + k5;
				k7 = j6 + k5;
				i8 = l;
				k8 = j2;
				j9 = colourD_;
			} else {
				i7 = i6 + j5;
				k7 = j6 + k5;
				i8 = k2;
				k8 = colourC_;
				j9 = k3;
			}
			origVertexX[k6] = i7;
			origVertexY[k6] = i8;
			origVertexZ[k6] = k7;
			ai1[k6] = k8;
			ai2[k6] = j9;
		}

		int ai3[] = shapedTileElementData[shape_];
		int j7 = ai3.length / 4;
		triangleA = new int[j7];
		triangleB = new int[j7];
		triangleC = new int[j7];
		triangleHslA = new int[j7];
		triangleHslB = new int[j7];
		triangleHslC = new int[j7];
		if (overlay_texture != -1 || underlay_texture != -1 && Client.getOption("hd_tex")) {
			triangleTexture = new int[j7];
			if(Client.getOption("hd_tex"))
				displayColor = new int[j7];
		}
		int l7 = 0;
		for (int j8 = 0; j8 < j7; j8++) {
			int l8 = ai3[l7];
			int k9 = ai3[l7 + 1];
			int i10 = ai3[l7 + 2];
			int k10 = ai3[l7 + 3];
			l7 += 4;
			if (k9 < 4)
				k9 = k9 - rot & 3;
			if (i10 < 4)
				i10 = i10 - rot & 3;
			if (k10 < 4)
				k10 = k10 - rot & 3;
			triangleA[j8] = k9;
			triangleB[j8] = i10;
			triangleC[j8] = k10;
			if (l8 == 0) {
				triangleHslA[j8] = ai1[k9];
				triangleHslB[j8] = ai1[i10];
				triangleHslC[j8] = ai1[k10];
				if(!Client.getOption("hd_tex")) {
					if (triangleTexture != null)
						triangleTexture[j8] = -1;
				} else {
					if(triangleTexture != null)
						triangleTexture[j8] = underlay_texture;//-1
						
					if(displayColor != null)
						displayColor[j8] = underlay_color;
				}
			} else {
				triangleHslA[j8] = ai2[k9];
				triangleHslB[j8] = ai2[i10];
				triangleHslC[j8] = ai2[k10];
				if(Client.getOption("hd_tex")) {
					if(triangleTexture != null)
						triangleTexture[j8] = overlay_texture;
						
					if(displayColor != null)
						displayColor[j8] = overlay_color;
				} else {
					if (triangleTexture != null)
						triangleTexture[j8] = overlay_texture;
				}
			}
		}

		int i9 = i3;
		int l9 = l2;
		if (l2 < i9)
			i9 = l2;
		if (l2 > l9)
			l9 = l2;
		if (l < i9)
			i9 = l;
		if (l > l9)
			l9 = l;
		if (k2 < i9)
			i9 = k2;
		if (k2 > l9)
			l9 = k2;
		i9 /= 14;
		l9 /= 14;
	}

	final int[] origVertexX;
	final int[] origVertexY;
	final int[] origVertexZ;
	final int[] triangleHslA;
	final int[] triangleHslB;
	final int[] triangleHslC;
	final int[] triangleA;
	final int[] triangleB;
	final int[] triangleC;
	int triangleTexture[];
	final boolean flat;
	final int shape;
	final int rotation;
	final int colourRGB;
	final int colourRGBA;
	static final int[] screenX = new int[6];
	static final int[] screenY = new int[6];
	static final int[] viewSpaceX = new int[6];
	static final int[] viewSpaceY = new int[6];
	static final int[] viewSpaceZ = new int[6];
	private static final int[][] shapedTilePointData = { { 1, 3, 5, 7 }, { 1, 3, 5, 7 }, { 1, 3, 5, 7 }, { 1, 3, 5, 7, 6 }, { 1, 3, 5, 7, 6 }, { 1, 3, 5, 7, 6 }, { 1, 3, 5, 7, 6 }, { 1, 3, 5, 7, 2, 6 }, { 1, 3, 5, 7, 2, 8 }, { 1, 3, 5, 7, 2, 8 }, { 1, 3, 5, 7, 11, 12 }, { 1, 3, 5, 7, 11, 12 }, { 1, 3, 5, 7, 13, 14 } };
	private static final int[][] shapedTileElementData = { { 0, 1, 2, 3, 0, 0, 1, 3 }, { 1, 1, 2, 3, 1, 0, 1, 3 }, { 0, 1, 2, 3, 1, 0, 1, 3 }, { 0, 0, 1, 2, 0, 0, 2, 4, 1, 0, 4, 3 }, { 0, 0, 1, 4, 0, 0, 4, 3, 1, 1, 2, 4 }, { 0, 0, 4, 3, 1, 0, 1, 2, 1, 0, 2, 4 }, { 0, 1, 2, 4, 1, 0, 1, 4, 1, 0, 4, 3 }, { 0, 4, 1, 2, 0, 4, 2, 5, 1, 0, 4, 5, 1, 0, 5, 3 }, { 0, 4, 1, 2, 0, 4, 2, 3, 0, 4, 3, 5, 1, 0, 4, 5 }, { 0, 0, 4, 5, 1, 4, 1, 2, 1, 4, 2, 3, 1, 4, 3, 5 }, { 0, 0, 1, 5, 0, 1, 4, 5, 0, 1, 2, 4, 1, 0, 5, 3, 1, 5, 4, 3, 1, 4, 2, 3 }, { 1, 0, 1, 5, 1, 1, 4, 5, 1, 1, 2, 4, 0, 0, 5, 3, 0, 5, 4, 3, 0, 4, 2, 3 }, { 1, 0, 5, 4, 1, 0, 1, 5, 0, 0, 4, 3, 0, 4, 5, 3, 0, 5, 2, 3, 0, 1, 2, 5 } };

}
