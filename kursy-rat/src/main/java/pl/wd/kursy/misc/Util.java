package pl.wd.kursy.misc;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.security.MessageDigest;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.StringTokenizer;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;

public class Util {
	public static final String TRUE = "true";

	public final static String VARSION_PATH = "/pl/wd/kursy/resource/version.txt";
	public final static String XSL_PATH = "/pl/wd/kursy/resource/xsl/";
	public final static String ENCODING_UTF8 = "utf-8";
	public final static String DEC_FORMAT_CURRENCY = "0.00";
	public final static String VALUES_SEPARATOR = ",";
	public final static boolean IGNORE_NULL = true;

	private static DecimalFormat _decimal_formatter;

	public final static String[][] national_chars2ascii = { 
		{ "ąàáâãäåæāă", "a" }, { "ćçĉċč", "c" }, { "ęèéêëěēĕė", "e" }, { "ĝğġģ", "g" }, { "ĥħ", "h" }, { "ĩīĭįìíîï", "i" }, { "ĵ", "j" }, { "ķ", "k" },      
		{ "łĺļľŀ", "l" }, { "ńņňŉŋñ", "n" }, { "óðòóôõöøōŏő", "o" }, { "ŕŗř", "r" }, { "śŝşš", "s" }, { "ţťŧ", "t" }, { "ũūŭůűųùúûü", "u" }, 
		{ "ŵ", "w" },  { "żźž", "z" }, 
		{ "ĄÀÁÂÃÄÅÆĀĂ", "A" }, { "ĆĊČĈÇ", "C" }, { "ĘÈÉÊËĒĔĖĚ", "E" }, { "ĜĞĠĢ", "G" },  { "ĤĦ", "H" }, { "ĨĪĬĮ", "I" }, { "Ĵ", "J" },
		{ "ŁĹĻĽĿ", "L" }, { "ŃŅŇŊ", "N" }, { "ÓŌŎŐÒÔÕÖ", "O" }, { "ŔŖŘ", "R" }, { "ŚŜŞŠ", "S" }, { "ŢŤŦ", "T" }, { "ÙÚÛÜŨŪŬŮŰŲ", "U" }, 
		{ "Ŵ", "W" }, { "ŻŹŽ", "Z" } };
	public final static String file_name_invalid_chars = "\\/:*?\"<>|"; 


	public static String encode_password( String pass ) throws Exception {
		MessageDigest md = MessageDigest.getInstance("SHA");
		md.update(pass.getBytes(ENCODING_UTF8));
		byte raw[] = md.digest();
		
		String hash = new String( Base64.getEncoder().encode(raw) );

		return hash;
	}

	public static Document load_dom_from_file( String path ) throws Exception {
		FileInputStream fis = new FileInputStream(path);
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		// get an instance of builder
		DocumentBuilder db = dbf.newDocumentBuilder();

		// create an instance of DOM

		Document dom_xml = db.parse(fis);

		return dom_xml;
	}

	public static void store_resource_to_file( String resource_path, String file_path ) throws Exception {
		InputStream is_res = Util.class.getResourceAsStream(resource_path);
		byte[] by_data = new byte[is_res.available()];
		is_res.read(by_data);
		FileOutputStream fos = new FileOutputStream(file_path);
		fos.write(by_data);
		fos.close();
	}

	public static String get_version() throws Exception {
		InputStream is_res = Util.class.getResourceAsStream(VARSION_PATH);
		InputStreamReader isr = new InputStreamReader(is_res);
		BufferedReader br = new BufferedReader(isr);
		String version = br.readLine();
		br.close();
		isr.close();
		is_res.close();

		return version.trim();
	}

	public static Collection<String> get_list_from_resource( String path, boolean unique ) throws Exception {
		Collection<String> list = null;
		
		if ( unique ) {
			list = new HashSet<String>();
		}
		else {
			list = new ArrayList<String>();
		}
		InputStream is_res = Util.class.getResourceAsStream( path );
		InputStreamReader isr = new InputStreamReader(is_res);
		BufferedReader br = new BufferedReader(isr);
		while( true ) {
			String line = br.readLine();
			if ( line == null ) {
				break;
			}
			String trimmed = line.trim(); 
			if ( ( trimmed.length() == 0 ) || ( trimmed.startsWith( "#" ) ) ) {
				continue;
			}
			if ( unique ) {
				if ( list.contains( trimmed ) ) {
					continue;
				}
			}
			list.add( trimmed );
		}
		br.close();
		isr.close();
		is_res.close();

		return list;
	}

	public static DecimalFormat getDecimalFormatter() {
		if ( _decimal_formatter == null ) {
			DecimalFormatSymbols symbols = new DecimalFormatSymbols();
			symbols.setDecimalSeparator('.');
			String format = "#0.###";
			_decimal_formatter = new DecimalFormat(format, symbols);
		}

		return _decimal_formatter;
	}

	public static DecimalFormat getDecimalFormatter( char separator ) {
		DecimalFormatSymbols symbols = new DecimalFormatSymbols();
		symbols.setDecimalSeparator(separator);
		String format = "#0.###";
		DecimalFormat decimal_formatter = new DecimalFormat(format, symbols);
		return decimal_formatter;
	}

	public static String stack2string( Throwable e ) {
		try {
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			e.printStackTrace(pw);
			return sw.toString();
		} catch (Exception e2) {
			return "bad stack2string";
		}
	}

	public static String national_chars_to_ascii( String pl_str ) {
		return national_chars_to_ascii( pl_str, true );
	}
	
