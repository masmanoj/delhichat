package in.novopay.collection.batch.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.logging.Logger;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.OkClient;
import retrofit.client.Response;

import com.squareup.okhttp.OkHttpClient;

@SuppressWarnings("unused")
public class ProcessHelper {

	

	@SuppressWarnings("null")
	public static OkHttpClient configureClient(final OkHttpClient client) {
		final TrustManager[] certs = new TrustManager[] { new X509TrustManager() {

			@Override
			public X509Certificate[] getAcceptedIssuers() {
				return null;
			}

			@Override
			public void checkServerTrusted(final X509Certificate[] chain,
					final String authType) throws CertificateException {
			}

			@Override
			public void checkClientTrusted(final X509Certificate[] chain,
					final String authType) throws CertificateException {
			}
		} };

		SSLContext ctx = null;
		try {
			ctx = SSLContext.getInstance("TLS");
			ctx.init(null, certs, new SecureRandom());
		} catch (final java.security.GeneralSecurityException ex) {
		}

		try {
			final HostnameVerifier hostnameVerifier = new HostnameVerifier() {
				@Override
				public boolean verify(final String hostname,
						final SSLSession session) {
					return true;
				}
			};
			client.setHostnameVerifier(hostnameVerifier);
			client.setSslSocketFactory(ctx.getSocketFactory());
		} catch (final Exception e) {
		}

		return client;
	}

	public static OkHttpClient createClient() {
		final OkHttpClient client = new OkHttpClient();
		return configureClient(client);
	}

	@SuppressWarnings("rawtypes")
	public static Callback createCallback(final String url) {

		return new Callback() {
			@Override
			public void success(final Object o, final Response response) {
				
				BufferedReader reader = null;
		        StringBuilder sb = new StringBuilder();
		        try {

		            reader = new BufferedReader(new InputStreamReader(response.getBody().in()));

		            String line;

		            try {
		                while ((line = reader.readLine()) != null) {
		                    sb.append(line);
		                }
		            } catch (IOException e) {
		                e.printStackTrace();
		            }
		        } catch (IOException e) {
		            e.printStackTrace();
		        }


		        String result = sb.toString();
				/*logger.info("URL : " + url + "\tStatus : "
						+ response.getStatus() + "\tResponse Body :" + result);*/
			}

			@Override
			public void failure(final RetrofitError retrofitError) {
				/*logger.info("URL : " + url + "\tMessage - " + retrofitError.getMessage() +
						"\tReason: " + retrofitError.getResponse().getReason());*/
				
			}
		};
	}
	
}