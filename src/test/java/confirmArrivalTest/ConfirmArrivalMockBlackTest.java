package confirmArrivalTest;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

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
import domain.Bidalketa;
import domain.Sale;
import domain.Seller;

public class ConfirmArrivalMockBlackTest {

	static DataAccess sut;

	protected MockedStatic<Persistence> persistenceMock;

	@Mock
	protected EntityManagerFactory entityManagerFactory;
	@Mock
	protected EntityManager db;
	@Mock
	protected EntityTransaction et;

	private String sellerMail;
	private String sellerName;
	private String sellerPass;
	private String title;
	private int status;
	private String description;
	private float price;

	@Before
	public void setUp() {
		sellerMail = "seller1@gmail.com";
		sellerName = "Seller Test";
		sellerPass = "pass";
		title = "futbol baloia";
		description = "Ordu bat erabilita";
		status = 0;
		price = 20.0f;

		MockitoAnnotations.openMocks(this);
		persistenceMock = Mockito.mockStatic(Persistence.class);
		persistenceMock.when(() -> Persistence.createEntityManagerFactory(Mockito.any()))
				.thenReturn(entityManagerFactory);

		Mockito.doReturn(db).when(entityManagerFactory).createEntityManager();
		Mockito.doReturn(et).when(db).getTransaction();
		sut = new DataAccess(db);
	}

	@After
	public void tearDown() {
		if (persistenceMock != null) {
			persistenceMock.close();
			persistenceMock = null;
		}
	}

	/*@Test
	public void test1() {
		Integer saleNumber = 1;
		Seller seller = new Seller(sellerMail, sellerName, sellerPass);
		seller.setMoney(0);
		
		Sale sale = new Sale(title, description, status, price, null, null, seller);
		sale.setSaleNumber(saleNumber);
		
		Bidalketa bid = new Bidalketa();
		bid.setEgoera("BIDALTZEKE");
		sale.setBidalketa(bid);

		when(db.find(Sale.class, saleNumber)).thenReturn(sale);

		boolean result = sut.confirmArrival(saleNumber);

		assertTrue(result);
		assertEquals(20.0f, seller.getMoney(), 0.01);
		verify(et).begin();
		verify(et).commit();
	}

	@Test
	public void test2() {
		boolean result = sut.confirmArrival(null);
		assertFalse(result);
	}

	@Test
	public void test3() {
		boolean result = sut.confirmArrival(-5);
		assertFalse(result);
	}

	@Test
	public void test4() {
		Integer saleNumber = 999;
		when(db.find(Sale.class, saleNumber)).thenReturn(null);

		boolean result = sut.confirmArrival(saleNumber);
		assertFalse(result);
	}

	@Test
	public void test5() {
		Integer saleNumber = 1;
		Seller seller = new Seller(sellerMail, sellerName, sellerPass);
		Sale sale = new Sale(title, description, status, price, null, null, seller);
		sale.setSaleNumber(saleNumber);
		sale.setBidalketa(null);

		when(db.find(Sale.class, saleNumber)).thenReturn(sale);

		boolean result = sut.confirmArrival(saleNumber);
		assertFalse(result);
	}

	@Test
	public void test6() {
		Integer saleNumber = 1;
		Seller seller = new Seller(sellerMail, sellerName, sellerPass);
		Sale sale = new Sale(title, description, status, price, null, null, seller);
		sale.setSaleNumber(saleNumber);
		
		Bidalketa bid = new Bidalketa();
		bid.setEgoera("JASOTA");
		sale.setBidalketa(bid);

		when(db.find(Sale.class, saleNumber)).thenReturn(sale);

		boolean result = sut.confirmArrival(saleNumber);
		assertFalse(result);
	}*/
}