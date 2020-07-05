package View;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.NotSerializableException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

import Controller.Editor;
import Model.Model;
import Model.NoteContent;
import application.Main;
import Controller.IOOperator;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Worker.State;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Separator;
import javafx.scene.control.ToolBar;
import javafx.scene.effect.DropShadow;
import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.text.Text;
import javafx.scene.web.HTMLEditor;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.FileChooser;
import javafx.stage.Window;
import netscape.javascript.JSException;
import netscape.javascript.JSObject;

interface EditorViewInterface
{
	NoteContent updateContent(boolean clear);
	void setCurrentContent(NoteContent content);
}

public class EditorView extends View implements EditorViewInterface
{
	@FXML private HTMLEditor htmlEditor;
	private WebEngine engine;
	
	private Button 
			ImgButton = new Button("Img"), 
			AttachButton = new Button("Attach"), 
			AudioButton = new Button("Audio");
	
	@Override
	public void initialize(URL location, ResourceBundle resources)
	{
		model = new NoteContent();
		model.initialize();
		controller = new Editor(model, this);
		
		model.addPropertyChangeListener(this);

		// 加载htmleditor编辑工具
		initWebEngine();
		
		// add pic audio attachment buttons
		addExtraButton();
	}
	
	// 响应被观察者model中的改变
	@Override
	public void propertyChange(PropertyChangeEvent evt)
	{
		htmlEditor.setHtmlText((String)evt.getNewValue());
	}
	
	// 给noteview一个接口
	@Override
	public NoteContent updateContent(boolean clear)
	{
		// 点击确认按钮之后，更新内容
		if (clear)
			((Editor)controller).updateText("");
		else
			((Editor)controller).updateText(htmlEditor.getHtmlText());
		
		return (NoteContent) this.model;
	}
	
	@Override
	public void setCurrentContent(NoteContent content)
	{
		model.removePropertyChangeListener(this);
		model = content.clone();
		model.addPropertyChangeListener(this);
		
		((Editor)controller).setCurrentContent(model);
	}
	
	private void addExtraButton()
	{
		Node node = htmlEditor.lookup(".top-toolbar");
		if (!(node instanceof ToolBar))
			throw new RuntimeException();
		
		ToolBar bar = (ToolBar) node;
		bar.getItems().add(ImgButton);
		bar.getItems().add(AttachButton);
		bar.getItems().add(AudioButton);
		bar.getItems().add(new Separator(Orientation.VERTICAL));
		
		ImgButton.setOnAction((event) -> onBtnAction("Img"));
		AttachButton.setOnAction((event) -> onBtnAction("Attach"));
		AudioButton.setOnAction((event) -> onBtnAction("Audio"));
	}
	
	private void onBtnAction(String actionType)
	{
		try
		{
			File file = IOOperator.ChooseFile(htmlEditor.getScene().getWindow(), actionType);
			String[] fileName = file.getAbsolutePath().split("\\\\");
			
        	String base64data = IOOperator.importDataFile(file);
        	String pic = htmlExcecutor.htmlImgHead + base64data + htmlExcecutor.htmlImgTail;
			switch(actionType)
			{
				case "Img":
					break;
				case "Attach":
//					((Editor)controller).addAttach(base64data);
					pic = urlToPic("file.png");
					pic = htmlExcecutor.getDownloadHead(base64data, fileName[fileName.length-1]) 
							+ htmlExcecutor.htmlImgHead + pic + htmlExcecutor.htmlImgTail;
					break;
				case "Audio":
//					((Editor)controller).addAudio(base64data);
					pic = urlToPic("audio.png");
					pic = htmlExcecutor.getPlayHead(base64data, fileName[fileName.length-1])
							+ htmlExcecutor.htmlImgHead + pic + htmlExcecutor.htmlImgTail;
					break;
				default:
					break;
			}

			insertPic(pic);
			
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}   
	}
	
	private void insertPic(String pic)
	{
		try
		{
			insertAfterCursor(pic);
			((Editor)controller).updateText(htmlEditor.getHtmlText());
		}
		catch (Exception e)
		{
			// 无光标
			((Editor)controller).updateText(htmlEditor.getHtmlText() + pic);
		}
	}

	// 取得resource中的图片
	private String urlToPic(String picName) throws Exception
	{
		URL url = getClass().getClassLoader().getResource("image/" + picName);
		String base64pic = IOOperator.getResourceImg(url);
		return base64pic;
	}
	
