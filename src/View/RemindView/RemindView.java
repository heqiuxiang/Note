package View.RemindView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;

import Model.Note;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class RemindView
{
	@FXML HBox hBox;
	@FXML CheckBox checkBox;
	@FXML Label label;
	
	private Remind dataHolder;
	private Calendar tempAlertHolder;
	
	private void setData(Remind data)
	{
		dataHolder = data;
		tempAlertHolder = dataHolder.getNote().getAlert();
	}
	
	@FXML
	private void onSelectedAction()
	{
		if (dataHolder.ifDone())
		{
			dataHolder.getNote().setAlert(tempAlertHolder);
			label.setText(dataHolder.getNote().getAlert().get(Calendar.YEAR)  + "/" 
					+(dataHolder.getNote().getAlert().get(Calendar.MONTH) + 1) + "/"
					+ dataHolder.getNote().getAlert().get(Calendar.DAY_OF_MONTH));
		}
		else
		{
			dataHolder.getNote().setAlert(null);
			label.setText("done");
		}
		dataHolder.setDone();
	}
	
	public RemindView()
	{
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/View/RemindView/RemindItem.fxml"));
		fxmlLoader.setController(this);
		
		try
        {
            fxmlLoader.load();
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }
	}
	
	public void setInfo(Remind data)
	{
		setData(data);
		checkBox.setText(data.getNote().getTitle() + ":" + data.getBookName());
		if (null == data.getNote().getAlert())
		{
			if (data.ifDone())
				data.setDone();
			
			onSelectedAction();
		}
		
		label.setText(data.getNote().getAlert().get(Calendar.YEAR)  + "/" 
					+(data.getNote().getAlert().get(Calendar.MONTH) + 1) + "/"
					+ data.getNote().getAlert().get(Calendar.DAY_OF_MONTH));
	}
	
	public HBox getBox()
	{
		return hBox;
	}
}
