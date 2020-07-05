package Controller;

import java.util.*;

import Model.Model;
import Model.Note;
import Model.NoteContent;
import View.View;

interface NoteControllerInterface
{
	// ¸üÐÂmodel
	void updateNote(String title, String labels, String date, NoteContent noteContent);
	void setCurrentNote(Model model);
}

public class NoteController extends Controller implements NoteControllerInterface
{
	public NoteController(Model model, View view)
	{
		super(model, view);
		// model.initialize();
	}

	@Override
	public void updateNote(String title, String labels, String date, NoteContent noteContent)
	{
		//title
		if (null == title)
			title = "";
		
		// label
		ArrayList<String> label;
		if (null == labels || labels.equals(""))
			label = null;
		else
			label = new ArrayList<String>(Arrays.asList(labels.split(",")));
		
		// date
		Calendar cal;
		if (null == date || date.equals(""))
			cal = null;
		else
		{
			String[] dates = date.split("/");
			cal = Calendar.getInstance();
			cal.set(Integer.parseInt(dates[0].trim()), 
					Integer.parseInt(dates[1].trim())-1,
					Integer.parseInt(dates[2].trim()));
		}
		
		((Note)model).setTitle(title);
		((Note)model).setLabels(label);
		((Note)model).setAlert(cal);
		((Note)model).setContent(noteContent);
	}

	@Override
	public void setCurrentNote(Model model)
	{
		this.model = model;
		
		((Note)model).setTitle(((Note)model).getTitle());
		((Note)model).setLabels(((Note)model).getLabels());
		((Note)model).setAlert(((Note)model).getAlert());
		((Note)model).setContent(((Note)model).getContent());
	}
}
