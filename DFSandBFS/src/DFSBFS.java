import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Scanner;

class Ex1 {
	static List<String> Vertex = new ArrayList<String>();
	static List<List<String>> Edge = new ArrayList<List<String>>();
	static int[][] Matrix = new int[6000][6000];
	static boolean[] visited = new boolean[6000];
	static int[] ccnum = new int[6000];
	static int TPLT;
	static int N;
	
	public static void readFile() throws IOException{
		FileInputStream fis = new FileInputStream("data.txt");
		InputStreamReader isr = new InputStreamReader(fis);
		BufferedReader bfr = new BufferedReader(isr);
		String line = bfr.readLine();
		
		while(line != null) {
			Vertex.add(line);
			line = bfr.readLine();
		}
		
		fis.close();
		isr.close();
		bfr.close();
	}
	
	public static int countDuplicate(String s1, String s2){
		int count = 0;
		for(int i=0; i<s1.length(); i++) {
			if(s1.charAt(i) != s2.charAt(i)) {
				count++;
			}
		}
		return count;
	}
	
	public static void createEdge() throws Exception{
		N = Vertex.size();
		for(int i=0; i<N; i++) {
			for(int j=0; j<N; j++) {
				if(countDuplicate(Vertex.get(i), Vertex.get(j)) == 1) {
					Matrix[i][j] = 1;
					
					List<String> t = new ArrayList<>();
					t.add(Vertex.get(i));
					t.add(Vertex.get(j));
					
					Edge.add(t);
				}
				else {
					Matrix[i][j] = 0;
				}
			}
		}
	}
	
	public static void DFS() {
		TPLT = 0;
		Arrays.fill(visited, false);
		for(int i=0; i<N; i++) {
			if(visited[i] == false) {
				TPLT++;
				explore(i);
			}
		}
	}
	
	public static void explore(int i) {
		visited[i] = true;
		ccnum[i] = TPLT;
		for(int j=0; j<N; j++) {
			if(Matrix[i][j] == 1) {
				if(visited[j] == false) explore(j);
			}
		}
	}
	
	public static void BFS(int k, String[] Prev) {
		int[] dist = new int[N];
		Arrays.fill(dist, Integer.MAX_VALUE);
		dist[k] = 0;
		Queue<String> q = new LinkedList<String>();
		q.add(Vertex.get(k));
		
		while(!q.isEmpty()) {
			String u = q.poll();
			String v;
			for(int i=0; i<Edge.size(); i++) {
				if(Edge.get(i).contains(u)) {
					v = (u.equals(Edge.get(i).get(1))) ? Edge.get(i).get(0) : Edge.get(i).get(1);
					if(dist[Vertex.indexOf(v)] == Integer.MAX_VALUE) {
						q.add(v);
						dist[Vertex.indexOf(v)] = dist[Vertex.indexOf(u)] + 1;
						Prev[Vertex.indexOf(v)] = u;
					}
				}
			}
		}
	}
	
	public static void findShortest(String start, String last) throws Exception {
		String[] Prev = new String[N];
		int distance = 0;
		BFS(Vertex.indexOf(start), Prev);
		int i = Vertex.indexOf(last);
		if(Prev[i] == null) {
			System.out.println("Không có đường đi từ " + start + " tới " + last);
		}
		else {
			System.out.println("Đường đi ngắn nhất từ " + start + " tới " + last + " là : ");
			System.out.print(last + " <-- ");
			while(!Prev[i].equals(start)) {
				distance++;
				System.out.print(Prev[i] + " <-- ");
				i = Vertex.indexOf(Prev[i]);
			}
			System.out.println(start);
			distance++;
			System.out.println("Độ dài đoạn đường này là " + distance);
		}
	}
}

class Ex2 {
	static List<String> Vertex = new ArrayList<String>();
	static List<List<String>> Edge = new ArrayList<List<String>>();
	static int[][] Matrix = new int[6000][6000];
	static int[] post = new int[6000];
	static int TPLT;
	static int[] ccnum = new int[6000];
	static boolean[] visited = new boolean[6000];
	static int clock = 1;
	static int[][] MatrixGr = new int[6000][6000];
	static String[] topo = new String[6000];
	static int N;
	
	public static void readFile() throws IOException{
		FileInputStream fis = new FileInputStream("data.txt");
		InputStreamReader isr = new InputStreamReader(fis);
		BufferedReader bfr = new BufferedReader(isr);
		String line = bfr.readLine();
		
		while(line != null) {
			Vertex.add(line);
			line = bfr.readLine();
		}
		
		fis.close();
		isr.close();
		bfr.close();
	}
	
	public static boolean isEdge(String s1, String s2) {
		boolean check = true;
		StringBuilder y = new StringBuilder(s2);
		String ss2 = s2;
		for(int i=1; i<s1.length(); i++) {
			String s = String.valueOf(s1.charAt(i));
			if(!ss2.contains(s)) {
				check = false;
				break;
			}
			else {
				y.deleteCharAt(y.indexOf(s));
				ss2 = String.valueOf(y);
			}
		}
		return check;
	}
	
