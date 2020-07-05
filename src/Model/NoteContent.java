package Model;

import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.Serializable;
import java.net.URL;
import java.util.*;

import Controller.IOOperator;

public class NoteContent extends Model
{
	// text������ֱ�Ӵ�ͼƬ����Ϊ����ͼƬ����base64
	private String text;
	private ArrayList<Attachment> attachs;
	private ArrayList<Audio> audios;

	@Override
	public void initialize()
	{
		attachs = new ArrayList<Attachment>();
		audios = new ArrayList<Audio>();
		text = "";
	}
	
	// getters
	public String getText()
	{
		return text;
	}
	
	public ArrayList<Attachment> getAttachs()
	{
		return new ArrayList<Attachment>(this.attachs);
	}
	
	public ArrayList<Audio> getAudios()
	{
		return new ArrayList<Audio>(this.audios);
	}
	
	// setter
	public void setText(String text)
	{
		observer.firePropertyChange("text", null, text);
		this.text = text;
	}
	
	public int addAttachs(String base64data)
	{
		Attachment attach = new Attachment(base64data);
		attachs.add(attach);
		return attach.getId();
	}
	
	public int addAudio(String base64data)
	{
		Audio audio = new Audio(base64data);
		audios.add(audio);
		return audio.getId();
	}
	
	// TODO : �����ⲿ��
	public void deleteContentFile()
	{
		
	}
	
	@Override
	public void addPropertyChangeListener(PropertyChangeListener listener)
	{
		observer.addPropertyChangeListener(listener);
	}

	@Override
	public void removePropertyChangeListener(PropertyChangeListener listener)
	{
		observer.removePropertyChangeListener(listener);
	}
	
	@Override
	public NoteContent clone()
	{
		NoteContent clone = new NoteContent();
		clone.initialize();
		clone.setText(getText());
		clone.attachs = attachs;
		clone.audios = audios;
		return clone;
	}
}

/**
 * 
 * @author 18069
 * Ҫʵ�ִ�base64����base4��Ϊ��֪��ַ�ı����ļ�������uri
 * Ҫ����htmleditor����ʾʱ��pic�����Լ���uri����������Ƶ����
 */
abstract class ContentFile implements Serializable
{
	// ��id���������
	private static int cnt = 0;
	protected final int id = cnt++;
	
	protected String base64data;
	
	protected int getId()
	{
		return id;
	}
	
	protected ContentFile(String base64)
	{
		this.base64data = base64;
	}
	// ת��Ϊfile������
	public File download(String path)
	{
		return null;
	}
	
	protected String getFile()
	{
		return base64data;
	}
}

class Attachment extends ContentFile
{
	public Attachment(String base64)
	{
		super(base64);
	}
}

class Audio extends ContentFile
{
	public Audio(String base64)
	{
		super(base64);
	}
}
