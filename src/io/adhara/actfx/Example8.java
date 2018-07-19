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

public class Example8 {
	
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
	
	public Example8(){
		
	}
	
	public static void main(String[] args) throws IOException, InterruptedException, DecoderException, KeyManagementException, CertificateException, NoSuchAlgorithmException, KeyStoreException{
		
		// get properties from file
    	getProperties();

    	wrapper = new AdharaHFT(domain, url_stream, url_polling, url_challenge, url_token, user, password, authentication_port, request_port, ssl, ssl_cert);
		
		wrapper.doAuthentication();
		
		// CANCEL PENDING ORDER WITH ORDER POLLING
		
		// get tinterfaces
		List<AdharaHFT.tinterfaceTick> tinterfaceTickList = wrapper.getInterface();
		String tinterface1 = tinterfaceTickList.get(0).name;
		
		System.out.println("Starting Polling1");
		List<AdharaHFT.orderTick> orderTickList1 = wrapper.getOrder(null, null, Arrays.asList(AdharaHFT.ORDERTYPE_PENDING, AdharaHFT.ORDERTYPE_CANCELED));
		for (AdharaHFT.orderTick tick : orderTickList1){
			System.out.println("TempId: " + tick.tempid + " OrderId: " + tick.orderid + " Security: " + tick.security + " Account: " + tick.account + " Quantity: " + tick.quantity + " Type: " + tick.type + " Side: " + tick.side + " Status: " + tick.status + " Price: " + tick.limitprice);
		}
		System.out.println("Polling1 Finished");
		Thread.sleep(2000);
		
		// get current price
        double price = 0.0;
        List<AdharaHFT.priceTick> priceTickList1 = wrapper.getPrice(Arrays.asList("EUR/USD"), Arrays.asList(tinterface1), AdharaHFT.GRANULARITY_TOB, 1);
        for (AdharaHFT.priceTick tick : priceTickList1)
        {
            price = tick.price;
        }
		
		// Create pending order. If buy, order price must be lower than current price
		AdharaHFT.orderRequest order1 = new AdharaHFT.orderRequest();
		order1.security = "EUR/USD";
		order1.tinterface = tinterface1;
		order1.quantity = 500000;
		order1.side = AdharaHFT.SIDE_BUY;
		order1.type = AdharaHFT.TYPE_LIMIT;
		order1.timeinforce = AdharaHFT.VALIDITY_DAY;
		order1.price = price - 0.01;
		
		System.out.println("Sending order");
		int tempid = -1;
		String fixid = "";
		List<AdharaHFT.orderRequest> orderList = wrapper.setOrder(Arrays.asList(order1));
		for (AdharaHFT.orderRequest orderresponse : orderList){
			System.out.println("Id: " + orderresponse.tempid + " Security: " + orderresponse.security + " Side: " + orderresponse.side + " Quantity: " + orderresponse.quantity + " Price: " + orderresponse.price + " Type: " + orderresponse.type);
			tempid = orderresponse.tempid;
		}
		System.out.println("Order sended order");
		Thread.sleep(2000);
		
		System.out.println("Starting Polling2");
		List<AdharaHFT.orderTick> orderTickList2 = wrapper.getOrder(null, null, Arrays.asList(AdharaHFT.ORDERTYPE_PENDING, AdharaHFT.ORDERTYPE_CANCELED));
		for (AdharaHFT.orderTick tick : orderTickList2){
			System.out.println("TempId: " + tick.tempid + " OrderId: " + tick.orderid + " Security: " + tick.security + " Account: " + tick.account + " Quantity: " + tick.quantity + " Type: " + tick.type + " Side: " + tick.side + " Status: " + tick.status + " Price: " + tick.limitprice);
			if (tempid==tick.tempid){
				fixid = tick.fixid;
			}
		}
		System.out.println("Polling2 Finished");
		Thread.sleep(2000);
		
		System.out.println("Cancel order");
		List<AdharaHFT.cancelTick> cancelList = wrapper.cancelOrder(Arrays.asList(fixid));
		for (AdharaHFT.cancelTick cancelresponse : cancelList){
			System.out.println("FixId: " + cancelresponse.fixid + " Result: " + cancelresponse.result);
		}
		System.out.println("Order canceled");
		Thread.sleep(2000);
		
		System.out.println("Starting Polling3");
		List<AdharaHFT.orderTick> orderTickList3 = wrapper.getOrder(null, null, Arrays.asList(AdharaHFT.ORDERTYPE_PENDING, AdharaHFT.ORDERTYPE_CANCELED));
		for (AdharaHFT.orderTick tick : orderTickList3){
			System.out.println("TempId: " + tick.tempid + " OrderId: " + tick.orderid + " Security: " + tick.security + " Account: " + tick.account + " Quantity: " + tick.quantity + " Type: " + tick.type + " Side: " + tick.side + " Status: " + tick.status + " Price: " + tick.limitprice);
		}
		System.out.println("Polling3 Finished");
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
