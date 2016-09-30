package in.novopay.collection.batch.service;

import retrofit.RestAdapter;
import retrofit.client.OkClient;

import com.squareup.okhttp.OkHttpClient;

public class CollectionWebHook {
	
	 public static CollectionService createCollectionService(final String url) {

	        OkHttpClient client = null;
	        if(url.toLowerCase().startsWith("https"))
	                        client = ProcessHelper.createClient();
	        else
	                client = new OkHttpClient();
	        
	        

	        final RestAdapter restAdapter = new RestAdapter.Builder()
	        .setLogLevel(RestAdapter.LogLevel.NONE)
	        .setEndpoint(url)
	        .setClient(new OkClient(client)).build();

	        return restAdapter.create(CollectionService.class);
	    }
}
