package pl.molasym.StockMarket;

import java.util.Date;

public class OrderItem {

	private String personId;
	private String firmId;	
	private int quantityOfShare;
	private double pricePerShare;
	private Date offerDate;
	private TypeOfOrder itemType;
	
	
	public String getFirmId() {
		return firmId;
	}
	public void setFirmId(String firmId) {
		this.firmId = firmId;
	}
	public int getQuantityOfShare() {
		return quantityOfShare;
	}
	public void setQuantityOfShare(int quantityOfShare) {
		this.quantityOfShare = quantityOfShare;
	}
	public double getPricePerShare() {
		return pricePerShare;
	}
	public void setPricePerShare(double pricePerShare) {
		this.pricePerShare = pricePerShare;
	}
	public Date getOfferDate() {
		return offerDate;
	}
	public void setOfferDate(Date offerDate) {
		this.offerDate = offerDate;
	}

	
	public String getPersonId() {
		return personId;
	}
	public void setPersonId(String personId) {
		this.personId = personId;
	}
	public TypeOfOrder getItemType() {
		return itemType;
	}
	public void setItemType(TypeOfOrder itemType) {
		this.itemType = itemType;
	}
	public String toString()
	{
		return "Person: "+personId+", firm: "+firmId+", type: "+itemType+", quantity of Shares: "+quantityOfShare+", price per Share: "+pricePerShare+", date: "+offerDate;
	}

	
}
