import  java.io.*;
import java.util.Scanner;
import java.util.ArrayList;
class Driver {
	public ArrayList<Taxi> CreateList() throws FileNotFoundException {
		Scanner scan=new Scanner(new File("taxis.csv"));
		String temp=new String();
		String[] A;
		ArrayList<Taxi> lista=new ArrayList<Taxi>();
		scan.nextLine();				//skipparoume to X,Y,id poy exei i prwti grammi tou arxeiou
		while(scan.hasNextLine()) {
			temp=scan.nextLine();
			A=temp.split(",");
			Taxi elem=new Taxi(A);		//enhmerwnoume tis times autou tou taxi
			lista.add(elem);			//to pros8etoume sti lista
		}
		scan.close();
		return lista;
	}
}
public class AIask1 {
	public static void main(String[] args) throws FileNotFoundException {	
		Graph G = new Graph();
		double min=G.A_star();
		G.write_path();
		System.out.println(G.get_BestTaxi()+" with distance "+min*100+"km");
	}
}
