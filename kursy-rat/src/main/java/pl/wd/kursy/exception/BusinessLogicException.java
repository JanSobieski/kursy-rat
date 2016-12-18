package pl.wd.kursy.exception;

public class BusinessLogicException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = -3926554330563775295L;
	protected String _description;
	
	public BusinessLogicException( String descr ) {
		_description = descr;
	}

	public BusinessLogicException( String message, String descr ) {
		super(message);
		
		_description = descr;
	}

	public String get_description() {
		return _description;
	}
}
