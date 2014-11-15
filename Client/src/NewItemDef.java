
public class NewItemDef extends ItemDef {
	
	public static int totalItems = 0;
	public static Stream stream;
	public static int cacheIndex;
	public static NewItemDef[] cache;
	public static int[] streamIndices;
	public static MemCache spriteCache = new MemCache(100);
	public static MemCache modelCache = new MemCache(50);
	
	public static void unpackConfig(CacheArchive streamLoader) {
		stream = new Stream(streamLoader.getDataForName("obj2.dat"));
		Stream stream = new Stream(streamLoader.getDataForName("obj2.idx"));
		totalItems = stream.readUnsignedWord();
		System.out.println("Loaded: " + totalItems + " *new* item definitions.");
		streamIndices = new int[totalItems + 1000];
		int i = 2;
		for (int j = 0; j < totalItems; j++) {
			streamIndices[j] = i;
			i += stream.readUnsignedWord();
		}
		cache = new NewItemDef[10];
		for (int k = 0; k < 10; k++)
			cache[k] = new NewItemDef();
	}
	
	@Override
	public void readValues(Stream stream) {
		try {
		do {
			int i = stream.readUnsignedByte();
			if (i == 0)
				return;
			if (i == 1) {
				modelID = stream.readInt();
				//FileOperations.copy(modelID);
			} else if (i == 2)
				name = stream.readString();
			else if (i == 3)
				description = stream.readString();
			else if (i == 4)
				modelZoom = stream.readUnsignedWord();
			else if (i == 5)
				rotationY = stream.readUnsignedWord();
			else if (i == 6)
				rotationX = stream.readUnsignedWord();
			else if (i == 7) {
				modelOffset1 = stream.readUnsignedWord();
				if (modelOffset1 > 32767)
					modelOffset1 -= 0x10000;
			} else if (i == 8) {
				modelOffsetY = stream.readUnsignedWord();
				if (modelOffsetY > 32767)
					modelOffsetY -= 0x10000;
			} else if (i == 10)
				stream.readUnsignedWord();
			else if (i == 11)
				stackable = true;
			else if (i == 12)
				value = stream.readInt();
			else if (i == 16)
				membersObject = true;
			else if (i == 23) {
				maleEquip1 = stream.readInt();
				//FileOperations.copy(maleEquip1);
				maleYOffset = stream.readSignedByte();
			} else if (i == 24) {
				maleEquip2 = stream.readInt();
				//FileOperations.copy(maleEquip2);
			} else if (i == 25) {
				femaleEquip1 = stream.readInt();
				//FileOperations.copy(femaleEquip1);
				femaleYOffset = stream.readSignedByte();
			} else if (i == 26) {
				femaleEquip2 = stream.readInt();
				//FileOperations.copy(femaleEquip2);
			} else if (i >= 30 && i < 35) {
				if (groundActions == null)
					groundActions = new String[5];
				groundActions[i - 30] = stream.readString();
				if (groundActions[i - 30].equalsIgnoreCase("hidden"))
					groundActions[i - 30] = null;
			} else if (i >= 35 && i < 40) {
				if (actions == null)
					actions = new String[5];
				actions[i - 35] = stream.readString();
				if (actions[i - 35].equalsIgnoreCase("null"))
					actions[i - 35] = null;
			} else if (i == 40) {
				int j = stream.readUnsignedByte();
				editedModelColor = new int[j];
				newModelColor = new int[j];
				for (int k = 0; k < j; k++) {
					editedModelColor[k] = stream.readUnsignedWord();
					newModelColor[k] = stream.readUnsignedWord();
				}
			} else if (i == 78) {
				maleEquip3 = stream.readInt();
				//FileOperations.copy(maleEquip3);
			} else if (i == 79) {
				femaleEquip3 = stream.readInt();
				//FileOperations.copy(femaleEquip3);
			} else if (i == 90) {
				maleDialogue = stream.readInt();
				//FileOperations.copy(maleDialogue);
			} else if (i == 91) { 
				femaleDialogue = stream.readInt();
				//FileOperations.copy(femaleDialogue);
			} else if (i == 92) {
				maleDialogueModel = stream.readInt();
				//FileOperations.copy(maleDialogueModel);
			} else if (i == 93) {
				femaleDialogueModel = stream.readInt();
				//FileOperations.copy(femaleDialogueModel);
			} else if (i == 95)
				modelOffsetX = stream.readUnsignedWord();
			else if (i == 97)
				certID = stream.readUnsignedWord();
			else if (i == 98)
				certTemplateID = stream.readUnsignedWord();
			else if (i >= 100 && i < 110) {
				if (stackIDs == null) {
					stackIDs = new int[10];
					stackAmounts = new int[10];
				}
				stackIDs[i - 100] = stream.readUnsignedWord();
				stackAmounts[i - 100] = stream.readUnsignedWord();
			} else if (i == 110)
				sizeX = stream.readUnsignedWord();
			else if (i == 111)
				sizeY = stream.readUnsignedWord();
			else if (i == 112)
				sizeZ = stream.readUnsignedWord();
			else if (i == 113)
				shadow = stream.readSignedByte();
			else if (i == 114)
				lightness = stream.readSignedByte() * 5;
			else if (i == 115)
				team = stream.readUnsignedByte();
			else if (i == 116)
				lendID = stream.readUnsignedWord();
			else if (i == 117)
				lentItemID = stream.readUnsignedWord();
		} while (true);
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static NewItemDef forID(int i) {
		for (int j = 0; j < 10; j++)
			if (cache[j].ID == i)
				return cache[j];
		cacheIndex = (cacheIndex + 1) % 10;
		NewItemDef itemDef = cache[cacheIndex];
		if(i >= streamIndices.length)
		{
			itemDef.ID = 1;
			itemDef.setDefaults();
			return itemDef;
		}
		stream.currentOffset = streamIndices[i];
		itemDef.ID = i;
		itemDef.setDefaults();
		itemDef.readValues(stream);
		if (itemDef.certTemplateID != -1)
			itemDef.toNote();
		if (itemDef.lentItemID != -1)
			itemDef.toLend();
		if (!isMembers && itemDef.membersObject) {
			itemDef.name = "Members Object";
			itemDef.description = "Login to a members' server to use this object.";
			itemDef.groundActions = null;
			itemDef.actions = null;
			itemDef.team = 0;
		}
		/*for (int blackItems : Configuration.ITEMS_WITH_BLACK) {
			if (itemDef.ID == blackItems) {
				if (itemDef.editedModelColor != null) {
					int[] oldc = itemDef.editedModelColor;
					int[] newc = itemDef.newModelColor;
					itemDef.editedModelColor = new int[oldc.length + 1];
					itemDef.newModelColor = new int[newc.length + 1];
					for (int index = 0; index < itemDef.newModelColor.length; index++) {
						if (index < itemDef.newModelColor.length - 1) {
							itemDef.editedModelColor[index] = oldc[index];
							itemDef.newModelColor[index] = newc[index];
						} else {
							itemDef.editedModelColor[index] = 0;
							itemDef.newModelColor[index] = 1;
						}
					}
				} else {
					itemDef.editedModelColor = new int[1];
					itemDef.newModelColor = new int[1];
					itemDef.editedModelColor[0] = 0;
					itemDef.newModelColor[0] = 1;
				}
			}
		}*/
		return itemDef;
	}
	
	@Override
	public void toNote() {
		NewItemDef itemDef = forID(certTemplateID);
		modelID = itemDef.modelID;
		modelZoom = itemDef.modelZoom;
		rotationY = itemDef.rotationY;
		rotationX = itemDef.rotationX;
		modelOffsetX = itemDef.modelOffsetX;
		modelOffset1 = itemDef.modelOffset1;
		modelOffsetY = itemDef.modelOffsetY;
		editedModelColor = itemDef.editedModelColor;
		newModelColor = itemDef.newModelColor;
		NewItemDef itemDef_1 = forID(certID);
		name = itemDef_1.name;
		membersObject = itemDef_1.membersObject;
		value = itemDef_1.value;
		String s = "a";
		char c = itemDef_1.name.charAt(0);
		if (c == 'A' || c == 'E' || c == 'I' || c == 'O' || c == 'U')
			s = "an";
		description = ("Swap this note at any bank for " + s + " " + itemDef_1.name + ".");
		stackable = true;
	}

	@Override
	protected void toLend() {
		NewItemDef itemDef = forID(lentItemID);
		actions = new String[5];
		modelID = itemDef.modelID;
		modelOffset1 = itemDef.modelOffset1;
		rotationX = itemDef.rotationX;
		modelOffsetY = itemDef.modelOffsetY;
		modelZoom = itemDef.modelZoom;
		rotationY = itemDef.rotationY;
		modelOffsetX = itemDef.modelOffsetX;
		value = 0;
		NewItemDef itemDef_1 = forID(lendID);
		maleDialogueModel = itemDef_1.maleDialogueModel;
		editedModelColor = itemDef_1.editedModelColor;
		maleEquip3 = itemDef_1.maleEquip3;
		maleEquip2 = itemDef_1.maleEquip2;
		femaleDialogueModel = itemDef_1.femaleDialogueModel;
		maleDialogue = itemDef_1.maleDialogue;
		groundActions = itemDef_1.groundActions;
		maleEquip1 = itemDef_1.maleEquip1;
		name = itemDef_1.name;
		femaleEquip1 = itemDef_1.femaleEquip1;
		membersObject = itemDef_1.membersObject;
		femaleDialogue = itemDef_1.femaleDialogue;
		femaleEquip2 = itemDef_1.femaleEquip2;
		femaleEquip3 = itemDef_1.femaleEquip3;
		newModelColor = itemDef_1.newModelColor;
		team = itemDef_1.team;
		if (itemDef_1.actions != null) {
			for (int i_33_ = 0; i_33_ < 4; i_33_++)
				actions[i_33_] = itemDef_1.actions[i_33_];
		}
		actions[4] = "Discard";
	}
	
	public boolean dialogueModelFetched(int j) {
		int k = maleDialogue;
		int l = maleDialogueModel;
		if (j == 1) {
			k = femaleDialogue;
			l = femaleDialogueModel;
		}
		if (k == -1)
			return true;
		boolean flag = true;
		if (!Model.modelIsFetched(k))
			flag = false;
		if (l != -1 && !Model.modelIsFetched(l))
			flag = false;
		return flag;
	}

	public Model getDialogueModel(int gender) {
		int k = maleDialogue;
		int l = maleDialogueModel;
		if (gender == 1) {
			k = femaleDialogue;
			l = femaleDialogueModel;
		}
		if (k == -1)
			return null;
		Model model = Model.fetchModel(k);
		if (l != -1) {
			Model model_1 = Model.fetchModel(l);
			Model models[] = { model, model_1 };
			model = new Model(2, models);
		}
		if (editedModelColor != null) {
			for (int i1 = 0; i1 < editedModelColor.length; i1++)
				model.recolour(editedModelColor[i1], newModelColor[i1]);
		}
		return model;
	}

	public boolean equipModelFetched(int gender) {
		int fistModel = maleEquip1;
		int secondModel = maleEquip2;
		int thirdModel = maleEquip3;
		if (gender == 1) {
			fistModel = femaleEquip1;
			secondModel = femaleEquip2;
			thirdModel = femaleEquip3;
		}
		if (fistModel == -1)
			return true;
		boolean flag = true;
		if (!Model.modelIsFetched(fistModel))
			flag = false;
		if (secondModel != -1 && !Model.modelIsFetched(secondModel))
			flag = false;
		if (thirdModel != -1 && !Model.modelIsFetched(thirdModel))
			flag = false;
		return flag;
	}

	public Model getEquipModel(int gender) {
		int j = maleEquip1;
		int k = maleEquip2;
		int l = maleEquip3;
		if (gender == 1) {
			j = femaleEquip1;
			k = femaleEquip2;
			l = femaleEquip3;
		}
		if (j == -1)
			return null;
		Model model = Model.fetchModel(j);
		if (k != -1)
			if (l != -1) {
				Model model_1 = Model.fetchModel(k);
				Model model_3 = Model.fetchModel(l);
				Model model_1s[] = { model, model_1, model_3 };
				model = new Model(3, model_1s);
			} else {
				Model model_2 = Model.fetchModel(k);
				Model models[] = { model, model_2 };
				model = new Model(2, models);
			}
		if (gender == 0 && maleYOffset != 0)
			model.translate(0, maleYOffset, 0);
		if (gender == 1 && femaleYOffset != 0)
			model.translate(0, femaleYOffset, 0);
		if (editedModelColor != null) {
			for (int i1 = 0; i1 < editedModelColor.length; i1++)
				model.recolour(editedModelColor[i1], newModelColor[i1]);
		}
		return model;
	}
	
	public static Sprite getSprite(int i, int j, int k, int zoom) {
		if (k == 0 && zoom != -1) {
			Sprite sprite = (Sprite) spriteCache.get(i);
			if (sprite != null && sprite.maxHeight != j && sprite.maxHeight != -1) {
				sprite.unlink();
				sprite = null;
			}
			if (sprite != null)
				return sprite;
		}
		NewItemDef itemDef = forID(i);
		if (itemDef.stackIDs == null)
			j = -1;
		if (j > 1) {
			int i1 = -1;
			for (int j1 = 0; j1 < 10; j1++)
				if (j >= itemDef.stackAmounts[j1] && itemDef.stackAmounts[j1] != 0)
					i1 = itemDef.stackIDs[j1];

			if (i1 != -1)
				itemDef = forID(i1);
		}
		Model model = itemDef.getItemModelFinalised(1);
		if (model == null)
			return null;
		Sprite sprite = null;
		if (itemDef.certTemplateID != -1) {
			sprite = getSprite(itemDef.certID, 10, -1);
			if (sprite == null)
				return null;
		}
		if (itemDef.lendID != -1) {
			sprite = getSprite(itemDef.lendID, 50, 0);
			if (sprite == null)
				return null;
		}
		Sprite sprite2 = new Sprite(32, 32);
		int k1 = Rasterizer.center_x;
		int l1 = Rasterizer.center_y;
		int ai[] = Rasterizer.lineOffsets;
		int ai1[] = DrawingArea.pixels;
		int i2 = DrawingArea.width;
		int j2 = DrawingArea.height;
		int k2 = DrawingArea.topX;
		int l2 = DrawingArea.bottomX;
		int i3 = DrawingArea.topY;
		int j3 = DrawingArea.bottomY;
		Rasterizer.notTextured = false;
		DrawingArea.initDrawingArea(32, 32, sprite2.myPixels);
		DrawingArea.drawPixels(32, 0, 0, 0, 32);
		Rasterizer.setDefaultBounds();
		int k3 = itemDef.modelZoom;
		if (zoom != -1 && zoom != 0)
			k3 = (itemDef.modelZoom * 100) / zoom;
		if (k == -1)
			k3 = (int) ((double) k3 * 1.5D);
		if (k > 0)
			k3 = (int) ((double) k3 * 1.04D);
		int l3 = Rasterizer.SINE[itemDef.rotationY] * k3 >> 16;
		int i4 = Rasterizer.COSINE[itemDef.rotationY] * k3 >> 16;
		model.renderSingle(itemDef.rotationX, itemDef.modelOffsetX, itemDef.rotationY, itemDef.modelOffset1, l3 + model.modelHeight / 2 + itemDef.modelOffsetY, i4 + itemDef.modelOffsetY);
		for (int i5 = 31; i5 >= 0; i5--) {
			for (int j4 = 31; j4 >= 0; j4--) {
				if (sprite2.myPixels[i5 + j4 * 32] != 0)
					continue;
				if (i5 > 0 && sprite2.myPixels[(i5 - 1) + j4 * 32] > 1) {
					sprite2.myPixels[i5 + j4 * 32] = 1;
					continue;
				}
				if (j4 > 0 && sprite2.myPixels[i5 + (j4 - 1) * 32] > 1) {
					sprite2.myPixels[i5 + j4 * 32] = 1;
					continue;
				}
				if (i5 < 31 && sprite2.myPixels[i5 + 1 + j4 * 32] > 1) {
					sprite2.myPixels[i5 + j4 * 32] = 1;
					continue;
				}
				if (j4 < 31 && sprite2.myPixels[i5 + (j4 + 1) * 32] > 1)
					sprite2.myPixels[i5 + j4 * 32] = 1;
			}

		}

		if (k > 0) {
			for (int j5 = 31; j5 >= 0; j5--) {
				for (int k4 = 31; k4 >= 0; k4--) {
					if (sprite2.myPixels[j5 + k4 * 32] != 0)
						continue;
					if (j5 > 0 && sprite2.myPixels[(j5 - 1) + k4 * 32] == 1) {
						sprite2.myPixels[j5 + k4 * 32] = k;
						continue;
					}
					if (k4 > 0 && sprite2.myPixels[j5 + (k4 - 1) * 32] == 1) {
						sprite2.myPixels[j5 + k4 * 32] = k;
						continue;
					}
					if (j5 < 31 && sprite2.myPixels[j5 + 1 + k4 * 32] == 1) {
						sprite2.myPixels[j5 + k4 * 32] = k;
						continue;
					}
					if (k4 < 31 && sprite2.myPixels[j5 + (k4 + 1) * 32] == 1)
						sprite2.myPixels[j5 + k4 * 32] = k;
				}

			}

		} else if (k == 0) {
			for (int k5 = 31; k5 >= 0; k5--) {
				for (int l4 = 31; l4 >= 0; l4--)
					if (sprite2.myPixels[k5 + l4 * 32] == 0 && k5 > 0 && l4 > 0 && sprite2.myPixels[(k5 - 1) + (l4 - 1) * 32] > 0)
						sprite2.myPixels[k5 + l4 * 32] = 0x302020;

			}

		}
		if (itemDef.certTemplateID != -1) {
			int l5 = sprite.maxWidth;
			int j6 = sprite.maxHeight;
			sprite.maxWidth = 32;
			sprite.maxHeight = 32;
			sprite.drawSprite(0, 0);
			sprite.maxWidth = l5;
			sprite.maxHeight = j6;
		}
		if (itemDef.lendID != -1) {
			int l5 = sprite.maxWidth;
			int j6 = sprite.maxHeight;
			sprite.maxWidth = 32;
			sprite.maxHeight = 32;
			sprite.drawSprite(0, 0);
			sprite.maxWidth = l5;
			sprite.maxHeight = j6;
		}
		if (k == 0)
			spriteCache.put(sprite2, i);
		DrawingArea.initDrawingArea(j2, i2, ai1);
		DrawingArea.setDrawingArea(j3, k2, l2, i3);
		Rasterizer.center_x = k1;
		Rasterizer.center_y = l1;
		Rasterizer.lineOffsets = ai;
		Rasterizer.notTextured = true;
		sprite2.maxWidth = itemDef.stackable ? 33 : 32;
		sprite2.maxHeight = j;
		return sprite2;
	}

	public static Sprite getSprite(int i, int j, int k) {
		if (k == 0) {
			Sprite sprite = (Sprite) spriteCache.get(i);
			if (sprite != null && sprite.maxHeight != j && sprite.maxHeight != -1) {
				sprite.unlink();
				sprite = null;
			}
			if (sprite != null)
				return sprite;
		}
		NewItemDef itemDef = forID(i);
		if (itemDef.stackIDs == null)
			j = -1;
		if (j > 1) {
			int i1 = -1;
			for (int j1 = 0; j1 < 10; j1++)
				if (j >= itemDef.stackAmounts[j1] && itemDef.stackAmounts[j1] != 0)
					i1 = itemDef.stackIDs[j1];
			if (i1 != -1)
				itemDef = forID(i1);
		}
		Model model = itemDef.getItemModelFinalised(1);
		if (model == null)
			return null;
		Sprite sprite = null;
		if (itemDef.certTemplateID != -1) {
			sprite = getSprite(itemDef.certID, 10, -1);
			if (sprite == null)
				return null;
		}
		if (itemDef.lentItemID != -1) {
			sprite = getSprite(itemDef.lendID, 50, 0);
			if (sprite == null)
				return null;
		}
		Sprite sprite2 = new Sprite(32, 32);
		int k1 = Rasterizer.center_x;
		int l1 = Rasterizer.center_y;
		int ai[] = Rasterizer.lineOffsets;
		int ai1[] = DrawingArea.pixels;
		int i2 = DrawingArea.width;
		int j2 = DrawingArea.height;
		int k2 = DrawingArea.topX;
		int l2 = DrawingArea.bottomX;
		int i3 = DrawingArea.topY;
		int j3 = DrawingArea.bottomY;
		Rasterizer.notTextured = false;
		DrawingArea.initDrawingArea(32, 32, sprite2.myPixels);
		DrawingArea.drawPixels(32, 0, 0, 0, 32);
		Rasterizer.setDefaultBounds();
		int k3 = itemDef.modelZoom;
		if (k == -1)
			k3 = (int) ((double) k3 * 1.5D);
		if (k > 0)
			k3 = (int) ((double) k3 * 1.04D);
		int l3 = Rasterizer.SINE[itemDef.rotationY] * k3 >> 16;
		int i4 = Rasterizer.COSINE[itemDef.rotationY] * k3 >> 16;
		model.renderSingle(itemDef.rotationX, itemDef.modelOffsetX, itemDef.rotationY, itemDef.modelOffset1, l3 + model.modelHeight / 2 + itemDef.modelOffsetY, i4 + itemDef.modelOffsetY);
		for (int i5 = 31; i5 >= 0; i5--) {
			for (int j4 = 31; j4 >= 0; j4--)
				if (sprite2.myPixels[i5 + j4 * 32] == 0)
					if (i5 > 0 && sprite2.myPixels[(i5 - 1) + j4 * 32] > 1)
						sprite2.myPixels[i5 + j4 * 32] = 1;
					else if (j4 > 0 && sprite2.myPixels[i5 + (j4 - 1) * 32] > 1)
						sprite2.myPixels[i5 + j4 * 32] = 1;
					else if (i5 < 31 && sprite2.myPixels[i5 + 1 + j4 * 32] > 1)
						sprite2.myPixels[i5 + j4 * 32] = 1;
					else if (j4 < 31 && sprite2.myPixels[i5 + (j4 + 1) * 32] > 1)
						sprite2.myPixels[i5 + j4 * 32] = 1;
		}
		if (k > 0) {
			for (int j5 = 31; j5 >= 0; j5--) {
				for (int k4 = 31; k4 >= 0; k4--)
					if (sprite2.myPixels[j5 + k4 * 32] == 0)
						if (j5 > 0 && sprite2.myPixels[(j5 - 1) + k4 * 32] == 1)
							sprite2.myPixels[j5 + k4 * 32] = k;
						else if (k4 > 0 && sprite2.myPixels[j5 + (k4 - 1) * 32] == 1)
							sprite2.myPixels[j5 + k4 * 32] = k;
						else if (j5 < 31 && sprite2.myPixels[j5 + 1 + k4 * 32] == 1)
							sprite2.myPixels[j5 + k4 * 32] = k;
						else if (k4 < 31 && sprite2.myPixels[j5 + (k4 + 1) * 32] == 1)
							sprite2.myPixels[j5 + k4 * 32] = k;
			}
		} else if (k == 0) {
			for (int k5 = 31; k5 >= 0; k5--) {
				for (int l4 = 31; l4 >= 0; l4--)
					if (sprite2.myPixels[k5 + l4 * 32] == 0 && k5 > 0 && l4 > 0 && sprite2.myPixels[(k5 - 1) + (l4 - 1) * 32] > 0)
						sprite2.myPixels[k5 + l4 * 32] = 0x302020;
			}
		}
		if (itemDef.certTemplateID != -1) {
			int l5 = sprite.maxWidth;
			int j6 = sprite.maxHeight;
			sprite.maxWidth = 32;
			sprite.maxHeight = 32;
			sprite.drawSprite(0, 0);
			sprite.maxWidth = l5;
			sprite.maxHeight = j6;
		}
		if (itemDef.lentItemID != -1) {
			int l5 = sprite.maxWidth;
			int j6 = sprite.maxHeight;
			sprite.maxWidth = 32;
			sprite.maxHeight = 32;
			sprite.drawSprite(0, 0);
			sprite.maxWidth = l5;
			sprite.maxHeight = j6;
		}
		if (k == 0)
			spriteCache.put(sprite2, i);
		DrawingArea.initDrawingArea(j2, i2, ai1);
		DrawingArea.setDrawingArea(j3, k2, l2, i3);
		Rasterizer.center_x = k1;
		Rasterizer.center_y = l1;
		Rasterizer.lineOffsets = ai;
		Rasterizer.notTextured = true;
		if (itemDef.stackable)
			sprite2.maxWidth = 33;
		else
			sprite2.maxWidth = 32;
		sprite2.maxHeight = j;
		return sprite2;
	}

	public Model getItemModelFinalised(int amount) {
		if (stackIDs != null && amount > 1) {
			int stackId = -1;
			for (int k = 0; k < 10; k++)
				if (amount >= stackAmounts[k] && stackAmounts[k] != 0)
					stackId = stackIDs[k];
			if (stackId != -1)
				return forID(stackId).getItemModelFinalised(1);
		}
		Model model = (Model) modelCache.get(ID);
		if (model != null)
			return model;
		model = Model.fetchModel(modelID);
		if (model == null)
			return null;
		if (sizeX != 128 || sizeY != 128 || sizeZ != 128)
			model.scaleT(sizeX, sizeZ, sizeY);
		if (editedModelColor != null) {
			for (int l = 0; l < editedModelColor.length; l++)
				model.recolour(editedModelColor[l], newModelColor[l]);
		}
		model.light(64 + shadow, 768 + lightness, -50, -10, -50, true);
		model.rendersWithinOneTile = true;
		modelCache.put(model, ID);
		return model;
	}

	public Model getItemModel(int i) {
		if (stackIDs != null && i > 1) {
			int j = -1;
			for (int k = 0; k < 10; k++)
				if (i >= stackAmounts[k] && stackAmounts[k] != 0)
					j = stackIDs[k];
			if (j != -1)
				return forID(j).getItemModel(1);
		}
		Model model = Model.fetchModel(modelID);
		if (model == null)
			return null;
		if (editedModelColor != null) {
			for (int l = 0; l < editedModelColor.length; l++)
				model.recolour(editedModelColor[l], newModelColor[l]);
		}
		return model;
	}
	
}