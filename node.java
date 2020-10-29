class node {
	private double X,Y;
	private float heur,real,f;
	private int id;
	/*constructor gia to node vazei tis times tou node*/
	public node(double a,double b,int c) {
		X=a;
		Y=b;
		id=c;
	}
	public node() {
		X=-1;
		Y=-1;
		id=-1;
	}
	/*elegxe gia to an einai to adeio antikeimeno ftiagmeno apo ton default constructor*/
	public boolean isEmpty() {
		if(X==-1 && Y==-1 && id==-1) return true;
		else return false;
	}
	/*elegxei an exoun koines syntetagmenes*/
	public boolean equals(node o) {
		if (this.X==o.get_X() && this.Y==o.get_Y()) {
			return true;
		}
		else return false;
	}
	/*elegxei an exoun idio id*/
	public boolean sameid(node other) {
		if (this.id==other.get_id()) return true;
		else return false;
	}
	/*i methodos allazei ta dedomena enos node kai pairnei tis time tou other*/
	public node change(node other) {
		X=other.get_X();
		Y=other.get_Y();
		id=other.get_id();
		return this;
	}
	/*ypologizei tin pragmatiki apostasi tou this apo to B kai ti vazei sti real,travelled einai i apostasi pou exei diani8ei*/
	private void real_calculation(float travelled,node B) {
		real= (float) (travelled+Math.sqrt(Math.pow(X-B.get_X(), 2)+Math.pow(Y-B.get_Y(), 2)));
	}
	/*vriskei tin euclidian heuristic timi apo to antikeimeno this sto B*/
	private void heur_calculation(node B) {
		heur = (float) Math.sqrt(Math.pow(X-B.get_X(), 2)+Math.pow(Y-B.get_Y(), 2));
	}
	/*ypologizei thn timi f(n)=g(n)+h(n) kai enimerwnei tis times real,heur kai f*/
	public void f_calc(float travelled,node Geit,node Pelatis) {
		this.heur_calculation(Pelatis);
		this.real_calculation(travelled, Geit);
		f=real+heur;
	}
	/*getters*/
	public float get_f () {
		return f;
	}
	public float get_heur(){
		return heur;
	}
	public float get_real() {
		return real;
	}
	public String hashing() {
		return Double.toString(X)+","+Double.toString(Y);
	}
	public void print_coordinates() {
		System.out.print(X+",");
		System.out.println(Y);
	}
	public double get_X(){
		return X;
	}
	public double get_Y() {
		return Y;
	}
	public int get_id() {
		return id;
	}
}