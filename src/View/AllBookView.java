package View;

import java.beans.PropertyChangeEvent;
import java.net.URL;
import java.util.*;

import Controller.UserController;
import Model.Note;
import Model.NoteBook;
import Model.User;
import View.ListView.ListItem;
import View.RemindView.Remind;
import View.RemindView.RemindItem;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.util.Callback;

interface AllBookViewInterface
{
	void addNoteToBook(Note note, String noteBookChoosed);
	void removeDoneRemindItem();
	void removeNoteFromBook(int id, String noteBookChoosed);
	void setCurrentNoteListener(View Listlistener);
	ArrayList<String> getUserNoteBookNames();
	void setNoteBookNameListener(View listener);
	void setCurrentUser(User currentUser);
	void search(String text);
}

public class AllBookView extends View implements AllBookViewInterface
{
	@FXML private ListView<Remind> remindList;
	@FXML private TextField searchText;
	@FXML private Button searchButton;
	@FXML private ChoiceBox<String> listChooser;
	@FXML private Button notifyListButton;
	@FXML private NoteBookView notelistviewController;
	
	@Override
	public void initialize(URL location, ResourceBundle resources)
	{
		model = new User();
		model.initialize();
		controller = new UserController(model, this);
		model.addPropertyChangeListener(this);  
	}
	
	@Override
	public void setCurrentUser(User currentUser)
	{
		if (model == currentUser)
			return;
		
		model.removePropertyChangeListener(this);
		model = currentUser;
		model.addPropertyChangeListener(this);
		((UserController)controller).setCurrentUser(model);
		
		initRemindList();
		initListChooser();
	}
	
	private void initListChooser()
	{
		listChooser.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() { 
			@Override
			public void changed(ObservableValue<? extends String> arg0, String arg1, String arg2)
			{
				notifyListButton.setText((arg2 == null)?arg0.getValue():arg2);
			} 
        }); 
	}
	
	private void initRemindList()
	{
		remindList.setItems(FXCollections.observableArrayList(getRemindItems()));
		
		remindList.setCellFactory(new Callback<ListView<Remind>, ListCell<Remind>>(){
			@Override
			public ListCell<Remind> call(ListView<Remind> List) {
				return new RemindItem();
			}
		});	
	}
	
	@FXML
	private void searchButtonPressAction()
	{
		ArrayList<Note> result = ((UserController)controller).search(searchText.getText());
		
		NoteBook searchShowNoteBook = new NoteBook();
		searchShowNoteBook.setName("");
		searchShowNoteBook.setNotes(result);
		
		notelistviewController.setCurrentNoteBook(searchShowNoteBook);
		notifyListButton.setText("Search < " + searchText.getText() + " > Result");
	}
	
	@Override
	public void search(String text)
	{
		searchText.setText(text);
	}
	
	private ArrayList<Remind> getRemindItems()
	{
		ArrayList<Remind> items = new ArrayList<Remind>();
		for (NoteBook book : ((User)model).getNoteBooks())
		{
			for (Note n : book.getNotes())
			{
				if (null != n.getAlert())
					items.add(new Remind(n.clone(), book.getName()));
			}
		}
		return items;
	}
	
	@FXML
	private void notifyListButtonPressAction()
	{
		for (NoteBook book : ((User)model).getNoteBooks())
		{
			if (book.getName().equals(notifyListButton.getText()))
			{
				notelistviewController.setCurrentNoteBook(book);
			}
		}
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt)
	{
		switch (evt.getPropertyName())
		{
		case "new noteBooks":
			String chooseHelper = notifyListButton.getText();
			listChooser.getItems().clear();
			listChooser.getItems().addAll((ArrayList<String>)evt.getNewValue());
			// 避免choicebox改变后丢失button text
			notifyListButton.setText(chooseHelper);
			
			// remindList深删除已打勾的事项
			break;
		default:
			break;
		}
	}
	
	@Override
	public void removeDoneRemindItem()
	{
		for (Remind item : remindList.getItems())
		{
			if (item.ifDone())
			{
				removeNoteFromBook(item.getNote().getId(), item.getBookName());
				addNoteToBook(item.getNote(), item.getBookName());
				return;
			}
		}
	}

	@Override
	public void addNoteToBook(Note note, String noteBookName)
	{
		User thisUser= (User)model;
		// 找到本笔记本
		for (NoteBook nb : thisUser.getNoteBooks())
		{
			if (nb.getName().equals(noteBookName))
			{
				notelistviewController.setCurrentNoteBook(nb);
				NoteBook updatedNoteBook = notelistviewController.addNote(note);
				// 改变chooser视图
				notifyListButton.setText(noteBookName);
				// remind视图
				addToRemind(note.clone(), noteBookName);
				
				// user本身添加
				((UserController)controller).updateNoteBook(updatedNoteBook);
				
				return;
			}
		}
		
		throw new RuntimeException("can't find notebook named: \"" + noteBookName + "\"");
	}
	
	private void addToRemind(Note note, String noteBookName)
	{
		if (null == findRemindById(note.getId(), noteBookName))
		{
			if (null == note.getAlert())
				return;
			
			remindList.getItems().add(new Remind(note, noteBookName));
		}
		else
		{
			remindList.getItems().remove(findRemindById(note.getId(), noteBookName));
			remindList.getItems().add(new Remind(note, noteBookName));
		}
	}

	@Override
	public void removeNoteFromBook(int id, String noteBookName)
	{
		if (null == noteBookName)
			return;
		
		final User thisUser= (User)model;
		// 找到本笔记本
		for (NoteBook nb : thisUser.getNoteBooks())
		{
			if (nb.getName().equals(noteBookName))
			{
				notelistviewController.setCurrentNoteBook(nb);
				NoteBook updatedNoteBook = notelistviewController.removeNote(id);
				// 改变chooser视图
				notifyListButton.setText(noteBookName);
				// remind视图
				remindList.getItems().remove(findRemindById(id, noteBookName));
				
				// user本身添加
				((UserController)controller).updateNoteBook(updatedNoteBook);
				
				return;
			}
		}
		
		throw new RuntimeException("can't find notebook named: \"" + noteBookName + "\"");
	}
	
	private Remind findRemindById(int id, String bookName)
	{
		for (Remind n : remindList.getItems())
		{
			if (n.getNote().getId() == id && n.getBookName().equals(bookName))
			{
				return n;
			}
		}
		return null;
	}
	
	@Override
	public void setCurrentNoteListener(View Listlistener)
	{
		notelistviewController.setCurrentNoteListener(Listlistener);
	}

	@Override
	public ArrayList<String> getUserNoteBookNames()
	{
		ArrayList<NoteBook> noteBooks = ((User)model).getNoteBooks();
		ArrayList<String> names = new ArrayList<String>();
		for (NoteBook book : noteBooks)
		{
			names.add(book.getName());
		}
		
		return names;
	}

	// 供noteview监听笔记本的增减
	@Override
	public void setNoteBookNameListener(View listener)
	{
		((UserController)controller).setNoteBookNameListener(listener);
	}
}
