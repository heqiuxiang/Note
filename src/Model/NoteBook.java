package Model;

import java.beans.PropertyChangeListener;
import java.util.*;
public class NoteBook extends Model
{
	private String name;
	private ArrayList<Note> notes;
	
	@Override
	public void initialize()
	{
		notes = new ArrayList<Note>();
	}
	
	// getter 
	public String getName()
	{
		return name;
	}

	public ArrayList<Note> getNotes()
	{
		return new ArrayList<Note>(notes);
	}
	
	// setter
	public void setName(String name)
	{
		observer.firePropertyChange("new name", null, name);
		this.name = name;
	}
	
	public void setNotes(ArrayList<Note> notes)
	{
		observer.firePropertyChange("new notes", null, notes);
		this.notes = new ArrayList<Note>(notes);
	}

	@Override
	public void addPropertyChangeListener(PropertyChangeListener listener)
	{
		observer.addPropertyChangeListener(listener);
	}

	@Override
	public void removePropertyChangeListener(PropertyChangeListener listener)
	{
		observer.removePropertyChangeListener(listener);
	}

	@Override
	public NoteBook clone()
	{
		NoteBook clone = new NoteBook();
		clone.setName(getName());
		clone.setNotes(getNotes());
		return clone;
	}
}
