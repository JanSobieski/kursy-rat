package pl.wd.kursy.data.constants;

import java.text.SimpleDateFormat;

public class SystemConstants {
	public final static String STATUS_PASSED = "zaliczony";
	public final static String STATUS_NOT_PASSED = "niezaliczony";

	public final static String UHOME_DIR = "kursy";

	public static final String TABLE_ID = "table_id";
	public static final String RECORD_ID = "record_id";
	public static final String CLIENT_ID = "client-id";
	public static final String VERSION = "version";
	public static final String TEXT = "text";
	public static final String TEXT_PLAIN = "text/plain";
	

	public static final String RESPONSE_OPERATION_FAILED = "operation_failed";
	public static final String RESPONSE_OPERATION_OK = "operation_ok";

	public static final String PASSED = "passed";
	public static final String FAILED = "failed";
	public static final String READ_ONLY = "read_only";
	public static final String LANG = "lang";
	public static final String FEATURE = "feature";
	public static final String KEY = "key";
	public static final String VALUE = "value";

	public static final String PATH_CLIENT_ID = "client_id";
	
	public static final String PATH_OLD_PASS = "old_pass";
	public static final String PATH_NEW_PASS = "new_pass";
	public static final String PATH_VERSION = "version";

	public static final String RESPONSE_CHANGE_PASS_FAILED = "change-pass-failed";
	public static final String RESPONSE_WRONG_PASS = "wrong-pass";
	public static final String RESPONSE_CHANGE_PASS_PASSED = "change-pass-passed";
	public static final String AUTH_FAILED = "authorization-failed";  
	
	public static final String SQL_GRAMMAR_EXCEPTION = "SQLGrammarException";
	public static final String ERROR_IN_TSQUERY = "syntax error in tsquery";
	

	public static final String SHOW_DATE_FORMAT_MMddHHmm_pattern = "MM-dd HH:mm";
	public static final SimpleDateFormat SHOW_DATE_FORMAT_MMddHHmm = new SimpleDateFormat( SHOW_DATE_FORMAT_MMddHHmm_pattern );


}
