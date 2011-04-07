import java.util.ArrayList;


public class Subset {
	private Double cost;
	private Boolean branch;
	private Subset left;
	private Subset right;
	private Integer size;
	private Double productOfSelectivities;
	private ArrayList<BasicTerm> basicTerms;
	
	
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
	
	
	

}
