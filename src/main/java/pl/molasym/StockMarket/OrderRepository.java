package pl.molasym.StockMarket;

import java.util.List;

public interface OrderRepository {
	
	public void searchTransaction(OrderItem item);
	public void searchTransactionInThreads(OrderItem orderItem);
	public List<OrderItem> getSaleTransactions();
	public List<OrderItem> getPurchaseTransactions();
	public void updateUnrealizedTransactions(OrderItem newItem, OrderItem oldItem);

}
