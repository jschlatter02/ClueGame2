package clueGame;

public class BadConfigFormatException extends Exception{
	
	public BadConfigFormatException() {
		super("File does not work!");
	}
	
	public BadConfigFormatException(String message) {
		super(message);
	}
}
