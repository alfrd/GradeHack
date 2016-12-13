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

		String[] alphanumeric = { "0", "1", "2", "3", "4", "5", "6", "7", "8",
				"9", "a", "b", "c", "d", "e", "f" };
		String signature = "";
		long threshold = 19000000;

		long timetime = System.currentTimeMillis();
		try {

			for (int j = 0; j < 20; j++) {

				// Make first connection so all after this one are "equal".
				// Maybe not needed
				String url = "https://eitn41.eit.lth.se:3119/ha4/addgrade.php?";
				url += "name=" + name + "&grade=" + grade;
				URL u2 = new URL(url);

				URLConnection uc2 = u2.openConnection();
				BufferedReader br2 = new BufferedReader(new InputStreamReader(
						uc2.getInputStream(), "UTF-8"));

				long longestResponse = 60000000;

				String pChar = ""; // This variable will hold a value that could
									// possibly be the correct one
				for (int i = 0; i < 16; i++) {
					int it = 0;
					long response = Long.MAX_VALUE;
					String tempsig = signature;
					tempsig += alphanumeric[i];
					while (it < 2) {
						String urlTest = url + "&signature=" + tempsig;
						URL u = new URL(urlTest);
						// System.out.println("URL: " + urlTest);
						long startTime = System.nanoTime();
						URLConnection uc = u.openConnection();
						BufferedReader br = new BufferedReader(
								new InputStreamReader(uc.getInputStream(),
										"UTF-8"));
						String line;
						while ((line = br.readLine()) != null) {
							// System.out.println("Read: " + line);
							if (line.equals("1")) {
								System.out.println("CRACKED");
								
							}
						}
						long responseTime = System.nanoTime() - startTime;
						if (responseTime < response) {
							response = responseTime;
							System.out.println("Response: " + response);
							// System.out.println("Shortest response: " +
							// response + " and longest: " + longestResponse);
						}
						it++;
					}

					System.out.println("Responstid på " + tempsig + " var "
							+ response);
					// Kollar om responstiden på det här talet var längre än
					// tidigare tal
					if (response > longestResponse) {
						long diff = response - longestResponse;
						System.out.println("Diff: " + diff
								+ " när tempsig var: " + tempsig + " och i = "
								+ i);

						System.out.println("2.");
						pChar = alphanumeric[i];
						longestResponse = response;

					}

				}
				signature += pChar;

			}
			System.out.println("Signaturen: " + signature);

			// StringSelection urlToClipboard = new StringSelection(url);
			// Clipboard clipboard = Toolkit.getDefaultToolkit()
			// .getSystemClipboard();
			// clipboard.setContents(urlToClipboard, urlToClipboard);

		} catch (IOException e) {
			e.printStackTrace();
		}

		long time3 = System.currentTimeMillis() - timetime;

		System.out.println("Det tog " + time3 / 60000 + " minuter! (" + time3 + " ms)");
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
