package addToBasketTest;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.Date;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

import org.junit.*;
import org.mockito.*;

import dataAccess.DataAccess;
import domain.Sale;
import domain.Seller;

public class AddToBasketMockBlackTest {

    @Mock EntityManager db;
    @Mock EntityTransaction tx;

    DataAccess sut;

    
    Seller buyer, seller, other;
    Sale sale1, sale2;

    final String buyerMail = "proba@ehu.eus";

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        when(db.getTransaction()).thenReturn(tx);
        sut = new DataAccess(db);           

        buyer  = new Seller(buyerMail, "Buyer", "pass");
        seller = new Seller("seller@ehu.eus", "Seller", "pass");
        other  = new Seller("other@ehu.eus", "Other", "pass");

        sale1 = new Sale("futbol baloia", "desc", 1, 10f, new Date(), null, seller);
        sale1.setSaleNumber(1);
        sale2 = new Sale("sale2", "desc", 1, 10f, new Date(), null, seller);
        sale2.setSaleNumber(2);

       
        when(db.find(Seller.class, buyerMail)).thenReturn(buyer);
        when(db.find(Sale.class, 1)).thenReturn(sale1);
    }

    @Test // 1: datu baliozkoak, saski hutsa
    public void test1() {
        assertTrue(sut.addToBasket(buyerMail, 1));

        assertEquals(1, buyer.getBasket().size());
        assertTrue(buyer.getBasket().contains(sale1));
    }

    @Test // 2: saski ez-hutsa, saltzaile bera
    public void test2() {
        buyer.getBasket().add(sale1);
        when(db.find(Sale.class, 2)).thenReturn(sale2);   // sale2: mismo vendedor

        assertTrue(sut.addToBasket(buyerMail, 2));

        assertEquals(2, buyer.getBasket().size());
        assertTrue(buyer.getBasket().contains(sale2));
    }

    @Test // 3: erabiltzailea ez dago DB-n
    public void test3() {
        
        assertFalse(sut.addToBasket("noexiste@ehu.eus", 1));
    }

    @Test // 4: email null
    public void test4() {
        
        when(db.find(eq(Seller.class), isNull())).thenThrow(new IllegalArgumentException());

        assertFalse(sut.addToBasket(null, 1));
    }

    @Test // 5: sale ez dago (-1)
    public void test5() {
        assertFalse(sut.addToBasket(buyerMail, -1));

        assertTrue(buyer.getBasket().isEmpty());
    }

    @Test // 6: sale ez dago (muga-balioa)
    public void test6() {
        assertFalse(sut.addToBasket(buyerMail, Integer.MAX_VALUE));

        assertTrue(buyer.getBasket().isEmpty());
    }

    @Test // 7: saleNumber null
    public void test7() {
        when(db.find(eq(Sale.class), isNull())).thenThrow(new IllegalArgumentException());

        assertFalse(sut.addToBasket(buyerMail, null));

        assertTrue(buyer.getBasket().isEmpty());
    }

    @Test // 8: sale-k badauka buyer
    public void test8() {
        sale1.setBuyer(other);

        assertFalse(sut.addToBasket(buyerMail, 1));

        assertTrue(buyer.getBasket().isEmpty());
    }

    @Test // 9: sale saskian dago jada
    public void test9() {
        buyer.getBasket().add(sale1);

        assertFalse(sut.addToBasket(buyerMail, 1));

        assertEquals(1, buyer.getBasket().size());
    }

    @Test // 10: saltzaile desberdina
    public void test10() {
        buyer.getBasket().add(sale1);                      // saltzailea: seller
        Sale besteSale = new Sale("x", "d", 1, 5f, new Date(), null, other);
        besteSale.setSaleNumber(3);
        when(db.find(Sale.class, 3)).thenReturn(besteSale);

        assertFalse(sut.addToBasket(buyerMail, 3));

        assertEquals(1, buyer.getBasket().size());
        assertFalse(buyer.getBasket().contains(besteSale));
    }
}