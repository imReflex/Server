

import java.text.SimpleDateFormat;
import java.util.Date;

public class GetDate {

	public static String currentDate() {
		SimpleDateFormat simpleDate = new SimpleDateFormat("MM/dd/yyyy");
		Date currentDate = new Date();
		return simpleDate.format(currentDate);
	}
}