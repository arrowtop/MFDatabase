/*************************************************************************************************
 * CS 562 Project 
 * Author: Yujie Du(10372723), Chuanhui Zhang(10387654)
 * 
 * MFStructInfo is the class to generate information for MFStructGenerator and MFMainGenerator
 * 
 *************************************************************************************************/

package imagineDragon;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Stack;
import java.util.TreeMap;

public class MFStructInfo {
	private static SchemaInfo si; //instance of SchemaInfo
	
	private static ArrayList<String> selAttrList;   //list of select attributes
	private static int numOfGv;  // number of grouping variable
	private static ArrayList<String> gaList;  //list of group attributes
	private static ArrayList<String> afList;  //list of sets of aggregation functions
	private static ArrayList<String> condList; //list of conditions for each grouping variable (such that)
	private static Map<String, String> gaNameToTypeMap;  //Map<ga, type> of grouping attributes for MFStruct.java output
	private static Map<String, String> afNameToTypeMap;  //Map<af, type> of aggregation functions for MFStruct.java output
	private static Map<String, HashMap<String, Stack<String>>> afCoreList;  //Map<loopNo, HashMap<aggregate, attributesInTable>> of aggregation functions for MFMainGenerator.java
	private static Map<String, Boolean> selAlignMap;  //Map<selAttr, false-left/true-right> of select attributes for MFMainGenerator.java
	private static Map<String, ArrayList<String>> selAttrMap;  //Map<selAttr, parsedSelAttrs> of select attributes for MFMainGenerator.java
	private static HashSet<String> numTypeSet; //identifying whether a type is numeric type or not
	private static Map<Integer, ArrayList<String>> condMap; //Map<loopNo, list of af> for MFMainGenerator.java to identify if condition involving dependencies is valid
	private static ArrayList<Pair> condDepPairList; //Pairs of condition dependency for TopologicalSortWithUpdate.java
	public MFStructInfo(ArrayList<String> selAttrList, int numOfGv, ArrayList<String> gaList, ArrayList<String> afList, ArrayList<String> condList){
		MFStructInfo.si = new SchemaInfo();
		
		MFStructInfo.selAttrList = selAttrList;
		MFStructInfo.numOfGv = numOfGv;
		MFStructInfo.gaList = gaList;
		MFStructInfo.afList = afList;
		MFStructInfo.condList = condList;
		MFStructInfo.gaNameToTypeMap = new HashMap<String, String>();
		MFStructInfo.afNameToTypeMap = new HashMap<String, String>();
		MFStructInfo.afCoreList = new HashMap<String, HashMap<String, Stack<String>>>();
		MFStructInfo.selAlignMap = new TreeMap<String, Boolean>();
		MFStructInfo.numTypeSet = new HashSet<String>();
		MFStructInfo.selAttrMap = new TreeMap<String, ArrayList<String>>();
		MFStructInfo.condMap = new HashMap<Integer, ArrayList<String>>();	
		MFStructInfo.condDepPairList = new ArrayList<Pair>();	
		
		//initialize numTypeSet
		numTypeSet.add("int");
		numTypeSet.add("double");
		numTypeSet.add("float");
		
		analyzeGa();
		analyzeAf();
		analyzeCond();
		analyzeSelAttr();	
	}
	
	//method for analyzing select attributes
	private void analyzeSelAttr(){
		for(int i = 0; i < selAttrList.size(); i++){
			String[] temp = selAttrList.get(i).split("\\+|\\-|\\*|\\/");
			ArrayList<String> selectList = new ArrayList<String>();
			if(!afNameToTypeMap.containsKey(temp[0])) {
				selectList.add(selAttrList.get(i));
				selAlignMap.put(selAttrList.get(i), numTypeSet.contains(gaNameToTypeMap.get(selAttrList.get(i))));
			} else {
				boolean first = true;;
				for(int j = 0; j < temp.length; j++) {
					if(temp[j].equals("")) continue;
					if(first == true) {
						selAlignMap.put(selAttrList.get(i), numTypeSet.contains(afNameToTypeMap.get(temp[j])));
						first = false;
					}
					selectList.add(temp[j]);
				}
			}
			selAttrMap.put(selAttrList.get(i), selectList);
		}
	}
	
