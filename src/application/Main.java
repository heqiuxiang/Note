package application;

import View.MainView;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;

public class Main extends Application
{
	@Override
	public void start(Stage primaryStage)
	{
		try
		{
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/layout/Main.fxml"));
			fxmlLoader.setBuilderFactory(new JavaFXBuilderFactory());
			VBox root = (VBox)fxmlLoader.load();
			Scene scene = new Scene(root, 1095, 760);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.setTitle("MyNote");
			primaryStage.show();
			
			((MainView)fxmlLoader.getController()).setCloseWindowEvent(primaryStage);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public static void main(String[] args)
	{
		launch(args);
	}
}
