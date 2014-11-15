

final class StillGraphic extends Animable {

	public StillGraphic(int i, int j, int l, int gfxId, int height, int k1,
						 int l1)
	{
		animFinished = false;
		gfx = SpotAnim.cache[gfxId];
		plane = i;
		xTile = l1;
		yTile = k1;
		drawHeight = height;
		startTime = j + l;
		animFinished = false;
	}

	public int frontLight = 84;//68
	public int backLight = 5050;//820
	public int rightLight = -90;//0
	public int middleLight = -580; // Cannot be 0 [-1]
	public int leftLight = -90;//0
	
	public Model getRotatedModel()
	{
		Model model = gfx.getModel();
		if(model == null)
			return null;
		int frameToPlay = gfx.animation.frameIDs[currentFrame];
		int nextFrame = gfx.animation.frameIDs[this.next_animation_frame];
		int cycle1 = gfx.animation.delays[this.next_animation_frame];
		int cycle2 = tick;
		Model animableModel = new Model(true, FrameReader.isNullFrame(frameToPlay), false, model);
		if(!animFinished)
		{
			animableModel.createBones();
			if(this.next_animation_frame >= 0)
				animableModel.applyTransform(frameToPlay, nextFrame, cycle1, cycle2);
			else
				animableModel.applyTransform(frameToPlay);
			animableModel.triangleSkin = null;
			animableModel.vertexSkin = null;
		}
		if(gfx.sizeXY != 128 || gfx.sizeZ != 128)
			animableModel.scaleT(gfx.sizeXY, gfx.sizeXY, gfx.sizeZ);
		if(gfx.rotation != 0)
		{
			if(gfx.rotation == 90)
				animableModel.rotateBy90();
			if(gfx.rotation == 180)
			{
				animableModel.rotateBy90();
				animableModel.rotateBy90();
			}
			if(gfx.rotation == 270)
			{
				animableModel.rotateBy90();
				animableModel.rotateBy90();
				animableModel.rotateBy90();
			}
		}
		animableModel.light(frontLight + gfx.shadow, backLight + gfx.lightness, rightLight, middleLight, leftLight, true);
		return animableModel;
	}

	public void processAnimation(int cycle)
	{
		for(tick += cycle; tick > gfx.animation.getFrameLength(currentFrame);)
		{
			tick -= gfx.animation.getFrameLength(currentFrame) + 1;
			currentFrame++;
			if(currentFrame >= gfx.animation.frameCount && (currentFrame < 0 || currentFrame >= gfx.animation.frameCount))
			{
				currentFrame = 0;
				animFinished = true;
			}
			this.next_animation_frame = currentFrame + 1;
			if(this.next_animation_frame >= gfx.animation.frameCount) {
				this.next_animation_frame = currentFrame;
			}
		}

	}

	public final int plane;
	public final int xTile;
	public final int yTile;
	public final int drawHeight;
	public final int startTime;
	public boolean animFinished;
	private final SpotAnim gfx;
	private int currentFrame;
	private int tick;
}
