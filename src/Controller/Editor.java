package Controller;

import java.util.*;

import Model.Model;
import Model.NoteContent;
import View.View;

interface EditorInterface
{
	void addAttach(String base64data);
	void addAudio(String base64data);
	void deleteContentFile(int id);
	void updateText(String text);
	void downloadFile();
	void playAudio();
	void setCurrentContent(Model model);
}

public class Editor extends Controller implements EditorInterface
{
	// 记录所有的文件id 删除时通过id确定model中该删除谁
	private ArrayList<Integer> IDs = new ArrayList<Integer>();
	
	public Editor(Model model, View view)
	{
		super(model, view);
		// model.initialize();
	}

	@Override
	public void updateText(String text)
	{
		((NoteContent)model).setText(text);
	}

	@Override
	public void addAttach(String base64data)
	{
		int id = ((NoteContent)model).addAttachs(base64data);
		IDs.add(id);
	}

	@Override
	public void addAudio(String base64data)
	{
		int id = ((NoteContent)model).addAttachs(base64data);
		IDs.add(id);
	}

	// TODO : 视图点击事件处理
	@Override
	public void downloadFile()
	{
		// TODO Auto-generated method stub
		
	}
	
	// TODO : 视图点击事件处理
	@Override
	public void playAudio()
	{
		// TODO Auto-generated method stub
		
	}

	// TODO : 视图删除事件的处理
	@Override
	public void deleteContentFile(int id)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setCurrentContent(Model model)
	{
		this.model = model;
		((NoteContent)model).setText(((NoteContent)model).getText());
	}
}
