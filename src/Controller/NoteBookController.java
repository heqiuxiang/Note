package Controller;

import java.util.*;

import Model.Model;
import Model.Note;
import Model.NoteBook;
import View.View;

interface NoteListControllerInterface
{
	NoteBook addNote(Note note);
	NoteBook removeNote(int id);
	void setCurrentNoteBook(Model model);
}

public class NoteBookController extends Controller implements NoteListControllerInterface
{

	public NoteBookController(Model model, View view)
	{
		super(model, view);
		// model.initialize();
	}

	@Override
	public NoteBook addNote(Note note)
	{
		if (null == note)
			throw new RuntimeException();
		
		ArrayList<Note> notes = (ArrayList<Note>) ((NoteBook)model).getNotes();
		
		// 如果是完全相同的笔记则覆盖
		for (Note n : notes)
		{
			if (n.getId() == note.getId())
			{
//				n = note.clone();
				notes.remove(n);
				notes.add(note);
				((NoteBook)model).setNotes(notes);
				return (NoteBook)model.clone();
			}
		}
		
		notes.add(note);
		((NoteBook)model).setNotes(notes);
		return (NoteBook)model.clone();
	}

	@Override
	public NoteBook removeNote(int id)
	{
		ArrayList<Note> notes = (ArrayList<Note>) ((NoteBook)model).getNotes();
		
		for (Note n : notes)
		{
			if (id == n.getId())
			{
				notes.remove(n);
				((NoteBook)model).setNotes(notes);
				return ((NoteBook)model).clone();
			}
		}
		
		return null;
//		throw new RuntimeException("can't find such note");
	}

	@Override
	public void setCurrentNoteBook(Model model)
	{
		this.model = model;
		((NoteBook)model).setName(((NoteBook)model).getName());
		((NoteBook)model).setNotes(((NoteBook)model).getNotes());
	}
}
