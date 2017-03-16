package pl.wd.kursy.web.ui.model;

import java.util.List;

import org.apache.log4j.Logger;
import org.zkoss.zul.ListModelList;

import pl.wd.kursy.data.Exercise;
import pl.wd.kursy.web.UserWorkspace;

public class ExerciseListModel extends ListModelList<Exercise> {
	private static final long serialVersionUID = 2126957580354333862L;

	private static final Logger logger = Logger.getLogger(ExerciseListModel.class);

	private final UserWorkspace _workspace;

	public ExerciseListModel(UserWorkspace workspace, boolean rko) throws Exception {
		_workspace = workspace;
		
		List<Exercise> exercises = _workspace.getDataServiceProvider().getExercises(rko);
		exercises.stream().forEach((exercise) -> {
			exercise.getSkills().stream().forEach((skill) -> {
				// init lazy data
				skill.getName();
			});			
		});
		addAll(exercises);
	}
	
}
