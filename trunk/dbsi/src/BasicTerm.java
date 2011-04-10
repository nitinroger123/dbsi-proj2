
public class BasicTerm {
	private Double selectivity;
	private String name;
	public BasicTerm(String name , Double selectivity){
		this.name = name;
		this.selectivity = selectivity;
	}
	
	public void setSelectivity(Double selectivity) {
		this.selectivity = selectivity;
	}
	public Double getSelectivity() {
		return selectivity;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getName() {
		return name;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result
				+ ((selectivity == null) ? 0 : selectivity.hashCode());
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
		BasicTerm other = (BasicTerm) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (selectivity == null) {
			if (other.selectivity != null)
				return false;
		} else if (!selectivity.equals(other.selectivity))
			return false;
		return true;
	}
	
	

}
