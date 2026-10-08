package confirmArrivalTest;

import static org.junit.Assert.*;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import dataAccess.DataAccess;
import domain.Sale;
import domain.Seller;
import test.TestDataAccess;

public class ConfirmArrivalBDBlackTest {
	static DataAccess sut = new DataAccess();
	static TestDataAccess testDA = new TestDataAccess();

	private String sellerMail;
	private String sellerName;
	private String sellerPass;
	private String buyerMail;
	private String buyerName;
	private String buyerPass;
	private String title;
	private int status;
	private String description;
	private Date pubDate;
	private float price;
	private File file;

	@Before
	public void defaultValues() {
		sellerMail = "seller1@gmail.com";
		sellerName = "Seller Test";
		sellerPass = "pass";
		buyerMail = "buyer@ehu.eus";
		buyerName = "Buyer Test";
		buyerPass = "pass";
		title = "futbol baloia";
		description = "Ordu bat erabilita";
		status = 0;
		price = 20.0f;

		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		pubDate = null;
		try {
			pubDate = sdf.parse("05/10/2026");
		} catch (ParseException e) {
			e.printStackTrace();
		}
		file = null;
	}

	@After
	public void cleanUp() {
		try {
			testDA.open();
			testDA.removeSeller(sellerMail);
			testDA.removeSeller(buyerMail);
			testDA.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/*@Test
	public void test1() {
		try {
			testDA.open();
			Seller seller = testDA.addSellerWithSaleAndBid(sellerMail, sellerName, sellerPass, title, description, status, price, pubDate, file, buyerMail, "BIDALTZEKE");
			Sale sale = seller.getSales().get(seller.getSales().size() - 1);
			Integer saleNumber = sale.getSaleNumber();
			float moneyBefore = seller.getMoney();
			testDA.close();

			sut.open();
			boolean result = sut.confirmArrival(saleNumber);
			sut.close();

			assertTrue(result);

			testDA.open();
			Seller sellerAfter = testDA.findSeller(sellerMail);
			assertEquals(moneyBefore + price, sellerAfter.getMoney(), 0.001);
			testDA.close();
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}*/

	@Test
	public void test2() {
		try {
			sut.open();
			boolean result = sut.confirmArrival(null);
			sut.close();

			assertFalse(result);
		} catch (Exception e) {
			assertTrue(true);
		}
	}

	@Test
	public void test3() {
		try {
			sut.open();
			boolean result = sut.confirmArrival(-5);
			sut.close();

			assertFalse(result);
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}

	@Test
	public void test4() {
		try {
			sut.open();
			boolean result = sut.confirmArrival(999);
			sut.close();

			assertFalse(result);
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}

	@Test
	public void test5() {
		try {
			testDA.open();
			Seller seller = testDA.addSellerWithSale(sellerMail, sellerName, sellerPass, title, description, status, price, pubDate, file);
			Sale sale = seller.getSales().get(seller.getSales().size() - 1);
			Integer saleNumber = sale.getSaleNumber();
			testDA.close();

			sut.open();
			boolean result = sut.confirmArrival(saleNumber);
			sut.close();

			assertFalse(result);
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}

	@Test
	public void test6() {
		try {
			testDA.open();
			Seller seller = testDA.addSellerWithSaleAndBid(sellerMail, sellerName, sellerPass, title, description, status, price, pubDate, file, buyerMail, "JASOTA");
			Sale sale = seller.getSales().get(seller.getSales().size() - 1);
			Integer saleNumber = sale.getSaleNumber();
			testDA.close();

			sut.open();
			boolean result = sut.confirmArrival(saleNumber);
			sut.close();

			assertFalse(result);
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}
}