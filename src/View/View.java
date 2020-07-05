package View;

import java.beans.PropertyChangeListener;

import Controller.Controller;
import Model.Model;
import javafx.fxml.Initializable;

public abstract class View implements Initializable, PropertyChangeListener
{
	protected Model model;
	protected Controller controller;
}
