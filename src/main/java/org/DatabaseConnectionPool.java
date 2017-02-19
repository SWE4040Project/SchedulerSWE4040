package org;

import java.sql.Connection;
import java.sql.SQLException;

import org.apache.tomcat.jdbc.pool.DataSource;
import org.apache.tomcat.jdbc.pool.PoolProperties;

public class DatabaseConnectionPool {
	
	private static DatabaseConnectionPool pool;
	private static PoolProperties p;
	private static DataSource datasource;
	
	/**
	 * Create private constructor
	 */
	private DatabaseConnectionPool(){
		p = new PoolProperties();
	    p.setUrl("jdbc:oracle:thin:@"+DBVar.DEV_URL+":"+ DBVar.DEV_PORT+":"+DBVar.DEV_SID);
	    p.setDriverClassName("oracle.jdbc.driver.OracleDriver");
	    p.setUsername(DBVar.DEV_USERNAME);
	    p.setPassword(DBVar.DEV_PASSWORD);
	    p.setJmxEnabled(true);
	    p.setTestWhileIdle(false);
	    p.setTestOnBorrow(true);
	    p.setValidationQuery("select 1 from companies where rownum = 1");
	    p.setTestOnReturn(false);
	    p.setValidationInterval(20000);
	    p.setTimeBetweenEvictionRunsMillis(30000);
	    p.setMaxActive(100);
	    p.setInitialSize(10);
	    p.setMaxWait(10000);
	    p.setRemoveAbandonedTimeout(60);
	    p.setMinEvictableIdleTimeMillis(30000);
	    p.setMinIdle(2);
	    p.setLogAbandoned(false);
	    p.setRemoveAbandoned(true);
	    p.setJdbcInterceptors(
	      "org.apache.tomcat.jdbc.pool.interceptor.ConnectionState;"+
	      "org.apache.tomcat.jdbc.pool.interceptor.StatementFinalizer");
	    datasource = new DataSource();
	    datasource.setPoolProperties(p);
	}
	/**
	 * Create a static method to get instance.
	 */
	public static DatabaseConnectionPool getInstance(){
	    if(pool == null){
	        pool = new DatabaseConnectionPool();
	    }
	    return pool;
	}
	
	public Connection getConnection() throws SQLException{
		return datasource.getConnection();
	}
	public int getActiveConnectionCount(){
		return datasource.getActive();
	}
}
