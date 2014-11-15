

public final class NPC extends Entity
{
	
	public int frontLight = 84;//68
	public int backLight = 1000;//820
	public int rightLight = -90;//0
	public int middleLight = -580; // Cannot be 0 [-1]
	public int leftLight = -90;//0

	private Model getAnimatedModel()
	{
		if(super.anim >= 0 && super.animationDelay == 0)
		{
			int currenftFrame = Animation.anims[super.anim].frameIDs[super.currentAnimFrame];
			int nextFrame = Animation.anims[super.anim].frameIDs[super.next_animation_frame];
			int cycle1 = Animation.anims[super.anim].delays[super.currentAnimFrame];
			int cycle2 = super.frameCycle;
			int i1 = -1;
			if(super.forcedAnimId >= 0 && super.forcedAnimId != super.standAnimIndex)
				i1 = Animation.anims[super.forcedAnimId].frameIDs[super.currentForcedAnimFrame];
			return desc.getAnimatedModel(i1, currenftFrame, Animation.anims[super.anim].animationFlowControl, nextFrame, cycle1, cycle2);
		}
		int l = -1;
		if(super.forcedAnimId >= 0)
			l = Animation.anims[super.forcedAnimId].frameIDs[super.currentForcedAnimFrame];
		return desc.getAnimatedModel(-1, l, null, -1, 0, 0);
	}

	public Model getRotatedModel()
	{
		if(desc == null)
			return null;
		Model model = getAnimatedModel();
		if(model == null)
			return null;
		super.height = model.modelHeight;
		if(super.gfxId != -1 && super.currentAnim != -1)
		{
			SpotAnim spotAnim = SpotAnim.cache[super.gfxId];
			Model model_1 = spotAnim.getModel();
			if(model_1 != null)
			{
				int currentFrameID = spotAnim.animation.frameIDs[super.currentAnim];
				int nextFrameID = spotAnim.animation.frameIDs[super.next_animation_frame];
				int cycle1 = spotAnim.animation.delays[super.currentAnim];
				int cycle2 = super.animCycle;
				Model model_2 = new Model(true, FrameReader.isNullFrame(currentFrameID), false, model_1);
				model_2.translate(0, -super.graphicHeight, 0);
				model_2.createBones();
				if(Client.getOption("tweening"))
					model_2.applyTransform(currentFrameID, nextFrameID, cycle1, cycle2);
				else
					model_2.applyTransform(currentFrameID);
				model_2.triangleSkin = null;
				model_2.vertexSkin = null;
				if(spotAnim.sizeXY != 128 || spotAnim.sizeZ != 128)
					model_2.scaleT(spotAnim.sizeXY, spotAnim.sizeXY, spotAnim.sizeZ);
				model_2.light(frontLight + spotAnim.shadow, backLight + spotAnim.lightness, rightLight, middleLight, leftLight, true);
				Model aModel[] = {
						model, model_2
				};
				model = new Model(aModel);
			}
		}
		if(desc.squaresNeeded == 1)
			model.rendersWithinOneTile = true;
		return model;
	}

	public boolean isVisible()
	{
		return desc != null;
	}

	NPC()
	{
	}

	public NPCDef desc;
}
