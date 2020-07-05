package Controller;

import Model.Model;
import View.View;

public abstract class Controller
{
	protected Model model;
	protected View view;
	
	protected Controller(Model model, View view)
	{
		this.model = model;
		this.view = view;
//		model.initialize();
	}
}
