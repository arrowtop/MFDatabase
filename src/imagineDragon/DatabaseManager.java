/*************************************************************************************************
 * CS 562 Project 
 * Author: Yujie Du(10372723), Chuanhui Zhang(10387654)
 * 
 * DatabaseManager is the class to manage all Postgres Database Operations
 * 
 *************************************************************************************************/

package imagineDragon;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.sql.*;

public class DatabaseManager {
	private static String usr; //username for postgres database
    private static String pwd; //password for postgres database
    private static String url; //url for postgres database
    
    public DatabaseManager () { // use the default setting of
    	usr ="toby";
        pwd ="12345678";
        url ="jdbc:postgresql://localhost:5432/mydb";
    }
    
    public DatabaseManager (String url, String usr, String pwd) { // use the user input setting of
    	DatabaseManager.url = url;
    	DatabaseManager.usr = usr;
    	DatabaseManager.pwd = pwd;
    }
    
    public String connect() {     //Connect to the Postgresql Database
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		PrintStream ps = new PrintStream(baos);
		PrintStream old = System.out;
	    // Tell Java to use the special stream
	    System.setOut(ps);
    	try {
    		Class.forName("org.postgresql.Driver");
    	} catch(Exception e) {
    		System.out.println("Failing loading Driver!");
    		e.printStackTrace();
    		System.setOut(old);
    		return baos.toString();
    	}
    	try{
    		Connection conn = DriverManager.getConnection(url, usr, pwd);
    		System.out.println("Success connecting server!");
    		conn.close();
    	} catch(Exception e) {
    		System.out.println("Connection errors!");
    		e.printStackTrace();
    	}
	    System.out.flush();    
	    System.setOut(old);
		return baos.toString();
    }
    
    public String retrieve(String tableName, String schema){      //Retrive the schema from the Postgresql Database
    	ByteArrayOutputStream baos = new ByteArrayOutputStream();
		PrintStream ps = new PrintStream(baos);
		PrintStream old = System.out;
	    // Tell Java to use the special stream
	    System.setOut(ps);
	    Boolean isSchemaProvided= true;
	    if(schema.replaceAll(" ", "").equals("")) {
	    	//means no schema is provided
	    	isSchemaProvided = false;
	    }
    	try{
    		Connection conn = DriverManager.getConnection(url, usr, pwd);
    		Statement st = conn.createStatement();
			SchemaInfo si = new SchemaInfo(tableName);
    		if(isSchemaProvided) {
    			st.executeUpdate("drop table if exists " + tableName + ";");
    			st.executeUpdate(schema);
    		}
        	String SQLQuery = "select column_name, data_type from information_schema.columns\n where table_name = '" + tableName + "'";  		
    		ResultSet rs = st.executeQuery(SQLQuery);
    		while(rs.next())
			{
				String col = rs.getString("column_name");
				String type = rs.getString("data_type");			
				si.addAttribute(col, type);
			}   		
    		si.getSchema();   	   
    	    st.close();
    	    conn.close();
    	} catch(SQLException e) {
    		if(isSchemaProvided) System.out.println("Schema invalid");
    		else System.out.println("No such table!");
            e.printStackTrace();
        }
    	
	    System.out.flush();    
	    System.setOut(old);
		return baos.toString();
    }
    
    public String importData(String SQLQuery){
    	ByteArrayOutputStream baos = new ByteArrayOutputStream();
		PrintStream ps = new PrintStream(baos);
		PrintStream old = System.out;
	    // Tell Java to use the special stream
	    System.setOut(ps);
	    try{
    		Connection conn = DriverManager.getConnection(url, usr, pwd);
    		Statement st = conn.createStatement();
    		st.executeUpdate(SQLQuery);
            System.out.println("Import Success!");

	    } catch(SQLException e) {
            System.out.println("Import Error!");
            e.printStackTrace();
        }
	    System.out.flush();    
	    System.setOut(old);
		return baos.toString();
    }
    
    public String getUsr(){
    	return usr;
    }
    
    public String getPwd(){
    	return pwd;
    }
    
    public String getUrl(){
    	return url;
    }

}
