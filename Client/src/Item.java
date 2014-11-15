




public final class Item extends Animable {

	public final Model getRotatedModel() {
		ItemDef itemDef = ItemDef.forID(ID);
		return itemDef.getItemModelFinalised(amount);
	}

	public Item() {
	}

	public int ID;
	public int x;
	public int y;
	public int amount;
}
