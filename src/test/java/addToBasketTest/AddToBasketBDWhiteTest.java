package addToBasketTest;

import static org.junit.Assert.*;


import java.util.ArrayList;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import dataAccess.DataAccess;
import domain.Sale;

import test.TestDataAccess;

public class AddToBasketBDWhiteTest {
	
	static DataAccess sut ;
	static TestDataAccess testDA;
	
	
	private String buyerMail;
	private String otherMail;
    private String buyerName;
    private String buyerPass;
    private String sellerMail;
   
    private String title;
    
    private int saleNumber;
    private List<Integer> createdSales = new ArrayList<>();
    
	
	@Before
    public  void defaultValues() {
		
		sut = new DataAccess();
		sut.open();
		testDA = new TestDataAccess();
		testDA.open();
		
		otherMail = "other@proba.com";
		buyerMail = "proba@ehu.eus";
		buyerName = "Seller Test";
		buyerPass = "pass";
		sellerMail = "seller@ehu.eus";
        
		title = "futbol baloia";
		

		
		testDA.createSeller(buyerMail, buyerName, buyerPass);
		saleNumber = newSale(sellerMail, title);

		

	}
    
	@After
	public void cleanUp() {
	    try {
	        testDA.open();

	        testDA.removeSeller(buyerMail);
	        testDA.removeSeller(sellerMail);
	        for(Integer n : createdSales) {
	        	testDA.removeSaleByNumber(n);  	
	        }
	        createdSales.clear();
	        testDA.removeSeller(sellerMail);
	        testDA.removeSeller(buyerMail);
	        
	        testDA.close();
	        sut.close();

	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}
	
	private int newSale(String email, String title) {
        int n = testDA.createSale(email, title);
        createdSales.add(n);
        return n;
    }
	
	

	@Test
	//null parametroak pasatzen dira
	public void test1() {
		assertFalse(sut.addToBasket(null, null));
		
	}
	
	@Test
	//buyer ez dago DB-n
	public void test2() {
		assertFalse(sut.addToBasket(buyerMail, saleNumber));
	}
	
	@Test
	//sale ez dago DB-n
	public void test3() {
		assertFalse(sut.addToBasket(buyerMail, -1));
		
	}
	
	@Test
	//sale-k badauka buyer bat dagoeneko
	public void test4() {
		Sale s = testDA.findSale(saleNumber);
		s.setBuyer(testDA.findSeller(buyerMail));
		testDA.updateSale(s);
		
		assertTrue(sut.addToBasket(buyerMail, saleNumber));
	}
	@Test
	//sale dagoeneko saskian dago
	public void test5() {
		testDA.addToBasket(buyerMail, saleNumber);
		
		
		assertFalse(sut.addToBasket(buyerMail, saleNumber));
		
	}
	
	@Test 
	//Saskia hutsik dago
	public void test6() {
		assertTrue(sut.addToBasket(buyerMail, saleNumber));
		assertEquals(1,testDA.getBasket(buyerMail).size());
	}
	
	@Test
	//Saskia ez hutsa eta saltzaile ezberdinak
	public void test7() {
		testDA.addToBasket(buyerMail, saleNumber);
		int sale2 = newSale(otherMail,"sale2");
		
		assertFalse(sut.addToBasket(buyerMail, sale2));
		assertEquals(1,testDA.getBasket(buyerMail).size());
	}
	
	@Test
	//saskia ez hutsa eta saltzaile berdinak
	public void test8() {
		testDA.addToBasket(buyerMail, saleNumber);
		int sale2 = newSale(sellerMail,"sale2");
		
		assertTrue(sut.addToBasket(buyerMail, sale2));
		assertEquals(2,testDA.getBasket(buyerMail).size());
		
	}

}
