class Taxi {
	private double X,Y;
	private int id;
	private node closest_node;
	/*o constructor tou taxi pairnei ton pinaka me ta X,Y,id kai ta vazei ws times*/
	public Taxi(String[] A){
		X=Double.parseDouble(A[0]);
		Y=Double.parseDouble(A[1]);
		id=Integer.parseInt(A[2]);
		closest_node= new node();
		}
	public void set_closest_node(node x) {
		closest_node.change(x);
	}
	/*getters*/
	public void print_coordinates() {
		System.out.print(closest_node.get_X()+",");
		System.out.println(closest_node.get_Y());
	}
	public node get_closest_node() {
		return closest_node;
	}
	public double get_X() {
		return X;
	}
	public double get_Y() {
		return Y;
	}
	public int get_id() {
		return id;
	}
}