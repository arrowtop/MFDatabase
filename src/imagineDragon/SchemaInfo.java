/*************************************************************************************************
 * CS 562 Project 
 * Author: Yujie Du(10372723), Chuanhui Zhang(10387654)
 * 
 * SchemaInfo is the class to store the schema of a selected table_name. 
 * 
 *************************************************************************************************/


package imagineDragon;

import java.util.HashMap;
import java.util.Map;

public class SchemaInfo {
	private static String table_name;
	private static Map<String, String> typeMap; //HashMap<Attributes of table, dataType> of table_name
	
	//convert origType to changeType
	private static final String[] origType = {"int", "float", "double", "char"};
	private static final String[] changeType = {"int", "float", "double", "String"};
	
	
	public SchemaInfo(String name) { //Constructor using table_name
		table_name = name;
		typeMap = new HashMap<String, String>();
	}
	
	public SchemaInfo() { //Contructor using preset value
		schemaInit();	
	}

	
	public String getTableName(){
		if(table_name == null) schemaInit();
		return table_name;
	}
	
	public void addAttribute(String col, String type) {
		typeMap.put(col, updateType(type));
	}
	
	public String getValue(String attr){
		if(table_name == null) schemaInit();
		return typeMap.get(attr);
	}
	
	public void getSchema() {
		if(typeMap.size() == 0) {
			System.out.printf("No such table!");
		} else {
			System.out.printf("%-8s","Column        ");             //left aligned
			System.out.printf("%-7s","Type   \n");              //left aligned
			for (Map.Entry<String, String> entry : typeMap.entrySet()) {
				String key = entry.getKey();
				String value = entry.getValue();
				System.out.printf("%-8s      ", key);             //left aligned
				System.out.printf("%-7s   \n", value);              //left aligned
			}
        }
	}
	
	//init with preset data
	private void schemaInit(){
		table_name = "sales";
		typeMap = new HashMap<String, String>();
		typeMap.put("cust", "String");
		typeMap.put("prod", "String");
		typeMap.put("day", "int");
		typeMap.put("month", "int");
		typeMap.put("year", "int");
		typeMap.put("state", "String");
		typeMap.put("quant", "int");
	}
	
	public Map<String, String> getSchemaInfo(){ //get schema's typeMap
		if(table_name == null) schemaInit();
		return typeMap;
	}

	//update types of each attributes so that only int, double, float and String is used
	private String updateType(String type){ 
		for(int i = 0; i < origType.length; i++){
			if(type.toLowerCase().contains(origType[i]))
				return changeType[i];
		}
		return "string";	
	}

}
