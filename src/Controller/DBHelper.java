package Controller;
 
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
 
import Model.User;
 
public class DBHelper {
	private static Connection conn;                                      //����
	private PreparedStatement pres;                                      //PreparedStatement����
	
	static{
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");              //��������
			System.out.println("���ݿ���سɹ�!!!");
			String url="jdbc:mysql://localhost:3306/teamproject?serverTimezone=UTC&useUnicode=true&characterEncoding=utf-8";
			String user="root";
			String password="123456";
			
			conn=DriverManager.getConnection(url,user,password); //��������
			System.out.println("���ݿ����ӳɹ�!!!");
		} catch (ClassNotFoundException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/*
	 * �����ݿ��еı�userinfo�в���User����
	 */
	public void saveUser(User user){
		String sql="insert into userinfo(account, info) values(?,?)";
		try {
			pres=conn.prepareStatement(sql);
			pres.setString(1, user.getAccount());
			pres.setObject(2, user);
			pres.executeUpdate();                                      
			
			if(pres!=null)
				pres.close();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	//���¶���
	public void updateUser(User user) {
		String sql="update userinfo set info=? where account = ?";
		
		try {
			pres=conn.prepareStatement(sql);
			
			pres.setObject(1, user);
			pres.setString(2, user.getAccount());
			
			pres.executeUpdate();                                      
			
			if(pres!=null)
				pres.close();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/*
	 * �����ݿ��ж�������Ķ���
	 * return:
	 * 	User����
	 */
	public User getUser(String account){
		User user = new User();
		String sql="select info from userinfo where account = '" + account + "'";
		
		try {
			pres=conn.prepareStatement(sql);
			
			ResultSet res=pres.executeQuery();
			if(res.next()){//����˺Ŵ��ڵĻ�
				Blob inBlob=res.getBlob(1);                             //��ȡblob����
				
				InputStream is=inBlob.getBinaryStream();                //��ȡ������������
				BufferedInputStream bis=new BufferedInputStream(is);    //����������������
				
				byte[] buff=new byte[(int) inBlob.length()];
				while(-1!=(bis.read(buff, 0, buff.length))){            //һ����ȫ������buff��
					ObjectInputStream in=new ObjectInputStream(new ByteArrayInputStream(buff));
					user =(User)in.readObject();                   //��������
				}
				
			}
		} catch (SQLException | IOException | ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return user;
	}
}