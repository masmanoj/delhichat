package in.novopay.collection.batch.service;

public class CollectionData {
	
	public Long getLoanId() {
		return this.loanId;
	}
	public void setLoanId(Long loanId) {
		this.loanId = loanId;
	}
	public String getAmount() {
		return this.amount;
	}
	public void setAmount(String amount) {
		this.amount = amount;
	}
	public String getTransDate() {
		return this.transDate;
	}
	public void setTransDate(String transDate) {
		this.transDate = transDate;
	}
	public CollectionData(Long loanId, String amount, String transDate) {
		this.loanId = loanId;
		this.amount = amount;
		this.transDate = transDate;
	}
	private Long loanId;
	private String amount;
	private String transDate;
	
}