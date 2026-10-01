package buyProductTest;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

import org.junit.Before;
import org.junit.Test;

import dataAccess.DataAccess;
import domain.Sale;
import domain.Seller;

public class BuyProductMockBlackTest {

	private DataAccess sut;
	private EntityManager db;
	private EntityTransaction transaction;

	private String buyerMail;
	private String buyerName;
	private String buyerPass;

	private String sellerMail;
	private String sellerName;
	private String sellerPass;

	private String title;
	private int status;
	private String description;
	private Date pubDate;
	private float prize;

	@Before
	public void setUp() {

		buyerMail = "proba@ehu.eus";
		buyerName = "Buyer Test";
		buyerPass = "pass";

		sellerMail = "seller@ehu.eus";
		sellerName = "Seller Test";
		sellerPass = "pass";

		title = "futbol baloia";
		description = "Used one hour";
		status = 0;
		prize = 30;

		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

		try {
			pubDate = sdf.parse("05/10/2026");
		} catch (ParseException e) {
			e.printStackTrace();
		}

		db = mock(EntityManager.class);
		transaction = mock(EntityTransaction.class);

		when(db.getTransaction()).thenReturn(transaction);

		sut = new DataAccess(db);
	}

	/*
	 * Erosketa ongi burutzen da
	 */
	@Test
	public void test1() {

		Seller buyer = new Seller(buyerMail, buyerName, buyerPass);

		buyer.setMoney(50);

		Seller seller = new Seller(sellerMail, sellerName, sellerPass);

		Sale sale = new Sale(title, description, status, prize, pubDate, null, seller);

		sale.setSaleNumber(1);

		when(db.find(Seller.class, buyerMail)).thenReturn(buyer);

		when(db.find(Sale.class, 1)).thenReturn(sale);

		boolean result = sut.buyProduct(buyerMail, 1);

		assertTrue(result);
		assertEquals(20, buyer.getMoney(), 0.01);
	}

	/*
	 * Erosleak ez du diru nahikorik
	 */
	@Test
	public void test2() {

		Seller buyer = new Seller(buyerMail, buyerName, buyerPass);

		buyer.setMoney(25);

		Seller seller = new Seller(sellerMail, sellerName, sellerPass);

		Sale sale = new Sale(title, description, status, prize, pubDate, null, seller);

		sale.setSaleNumber(1);

		when(db.find(Seller.class, buyerMail)).thenReturn(buyer);

		when(db.find(Sale.class, 1)).thenReturn(sale);

		boolean result = sut.buyProduct(buyerMail, 1);

		assertFalse(result);
		assertEquals(25, buyer.getMoney(), 0.01);
	}

	/*
	 * buyerMail==null
	 */
	@Test
	public void test3() {

		Seller seller = new Seller(sellerMail, sellerName, sellerPass);

		Sale sale = new Sale(title, description, status, prize, pubDate, null, seller);

		sale.setSaleNumber(1);

		when(db.find(Seller.class, null)).thenReturn(null);

		when(db.find(Sale.class, 1)).thenReturn(sale);

		boolean result = sut.buyProduct(null, 1);

		assertFalse(result);
	}

	/*
	 * buyerEmail formatu ez egokian
	 */
	@Test
	public void test4() {

		String invalidEmail = "a";

		Seller seller = new Seller(sellerMail, sellerName, sellerPass);

		Sale sale = new Sale(title, description, status, prize, pubDate, null, seller);

		sale.setSaleNumber(1);

		when(db.find(Seller.class, invalidEmail)).thenReturn(null);

		when(db.find(Sale.class, 1)).thenReturn(sale);

		boolean result = sut.buyProduct(invalidEmail, 1);

		assertFalse(result);
	}

	/*
	 * buyer ez dago datubasean
	 */
	@Test
	public void test5() {

		String nonexistentBuyer = "compradorInexistente999@gmail.com";

		Seller seller = new Seller(sellerMail, sellerName, sellerPass);

		Sale sale = new Sale(title, description, status, prize, pubDate, null, seller);

		sale.setSaleNumber(1);

		when(db.find(Seller.class, nonexistentBuyer)).thenReturn(null);

		when(db.find(Sale.class, 1)).thenReturn(sale);

		boolean result = sut.buyProduct(nonexistentBuyer, 1);

		assertFalse(result);
	}

	/*
	 * sale-aren identifikatzailea 0 baino txikiagoa
	 */
	@Test
	public void test6() {

		Seller buyer = new Seller(buyerMail, buyerName, buyerPass);

		buyer.setMoney(50);

		when(db.find(Seller.class, buyerMail)).thenReturn(buyer);

		when(db.find(Sale.class, 0)).thenReturn(null);

		boolean result = sut.buyProduct(buyerMail, 0);

		assertFalse(result);
	}

	/*
	 * Sale ez dago DB-an jasota.
	 */
	@Test
	public void test7() {

		Seller buyer = new Seller(buyerMail, buyerName, buyerPass);

		buyer.setMoney(50);

		when(db.find(Seller.class, buyerMail)).thenReturn(buyer);

		when(db.find(Sale.class, 99999)).thenReturn(null);

		boolean result = sut.buyProduct(buyerMail, 99999);

		assertFalse(result);
	}
}
