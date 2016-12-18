package pl.wd.kursy.web.ui.components;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.zkoss.zul.Datebox;
import org.zkoss.zul.Textbox;

import pl.wd.kursy.web.ui.util.WebUtil;

public class DateTimePanel {
	private Datebox _dbDate;
	private Textbox _tbHour; 
	private Textbox _tbMinute; 

	
	public DateTimePanel( Datebox dbDate, Textbox tbHour, Textbox tbMinute ) {
		_dbDate = dbDate;
		_tbHour = tbHour;
		_tbMinute = tbMinute;
	}
	
	public void setEnabled( boolean enabled ) {
//		_jcbLastContact.setEnabled( enabled );
//		_jtfMinute.setEditable( enabled );
//		_jtfHour.setEditable( enabled );
	}
	
	public void set_time( long time ) {
		GregorianCalendar cal = new GregorianCalendar();
		cal.setTimeInMillis( time );
		String hm = Integer.toString( cal.get(Calendar.HOUR_OF_DAY ) );
		if ( hm.length() < 2 ) {
			hm = "0" + hm;
		}
		_tbHour.setText( hm );
		set_minute( cal.get(Calendar.MINUTE) );
	}
	
	public void set_minute( int min ) {
		String hm = Integer.toString( min );
		if ( hm.length() < 2 ) {
			hm = "0" + hm;
		}
		_tbMinute.setText( hm );
	}

	public void set_date_time( Date date ) {
		if ( date != null ) {
			_dbDate.setValue( date );
			set_time( date.getTime() );
		}
	}

	public Date getDateTime() {
		Date date = _dbDate.getValue();
		if ( date == null ) {
			return null;
		}
		Calendar cal = new GregorianCalendar();
		cal.setTime(date);
		try {
			int hour = Integer.parseInt(_tbHour.getText() );
			cal.set(Calendar.HOUR_OF_DAY, hour);
			int min = Integer.parseInt(_tbMinute.getText() );
			cal.set(Calendar.MINUTE, min);
		} catch (Exception e) {
			return null;
		}
		
		return cal.getTime();
	}

	public boolean check_time() {
		try {
			Integer.parseInt(_tbHour.getText() );
			Integer.parseInt(_tbMinute.getText() );
		} catch (Exception e) {
			return false;
		}
		
		return true;
	}
	
	public void setReadOnly() {
		WebUtil.setTbReadOnly(_tbMinute );
		WebUtil.setTbReadOnly(_tbHour );
		WebUtil.setDbReadOnly( _dbDate );
	}

	public void setEditable() {
		WebUtil.setTbEditable(_tbMinute );
		WebUtil.setTbEditable(_tbHour );
		WebUtil.setDbEditable( _dbDate );
	}
}
