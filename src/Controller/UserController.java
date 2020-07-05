package Controller;

import java.util.*;

import Model.Model;
import Model.Note;
import Model.NoteBook;
import Model.User;
import View.View;

interface MainViewInterface
{
	void addNoteBook(String noteBookName);
	void setCurrentUser(Model model);
}

interface AllBookControllerInterface
{
	void setCurrentUser(Model model);
	void updateNoteBook(NoteBook updatedNoteBook);
	void setNoteBookNameListener(View listener);
	ArrayList<Note> search(String searchText);
}

public class UserController extends Controller 
		implements AllBookControllerInterface, MainViewInterface
{
	private View noteBookNameListener = null;
	
	public UserController(Model model, View view)
	{
		super(model, view);
	}

	@Override
	public void setCurrentUser(Model model)
	{
		this.model = model;
		
		// Ã·–—noteview
		notifynoteBookNameListener();
		
		((User)model).setAccount(((User)model).getAccount());
		((User)model).setNoteBooks(((User)model).getNoteBooks());
		((User)model).setPassword(((User)model).getPassword());
	}

	@Override
	public void updateNoteBook(NoteBook updatedNoteBook)
	{
		if (null == updatedNoteBook)
			return;
		
		ArrayList<NoteBook> noteBooks = ((User)model).getNoteBooks();
		for (NoteBook book : noteBooks)
		{
			if (book.getName().equals(updatedNoteBook.getName()))
			{
				noteBooks.remove(book);
				noteBooks.add(updatedNoteBook);
				break;
			}
		}
		((User)model).setNoteBooks(noteBooks);
	}

	@Override
	public void addNoteBook(String noteBookName)
	{		
		if (null == noteBookName || noteBookName.isBlank() || noteBookName.isEmpty())
			throw new RuntimeException("invalide NoteBook name : empty input");
		for (NoteBook book : ((User)model).getNoteBooks())
		{
			if (book.getName().equals(noteBookName))
				throw new RuntimeException("invalide NoteBook name : repeated NoteBook name");
		}
		
		ArrayList<NoteBook> books = ((User)model).getNoteBooks();
		NoteBook book = new NoteBook();
		book.initialize();
		book.setName(noteBookName);
		books.add(book);
		((User)model).setNoteBooks(books);
	}
	
	@Override
	public ArrayList<Note> search(String searchText)
	{
		searchText = searchText.trim();
		ArrayList<Note> result = new ArrayList<>();
		
		for (NoteBook book : ((User)model).getNoteBooks())
		{
			for (Note note : book.getNotes())
			{
				String labelStr = "";
				try
				{
					labelStr = note.getLabels().toString()
							.substring(1, note.getLabels().toString().length()-1)
							.replace(',', ' ');
				}
				catch(Exception e)
				{
					labelStr = " ";
				}
				
				if (labelStr.contains(searchText)
						|| note.getTitle().contains(searchText))
					result.add(note);
			}
		}
		
		return result;
	}

	@Override
	public void setNoteBookNameListener(View listener)
	{
		noteBookNameListener = listener;
	}
	
	private void notifynoteBookNameListener()
	{
		model.addPropertyChangeListener(noteBookNameListener);
	}
}
