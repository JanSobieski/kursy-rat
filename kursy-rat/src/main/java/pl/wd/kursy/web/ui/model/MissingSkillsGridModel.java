package pl.wd.kursy.web.ui.model;

import java.util.List;

import org.zkoss.zul.ListModelList;

import pl.wd.kursy.data.Exercise;
import pl.wd.kursy.data.RateCardItem;
import pl.wd.kursy.data.Student;
import pl.wd.kursy.web.UserWorkspace;
import pl.wd.kursy.web.data.MissingSkillGridRow;

public class MissingSkillsGridModel extends ListModelList<MissingSkillGridRow> {
	private static final long serialVersionUID = 6739520686673638280L;
	
	private final UserWorkspace _workspace;

	public MissingSkillsGridModel(UserWorkspace workspace) throws Exception {
		_workspace = workspace;
		List<Exercise> exercises = _workspace.getDataServiceProvider().getExercises();
		List<Student> students = _workspace.getDataServiceProvider().getStudents(_workspace.getCourseId());
		for (Student student : students) {
			List<RateCardItem> rateCards = _workspace.getDataServiceProvider().getRateCard(student.getId());
			for (RateCardItem rateCardItem : rateCards) {
				rateCardItem.getExerciseId();
			}
		}
		
		
		
		//List<> missingSkills = _workspace.getDataServiceProvider().
		MissingSkillGridRow msRow = new MissingSkillGridRow();
		msRow.setGroupText( "Grupa 1: 1.Prawidłowo ocenia bezpieczeństwo i zabezpiecza miejsce zdarzenia, 2.Prawidłowo sprawdza przytomność, 8.Prawidłowo układa poszkodowanego w pozycji bezpiecznej" );
		add(msRow);

		msRow = new MissingSkillGridRow();
		msRow.setStudent(students.get(0));
		msRow.setSuggestedExercise("Symulacja 01");
		add(msRow);

		msRow = new MissingSkillGridRow();
		msRow.setGroupText( "Grupa 2: 2.Prawidłowo sprawdza przytomność, 8.Prawidłowo układa poszkodowanego w pozycji bezpiecznej" );
		add(msRow);
		msRow = new MissingSkillGridRow();
		msRow.setStudent(students.get(1));
		msRow.setSuggestedExercise("Symulacja 02");
		add(msRow);
		msRow = new MissingSkillGridRow();
		msRow.setStudent(students.get(2));
		msRow.setSuggestedExercise("Symulacja 02");
		add(msRow);
	}
	

}
