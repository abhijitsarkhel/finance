package in.indigenous.finance.util;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class ConfigTest {

	private static final String FINANCE_PAGE_URL_PROPERTY = "finance.page.url";
	private static final String FINANCE_PAGE_URL_VALUE = "https://in.finance.yahoo.com/";

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testGetPropertyFromFinance() {
		assertEquals("The value is not as expected", FINANCE_PAGE_URL_VALUE, Config.get(FINANCE_PAGE_URL_PROPERTY));
	}

}
