import org.openqa.selenium.WebDriver
import org.openqa.selenium.By
import java.net.URLEncoder

import Config

class Common {
	public static WebDriver set(String casServer, String caFile = null,
			String method = null, String serviceUrl = null,
			WebDriver driver = null) {
		if(driver == null) {
			driver = Config.getDriver()
		}
		
		def url = Config.baseUrl + '/basic/set.php'
		url += '?cas=' + URLEncoder.encode(casServer)
		if(caFile != null) {
			url += '&cafile=' + URLEncoder.encode(caFile)
		}
		if(method != null) {
			url += '&method=' + URLEncoder.encode(method)
		}
		if(serviceUrl != null) {
			url += '&url=' + URLEncoder.encode(serviceUrl)
		}
		
		driver.get(url);
		
		assert driver.getPageSource().contains('CAS server set.');
		if(caFile != null) {
			assert driver.getPageSource().contains('CA file set.');
		} else {
			assert driver.getPageSource().contains('CA file unset.');
		}
		if(method != null) {
			assert driver.getPageSource().contains('Method set.');
		} else {
			assert driver.getPageSource().contains('Method unset.');
		}
		if(serviceUrl != null) {
			assert driver.getPageSource().contains('URL set.');
		} else {
			assert driver.getPageSource().contains('URL unset.');
		}
		
		
		return driver;
	}
	
	public static WebDriver login(String url, WebDriver driver,
			String user = null, String attrs = null) {
		if(user == null) {
			user = "user123"
		}
		
		assert driver.getCurrentUrl().contains('service=' + URLEncoder.encode(url))
		assert driver.getPageSource().contains('<form')
		
		def element = driver.findElement(By.name('user'))
		element.sendKeys(user)
		element.submit()
		
		return driver
	}
	
	public static WebDriver loginSingle(String url, WebDriver driver = null,
			String user = null, String attrs = null) {
		if(driver == null) {
			driver = Config.getDriver()
		}
		
		driver.get(url)
		this.login(url, driver, user, attrs)
		
		return driver
	}
	
	public static WebDriver loginMulti(String url, WebDriver driver = null,
			String user = null, String attrs = null) {
		if(driver == null) {
			driver = Config.getDriver()
		}
		
		driver.get(url)
		assert driver.getPageSource().contains("Not authenticated.")
		
		// Go to login page
		driver.findElement(By.tagName("a")).click()
		
		// Login
		this.login(url, driver, user, attrs)
		
		return driver
	}
}
