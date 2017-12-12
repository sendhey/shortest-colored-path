import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class Solution {

	public static void main(String[] args) {

		
		try{
			BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
			String temp = br.readLine();
			String[] tempParts= temp.split(" ");
			int number = Integer.parseInt(tempParts[0]);
			
			Graph newGraph = new Graph((2*number)+1);
			while((temp = br.readLine()) != null){
				tempParts= temp.split(" ");
				int one = Integer.parseInt(tempParts[0]);
				int two = Integer.parseInt(tempParts[1]);
				int three = Integer.parseInt(tempParts[2]);
				//System.out.println(a + " : " + b + " : " + c);
				if (one != two){
				//	System.out.println("adding " + a + b + c + " and " + (a+num) + (b+num) + c);
					
					newGraph.insertEdge(one, two, three);
					int oneSum = one + number;
					int twoSum = two + number;
					int threeSum = three;
					newGraph.insertEdge(oneSum, twoSum, threeSum);
				}
			}
			br.close();
			int iteration = iterate(newGraph);
			System.out.println(iteration);
		}catch(IOException e){
			e.printStackTrace();
		}
	}
	
	static class Vertex{
		ArrayList<Vertex> successors = new ArrayList<Vertex>();
		ArrayList<Integer> edges = new ArrayList<Integer>();
		public int val;
		public int dist;
		public int position;
		public Vertex(int val){
			//System.out.println("new vertex added with value: " + val);
			this.dist = Integer.MAX_VALUE;
			this.val = val;
		}
	}
	static class Graph{
		public int length;
		public Vertex[] vertexList;
		public boolean[] vertexes;
		
		public Graph(int length){
			this.vertexList = new Vertex[length+1];
			this.vertexes = new boolean[length+1];
			this.length = length;
			
			
		}
		public void insertEdge(int zero, int one, int two){
			Vertex vertexZero = null;
			Vertex vertexOne = null;

			if (vertexes[one] == true){
				
				vertexOne = vertexList[one];
			}
			
			//else if(vertexes[zero] == false){
			if (vertexes[zero] == true){
				vertexZero = vertexList[zero];
			}
			
			if(vertexes[zero] == false){
				
				vertexZero = new Vertex(zero);
				vertexList[zero] = vertexZero;
				vertexes[zero] = true;
				
			}

			if(vertexes[one] == false){
				
				vertexOne = new Vertex(one);
				vertexList[one] = vertexOne;
				vertexes[one] = true;
			}
			//else if(vertexes[one] == false){

			//System.out.println("adding " + one + " to " + zero);
			vertexZero.successors.add(vertexOne);
			vertexZero.edges.add(two-1);
		}
	}
	static int Djik(Graph graph, Vertex vertex){
		int numPaths = 0;
		vertex.dist = 0;
		Heap heap = new Heap(graph.length+1);
		//PriorityQueue<Vertex> heap = new PriorityQueue<Vertex>(graph.length+1);
		//System.out.println("HEAP SIZE IS " + heap.length);
		heap.add(vertex);
		
		//System.out.println("1. HEAP SIZE IS NOW: " + heap.length);
		while(heap.length > 0){
			//System.out.println("2. HEAP SIZE IS NOW: " + heap.length);
			Vertex vertA = heap.pop();
			//System.out.println("heap min dist is " + vertA.dist);
			//System.out.println("heap min val is " + vertA.val);
			//System.out.println("successors: " + vertex.successors.size());
			for (int i = 0; i < vertA.successors.size(); i++){
				//System.out.println(vertA.successors == null);
				//System.out.println(vertA.successors.get(0));
				//System.out.println("printing i: " + i);
				//System.out.println(vertA.successors.get(i) == null);
				Vertex vertB = vertA.successors.get(i);
				//System.out.println("GOT HERE");
				int edgeWeight = vertA.edges.get(i);
				//System.out.println("edgeWeight is " + edgeWeight);
				if((vertA.dist + edgeWeight) < vertB.dist){
					
					if(vertB.dist == Integer.MAX_VALUE){
						//System.out.println("Adding b to the heap");
						heap.add(vertB);
					}
					vertB.dist = vertA.dist + edgeWeight;
				}
				if(edgeWeight == 0){
					//System.out.println("Incrementing p: " + numPaths);
					numPaths++;
				}
			}
		}
		return numPaths;
		
	}
	static int iterate(Graph graph){
		
		//System.out.println("Got to iterate");
		for(int i = 1; i <= ((graph.length-1)/2); i++){
			//System.out.println("i = " + i);
			Vertex a = graph.vertexList[i];
			int size = a.successors.size();
			for(int j = 0; j < size; j++){
				Vertex b = a.successors.get(j);
				int edgeWeight = a.edges.get(j);
				if(edgeWeight == 0){
					graph.insertEdge(a.val, (b.val+((graph.length-1)/2)), edgeWeight);
				}
			}
		}
		//System.out.println("got to end of iterate");
		//System.out.println(graph.vertexList[1].val);
		int dj = Djik(graph,graph.vertexList[1]);
		//System.out.println("finished d");
		if(graph.vertexList[((graph.length-1)/2)*2].dist == Integer.MAX_VALUE || dj == 0){
		//	System.out.println("returning -1");
			return -1;
		}
		//System.out.println("returning else");
		return graph.vertexList[((graph.length-1)/2)*2].dist+1;
		
		
	}
	
	static class Heap {
		 public int cap;
		 public Vertex[] heapArray;
		 public int length = 0;
		 
		 Heap(int cap){
			 this.cap = cap;
			 this.heapArray = new Vertex[cap];
		 }
		 
		 public void add(Vertex vert){
			 heapArray[length] = vert;
			 vert.position = length;
			 length++;
			 heapUp(vert);
		 }
		 public Vertex pop(){
			 Vertex min = heapArray[0];
			 heapArray[0].position = -1;
			 length--;
			 heapArray[length].position = 0;
			 heapArray[0] = heapArray[length];
			 heapDown(heapArray[length]);
			 //System.out.println("min was " + min);
			 return min;
		 }
		 public void heapUp(Vertex vert){
			 Vertex current = vert;
			 int parentPosition = (((vert.position+1) / 2) - 1);
			 while (parentPosition >= 0){
				 Vertex vertex = heapArray[parentPosition];
				 if(vertex.dist > current.dist){
					 swap(current,vertex);
					 parentPosition = (((parentPosition+1)/2)-1);
				 }
				 else{
					 break;
				 }
			 }
		 }
		 public void heapDown(Vertex vert){
			 Vertex current = vert;
			 int leftPosition = (2*(vert.position+1))-1;
			 int rightPosition = (2*(vert.position+1));
			 while(leftPosition < length){
				 rightPosition = leftPosition+1;
				 Vertex leftChild = heapArray[leftPosition];
				 Vertex rightChild = null;
				 if(rightPosition< length){
					 rightChild = heapArray[rightPosition];
				 }
				 Vertex newChild = null;
				 if(rightChild != null && current.dist > rightChild.dist){
					 newChild = rightChild;
				 }
				 if((current.dist > leftChild.dist) && (newChild==null || leftChild.dist < rightChild.dist)){
					 newChild = leftChild;
				 }
				 if(newChild == null){
					 break;
				 }
				 swap(current,newChild);
				 leftPosition = (2*(current.position+1))-1;
			 }
			 
			 
		 }
		 public void swap(Vertex one, Vertex two){
			 heapArray[two.position] = one;
			 heapArray[one.position] = two;
			 int temp = one.position;
			 one.position = two.position;
			 two.position = temp;
		 }
	}
}
