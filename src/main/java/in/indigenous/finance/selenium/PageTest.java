package in.indigenous.finance.selenium;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.openqa.selenium.WebElement;

import in.indigenous.finance.util.CSVWriter;
import in.indigenous.finance.util.Config;

public class PageTest extends Page {
	private static final String FINANCE_PAGE_URL_PROPERTY = "finance.page.url";

	public static void main(String[] args) {
		PageTest test = new PageTest();
		test.open(Config.get(FINANCE_PAGE_URL_PROPERTY));
		test.waitForPageLoad();
		test.getElementByObjectName("IND_FIN_HOME_MARKETS_LINK").click();
		//test.getElementByXpath(".//*[@id='Markets']/div/ul/li[3]/a").click();
		//test.getElementByXpath(".//*[@id='yfi_sidebar']/div/ul/li[2]/ul/li[1]/a").click();
		test.getElementByLinkText("Price % Gainers").click();
		List<WebElement> symbols = test.getElementsByCss(".first>b>a");
		List<WebElement> names = test.getElementsByCss(".second.name");
		List<WebElement> lastTrades = test.getElementsByCss(".last_trade");
		System.out.println("Symbols size " + symbols.size());
		System.out.println("Names size " + names.size());
		System.out.println("lastTrades size " + lastTrades.size());
		List<String> symbolText = new ArrayList<String>(symbols.size() - 1);
		List<String> nameText = new ArrayList<String>(names.size() - 1);
		List<String> lastTradeText = new ArrayList<String>(lastTrades.size());
		List<String> changeVolText = new ArrayList<String>(lastTrades.size());
		List<String> changePerctText = new ArrayList<String>(lastTrades.size());
		List<String> volText = new ArrayList<String>(lastTrades.size());
		for(int i=0; i<lastTrades.size();i++) {
			symbolText.add(symbols.get(i).getText());
			nameText.add(names.get(i + 1).getText());
			lastTradeText.add(lastTrades.get(i).getText());
			String changeVolXpath = ".//*[@id='yfs_c63_" + symbols.get(i).getText().toLowerCase()+"']/span";
			test.waitForElement(changeVolXpath);
			changeVolText.add(test.getElementByXpath(changeVolXpath).getText());
			String percXpath = ".//*[@id='yfs_p43_" + symbols.get(i).getText().toLowerCase() + "']/span";
			changePerctText.add(test.getElementByXpath(percXpath).getText());
			String volXpath = ".//*[@id='yfs_v53_" + symbols.get(i).getText().toLowerCase() + "']";
			volText.add(test.getElementByXpath(volXpath).getText());
		}
		Date date = new Date();
		String dateStr = date.toString();
		String [] dateValues = dateStr.split("\\s"); 
		String fileName = "./output/top_gainers_" + new StringBuilder().append(dateValues[2]).append("-").append(dateValues[1].toUpperCase()).append("-").append(dateValues[5]).toString() + ".csv";
		CSVWriter.writeTodaysGainer(fileName, symbolText, nameText, lastTradeText, changeVolText, changePerctText, volText);
		test.close();
	}

}
