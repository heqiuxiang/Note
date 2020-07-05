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
		Label lblName = new Label("用户");
		Label lblPassword = new Label("密码");
		Label lblRePassword = new Label("再次输入密码");
		
		TextField txtName = new TextField();
		PasswordField pwdPassword = new PasswordField();
		PasswordField pwdRePassword = new PasswordField();
		
		Button btnRegister = new Button("注册");
		Button btnCancel = new Button("取消");
		Button btnClear = new Button("清除");
		
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
					alert.setTitle("提示");
					alert.setHeaderText(null);
					alert.setContentText("两次密码输入不同!");
					 
					alert.showAndWait();
					
					pwdPassword.setText("");
					pwdRePassword.setText("");
				}
				else {
					User user = new User();
					user.setAccount(username);
					user.setPassword(password);
					user.initialize();
					
					//存入对象
					DBHelper db= new DBHelper();
					db.saveUser(user);
					//IOOperator.serialize(userPath, user);
					
					Alert alert = new Alert(AlertType.INFORMATION);
					alert.setTitle("提示");
					alert.setHeaderText(null);
					alert.setContentText("注册成功!");
					 
					alert.showAndWait();
					//回到登录窗口
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
				//回到登录窗口
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
				//清空所有文本框密码框
				txtName.setText("");
				pwdPassword.setText("");
				pwdRePassword.setText("");
			}
			
		});
		
	}
}
