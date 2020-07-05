package View.ListView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import Model.Note;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class ItemView
{
	@FXML Label label_1;
	@FXML Label label_2;
	@FXML VBox vbox;
	
	public ItemView()
	{
//		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/View/ListView/ListItem.fxml"));
//		
//		fxmlLoader.setController(this);
//		try
//        {
//            fxmlLoader.load();
//        }
//        catch (IOException e)
//        {
//            throw new RuntimeException(e);
//        }
		label_1 = new Label();
		label_2 = new Label();
		vbox = new VBox(label_1, label_2);
	}
	
	public void setInfo(Note note)
	{
		label_1.setText("标题：" + note.getTitle());
		
		ArrayList<String> noteLabels = note.getLabels();
		
		try
		{
			label_2.setText("标签：" + noteLabels.toString());
		}
		catch(Exception e)
		{
			label_2.setText("标签：" + "");
		}
		
	}
	
	public VBox getBox()
	{
		return vbox;
	}
}
