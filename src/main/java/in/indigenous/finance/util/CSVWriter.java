package in.indigenous.finance.util;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class CSVWriter {

	private CSVWriter() {

	}

	public static void writeTodaysGainer(final String file, List<String> symbolText, List<String> nameText,
			List<String> lastTradeText, List<String> changeVolText, List<String> changePerctText,
			List<String> volText) {
		com.opencsv.CSVWriter writer = null;
		try {
			writer = new com.opencsv.CSVWriter(new FileWriter(file));
			String [] nextLine = new String [6];
			nextLine[0] = "SYMBOL";
			nextLine[1] = "NAME";
			nextLine[2] = "LAST_TRADE";
			nextLine[3] = "CHANGE_VOLUME";
			nextLine[4] = "CHANGE_PERCENTAGE";
			nextLine[5] = "VOLUME";
			writer.writeNext(nextLine);
			for(int i=0; i<symbolText.size(); i++) {
				nextLine = new String [6];
				nextLine[0] = symbolText.get(i).trim().replaceAll("//s", "");
				nextLine[1] = nameText.get(i).trim().replaceAll("//s", "");
				nextLine[2] = lastTradeText.get(i).trim().replaceAll("//s", "");
				nextLine[3] = changeVolText.get(i).trim().replaceAll("//s", "");
				nextLine[4] = changePerctText.get(i).trim().replaceAll("//s", "");
				nextLine[5] = volText.get(i).trim().replaceAll("//s", "");
				writer.writeNext(nextLine);	
			}
		} catch (IOException e) {
			e.printStackTrace();
		}finally {
			try {
				writer.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
