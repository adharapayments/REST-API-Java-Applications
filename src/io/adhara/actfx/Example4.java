package io.adhara.actfx;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import org.apache.commons.codec.DecoderException;

public class Example4 {
	
	private static final boolean ssl = true;
	private static AdharaHFT wrapper;
	private static String domain;
	private static String url_stream;
	private static String url_polling;
	private static String url_challenge;
	private static String url_token;
	private static String user;
	private static String password;
	private static String authentication_port;
	private static String request_port;
	private static String ssl_cert;
	
	public Example4(){
		
	}
	
	public static void main(String[] args) throws IOException, InterruptedException, DecoderException, KeyManagementException, CertificateException, NoSuchAlgorithmException, KeyStoreException{
		
		// get properties from file
    	getProperties();

    	wrapper = new AdharaHFT(domain, url_stream, url_polling, url_challenge, url_token, user, password, authentication_port, request_port, ssl, ssl_cert);
		
		wrapper.doAuthentication();
		
		// POSITION POLLING
		
		// get accounts
		List<AdharaHFT.accountTick> accountTickList = wrapper.getAccount();

		System.out.println("Starting Polling1");
		AdharaHFT.positionTick positionTickList1 = wrapper.getPosition(null, Arrays.asList("EUR/USD", "GBP/JPY", "GBP/USD"), null);
		System.out.println("StrategyPL: " + positionTickList1.accountingTick.strategyPL + " TotalEquity: " + positionTickList1.accountingTick.totalequity + " UsedMargin: " + positionTickList1.accountingTick.usedmargin + " FreeMargin: " + positionTickList1.accountingTick.freemargin);
		for (AdharaHFT.assetPositionTick tick : positionTickList1.assetPositionTickList){
			System.out.println("Asset: " + tick.asset + " Account: " + tick.account + " Exposure: " + tick.exposure + " TotalRisk: " + tick.totalrisk);
		}
		for (AdharaHFT.securityPositionTick tick : positionTickList1.securityPositionTickList){
			System.out.println("Security: " + tick.security + " Account: " + tick.account + " Equity: " + tick.equity + " Exposure: " + tick.exposure + " Price: " + tick.price + " Pips: " + tick.pips);
		}
		System.out.println("Polling1 Finished");
		
		System.out.println("Starting Polling2");
		List<String> accountlist = null;
		if (accountTickList!=null && accountTickList.size()>1){
			accountlist = new ArrayList<String>();
			accountlist.add(accountTickList.get(0).name);
			accountlist.add(accountTickList.get(1).name);
		}
		AdharaHFT.positionTick positionTickList2 = wrapper.getPosition(Arrays.asList("EUR", "GBP", "JPY", "USD"), null, accountlist);
		System.out.println("StrategyPL: " + positionTickList2.accountingTick.strategyPL + " TotalEquity: " + positionTickList2.accountingTick.totalequity + " UsedMargin: " + positionTickList2.accountingTick.usedmargin + " FreeMargin: " + positionTickList2.accountingTick.freemargin);
		for (AdharaHFT.assetPositionTick tick : positionTickList2.assetPositionTickList){
			System.out.println("Asset: " + tick.asset + " Account: " + tick.account + " Exposure: " + tick.exposure + " TotalRisk: " + tick.totalrisk);
		}
		for (AdharaHFT.securityPositionTick tick : positionTickList2.securityPositionTickList){
			System.out.println("Security: " + tick.security + " Account: " + tick.account + " Equity: " + tick.equity + " Exposure: " + tick.exposure + " Price: " + tick.price + " Pips: " + tick.pips);
		}
		System.out.println("Polling2 Finished");
	}
	
	public static void getProperties(){
    	Properties prop = new Properties();
		InputStream input = null;
		try {
			input = new FileInputStream("config.properties");
			prop.load(input);
			url_stream = prop.getProperty("url-stream");
			url_polling = prop.getProperty("url-polling");
			url_challenge = prop.getProperty("url-challenge");
			url_token = prop.getProperty("url-token");
			user = prop.getProperty("user");
			password = prop.getProperty("password");
			if (ssl){
				domain = prop.getProperty("ssl-domain");
				authentication_port = prop.getProperty("ssl-authentication-port");
				request_port = prop.getProperty("ssl-request-port");
				ssl_cert = prop.getProperty("ssl-cert");
			}
			else{
				domain = prop.getProperty("domain");
				authentication_port = prop.getProperty("authentication-port");
				request_port = prop.getProperty("request-port");
			}
		}
		catch (IOException ex) {
			ex.printStackTrace();
		}
		finally {
			if (input != null) {
				try {
					input.close();
				}
				catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
    }

}
