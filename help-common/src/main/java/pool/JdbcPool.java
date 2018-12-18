package pool;

import javax.sql.DataSource;
import java.io.InputStream;
import java.io.PrintWriter;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.Properties;
import java.util.Vector;
import java.util.logging.Logger;

/**
 * Created by fitz.li on 2018/12/5.
 */
public class JdbcPool implements DataSource {

    private static Vector<Connection> connections = new Vector<>();

    static {
        InputStream in = JdbcPool.class.getClassLoader().getResourceAsStream("db.properties");
        Properties prop = new Properties();
        try {
            prop.load(in);
            String driver = prop.getProperty("driver");
            String url = prop.getProperty("url");
            String username = prop.getProperty("username");
            String password = prop.getProperty("password");
            int corePoolSize = Integer.parseInt(prop.getProperty("corePollSize"));
            int maxPoolSize = Integer.parseInt(prop.getProperty("maxPollSize"));
            Class.forName(driver);
            for (int i = 0; i < corePoolSize; i++) {
                Connection conn = DriverManager.getConnection(url, username, password);
                connections.addElement(conn);
            }
        } catch (Exception e) {
            System.out.println(" 创建数据库连接失败！ " + e.getMessage());
        }
    }

    @Override
    public Connection getConnection() throws SQLException {
        if(connections.size() > 0){
            Connection conn = connections.remove(0);
            return (Connection) Proxy.newProxyInstance(JdbcPool.class.getClassLoader(), conn.getClass().getInterfaces(), new InvocationHandler() {
                @Override
                public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                    if(!method.equals("close")){
                        return method.invoke(conn, args);
                    }else{
                        connections.addElement(conn);
                        System.out.println(conn + "被还给Connections数据库连接池了！！");
                        return null;
                    }

                }
            });
        }
        throw new RuntimeException("数据库忙");
    }

    @Override
    public Connection getConnection(String username, String password) throws SQLException {
        return null;
    }

    @Override
    public <T> T unwrap(Class<T> iface) throws SQLException {
        return null;
    }

    @Override
    public boolean isWrapperFor(Class<?> iface) throws SQLException {
        return false;
    }

    @Override
    public PrintWriter getLogWriter() throws SQLException {
        return null;
    }

    @Override
    public void setLogWriter(PrintWriter out) throws SQLException {

    }

    @Override
    public void setLoginTimeout(int seconds) throws SQLException {

    }

    @Override
    public int getLoginTimeout() throws SQLException {
        return 0;
    }

    @Override
    public Logger getParentLogger() throws SQLFeatureNotSupportedException {
        return null;
    }
}
