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

public class AddToBasketBDBlackTest {
	
	DataAccess sut;
	TestDataAccess testDA;
	
	 private final String buyerMail  = "proba@ehu.eus";
	 private final String sellerMail = "seller@ehu.eus";
	 private final String otherMail  = "other@ehu.eus";

	 private int saleNumber;
	 private List<Integer> createdSales = new ArrayList<>();
	 
	 @Before
	 public void defaultValues() {
		 sut=new DataAccess();
		 sut.open();
		 testDA = new TestDataAccess();
		 testDA.open();
		 
		 testDA.createSeller(buyerMail, "buyer", "pass");
		 saleNumber = newSale(sellerMail, "futbol baloia");
	 }
	 
	 @After
	    public void cleanUp() {
	        testDA.removeSeller(buyerMail);
	        for (Integer n : createdSales)
	            testDA.removeSaleByNumber(n);
	        createdSales.clear();
	        testDA.removeSeller(sellerMail);
	        testDA.removeSeller(otherMail);

	        testDA.close();
	        sut.close();
	    }
	 
	 private int newSale(String email, String title) {
	        int n = testDA.createSale(email, title);
	        createdSales.add(n);
	        return n;
	    }

	@Test
	//Balio egokiak (saski hutsa)
	public void test1() {
		assertTrue(sut.addToBasket(buyerMail, saleNumber));
        assertEquals(1, testDA.getBasket(buyerMail).size());
	}
	
	@Test
	//sale guztiak saltzaile bera
	public void test2() {
		testDA.addToBasket(buyerMail, saleNumber);
        int bigarrena = newSale(sellerMail, "sale2");

        assertTrue(sut.addToBasket(buyerMail, bigarrena));
        assertEquals(2, testDA.getBasket(buyerMail).size());
	}
	
	@Test
	//erabiltzailea ez dago DB-n
	public void test3() {
		assertFalse(sut.addToBasket("ezdaexistitzen@ehu.eus", saleNumber));
	}
	
	 @Test 
	 //email null
	public void test4() {
	   assertFalse(sut.addToBasket(null, saleNumber));
	   
	}
	 
	 @Test // 5: sale ez dago (-1)
	    public void test5() {
	        assertFalse(sut.addToBasket(buyerMail, -1));
	        assertTrue(testDA.getBasket(buyerMail).isEmpty());
	    }
	 
	 @Test // 6: sale ez dago (muga-balioa)
	    public void test6() {
	        assertFalse(sut.addToBasket(buyerMail, Integer.MAX_VALUE));
	        assertTrue(testDA.getBasket(buyerMail).isEmpty());
	    }
	 
	 @Test // 7: saleNumber null
	    public void test7() {
	        assertFalse(sut.addToBasket(buyerMail, null));
	        assertTrue(testDA.getBasket(buyerMail).isEmpty());
	    }
	 
	 @Test // 8: sale-k badauka buyer
	    public void test8() {
	        Sale s = testDA.findSale(saleNumber);
	        s.setBuyer(testDA.findSeller(sellerMail));
	        testDA.updateSale(s);

	        assertFalse(sut.addToBasket(buyerMail, saleNumber));
	        assertTrue(testDA.getBasket(buyerMail).isEmpty());
	    }
	 
	 @Test // 9: sale saskian dago dagoeneko
	    public void test9() {
	        testDA.addToBasket(buyerMail, saleNumber);

	        assertFalse(sut.addToBasket(buyerMail, saleNumber));
	        assertEquals(1, testDA.getBasket(buyerMail).size());
	    }
	 
	 @Test // 10: saltzaile desberdina
	    public void test10() {
	        testDA.addToBasket(buyerMail, saleNumber);
	        int otra = newSale(otherMail, "sale2");

	        assertFalse(sut.addToBasket(buyerMail, otra));
	        assertEquals(1, testDA.getBasket(buyerMail).size());

	        // transakzioa irekita geratu ez dela egiaztatzeko
	        int baliozkoa = newSale(sellerMail, "sale3");
	        assertTrue(sut.addToBasket(buyerMail, baliozkoa));
	    }

}
