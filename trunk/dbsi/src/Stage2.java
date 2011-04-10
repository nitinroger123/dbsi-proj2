import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
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
	
	//given an integer get the term. Ex 1 -> f1. Useful for quick set intersection
	HashMap<Integer, BasicTerm> integerToBasicTermMap = new HashMap<Integer, BasicTerm>();
	
	//given a basic term get the integer. Ex f1 -> 1. Useful for quick set intersection
	HashMap<BasicTerm, Integer> basicTermToIntegerMap = new HashMap<BasicTerm, Integer>();
	
	//given a bitmap returns the index corresponding to the bitmap in A
	HashMap<String, Integer> indexInAForBitMap = new HashMap<String, Integer>();
	public Stage2() throws FileNotFoundException, IOException{
		config = new Properties();
		config.load(new FileInputStream("config.properties"));
		A = new ArrayList<Subset>();
		
	}
	/**
	 * populate A with 2^k -1 subsets using bitmaps!
	 * @param list
	 * @throws IOException 
	 * @throws FileNotFoundException 
	 */
	private void buildSubsets(ArrayList<BasicTerm> list) throws FileNotFoundException, IOException{
		int bit =1;
		for(int i=0;i<Math.pow(2.0,list.size())-1;i++){
			String bitmap = Integer.toBinaryString(bit);
			A.add(new Subset(bitmap));
			bit++;
		}
		for(Subset s:A){
			String bitmap = s.getBitmap();
			for(int i = bitmap.length()-1;i>=0;i--){
				if(bitmap.charAt(i)=='1'){
					s.addBasicTermToSubset(integerToBasicTermMap.get(bitmap.length()-i));
				}
			}
		}
		indexInAForBitMap = new HashMap<String, Integer>();
		Collections.sort(A);
		int i=0;
		for(Subset s : A){
			s.setInitialCostForANDTerms();
			indexInAForBitMap.put(s.getBitmap(),i );
			i++;
		}
		
	}
	/**
	 * helper method to generate the 2^k -1 subsets of basic terms
	 * This method will call buildSubsets which will 
	 * populate the field "A" which will contain the 
	 * subsets.
	 * @param selectivities
	 * @throws IOException 
	 * @throws FileNotFoundException 
	 */
	private void generateSubsets(ArrayList<Double> selectivities) throws FileNotFoundException, IOException{
		Integer append = 1;
		integerToBasicTermMap = new HashMap<Integer, BasicTerm>();
		ArrayList<BasicTerm> list = new ArrayList<BasicTerm>();
		/**
		 * create a basic list of basic terms to help the creation
		 * of the Subsets. Give each basic term a name and set its
		 * selectivity.
		 */
		for(Double d: selectivities){
			BasicTerm basicTerm = new BasicTerm("f"+append, d);
			list.add(basicTerm);
			integerToBasicTermMap.put(append, basicTerm);
			basicTermToIntegerMap.put(basicTerm, append);
			append++;
		}
		for(BasicTerm b : list){
			//System.out.println(b.getName() + " : "+b.getSelectivity());
		}
		buildSubsets(list);
		System.out.println("Subsets are");
		for(Subset s: A){
			System.out.println("Cost is "+s.getCost()+ " No branch used? "+s.getBranch().toString());
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
	 * @throws IOException 
	 * @throws FileNotFoundException 
	 */
	public String dpOptimization(ArrayList<Double> selectivities) throws FileNotFoundException, IOException{
		for(Double d: selectivities){
			//System.out.print(d+ " ");
		}
		//System.out.println();
		generateSubsets(selectivities);
		System.out.println("Some test");
		for(Subset s : A){
			for(Subset sDash : A){
				if(s.isIntersectionNull(sDash)){
					System.out.println(s.union(sDash));
					System.out.println("Index of union is "+indexInAForBitMap.get(s.union(sDash)));
				}
			}
		}
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
