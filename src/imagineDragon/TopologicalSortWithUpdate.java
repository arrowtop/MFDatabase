/*************************************************************************************************
 * CS 562 Project 
 * Author: Yujie Du(10372723), Chuanhui Zhang(10387654)
 * 
 * TopologicalSortWithUpdate is the class to calculate the minimum loop for 
 * the MFMainMinLoopGenerator.java
 * 
 * Suppose we start from the startPoint 3 and get 3(0)<-4(1). If the next startPoint is 0 and
 * get 0(0) <- 1(1) <- 2(2) <- 3(3). Thus 3 should be updated to 3 and 4 should be updated to 4.
 * Thus the schedule is 0(0) <- 1(1) <- 2(2) <- 3(3) <- 4(4). This algorithm is based on the
 * topological sort.
 * 
 *************************************************************************************************/

package imagineDragon;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Stack;

public class TopologicalSortWithUpdate {	

	private static Graph g; //gragh instance
	private static int[] step; // the ith loop appears on the step step[i] in the schedule
	private static int num; //max vertice value
	private static boolean[] marked; //check if the vertice is seen
	private static Map<Integer, HashSet<Integer>> schedule; //Map<step, list of LoopNo> -> This is the final result
	private static int start = 0;
	
	public TopologicalSortWithUpdate(MFStructInfo mfsi, int number, boolean isZeroPresent) //Constructor
	{
		if(isZeroPresent == false) this.start = 1;
		num = number;
		g = new Graph(num);
		ArrayList<Pair> condDepPairList = mfsi.getCondDepPairList();
		for(int i = 0; i < condDepPairList.size(); i++){ //initialize the edges
			g.addEdge(condDepPairList.get(i).getFirst(), condDepPairList.get(i).getSecond());
		}
		marked = new boolean[num];
		step = new int[num];
		schedule = new HashMap<Integer, HashSet<Integer>>();
	}
	
	public static void run(){ //This is the execute method for this class
		depthFirstOrder(g);
		
		for(int i = start; i < num; i++){
			HashSet<Integer> set;
			if(!schedule.containsKey(step[i])){
				set = new HashSet<Integer>();
			} else {
				set = schedule.get(step[i]);

			}
			set.add(i);
			schedule.put(step[i], set);
		}
		
	}
	private static void depthFirstOrder(Graph g){
		for(int v : g.start()) {//start from the vertices only on the left side
			if(!marked[v]) dfs(g, v, 0); }
		for(int v = 0; v < num; v++) // find if there are vertices which has not been seen. Add them on the list
			if(!marked[v]) dfs(g, v, 0);
	}
	
	private static void dfs(Graph g, int v, int count){ //Depth Search First With Updates
		marked[v] = true;
		for(int w : g.adj(v)){
			if(!marked[w]){
				dfs(g, w, count+1);
			} else if(step[w] < count+1){
				step[w] = count+1;
				update(g, g.adj(w), count+2);
			} 
		}
		if(step[v] < count) //update step value for v
			step[v] = count;
	}
	
	//Update the vertices which appears on the hs or depends on the vertices on the hs
	private static void update(Graph g, HashSet<Integer> hs, int value){ 
		for(int i : hs){
			if(step[i] < value) step[i] = value;
			update(g, g.adj(i), ++value);		
		}
	}
	
	public static Map<Integer, HashSet<Integer>> getSchedule() {
		return schedule;
	}
	
}