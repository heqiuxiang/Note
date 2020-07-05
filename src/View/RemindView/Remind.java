package View.RemindView;

import Model.Note;

public class Remind
{
	private boolean isDone;
	private String bookName;
	private Note note;
	
	public Remind(Note note, String bookName)
	{
		this.note = note;
		this.bookName = bookName;
	}
	
	public String getBookName()
	{
		return bookName;
	}
	
	public Note getNote()
	{
		return note;
	}
	
	public void setNote(Note note)
	{
		note = note.clone();
	}
	
	public void setDone()
	{
		isDone = !isDone;
	}
	
	public boolean ifDone()
	{
		return isDone;
	}
}
