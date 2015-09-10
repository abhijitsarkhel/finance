package in.indigenous.finance.selenium;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.opencsv.CSVReader;

import in.indigenous.finance.util.Config;
import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;

public abstract class Page {

	public static final String OBJECT_NAME = "OBJECT_NAME";
	public static final String OBJECT_LOCATOR_TYPE = "OBJECT_LOCATOR_TYPE";
	public static final String OBJECT_LOCATOR_VALUE = "OBJECT_LOCATOR_VALUE";

	private static final String OBJECT_REPO_CACHE_NAME = "ObjectRepoCache";
	private static final int PAGE_LOAD_WAIT_TIME = 10;
	private static final String OBJECT_REPO_PATH = "finance.object_repo.path";
	private static final String LINK_TEXT_TYPE = "link_text";

	private static CacheManager cacheManager;

	private WebDriver driver;

	static {
		cacheManager = CacheManager.create();
		Cache cache = new Cache(OBJECT_REPO_CACHE_NAME, 10000, false, true, 0, 0, false, 0);
		cacheManager.addCache(cache);
		try {
			CSVReader reader = new CSVReader(new FileReader(Config.get(OBJECT_REPO_PATH)), ',', '"', 1);
			String[] nextLine;
			try {
				while ((nextLine = reader.readNext()) != null) {
					if (nextLine != null) {
						Map<String, String> objectDetails = new HashMap<String, String>();
						objectDetails.put(OBJECT_NAME, nextLine[0]);
						objectDetails.put(OBJECT_LOCATOR_TYPE, nextLine[1]);
						objectDetails.put(OBJECT_LOCATOR_VALUE, nextLine[2]);
						cache.put(new Element(nextLine[0], objectDetails));
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	public Page() {
		// FirefoxProfile profile = new FirefoxProfile();
		FirefoxProfile profile = new FirefoxProfile(
				new File("C:/Users/Abhijit Sarkhel/AppData/Roaming/Mozilla/Firefox/Profiles/noym8ie0.default"));
		driver = new FirefoxDriver(profile);
	}

	protected void open(String url) {
		driver.get(url);
	}

	protected WebElement getElementByCss(String css) {
		return driver.findElement(By.cssSelector(css));
	}
	
	protected WebElement getElementByXpath(String xpath) {
		return driver.findElement(By.xpath(xpath));
	}
	
	protected WebElement getElementById(String id) {
		return driver.findElement(By.id(id));
	}
	
	protected WebElement getElementByLinkText(String linkText) {
		return driver.findElement(By.linkText(linkText));
	}

	protected WebElement getElementByObjectName(String objectName) {
		String type = getObjectDetails(objectName).get(OBJECT_LOCATOR_TYPE);
		String value = getObjectDetails(objectName).get(OBJECT_LOCATOR_VALUE);
		WebElement element = null;
		if(LINK_TEXT_TYPE.equalsIgnoreCase(type)) {
			element = driver.findElement(By.linkText(value));
		}
		return element;
	}
	
	protected List<WebElement> getElementsByCss(String css) {
		return driver.findElements(By.cssSelector(css));
	}

	protected Map<String, String> getObjectDetails(String objectName) {
		Cache cache = cacheManager.getCache(OBJECT_REPO_CACHE_NAME);
		@SuppressWarnings("unchecked")
		Map<String, String> objectDetails = (Map<String, String>) cache.get(objectName).getObjectValue();
		if (objectDetails == null) {
			try {
				CSVReader reader = new CSVReader(new FileReader(Config.get(OBJECT_REPO_PATH)), ',', '"', 1);
				String[] nextLine;
				try {
					while ((nextLine = reader.readNext()) != null) {
						if (nextLine != null) {
							objectDetails = new HashMap<String, String>();
							objectDetails.put(OBJECT_NAME, nextLine[0]);
							objectDetails.put(OBJECT_LOCATOR_TYPE, nextLine[1]);
							objectDetails.put(OBJECT_LOCATOR_VALUE, nextLine[2]);
							cache.put(new Element(nextLine[0], objectDetails));
						}
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}
		return objectDetails;
	}

	protected void waitForPageLoad() {
		driver.manage().timeouts().pageLoadTimeout(PAGE_LOAD_WAIT_TIME, TimeUnit.SECONDS);
	}
	
	protected void waitForElement(String xpath) {
		new WebDriverWait(driver, 10).until(ExpectedConditions.visibilityOfElementLocated(By.xpath(xpath)));
	}

	protected void close() {
		driver.quit();
	}
}
