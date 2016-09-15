package pl.molasym.StockMarket;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

import javax.swing.plaf.synth.SynthSpinnerUI;

public class OrderRepositoryImpl implements OrderRepository {
	
    private static Lock lock = new ReentrantLock();

	private List<OrderItem> transactionsRealized;
	private List<OrderItem> transactionsUnrealized;

	public OrderRepositoryImpl() {
		transactionsRealized = new ArrayList<OrderItem>();
		transactionsUnrealized = new ArrayList<OrderItem>();
	}


	public List<OrderItem> getTransactionsRealized() {
		return transactionsRealized;
	}

	public void setTransactionsRealized(List<OrderItem> transactionsRealized) {
		this.transactionsRealized = transactionsRealized;
	}

	public List<OrderItem> getTransactionsUnrealized() {
		return transactionsUnrealized;
	}

	public void setTransactionsUnrealized(List<OrderItem> transactionsUnrealized) {
		this.transactionsUnrealized = transactionsUnrealized;
	}

	
	// sekcja krytyczna
	public synchronized void searchTransaction(OrderItem buyOrderItem) {

		List<OrderItem> foundedTransactions = null; 

		// Szukamy oferty sprzedazy, gdzie cena za jedna akcje sa sobie rowne
		foundedTransactions = getSaleTransactions().stream()
				.filter(x -> x.getPricePerShare() == buyOrderItem.getPricePerShare()
						&& x.getFirmId().equals(buyOrderItem.getFirmId()))
				.collect(Collectors.toList());

		// Jesli nie znaleziono -> return
		if (foundedTransactions == null) {
			return;
		}

		int sumOfQuantityShare = foundedTransactions.stream().mapToInt(i -> i.getQuantityOfShare()).sum();

		// Sprawdzamy czy liczba akcji na sprzedaz jest sobie rowna
		if (buyOrderItem.getQuantityOfShare() == sumOfQuantityShare) {

			transactionsRealized.add(buyOrderItem);
			foundedTransactions.stream().forEach(x -> transactionsRealized.add(x));

			transactionsUnrealized.remove(buyOrderItem);
			foundedTransactions.stream().forEach(x -> transactionsUnrealized.remove(x));

		}

		// Jesli liczba akcji ktora chcemy kupic jest wieksza niz oferowana
		else if (buyOrderItem.getQuantityOfShare() > sumOfQuantityShare) {

			OrderItem buyOrderItemCopy = copyOrderItem(buyOrderItem);
			buyOrderItemCopy.setQuantityOfShare(buyOrderItem.getQuantityOfShare() - sumOfQuantityShare);

			updateUnrealizedTransactions(buyOrderItemCopy, buyOrderItem);
			foundedTransactions.stream().forEach(x -> transactionsUnrealized.remove(x));

			buyOrderItem.setQuantityOfShare(sumOfQuantityShare);
			transactionsRealized.add(buyOrderItem);
			foundedTransactions.stream().forEach(x -> transactionsRealized.add(x));

		} else {

			int itemQuantity = buyOrderItem.getQuantityOfShare();

			for (OrderItem foundedTransaction : foundedTransactions) {
				if (itemQuantity >= foundedTransaction.getQuantityOfShare()) {

					OrderItem buyOrderItemCopy = copyOrderItem(buyOrderItem);

					transactionsRealized.add(foundedTransaction);
					transactionsUnrealized.remove(foundedTransaction);
					transactionsUnrealized.remove(buyOrderItem);
					itemQuantity -= foundedTransaction.getQuantityOfShare();
					buyOrderItemCopy.setQuantityOfShare(
							buyOrderItem.getQuantityOfShare() - foundedTransaction.getQuantityOfShare());

				} else {

					OrderItem foundedTransactionCopy = copyOrderItem(foundedTransaction);

					OrderItem secondCopyOfBuyOrderItem = copyOrderItem(foundedTransaction);
					secondCopyOfBuyOrderItem.setQuantityOfShare(itemQuantity);

					foundedTransactionCopy.setQuantityOfShare(sumOfQuantityShare - buyOrderItem.getQuantityOfShare());
					updateUnrealizedTransactions(foundedTransactionCopy, foundedTransaction);

					transactionsRealized.add(secondCopyOfBuyOrderItem);
					transactionsRealized.add(buyOrderItem);
					transactionsUnrealized.remove(buyOrderItem);

				}
			}

		}

	}
	
	
	public void searchTransactionInThreads(OrderItem orderItem) {
		
		lock.lock();
		try{
			searchTransaction(orderItem);
		}finally {
			lock.unlock();
		}

    }
		
	
	
	public List<OrderItem> getSaleTransactions() {
		return transactionsUnrealized.stream().filter(x -> x.getItemType() == TypeOfOrder.ASK)
				.collect(Collectors.toList());
	}

	public List<OrderItem> getPurchaseTransactions() {
		return transactionsUnrealized.stream().filter(x -> x.getItemType() == TypeOfOrder.BID)
				.collect(Collectors.toList());
	}

	public void updateUnrealizedTransactions(OrderItem newItem, OrderItem oldItem) {
		transactionsUnrealized.remove(oldItem);
		transactionsUnrealized.add(newItem);
	}

	public OrderItem copyOrderItem(OrderItem orderItem) {
		OrderItem result = new OrderItem();
		result.setFirmId(orderItem.getFirmId());
		result.setItemType(orderItem.getItemType());
		result.setOfferDate(orderItem.getOfferDate());
		result.setPersonId(orderItem.getPersonId());
		result.setPricePerShare(orderItem.getPricePerShare());
		result.setQuantityOfShare(orderItem.getQuantityOfShare());

		return result;
	}



}
