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

class AdharaHFTListenerImp1 implements AdharaHFTListener {

	@Override
	public void timestampEvent(String timestamp) {
		System.out.println("Response timestamp: " + timestamp + " Contents:");
	}
	
	@Override
	public void heartbeatEvent() {
		System.out.println("Heartbeat!");
	}
	
	@Override
	public void messageEvent(String message) {
		System.out.println("Message from server: " + message);
	}

	@Override
	public void priceEvent(List<AdharaHFT.priceTick> priceTickList) {
		for (AdharaHFT.priceTick tick : priceTickList){
			System.out.println("Security: " + tick.security + " Price: " + String.format("%." + tick.pips + "f", tick.price) + " Side: " + tick.side + " TI: " + tick.tinterface + " Liquidity: " + tick.liquidity);
		}
	}
	
	@Override
    public void accountingEvent(AdharaHFT.accountingTick accountingTick) {
		System.out.println("StrategyPL: " + accountingTick.strategyPL + " TotalEquity: " + accountingTick.totalequity + " UsedMargin: " + accountingTick.usedmargin + " FreeMargin: " + accountingTick.freemargin);
    }

	@Override
	public void assetPositionEvent(List<AdharaHFT.assetPositionTick> assetPositionTickList) {
		for (AdharaHFT.assetPositionTick tick : assetPositionTickList){
			System.out.println("Asset: " + tick.asset + " Account: " + tick.account + " Exposure: " + tick.exposure + " TotalRisk: " + tick.totalrisk);
		}
	}

	@Override
	public void securityPositionEvent(List<AdharaHFT.securityPositionTick> securityPositionTickList) {
		for (AdharaHFT.securityPositionTick tick : securityPositionTickList){
			System.out.println("Security: " + tick.security + " Account: " + tick.account + " Equity: " + tick.equity + " Exposure: " + tick.exposure + " Price: " + tick.price + " Pips: " + tick.pips);
		}
	}

	@Override
	public void positionHeartbeatEvent(AdharaHFT.positionHeartbeat positionHeartbeatList) {
		System.out.print("Asset: " );
		for (int i=0; i<positionHeartbeatList.asset.size(); i++){
			System.out.print(positionHeartbeatList.asset.get(i));
			if (i<positionHeartbeatList.asset.size()-1){
				System.out.print(",");
			}
		}
		System.out.print(" Security: " );
		for (int i=0; i<positionHeartbeatList.security.size(); i++){
			System.out.print(positionHeartbeatList.security.get(i));
			if (i<positionHeartbeatList.security.size()-1){
				System.out.print(", ");
			}
		}
		System.out.print(" Account: " );
		for (int i=0; i<positionHeartbeatList.account.size(); i++){
			System.out.print(positionHeartbeatList.account.get(i));
			if (i<positionHeartbeatList.account.size()-1){
				System.out.print(",");
			}
		}
		System.out.println();
	}

	@Override
	public void orderEvent(List<AdharaHFT.orderTick> orderTickList) {
		for (AdharaHFT.orderTick tick : orderTickList){
			System.out.println("TempId: " + tick.tempid + " OrderId: " + tick.orderid + " Security: " + tick.security + " Account: " + tick.account + " Quantity: " + tick.quantity + " Type: " + tick.type + " Side: " + tick.side + " Status: " + tick.status);
		}
	}

	@Override
	public void orderHeartbeatEvent(AdharaHFT.orderHeartbeat orderHeartbeat) {
		System.out.print("Security: " );
		for (int i=0; i<orderHeartbeat.security.size(); i++){
			System.out.print(orderHeartbeat.security.get(i));
			if (i<orderHeartbeat.security.size()-1){
				System.out.print(", ");
			}
		}
		System.out.print(" Interface: " );
		for (int i=0; i<orderHeartbeat.tinterface.size(); i++){
			System.out.print(orderHeartbeat.tinterface.get(i));
			if (i<orderHeartbeat.tinterface.size()-1){
				System.out.print(",");
			}
		}
		System.out.println();
	}    
}

public class Example1 {
	
	private static final boolean ssl = false;
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
	private static int interval;
	
	public Example1(){
		
	}
	
	public static void main(String[] args) throws IOException, InterruptedException, DecoderException, KeyManagementException, CertificateException, NoSuchAlgorithmException, KeyStoreException{
		
		// get properties from file
    	getProperties();

		wrapper = new AdharaHFT(domain, url_stream, url_polling, url_challenge, url_token, user, password, authentication_port, request_port, ssl, ssl_cert);
		
		wrapper.doAuthentication();
		
		// PRICE STREAMING
		
		// get tinterfaces
		List<AdharaHFT.tinterfaceTick> tinterfaceTickList = wrapper.getInterface();
		
		// Open first price streaming for one security in all tinterfaces
		long id1 = wrapper.getPriceBegin(Arrays.asList("GBP/USD"), null, AdharaHFT.GRANULARITY_TOB, 1, interval, new AdharaHFTListenerImp1());
		Thread.sleep(5000);
		
		// Open second price streaming for two securities in the two first tinterfaces
		List<String> tinterfacelist = null;
		if (tinterfaceTickList!=null && tinterfaceTickList.size()>1){
			tinterfacelist = new ArrayList<String>();
			tinterfacelist.add(tinterfaceTickList.get(0).name);
			tinterfacelist.add(tinterfaceTickList.get(1).name);
		}
		long id2 = wrapper.getPriceBegin(Arrays.asList("EUR/USD", "GBP/JPY"), tinterfacelist, AdharaHFT.GRANULARITY_FAB, 2, interval, new AdharaHFTListenerImp1());
		Thread.sleep(5000);
		
		// Close second price streaming
		wrapper.getPriceEnd(id2);
		Thread.sleep(5000);
		
		// Close first price streaming
		wrapper.getPriceEnd(id1);
		Thread.sleep(5000);
		
		// Open third price streaming for six securities in the first tinterface
		if (tinterfaceTickList!=null && !tinterfaceTickList.isEmpty()){
			tinterfacelist = new ArrayList<String>();
			tinterfacelist.add(tinterfaceTickList.get(0).name);
		}
		long id3 = wrapper.getPriceBegin(Arrays.asList("EUR/USD", "EUR/GBP", "EUR/JPY", "GBP/JPY", "GBP/USD", "USD/JPY"), tinterfacelist, AdharaHFT.GRANULARITY_TOB, 1, interval, new AdharaHFTListenerImp1());
		Thread.sleep(5000);
		
		// Close third price streaming
		wrapper.getPriceEnd(id3);
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
			interval = Integer.parseInt(prop.getProperty("interval"));
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
