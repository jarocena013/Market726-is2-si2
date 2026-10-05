package addToBasketTest;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.when;

import java.util.Date;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.MockitoAnnotations;

import dataAccess.DataAccess;
import domain.Sale;
import domain.Seller;

public class AddToBasketMockWhiteTest {
	
	static DataAccess sut;
	
	protected MockedStatic<Persistence> persistenceMock;
	
	@Mock
	protected  EntityManagerFactory entityManagerFactory;
	@Mock
	protected  EntityManager db;
	@Mock
    protected  EntityTransaction  et;
	
	private Seller buyer, seller, bestea;
	private Sale sale1, sale2;
	private String buyerMail = "proba@ehu.eus";
	
	@Before
	public void setUp() {
		MockitoAnnotations.openMocks(this);
		when(db.getTransaction()).thenReturn(et);
		sut = new DataAccess(db);
		
		buyer  = new Seller(buyerMail, "Buyer", "pass");
        seller = new Seller("seller@ehu.eus", "Seller", "pass");
        bestea  = new Seller("other@ehu.eus", "Bestea", "pass");

        sale1 = new Sale("futbol baloia", "desc", 1, 10f, new Date(), null, seller);
        sale1.setSaleNumber(1);
        sale2 = new Sale("sale2", "desc", 1, 10f, new Date(), null, seller);
        sale2.setSaleNumber(2);
        
        when(db.find(Seller.class, buyerMail)).thenReturn(buyer);
        when(db.find(Sale.class, 1)).thenReturn(sale1);
	}

	@Test
	//null parametroak
	public void test1() {
		when(db.find(eq(Seller.class), isNull())).thenThrow(new IllegalArgumentException());
		assertFalse(sut.addToBasket(null, null));
	}
	
	@Test
	//buyer ez dago DB-n
	public void test2() {
		assertFalse(sut.addToBasket(buyerMail, 1));
	}
	
	@Test
	//sale ez dago DB-n
	public void test3() {
		assertFalse(sut.addToBasket(buyerMail, -1));
		
	}
	
	@Test
	//salek badauka buyer dagoeneko
	public void test4() {
		sale1.setBuyer(bestea);
		
		assertFalse(sut.addToBasket(buyerMail, 1));
		
	}
	
	@Test
	//sale dagoeneko saskian dago
	public void test5() {
		buyer.getBasket().add(sale1);
		assertFalse(sut.addToBasket(buyerMail, 1));
	}
	@Test
	//saski hutsa
	public void test6(){
		assertTrue(sut.addToBasket(buyerMail, 1));
		assertEquals(1,buyer.getBasket().size());
		assertTrue(buyer.getBasket().contains(sale1));
		
	}
	
	@Test
	//saskian saltzaile ezberdinak
	public void test7() {
		buyer.getBasket().add(sale1);
		
		Sale besteSale = new Sale("x","d",1,5f, new Date(), null, bestea);
		besteSale.setSaleNumber(3);
		when(db.find(Sale.class,3)).thenReturn(besteSale);
		
		assertFalse(sut.addToBasket(buyerMail, 3));
	}
	
	@Test 
	// saski ez-hutsa, saltzaile bera
    public void test8() {
        buyer.getBasket().add(sale1);
        when(db.find(Sale.class, 2)).thenReturn(sale2);    // sale2: mismo vendedor

        assertTrue(sut.addToBasket(buyerMail, 2));

        assertEquals(2, buyer.getBasket().size());
        assertTrue(buyer.getBasket().contains(sale2));
    }

}
