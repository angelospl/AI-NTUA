import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

class Client {
	private double X,Y;
	private node closest_node;
	/*diavazume ston constructor to arxeio tou pelati kai ton dimiourgoume*/
	public Client () throws FileNotFoundException{	
		Scanner scan = new Scanner(new File("client.csv"));
		scan.nextLine();	//pername ta prwta X,Y tou arxeiou
		String temp=new String();
		temp=scan.nextLine();
		String[] A=temp.split(",");
		X=Double.parseDouble(A[0]);
		Y=Double.parseDouble(A[1]);
		closest_node = new node();
		scan.close();
	}
	public void set_closest_node(node x) {
		closest_node.change(x);
	}
	/*getters*/
	public void print_coordinates(){
		System.out.print(closest_node.get_X()+",");
		System.out.println(closest_node.get_Y());
	}
	public node get_closest_node(){
		return closest_node;
	}
	public double get_X() {
		return X;
	}
	public double get_Y() {
		return Y;
	}
}