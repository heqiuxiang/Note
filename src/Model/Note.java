package Model;

import java.beans.PropertyChangeListener;
import java.util.*;

public class Note extends Model
{	
	// 同一个笔记本下的笔记允许同名，所以用id来识别
	private static int cnt = 1;
	private int id = cnt ++;	
	private String title;
	private ArrayList<String> labels; 
	private Calendar alert;
	private NoteContent noteContent;

	@Override
	public void initialize()
	{
		// TODO Auto-generated method stub
		labels = new ArrayList<String>();
		alert = null;
		noteContent = new NoteContent();
		noteContent.initialize();
	}
	
	// getter
	public int getId()
	{
		return id;
	}
	
	public ArrayList<String> getLabels()
	{
		try
		{
			return new ArrayList<String>(this.labels);
		}
		catch (NullPointerException e)
		{
			return null;
		}
	}
	
	public Calendar getAlert()
	{
		try
		{
			return (Calendar) alert.clone();
		}
		catch (NullPointerException e)
		{
			return null;
		}
		
	}
	
	public NoteContent getContent()
	{
		return noteContent.clone();
	}
	
	public String getTitle()
	{
		return title;
	}
	
	
	// setter
	public void setLabels(final ArrayList<String> labels)
	{
		observer.firePropertyChange("new labels", null, labels);
		if (labels == null)
			this.labels = null;
		else
			this.labels = new ArrayList<String>(labels);
	}
	
	public void setAlert(final Calendar alert)
	{
		observer.firePropertyChange("new alert", null, alert);
		try
		{
			this.alert = (Calendar) alert.clone();
		}
		catch (NullPointerException e)
		{
			this.alert = null;
		}
		
	}
	
	public void setContent(final NoteContent content)
	{
		if (null == content)
			throw new RuntimeException();
		
		observer.firePropertyChange("new content", null, content);
		this.noteContent = content.clone();
	}
	
	public void setTitle(String title)
	{
		observer.firePropertyChange("new title", null, title);
		this.title = title;
	}
	
	// noteObserver
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
	public Note clone()
	{
		Note clone = new Note();
		clone.id = id;
		clone.setAlert(getAlert());
		clone.setContent(getContent());
		clone.setLabels(getLabels());
		clone.setTitle(getTitle());
		return clone;
	}
}