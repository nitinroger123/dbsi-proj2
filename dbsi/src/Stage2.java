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
	public Stage2(String s){
		
	}
	/**
	 * populate A with 2^k -1 subsets using bitmaps!
	 * @param list
	 * @throws IOException 
	 * @throws FileNotFoundException 
	 */
	private void buildSubsets(ArrayList<BasicTerm> list) throws FileNotFoundException, IOException{
		A = new ArrayList<Subset>();
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
		
		/**
		 * this part is the end of part (1) of algo 4.11
		 */
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
			BasicTerm basicTerm = new BasicTerm("t"+append+"[o"+append+"[i]]", d);
			list.add(basicTerm);
			integerToBasicTermMap.put(append, basicTerm);
			basicTermToIntegerMap.put(basicTerm, append);
			append++;
		}
		buildSubsets(list);
	}
	/**
	 * The actual dynamic programming algorithm which operates on a list of basic blocks
	 * @param selectivities
	 * @return
	 * @throws IOException 
	 * @throws FileNotFoundException 
	 */
	public String dpOptimization(ArrayList<Double> selectivities) throws FileNotFoundException, IOException{	
		generateSubsets(selectivities);
		/**
		 * start of 4.11 part (2)
		 */
		for(Subset s : A){
			for(Subset sDash : A){
				if(s.isIntersectionNull(sDash)){
					Subset leftMost = s;
					while(leftMost.getLeft() != null){
						leftMost = s.getLeft();
					}
					//cmetric check					
					Double sDashp = sDash.getProductOfSelectivities();
					Double sp = leftMost.getProductOfSelectivities();
					if(sDashp > sp && (sDashp-1)/sDash.getFcost() > (sp-1)/leftMost.getFcost()){
						continue;
					}
					//dmetric check
					if(sDash.getProductOfSelectivities()<= 0.5 && sDash.getFcost() > s.getFcost()
							&& sDash.getProductOfSelectivities() > s.getProductOfSelectivities()){
						continue;
					}
					//else we will do the union
					if(A.get(indexInAForBitMap.get(s.union(sDash))).getCost() > sDash.combinedCost(s)){						
						A.get(indexInAForBitMap.get(s.union(sDash))).setCost(sDash.combinedCost(s));
						A.get(indexInAForBitMap.get(s.union(sDash))).setLeft(sDash);
						A.get(indexInAForBitMap.get(s.union(sDash))).setRight(s);
					}
				}
			}
		}
		String noBranchTerm = "";
		Subset s = A.get(A.size()-1);
		while(s.getRight()!=null){
			s= s.getRight();
		}
		if(s.getBranch()){
			noBranchTerm = s.getAndTerms();
			s.isLast = true;
			//System.out.println("Last term is "+noBranchTerm);
		}
		String outerPlan = spitOutPlan(A.get(A.size()-1),"");
		//System.out.println(outerPlan);
		System.out.println(prettyPrint(outerPlan , noBranchTerm));
		return A.get(A.size()-1).getCost().toString();
	}
	
	private String prettyPrint(String outerPlan, String noBranchTerm) {
		String answer = "";
		if(outerPlan.length()>0){
			answer = answer + "if("+outerPlan+") {\n\t";
			answer = answer + "answer[j]=i;\n\t";
			answer = answer + "j+=("+noBranchTerm+");\n";
			answer = answer + "}";
		}
		else{
			answer = answer + "answer[j]=i;\n";
			answer = answer + "j+=("+noBranchTerm+");\n";
		}
		return answer;
	}
	/**
	 * Recursive method to compute the plan
	 * @param s
	 * @param str
	 * @return
	 */
	private String spitOutPlan(Subset s,String str) {
		if(s.isLast) return "";
		if(s.getLeft() == null ){
			str+=s.getAndTerms();
			return "("+str+")";
		}
		else{
			if(!s.getRight().isLast)
				return "("+s.getLeft().getAndTerms()+" && "+spitOutPlan(s.getRight(),str)+")";
			else{
				return s.getLeft().getAndTerms();
			}
		}
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
			System.out.println("Overall Cost is: "+stage2.dpOptimization(selectivity));
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
