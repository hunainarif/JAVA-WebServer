package lab4_AP;

import static org.junit.Assert.*;

import org.junit.AfterClass;
import org.junit.Test;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


public class headTest {

	@Test
	public void test() {
		URL url;
		try {
			url = new URL("http://127.0.0.1:55555/abc.html");
			HttpURLConnection conn =     (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("HEAD");
			System.out.println("ac");
			
			
			BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			StringBuilder sb = new StringBuilder();
			String line;
		
			while ((line = rd.readLine()) != null) {
				System.out.println(line);
				sb.append(line);
			}
			
			
			conn.setDoOutput(true);
			System.out.println("ac");
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