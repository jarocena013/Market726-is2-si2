package buyProductTest;

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

public class BuyProductBDBlackTest {
	static DataAccess sut = new DataAccess();

	static TestDataAccess testDA = new TestDataAccess();

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
	private File file;

	@Before
	public void defaultValues() {

		buyerMail = "proba@ehu.eus";
		buyerName = "Seller Test";
		buyerPass = "pass";
		sellerMail = "seller@ehu.eus";
		sellerName = "Seller Test";
		sellerPass = "pass";
		title = "futbol baloia";
		description = "Used one hour";
		status = 0;

		prize = 30;

		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

		pubDate = null;

		try {
			pubDate = sdf.parse("05/10/2026");
		} catch (ParseException e) {
			e.printStackTrace();
		}

		file = new File("file");
	}

	@After
	public void cleanUp() {
		try {
			testDA.open();
			testDA.removeSeller(buyerMail);
			testDA.removeSeller(sellerMail);
			testDA.removeSale(sellerMail, title, description, status, prize, pubDate, file);
			testDA.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// Erosketa egiterako orduan, beharrezko elementu guztiak existitzen dira eta
	// erosketa ongi burutzen da.
	@Test
	public void test1() {
		try {

			testDA.open();
			Seller buyer = testDA.createSellerWithMoney(buyerMail, buyerName, buyerPass, 50);

			Seller seller = testDA.addSellerWithSale(sellerMail, sellerName, sellerPass, title, description, status,
					prize, pubDate, file);

			Sale sale = seller.getSales().get(0);
			Integer saleNumber = sale.getSaleNumber();

			testDA.close();

			sut.open();

			boolean result = sut.buyProduct(buyerMail, saleNumber);

			sut.close();

			assertTrue(result);
			testDA.open();

			Seller buyerAfter = testDA.findSeller(buyerMail);

			assertNotNull(buyerAfter);

			assertEquals(20, buyerAfter.getMoney(), 0.001);

			testDA.close();

		} catch (Exception e) {

			e.printStackTrace();
			fail();
		}
	}

	// Erosketa egiterako orduan, beharrezko elementu guztiak existitzen dira,
	// baina eroslearen dirua sale-aren prize baino txikiago da, beraz ez da
	// burutzen erosketa.
	@Test
	public void test2() {
		try {

			testDA.open();

			Seller buyer = testDA.createSellerWithMoney(buyerMail, buyerName, buyerPass, 25);

			Seller seller = testDA.addSellerWithSale(sellerMail, sellerName, sellerPass, title, description, status,
					prize, pubDate, file);

			Sale sale = seller.getSales().get(0);
			Integer saleNumber = sale.getSaleNumber();

			testDA.close();

			sut.open();

			boolean result = sut.buyProduct(buyerMail, saleNumber);

			sut.close();

			assertFalse(result);

			testDA.open();

			Seller buyerAfter = testDA.findSeller(buyerMail);

			assertNotNull(buyerAfter);

			assertEquals(25, buyerAfter.getMoney(), 0.001);

			testDA.close();

		} catch (Exception e) {

			e.printStackTrace();
			fail();
		}
	}
/*
	// buyerMail parametroa null da.
	@Test
	public void test3() {
		try {
			testDA.open();

			Seller seller = testDA.addSellerWithSale(sellerMail, sellerName, sellerPass, title, description, status,
					prize, pubDate, file);

			Sale sale = seller.getSales().get(0);
			Integer saleNumber = sale.getSaleNumber();

			testDA.close();

			sut.open();

			boolean result = sut.buyProduct(null, saleNumber);

			sut.close();

			assertFalse(result);

		} catch (Exception e) {
			e.printStackTrace();
			fail("Error en test3: " + e.getMessage());
		}
	}
	*/
/*
	// buyerMail parametroak ez du behar den patroia jarraitzen.
	@Test
	public void test4() {
		try {
			testDA.open();

			Seller seller = testDA.addSellerWithSale(sellerMail, sellerName, sellerPass, title, description, status,
					prize, pubDate, file);

			Sale sale = seller.getSales().get(0);
			Integer saleNumber = sale.getSaleNumber();

			testDA.close();

			sut.open();

			boolean result = sut.buyProduct("a", saleNumber);

			sut.close();

			assertFalse(result);

		} catch (Exception e) {
			e.printStackTrace();
			fail("Error en test4: " + e.getMessage());
		}
	}
	*/
/*
	// buyer ez da datu basean existitzen.
	@Test
	public void test5() {
		try {
			testDA.open();

			// Creamos una venta válida
			Seller seller = testDA.addSellerWithSale(sellerMail, sellerName, sellerPass, title, description, status,
					prize, pubDate, file);

			Sale sale = seller.getSales().get(0);
			Integer saleNumber = sale.getSaleNumber();

			testDA.close();

			// Email correcto, pero el comprador no existe
			sut.open();

			boolean result = sut.buyProduct("compradorInexistente999@gmail.com", saleNumber);

			sut.close();

			assertFalse(result);

		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}
	*/
/*
	// saleNumber balioa 0 baino txikiagoa edo berdina.
	@Test
	public void test6() {
		buyerMail = "a";
		try {
			testDA.open();

			// Comprador válido
			testDA.createSellerWithMoney(buyerMail, buyerName, buyerPass, 30);

			testDA.close();
			sut.open();

			boolean result = sut.buyProduct(buyerMail, 0);

			sut.close();
			assertFalse(result);

		} catch (Exception e) {
			fail();
		}
	}
	*/
	/*

	// saleNumber egokia baina ez da existitzen DBan..
	@Test
	public void test7() {
		buyerMail = "a";
		try {
			testDA.open();

			// Comprador válido
			testDA.createSellerWithMoney(buyerMail, buyerName, buyerPass, 30);

			testDA.close();
			sut.open();

			boolean result = sut.buyProduct(buyerMail, 99999);

			sut.close();
			assertFalse(result);

		} catch (Exception e) {
			fail();
		}
	}
	*/

}
