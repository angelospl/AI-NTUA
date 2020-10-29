import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.PriorityQueue;
import java.util.Scanner;
import java.util.Set;

class Graph {
	private HashMap<String,ArrayList<node>> Map;				//ena Map pou kwdikopoiei to grafo ousiastika me lista geitniasis
	private Client Pelatis;									//o pelatis
	private ArrayList<Taxi> Taxis;							//lsita me ta taxi kai ton kontinotero komvo tous sto grafo
	private int BestTaxi;
	private ArrayList<ArrayList<node>> minpath;
	private double distance_calc(node A,node B) {
		return Math.sqrt(Math.pow(A.get_X()-B.get_X(), 2)+Math.pow(A.get_Y()-B.get_Y(), 2));
	}
	/*Constructor ths klassis graph pou gemizei to HashMap vriskei ton kontinotero komvo gia ton pelati kai ta taxi kai enimerwnei ti lista me ta taxi*/ 	public Graph() throws FileNotFoundException{
 		Pelatis=new Client();							//dhmiourgoume to antikeimeno tou pelati
		Driver dr=new Driver();							//friaxnoume ti lista me ta taxi
		Taxis = dr.CreateList();						//me ti voi8itiki klassi driver
		Scanner scan = new Scanner(new File("nodes1.csv"),"UTF-8");
		scan.nextLine();
		node prev= new node();							//proigoumenos komvos
		double[] mintaxis=new double[Taxis.size()];		//dilwsi voi8itiku pinaka mintaxis
		double minclient=Double.MAX_VALUE;				//dilwsi kai arxikopoisi tis voi8itikis metavlitis minclient
		Arrays.fill(mintaxis,Double.MAX_VALUE);			//arxikopoihsh tou voi8itiku pinaka mintaxis me megala noumera
		Taxi taxi;										//voi8itiki metavliti taxi
		Map = new HashMap<String, ArrayList<node>>();
		while (scan.hasNextLine()){						//oso mporei na proxwrisei o scanner
			String temp=new String();					//pare ena temp string
			temp = scan.nextLine();						//vale sto temp tin epomeni grammi
			String[] A = temp.split(",");				//spastin me kommata
			node neo=new node(Double.parseDouble(A[0]),Double.parseDouble(A[1]),Integer.parseInt(A[2])); //neos komvos
			ArrayList<node> Emptylist,prevlist;					//dilwsi metavlitwn
			/****vres to pio kontino node ston pelati kai valto sto pedio closest_node toy pelati******/
			if (Math.abs(Pelatis.get_X()-neo.get_X())+Math.abs(Pelatis.get_Y()-neo.get_Y() )< minclient) {	
				minclient=Math.abs(Pelatis.get_X()-neo.get_X())+Math.abs(Pelatis.get_Y()-neo.get_Y() );
				Pelatis.set_closest_node(neo);
			}
			/*vres gia ka8e taxi ton pio kontino tou komvo kai apo8ikeuse to sto pedio closest_node gia ka8e taxi sti lista*/
			for(int i=0;i<Taxis.size();i++) {
				taxi=Taxis.get(i);
				if(Math.abs(taxi.get_X()-neo.get_X())+Math.abs(taxi.get_Y()-neo.get_Y())<mintaxis[i]) {
					mintaxis[i]=Math.abs(taxi.get_X()-neo.get_X())+Math.abs(taxi.get_Y()-neo.get_Y());
					taxi.set_closest_node(neo);
					Taxis.set(i, taxi);
				}
			}
			node testnode= new node();								//testnode 8a exei ton proigumeno an exw to idio id me ton proigumeno
			if (Map.isEmpty()) {
				Emptylist=new ArrayList<node>();
				Map.put(neo.hashing(), Emptylist);					//an einai prwti fora vazw apla to stoixeio me keni lista
			}
			else {
				if (neo.sameid(prev)) {								//an exw idio id me proigumeno 
					testnode.change(prev);							//an eixa idio id me ton proigumeno to testnode pavei na einai empty node
					prevlist=Map.get(prev.hashing());				//pare ti lista geitonwn tou proigumenu					
					if (prevlist==null) {
						prevlist=new ArrayList<node>();							
					}
					prevlist.add(neo);								//vale ton eauto sou sti lista tu
					Map.put(prev.hashing(),prevlist);				//vale sto prev tin ananeomeni lista		
				}
				if(Map.containsKey(neo.hashing())) {				//an eisai se stavrodromi
					prevlist=Map.get(neo.hashing());				//pare ti lista tou komvou pou proipirxe
					if(!testnode.isEmpty()) prevlist.add(testnode);		//an eixa idio id me ton proigumeno vazw sti lista tu emena
					Map.put(neo.hashing(), prevlist);				//ananewse to map
				}
				else {		//an den eimai se stavrodromi
					Emptylist=new ArrayList<node>();
					if(!testnode.isEmpty()) Emptylist.add(testnode);	//an eixa idio id me ton proigumeno vale me san geitona alliws vale tin empty lista
					Map.put(neo.hashing(), Emptylist);						//apla ananewse to map
				}
			}
			prev=prev.change(neo);						//allaxe to proigumeno
		}
		scan.close();
	}
	/*ylopoihsh tou A* apo enan start_node se enan teliko target_node
	 * gyrnaei mia lista apo listes me tis enallaktikes diadromes proseggisis tou target node*/
 	private ArrayList<ArrayList<node>> A_star_implementation(node start_node,node target_node){
		ArrayList<node> path;									//arxikopoihsh tou path einai gia ka8e taxi
		boolean found=new Boolean(false);							//boolean metavliti pou dilwnei an exoume ftasei ston stoxo komvo
		Comparator<node> comp = new Comparator<node>() {			//ylopoioume enan comparator gia na sygrinei antikeimena typoy node
			@Override
			public int compare(node A, node B) {
				if (A.get_f()<B.get_f()) {
					return -1;
				}
				else return 1;
			}
			
		};
		PriorityQueue<node> Oura=new PriorityQueue<node>(10,comp);	//me vasi ton comparator ftiaxnoume mia priority queue
		Set<String> closedset =new HashSet<String>();				//closed set periexei tous komvous tous opoios exoume episkeftei
		Set<String> openset= new HashSet<String>();					//openset periexei tous komvous pou exoume stin oura gia na mn exoume duplicates
		node current_node=start_node;								//3ekiname apo ton arxiko komvo
		node foundloc=new node();									//foundloc o komvos ston opoio vrikame ton target node	
		Oura.add(current_node);										//vazoume stin oura ton trexonta kkomvo
		openset.add(current_node.hashing());						//to vazoume kai sto open set
		ArrayList<ArrayList<node>> alternatives=new ArrayList<ArrayList<node>>();	//pinakas me oles tis enallaktikes diadromes
		float real_dist=0;															//h pragmatikh apostash  pou exoume dianisi
		HashMap<String,ArrayList<node>> came_from= new HashMap<String,ArrayList<node>>();		//exoume ena map pou apo8ikeuoume ton komvo apo ton opoio ftanei syntomotera enas komvos
		boolean is_alternative;								//metavliti pou elegxoume an yparxei kai allo enallaktiko monopati
		while(!Oura.isEmpty()) {							//oso den exeis ftasei se termatiko komvo
			current_node=Oura.poll();								//vgale ena stoixeio apo tin oura
			real_dist=current_node.get_real();						//au3hse th synolikh diadromi pou exei diani8i kata oso taxidepses gia na ftaseis sto current node
			closedset.add(current_node.hashing());					//vale sto closed set ton komvo pou molis episkef8ikes
			if (current_node.equals(target_node)) {					//an exoume ftasei ston komvo pelati
				found=true;											//dilwse oti to vrikam
				foundloc=current_node;								//kai apo8ikeuse ton komvo ston opoio ton vrikame
			}
			else {													//an den eisai se termatiko komvo
				ArrayList<node> neighbours = Map.get(current_node.hashing());	//pare ti lista twn geitonwn tou komvou
				for(int j=0;j<neighbours.size();j++) {							//gia ka8e geitona tou komvou pou eisai
					ArrayList<node> lista=new ArrayList<node>();				//arxikopoihsh ths listas
					lista.add(current_node);									//h lista 8a periexei olous tous dynatous tropous afixis se enan komvo
					node geit=neighbours.get(j);								//arxika afou ftasame se auton apo ton current node,8a mpei sti lista
					if(closedset.contains(geit.hashing())) continue;			//an ton exoume e3etasei idi ton komvo pigaine ston epomeno
					else {
						geit.f_calc(real_dist, current_node, target_node);		//ypologise to f(n)=g(n)+f(n)
						if (!openset.contains(geit.hashing())) {				//an den yparxei stin oura (metwpo anazitisis) 
							Oura.add(geit);										//valto stin oura
							openset.add(geit.hashing());						//kai valto kai sto open set
						}
						else {													//alliws an yparxei sto metwpo anazitisis
							Iterator<node> it;									//ftiaxe enan iterator it
							node mesa;											//dilwsi tou node mesa
							it=Oura.iterator();									
							while(it.hasNext()) {								//oso yparxei epomeno stoixeio tou iterator
								mesa=it.next();									//pare to epomeno stoixeio pes to mesa
								if(mesa.equals(geit)) {							//an to mesa exei idies syntetagmenes me ton ypo e3etash geitona
									if(Math.abs((geit.get_f()-mesa.get_f())/mesa.get_f()*100)<1) {	//elegxe an exoun peripou idio f-value
										lista=came_from.get(mesa.hashing());		//an exoun tote pare ti lista me tous tropous afixis ston komvo  auto
										lista.add(current_node);					//kai pros8ese se autin ton trexonta komvo
									}
									else if(geit.get_f()>mesa.get_f()) {
										lista=came_from.get(mesa.hashing());		//alliws an auto pou iparxei mesa exei tropo afixis kalytero apo ton current node
									}												//mhn peiraxeis ti lista
									else {
										it.remove();								//alliws an auto pou iparxei mesa einai xeirotero apo to current node
										Oura.add(geit);								//vgalto kai vale ton geitona pou einai kalyteros
									}
									break;											//ama to vroume stin oura teleiwnei i anazitisi
								}													//gia to an yparxoun o idios komvos polles fores to exei checkarei to set poy exoume
							}
						}
					}
					came_from.put(geit.hashing(), lista);							//exontas ypologisei ti lista gia oles tis periptwseis tin enimerwnoume sto hashmap
				}																	//me tous paterades olwn twn komvwn
			}
		}
		if(found) {																			//an o algori8mos vrike to stoxo
			is_alternative=true;															//arxikopoioume ws true oti yparxei kai enallaktiko monopati
			Double min_dist=Double.MAX_VALUE;
			while(is_alternative) {															//oso exoume enallaktika monopatia
				boolean flag1=false;														//flag1 elegxei an exei mpei sti grammi 166
				current_node=foundloc;														//3ekiname apo to telos apo to node sto opoio vrikame ton pelati
				path=new ArrayList<node>();													//arxikopoioume to monopati path
				path.add(current_node);														//pros8etoume sto monopati ton termatiko komvo
				real_dist=0;
				double travel=0;
				while(true) {						
					node pateras;								
					if (flag1==false&&came_from.get(current_node.hashing()).size()>1) {		//an den exoume mpei se enallaktiki diadromi kai an den yparxei mono enas tropos metavasis ston current_node
						pateras=came_from.get(current_node.hashing()).get(1);				//vale ws patera tou komvou to extra stoixeio
						came_from.get(current_node.hashing()).remove(1);					//afairese to extra stoixeio afou to evales
						flag1=true;															//enimerwse oti evales ena enallaktiko path
					}
					else {
						pateras=came_from.get(current_node.hashing()).get(0);		//pairnoume ton patera ka8e komvou,diladi ton komvo apo ton opoio ir8ame me veltisto tropo
					}
					travel=distance_calc(pateras,current_node);
					real_dist=(float) (real_dist+travel);
					path.add(pateras);								//ton pros8etoume sto monopati
					current_node=pateras;							//kane current ton patera
					if (pateras.equals(start_node)) break;			//an exeis ftasei ston komvo apo ton opoio 3ekinhses stamata ti while
				}							
				if (flag1==false) is_alternative=false;				//an den vre8ike kapoio enallaktiko monopati stamata ti while
				if (real_dist<min_dist) {
					min_dist=(double) real_dist;
					alternatives.add(0, path);						//exoume to elaxisto monopati stin prwti 8esi tis listas listwn
				}
				else alternatives.add(path);						//alliws apla pros8etoume to monopati pou vrikame sti lista me ta enallaktika monopatia
			}
		}
		return alternatives;										//epistrefoume ti lista me ta enallaktika monopatia
	}
 	/*gia ka8e taxi trexei tin A_star_implementation kai vriskei to taxi me to elaxisto kostos
 	 * kai apo8ikeuei tis enallaktikes diadromes autou tou taxi*/
 	public double A_star() {
		Taxi tax;														//antikeimeno taxi
		ArrayList<ArrayList<node>> alternatives;									//dilwsi tou path kai min path									//elaxisto path synoliko
		minpath = new ArrayList<ArrayList<node>>();
		double mindist=Double.MAX_VALUE;								//elaxisti apostash synolika
		double real_dist=0;
		for(int i=0;i<Taxis.size();i++) {								//gia ka8e taxi
			tax=Taxis.get(i);											//vale sto tax to taxi pou phres ap th lista
			alternatives=this.A_star_implementation(tax.get_closest_node(), Pelatis.get_closest_node());
			real_dist=alternatives.get(0).get(0).get_real();			//ypologise tin apostasi apo tin apostasi tou prwtou stoixeiou tis prwtis enallaktikis diadromis
			/*to prwto stoixeio tou monopatiou einai o komvos pelatis kai exei apo8ikeumeni mesa tou tin pliroforia gia tin synoliki apostasi pou diani8ike*/
			if (real_dist<mindist) {				//vres to taxi me tin elaxisti diadromi					
				mindist=real_dist;
				minpath=alternatives;				//krata to monopati me tis enallaktikes autou tou taxi
				BestTaxi=tax.get_id();				//apo8ikeuse to id tou kalyterou taxi
			}
		}		
		return mindist;								//epistrepse tin elaxisti apostasi
	}
 	/*grafei sto arxeio map.kml me prasino mia kyria diadromi, me kokkino tis enallaktikes*/
 	public void write_path() throws FileNotFoundException {
 		Set<String> Visited = new HashSet<String>();
 		PrintWriter writer = new PrintWriter("map.kml");
        writer.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<kml xmlns=\"http://earth.google.com/kml/2.1\">\n" +
                "<Document>\n" +
                "<name>Taxi Routes</name>\n" +
                "<Style id=\"green\">\n" +
                "<LineStyle>\n" +
                "<color>ff009900</color>\n" +
                "<width>4</width>\n" +
                "</LineStyle>\n" +
                "</Style>\n" +
                "<Style id=\"red\">\n" +
                "<LineStyle>\n" +
                "<color>ff0000ff</color>\n" +
                "<width>4</width>\n" +
                "</LineStyle>\n" +
                "</Style>");
        writer.println("<Placemark>");
        writer.println("<name>Taxi with id "+BestTaxi+"</name>");
        writer.println("<styleUrl>#green</styleUrl><LineString>\n" + 
        		"<altitudeMode>relative</altitudeMode>\n" + 
        		"<coordinates>");
        for(int j=0;j<minpath.get(0).size();j++) {
			writer.println(minpath.get(0).get(j).get_X()+","+minpath.get(0).get(j).get_Y());
			Visited.add(minpath.get(0).get(j).hashing());
        }
        writer.println("</coordinates>");
        writer.println("</LineString>\n" + 
        		"</Placemark>");
        if(minpath.size()>1) {
        	for(int i=1;i<minpath.size();i++) {
        		writer.println("<Placemark>");
        		writer.println("<name>Alternative "+i+" for taxi with id"+BestTaxi+"</name>");
                writer.println("<styleUrl>#red</styleUrl><LineString>\n" + 
                		"<altitudeMode>relative</altitudeMode>\n" + 
                		"<coordinates>");
                for(int j=0;j<minpath.get(i).size();j++) {
                	node elem=minpath.get(i).get(j);
                	writer.println(elem.get_X()+","+elem.get_Y());

                }
        	 writer.println("</coordinates>");
             writer.println("</LineString>\n" + 
             		"</Placemark>");
        	}
        }
        writer.println("</Document>\n" + 
        		"</kml>");
        writer.close();
 	}
 	public ArrayList<ArrayList<node>> get_minpath() {
 		return minpath;
 	}
 	public int get_BestTaxi() {
 		return BestTaxi;
 	}
 	public ArrayList<Taxi> get_Taxis() {
		return Taxis;
	}
	public Client get_Client() {
		return Pelatis;
	}
}