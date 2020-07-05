package application;

import Controller.DBHelper;
import Controller.IOOperator;
import Model.User;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class Login extends Application
{
	private Label lblName;
	private Label lblPassword;

	private TextField txtName;
	private PasswordField pwdPassword;

	private Button btnLogin;
	private Button btnRegister;
	private Button btnClear;

	private GridPane gr;

	@Override
	public void start(Stage primaryStage) throws Exception
	{
		initView(primaryStage);

		btnLogin.setOnAction(new EventHandler<ActionEvent>()
		{
			@Override
			public void handle(ActionEvent event)
			{
				// ��ȡ�û�������
				String username = txtName.getText().trim();
				String password = pwdPassword.getText().trim();
				String userPath = "outputFile\\" + username + ".user";

				try
				{
					//��ȡ�û�
					DBHelper db = new DBHelper();
					User user = db.getUser(username);
					//User user = (User)IOOperator.deserialize(userPath);
					if (!user.getPassword().equals(password))
						throw new Exception();

					// ����������
					Platform.runLater(() ->
					{
						try
						{
							UserManager.user = user;
							Main m = new Main();
							m.start(new Stage());
							primaryStage.close();
						}
						catch (Exception e)
						{
							e.printStackTrace();
						}

					});
				}
				catch (Exception e)
				{
					showAlert();
					pwdPassword.setText("");
				}
			}
		});

		btnRegister.setOnAction(new EventHandler<ActionEvent>()
		{

			@Override
			public void handle(ActionEvent event)
			{
				// ����ע�����
				Platform.runLater(() ->
				{
					try
					{
						new Register().start(new Stage());
					}
					catch (Exception e)
					{
						e.printStackTrace();
					}
					primaryStage.close();
				});
			}
		});

		btnClear.setOnAction(new EventHandler<ActionEvent>()
		{

			@Override
			public void handle(ActionEvent event)
			{
				// ����ı��������
				txtName.setText("");
				pwdPassword.setText("");
			}

		});
	}

	private void initView(Stage primaryStage)
	{
		lblName = new Label("�û�");
		lblPassword = new Label("����");

		txtName = new TextField();
		pwdPassword = new PasswordField();

		btnLogin = new Button("��¼");
		btnRegister = new Button("ע��");
		btnClear = new Button("���");

		gr = new GridPane();
		gr.add(lblName, 0, 0);
		gr.add(txtName, 1, 0);
		gr.add(lblPassword, 0, 1);
		gr.add(pwdPassword, 1, 1);

		gr.add(btnLogin, 0, 2);
		gr.add(btnRegister, 1, 2);
		gr.add(btnClear, 2, 2);

		gr.setHgap(5);
		gr.setVgap(17);

//		GridPane.setMargin(btnLogin, new Insets(0,0,0,120));

		gr.setAlignment(Pos.CENTER);

		Scene scene = new Scene(gr);
		primaryStage.setScene(scene);
		primaryStage.setTitle("Login");
		primaryStage.setWidth(500);
		primaryStage.setHeight(300);
		primaryStage.setResizable(false);
		primaryStage.show();
	}

	private void showAlert()
	{
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("��ʾ");
		alert.setHeaderText(null);
		alert.setContentText("�û������������!");

		alert.showAndWait();
	}
}