	public static void createEdge() throws Exception {
		N = Vertex.size();
		for(int i=0; i<N; i++) {
			for(int j=0; j<N; j++) {
				if(isEdge(Vertex.get(i), Vertex.get(j))) {
					Matrix[i][j] = 1;
					
					List<String> t = new ArrayList<String>();
					t.add(Vertex.get(i));
					t.add(Vertex.get(j));
					
					Edge.add(t);
				}
				else {
					Matrix[i][j] = 0;
				}
			}
		}
		
		for(int i=0; i<N; i++) {
			for(int j=0; j<N; j++) {
				MatrixGr[i][j] = Matrix[j][i];
			}
		}
		Arrays.fill(ccnum, 0);
	}
	
	public static void exploreGr(int i) {
		visited[i] = true;
		for(int j=0; j<N; j++) {
			if(MatrixGr[i][j] == 1) {
				if(visited[j] == false) exploreGr(j);
			}
		}
		post[i] = clock;
		clock++;
	}
	
	public static void DFSGr() {
		Arrays.fill(visited, false);
		for(int i=0; i<N; i++) {
			if(visited[i] == false) {
				exploreGr(i);
			}
		}
	}
	
	public static void topoSort() {
		for(int i=0; i<N; i++) {
			topo[N - post[i]] = Vertex.get(i);
		}
	}
	
	public static void explore(int i) {
		visited[i] = true;
		ccnum[i] = TPLT;
		for(int j=0; j<N; j++) {
			if(Matrix[i][j] == 1) {
				if(visited[j] == false) explore(j);
			}
		}
	}
	
	public static void DFS() {
		TPLT = 0;
		Arrays.fill(visited, false);
		for(int i=0; i<N; i++) {
			if(visited[Vertex.indexOf(topo[i])] == false) {
				TPLT++;
				explore(Vertex.indexOf(topo[i]));
			}
		}
	}
	
	public static void PrintVertexInConect(String input) {
		int count = 0;
		if(Vertex.contains(input)) {
			for(int i=0; i<N; i++) {
				if(ccnum[i] == ccnum[Vertex.indexOf(input)]) {
					count++;
					System.out.println(Vertex.get(i));
				}
			}
			System.out.println("Có tất cả " + count + " từ thuộc thành phần liên thông này");
		}
		else {
			System.out.println("Không tồn tại từ này trong data");
		}
	}
	
	public static void BFS(int k, String[] Prev) {
		int[] dist = new int[N];
		Arrays.fill(dist, Integer.MAX_VALUE);
		dist[k] = 0;
		Queue<String> q = new LinkedList<String>();
		q.add(Vertex.get(k));
		
		while(!q.isEmpty()) {
			String u = q.poll();
			String v;
			for(int i=0; i<Edge.size(); i++) {
				if(Edge.get(i).get(0).equals(u)) {
					v = Edge.get(i).get(1);
					if(dist[Vertex.indexOf(v)] == Integer.MAX_VALUE) {
						q.add(v);
						dist[Vertex.indexOf(v)] = dist[Vertex.indexOf(u)] + 1;
						Prev[Vertex.indexOf(v)] = u;
					}
				}
			}
		}
	}
	
	public static void findShortest(String start, String last) throws Exception {
		String[] Prev = new String[N];
		int distance = 0;
		BFS(Vertex.indexOf(start), Prev);
		int i = Vertex.indexOf(last);
		if(Prev[i] == null) {
			System.out.println("Không có đường đi từ " + start + " tới " + last);
		}
		else {
			System.out.println("Đường đi ngắn nhất từ " + start + " tới " + last + " là : ");
			System.out.print(last + " <-- ");
			while(!Prev[i].equals(start)) {
				distance++;
				System.out.print(Prev[i] + " <-- ");
				i = Vertex.indexOf(Prev[i]);
			}
			System.out.println(start);
			distance++;
			System.out.println("Độ dài đoạn đường là " + distance);
		}
	}
}

public class DFSBFS {

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		Ex1 ex1 = new Ex1();
		Ex2 ex2 = new Ex2();
		
		Scanner sc = new Scanner(System.in);
		
		System.out.println("------------BÀI 1------------");
		String start, last;
		Ex1.readFile();
		ex1.createEdge();
		ex1.DFS();
		System.out.println("SỐ THÀNH PHẦN LIÊN THÔNG LÀ : " + ex1.TPLT);
		System.out.println("Nhập từ bắt đầu (5 chữ cái) : ");
		start = sc.next();
		System.out.println("Nhập từ kết thúc (5 chữ cái) : ");
		last = sc.next();
		ex1.findShortest(start, last);
		
		System.out.println("------------BÀI 2------------");
		String start1, last1;
		ex2.readFile();
		ex2.createEdge();
		ex2.DFSGr();
		ex2.topoSort();
		ex2.DFS();
		System.out.println("SỐ THÀNH PHẦN LIÊN THÔNG MẠNH LÀ : " + ex2.TPLT);
		System.out.println("BẠN MUỐN TÌM CÁC TỪ THUỘC CÙNG THÀNH PHẦN LIÊN THÔNG MẠNH CỦA TỪ NÀO : ");
		String input = sc.next();
		System.out.println("Các từ thuộc cùng thành phần liên thông mạnh với " + input + " là :");
		ex2.PrintVertexInConect(input);
		System.out.println("Nhập từ bắt đầu (5 chữ cái) : ");
		start1 = sc.next();
		System.out.println("Nhập từ kết thúc (5 chữ cái) : ");
		last1 = sc.next();
		ex2.findShortest(start1, last1);
	}

}
