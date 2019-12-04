import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public final class UserDao {
	private static UserDao userDao=new UserDao();
	private UserDao(){}
	public static UserDao getInstance(){
		return userDao;
	}
	//根据id值查找
	public User find(Integer id) throws SQLException{
		User user = null;
		Connection connection = JdbcHelper.getConn();
		String selectUser_sql = "SELECT * FROM user WHERE id=?";
		//在该连接上创建预编译语句对象
		PreparedStatement preparedStatement = connection.prepareStatement(selectUser_sql);
		//为预编译参数赋值
		preparedStatement.setInt(1,id);
		ResultSet resultSet = preparedStatement.executeQuery();
		//由于id不能取重复值，故结果集中最多有一条记录
		//若结果集有一条记录，则以当前记录中的id,username,password,teacher值为参数，创建Teacher对象
		//若结果集中没有记录，则本方法返回null
		if (resultSet.next()){
			Teacher teacher = TeacherDao.getInstance().find(resultSet.getInt("teacher_id"));
			user = new User(resultSet.getInt("id"),
					resultSet.getString("username"),
					resultSet.getString("password"),
					resultSet.getDate("loginTime"),
					teacher);
		}
		//关闭资源
		JdbcHelper.close(resultSet,preparedStatement,connection);
		return user;
	}
	//根据username查找
	public User findByUserName(String userName) throws SQLException {
		User user = null;
		Connection connection = JdbcHelper.getConn();
		String selectUser_sql = "SELECT * FROM user WHERE username=?";
		//在该连接上创建预编译语句对象
		PreparedStatement preparedStatement = connection.prepareStatement(selectUser_sql);
		//为预编译参数赋值
		preparedStatement.setString(1,userName);
		ResultSet resultSet = preparedStatement.executeQuery();
		//由于id不能取重复值，故结果集中最多有一条记录
		//若结果集有一条记录，则以当前记录中的id,username,password,teacher值为参数，创建Teacher对象
		//若结果集中没有记录，则本方法返回null
		if (resultSet.next()){
			Teacher teacher = TeacherDao.getInstance().find(resultSet.getInt("teacher_id"));
			user = new User(resultSet.getInt("id"),
					resultSet.getString("username"),
					resultSet.getString("password"),
					resultSet.getDate("loginTime"),
					teacher);
		}
		//关闭资源
		JdbcHelper.close(resultSet,preparedStatement,connection);
		return user;
	}
	//登录
	public User login(String username,String password) throws SQLException {
		//根据用户名查找对应的user
		User user = userDao.findByUserName(username);
		//声明变量desiredUser
		User desiredUser = null;
		//判断前台传过来的user及密码是否与查找结果一致
		if(username.equals(user.getUsername()) && password.equals(user.getPassword())){
			//一致返回user对象
			desiredUser = user;
		}
		return desiredUser;
	}
	//改密码
	public boolean changePassword(User user) throws SQLException {
		Connection connection = JdbcHelper.getConn();
		//写sql语句
		String changeUserPassword_sql = "UPDATE user SET password=? WHERE id=?";
		//在该连接上创建预编译语句对象
		PreparedStatement preparedStatement = connection.prepareStatement(changeUserPassword_sql);
		//为预编译参数赋值
		preparedStatement.setString(1,user.getPassword());
		preparedStatement.setInt(2,user.getId());
		//执行预编译语句，获取改变记录行数并赋值给affectedRowNum
		int affectedRows = preparedStatement.executeUpdate();
		//关闭资源
		JdbcHelper.close(preparedStatement,connection);
		return affectedRows>0;
	}
}
