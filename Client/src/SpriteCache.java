


public class SpriteCache {

	public static Sprite[] spriteCache;
	public static Sprite[] spriteLink;
	private static OnDemandFetcher onDemandFetcher;
	
	public static boolean initialised() {
		return onDemandFetcher != null;
	}
	
	public static void initialise(int total, OnDemandFetcher onDemandFetcher_)
	{
		spriteCache = new Sprite[total+100];
		spriteLink = new Sprite[total+100];
		onDemandFetcher = onDemandFetcher_;
	}
	public static void loadSprite(final int spriteId, final Sprite sprite, boolean priority)
	{
		spriteLink[spriteId] = sprite;
		if(spriteCache[spriteId] == null)
			onDemandFetcher.requestFileData(Client.IMAGE_IDX-1, spriteId);
		if(priority)
			c.processOnDemandQueue();
		
	}
	public static void loadSprite(final int spriteId, final Sprite sprite)
	{
		loadSprite(spriteId, sprite, false);
	}
	public static void fetchIfNeeded(int spriteId)
	{
		if(spriteCache[spriteId] != null)
			return;
		onDemandFetcher.requestFileData(Client.IMAGE_IDX-1, spriteId);
	}
	public static Sprite get(int spriteId)
	{
		fetchIfNeeded(spriteId);
		return spriteCache[spriteId];
	}
	private static Client c;
	public static void preloadedPriorityImages(Client c)
	{
		SpriteCache.c = c;
		//loadSprite(36, c.backgroundFix, true);//main background
		loadSprite(694,null,true);
		loadSprite(55, null, true);
		loadSprite(56, null, true);
	
		loadSprite(37, c.candle1, true);//candle 1
		loadSprite(38, c.candle2, true);//candle 2
		//loadSprite(39, c.fire1, true);//bg 1
		//loadSprite(40, c.fire2, true);//bg 2
		//loadSprite(41, c.fire3, true);//bg 3
		loadSprite(42, c.loginButton, true);//loginButton = new Sprite("Login/login");
		loadSprite(43, c.registerButton, true);//registerButton = new Sprite("Login/register");
		loadSprite(44, c.loginBackButton, true);//loginBackButton = new Sprite("Login/back");
		loadSprite(45, c.scroll, true);//scroll = new Sprite("Login/scroll");
		loadSprite(46, c.scrollBottom, true);//scrollBottom = new Sprite("Login/scroll_bottom");'
		for(int i = 693; i <= 795; i++)
			loadSprite(i, null, true);
		loadSprite(53, null, true);
		loadSprite(54, null, true);
		loadSprite(57, null, true);
		
		for(int i = 327; i <= 336; i++)
			loadSprite(i, null, true);
		
		for(int skill = 695; skill <= 755; skill++) {
			loadSprite(skill, null, true);
		}
	}
	public static void preloadLowPriorityImages() {	
		loadSprite(678, c.search);//c.search = new Sprite("1");
		loadSprite(679, c.Search);//c.Search = new Sprite("2");
		loadSprite(27, c.SubmitBuy);//("Interfaces/GE/SubmitBuy");
		loadSprite(28, c.SubmitSell);//("Interfaces/GE/SubmitSell");
		loadSprite(29, c.Buy);//("Interfaces/GE/buySubmit");
		loadSprite(30, c.Sell);//("Interfaces/GE/sellSubmit");
		loadSprite(31, c.loadingPleaseWait);//loadingPleaseWait = new Sprite("loadingPleaseWait");
		loadSprite(32, c.reestablish);//reestablish = new Sprite("reestablish");
		/* Custom sprite unpacking */
		loadSprite(33, c.HPBarFull);//HPBarFull = new Sprite("Player/HP 0");
		loadSprite(34, c.HPBarEmpty);//HPBarEmpty = new Sprite("Player/HP 1");
		loadSprite(35, c.HPBarBigEmpty);//HPBarBigEmpty = new Sprite("Player/HP 2");
		loadSprite(47, c.magicAuto);//magicAuto = new Sprite("Player/magicauto");
		for (int i = 0; i <= 4; i++) {
			loadSprite(48+i, c.LOGOUT[i]);//LOGOUT[i] = new Sprite("Frame/X " + i);
		}
		for (int i = 0; i <= 4; i++) {
			c.ADVISOR[i] = new Sprite("Gameframe/A " + i);
		}
		for (int i = 0; i < 2; i++) {
			loadSprite(51+i, c.WorldOrb[i]);//WorldOrb[i] = new Sprite("Frame/WorldOrb " + i);
		}
		/*for (int i = 4; i < 17; i++) {
			loadSprite(53+i, c.titleBox[i]);//titleBox[i] = new Sprite("Login/titlebox " + i);
		}*/
		for (int i = 0; i < 2; i++) {
			loadSprite(70+i, c.backButton[i]);//backButton[i] = new Sprite("Interfaces/Minigame/Back " + i);
		}
		/*for (int i = 0; i < 8; i++) {
			loadSprite(72+i, c.loadCircle[i]);//loadCircle[i] = new Sprite("Login/load " + i);
		}*/
		/* End custom sprites */
		loadSprite(80, c.newMapBack);//newMapBack = new Sprite("Frame/Mapback");
		for (int i4 = 0; i4 < 33; i4++)
			loadSprite(81+i4, c.hitMark[i4]);//hitMark[i4] = new Sprite("/Hitmarks/hit " + i4);
		for (int i4 = 0; i4 < 6; i4++)
			loadSprite(114+i4, c.hitIcon[i4]);//hitIcon[i4] = new Sprite("/Hitmarks/icon " + i4);
		for (int i4 = 0; i4 < 1; i4++)
			c.newCrowns[i4] = new Sprite("/Crowns/" + i4);
		
		loadSprite(120, c.modIcons[8]);//modIcons[l4] = new Sprite("modicons " + l4);
		loadSprite(121, c.modIcons[9]);//modIcons[l4] = new Sprite("modicons " + l4);
		loadSprite(681, null);//modIcons[l4] = new Sprite("modicons " + l4);
		
		for(int i5 = 682; i5 <= 691; i5++)
			loadSprite(i5, c.hitMark[i5 - 631]);
		
		loadSprite(807, null);
		loadSprite(808, null);
	}
}
