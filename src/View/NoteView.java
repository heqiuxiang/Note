package View;

import java.beans.PropertyChangeEvent;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.*;

import Controller.NoteController;
import Model.Note;
import Model.NoteBook;
import Model.NoteContent;
import Model.User;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;

interface NoteViewInterface
{
	void setCurrentUser(User currentUser);
	void updateNote(Note note);
	Note getCurrentNote();
	void allBookViewSearch(String text);
	void refreshRemind();
}

public class NoteView extends View implements CurrentNoteListener, NoteViewInterface
{
	@FXML private TextField titleText, remindTimeText;
	@FXML private Label labelText;
	@FXML private Button deleteButton, remindButton, 
				doneButton, labelButton;
	@FXML private ChoiceBox<String> noteBookChooser;
	private String noteBookChoosed;
	private String lastnoteBookChoosed;
	
	// ���ڿ���editor�Լ�notebook����
	@FXML private EditorView editorviewController;
	@FXML private AllBookView allbookviewController;
	
	private void setNoteBookChoosed(String noteBookName, String lastnoteBookName)
	{
		noteBookChoosed = noteBookName;
		lastnoteBookChoosed = lastnoteBookName;
	}
	
	@Override
	public void initialize(URL location, ResourceBundle resources)
	{
		// mvc
		model = new Note();
		model.initialize();
		controller = new NoteController(model, this);
		
		model.addPropertyChangeListener(this);
		//ע��list��listener ��ȡ����notebook����Ϣ
		setCurrentNoteListener(this);
		
		// �����ʼ��
		remindTimeText.setDisable(true);
		initNoteBookChooser();
	}

	@SuppressWarnings("unchecked")
	@Override
	public void propertyChange(PropertyChangeEvent evt)
	{
		switch (evt.getPropertyName())
		{
		case "new labels":
			String labels = getLabelsText((ArrayList<String>)evt.getNewValue());
			labelText.setText(labels);
			break;
		case "new alert":
			remindTimeText.setText(getCalendarStr((Calendar)evt.getNewValue()));
			break;
		case "new content":
			editorviewController.setCurrentContent((NoteContent)evt.getNewValue());
			break;
		case "new title":
			try
			{
				titleText.setText(((String)evt.getNewValue()).toString());
			}
			catch (NullPointerException e)
			{
				titleText.setText("");
			}
			break;
		case "new noteBooks":
			String chooseHelper = noteBookChooser.getValue();
			noteBookChooser.getItems().clear();
			noteBookChooser.getItems().addAll((ArrayList<String>)evt.getNewValue());
			noteBookChooser.setValue(chooseHelper);
			break;
		case "listChoosedNoteChanged":
			updateNote((Note)evt.getNewValue());
			break;
		default:
			break;
		}
	}
	
	private String getLabelsText(ArrayList<String> strs)
	{
		if (null == strs || strs.isEmpty())
			return "";
		
		StringBuilder builder = new StringBuilder();
		for (String s : strs)
		{
			builder.append(s);
			builder.append(",");
		}
		
		return builder.toString();
	}
	
	@Override
	public void updateNote(Note choosedNote)
	{	
		if (null == choosedNote)
			return;
		
		model.removePropertyChangeListener(this);
		model = choosedNote.clone();
		model.addPropertyChangeListener(this);
		((NoteController)controller).setCurrentNote(model);
		
		// ������ͼ
		editorviewController.setCurrentContent(choosedNote.getContent());
	}
	
	private String getCalendarStr(Calendar cal)
	{
		if (null == cal)
			return "";
		SimpleDateFormat format1 = new SimpleDateFormat("yyyy/MM/dd");
		String formatted = format1.format(cal.getTime());
		return formatted.toString();
	}
	
	// ����¼�
	@FXML
	private void doneButtonPressAction()
	{
		// ����model����
		((NoteController)controller).updateNote(
				titleText.getText(), 
				labelText.getText(), 
				remindTimeText.getText(),
				editorviewController.updateContent(false));
		
		// ֪ͨ��Ӧ��notebook��ӱ�note
		// remove�������ʼǱ�
		allbookviewController.removeNoteFromBook(((Note)model).getId(), lastnoteBookChoosed);
		allbookviewController.addNoteToBook(((Note)model).clone(), noteBookChoosed);
	}
	
	@FXML
	private void deleteButtonPressAction()
	{
		// ����model����
		((NoteController)controller).updateNote("", "", "", 
				editorviewController.updateContent(true));
		
		// ֪ͨ��Ӧ��notebookɾ����note
		allbookviewController.removeNoteFromBook(((Note)model).getId(), noteBookChoosed);
	}
	
	// TODO : ֪ͨallbookview�������б�ı�
	@FXML
	private void remindButtonPressAction()
	{
		TextInputDialog dialog = new TextInputDialog("2020/05/16");
		dialog.setTitle("alert");
		dialog.setHeaderText("set your alert");
		dialog.setContentText("Please enter your alert time:");

		Optional<String> result = dialog.showAndWait();

		result.ifPresent(alert -> remindTimeText.setText(alert));
	}

	@FXML
	private void labelButtonPressAction()
	{
		TextInputDialog dialog = new TextInputDialog("label1,label2");
		dialog.setTitle("labels");
		dialog.setHeaderText("set your labels");
		dialog.setContentText("Please enter your labels:");

		Optional<String> result = dialog.showAndWait();

		result.ifPresent(labels -> labelText.setText(labels));
	}
	
	private void initNoteBookChooser()
	{
		setNoteBookChoosed("defaultBook", null);
		noteBookChooser.setValue(noteBookChoosed);
		
		// ����ʼǱ�������ע��ʼǱ����ļ�����
		ArrayList<String> noteBookNames = allbookviewController.getUserNoteBookNames();
		allbookviewController.setNoteBookNameListener(this);
		
		noteBookChooser.setItems(FXCollections.observableArrayList(noteBookNames));
		noteBookChooser.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() { 
			@Override
			public void changed(ObservableValue<? extends String> arg0, String arg1, String arg2)
			{
				setNoteBookChoosed((arg2 == null)?arg0.getValue():arg2, arg1);
			} 
        }); 
	}
	
	
	// ��list�ļ���
	private void setCurrentNoteListener(View Listlistner)
	{
		allbookviewController.setCurrentNoteListener(Listlistner);
	}
	
	@Override
	public void setCurrentUser(User currentUser)
	{
		allbookviewController.setCurrentUser(currentUser);
	}

	@Override
	public Note getCurrentNote()
	{
		return (Note)model.clone();
	}

	@Override
	public void allBookViewSearch(String text)
	{
		allbookviewController.search(text);
	}

	@Override
	public void refreshRemind()
	{
		allbookviewController.removeDoneRemindItem();
	}
}