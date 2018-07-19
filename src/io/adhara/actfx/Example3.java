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

class AdharaHFTListenerImp3 implements AdharaHFTListener {

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

public class Example3 {
	
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
	private static int interval;
	
	public Example3(){
		
	}
	
	public static void main(String[] args) throws IOException, InterruptedException, DecoderException, KeyManagementException, CertificateException, NoSuchAlgorithmException, KeyStoreException{
		
		// get properties from file
    	getProperties();

    	wrapper = new AdharaHFT(domain, url_stream, url_polling, url_challenge, url_token, user, password, authentication_port, request_port, ssl, ssl_cert);
		
		wrapper.doAuthentication();
		
		// POSITION STREAMING
		
		// get tinterfaces
		List<AdharaHFT.tinterfaceTick> tinterfaceTickList = wrapper.getInterface();

		String tinterface1 = tinterfaceTickList.get(0).name;
		AdharaHFT.orderRequest order1 = new AdharaHFT.orderRequest();
		order1.security = "EUR/USD";
		order1.tinterface = tinterface1;
		order1.quantity = 400000;
		order1.side = AdharaHFT.SIDE_SELL;
		order1.type = AdharaHFT.TYPE_LIMIT;
		order1.timeinforce = AdharaHFT.VALIDITY_DAY;
		order1.price = 1.15548;
		
		String tinterface2;
		if (tinterfaceTickList.size()>1){
			tinterface2 = tinterfaceTickList.get(1).name;
		}
		else{
			tinterface2 = tinterfaceTickList.get(0).name;
		}
		AdharaHFT.orderRequest order2 = new AdharaHFT.orderRequest();
		order2.security = "GBP/USD";
		order2.tinterface = tinterface2;
		order2.quantity = 500000;
		order2.side = AdharaHFT.SIDE_BUY;
		order2.type = AdharaHFT.TYPE_LIMIT;
		order2.timeinforce = AdharaHFT.VALIDITY_FILLORKILL;
		order2.price = 1.67389;
		
		// Open position streaming
		long id1 = wrapper.getPositionBegin(null, Arrays.asList("EUR/USD", "GBP/USD"), null, interval, new AdharaHFTListenerImp3());
		Thread.sleep(5000);
		
		// Create two orders
		List<AdharaHFT.orderRequest> orderList = wrapper.setOrder(Arrays.asList(order1, order2));
		for (AdharaHFT.orderRequest orderresponse : orderList){
			System.out.println("Id: " + orderresponse.tempid + " Security: " + orderresponse.security + " Side: " + orderresponse.side + " Quantity: " + orderresponse.quantity + " Price: " + orderresponse.price + " Type: " + orderresponse.type);
		}
		Thread.sleep(5000);
		
		// Close position streaming
		wrapper.getPositionEnd(id1);
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
