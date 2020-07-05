package View.RemindView;

import Model.Note;
import View.ListView.ItemView;
import javafx.scene.control.Cell;
import javafx.scene.control.ListCell;

public class RemindItem extends ListCell<Remind>
{
	@Override
    public void updateItem(Remind remind, boolean empty)
    {
        super.updateItem(remind, empty);
        if(remind != null && !empty)
        {
            RemindView data = new RemindView();
            data.setInfo(remind);
            setGraphic(data.getBox());
        }
        else
        {
        	setGraphic(null);
        }
    }
}
