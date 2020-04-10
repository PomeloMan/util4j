package io.pomelo.util;

import org.junit.Test;

import com.microsoft.aad.adal4j.AuthenticationResult;

import io.pomelo.util.http.PublicClient;

public class ADALTest {

	// ICMS
	private String username = "";
	private String password = "";
	private String icmsClientId = "a8678b53-df7b-4a0c-be1a-644c9bd2e6d9";
	private String icmsResourceId = "https://icmsauthorizer.azurewebsites.net/";

	// Policheck
	private String policheckClientKey = "";
	private String policheckClientId = "16aeb086-61cd-421c-a428-03079bbc5d1e";
	private String policheckResourceId = "https://microsoft.onmicrosoft.com/OnePolicheck";

	@Test
	public void getIcmsAccessToken() throws Exception {
		PublicClient client = new PublicClient(icmsClientId, icmsResourceId);
		AuthenticationResult result = client.getAccessTokenFromUserCredentials(username, password);
		System.out.println("Access Token\t-\t" + result.getAccessToken());
		System.out.println("Refresh Token\t-\t" + result.getRefreshToken());
		System.out.println("ID Token\t-\t" + result.getIdToken());
	}

	@Test
	public void getPolicheckAccessToken() throws Exception {
		PublicClient client = new PublicClient(policheckClientId, policheckResourceId);
		AuthenticationResult result = client.getAccessTokenFromClientCredentials(policheckClientKey);
		System.out.println("Access Token\t-\t" + result.getAccessToken());
		System.out.println("Refresh Token\t-\t" + result.getRefreshToken());
		System.out.println("ID Token\t-\t" + result.getIdToken());
	}
}
