package application;

import java.io.IOException;

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
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class Register extends Application{
	@Override
	public void start(Stage primaryStage) throws Exception {
		Label lblName = new Label("�û�");
		Label lblPassword = new Label("����");
		Label lblRePassword = new Label("�ٴ���������");
		
		TextField txtName = new TextField();
		PasswordField pwdPassword = new PasswordField();
		PasswordField pwdRePassword = new PasswordField();
		
		Button btnRegister = new Button("ע��");
		Button btnCancel = new Button("ȡ��");
		Button btnClear = new Button("���");
		
		GridPane gr = new GridPane();
		
		gr.add(lblName, 0, 0);
		gr.add(txtName, 1, 0);
		gr.add(lblPassword, 0, 1);
		gr.add(pwdPassword, 1, 1);
		gr.add(lblRePassword, 0, 2);
		gr.add(pwdRePassword, 1, 2);
		
		gr.add(btnRegister, 0, 3);
		gr.add(btnCancel, 1, 3);
		gr.add(btnClear, 2, 3);
		
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
		
		btnRegister.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				String username = txtName.getText();
				String password = pwdPassword.getText();
				String rePassword = pwdRePassword.getText();
				String userPath = ".\\outputFile\\" + username + ".user";
				
				if(username.equals("")||password.equals("")||rePassword.equals("")) {
					//do nothing
				}
				else if(!password.equals(rePassword)) {
					Alert alert = new Alert(AlertType.INFORMATION);
					alert.setTitle("��ʾ");
					alert.setHeaderText(null);
					alert.setContentText("�����������벻ͬ!");
					 
					alert.showAndWait();
					
					pwdPassword.setText("");
					pwdRePassword.setText("");
				}
				else {
					User user = new User();
					user.setAccount(username);
					user.setPassword(password);
					user.initialize();
					
					//�������
					DBHelper db= new DBHelper();
					db.saveUser(user);
					//IOOperator.serialize(userPath, user);
					
					Alert alert = new Alert(AlertType.INFORMATION);
					alert.setTitle("��ʾ");
					alert.setHeaderText(null);
					alert.setContentText("ע��ɹ�!");
					 
					alert.showAndWait();
					//�ص���¼����
					Platform.runLater(() -> {
						try {
							new Login().start(new Stage());
						} catch (Exception e) {
						e.printStackTrace();
						}
						primaryStage.close();
					});
				}
			}
			
		});
		
		btnCancel.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) {
				//�ص���¼����
				Platform.runLater(() -> {
					try {
						new Login().start(new Stage());
					} catch (Exception e) {
					e.printStackTrace();
					}
					primaryStage.close();
				});
				
			}
		});
		
		btnClear.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				//��������ı��������
				txtName.setText("");
				pwdPassword.setText("");
				pwdRePassword.setText("");
			}
			
		});
		
	}
}
