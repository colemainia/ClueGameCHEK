package clueGame;
public class BadConfigFormatException extends Exception {
	private static final long serialVersionUID = 1L;
	public BadConfigFormatException(String badFile) {
		super();
		System.out.println("Bad File: " + badFile);
	}
}
