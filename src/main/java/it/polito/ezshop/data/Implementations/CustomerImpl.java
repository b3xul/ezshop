
public class CustomerImpl implements Customer {
	private Integer id;
	private String name;
	private String card;
	private Integer points;
	

	public CustomerImpl(Integer id, String name, String card, Integer points) {
		this.id = id;
		this.name = name;
		this.card = card;
		this.points = points;
	}
	

	@Override
	public String getCustomerName() {
		return this.name;
	}

	@Override
	public void setCustomerName(String customerName) {
		this.name = customerName;
	}

	@Override
	public String getCustomerCard() {
		return this.card;
	}

	@Override
	public void setCustomerCard(String customerCard) {
		this.card = customerCard;
	}

	@Override
	public Integer getId() {
		return this.id;
	}

	@Override
	public void setId(Integer id) {
		this.id = id; 
	}
	
	@Override
	public Integer getPoints() {
		return this.points;
	}

	@Override
	public void setPoints(Integer points) {
		this.points = points;
	}

	public String toString() {
		String s = "";
		if(this.card != null)
			s = " card:" + this.card + " points:" + this.points; 
		return "id:" + this.id + " name:" + this.name + s;
	}

}
