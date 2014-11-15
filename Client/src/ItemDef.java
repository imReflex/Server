


public class ItemDef {

	public static void nullLoader() {
		modelCache = null;
		spriteCache = null;
		streamIndices = null;
		cache = null;
		stream = null;
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

	public void setDefaults() {
		modelID = 0;
		name = null;
		description = null;
		editedModelColor = null;
		newModelColor = null;
		modelZoom = 2000;
		rotationY = 0;
		rotationX = 0;
		modelOffsetX = 0;
		modelOffset1 = 0;
		modelOffsetY = 0;
		stackable = false;
		value = 1;
		membersObject = false;
		groundActions = null;
		actions = null;
		maleEquip1 = -1;
		maleEquip2 = -1;
		maleYOffset = 0;
		femaleEquip1 = -1;
		femaleEquip2 = -1;
		femaleYOffset = 0;
		maleEquip3 = -1;
		femaleEquip3 = -1;
		maleDialogue = -1;
		maleDialogueModel = -1;
		femaleDialogue = -1;
		femaleDialogueModel = -1;
		stackIDs = null;
		stackAmounts = null;
		certID = -1;
		certTemplateID = -1;
		sizeX = 128;
		sizeY = 128;
		sizeZ = 128;
		shadow = 0;
		lightness = 0;
		team = 0;
		lendID = -1;
		lentItemID = -1;
	}

	public static void unpackConfig(CacheArchive streamLoader) {
		stream = new Stream(streamLoader.getDataForName("obj.dat"));
		Stream stream = new Stream(streamLoader.getDataForName("obj.idx"));
		totalItems = stream.readUnsignedWord();
		System.out.println("Loaded: " + totalItems + " item definitions.");
		streamIndices = new int[totalItems + 1000];
		int i = 2;
		for (int j = 0; j < totalItems; j++) {
			streamIndices[j] = i;
			i += stream.readUnsignedWord();
		}
		cache = new ItemDef[10];
		for (int k = 0; k < 10; k++)
			cache[k] = new ItemDef();
	}

	public static ItemDef forID(int i) {
		if((i >= totalItems || Client.getOption("new_items"))) {
			ItemDef def = NewItemDef.forID(i);
			if(def != null)
				return NewItemDef.forID(i);
		}
		for (int j = 0; j < 10; j++)
			if (cache[j].ID == i)
				return cache[j];
		cacheIndex = (cacheIndex + 1) % 10;
		ItemDef itemDef = cache[cacheIndex];
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
		for (int blackItems : Configuration.ITEMS_WITH_BLACK) {
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
		}
		switch (i) {
		case 6199:
			itemDef.name = "Herb Box";
			itemDef.actions = new String[5];
			itemDef.actions[0] = "Open";
			itemDef.description = "Smells like herbs are in there";
			break;
		case 3062: 
			itemDef.name = "Membership GiftBox";
			itemDef.description = "A present from RevolutionX, I wonder what's inside!";
			break;
		case 6568: // To replace Transparent black with opaque black.
			itemDef.editedModelColor = new int[1];
			itemDef.newModelColor = new int[1];
			itemDef.editedModelColor[0] = 0;
			itemDef.newModelColor[0] = 2059;
			break;
		case 996:
		case 997:
		case 998:
		case 999:
		case 1000:
		case 1001:
		case 1002:
		case 1003:
		case 1004:
			itemDef.name = "Coins";
			break;
		case 20671:
			itemDef.name = "Brackish blade";
			itemDef.modelZoom = 1488;
			itemDef.rotationY = 276;
			itemDef.rotationX = 1580;
			itemDef.modelOffsetY = 1;
			itemDef.groundActions = new String[] { null, null, "Take", null, null };
			itemDef.actions = new String[] { null, "Wield", null, null, "Drop" };
			itemDef.modelID = 64593;
			itemDef.maleEquip1 = 64704;
			itemDef.femaleEquip2 = 64704;
			break;
		case 15220:
			itemDef.name = "Berserker ring (i)";
			itemDef.modelZoom = 600;
			itemDef.rotationY = 324;
			itemDef.rotationX = 1916;
			itemDef.modelOffset1 = 3;
			itemDef.modelOffsetY = -15;
			itemDef.groundActions = new String[5];
			itemDef.groundActions[2] = "Take";
			itemDef.actions = new String[5];
			itemDef.actions[1] = "Wear";
			itemDef.actions[4] = "Drop";
			itemDef.modelID = 7735; // if it doesn't work try 7735
			itemDef.maleEquip1 = -1;
			// itemDefinition.maleArm = -1;
			itemDef.femaleEquip1 = -1;
			// itemDefinition.femaleArm = -1;
			break;
		case 20747:
			itemDef.modelID = 65262;
			itemDef.name = "Max Cape";
			itemDef.description = "A cape worn by those who've achieved 99 in all skills.";
			itemDef.modelZoom = 1385;
			itemDef.modelOffset1 = 0;
			itemDef.modelOffsetY = 24;
			itemDef.rotationY = 279;
			itemDef.rotationX = 948;
			itemDef.maleEquip1 = 65300;
			itemDef.femaleEquip1 = 65322;
			itemDef.groundActions = new String[5];
			itemDef.groundActions[2] = "Take";
			itemDef.actions = new String[5];
			itemDef.actions[1] = "Wear";
			break;
		case 20744:
			itemDef.name = "Veteran hood";
			itemDef.description = "A hood worn by Project-Exile's veterans.";
			itemDef.modelZoom = 760;
			itemDef.rotationY = 11;
			itemDef.rotationX = 81;
			itemDef.modelOffset1 = 1;
			itemDef.modelOffsetY = -3;
			itemDef.groundActions = new String[] { null, null, "Take", null, null };
			itemDef.actions = new String[] { null, "Wear", null, null, "Drop" };
			itemDef.modelID = 65271;
			itemDef.maleEquip1 = 65289;
			itemDef.femaleEquip1 = 65314;
			break;
		case 20745:
			itemDef.modelID = 65261;
			itemDef.name = "Veteran Cape";
			itemDef.description = "A cape worn by Project-Exile's veterans.";
			itemDef.modelZoom = 760;
			itemDef.modelOffset1 = 0;
			itemDef.modelOffsetY = 24;
			itemDef.rotationY = 279;
			itemDef.rotationX = 948;
			itemDef.maleEquip1 = 65305;
			itemDef.femaleEquip1 = 65318;
			itemDef.groundActions = new String[5];
			itemDef.groundActions[2] = "Take";
			itemDef.actions = new String[5];
			itemDef.actions[1] = "Wear";
			break;
		case 20769:
			itemDef.modelID = 65270;
			itemDef.name = "Completionist Cape";
			itemDef.description = "We'd pat you on the back, but this cape would get in the way.";
			itemDef.modelZoom = 1385;
			itemDef.modelOffset1 = 0;
			itemDef.modelOffsetY = 24;
			itemDef.rotationY = 279;
			itemDef.rotationX = 948;
			itemDef.maleEquip1 = 65297;
			itemDef.femaleEquip1 = 65297;
			itemDef.groundActions = new String[5];
			itemDef.groundActions[2] = "Take";
			itemDef.actions = new String[5];
			itemDef.actions[1] = "Wear";
			break;
		case 20746:
			itemDef.modelID = 65257;
			itemDef.name = "Classic Cape";
			itemDef.description = "A cape worn by those who've seen the world in a different light.";
			itemDef.modelZoom = 1385;
			itemDef.modelOffset1 = 0;
			itemDef.modelOffsetY = 24;
			itemDef.rotationY = 279;
			itemDef.rotationX = 948;
			itemDef.maleEquip1 = 65302;
			itemDef.femaleEquip1 = 65327;
			itemDef.groundActions = new String[5];
			itemDef.groundActions[2] = "Take";
			itemDef.actions = new String[5];
			itemDef.actions[1] = "Wear";
			break;
		case 9666:
		case 11814:
		case 11816:
		case 11818:
		case 11820:
		case 11822:
		case 11824:
		case 11826:
		case 11828:
		case 11830:
		case 11832:
		case 11834:
		case 11836:
		case 11838:
		case 11840:
		case 11842:
		case 11844:
		case 11846:
		case 11848:
		case 11850:
		case 11852:
		case 11854:
		case 11856:
		case 11858:
		case 11860:
		case 11862:
		case 11864:
		case 11866:
		case 11868:
		case 11870:
		case 11874:
		case 11876:
		case 11878:
		case 11882:
		case 11886:
		case 11890:
		case 11894:
		case 11898:
		case 11902:
		case 11904:
		case 11906:
		case 11926:
		case 11928:
		case 11930:
		case 11938:
		case 11942:
		case 11944:
		case 11946:
		case 14525:
		case 14527:
		case 14529:
		case 14531:
		case 19588:
		case 19592:
		case 19596:
		case 11908:
		case 11910:
		case 11912:
		case 11914:
		case 11916:
		case 11618:
		case 11920:
		case 11922:
		case 11924:
		case 11960:
		case 11962:
		case 11967:
		case 11982:
		case 19586:
		case 19584:
		case 19590:
		case 19594:
		case 19598:
			itemDef.actions = new String[5];
			itemDef.actions[0] = "Open";
			break;
		case 920:
			itemDef.modelID = 10919;
			itemDef.name = "Ganodermic Poncho";
			itemDef.description = "It's a Ganodermic Poncho";
			itemDef.modelZoom = 1513;
			itemDef.rotationY = 485;
			itemDef.rotationX = 13;
			itemDef.modelOffset1 = 1;
			itemDef.modelOffsetY = -3;
			itemDef.maleEquip1 = 10490;
			itemDef.femaleEquip1 = 10664;
			itemDef.groundActions = new String[5];
			itemDef.groundActions[2] = "Take";
			itemDef.actions = new String[5];
			itemDef.actions[1] = "Wear";
			itemDef.actions[4] = "Drop";
			break;
		case 919:
			itemDef.modelID = 10942;
			itemDef.name = "Ganodermic Leggings";
			itemDef.description = "It's a Ganodermic Leggings";
			itemDef.modelZoom = 1513;
			itemDef.rotationY = 498;
			itemDef.rotationX = 0;
			itemDef.modelOffset1 = 9;
			itemDef.modelOffsetY = -18;
			itemDef.maleEquip1 = 10486;
			itemDef.femaleEquip1 = 10578;
			itemDef.groundActions = new String[5];
			itemDef.groundActions[2] = "Take";
			itemDef.actions = new String[5];
			itemDef.actions[1] = "Wear";
			itemDef.actions[4] = "Drop";
			break;
		case 922:
			itemDef.modelID = 10935;
			itemDef.name = "Ganodermic Visor";
			itemDef.description = "It's a Ganodermic Visor";
			itemDef.modelZoom = 1118;
			itemDef.rotationY = 215;
			itemDef.rotationX = 175;
			itemDef.modelOffset1 = 1;
			itemDef.modelOffsetY = -30;
			itemDef.maleEquip1 = 10373;
			itemDef.femaleEquip1 = 10523;
			itemDef.groundActions = new String[5];
			itemDef.groundActions[2] = "Take";
			itemDef.actions = new String[5];
			itemDef.actions[1] = "Wear";
			itemDef.actions[4] = "Drop";
			break;
		case 20115:
			itemDef.modelID = 62694;
			itemDef.name = "Ancient ceremonial hood";
			itemDef.modelZoom = 980;
			itemDef.rotationY = 208;
			itemDef.rotationX = 220;
			itemDef.modelOffset1 = 0;
			itemDef.modelOffsetY = -18;
			itemDef.maleEquip1 = 62737;
			itemDef.femaleEquip1 = 62753;
			itemDef.groundActions = new String[5];
			itemDef.groundActions[2] = "Take";
			itemDef.actions = new String[5];
			itemDef.actions[1] = "Wear";
			itemDef.actions[4] = "Drop";
			itemDef.maleDialogue = 62730;
			itemDef.femaleDialogue = 62730;
			break;

		case 20116:
			itemDef.modelID = 62705;
			itemDef.name = "Ancient ceremonial top";
			itemDef.modelZoom = 1316;
			itemDef.rotationY = 477;
			itemDef.rotationX = 9;
			itemDef.modelOffset1 = 0;
			itemDef.modelOffsetY = 13;
			itemDef.maleEquip1 = 62745;
			itemDef.femaleEquip1 = 62763;
			itemDef.groundActions = new String[5];
			itemDef.groundActions[2] = "Take";
			itemDef.actions = new String[5];
			itemDef.actions[1] = "Wear";
			itemDef.actions[4] = "Drop";
			itemDef.modelOffsetX = 54;
			break;

		case 20117:
			itemDef.modelID = 62707;
			itemDef.name = "Ancient ceremonial legs";
			itemDef.modelZoom = 1828;
			itemDef.rotationY = 539;
			itemDef.rotationX = 0;
			itemDef.modelOffset1 = -1;
			itemDef.modelOffsetY = 0;
			itemDef.maleEquip1 = 62740;
			itemDef.femaleEquip1 = 62759;
			itemDef.groundActions = new String[5];
			itemDef.groundActions[2] = "Take";
			itemDef.actions = new String[5];
			itemDef.actions[1] = "Wear";
			itemDef.actions[4] = "Drop";
			itemDef.modelOffsetX = 40;
			itemDef.shadow = 30;
			itemDef.lightness = 100;
			break;

		case 20118:
			itemDef.modelID = 62697;
			itemDef.name = "Ancient ceremonial gloves";
			itemDef.modelZoom = 548;
			itemDef.rotationY = 618;
			itemDef.rotationX = 1143;
			itemDef.modelOffset1 = 0;
			itemDef.modelOffsetY = -5;
			itemDef.maleEquip1 = 62735;
			itemDef.femaleEquip1 = 62752;
			itemDef.groundActions = new String[5];
			itemDef.groundActions[2] = "Take";
			itemDef.actions = new String[5];
			itemDef.actions[1] = "Wear";
			itemDef.actions[4] = "Drop";
			break;

		case 20119:
			itemDef.modelID = 62696;
			itemDef.name = "Ancient ceremonial boots";
			itemDef.modelZoom = 676;
			itemDef.rotationY = 63;
			itemDef.rotationX = 106;
			itemDef.modelOffset1 = 5;
			itemDef.modelOffsetY = -1;
			itemDef.maleEquip1 = 62734;
			itemDef.femaleEquip1 = 62751;
			itemDef.groundActions = new String[5];
			itemDef.groundActions[2] = "Take";
			itemDef.actions = new String[5];
			itemDef.actions[1] = "Wear";
			itemDef.actions[4] = "Drop";
			break;

		case 20120:
			itemDef.modelID = 57037;
			itemDef.name = "Frozen key";
			itemDef.modelZoom = 1184;
			itemDef.rotationY = 384;
			itemDef.rotationX = 162;
			itemDef.modelOffset1 = -8;
			itemDef.modelOffsetY = -14;
			itemDef.groundActions = new String[5];
			itemDef.groundActions[2] = "Take";
			itemDef.actions = new String[5];
			itemDef.actions[0] = "Check-uses";
			itemDef.actions[4] = "Destroy";
			break;

		case 20121:
			itemDef.modelID = 52559;
			itemDef.name = "Frozen key piece (armadyl)";
			itemDef.description = "Frozen key piece (armadyl)";
			itemDef.modelZoom = 925;
			itemDef.rotationY = 553;
			itemDef.rotationX = 131;
			itemDef.modelOffset1 = 12;
			itemDef.modelOffsetY = -8;
			itemDef.groundActions = new String[5];
			itemDef.groundActions[2] = "Take";
			itemDef.actions = new String[5];
			itemDef.actions[0] = "Assemble";
			itemDef.actions[4] = "Drop";
			itemDef.sizeX = 260;
			itemDef.sizeY = 260;
			itemDef.sizeZ = 260;
			break;

		case 20122:
			itemDef.modelID = 52562;
			itemDef.name = "Frozen key piece (bandos)";
			itemDef.description = "Frozen key piece (bandos)";
			itemDef.modelZoom = 720;
			itemDef.rotationY = 600;
			itemDef.rotationX = 223;
			itemDef.modelOffset1 = -7;
			itemDef.modelOffsetY = 2;
			itemDef.groundActions = new String[5];
			itemDef.groundActions[2] = "Take";
			itemDef.actions = new String[5];
			itemDef.actions[0] = "Assemble";
			itemDef.actions[4] = "Drop";
			itemDef.sizeX = 260;
			itemDef.sizeY = 260;
			itemDef.sizeZ = 260;
			break;

		case 19111:
			itemDef.name = "TokHaar-Kal";
			// itemDef.femaleOffset = 0;
			itemDef.value = 60000;
			itemDef.maleEquip1 = 62575;
			itemDef.femaleEquip1 = 62582;
			itemDef.groundActions = new String[5];
			itemDef.groundActions[2] = "Take";
			itemDef.modelOffset1 = -4;
			itemDef.modelID = 62592;
			itemDef.stackable = false;
			itemDef.description = "A cape made of ancient, enchanted obsidian.";
			itemDef.modelZoom = 2086;
			itemDef.actions = new String[5];
			itemDef.actions[1] = "Wear";
			itemDef.actions[4] = "Drop";
			itemDef.modelOffsetY = 0;
			itemDef.rotationY = 533;
			itemDef.rotationX = 333;
			break;

		case 20000:
			itemDef.actions = new String[5];
			itemDef.actions[1] = "Wear";
			itemDef.actions[4] = "Drop";
			itemDef.modelID = 53835;
			itemDef.name = "Steadfast boots";
			itemDef.modelZoom = 900;
			itemDef.rotationY = 165;
			itemDef.rotationX = 99;
			itemDef.modelOffset1 = 3;
			itemDef.modelOffsetY = -7;
			itemDef.maleEquip1 = 53327;
			itemDef.femaleEquip1 = 53643;
			itemDef.description = "A pair of Steadfast boots.";
			break;

		case 20001:
			itemDef.actions = new String[5];
			itemDef.actions[1] = "Wear";
			itemDef.actions[4] = "Drop";
			itemDef.modelID = 53828;
			itemDef.name = "Glaiven boots";
			itemDef.modelZoom = 900;
			itemDef.rotationY = 165;
			itemDef.rotationX = 99;
			itemDef.modelOffset1 = 3;
			itemDef.modelOffsetY = -7;
			itemDef.femaleEquip1 = 53309;
			itemDef.maleEquip1 = 53309;
			itemDef.description = "A pair of Glaiven boots.";
			break;

		case 20002:
			itemDef.actions = new String[5];
			itemDef.actions[1] = "Wear";
			itemDef.actions[4] = "Drop";
			itemDef.description = "A pair of Ragefire boots.";
			itemDef.modelID = 53897;
			itemDef.name = "Ragefire boots";
			itemDef.modelZoom = 900;
			itemDef.rotationY = 165;
			itemDef.rotationX = 99;
			itemDef.modelOffset1 = 3;
			itemDef.modelOffsetY = -7;
			itemDef.maleEquip1 = 53330;
			itemDef.femaleEquip1 = 53651;
			break;

		case 20123:
			itemDef.modelID = 52564;
			itemDef.name = "Frozen key piece (zamorak)";
			itemDef.description = "Frozen key piece (zamorak)";
			itemDef.modelZoom = 457;
			itemDef.rotationY = 387;
			itemDef.rotationX = 95;
			itemDef.modelOffset1 = 26;
			itemDef.modelOffsetY = -34;
			itemDef.groundActions = new String[5];
			itemDef.groundActions[2] = "Take";
			itemDef.actions = new String[5];
			itemDef.actions[0] = "Assemble";
			itemDef.actions[4] = "Drop";
			itemDef.sizeX = 260;
			itemDef.sizeY = 260;
			itemDef.sizeZ = 260;
			break;

		case 20124:
			itemDef.modelID = 52561;
			itemDef.name = "Frozen key piece (saradomin)";
			itemDef.description = "Frozen key piece (saradomin)";
			itemDef.modelZoom = 541;
			itemDef.rotationY = 444;
			itemDef.rotationX = 32;
			itemDef.modelOffset1 = 16;
			itemDef.modelOffsetY = -47;
			itemDef.groundActions = new String[5];
			itemDef.groundActions[2] = "Take";
			itemDef.actions = new String[5];
			itemDef.actions[0] = "Assemble";
			itemDef.actions[4] = "Drop";
			itemDef.sizeX = 260;
			itemDef.sizeY = 260;
			itemDef.sizeZ = 260;
			break;

		case 20135:
			itemDef.modelID = 62714;
			itemDef.name = "Torva full helm";
			itemDef.description = "Torva full helm";
			itemDef.modelZoom = 672;
			itemDef.rotationY = 85;
			itemDef.rotationX = 1867;
			itemDef.modelOffset1 = 0;
			itemDef.modelOffsetY = -3;
			itemDef.maleEquip1 = 62738;
			itemDef.femaleEquip1 = 62754;
			itemDef.groundActions = new String[5];
			itemDef.groundActions[2] = "Take";
			itemDef.actions = new String[5];
			itemDef.actions[1] = "Wear";
			itemDef.actions[2] = "Check-charges";
			itemDef.actions[4] = "Drop";
			itemDef.maleDialogue = 62729;
			itemDef.femaleDialogue = 62729;
			break;

		case 20139:
			itemDef.modelID = 62699;
			itemDef.name = "Torva platebody";
			itemDef.description = "Torva platebody";
			itemDef.modelZoom = 1506;
			itemDef.rotationY = 473;
			itemDef.rotationX = 2042;
			itemDef.modelOffset1 = 0;
			itemDef.modelOffsetY = 0;
			itemDef.maleEquip1 = 62746;
			itemDef.femaleEquip1 = 62762;
			itemDef.groundActions = new String[5];
			itemDef.groundActions[2] = "Take";
			itemDef.actions = new String[5];
			itemDef.actions[1] = "Wear";
			itemDef.actions[2] = "Check-charges";
			itemDef.actions[4] = "Drop";
			break;

		case 20143:
			itemDef.modelID = 62701;
			itemDef.name = "Torva platelegs";
			itemDef.description = "Torva platelegs";
			itemDef.modelZoom = 1740;
			itemDef.rotationY = 474;
			itemDef.rotationX = 2045;
			itemDef.modelOffset1 = 0;
			itemDef.modelOffsetY = -5;
			itemDef.maleEquip1 = 62743;
			itemDef.femaleEquip1 = 62760;
			itemDef.groundActions = new String[5];
			itemDef.groundActions[2] = "Take";
			itemDef.actions = new String[5];
			itemDef.actions[1] = "Wear";
			itemDef.actions[2] = "Check-charges";
			itemDef.actions[4] = "Drop";
			break;

		case 20147:
			itemDef.modelID = 62693;
			itemDef.name = "Pernix cowl";
			itemDef.description = "Pernix cowl";
			itemDef.modelZoom = 800;
			itemDef.rotationY = 532;
			itemDef.rotationX = 14;
			itemDef.modelOffset1 = -1;
			itemDef.modelOffsetY = 1;
			itemDef.maleEquip1 = 62739;
			itemDef.femaleEquip1 = 62756;
			itemDef.groundActions = new String[5];
			itemDef.groundActions[2] = "Take";
			itemDef.actions = new String[5];
			itemDef.actions[1] = "Wear";
			itemDef.actions[2] = "Check-charges";
			itemDef.actions[4] = "Drop";
			itemDef.maleDialogue = 62731;
			itemDef.femaleDialogue = 62727;
			itemDef.editedModelColor = new int[2];
			itemDef.newModelColor = new int[2];
			itemDef.editedModelColor[0] = 4550;
			itemDef.newModelColor[0] = 0;
			itemDef.editedModelColor[1] = 4540;
			itemDef.newModelColor[1] = 0;
			break;

		case 20151:
			itemDef.modelID = 62709;
			itemDef.name = "Pernix body";
			itemDef.description = "Pernix body";
			itemDef.modelZoom = 1378;
			itemDef.rotationY = 485;
			itemDef.rotationX = 2042;
			itemDef.modelOffset1 = -1;
			itemDef.modelOffsetY = 7;
			itemDef.maleEquip1 = 62744;
			itemDef.femaleEquip1 = 62765;
			itemDef.groundActions = new String[5];
			itemDef.groundActions[2] = "Take";
			itemDef.actions = new String[5];
			itemDef.actions[1] = "Wear";
			itemDef.actions[2] = "Check-charges";
			itemDef.actions[4] = "Drop";
			break;

		case 20155:
			itemDef.modelID = 62695;
			itemDef.name = "Pernix chaps";
			itemDef.description = "Pernix chaps";
			itemDef.modelZoom = 1740;
			itemDef.rotationY = 504;
			itemDef.rotationX = 0;
			itemDef.modelOffset1 = 4;
			itemDef.modelOffsetY = 3;
			itemDef.maleEquip1 = 62741;
			itemDef.femaleEquip1 = 62757;
			itemDef.groundActions = new String[5];
			itemDef.groundActions[2] = "Take";
			itemDef.actions = new String[5];
			itemDef.actions[1] = "Wear";
			itemDef.actions[2] = "Check-charges";
			itemDef.actions[4] = "Drop";
			break;

		case 20159:
			itemDef.modelID = 62710;
			itemDef.name = "Virtus mask";
			itemDef.description = "Virtus mask";
			itemDef.modelZoom = 928;
			itemDef.rotationY = 406;
			itemDef.rotationX = 2041;
			itemDef.modelOffset1 = 1;
			itemDef.modelOffsetY = -5;
			itemDef.maleEquip1 = 62736;
			itemDef.femaleEquip1 = 62755;
			itemDef.groundActions = new String[5];
			itemDef.groundActions[2] = "Take";
			itemDef.actions = new String[5];
			itemDef.actions[1] = "Wear";
			itemDef.actions[2] = "Check-charges";
			itemDef.actions[4] = "Drop";
			itemDef.maleDialogue = 62728;
			itemDef.femaleDialogue = 62728;
			break;

		case 20163:
			itemDef.modelID = 62704;
			itemDef.name = "Virtus robe top";
			itemDef.description = "Virtus robe top";
			itemDef.modelZoom = 1122;
			itemDef.rotationY = 488;
			itemDef.rotationX = 3;
			itemDef.modelOffset1 = 1;
			itemDef.modelOffsetY = 0;
			itemDef.maleEquip1 = 62748;
			itemDef.femaleEquip1 = 62764;
			itemDef.groundActions = new String[5];
			itemDef.groundActions[2] = "Take";
			itemDef.actions = new String[5];
			itemDef.actions[1] = "Wear";
			itemDef.actions[2] = "Check-charges";
			itemDef.actions[4] = "Drop";
			break;

		case 20167:
			itemDef.modelID = 62700;
			itemDef.name = "Virtus robe legs";
			itemDef.description = "Virtus robe legs";
			itemDef.modelZoom = 1740;
			itemDef.rotationY = 498;
			itemDef.rotationX = 2045;
			itemDef.modelOffset1 = -1;
			itemDef.modelOffsetY = 4;
			itemDef.maleEquip1 = 62742;
			itemDef.femaleEquip1 = 62758;
			itemDef.groundActions = new String[5];
			itemDef.groundActions[2] = "Take";
			itemDef.actions = new String[5];
			itemDef.actions[1] = "Wear";
			itemDef.actions[2] = "Check-charges";
			itemDef.actions[4] = "Drop";
			break;

		case 20171:
			itemDef.modelID = 62692;
			itemDef.name = "Zaryte bow";
			itemDef.description = "Zaryte bow";
			itemDef.modelZoom = 1703;
			itemDef.rotationY = 221;
			itemDef.rotationX = 404;
			itemDef.modelOffset1 = 0;
			itemDef.modelOffsetY = -13;
			itemDef.maleEquip1 = 62750;
			itemDef.femaleEquip1 = 62750;
			itemDef.femaleYOffset = -11;
			itemDef.groundActions = new String[5];
			itemDef.groundActions[2] = "Take";
			itemDef.actions = new String[5];
			itemDef.actions[1] = "Wield";
			itemDef.actions[2] = "Check-charges";
			itemDef.actions[4] = "Drop";
			break;

		case 20175:
			itemDef.modelID = 57921;
			itemDef.name = "Trollheim tablet";
			itemDef.modelZoom = 465;
			itemDef.rotationY = 373;
			itemDef.rotationX = 0;
			itemDef.modelOffset1 = 0;
			itemDef.modelOffsetY = -1;
			itemDef.value = 1;
			itemDef.groundActions = new String[5];
			itemDef.groundActions[2] = "Take";
			itemDef.actions = new String[5];
			itemDef.actions[0] = "Break";
			itemDef.actions[4] = "Drop";
			break;
		}
		return itemDef;
	}

	public void readValues(Stream stream) {
		do {
			int i = stream.readUnsignedByte();
			if (i == 0)
				return;
			if (i == 1) {
				modelID = stream.readUnsignedWord();
				//System.out.println("modelid: " + modelID);
				if(this.ID == 303 || this.ID == 305 || this.ID == 1513)
					System.out.println("item: " + this.ID + " modelid: " + modelID);
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
				value = stream.readUnsignedWord();
			else if(i == 13)
				System.out.println("i == 13 for " + this.ID);
			else if (i == 16)
				membersObject = true;
			else if (i == 23) {
				maleEquip1 = stream.readUnsignedWord();
				maleYOffset = stream.readSignedByte();
			} else if (i == 24)
				maleEquip2 = stream.readUnsignedWord();
			else if (i == 25) {
				femaleEquip1 = stream.readUnsignedWord();
				femaleYOffset = stream.readSignedByte();
			} else if (i == 26)
				femaleEquip2 = stream.readUnsignedWord();
			else if (i >= 30 && i < 35) {
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
			} else if (i == 78)
				maleEquip3 = stream.readUnsignedWord();
			else if (i == 79)
				femaleEquip3 = stream.readUnsignedWord();
			else if (i == 90) {
				maleDialogue = stream.readUnsignedWord();
				//System.out.println("maledialogue: " + maleDialogue);
			} else if (i == 91) {
				femaleDialogue = stream.readUnsignedWord();
			} else if (i == 92)
				maleDialogueModel = stream.readUnsignedWord();
			else if (i == 93)
				femaleDialogueModel = stream.readUnsignedWord();
			else if (i == 95)
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
	}

	public void toNote() {
		ItemDef itemDef = forID(certTemplateID);
		modelID = itemDef.modelID;
		modelZoom = itemDef.modelZoom;
		rotationY = itemDef.rotationY;
		rotationX = itemDef.rotationX;
		modelOffsetX = itemDef.modelOffsetX;
		modelOffset1 = itemDef.modelOffset1;
		modelOffsetY = itemDef.modelOffsetY;
		editedModelColor = itemDef.editedModelColor;
		newModelColor = itemDef.newModelColor;
		ItemDef itemDef_1 = forID(certID);
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

	protected void toLend() {
		ItemDef itemDef = forID(lentItemID);
		actions = new String[5];
		modelID = itemDef.modelID;
		modelOffset1 = itemDef.modelOffset1;
		rotationX = itemDef.rotationX;
		modelOffsetY = itemDef.modelOffsetY;
		modelZoom = itemDef.modelZoom;
		rotationY = itemDef.rotationY;
		modelOffsetX = itemDef.modelOffsetX;
		value = 0;
		ItemDef itemDef_1 = forID(lendID);
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

	public static Sprite getSprite(int id, int stackSize, int outlineColor, int zoom) {
		if((id >= totalItems || Client.getOption("new_items"))) {
			Sprite sprite = NewItemDef.getSprite(id, stackSize, outlineColor, zoom);
			if(sprite != null)
			return sprite;
		}
		if (outlineColor == 0 && zoom != -1) {
			Sprite sprite = (Sprite) spriteCache.get(id);
			if (sprite != null && sprite.maxHeight != stackSize && sprite.maxHeight != -1) {
				sprite.unlink();
				sprite = null;
			}
			if (sprite != null)
				return sprite;
		}
		ItemDef itemDef = forID(id);
		if (itemDef.stackIDs == null)
			stackSize = -1;
		if (stackSize > 1) {
			int i1 = -1;
			for (int j1 = 0; j1 < 10; j1++)
				if (stackSize >= itemDef.stackAmounts[j1] && itemDef.stackAmounts[j1] != 0)
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
		int centerX = Rasterizer.center_x;
		int centerY = Rasterizer.center_y;
		int lineOffsets[] = Rasterizer.lineOffsets;
		int pixels[] = DrawingArea.pixels;
		int width = DrawingArea.width;
		int height = DrawingArea.height;
		int vp_left = DrawingArea.topX;
		int vp_right = DrawingArea.bottomX;
		int vp_top = DrawingArea.topY;
		int vp_bottom = DrawingArea.bottomY;
		Rasterizer.notTextured = false;
		DrawingArea.initDrawingArea(32, 32, sprite2.myPixels);
		DrawingArea.drawPixels(32, 0, 0, 0, 32);
		Rasterizer.setDefaultBounds();
		int k3 = itemDef.modelZoom;
		if (zoom != -1 && zoom != 0)
			k3 = (itemDef.modelZoom * 100) / zoom;
		if (outlineColor == -1)
			k3 = (int) ((double) k3 * 1.5D);
		if (outlineColor > 0)
			k3 = (int) ((double) k3 * 1.04D);
		int sine = Rasterizer.SINE[itemDef.rotationY] * k3 >> 16;
		int cosine = Rasterizer.COSINE[itemDef.rotationY] * k3 >> 16;
		model.renderSingle(itemDef.rotationX, itemDef.modelOffsetX, itemDef.rotationY, itemDef.modelOffset1, sine + model.modelHeight / 2 + itemDef.modelOffsetY, cosine + itemDef.modelOffsetY);
		for (int _x = 31; _x >= 0; _x--) {
			for (int _y = 31; _y >= 0; _y--) {
				if (sprite2.myPixels[_x + _y * 32] != 0)
					continue;
				if (_x > 0 && sprite2.myPixels[(_x - 1) + _y * 32] > 1) {
					sprite2.myPixels[_x + _y * 32] = 1;
					continue;
				}
				if (_y > 0 && sprite2.myPixels[_x + (_y - 1) * 32] > 1) {
					sprite2.myPixels[_x + _y * 32] = 1;
					continue;
				}
				if (_x < 31 && sprite2.myPixels[_x + 1 + _y * 32] > 1) {
					sprite2.myPixels[_x + _y * 32] = 1;
					continue;
				}
				if (_y < 31 && sprite2.myPixels[_x + (_y + 1) * 32] > 1)
					sprite2.myPixels[_x + _y * 32] = 1;
			}

		}

		if (outlineColor > 0) {
			for (int j5 = 31; j5 >= 0; j5--) {
				for (int k4 = 31; k4 >= 0; k4--) {
					if (sprite2.myPixels[j5 + k4 * 32] != 0)
						continue;
					if (j5 > 0 && sprite2.myPixels[(j5 - 1) + k4 * 32] == 1) {
						sprite2.myPixels[j5 + k4 * 32] = outlineColor;
						continue;
					}
					if (k4 > 0 && sprite2.myPixels[j5 + (k4 - 1) * 32] == 1) {
						sprite2.myPixels[j5 + k4 * 32] = outlineColor;
						continue;
					}
					if (j5 < 31 && sprite2.myPixels[j5 + 1 + k4 * 32] == 1) {
						sprite2.myPixels[j5 + k4 * 32] = outlineColor;
						continue;
					}
					if (k4 < 31 && sprite2.myPixels[j5 + (k4 + 1) * 32] == 1)
						sprite2.myPixels[j5 + k4 * 32] = outlineColor;
				}

			}

		} else if (outlineColor == 0) {
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
		if (outlineColor == 0)
			spriteCache.put(sprite2, id);
		DrawingArea.initDrawingArea(height, width, pixels);
		DrawingArea.setDrawingArea(vp_bottom, vp_left, vp_right, vp_top);
		Rasterizer.center_x = centerX;
		Rasterizer.center_y = centerY;
		Rasterizer.lineOffsets = lineOffsets;
		Rasterizer.notTextured = true;
		sprite2.maxWidth = itemDef.stackable ? 33 : 32;
		sprite2.maxHeight = stackSize;
		return sprite2;
	}

	public static Sprite getSprite(int i, int j, int k) {
		if((i >= totalItems || Client.getOption("new_items"))) {
			Sprite sprite = NewItemDef.getSprite(i, j, k);
			if(sprite != null)
			return sprite;
		}
		if (k == 0) {
			Sprite sprite = (Sprite) spriteCache.get(i);
			if (sprite != null && sprite.maxHeight != j && sprite.maxHeight != -1) {
				sprite.unlink();
				sprite = null;
			}
			if (sprite != null)
				return sprite;
		}
		ItemDef itemDef = forID(i);
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

	public ItemDef() {
		ID = -1;
	}

	public byte femaleYOffset;
	public int value;
	public int[] editedModelColor;
	public int ID;
	public static MemCache spriteCache = new MemCache(100);
	public static MemCache modelCache = new MemCache(50);
	public int[] newModelColor;
	public boolean membersObject;
	public int femaleEquip3;
	public int certTemplateID;
	public int femaleEquip2;
	public int maleEquip1;
	public int maleDialogueModel;
	public int sizeX;
	public String groundActions[];
	public int modelOffset1;
	public String name;
	public static ItemDef[] cache;
	public int femaleDialogueModel;
	public int modelID;
	public int maleDialogue;
	public boolean stackable;
	public String description;
	public int certID;
	public static int cacheIndex;
	public int modelZoom;
	public static boolean isMembers = true;
	public static Stream stream;
	public int lightness;
	public int maleEquip3;
	public int maleEquip2;
	public String actions[];
	public int rotationY;
	public int sizeZ;
	public int sizeY;
	public int[] stackIDs;
	public int modelOffsetY;
	public static int[] streamIndices;
	public int shadow;
	public int femaleDialogue;
	public int rotationX;
	public int femaleEquip1;
	public int[] stackAmounts;
	public int team;
	public static int totalItems;
	public int modelOffsetX;
	public byte maleYOffset;
	public int lendID;
	public int lentItemID;
}