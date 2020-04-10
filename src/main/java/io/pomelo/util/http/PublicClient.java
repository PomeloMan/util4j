package io.pomelo.util.http;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import javax.naming.ServiceUnavailableException;

import org.apache.http.Header;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;

import com.microsoft.aad.adal4j.AuthenticationContext;
import com.microsoft.aad.adal4j.AuthenticationResult;
import com.microsoft.aad.adal4j.ClientCredential;

/**
 * @ClassName PublicClient.java
 * @Description Microsoft Azure Active Directory Authentication Library (ADAL)
 *              for Java
 *              {https://github.com/AzureAD/azure-activedirectory-library-for-java/releases}
 * @author PomeloMan
 */
public class PublicClient {

	private final static String defaultInstance = "https://login.microsoftonline.com/";
	private final static String defaultTenantId = "microsoft.onmicrosoft.com";

	private String tenantId = "microsoft.onmicrosoft.com";
	private String clientId = "9ba1a5c7-f17a-4de9-a1f1-6178c8d51223";
	private String instance = "https://login.microsoftonline.com/";
	private String resourceId = "https://graph.windows.net";
	private String authority = instance + tenantId;

	public PublicClient(String clientId, String resource) {
		this(defaultTenantId, clientId, defaultInstance, resource);
	}

	public PublicClient(String tenantId, String clientId, String instance, String resourceId) {
		super();
		this.tenantId = tenantId;
		this.clientId = clientId;
		this.instance = instance;
		this.resourceId = resourceId;
		this.authority = this.instance + this.tenantId;
	}

	/**
	 * @param clientKey
	 * @return
	 * @throws Exception
	 */
	public CloseableHttpClient getHttpClient(String clientKey) throws Exception {
		ArrayList<Header> headers = new ArrayList<Header>();
		headers.add(new BasicHeader("Authorization",
				"Bearer " + getAccessTokenFromClientCredentials(clientKey).getAccessToken()));
		return HttpClients.custom().setDefaultHeaders(headers).build();
	}

	/**
	 * @param username
	 * @param password
	 * @return
	 * @throws Exception
	 */
	public CloseableHttpClient getHttpClient(String username, String password) throws Exception {
		ArrayList<Header> headers = new ArrayList<Header>();
		headers.add(new BasicHeader("Authorization",
				"Bearer " + getAccessTokenFromUserCredentials(username, password).getAccessToken()));
		return HttpClients.custom().setDefaultHeaders(headers).build();
	}

	/**
	 * @param clientSecret
	 * @return
	 * @throws Exception
	 */
	public AuthenticationResult getAccessTokenFromClientCredentials(String clientSecret) throws Exception {
		AuthenticationContext context = null;
		AuthenticationResult result = null;
		ExecutorService service = null;
		try {
			service = Executors.newFixedThreadPool(1);
			context = new AuthenticationContext(authority, false, service);
			Future<AuthenticationResult> future = context.acquireToken(resourceId,
					new ClientCredential(clientId, clientSecret), null);
			result = future.get();
		} finally {
			service.shutdown();
		}
		if (result == null)
			throw new ServiceUnavailableException("authentication result was null");
		return result;
	}

	/**
	 * @param username
	 * @param password
	 * @return
	 * @throws Exception
	 */
	public AuthenticationResult getAccessTokenFromUserCredentials(String username, String password) throws Exception {
		AuthenticationContext context = null;
		AuthenticationResult result = null;
		ExecutorService service = null;
		try {
			service = Executors.newFixedThreadPool(1);
			context = new AuthenticationContext(authority, false, service);
			Future<AuthenticationResult> future = context.acquireToken(resourceId, clientId, username, password, null);
			result = future.get();
		} finally {
			service.shutdown();
		}
		if (result == null)
			throw new ServiceUnavailableException("authentication result was null");
		return result;
	}
}
