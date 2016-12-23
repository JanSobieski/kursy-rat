package pl.wd.kursy.web.ui.menu.domain;

/**
 * 
 */
public interface IMenuDomain {

	String getRightName();

	String getId();

	String getLabel();

	Boolean isWithOnClickAction();

	String getZulNavigation();

	String getIconName();

	Boolean isCreateTab();
}
