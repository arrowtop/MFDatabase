/*************************************************************************************************
 * CS 562 Project 
 * Author: Yujie Du(10372723), Chuanhui Zhang(10387654)
 * 
 * Graph is the class to store all the vertices and return the start points and edges to the
 * ToplogicalSortWithUpdate.java
 * 
 *************************************************************************************************/

package imagineDragon;

import java.util.HashSet;

public class Graph
{
	private final HashSet<Integer>[] adj; //adj[i] means all the vertices that depends on i
	private HashSet<Integer> end; //add all vertices which appear on the w side
	private HashSet<Integer> allSet; //add all vertices which appear on the v side
	public Graph(int V) {
		adj = (HashSet<Integer>[]) new HashSet[V];
		end = new HashSet<Integer>();
		allSet = new HashSet<Integer>();
		for(int v = 0; v < V; v++){
			adj[v] = new HashSet<Integer>();
			allSet.add(v);
		}
	}
	public void addEdge(int v, int w){
		end.add(w);
		adj[v].add(w);
	}
	public HashSet<Integer> adj(int v){ //return the HashSet of all the vertices that depends on v
		return adj[v];
	}
	public HashSet<Integer> start(){ //Vertices that only appears on the v side
		HashSet<Integer> start = new HashSet<Integer>(allSet);
		start.removeAll(end); //remove all vertices of end from allSet
		return start;
	}
}
