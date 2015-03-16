package lab4_AP;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.junit.AfterClass;
import org.junit.Test;

public class getTest {

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Test
	public void test() {
		URL url;
		try {
			url = new URL("http://127.0.0.1:55555/abc.html");
			HttpURLConnection conn =     (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			if (conn.getResponseCode() != 200) {
				throw new IOException(conn.getResponseMessage());
			}
			
			BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			StringBuilder sb = new StringBuilder();
			String line;
			while ((line = rd.readLine()) != null) {
				sb.append(line);
			}
		
			System.out.println(sb.toString());
			
			rd.close();
			conn.disconnect();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			fail();
			e.printStackTrace();
		}
	}

}
