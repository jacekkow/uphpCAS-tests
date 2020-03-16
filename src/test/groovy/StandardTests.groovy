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
	@Parameters(name = "{0}")
	public static Iterable<Object[]> data() {
		return [
			// name, cas, cafile, method, login page expected text, main page expected text
			
			// HTTP should succeed
			[ "HTTP",  "http://127.0.0.1:8081/cas.php", null, null, "Authenticated as user123", "Authenticated as user123" ] as Object[],
			[ "HTTP GET", "http://127.0.0.1:8081/cas.php", null, "GET", "Authenticated as user123", "Authenticated as user123" ] as Object[],
			[ "HTTP POST", "http://127.0.0.1:8081/cas.php", null, "POST", "Authenticated as user123", "Authenticated as user123" ] as Object[],
			
			// HTTPS should succeed
			[ "HTTPS", "https://127.0.0.1:8444/cas.php", "/tmp/ca.crt", null, "Authenticated as user123", "Authenticated as user123" ] as Object[],
			[ "HTTPS GET", "https://127.0.0.1:8444/cas.php", "/tmp/ca.crt", "GET", "Authenticated as user123", "Authenticated as user123" ] as Object[],
			[ "HTTPS POST", "https://127.0.0.1:8444/cas.php", "/tmp/ca.crt", "POST", "Authenticated as user123", "Authenticated as user123" ] as Object[],

			// system CAfile does not contain CA certificate - should fail
			[ "HTTPS SysCA", "https://127.0.0.1:8444/cas.php", null, null, "CAS server is unavailable", "Not authenticated." ] as Object[],
			[ "HTTPS SysCA GET", "https://127.0.0.1:8444/cas.php", null, "GET", "CAS server is unavailable", "Not authenticated." ] as Object[],
			[ "HTTPS SysCA POST", "https://127.0.0.1:8444/cas.php", null, "POST", "CAS server is unavailable", "Not authenticated." ] as Object[],
			// correct.crt is a leaf certificate - should fail
			[ "HTTPS LeafCA", "https://127.0.0.1:8444/cas.php", "/tmp/correct.crt", null, "CAS server is unavailable", "Not authenticated." ] as Object[],
			[ "HTTPS LeafCA GET", "https://127.0.0.1:8444/cas.php", "/tmp/correct.crt", "GET", "CAS server is unavailable", "Not authenticated." ] as Object[],
			[ "HTTPS LeafCA POST", "https://127.0.0.1:8444/cas.php", "/tmp/correct.crt", "POST", "CAS server is unavailable", "Not authenticated." ] as Object[],
			// wrongcn.crt does not contain correct.crt - should fail
			[ "HTTPS WrongCA", "https://127.0.0.1:8444/cas.php", "/tmp/wrongcn.crt", null, "CAS server is unavailable", "Not authenticated." ] as Object[],
			[ "HTTPS WrongCA GET", "https://127.0.0.1:8444/cas.php", "/tmp/wrongcn.crt", "GET", "CAS server is unavailable", "Not authenticated." ] as Object[],
			[ "HTTPS WrongCA POST", "https://127.0.0.1:8444/cas.php", "/tmp/wrongcn.crt", "POST", "CAS server is unavailable", "Not authenticated." ] as Object[],
			
			// system CAfile does not contain this self-signed certificate - should fail
			[ "HTTPS2 SysCA", "https://127.0.0.1:8445/cas.php", null, null, "CAS server is unavailable", "Not authenticated." ] as Object[],
			[ "HTTPS2 SysCA GET", "https://127.0.0.1:8445/cas.php", null, "GET", "CAS server is unavailable", "Not authenticated." ] as Object[],
			[ "HTTPS2 SysCA POST", "https://127.0.0.1:8445/cas.php", null, "POST", "CAS server is unavailable", "Not authenticated." ] as Object[],
			// correct.crt does not contain wrongcn.crt - should fail
			[ "HTTPS2 WrongCA", "https://127.0.0.1:8445/cas.php", "/tmp/correct.crt", null, "CAS server is unavailable", "Not authenticated." ] as Object[],
			[ "HTTPS2 WrongCA GET", "https://127.0.0.1:8445/cas.php", "/tmp/correct.crt", "GET", "CAS server is unavailable", "Not authenticated." ] as Object[],
			[ "HTTPS2 WrongCA POST", "https://127.0.0.1:8445/cas.php", "/tmp/correct.crt", "POST", "CAS server is unavailable", "Not authenticated." ] as Object[],
			// wrongcn.crt is a leaf certificate - should fail
			[ "HTTPS2 WrongCN", "https://127.0.0.1:8445/cas.php", "/tmp/wrongcn.crt", null, "CAS server is unavailable", "Not authenticated." ] as Object[],
			[ "HTTPS2 WrongCN GET", "https://127.0.0.1:8445/cas.php", "/tmp/wrongcn.crt", "GET", "CAS server is unavailable", "Not authenticated." ] as Object[],
			[ "HTTPS2 WrongCN POST", "https://127.0.0.1:8445/cas.php", "/tmp/wrongcn.crt", "POST", "CAS server is unavailable", "Not authenticated." ] as Object[],
			// wrongcn.crt is issued to 127.0.0.2, not 127.0.0.1 - should fail
			[ "HTTPS2 CA+WrongCN", "https://127.0.0.1:8445/cas.php", "/tmp/ca.crt", null, "CAS server is unavailable", "Not authenticated." ] as Object[],
			[ "HTTPS2 CA+WrongCN GET", "https://127.0.0.1:8445/cas.php", "/tmp/ca.crt", "GET", "CAS server is unavailable", "Not authenticated." ] as Object[],
			[ "HTTPS2 CA+WrongCN POST", "https://127.0.0.1:8445/cas.php", "/tmp/ca.crt", "POST", "CAS server is unavailable", "Not authenticated." ] as Object[],
		]
	}
	
	@Parameter(0)
	public String name
	@Parameter(1)
	public String cas
	@Parameter(2)
	public String cafile
	@Parameter(3)
	public String method
	@Parameter(4)
	public String expectLogin
	@Parameter(5)
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
