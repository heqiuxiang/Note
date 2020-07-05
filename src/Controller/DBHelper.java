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
	private static Connection conn;                                      //连接
	private PreparedStatement pres;                                      //PreparedStatement对象
	
	static{
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");              //加载驱动
			System.out.println("数据库加载成功!!!");
			String url="jdbc:mysql://localhost:3306/teamproject?serverTimezone=UTC&useUnicode=true&characterEncoding=utf-8";
			String user="root";
			String password="123456";
			
			conn=DriverManager.getConnection(url,user,password); //建立连接
			System.out.println("数据库连接成功!!!");
		} catch (ClassNotFoundException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/*
	 * 向数据库中的表userinfo中插入User对象
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
	
	//更新对象
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
	 * 从数据库中读出存入的对象
	 * return:
	 * 	User对象
	 */
	public User getUser(String account){
		User user = new User();
		String sql="select info from userinfo where account = '" + account + "'";
		
		try {
			pres=conn.prepareStatement(sql);
			
			ResultSet res=pres.executeQuery();
			if(res.next()){//如果账号存在的话
				Blob inBlob=res.getBlob(1);                             //获取blob对象
				
				InputStream is=inBlob.getBinaryStream();                //获取二进制流对象
				BufferedInputStream bis=new BufferedInputStream(is);    //带缓冲区的流对象
				
				byte[] buff=new byte[(int) inBlob.length()];
				while(-1!=(bis.read(buff, 0, buff.length))){            //一次性全部读到buff中
					ObjectInputStream in=new ObjectInputStream(new ByteArrayInputStream(buff));
					user =(User)in.readObject();                   //读出对象
				}
				
			}
		} catch (SQLException | IOException | ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return user;
	}
}