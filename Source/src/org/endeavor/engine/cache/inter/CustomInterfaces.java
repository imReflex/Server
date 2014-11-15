package org.endeavor.engine.cache.inter;

public class CustomInterfaces {

	public static void drawCustomInterface() {
		RSInterface rsinterface = RSInterface.addTabInterface(17600);
		rsinterface.children = new int[5];
		rsinterface.childX = new int[5];
		rsinterface.childY = new int[5];
		rsinterface.children[0] = 17601;
		rsinterface.childX[0] = 266;
		rsinterface.childY[0] = 10;
		rsinterface.children[1] = 17602;
		rsinterface.childX[1] = 266;
		rsinterface.childY[1] = rsinterface.childY[0] + 17;
		rsinterface.children[2] = 17603;
		rsinterface.childX[2] = 266;
		rsinterface.childY[2] = rsinterface.childY[1] + 17;
		rsinterface.children[3] = 17604;
		rsinterface.childX[3] = 266;
		rsinterface.childY[3] = rsinterface.childY[2] + 17;
		rsinterface.children[4] = 17605;
		rsinterface.childX[4] = 266;
		rsinterface.childY[4] = rsinterface.childY[3] + 17;
	}

}
