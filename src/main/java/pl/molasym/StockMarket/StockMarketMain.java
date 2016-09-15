package pl.molasym.StockMarket;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Time;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class StockMarketMain {

	static OrderRepositoryImpl orderRepository = new OrderRepositoryImpl();
	

	public static void main(String[] args) {
		OrderItem readItem = new OrderItem();

		try {
			String line;
			BufferedReader read = new BufferedReader(new FileReader("/home/moliq/test.txt"));
			try {
				while ((line = read.readLine()) != null) {

					String[] tmp = line.split(";");
					readItem = new OrderItem();
					try {
						readItem.setFirmId(tmp[0]);
						readItem.setPersonId(tmp[1]);
						readItem.setQuantityOfShare(Integer.parseInt(tmp[2]));
						readItem.setPricePerShare(Double.parseDouble(tmp[3]));
						DateFormat formatter = new SimpleDateFormat("dd/MM/yy HH:mm:ss");
						Date date = (Date) formatter.parse(tmp[4]);
						readItem.setOfferDate(date);
						if (tmp[5].equals("BID"))
							readItem.setItemType(TypeOfOrder.BID);
						else if (tmp[5].equals("ASK"))
							readItem.setItemType(TypeOfOrder.ASK);
					} catch (Exception e3) {
						System.err.println("Wrong data format " + e3.getMessage());
						readItem = null;
					}
					if (readItem != null) {
						orderRepository.getTransactionsUnrealized().add(readItem);
						for (OrderItem x : orderRepository.getPurchaseTransactions()) {

							/* WERSJA Z WATKAMI */
							FindOrderItemThread findOrderItemThread = new FindOrderItemThread(x);
							findOrderItemThread.start();

							/* WERSJA BEZ WATKOW */
							//orderRepository.searchTransaction(x);

						}

					}
				}
			} catch (IOException e) {
				System.err.println(e.getMessage());
				e.printStackTrace();
			}
		} catch (FileNotFoundException e) {
			System.err.println(e.getMessage());
			e.printStackTrace();
		}

		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Unrealized transactions");
		for (OrderItem Unrealized : orderRepository.getTransactionsUnrealized())
			System.out.println(Unrealized);
		System.out.println("");
		System.out.println("Realized transactions");
		for (OrderItem Realized : orderRepository.getTransactionsRealized())
			System.out.println(Realized);

	}

	public static class FindOrderItemThread extends Thread {

		private OrderItem orderItem;

		public FindOrderItemThread(OrderItem orderItem) {
			this.orderItem = orderItem;
		}

		public OrderItem getOrderItem() {
			return orderItem;
		}

		public void setOrderItem(OrderItem orderItem) {
			this.orderItem = orderItem;
		}

		@Override
		public void run() {

			orderRepository.searchTransactionInThreads(orderItem);

		}

	}

}
