/* Starter code for PERT algorithm (Project 4)
 * @author rbk
 */

// change to your netid
package yra230000;

// replace sxa173731 with your netid below
import yra230000.Graph;
import yra230000.Graph.Vertex;
import yra230000.Graph.Edge;
import yra230000.Graph.GraphAlgorithm;
import yra230000.Graph.Factory;

import java.io.File;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Scanner;

public class PERT extends GraphAlgorithm<PERT.PERTVertex> {
	public static void main(String[] args) throws Exception {
		String graph = "10 13   1 2 1   2 4 1   2 5 1   3 5 1   3 6 1   4 7 1   5 7 1   5 8 1   6 8 1   6 9 1   7 10 1   8 10 1   9 10 1      0 3 2 3 2 1 3 2 4 1";
		Scanner in;
		// If there is a command line argument, use it as file from which
		// input is read, otherwise use input from string.
		in = args.length > 0 ? new Scanner(new File(args[0])) : new Scanner(graph);
		Graph g = Graph.readDirectedGraph(in);
		g.printGraph(false);

		int[] duration = new int[g.size()];
		for(int i=0; i<g.size(); i++) {
		    duration[i] = in.nextInt();
		}
		PERT p = pert(g, duration);
		if(p == null) {
		    System.out.println("Invalid graph: not a DAG");
		} else {
		    System.out.println("Number of critical vertices: " + p.numCritical());
		    System.out.println("u\tEC\tLC\tSlack\tCritical");
		    for(Vertex u: g) {
			System.out.println(u + "\t" + p.ec(u) + "\t" + p.lc(u) + "\t" + p.slack(u) + "\t" + p.critical(u));
		    }
		}
	}
	
    LinkedList<Vertex> finishList;
    static final int INFINITY = Integer.MAX_VALUE;
    Vertex src;

    public static class PERTVertex implements Factory {
	// Add fields to represent attributes of vertices here
	boolean seen;
	Vertex parent;
	int distance;
	int es;
    int ef;
    int ls;
    int lf;
    int slack;
    int duration;
    String status;
    

	public PERTVertex(Vertex u) {
		seen = false;
		es = 0; 
		ef = 0;
		ls = 0;
		lf = 0;
		slack = 0;
		duration = 0;
	}
	public PERTVertex make(Vertex u) { return new PERTVertex(u); }
    }

    // Constructor for PERT is private. Create PERT instances with static method pert().
    private PERT(Graph g) {
	super(g, new PERTVertex(null));
	this.src = g.iterator().next();
    }
    public void setDuration(Vertex u, int d) {
    	get(u).duration = d;
    }

    // Implement the PERT algorithm. Returns false if the graph g is not a DAG.
    public boolean pert() {
    	if(isDAGALL() == false) {
    		return false;
    	}
        topologicalOrder();
    	
    	for(Vertex vert : g) {
    		get(vert).es =0;
    	}
    	
    	for(Vertex u : finishList) {
    		get(u).ef = get(u).es + get(u).duration;
        	for (Edge e : g.outEdges(u)) {
        		Vertex v = e.otherEnd(u);
                if (get(v).es < get(u).ef) {
                	get(v).es = get(u).ef;
                }
        	}          
        }
    	
    	
    	int CPL = 0;
        for (Vertex vert : g) {
            if (get(vert).ef > CPL) {
                CPL = get(vert).ef;
            }
        }
        
        for (Vertex vert : g) {
        	get(vert).lf = CPL;
        }
        LinkedList<Vertex> reversedFinishList = new LinkedList<>(finishList);
        Collections.reverse(reversedFinishList);

        for (Vertex u : reversedFinishList) {
        	get(u).ls = get(u).lf - get(u).duration;
        	get(u).slack = get(u).lf - get(u).ef;
            for (Edge edge : g.inEdges(u)) {
            	Vertex v = edge.otherEnd(u);
                if (get(v).lf > get(u).ls) {
                	get(v).lf = get(u).ls;
                }
            }	
        }
		return true;
    }
    	
    // Find a topological order of g using DFS
    LinkedList<Vertex> topologicalOrder() {
        finishList = new LinkedList<>();

        dfsAll();

        return finishList;
    }
    
    public void dfsAll() {
        for (Vertex v : g) {
            get(v).seen = false;
        }

        for (Vertex v : g) {
            if (!get(v).seen) {
                dfs(v);
            }
        }
    }
    
    private void dfs(Vertex v) {
        get(v).seen = true;
        for (Edge edge : g.outEdges(v)) {
            Vertex w = edge.otherEnd(v);
            if (!get(w).seen) {
                dfs(w);
            }
        }
        finishList.addFirst(v);
    }
    
    private boolean isDAGALL() {
    	for(Vertex vert : g) {
    		get(vert).status = "new";
    	}
    	for(Vertex vert : g) {
    		if(!isDAG(vert)) {
    			return false;
    		}
    	}
		return true;
    }
    
    private boolean isDAG(Vertex vert) {
    	get(vert).status = "active";
    	for(Edge edge : g.outEdges(vert)) {
    		Vertex otherVert = edge.otherEnd(vert);
    		if(get(otherVert).status.equals("active")) {
    			return false;
    		}
    		else if(get(otherVert).status.equals("new")) {
    			if(!isDAG(otherVert)) {
    				return false;
    			}
    		}
    	}
    	get(vert).status = "finished";
    	return true;
    }
    

    // The following methods are called after calling pert().

    // Earliest time at which task u can be completed
    public int ec(Vertex u) {
	return get(u).es + get(u).duration;
    }

    // Latest completion time of u
    public int lc(Vertex u) {
	return get(u).lf;
    }

    // Slack of u
    public int slack(Vertex u) {
	return get(u).slack;
    }

    // Length of a critical path (time taken to complete project)
    public int criticalPath() {
    int maxLength = 0;
    for (Vertex u : g) {
    	if (critical(u) && get(u).lf > maxLength) {
    		maxLength = get(u).lf;
        }
    }
     return maxLength;    
     }

    // Is u a critical vertex?
    public boolean critical(Vertex u) {
    return get(u).slack == 0;
    }

    // Number of critical vertices of g
    public int numCritical() {
    int count = 0;
    for (Vertex u : g) {
    	if (critical(u)) {
    		count++;
        }
    }
    return count;
    }

    /* Create a PERT instance on g, runs the algorithm.
     * Returns PERT instance if successful. Returns null if G is not a DAG.
     */
    public static PERT pert(Graph g, int[] duration) {
	PERT p = new PERT(g);
	for(Vertex u: g) {
	    p.setDuration(u, duration[u.getIndex()]);
	}
	// Run PERT algorithm.  Returns false if g is not a DAG
	if(p.pert()) {
	    return p;
	} else {
	    return null;
	}
    }
    
}
