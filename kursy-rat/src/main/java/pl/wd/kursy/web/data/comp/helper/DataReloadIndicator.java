package pl.wd.kursy.web.data.comp.helper;

public class DataReloadIndicator {
	protected boolean _dataLoaded = false;

	public boolean isDataLoaded() {
		return _dataLoaded;
	}

	public void setDataLoaded( boolean dataLoaded ) {
		_dataLoaded = dataLoaded;
	}
}
