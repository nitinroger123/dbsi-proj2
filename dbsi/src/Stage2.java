import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Properties;

/**
 * The branch misprediction optimizer
 * @author nitin
 *
 */
public class Stage2 {
	// The config file which will contain r,t,l,m .. etc values
	Properties config;
	//Field which represents the 2^k -1 subsets.
	ArrayList<Subset> A;
	
	public Stage2() throws FileNotFoundException, IOException{
		config = new Properties();
		config.load(new FileInputStream("config.properties"));
		A = new ArrayList<Subset>();
		
	}
	
	private boolean contains(ArrayList<Subset> array , Subset s){
		for(Subset subset : array){
			HashMap<String,String> map = new HashMap<String, String>();
			ArrayList<BasicTerm> basicTerms = subset.getBasicTerms();
			if(basicTerms.size() == s.getBasicTerms().size()){
				for(BasicTerm b: basicTerms){
					map.put(b.getName(), "");
				}
				boolean flag = false;
				for(BasicTerm b : s.getBasicTerms()){
					if(!map.containsKey(b.getName())){
						flag = false;
					}
					else{
						flag = true;
					}
				}
				if(flag) return true;
			}
		}
		return false;
	}
	/**
	 * Helper method to generate the different & separated
	 * subsets. Basically generates all the different permutations
	 * @param list
	 */
	
	private void buildSubsets(ArrayList<BasicTerm> list){
		/**
		 * populate the subset initially with one basic term
		 */
		boolean flag = true;
		for(BasicTerm bt: list){
			A.add(new Subset(bt));
		}
		while(flag){
			for(BasicTerm bt: list){
				ArrayList<Subset> copy =(ArrayList<Subset>) A.clone();
				for(Subset s: copy){
						ArrayList<BasicTerm> temp = new ArrayList<BasicTerm>(s.getBasicTerms());
						if(!s.contains(bt)){
							temp.add(bt);
							if(!contains(A,new Subset(temp))){
								A.add(new Subset(temp));
							}	
						}
						if(temp.size()>= list.size()){
							flag=false;
						}
				}
			}
		}
	}
	
	/**
	 * helper method to generate the 2^k -1 subsets of basic terms
	 * This method will call buildSubsets which will 
	 * populate the field "A" which will contain the 
	 * subsets.
	 * @param selectivities
	 */
	private void generateSubsets(ArrayList<Double> selectivities){
		Integer append = 1;
		ArrayList<BasicTerm> list = new ArrayList<BasicTerm>();
		/**
		 * create a basic list of basic terms to help the creation
		 * of the Subsets. Give each basic term a name and set its
		 * selectivity.
		 */
		for(Double d: selectivities){
			list.add(new BasicTerm("f"+append, d));
			append++;
		}
		for(BasicTerm b : list){
			//System.out.println(b.getName() + " : "+b.getSelectivity());
		}
		buildSubsets(list);
		System.out.println("Subsets are");
		for(Subset s: A){
			for(BasicTerm b : s.getBasicTerms()){
				System.out.print(b.getName()+ "&");
			}
			System.out.println();
		}
	}
	/**
	 * The actual dynamic programming algorithm which operates on a list of basic blocks
	 * @param selectivities
	 * @return
	 */
	public String dpOptimization(ArrayList<Double> selectivities){
		for(Double d: selectivities){
			//System.out.print(d+ " ");
		}
		//System.out.println();
		generateSubsets(selectivities);
		return null;
	}
	
	/**
	 * Reads the query file and calls the optimizer
	 * @param stage2
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	private static void readAndEvaluateCost(Stage2 stage2)
			throws FileNotFoundException, IOException {
		BufferedReader reader = new BufferedReader(new FileReader("query.txt"));
		String input;
		while((input = reader.readLine())!=null){
			String s[] = input.split(" ");
			ArrayList<Double> selectivity = new ArrayList<Double>();
			for(int i=0;i<s.length;i++){
				selectivity.add(Double.parseDouble(s[i]));
			}
			stage2.dpOptimization(selectivity);
		}
	}
	
	/**
	 * The entry point of the program
	 * @param args
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public static void main(String args[]) throws FileNotFoundException, IOException{
		Stage2 stage2 = new Stage2();
		readAndEvaluateCost(stage2);
		
	}

}
