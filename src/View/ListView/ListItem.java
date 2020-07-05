package View.ListView;

import Model.Note;
import javafx.scene.control.ListCell;

public class ListItem extends ListCell<Note>
{
    @Override
    public void updateItem(Note note, boolean empty)
    {
        super.updateItem(note,empty);
        if (note != null && !empty)
        {
            ItemView data = new ItemView();
            data.setInfo(note);
            setGraphic(data.getBox());
        }
        else
        {
        	setGraphic(null);
        }
    }
}