	// 加载htmleditor编辑工具
	private void initWebEngine()
	{
		Node webNode = htmlEditor.lookup(".web-view");
		if (!(webNode instanceof WebView)) 
			throw new RuntimeException();
		
		WebView webView = (WebView) webNode;
		engine = webView.getEngine();
		
		// html内点击下载
		EventInHtml eventInHtml = new EventInHtml();
		engine.getLoadWorker().stateProperty().addListener(new ChangeListener<State>()
		{
			@Override
			public void changed(ObservableValue ov, State oldState, State newState)
			{
				if (newState == State.SUCCEEDED)
				{
					JSObject win = (JSObject) engine.executeScript("window");
                    win.setMember("eventInHtml", eventInHtml);
				}
			}
		});
	}
	
	private void insertAfterCursor(String txt) throws Exception
	{
		engine.executeScript(htmlExcecutor.jsCodeInsertHtml.replace("####html####",
				htmlExcecutor.escapeJavaStyleString(txt, true, true)));
	}
	
	// html内点击下载
	public class EventInHtml
	{
		private MediaPlayer mediaPlayer;
		
		public void download(String base64data, String name)
		{
			// 下载
			String path = IOOperator.ChooseFolder(htmlEditor.getScene().getWindow());
			try
			{
				IOOperator.serialize(path + "\\" + name, Base64.getDecoder().decode(base64data));
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}
		
		public void play(String base64data, String name)
		{
			try
			{
				Path path = Paths.get(".\\outputFile\\audio\\" + name);
				Files.write(path, Base64.getDecoder().decode(base64data));
				
				Media hit = new Media(path.toUri().toString());
				mediaPlayer = new MediaPlayer(hit);
				mediaPlayer.play();
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
	}
}

class htmlExcecutor
{
	static String 
	htmlImgPlay = "<a href=\"javascript:eventInHtml.download();\">",
	htmlImgDownload = "<a href=\"javascript:eventInHtml.play();\">",
	htmlImgHead = "<p><img src=\"data:image/;base64, ",
	htmlImgTail = "\"/></p>";
	
	static String jsCodeInsertHtml = 
			"function insertHtmlAtCursor(html) {\n" +
                    "    var range, node;\n" +
                    "    if (window.getSelection && window.getSelection().getRangeAt) {\n" +
                    "        range = window.getSelection().getRangeAt(0);\n" +
                    "        node = range.createContextualFragment(html);\n" +
                    "        range.insertNode(node);\n" +
                    "    } else if (document.selection && document.selection.createRange) {\n" +
                    "        document.selection.createRange().pasteHTML(html);\n" +
                    "    }\n" +
                    "}\n" +
                    "insertHtmlAtCursor('####html####')";
	
	static String getPlayHead(String base64data, String name)
	{
		return "<a href=\"javascript:eventInHtml.play('" + base64data + "', '"+ name +"');\">"+ name +"\n";
	}
	
	static String getDownloadHead(String base64data, String name)
	{
		return "<a href=\"javascript:eventInHtml.download('" + base64data + "', '"+ name +"');\">"+ name +"\n";
	}
	
    private static String hex(int i) {
        return Integer.toHexString(i);
    }
    
	static String escapeJavaStyleString(String str,
            boolean escapeSingleQuote, boolean escapeForwardSlash) {
        StringBuilder out = new StringBuilder("");
        if (str == null) {
            return null;
        }
        int sz;
        sz = str.length();
        for (int i = 0; i < sz; i++) {
            char ch = str.charAt(i);

            // handle unicode
            if (ch > 0xfff) {
                out.append("\\u").append(hex(ch));
            } else if (ch > 0xff) {
                out.append("\\u0").append(hex(ch));
            } else if (ch > 0x7f) {
                out.append("\\u00").append(hex(ch));
            } else if (ch < 32) {
                switch (ch) {
                    case '\b':
                        out.append('\\');
                        out.append('b');
                        break;
                    case '\n':
                        out.append('\\');
                        out.append('n');
                        break;
                    case '\t':
                        out.append('\\');
                        out.append('t');
                        break;
                    case '\f':
                        out.append('\\');
                        out.append('f');
                        break;
                    case '\r':
                        out.append('\\');
                        out.append('r');
                        break;
                    default:
                        if (ch > 0xf) {
                            out.append("\\u00").append(hex(ch));
                        } else {
                            out.append("\\u000").append(hex(ch));
                        }
                        break;
                }
            } else {
                switch (ch) {
                    case '\'':
                        if (escapeSingleQuote) {
                            out.append('\\');
                        }
                        out.append('\'');
                        break;
                    case '"':
                        out.append('\\');
                        out.append('"');
                        break;
                    case '\\':
                        out.append('\\');
                        out.append('\\');
                        break;
                    case '/':
                        if (escapeForwardSlash) {
                            out.append('\\');
                        }
                        out.append('/');
                        break;
                    default:
                        out.append(ch);
                        break;
                }
            }
        }
        return out.toString();
    }
}
