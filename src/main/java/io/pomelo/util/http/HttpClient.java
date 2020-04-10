package io.pomelo.util.http;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.Consts;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.Asserts;
import org.apache.http.util.EntityUtils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

/**
 * @ClassName HttpClient.java
 * @author PomeloMan
 */
public class HttpClient {

	private static Gson gson = new GsonBuilder().create();

	public enum RequestType {
		Get, Post
	}

	/**
	 * @param username
	 * @param password
	 * @return CloseableHttpClient
	 */
	public static CloseableHttpClient getBasicHttpClient(String username, String password) {
		CredentialsProvider provider = new BasicCredentialsProvider();
		provider.setCredentials(AuthScope.ANY, new UsernamePasswordCredentials(username, password));
		return HttpClients.custom().setDefaultCredentialsProvider(provider).build();
	}

	/**
	 * @param uri
	 * @param type
	 * @param input
	 * @return
	 * @throws IOException
	 */
	public static InputStream getMetadata(String uri, RequestType type, Map<String, Object> input) throws IOException {
		InputStream content = null;
		try (CloseableHttpClient httpclient = HttpClients.createDefault()) {
			content = exec(httpclient, uri, type, input).getEntity().getContent();
		}
		return content;
	}

	/**
	 * @param uri
	 * @param type
	 * @param input
	 * @return
	 * @throws IOException
	 */
	public static String getResult(String uri, RequestType type, Map<String, Object> input) throws IOException {
		String content = null;
		try (CloseableHttpClient httpclient = HttpClients.createDefault()) {
			content = EntityUtils.toString(exec(httpclient, uri, type, input).getEntity());
		}
		return content;
	}

	/**
	 * @param httpclient
	 * @param uri
	 * @param type
	 * @param input
	 * @return
	 * @throws IOException
	 */
	public static HttpResponse exec(CloseableHttpClient httpclient, String uri, RequestType type,
			Map<String, Object> input) throws IOException {
		Asserts.notNull(httpclient, "CloseableHttpClient");
		Asserts.notEmpty(uri, "URI");
		String content = null;
		HttpResponse httpResponse = null;
		if (type == RequestType.Get) {
			if (input != null && input.size() != 0) {
				List<NameValuePair> params = new ArrayList<NameValuePair>();
				input.keySet().stream().forEach(k -> params.add(new BasicNameValuePair(k, input.get(k).toString())));
				uri += "?" + EntityUtils.toString(new UrlEncodedFormEntity(params, Consts.UTF_8));
			}
			HttpGet httpRequest = new HttpGet(uri);
			httpResponse = httpclient.execute(httpRequest);
		} else if (type == RequestType.Post) {
			HttpPost httpRequest = new HttpPost(uri);
			if (input != null && input.size() != 0) {
				httpRequest.setEntity(new StringEntity(gson.toJson(input), ContentType.APPLICATION_JSON));
			}
			httpResponse = httpclient.execute(httpRequest);
		} else {
			throw new IllegalArgumentException("Request type is neither get nor post");
		}
		if (httpResponse.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
			Map<String, Object> result = gson.fromJson(content, new TypeToken<Map<String, Object>>() {
			}.getType());
			throw new IOException(result.get("message").toString());
		}
		return httpResponse;
	}
}