	public static String national_chars_to_ascii( String pl_str, boolean to_lower_case ) {
		String normalized = pl_str;
		if ( to_lower_case ) {
			normalized = pl_str.toLowerCase();
		}
		for( int i = 0; i < national_chars2ascii.length; i++ ) {
			String national_chars = national_chars2ascii[i][0];

			for( int ii = 0; ii < national_chars.length(); ii++ ) {
				normalized = normalized.replace(national_chars.charAt( ii ), national_chars2ascii[i][1].charAt( 0 ));
			}
		}

		return normalized;
	}

	public static String name_first_capital_rest_lower(String name) {
		String new_name = "";
		StringTokenizer st = new StringTokenizer(name); 
		while( st.hasMoreTokens() ) {
			if ( new_name.length() > 0 ) {
				new_name += " ";
			}
			String token = st.nextToken().trim();
			new_name += token.substring( 0, 1 ).toUpperCase();
			if ( token.length() > 2 ) {
				new_name += token.substring( 1, token.length() ).toLowerCase();
			}
		}
		
		return new_name;
	}

	public static String get_formatted_double( double value, Locale loc, int fract ) {
		NumberFormat f = NumberFormat.getInstance(loc);

		DecimalFormatSymbols symbols = new DecimalFormatSymbols();
		symbols.setDecimalSeparator('.');

		if ( f instanceof DecimalFormat ) {
			symbols = (((DecimalFormat) f).getDecimalFormatSymbols());
		}
		String format = "0.";
		for( int i = 0; i < fract; i++ ) {
			format += "0";
		}
		
		DecimalFormat decimal_formatter = new DecimalFormat(format, symbols);

		return decimal_formatter.format(value);
	}

	public static String get_formatted_currency( double value, Locale loc ) {
		return get_formatted_currency( value, loc, true );
		
	}
	
	public static String get_formatted_currency( double value, Locale loc, boolean show_zero ) {
		if ( !show_zero && ( value == 0 ) ) {
			return "";
		}
		NumberFormat f = NumberFormat.getInstance(loc);

		DecimalFormatSymbols symbols = new DecimalFormatSymbols();
		symbols.setDecimalSeparator('.');

		if ( f instanceof DecimalFormat ) {
			symbols = (((DecimalFormat) f).getDecimalFormatSymbols());
		}
		DecimalFormat decimal_formatter = new DecimalFormat(DEC_FORMAT_CURRENCY, symbols);

		return decimal_formatter.format(value);
	}

	public static DecimalFormat get_decimal_formatter( Locale loc ) {
		NumberFormat f = NumberFormat.getInstance(loc);

		DecimalFormatSymbols symbols = new DecimalFormatSymbols();
		symbols.setDecimalSeparator('.');

		if ( f instanceof DecimalFormat ) {
			symbols = (((DecimalFormat) f).getDecimalFormatSymbols());
		}
		DecimalFormat decimal_formatter = new DecimalFormat(DEC_FORMAT_CURRENCY, symbols);

		return decimal_formatter;
	}
	
	public static String get_list2str( List<String> list ) {
		String ret = "";
		for( String text : list ) {
			if ( ret.length() > 0 ) {
				ret += ",";
			}
			ret += text;
		}
		
		return ret;
	}
	
	public static String get_vaild_file_name( String name ) {
		StringBuffer sbValidName = new StringBuffer();
		name = name.trim();
		for (int i = 0; i < name.length(); i++) {
	    if ((file_name_invalid_chars.indexOf( name.charAt(i) ) >= 0 ) 
	        || ( name.charAt(i) < '\u0020') // ctrls
	        || (name.charAt(i) > '\u007e' && name.charAt(i) < '\u00a0') // ctrls
	    ) {
	    }
	    else {
	    	sbValidName.append( name.charAt(i) );
	    }
		}
		
		return sbValidName.toString();
	}
	
	public static String time_diff( long t0, long t1 ) {
		long diff = t1 - t0;
		long diffSeconds = diff / 1000;
	  long diffMinutes = diff / (60 * 1000);
	  long diffHours = diff / (60 * 60 * 1000);
	  
	  String ret = "";
	  if ( diffHours < 10 ) {
	  	ret += "0";
	  }
  	ret += diffHours;
  	ret += ":";
	  if ( diffMinutes < 10 ) {
	  	ret += "0";
	  }
  	ret += diffMinutes;
  	ret += ":";
	  if ( diffSeconds < 10 ) {
	  	ret += "0";
	  }
  	ret += diffSeconds;
	  
	  return ret;
	}

	public static String remove_spaces( String str ) {
		String ret = str.replaceAll( "( |\u00A0)+", "" );
//		ret = ret.replaceAll( "(\u00A0)+", "" );
		
		return ret;
	}

	public static String get_ascii_digit_str( String str ) {
		String ret = "";
		for( int i = 0; i < str.length(); i++ ) {
			if ( Character.isLetter( str.charAt( i ) ) || Character.isDigit( str.charAt( i ) ) || ( str.charAt( i ) == '_' ) ) {
				ret += str.charAt( i );
			}
		}
		
		return ret;
	}
	
	public static void main( final String[] args ) {
		File start_dir = new File("/projects/mgm/applicants/trunk/EIS/sss");
		System.out.println("Done.");
	}
	
	static public boolean deleteDirectory(File path) {
		if (path.exists()) {
			File[] files = path.listFiles();
			for (int i = 0; i < files.length; i++) {
				if (files[i].isDirectory()) {
					deleteDirectory(files[i]);
				} else {
					files[i].delete();
				}
			}
		}
		return (path.delete());
	}
	
	public static String getFileExt( String path ) {
		String extension = "";

		int i = path.lastIndexOf('.');
		if (i > 0) {
		    extension = path.substring(i+1).toLowerCase();
		}
		
		return extension;
	}
	

}
