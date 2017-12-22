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

public class Example14 {
	
	private static final boolean ssl = true;
	private static ArthikaHFT wrapper;
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
	
	public Example14(){
		
	}
	
	public static void main(String[] args) throws IOException, InterruptedException, DecoderException, KeyManagementException, CertificateException, NoSuchAlgorithmException, KeyStoreException{
		
		// get properties from file
    	getProperties();

    	wrapper = new ArthikaHFT(domain, url_stream, url_polling, url_challenge, url_token, user, password, authentication_port, request_port, ssl, ssl_cert);
		
		wrapper.doAuthentication();

		// HISTORICAL PRICE POLLING
		
		// get tinterfaces
		List<ArthikaHFT.tinterfaceTick> tinterfaceTickList = wrapper.getInterface();
		
		System.out.println("Starting Candle list 1");
		List<String> tinterfacelist = null;
		if (tinterfaceTickList!=null && tinterfaceTickList.size()>1){
			tinterfacelist = new ArrayList<String>();
			tinterfacelist.add(tinterfaceTickList.get(1).name);
		}
		List<ArthikaHFT.candleTick> candleTickList1 = wrapper.getHistoricalPrice(Arrays.asList("EUR/USD", "EUR/GBP", "EUR/JPY", "GBP/JPY", "GBP/USD", "USD/JPY"), tinterfacelist, ArthikaHFT.CANDLE_GRANULARITY_10MINUTES, ArthikaHFT.SIDE_ASK, 5);
		for (ArthikaHFT.candleTick tick : candleTickList1){
			System.out.println("Security: " + tick.security + " tinterface: " + tick.tinterface +  " TimeStamp: " + tick.timestamp +  " Side: " + tick.side + " Open: " + tick.open + " High: " + tick.high + " Low: " + tick.low + " Close: " + tick.close + " Ticks: " + tick.ticks);
		}
		System.out.println("Candle list 1 Finished");
		
		System.out.println("Starting Candle list 2");
		List<ArthikaHFT.candleTick> candleTickList2 = wrapper.getHistoricalPrice(Arrays.asList("EUR/USD"), null, ArthikaHFT.CANDLE_GRANULARITY_30MINUTES, ArthikaHFT.SIDE_BID, 3);
		for (ArthikaHFT.candleTick tick : candleTickList2){
			System.out.println("Security: " + tick.security + " tinterface: " + tick.tinterface +  " TimeStamp: " + tick.timestamp +  " Side: " + tick.side + " Open: " + tick.open + " High: " + tick.high + " Low: " + tick.low + " Close: " + tick.close + " Ticks: " + tick.ticks);
		}
		System.out.println("Candle list 2 Finished");
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
