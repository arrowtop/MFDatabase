/*************************************************************************************************
 * CS 562 Project 
 * Author: Yujie Du(10372723), Chuanhui Zhang(10387654)
 * 
 * Pair class is the class to assist the dependent information passed from the MFStructInfo.java
 * to the TopologicalSortWithUpdate.java
 * 
 *************************************************************************************************/

package imagineDragon;

//Pair(a, b) means b depends on a (a<-b)
public class Pair{
	private int first;
	private int second;
	
	public Pair(int first, int second) {
		this.first = first;
		this.second = second;
	}
	
	public int getFirst() {
		return first;
	}
	
	public int getSecond() {
		return second;
	}
}
