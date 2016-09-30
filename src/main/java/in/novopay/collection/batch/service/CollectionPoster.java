package in.novopay.collection.batch.service;

import java.util.List;

import retrofit.client.Response;

import com.google.gson.JsonObject;

public class CollectionPoster implements Runnable {
	String command = "repayment";
	private List<CollectionData> collections;
	private final CollectionService service;
	private final String authorization; 
	private Integer receiptStartNum;
	private final List<String> otps;
	public CollectionPoster(final List<CollectionData> collections,
			final CollectionService service,
			final Integer receiptStartNum,
			final String authorization,
			final List<String> otps) {
		this.collections = collections;
		this.service = service;
		this.receiptStartNum = receiptStartNum;
		this.authorization = authorization;
		this.otps = otps;
	}

	@Override
	public void run() {
		int i = 0;
		for(CollectionData col : collections) {
			
	           JsonObject json = new JsonObject();
	           json.addProperty("paymentTypeId", 408);
	           json.addProperty("channelTypeId", 1048);
	           json.addProperty("transactionAmount", col.getAmount());
	           json.addProperty("transactionDate", col.getTransDate());
	           json.addProperty("receiptNumber", receiptStartNum++);
	           json.addProperty("locale", "en");
	           json.addProperty("dateFormat", "dd/MM/yyyy");
	          
	           Response response =    this.service.postCollection(CollectionBatch.tenant,
	        		   this.authorization, 
	        		   otps.get(i++), 
	        		  //, CollectionBatch.Host, CollectionBatch.Referer 
	        		   command, 
	        		   col.getLoanId(), 
	        		   json);
	           
	           System.out.println( col.getLoanId() +"   "+ response.getStatus());
	           
		}
	}

}
