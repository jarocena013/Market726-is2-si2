package buyProductTest;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.lang.reflect.Field;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import dataAccess.DataAccess;
import domain.Sale;
import domain.Seller;

public class BuyProductMockWhiteTest {

	static DataAccess sut;
	
	protected MockedStatic<Persistence> persistenceMock;

	@Mock
	protected  EntityManagerFactory entityManagerFactory;
	@Mock
	protected  EntityManager db;
	@Mock
    protected  EntityTransaction  et;

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

		MockitoAnnotations.openMocks(this);
        persistenceMock = Mockito.mockStatic(Persistence.class);
		persistenceMock.when(() -> Persistence.createEntityManagerFactory(Mockito.any()))
        .thenReturn(entityManagerFactory);
        
        Mockito.doReturn(db).when(entityManagerFactory).createEntityManager();
		Mockito.doReturn(et).when(db).getTransaction();
	    sut=new DataAccess(db);
	}
	@After
	public void tearDown() {
	    if (persistenceMock != null) {
	        persistenceMock.close();
	        persistenceMock = null;
	    }
	}

	// Catchean sartzen da
	/*@Test
	public void test1() {

		Seller buyer = new Seller(buyerMail, buyerName, buyerPass);

		buyer.setMoney(50);

		when(db.find(Seller.class, "buyer@ehu.eus")).thenReturn(buyer);

		when(db.find(Sale.class, 99999)).thenReturn(null);


		boolean result = sut.buyProduct("buyer@ehu.eus", 1);

		assertFalse(result);

	}
	*/

	// Buyer==null
	@Test
	public void test2() {

		Seller seller = new Seller(sellerMail, sellerName, sellerPass);

		Sale sale = new Sale(title, description, status, prize, pubDate, null, seller);

		sale.setSaleNumber(1);

		when(db.find(Seller.class, "buyer@ehu.eus")).thenReturn(null);

		when(db.find(Sale.class, 1)).thenReturn(sale);



		boolean result = sut.buyProduct("buyer@ehu.eus", 1);

		assertFalse(result);


	}

	// Sale==null
	@Test
	public void test3() {

		Seller buyer = new Seller(buyerMail, buyerName, buyerPass);

		buyer.setMoney(50);

		when(db.find(Seller.class, "buyer@ehu.eus")).thenReturn(buyer);

		when(db.find(Sale.class, 1)).thenReturn(null);


		boolean result = sut.buyProduct("buyer@ehu.eus", 1);

		assertFalse(result);

	}

	// Dagoeneko erosle bat du.
	@Test
	public void test4() {

		Seller buyer = new Seller(buyerMail, buyerName, buyerPass);

		buyer.setMoney(50);

		Seller seller = new Seller(sellerMail, sellerName, sellerPass);

		Seller existingBuyer = new Seller("otro@ehu.eus", "Other Buyer", "pass");

		Sale sale = new Sale(title, description, status, prize, pubDate, null, seller);

		sale.setSaleNumber(1);
		sale.setBuyer(existingBuyer);

		when(db.find(Seller.class, "buyer@ehu.eus")).thenReturn(buyer);

		when(db.find(Sale.class, 1)).thenReturn(sale);


		boolean result = sut.buyProduct("buyer@ehu.eus", 1);

		assertFalse(result);
		assertEquals(50, buyer.getMoney(), 0.01);


	}

	// Ez du diru nahikorik
	@Test
	public void test5() {

		Seller buyer = new Seller(buyerMail, buyerName, buyerPass);

		buyer.setMoney(25);

		Seller seller = new Seller(sellerMail, sellerName, sellerPass);

		Sale sale = new Sale(title, description, status, prize, pubDate, null, seller);

		sale.setSaleNumber(1);

		when(db.find(Seller.class, "buyer@ehu.eus")).thenReturn(buyer);

		when(db.find(Sale.class, 1)).thenReturn(sale);


		boolean result = sut.buyProduct("buyer@ehu.eus", 1);

		assertFalse(result);
		assertEquals(25, buyer.getMoney(), 0.01);
		assertNull(sale.getBuyer());


	}

	// Erosketa zuzen burutzen da.

	@Test
	public void test6() {

		Seller buyer = new Seller(buyerMail, buyerName, buyerPass);

		buyer.setMoney(50);

		Seller seller = new Seller(sellerMail, sellerName, sellerPass);

		Sale sale = new Sale(title, description, status, prize, pubDate, null, seller);

		sale.setSaleNumber(1);

		when(db.find(Seller.class, "buyer@ehu.eus")).thenReturn(buyer);

		when(db.find(Sale.class, 1)).thenReturn(sale);

		boolean result = sut.buyProduct("buyer@ehu.eus", 1);

		assertTrue(result);
		assertEquals(20, buyer.getMoney(), 0.01);
		assertEquals(buyer, sale.getBuyer());
		assertTrue(buyer.getPurchasedSales().contains(sale));
	}

}
