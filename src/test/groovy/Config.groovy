import org.junit.Test
import org.openqa.selenium.WebDriver
import org.openqa.selenium.htmlunit.HtmlUnitDriver

class Config {
	public static final String baseUrl = "http://127.0.0.1:8080";
	
	public static WebDriver getDriver() {
		WebDriver driver = new HtmlUnitDriver()
		driver.setJavascriptEnabled(true)
		return driver
	}
}
