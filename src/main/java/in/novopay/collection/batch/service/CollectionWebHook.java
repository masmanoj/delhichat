package in.novopay.collection.batch.service;

import retrofit.RestAdapter;
import retrofit.client.OkClient;

import com.squareup.okhttp.OkHttpClient;

public class CollectionWebHook {
	
	 public static CollectionService createCollectionService(final String url, Integer isLogEnabled) {

	        OkHttpClient client = null;
	        if(url.toLowerCase().startsWith("https"))
	                        client = ProcessHelper.createClient();
	        else
	                client = new OkHttpClient();
	        
	        RestAdapter.LogLevel ls =  RestAdapter.LogLevel.NONE;
	        
	        if(isLogEnabled == 1){
	        	ls =  RestAdapter.LogLevel.FULL;
	        }
	        

	        final RestAdapter restAdapter = new RestAdapter.Builder()
	        .setLogLevel(ls)
	        .setEndpoint(url)
	        .setClient(new OkClient(client)).build();

	        return restAdapter.create(CollectionService.class);
	    }
}
