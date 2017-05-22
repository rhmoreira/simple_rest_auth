package br.com.auth.dist.classes;

public class Message {

	private String message;
	
	public Message(String message) {
		super();
		this.message = message;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	
	public static Message newMessage(String msg){
		return new Message(msg);
	}
}
