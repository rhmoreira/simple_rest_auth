package br.com.auth.dist.classes;

public class AuthException extends Exception {

	private static final long serialVersionUID = 7758889200099493484L;

	private int statusCode;
	
	public AuthException() {
	}
	
	public AuthException(int statusCode) {
		this.statusCode = statusCode;
	}
	
	public AuthException(int statusCode, Throwable cause) {
		this(statusCode, null, cause);
	}
	
	public AuthException(int statusCode, String message) {
		this(statusCode, message, null);
	}
	
	public AuthException(int statusCode, String message, Throwable cause) {
		super(message, cause);
		this.statusCode = statusCode;
	}

	public AuthException(String message) {
		super(message);
	}

	public AuthException(Throwable cause) {
		super(cause);
	}

	public AuthException(String message, Throwable cause) {
		super(message, cause);
	}

	public AuthException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public int getStatusCode() {
		return statusCode;
	}
}