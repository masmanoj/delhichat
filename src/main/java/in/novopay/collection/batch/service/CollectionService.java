package in.novopay.collection.batch.service;

import retrofit.client.Response;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.Header;
import retrofit.http.POST;
import retrofit.http.Path;
import retrofit.http.Query;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
public interface CollectionService {

	String HOST = "Host";
	String Referer = "Referer";
	String TenantId  = "Novobank-TenantId";
	String Authorization = "Authorization";
	
    @GET("/crypt/getpublicrsakey")
    JsonObject getRsaKey(
    		@Header(TenantId) String tenantId
    		);
    @POST("/authentication")
    JsonObject authenticate(
    		@Header(TenantId) String tenantId,
    		@Body JsonElement request
    		);
    
    @POST("/loans/{loanId}/transactions")
    Response postCollection(
    		@Header(TenantId) String tenantId,
    		@Header(Authorization) String authorization,
    		@Header("OTP") String otp,
    		//@Header(HOST) String host,
    		//@Header(Referer) String referer,
    		@Query("command") String command,
    		@Path("loanId") Long loanId,
    		@Body JsonElement request
    		);
    
    @GET("/crypt/getotp")
    JsonArray getOtp(
    		@Header(TenantId) String tenantId,
    		@Header(Authorization) String authorization,
    		//@Header(HOST) String host,
    		//@Header(Referer) String referer,
    		@Query("size") Integer otpSize
    		
    		);
}