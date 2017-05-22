package br.com.ecosistemas.auth.web;

import java.io.IOException;
import java.util.Collections;
import java.util.Set;

import org.apache.commons.io.IOUtils;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import br.com.auth.dist.classes.AuthException;
import br.com.auth.dist.classes.Token;

public class AuthFacade {

	private static final String AUTH_URL = "/auth-rest/api/auth/login";
	private static final String ACCESS_AUTH_URL = "/auth-rest/api/auth/access";
	private static final String TOKEN_AUTH_URL = "/auth-rest/api/auth/token/validate";
	
	private static String host = "http://localhost:8080";
	
	static{
		HttpClientBuilder custom = HttpClients.custom();
		Set<BasicHeader> headers = Collections.singleton(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
		custom.setDefaultHeaders(headers);
	}
	
	public static void setAuthHost(String hostUrl){
		host = hostUrl;
	}
	
	public Token doAuth(String login, String senha) throws ClientProtocolException, IOException, AuthException{
		
		ObjectMapper objectMapper = new ObjectMapper();
		ObjectNode jsonObj = objectMapper.createObjectNode();
		jsonObj.put("login", login);
		jsonObj.put("senha", senha);
		
		try (CloseableHttpResponse response = call(AUTH_URL, jsonObj.toString());){
			validateResponse(response);
			return readResponseAsObject(response.getEntity(), Token.class);
		}
	}
	
	public boolean checkAccess(String token, String path, String method) throws ClientProtocolException, IOException{
		
		ObjectMapper objectMapper = new ObjectMapper();
		ObjectNode jsonObj = objectMapper.createObjectNode();
		jsonObj.put("token", token);
		jsonObj.put("accessPath", path);
		jsonObj.put("accessMethod", method);
		
		try (CloseableHttpResponse response = call(ACCESS_AUTH_URL, jsonObj.toString());){
			validateResponse(response);
			return true;
		} catch (AuthException e) {
			return false;
		}
	}
	
	public boolean validateToken(String token) throws ClientProtocolException, IOException{
		
		try (CloseableHttpResponse response = callValidate(token);){
			validateResponse(response);
			return true;
		} catch (AuthException e) {
			return false;
		}
	}
	
	private <T> T readResponseAsObject(HttpEntity entity, Class<T> clazz) throws AuthException{
		try{
			String token = IOUtils.toString(entity.getContent());
			
			ObjectMapper objectMapper = new ObjectMapper();
			T tInstance = objectMapper.readValue(token, clazz);
			EntityUtils.consume(entity);
			return tInstance;
		}catch (Exception e){
			throw new AuthException(500, e);
		}
	}

	private CloseableHttpResponse callValidate(String token) throws IOException, ClientProtocolException {
		ObjectMapper objectMapper = new ObjectMapper();
		ObjectNode jsonObj = objectMapper.createObjectNode();
		jsonObj.put("token", token);
		
		CloseableHttpResponse response = call(TOKEN_AUTH_URL, jsonObj.toString());
		return response;
	}
	
	private CloseableHttpResponse call(String url, String body, Header... headers) throws ClientProtocolException, IOException{
		CloseableHttpClient httpclient = HttpClients.createDefault();
		HttpPut putRequest = new HttpPut(host + url);
		
		StringEntity httpEntity = new StringEntity(body, ContentType.APPLICATION_JSON);
		putRequest.setEntity(httpEntity);
		if (headers != null)
			putRequest.setHeaders(headers);

		CloseableHttpResponse response = httpclient.execute(putRequest);
		
		return response;
	}
	
	private void validateResponse(CloseableHttpResponse response) throws AuthException {
		int statusCode = response.getStatusLine().getStatusCode();
		if (statusCode != HttpStatus.SC_OK)
			throw new AuthException(statusCode);
	}
}
