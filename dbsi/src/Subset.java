import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Properties;


public class Subset implements Comparable<Subset>{
	private Double cost;
	private Boolean branch;
	private Subset left;
	private Subset right;
	private Integer size;
	private Double productOfSelectivities;
	private ArrayList<BasicTerm> basicTerms;
	private String bitmap = new String();
	/**
	 * Adds a basic term into the Subset
	 * @param b
	 */
	public void addBasicTermToSubset(BasicTerm b){
		this.basicTerms.add(b);
	}
	/**
	 * Creates a subset with one member
	 * @param b
	 */
	public Subset(BasicTerm b){
		basicTerms = new ArrayList<BasicTerm>();
		this.basicTerms.add(b);
	}
	
	public Subset(String bitmap){
		this.basicTerms = new ArrayList<BasicTerm>();
		this.setBitmap(bitmap);
	}
	/**
	 * Creates a subset with a list of basic terms
	 * @param terms
	 */
	public Subset(ArrayList<BasicTerm> terms){
		basicTerms = new ArrayList<BasicTerm>();
		this.basicTerms.addAll(terms);
	}
	
	public void setCost(Double cost) {
		this.cost = cost;
	}
	public Double getCost() {
		return cost;
	}
	public void setBranch(Boolean branch) {
		this.branch = branch;
	}
	public Boolean getBranch() {
		return branch;
	}
	public void setLeft(Subset left) {
		this.left = left;
	}
	public Subset getLeft() {
		return left;
	}
	public void setRight(Subset right) {
		this.right = right;
	}
	public Subset getRight() {
		return right;
	}
	public void setProductOfSelectivities(Double productOfSelectivities) {
		this.productOfSelectivities = productOfSelectivities;
	}
	
	/**
	 * go through the basic terms, multiply selectivity and return
	 * @return
	 */
	
	public Double getProductOfSelectivities() {
		Double result = 1.0;
		for(BasicTerm b: this.basicTerms){
			result*=b.getSelectivity();
		}
		return result;
	}
	public void setBasicTerms(ArrayList<BasicTerm> basicTerms) {
		this.basicTerms = basicTerms;
	}
	public ArrayList<BasicTerm> getBasicTerms() {
		return basicTerms;
	}
	
	public Integer getSize() {
		return basicTerms.size();
	}
	
	public boolean contains(BasicTerm b){
		for(BasicTerm bt : basicTerms){
			if(b.equals(bt)){
				return true;
			}
		}
		return false;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((basicTerms == null) ? 0 : basicTerms.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Subset other = (Subset) obj;
		if (basicTerms == null) {
			if (other.basicTerms != null)
				return false;
		} else if (!basicTerms.equals(other.basicTerms))
			return false;
		return true;
	}
	public void setBitmap(String bitmap) {
		this.bitmap = bitmap;
	}
	public String getBitmap() {
		return bitmap;
	}
	
	public int position(){
		int sum = 0;
		for(int i=0;i<bitmap.length();i++){
			if(bitmap.charAt(i)=='1') {
				sum++;
			}
		}
		return sum;
	}
	@Override
	public int compareTo(Subset x){
		if(this.position() >= x.position()){
			return 1;
		}
		if(this.position() == x.position()){
			return 0;
		}
		return -1;
	}
	/**
	 * returns true if the intersection is null
	 * @param other
	 * @return
	 */
	public boolean isIntersectionNull(Subset other){
		HashMap<Integer,Integer> map = new HashMap<Integer, Integer>();
		int j=0;
		for(int i = this.getBitmap().length()-1;i>=0;i--){
			if(this.getBitmap().charAt(i)=='1'){
				map.put(j, j);
			}
			j++;
		}
		j=0;
		for(int i = other.getBitmap().length()-1;i>=0;i--){
			if(other.getBitmap().charAt(i)=='1'){
				if(map.get(j)!=null){
					return false;
				}
			}
			j++;
		}
		return true;
	}
	/**
	 * Given two subsets, returns the union of their bitmaps
	 * so that the union can be located directly in A
	 * @param other
	 * @return
	 */
	public String union(Subset other){
		Integer a = Integer.parseInt(this.getBitmap(),2);
		Integer b = Integer.parseInt(other.getBitmap(),2);
		Integer c = a | b;
		String result = Integer.toBinaryString(c);
		return result;
	}
	
	public void setInitialCostForANDTerms() throws FileNotFoundException, IOException{
		Double noBranchCost = 0.0;
		Double logicalAndCost = 0.0;
		Properties config = new Stage2().config;
		Double r = Double.parseDouble(config.getProperty("r"));
		Double l = Double.parseDouble(config.getProperty("l"));
		Double f = Double.parseDouble(config.getProperty("f"));
		Double a = Double.parseDouble(config.getProperty("a"));
		Double t = Double.parseDouble(config.getProperty("t"));
		Double m = Double.parseDouble(config.getProperty("m"));
		Integer k = this.getSize();
		
		// Calculate value of q
		Double prodOfSelectivities = this.getProductOfSelectivities();
		Double q = 0.0;
		if(prodOfSelectivities <= 0.5 ) q=prodOfSelectivities;
		else q = 1.0 - prodOfSelectivities;
		
		//Example 4.4 in the paper
		noBranchCost = k*r+ (k-1)*l + k*f + a;
		
		//Example 4.5
		logicalAndCost = k*r + (k-1)*l + k*f + t + m*q + prodOfSelectivities*a; 
		if(logicalAndCost <= noBranchCost){
			this.cost = logicalAndCost;
			this.branch = false;
		}
		else{
			this.cost = noBranchCost;
			this.branch = true;
		}
	}
	
	

}
