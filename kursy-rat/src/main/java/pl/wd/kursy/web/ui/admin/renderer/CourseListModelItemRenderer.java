 package pl.wd.kursy.web.ui.admin.renderer;

import java.io.Serializable;

import org.apache.log4j.Logger;
import org.zkoss.zk.ui.sys.ComponentsCtrl;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.ListitemRenderer;

import pl.wd.kursy.data.Course;

public class CourseListModelItemRenderer implements ListitemRenderer<Course>, Serializable {

	private static final long serialVersionUID = -1516562571683859292L;
	private static final Logger logger = Logger.getLogger(CourseListModelItemRenderer.class);

	@Override
	public void render( Listitem item, Course course, int index ) throws Exception {
		Listcell lc;
		lc = new Listcell(Integer.toString(course.getId()));
		lc.setStyle("text-align:right");
		lc.setParent(item);
		lc = new Listcell(course.getName());
		lc.setStyle("text-align:left");
		lc.setParent(item);

		item.setAttribute("data", course);
		
		ComponentsCtrl.applyForward(item, "onDoubleClick=onCourseListItemDoubleClicked");
	}

}

