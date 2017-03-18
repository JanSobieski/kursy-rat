package pl.wd.kursy.web.ui.renderer;

import java.io.Serializable;

import org.apache.log4j.Logger;
import org.zkoss.zk.ui.sys.ComponentsCtrl;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.ListitemRenderer;

import pl.wd.kursy.data.StudentStatus;
import pl.wd.kursy.data.wrapper.StudentWrapper;

public class StudentListItemRenderer implements ListitemRenderer<StudentWrapper>, Serializable {
	private static final long serialVersionUID = 4989449233252555998L;

	private static final Logger logger = Logger.getLogger(StudentListItemRenderer.class);


	public StudentListItemRenderer( ) {
	}

	@Override
	public void render(Listitem item, StudentWrapper sw, int index) throws Exception {
		Listcell lc;
		lc = new Listcell(sw.getStudent().getFirstName() );
		lc.setStyle("text-align:left");
		lc.setParent(item);
		
		lc = new Listcell(sw.getStudent().getLastName() );
		lc.setStyle("text-align:left");
		lc.setParent(item);

		lc = new Listcell(sw.getGroups() );
		lc.setStyle("text-align:left");
		lc.setParent(item);

		lc = new Listcell(sw.getStudent().getStatus().getName() );
		String style = "text-align:left";
		if ( StudentStatus.EXAM.equals(sw.getStudent().getStatus())) {
			style = "; font-weight:bold; color:red";
		}
		if ( StudentStatus.PASSED.equals(sw.getStudent().getStatus())) {
			style = "; font-weight:bold; color:green";
		}
		lc.setStyle(style);
		lc.setParent(item);

		item.setValue(sw);
		
		ComponentsCtrl.applyForward(item, "onDoubleClick=onStudentListItemDoubleClicked");
	}

}
