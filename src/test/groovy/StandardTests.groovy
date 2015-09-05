import org.junit.Test

import org.openqa.selenium.WebDriver
import org.openqa.selenium.By

import org.junit.runner.RunWith
import org.junit.runners.Parameterized
import org.junit.runners.Parameterized.Parameter
import org.junit.runners.Parameterized.Parameters

import Config
import Common

@RunWith(Parameterized.class)
class StandardTests {
	@Parameters
	public static Iterable<Object[]> data() {
		return [
			// cas, cafile, method, login page expected text, main page expected text
			
			// HTTP should succeed
			[ "http://127.0.0.1:8081/cas.php", null, null, "Authenticated as user123", "Authenticated as user123" ] as Object[],
			[ "http://127.0.0.1:8081/cas.php", null, "GET", "Authenticated as user123", "Authenticated as user123" ] as Object[],
			[ "http://127.0.0.1:8081/cas.php", null, "POST", "Authenticated as user123", "Authenticated as user123" ] as Object[],
			
			// HTTPS should succeed
			[ "https://127.0.0.1:8444/cas.php", "/tmp/correct.crt", null, "Authenticated as user123", "Authenticated as user123" ] as Object[],
			[ "https://127.0.0.1:8444/cas.php", "/tmp/correct.crt", "GET", "Authenticated as user123", "Authenticated as user123" ] as Object[],
			[ "https://127.0.0.1:8444/cas.php", "/tmp/correct.crt", "POST", "Authenticated as user123", "Authenticated as user123" ] as Object[],
			
			// system CAfile does not contain this self-signed certificate - should fail
			[ "https://127.0.0.1:8444/cas.php", null, null, "CAS server is unavailable", "Not authenticated." ] as Object[],
			[ "https://127.0.0.1:8444/cas.php", null, "GET", "CAS server is unavailable", "Not authenticated." ] as Object[],
			[ "https://127.0.0.1:8444/cas.php", null, "POST", "CAS server is unavailable", "Not authenticated." ] as Object[],
			// wrongcn.crt does not contain correct.crt - should fail
			[ "https://127.0.0.1:8444/cas.php", "/tmp/wrongcn.crt", null, "CAS server is unavailable", "Not authenticated." ] as Object[],
			[ "https://127.0.0.1:8444/cas.php", "/tmp/wrongcn.crt", "GET", "CAS server is unavailable", "Not authenticated." ] as Object[],
			[ "https://127.0.0.1:8444/cas.php", "/tmp/wrongcn.crt", "POST", "CAS server is unavailable", "Not authenticated." ] as Object[],
			
			// system CAfile does not contain this self-signed certificate - should fail
			[ "https://127.0.0.1:8445/cas.php", null, null, "CAS server is unavailable", "Not authenticated." ] as Object[],
			[ "https://127.0.0.1:8445/cas.php", null, "GET", "CAS server is unavailable", "Not authenticated." ] as Object[],
			[ "https://127.0.0.1:8445/cas.php", null, "POST", "CAS server is unavailable", "Not authenticated." ] as Object[],
			// correct.crt does not contain wrongcn.crt - should fail
			[ "https://127.0.0.1:8445/cas.php", "/tmp/correct.crt", null, "CAS server is unavailable", "Not authenticated." ] as Object[],
			[ "https://127.0.0.1:8445/cas.php", "/tmp/correct.crt", "GET", "CAS server is unavailable", "Not authenticated." ] as Object[],
			[ "https://127.0.0.1:8445/cas.php", "/tmp/correct.crt", "POST", "CAS server is unavailable", "Not authenticated." ] as Object[],
			// wrongcn.crt is issued to 127.0.0.2, not 127.0.0.1 - should fail
			[ "https://127.0.0.1:8445/cas.php", "/tmp/wrongcn.crt", null, "CAS server is unavailable", "Not authenticated." ] as Object[],
			[ "https://127.0.0.1:8445/cas.php", "/tmp/wrongcn.crt", "GET", "CAS server is unavailable", "Not authenticated." ] as Object[],
			[ "https://127.0.0.1:8445/cas.php", "/tmp/wrongcn.crt", "POST", "CAS server is unavailable", "Not authenticated." ] as Object[],
		]
	}
	
	@Parameter(0)
	public String cas
	@Parameter(1)
	public String cafile
	@Parameter(2)
	public String method
	@Parameter(3)
	public String expectLogin
	@Parameter(4)
	public String expectMain
	
	@Test
	public void testSinglePage() {
		WebDriver driver = Common.set(cas, cafile, method)
		
		def url = Config.baseUrl + "/basic/"
		Common.loginSingle(url, driver)
		
		if(method.equals("GET")) {
			assert driver.getCurrentUrl().contains("ticket=")
		} else {
			assert !driver.getCurrentUrl().contains("ticket=")
		}
		
		// Post-login
		assert driver.getPageSource().contains(expectLogin)
	}
	
	@Test
	public void testMultiPage() {
		WebDriver driver = Common.set(cas, cafile, method)
		
		def url = Config.baseUrl + "/login-page/"
		Common.loginMulti(url, driver)
		
		// Post-login
		assert driver.getPageSource().contains(expectLogin)
		
		// Main page (again)
		driver.get(url)
		assert driver.getPageSource().contains(expectMain)
	}
}
