# REST API Java Applications
This repository contains complete Application Examples using our REST API wrapper

*  Example 0: GET ACCOUNTS AND TINTERFACES
*  Example 1: PRICE STREAMING
*  Example 2: PRICE POLLING
*  Example 3: POSITION STREAMING
*  Example 4: POSITION POLLING
*  Example 5: ORDER STREAMING
*  Example 6: ORDER POLLING
*  Example 7: ORDER CREATION
*  Example 8: CANCEL PENDING ORDER WITH ORDER POLLING
*  Example 9: MODIFY PENDING ORDER WITH ORDER POLLING
* Example 10: CANCEL PENDING ORDER WITH ORDER STREAMING
* Example 11: MODIFY PENDING ORDER WITH ORDER STREAMING
* Example 12: STRATEGY
* Example 13: MULTIPLE ORDER CREATION
* Example 14: GET HISTORICAL PRICE

### Pre-requisites:
Will users need previous registration, account, strategy set-up...? After all, these examples require a pre-existing strategy
JDK, IDE...

### How to:

**1. Clone this repository to the location of your choice** 

The repository contains the wrapper and all the examples listed above together with the classes needed. 

**2. Modify config.properties file with your settings** 

```
domain=http://demo.arthikatrading.com
user=demo
password=demo
```

**3. Modify the following lines in the Java program you would like to run.** 

From here on we will assume it is Example1.java.
```
long id1 = wrapper.getPriceBegin(Arrays.asList("GBP/USD"), null, ArthikaHFT.GRANULARITY_TOB, 1, interval, new ArthikaHFTListenerImp1());
```

In case you want to disable ssl protocol, change the following line:
```
private static final boolean ssl = true;
```

**4. Run the examples using the '.sh' script such as this one:**
```javascript
$ export JARS=lib/commons-codec-1.9.jar:lib/commons-logging-1.2.jar:lib/fluent-hc-4.5.jar:lib/httpclient-4.5.jar:lib/httpclient-cache-4.5.jar:lib/httpclient-win-4.5.jar:lib/httpcore-4.4.1.jar:lib/httpmime-4.5.jar:lib/jackson-all-1.9.9.jar:lib/jna-4.1.0.jar:lib/jna-platform-4.1.0.jar

$ javac -cp $JARS src/Example1.java
```

**5. Execute the sample.**
```javascript
$ export JARS=lib/commons-codec-1.9.jar:lib/commons-logging-1.2.jar:lib/fluent-hc-4.5.jar:lib/httpclient-4.5.jar:lib/httpclient-cache-4.5.jar:lib/httpclient-win-4.5.jar:lib/httpcore-4.4.1.jar:lib/httpmime-4.5.jar:lib/jackson-all-1.9.9.jar:lib/jna-4.1.0.jar:lib/jna-platform-4.1.0.jar

$ java -cp $JARS src/Example1

Response timestamp: 1445961714.977141 Contents:
Security: GBP/USD Price: 1.5312 Side: ask Liquidity: 10000000
Security: GBP/USD Price: 1.5312 Side: bid Liquidity: 10000000
Security: GBP/USD Price: 1.5312 Side: ask Liquidity: 10000000
Security: GBP/USD Price: 1.53129 Side: bid Liquidity: 2000000
Response timestamp: 1445961715.334755 Contents:
Security: GBP/USD Price: 1.5312 Side: ask Liquidity: 10000000
Security: GBP/USD Price: 1.5312 Side: bid Liquidity: 10000000
Security: GBP/USD Price: 1.5312 Side: ask Liquidity: 10000000
Security: GBP/USD Price: 1.53134 Side: bid Liquidity: 500000
```
#### Going further
Using IDE's such as Eclipse or Netbeans
