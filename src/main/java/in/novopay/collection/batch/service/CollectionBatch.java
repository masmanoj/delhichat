package in.novopay.collection.batch.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import javax.crypto.Cipher;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;


public class CollectionBatch {
	public static final String ALGORITHM = "RSA";
	static String tenant = "default";
	static String Host =  "bharatbank.idfcbank.com";
	static String Referer = "https://bharatbank.idfcbank.com/novobank/app";
	
	public static void execute( final CollectionService service, final String authorization,
			Integer receiptStartNum, String filename, Integer threadPoolSize){
		ExecutorService executorService = Executors.newFixedThreadPool(threadPoolSize);
		List<Callable<Object>> collectionPosters = new ArrayList<Callable<Object>>();
		File collections = new File(filename);
		final List<CollectionData> colls = new ArrayList<>();
		try {
			FileInputStream fis = new FileInputStream(collections);
			BufferedReader br = new BufferedReader(new InputStreamReader(fis));
			String entry;
			while((entry = br.readLine()) != null) {
				if(StringUtils.isEmpty(entry))
					continue;
				final String[] items = entry.split(",");
				final Long loanId = Long.parseLong(items[0]);
				final String amount = items[1];
				final String transDate = items[2];
				CollectionData coll = new CollectionData(loanId, amount, transDate);
				colls.add(coll);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		//fetch Otp
		
			JsonArray otps = service.getOtp(tenant, authorization
				  //, Host, Referer
				  , (colls.size()+100));
		  final List<String> otpList = new ArrayList<> ();
		  for(int i=0; i<otps.size(); i++) {
			  otpList.add(otps.get(i).getAsJsonObject().get("otp").getAsString());
		  }
		
		
		
		int batchSize = (int)Math.ceil(colls.size()/threadPoolSize);
		int fromIndex = 0;
        int size = colls.size();
        int toIndex = (batchSize > size - 1)? size : batchSize ;
        boolean lastBatch = false;
        int loopCount = size/batchSize + 1;
        
        for (long i=0; i < loopCount; i++) { 
            List<CollectionData> subList = colls.subList(fromIndex, toIndex);
            List<String> otpSubList = otpList.subList(fromIndex, toIndex);
            CollectionPoster poster = new CollectionPoster(subList, service, receiptStartNum, authorization, otpSubList);
            collectionPosters.add(Executors.callable(poster));
            if(lastBatch)
                break;

            if(toIndex + batchSize > size - 1)
                lastBatch = true;

            fromIndex = fromIndex + batchSize;
            toIndex = (toIndex + batchSize > size - 1)? size : toIndex + batchSize;
            receiptStartNum +=  batchSize;
        }

        try {
            List<Future<Object>> responses = executorService.invokeAll(collectionPosters);
            System.out.println("Request process success!");
            executorService.shutdown();
        } catch (InterruptedException e1) {
            e1.printStackTrace();
        }
	}
	
	
	public static void main(String[] args) {
		System.out.println("Initializing the stage...");
		String userHome = System.getProperty("user.home");
		Properties prop = new Properties();
		FileInputStream fis = null;
		try{
			File file = new File(userHome + "/" + "collections.properties");
			fis = new FileInputStream(file);
			prop.load(fis);
			
		}catch(IOException e){
			System.out.println("IO exception  : property file not found!");
		}
		
		String usrname = prop.getProperty("username");
		String password = prop.getProperty("password");
		String filename =userHome + "/" +  prop.getProperty("filename");
		Integer receiptStartNum = Integer.parseInt(prop.getProperty("receiptStartNum"));
		Integer threadPoolSize = Integer.parseInt(prop.getProperty("threadPoolSize"));
		String url = prop.getProperty("url");
		Referer = prop.getProperty("referer");
		Host = prop.getProperty("host");  
		
		final CollectionService service  =  CollectionWebHook.createCollectionService(url);
		  JsonObject jsonResponse = service.getRsaKey(tenant);
		  
		  String pem =  jsonResponse.get("keyValue").getAsString();
		  String encdPassword = encMyPassword(pem, password);
		  JsonObject authReq = new JsonObject();
		  authReq.addProperty("username", usrname);
		  authReq.addProperty("password", encdPassword);
		  JsonObject auth = service.authenticate(tenant, authReq);
		  
		  String sessionKey = auth.get("sessionKey").getAsString();
		  
		  String authorization  =  "Custom "+sessionKey;
		  System.out.println("User Authorized!!!");
		  
		  
		  
		  execute(service, authorization, receiptStartNum, filename, threadPoolSize);
		  
          //System.out.println(otps.toString());
		  
		
	}
	
	
	
	public static String encMyPassword(String  pem, String password) {

		try {
			// plain text password tobe encrypted
			String plaintext = "password";
			// base64 encoded public rsa key from server
			//String pem = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCYB7vrerWtKxg3A/P9dTWdjnBcrctt6+yfFuBaMZ69hbBkYB9Qiu+SsvrsE+wS4NvOJFv3/17S14nRLnGungv4qBNVM3BlhIiZm6V15L0Vu2qq/Yhl9oYRk5wlotLz4C8lAG2qUeD+n1+LsdSZ5uEBCP71ARQVPYhyIhgZ8RkpQQIDAQAB";

			final byte[] publicKeyAsBytes = Base64.decodeBase64(pem);

			KeyFactory kf = KeyFactory.getInstance(ALGORITHM);
			PublicKey publicKey = kf.generatePublic(new X509EncodedKeySpec(
					publicKeyAsBytes));
			Cipher encrypt = Cipher.getInstance(ALGORITHM);
			encrypt.init(Cipher.ENCRYPT_MODE, publicKey);
			// get encrypted bytes
			byte[] encrypted = encrypt.doFinal(plaintext.getBytes());
			// base64 encoded password to be sent to server
			String encryptedString = Base64.encodeBase64String(encrypted);

			return encryptedString;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
}


