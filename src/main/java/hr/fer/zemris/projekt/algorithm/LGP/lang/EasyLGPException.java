package hr.fer.zemris.projekt.algorithm.LGP.lang;

@SuppressWarnings("serial")
public class EasyLGPException extends RuntimeException {
	
	public EasyLGPException() {
		super();
	}
	
	public EasyLGPException(String message) {
		super(message);
	}
	
	public EasyLGPException(Throwable cause) {
		super(cause);
	}

	public EasyLGPException(String message, Throwable cause) {
		super(message, cause);
	}
	
}