	//method for analyzing group attributes
	private void analyzeGa() {  
		for(String s: gaList){
			String attrType = si.getValue(s);
			gaNameToTypeMap.put(s, attrType);
		}
	}
	
	//method for analyzing aggragate functions
	private void analyzeAf() {
		for(String s: afList){
			if(!s.equals("")){
				String[] fv1 = s.split("_");
				assert fv1.length == 3;
				String aggrType = si.getValue(fv1[2]);
				if(fv1[0].equals("avg")){
					afNameToTypeMap.put("sum_"+ fv1[1] + "_" + fv1[2], aggrType);
					afNameToTypeMap.put("count_"+ fv1[1] + "_" + fv1[2], "int");
				}
				if(fv1[0].equals("count")) afNameToTypeMap.put(s, "int");
				else afNameToTypeMap.put(s, aggrType);
				HashMap<String, Stack<String>> map = new HashMap<String, Stack<String>>();
				if(afCoreList.containsKey(fv1[1])){
					map = afCoreList.get(fv1[1]);
				}
				Stack<String> stack1 = new Stack<String>();
				Stack<String> stack2 = new Stack<String>();
				if(fv1[0].equals("avg")){
					if(map.containsKey("sum")){
						stack1 = map.get("sum");
					}
					if(map.containsKey("count")){
						stack2 = map.get("count");
					}
					if(!stack1.contains(fv1[2])) stack1.push(fv1[2]);
					if(!stack2.contains(fv1[2])) stack2.push(fv1[2]);
					map.put("sum", stack1);
					map.put("count", stack2);
				}else{
					if(map.containsKey(fv1[0])){
						stack1 = map.get(fv1[0]);
					}
					if(!stack1.contains(fv1[2])) stack1.push(fv1[2]);	
					map.put(fv1[0], stack1);
				}
				afCoreList.put(fv1[1], map);
			}
			
		}
	}
	
	//method for analyze conditions
	public void analyzeCond()
	{
		for(int i = 0; i < condList.size(); i++) {
			String cond = condList.get(i);
			for(Map.Entry<String, String> entry: afNameToTypeMap.entrySet()) {
				if(cond.contains(entry.getKey())) {
					ArrayList<String> condList;
					if(!condMap.containsKey(i)) condList = new ArrayList<String>();
					else condList = condMap.get(i);
					condList.add(entry.getKey());
					condMap.put(i, condList);
					int j = 0;
					for(; j < numOfGv; j++) {
						if(entry.getKey().contains(j+"")) break;
					}
					condDepPairList.add(new Pair(j , i));
				}
			}
		}
	}
	
	public ArrayList<Pair> getCondDepPairList() {	
		return condDepPairList;
	}
	
	public Map<Integer, ArrayList<String>> getCondMap() {
		return condMap;
	}
	
	public Map<String, String> getSchemaInfo(){
		return si.getSchemaInfo();
	}
	
	public Map<String, String> getGaNameToTypeMap(){
		return gaNameToTypeMap;
	}
	
	public Map<String, String> getAfNameToTypeMap(){
		return afNameToTypeMap;
	}
	
	public ArrayList<String> getGaList(){
		return gaList;
	}
	
	public Map<String, HashMap<String, Stack<String>>> getAfCoreList(){
		return afCoreList;
	}
	
	public int getNumOfGv(){
		return numOfGv;
	}
	
	public ArrayList<String> getCondList(){
		return condList;
	}
	
	public ArrayList<String> getSelAttrList(){
		return selAttrList;
	}
	
	public Map<String, Boolean> getSelAlignMap(){
		return selAlignMap;
	}
	
	public Map<String, ArrayList<String>> getSelectMap() {
		return selAttrMap;
	}
}
