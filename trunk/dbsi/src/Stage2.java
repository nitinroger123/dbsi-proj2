import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
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
	/**
	 * helper method to generate the 2^k -1 subsets of basic terms
	 * This method will populate the field "A" which will contain the 
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
		
		for(BasicTerm b:list){
			System.out.println(b.getName()+ " : "+b.getSelectivity());
		}
		
		
	}
	/**
	 * The actual dynamic programming algorithm which operates on a list of basic blocks
	 * @param selectivities
	 * @return
	 */
	public String dpOptimization(ArrayList<Double> selectivities){
		for(Double d: selectivities){
			System.out.print(d+ " ");
		}
		System.out.println();
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
