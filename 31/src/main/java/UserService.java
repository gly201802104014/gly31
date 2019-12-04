import java.sql.SQLException;

public final class UserService {
	private UserDao userDao = UserDao.getInstance();
	private static UserService userService = new UserService();

	public UserService() {}

	public static UserService getInstance(){
		return UserService.userService;
	}

	public User find(Integer id) throws SQLException {
		return userDao.find(id);
	}
	public User findByUserName(String userName) throws SQLException {
		return userDao.findByUserName(userName);
	}

	public User login(String username,String password) throws SQLException {
		return userDao.login(username,password);
	}
	public boolean changePassword(User user) throws SQLException {
		return userDao.changePassword(user);
	}
}
