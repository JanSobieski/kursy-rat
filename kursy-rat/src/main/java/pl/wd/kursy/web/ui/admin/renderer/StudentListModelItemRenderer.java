 package pl.wd.kursy.web.ui.admin.renderer;

import java.io.Serializable;

import org.apache.log4j.Logger;
import org.zkoss.zk.ui.sys.ComponentsCtrl;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.ListitemRenderer;

import pl.wd.kursy.data.Student;

public class StudentListModelItemRenderer implements ListitemRenderer<Student>, Serializable {

	private static final long serialVersionUID = -1516562571683859292L;
	private static final Logger logger = Logger.getLogger(StudentListModelItemRenderer.class);

	@Override
	public void render( Listitem item, Student student, int index ) throws Exception {
		Listcell lc;
		lc = new Listcell(Integer.toString(student.get_id()));
		lc.setStyle("text-align:right");
		lc.setParent(item);
		lc = new Listcell(student.getFirstName());
		lc.setStyle("text-align:left");
		lc.setParent(item);
		lc = new Listcell(student.getLastName());
		lc.setStyle("text-align:left");
		lc.setParent(item);

		item.setAttribute("data", student);
		
		ComponentsCtrl.applyForward(item, "onDoubleClick=onStudentListItemDoubleClicked");
	}

}

