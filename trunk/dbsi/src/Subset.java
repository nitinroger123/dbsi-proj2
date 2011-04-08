import java.util.ArrayList;


public class Subset {
	private Double cost;
	private Boolean branch;
	private Subset left;
	private Subset right;
	private Integer size;
	private Double productOfSelectivities;
	private ArrayList<BasicTerm> basicTerms;
	
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
	public Double getProductOfSelectivities() {
		return productOfSelectivities;
	}
	public void setBasicTerms(ArrayList<BasicTerm> basicTerms) {
		this.basicTerms = basicTerms;
	}
	public ArrayList<BasicTerm> getBasicTerms() {
		return basicTerms;
	}
	public void setSize(Integer size) {
		this.size = size;
	}
	public Integer getSize() {
		return size;
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
	
	

}
