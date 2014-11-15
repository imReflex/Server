

public final class OnDemandRequest extends QueueNode {

	public OnDemandRequest() {
		isNotExtraFile = true;
	}

	int dataType;
	byte buffer[];
	int id;
	boolean isNotExtraFile;
	int loopCycle;

	@Override
	public String toString() {
		return "OnDemandData [dataType=" + dataType + ", ID=" + id
				+ ", incomplete=" + isNotExtraFile + ", loopCycle=" + loopCycle
				+ "]";
	}

}
