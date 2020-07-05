package View;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeSupport;
import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

import Controller.NoteBookController;
import Model.Note;
import Model.NoteBook;
import Model.User;
import View.ListView.ListItem;
import javafx.collections.FXCollections;
import javafx.collections.ObservableArray;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.util.Callback;

interface NoteListViewInterface
{
	NoteBook addNote(Note note);
	NoteBook removeNote(int id);
	void setCurrentNoteBook(NoteBook noteBookChoosed);
	void setCurrentNoteListener(View ListListener);
}

// 本区域没有显式的改变model的事件，而用于给外界接口
public class NoteBookView extends View implements NoteListViewInterface
{
	PropertyChangeSupport listObserver = new PropertyChangeSupport(this);
	
	@FXML private ListView<Note> noteList;
	
	@Override
	public void initialize(URL location, ResourceBundle resources)
	{
		model = new NoteBook();
		model.initialize();
		controller = new NoteBookController(model, this);
		
		model.addPropertyChangeListener(this);
		
		// 初始化listview
		initNoteList();
	}
	
	private void initNoteList()
	{
		noteList.setItems(FXCollections.observableArrayList(((NoteBook)model).getNotes()));
		
		noteList.setCellFactory(new Callback<ListView<Note>, ListCell<Note>>(){
			@Override
			public ListCell<Note> call(ListView<Note> noteList) {
				return new ListItem();
			}
		});	
	}
	
	@FXML
	private void onItemClicked()
	{
		// 避免只点了框，没点选项报错np
		if (null == noteList.getSelectionModel().getSelectedItem())
			return;
		
		int noteNameId = noteList.getSelectionModel().getSelectedItem().getId();
		Note choosedNote = findNoteById(noteNameId);
		
		// 获得点击的note
		listObserver.firePropertyChange("listChoosedNoteChanged", null, choosedNote.clone());
	}
	
	@Override
	public void propertyChange(PropertyChangeEvent evt)
	{
		//改变list
		switch (evt.getPropertyName())
		{
		case "new notes":
			noteList.getItems().clear();
			noteList.getItems().addAll((ArrayList<Note>)evt.getNewValue());
			break;
		}
	}
	
	private Note findNoteById(int id)
	{
		for (Note n : ((NoteBook)model).getNotes())
		{
			if (n.getId() == id)
			{
				return n;
			}
		}
		throw new RuntimeException("can't find Note with id: "+id);
	}

	@Override
	public NoteBook addNote(Note note)
	{
		return ((NoteBookController)controller).addNote(note);
	}

	@Override
	public NoteBook removeNote(int id)
	{
		return ((NoteBookController)controller).removeNote(id);
	}
	
	@Override
	public void setCurrentNoteBook(NoteBook noteBook)
	{	
		model.removePropertyChangeListener(this);
		model = noteBook.clone();
		model.addPropertyChangeListener(this);
		((NoteBookController)controller).setCurrentNoteBook(model);
	}

	@Override
	public void setCurrentNoteListener(View ListListener)
	{
		listObserver.addPropertyChangeListener(ListListener);
	}
}
