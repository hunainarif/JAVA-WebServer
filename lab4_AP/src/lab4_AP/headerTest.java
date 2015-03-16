package lab4_AP;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Map;

import org.junit.AfterClass;
import org.junit.Test;

public class headerTest {

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Test
	public void test() {
		
		URL url;
		try {
			url = new URL("http://127.0.0.1:55555/abc.html");
			HttpURLConnection conn =     (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("HEAD");
		
			
			Map<String, List<String>> map = conn.getHeaderFields();
			for (Map.Entry<String, List<String>> entry : map.entrySet()) {
				System.out.println("Key : " + entry.getKey() + 
		                 " ,Value : " + entry.getValue());
			}
			
			conn.disconnect();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			fail();
			e.printStackTrace();
		}
	}

}
