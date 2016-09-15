package pl.molasym.StockMarket;

import static org.junit.Assert.assertEquals;

import javax.annotation.processing.SupportedAnnotationTypes;

import org.junit.Test;
import org.junit.experimental.theories.suppliers.TestedOn;


public class StockMarketTest {
	
	
	@Test
	public void SellAllBuyAll(){
		
		OrderRepositoryImpl repo = new OrderRepositoryImpl();

		
		OrderItem orderItem = new OrderItem();
		orderItem.setFirmId("Facebook");
		orderItem.setItemType(TypeOfOrder.ASK);
		orderItem.setQuantityOfShare(3000);
		orderItem.setPricePerShare(20);
		orderItem.setPersonId("Mateusz");
		
		OrderItem orderItem2 = new OrderItem();
		orderItem2.setFirmId("Facebook");
		orderItem2.setItemType(TypeOfOrder.ASK);
		orderItem2.setQuantityOfShare(3000);
		orderItem2.setPricePerShare(20);
		orderItem2.setPersonId("Mateusz2");
		
		OrderItem orderItem3 = new OrderItem();
		orderItem3.setFirmId("Facebook");
		orderItem3.setItemType(TypeOfOrder.BID);
		orderItem3.setQuantityOfShare(6000);
		orderItem3.setPricePerShare(20);
		orderItem3.setPersonId("Mateusz3");
		
		
		
		
		repo.getTransactionsUnrealized().add(orderItem);
		repo.getTransactionsUnrealized().add(orderItem2);
		repo.getTransactionsUnrealized().add(orderItem3);

		for(OrderItem x: repo.getPurchaseTransactions())
		{
			repo.searchTransaction(x);
		}

		assertEquals(3, repo.getTransactionsRealized().size());
		assertEquals(true, repo.getTransactionsUnrealized().isEmpty());

	}
	
	@Test
	public void buyNotAll(){
		
		OrderRepositoryImpl repo = new OrderRepositoryImpl();

		
		OrderItem orderItem = new OrderItem();
		orderItem.setFirmId("Facebook");
		orderItem.setItemType(TypeOfOrder.ASK);
		orderItem.setQuantityOfShare(3000);
		orderItem.setPricePerShare(20);
		orderItem.setPersonId("Mateusz");
		
		OrderItem orderItem2 = new OrderItem();
		orderItem2.setFirmId("Facebook");
		orderItem2.setItemType(TypeOfOrder.ASK);
		orderItem2.setQuantityOfShare(3000);
		orderItem2.setPricePerShare(20);
		orderItem2.setPersonId("Mateusz2");
		
		OrderItem orderItem3 = new OrderItem();
		orderItem3.setFirmId("Facebook");
		orderItem3.setItemType(TypeOfOrder.BID);
		orderItem3.setQuantityOfShare(6150);
		orderItem3.setPricePerShare(20);
		orderItem3.setPersonId("Mateusz3");
		
		repo.getTransactionsUnrealized().add(orderItem);
		repo.getTransactionsUnrealized().add(orderItem2);
		repo.getTransactionsUnrealized().add(orderItem3);
		
		for(OrderItem x: repo.getPurchaseTransactions())
		{
			repo.searchTransaction(x);
		}

			
		assertEquals(1, repo.getTransactionsUnrealized().size());
		assertEquals(150, repo.getTransactionsUnrealized().get(0).getQuantityOfShare());
		
	}
	
	
		@Test
		public void sellNotAll(){

			OrderRepositoryImpl repo = new OrderRepositoryImpl();

			
			OrderItem orderItem = new OrderItem();
			orderItem.setFirmId("Facebook");
			orderItem.setItemType(TypeOfOrder.ASK);
			orderItem.setQuantityOfShare(4000);
			orderItem.setPricePerShare(20);
			orderItem.setPersonId("Mateusz");
			
			OrderItem orderItem2 = new OrderItem();
			orderItem2.setFirmId("Facebook");
			orderItem2.setItemType(TypeOfOrder.ASK);
			orderItem2.setQuantityOfShare(3000);
			orderItem2.setPricePerShare(20);
			orderItem2.setPersonId("Mateusz2");
			
			OrderItem orderItem3 = new OrderItem();
			orderItem3.setFirmId("Facebook");
			orderItem3.setItemType(TypeOfOrder.BID);
			orderItem3.setQuantityOfShare(6150);
			orderItem3.setPricePerShare(20);
			orderItem3.setPersonId("Mateusz3");
			
			OrderItem orderItem4 = new OrderItem();
			orderItem4.setFirmId("Microsoft");
			orderItem4.setItemType(TypeOfOrder.BID);
			orderItem4.setQuantityOfShare(6150);
			orderItem4.setPricePerShare(20);
			orderItem4.setPersonId("Mateusz3");
			
			repo.getTransactionsUnrealized().add(orderItem);
			repo.getTransactionsUnrealized().add(orderItem2);
			repo.getTransactionsUnrealized().add(orderItem3);
			repo.getTransactionsUnrealized().add(orderItem4);

			
			for(OrderItem x: repo.getPurchaseTransactions())
			{
				repo.searchTransaction(x);
			}
			

			assertEquals(2, repo.getTransactionsUnrealized().size());
			assertEquals(850, repo.getTransactionsUnrealized().get(0).getQuantityOfShare());
			assertEquals(TypeOfOrder.BID, repo.getTransactionsUnrealized().get(1).getItemType());
		}
	
	
	

}
