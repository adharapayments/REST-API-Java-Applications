package io.adhara.actfx;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import org.apache.commons.codec.DecoderException;

public class Example13 {
	
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
	
	public Example13(){
		
	}
	
	public static void main(String[] args) throws IOException, InterruptedException, DecoderException, KeyManagementException, CertificateException, NoSuchAlgorithmException, KeyStoreException{
		
		// get properties from file
    	getProperties();

    	wrapper = new AdharaHFT(domain, url_stream, url_polling, url_challenge, url_token, user, password, authentication_port, request_port, ssl, ssl_cert);
		
		wrapper.doAuthentication();
		
		// MULTIPLE ORDER CREATION
		
		// get tinterfaces
		List<AdharaHFT.tinterfaceTick> tinterfaceTickList = wrapper.getInterface();
		String tinterface1 = tinterfaceTickList.get(0).name;
		String tinterface2;
		if (tinterfaceTickList.size()>1){
			tinterface2 = tinterfaceTickList.get(1).name;
		}
		else{
			tinterface2 = tinterfaceTickList.get(0).name;
		}
		
		AdharaHFT.orderRequest order1 = new AdharaHFT.orderRequest();
		order1.security = "EUR/USD";
		order1.tinterface = tinterface2;
		order1.quantity = 1000000;
		order1.side = AdharaHFT.SIDE_BUY;
		order1.type = AdharaHFT.TYPE_MARKET;
		order1.timeinforce = AdharaHFT.VALIDITY_DAY;
		
		AdharaHFT.orderRequest order2 = new AdharaHFT.orderRequest();
		order2.security = "EUR/USD";
		order2.tinterface = tinterface1;
		order2.quantity = 1000000;
		order2.side = AdharaHFT.SIDE_SELL;
		order2.type = AdharaHFT.TYPE_MARKET;
		order2.timeinforce = AdharaHFT.VALIDITY_DAY;
		
		AdharaHFT.orderRequest order3 = new AdharaHFT.orderRequest();
		order3.security = "EUR/USD";
		order3.tinterface = tinterface2;
		order3.quantity = 2000000;
		order3.side = AdharaHFT.SIDE_BUY;
		order3.type = AdharaHFT.TYPE_MARKET;
		order3.timeinforce = AdharaHFT.VALIDITY_DAY;
		
		AdharaHFT.orderRequest order4 = new AdharaHFT.orderRequest();
		order4.security = "EUR/USD";
		order4.tinterface = tinterface1;
		order4.quantity = 2000000;
		order4.side = AdharaHFT.SIDE_SELL;
		order4.type = AdharaHFT.TYPE_MARKET;
		order4.timeinforce = AdharaHFT.VALIDITY_DAY;
		
		AdharaHFT.orderRequest order5 = new AdharaHFT.orderRequest();
		order5.security = "EUR/USD";
		order5.tinterface = tinterface2;
		order5.quantity = 1000000;
		order5.side = AdharaHFT.SIDE_BUY;
		order5.type = AdharaHFT.TYPE_MARKET;
		order5.timeinforce = AdharaHFT.VALIDITY_DAY;
		
		AdharaHFT.orderRequest order6 = new AdharaHFT.orderRequest();
		order6.security = "EUR/USD";
		order6.tinterface = tinterface1;
		order6.quantity = 1000000;
		order6.side = AdharaHFT.SIDE_SELL;
		order6.type = AdharaHFT.TYPE_MARKET;
		order6.timeinforce = AdharaHFT.VALIDITY_DAY;

		System.out.println("Starting Polling1");
		List<AdharaHFT.orderTick> orderTickList1 = wrapper.getOrder(Arrays.asList("EUR/USD"), null, null);
		for (AdharaHFT.orderTick tick : orderTickList1){
			System.out.println("TempId: " + tick.tempid + " OrderId: " + tick.orderid + " Security: " + tick.security + " Account: " + tick.account + " Quantity: " + tick.quantity + " Type: " + tick.type + " Side: " + tick.side + " Status: " + tick.status);
		}
		System.out.println("Polling1 Finished");
		Thread.sleep(5000);
		
		System.out.println("Sending order");
		List<AdharaHFT.orderRequest> orderList1 = wrapper.setOrder(Arrays.asList(order1, order2, order3, order4, order5, order6, order1, order2, order3, order4, order5, order6, order1, order2, order3, order4, order5, order6));
		for (AdharaHFT.orderRequest orderresponse : orderList1){
			System.out.println("Id: " + orderresponse.tempid + " Security: " + orderresponse.security + " Side: " + orderresponse.side + " Quantity: " + orderresponse.quantity + " Price: " + orderresponse.price + " Type: " + orderresponse.type);
		}
		List<AdharaHFT.orderRequest> orderList2 = wrapper.setOrder(Arrays.asList(order1, order2, order3, order4, order5, order6, order1, order2, order3, order4, order5, order6, order1, order2, order3, order4, order5, order6));
		for (AdharaHFT.orderRequest orderresponse : orderList2){
			System.out.println("Id: " + orderresponse.tempid + " Security: " + orderresponse.security + " Side: " + orderresponse.side + " Quantity: " + orderresponse.quantity + " Price: " + orderresponse.price + " Type: " + orderresponse.type);
		}
		List<AdharaHFT.orderRequest> orderList3 = wrapper.setOrder(Arrays.asList(order1, order2, order3, order4, order5, order6, order1, order2, order3, order4, order5, order6, order1, order2, order3, order4, order5, order6));
		for (AdharaHFT.orderRequest orderresponse : orderList3){
			System.out.println("Id: " + orderresponse.tempid + " Security: " + orderresponse.security + " Side: " + orderresponse.side + " Quantity: " + orderresponse.quantity + " Price: " + orderresponse.price + " Type: " + orderresponse.type);
		}
		System.out.println("Order sended");
		Thread.sleep(5000);
		
		System.out.println("Starting Polling2");
		List<AdharaHFT.orderTick> orderTickList2 = wrapper.getOrder(Arrays.asList("EUR/USD"), null, null);
		for (AdharaHFT.orderTick tick : orderTickList2){
			System.out.println("TempId: " + tick.tempid + " OrderId: " + tick.orderid + " Security: " + tick.security + " Account: " + tick.account + " Quantity: " + tick.finishedquantity + " Type: " + tick.type + " Side: " + tick.side + " Status: " + tick.status);
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
