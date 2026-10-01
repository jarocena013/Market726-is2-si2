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

public class BuyProductBDWhiteTest {

	static DataAccess sut = new DataAccess();

	// additional operations needed to execute the test
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

		prize =30;

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

	        testDA.close();

	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}


	@Test
	public void test1() {

		/*
		 * Aquí necesitamos crear una venta cuyo precio sea null.
		 *
		 * No podemos hacer:
		 *
		 * prize = null;
		 *
		 * y después pasar prize a addSellerWithSale()
		 * si ese método recibe float.
		 */

	}


	@Test
	public void test2() {

		try {

			testDA.open();

			testDA.addSellerWithSale(
					"seller@ehu.eus",
					"Seller Test",
					"pass",
					title,
					description,
					status,
					prize,
					pubDate,
					file
					);

			testDA.close();

			sut.open();

			boolean result = sut.buyProduct(
					buyerMail,
					1
					);

			sut.close();

			assertFalse(result);

		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}


	/*
	 * T3
	 * sale == null
	 */
	@Test
	public void test3() {

		try {

			testDA.open();

			testDA.createSeller(
					buyerMail,
					buyerName,
					buyerPass
					);

			testDA.close();

			sut.open();


			boolean result = sut.buyProduct(
					buyerMail,
					2
					);

			sut.close();

			assertFalse(result);

		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}


	/*
	 * T4
	 * sale.getBuyer() != null
	 */
	@Test
	public void test4() {

		try {

			testDA.open();

			// Creamos comprador
			Seller buyer = testDA.createSeller(
					buyerMail,
					buyerName,
					buyerPass
					);

			// Creamos vendedor + venta
			Seller seller = testDA.addSellerWithSale(
					sellerMail,
					sellerName,
					sellerPass,
					title,
					description,
					status,
					prize,
					pubDate,
					file
					);

			Sale sale = seller.getSales().get(0);

			// La venta YA tiene comprador
			sale.setBuyer(buyer);

			Integer saleNumber = sale.getSaleNumber();

			/*
			 * Como testDA tiene la entidad abierta, hacemos commit
			 * de la modificación.
			 */
			testDA.updateSale(sale);

			testDA.close();

			sut.open();

			boolean result = sut.buyProduct(
					buyerMail,
					saleNumber
					);

			sut.close();

			assertFalse(result);

		} catch (Exception e) {

			e.printStackTrace();
			fail();
		}

	}


	/*
	 * T5
	 * buyer.money < sale.price
	 */
	@Test
	public void test5() {

		try {

			testDA.open();

			// Creamos comprador
			Seller buyer = testDA.createSeller(
					buyerMail,
					buyerName,
					buyerPass
					);

			// Le damos 25 €
			buyer.setMoney(25);

			testDA.updateSeller(buyer);

			// Creamos vendedor + venta de 30 €
			Seller seller = testDA.addSellerWithSale(
					sellerMail,
					sellerName,
					sellerPass,
					title,
					description,
					status,
					prize,
					pubDate,
					file
					);

			Sale sale = seller.getSales().get(0);
			Integer saleNumber = sale.getSaleNumber();

			testDA.close();

			sut.open();

			boolean result = sut.buyProduct(
					buyerMail,
					saleNumber
					);

			sut.close();

			assertFalse(result);

			// Comprobamos que el dinero no ha cambiado
			testDA.open();

			Seller buyerAfter =
					testDA.findSeller(buyerMail);

			assertNotNull(buyerAfter);

			assertEquals(
					25,
					buyerAfter.getMoney(),
					0.001
					);

			testDA.close();

		} catch (Exception e) {

			e.printStackTrace();
			fail();
		}

	}


	/*
	 * T6
	 * buyer.money >= sale.price
	 */
	@Test
	public void test6() {

		try {

			testDA.open();

			// Creamos comprador
			Seller buyer = testDA.createSellerWithMoney(
					buyerMail,
					buyerName,
					buyerPass,50
					);

			// El comprador tiene 50 €
			


			// Creamos vendedor + venta de 30 €
			Seller seller = testDA.addSellerWithSale(
					sellerMail,
					sellerName,
					sellerPass,
					title,
					description,
					status,
					prize,
					pubDate,
					file
					);

			Sale sale = seller.getSales().get(0);
			Integer saleNumber = sale.getSaleNumber();

			testDA.close();

			// Ejecutamos compra
			sut.open();

			boolean result = sut.buyProduct(
					buyerMail,
					saleNumber
					);

			sut.close();

			// Debe devolver true
			assertTrue(result);

		} catch (Exception e) {

			e.printStackTrace();
			fail();
		}
	}
}
