package test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

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

import dataAccess.CreateSaleParameter;
import dataAccess.DataAccess;
import domain.AddSaleParameter;
import domain.Sale;
import domain.Seller;
import exceptions.MustBeLaterThanTodayException;
import exceptions.ParamNullException;
import exceptions.SaleAlreadyExistException;

public class CreateSaleMockBlackTest {
	
	static DataAccess sut;
	
	protected MockedStatic<Persistence> persistenceMock;

	@Mock
	protected EntityManagerFactory entityManagerFactory;
	@Mock
	protected EntityManager db;
	@Mock
    protected EntityTransaction et;
	
	private Seller seller; 
	private String sellerMail;
	private String sellerName;
	private String sellerPass;
	private String title;
	private String description;
	private int status;
	private float price;
	private Date pubDate;
	
	
	@Before
    public void init() {
        MockitoAnnotations.openMocks(this);
        persistenceMock = Mockito.mockStatic(Persistence.class);
		persistenceMock.when(() -> Persistence.createEntityManagerFactory(Mockito.any()))
        .thenReturn(entityManagerFactory);
        
        Mockito.doReturn(db).when(entityManagerFactory).createEntityManager();
		Mockito.doReturn(et).when(db).getTransaction();
	    sut=new DataAccess(db);
	    
	    sellerMail="sellerTest@ehu.eus";
		sellerName="Seller Test";
		sellerPass="pass";
		seller=new Seller(sellerMail,sellerName,sellerPass);
		title="futbol baloia";
		description="Used one hour";
		status=0;
		price=10;
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		pubDate=null;
		try {
			pubDate = sdf.parse("05/10/2026");
		} catch (ParseException e) {
			e.printStackTrace();
		}	
	    
        Mockito.when(db.find(Seller.class, seller.getEmail())).thenReturn(seller);

    }
	@After
    public void tearDown() {
		persistenceMock.close();
    }
	
	@Test
	public void test1() {
		try {
			sut.open();
			Sale sale=sut.createSale(new CreateSaleParameter(title, description, status, price, pubDate, sellerMail, null));
			sut.close();			
			assertNotNull(sale);
			assertEquals(sale.getTitle(),title);
			assertEquals(sale.getDescription(),description);
			assertEquals(sale.getStatus(),status);
			
		} catch (ParamNullException | SaleAlreadyExistException | MustBeLaterThanTodayException e ) { 
			e.printStackTrace();
		    System.out.println("Error: " + e.getMessage());
			fail();
		} catch (Exception e) {
			fail();
		} 
	}

	@Test
	public void test2() {
		title= null;
		try {
			sut.open();
			sut.createSale(new CreateSaleParameter(title, description, status, price, pubDate, sellerMail, null));
			sut.close();			
			fail("The sale must not be created");
			
		} catch (ParamNullException e ) { 
			assertTrue(true);
		} catch ( SaleAlreadyExistException | MustBeLaterThanTodayException e ) { 
			e.printStackTrace();
		    System.out.println("Error: " + e.getMessage());
			fail();
		} catch (Exception e) {
			fail();
		} 
	}
	@Test
	public void test3() {
		title= "";
		try {
			sut.open();
			Sale s=sut.createSale(new CreateSaleParameter(title, description, status, price, pubDate, sellerMail, null));
			sut.close();
			assertNull(s);
			
		} catch (ParamNullException | SaleAlreadyExistException | MustBeLaterThanTodayException e ) { 
			e.printStackTrace();
		    System.out.println("Error: " + e.getMessage());
			fail();
		} catch (Exception e) {
			fail();
		} 
	}
	@Test
	public void test4() {
		title= "1234";
		try {
			sut.open();
			Sale s=sut.createSale(new CreateSaleParameter(title, description, status, price, pubDate, sellerMail, null));
			sut.close();
			assertTrue(s==null);
			
		} catch (ParamNullException | SaleAlreadyExistException | MustBeLaterThanTodayException e ) { 
			e.printStackTrace();
		    System.out.println("Error: " + e.getMessage());
			fail();
		} catch (Exception e) {
			fail();
		} 
	}
	
	@Test
	public void test5() {
		description= null;
		try {
			sut.open();
			sut.createSale(new CreateSaleParameter(title, description, status, price, pubDate, sellerMail, null));
			sut.close();			
			fail("The sale must not be created");
			
		} catch (ParamNullException e ) { 
			assertTrue(true);
		} catch ( SaleAlreadyExistException | MustBeLaterThanTodayException e ) { 
			e.printStackTrace();
		    System.out.println("Error: " + e.getMessage());
			fail();
		} catch (Exception e) {
			fail();
		} 
	}	
	
	@Test
	public void test6() {
		description= "";
		try {
			sut.open();
			Sale s=sut.createSale(new CreateSaleParameter(title, description, status, price, pubDate, sellerMail, null));
			sut.close();
			assertTrue(s==null);
			
		} catch (ParamNullException | SaleAlreadyExistException | MustBeLaterThanTodayException e ) { 
			e.printStackTrace();
		    System.out.println("Error: " + e.getMessage());
			fail();
		} catch (Exception e) {
			fail();
		} 
	}
	
	@Test
	public void test7() {
		status= 7;
		try {
			sut.open();
			Sale s=sut.createSale(new CreateSaleParameter(title, description, status, price, pubDate, sellerMail, null));
			sut.close();
			assertTrue(s==null);
						
		} catch (ParamNullException | SaleAlreadyExistException | MustBeLaterThanTodayException e ) { 
			e.printStackTrace();
		    System.out.println("Error: " + e.getMessage());
			fail();
		} catch (Exception e) {
			fail();
		} 
	}
	@Test
	public void test8() {
		price= -20;
		try {
			sut.open();
			Sale s=sut.createSale(new CreateSaleParameter(title, description, status, price, pubDate, sellerMail, null));
			sut.close();
			assertTrue(s==null);
			
		} catch (ParamNullException | SaleAlreadyExistException | MustBeLaterThanTodayException e ) { 
			e.printStackTrace();
		    System.out.println("Error: " + e.getMessage());
			fail();
		} catch (Exception e) {
			fail();
		} 
	}
	@Test
	public void test9() {
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		pubDate=null;
		try {
			pubDate = sdf.parse("05/10/2020");
		} catch (ParseException e) {
			e.printStackTrace();
		}	
		try {
			sut.open();
			sut.createSale(new CreateSaleParameter(title, description, status, price, pubDate, sellerMail, null));
			sut.close();
			
		} catch ( MustBeLaterThanTodayException e ) { 
			assertTrue(true);
		} catch (ParamNullException | SaleAlreadyExistException  e ) { 
			e.printStackTrace();
		    System.out.println("Error: " + e.getMessage());
			fail();
		} catch (Exception e) {
			fail();
		} 
	}
	@Test
	public void test10() {
		sellerMail=null;
		try {
			sut.open();
			sut.createSale(new CreateSaleParameter(title, description, status, price, pubDate, sellerMail, null));
			sut.close();
			
		} catch (ParamNullException  e ) { 
			assertTrue(true);
		} catch (MustBeLaterThanTodayException | SaleAlreadyExistException  e ) { 
			e.printStackTrace();
		    System.out.println("Error: " + e.getMessage());
			fail();
		} catch (Exception e) {
			fail();
		} 
	}
	@Test
	public void test11() {
		sellerMail="sellerFake";
		try {
			sut.open();
			Sale s=sut.createSale(new CreateSaleParameter(title, description, status, price, pubDate, sellerMail, null));
			sut.close();
			assertTrue(s==null);
			
		} catch (ParamNullException | SaleAlreadyExistException | MustBeLaterThanTodayException e ) { 
			e.printStackTrace();
		    System.out.println("Error: " + e.getMessage());
			fail();
		} catch (Exception e) {
			fail();
		} 
	}
	
	@Test
	public void test12() {
		sellerMail="sellerTest1@ehu.eus";
		Seller s1=new Seller(sellerMail,"Seller Test 1","pass");
		
		// CORREGIDO: Usamos AddSaleParameter aquí también
		s1.addSale(new AddSaleParameter(title, description, status, price, pubDate, null));
		
        Mockito.when(db.find(Seller.class, s1.getEmail())).thenReturn(s1);

		try {	
			sut.open();
			sut.createSale(new CreateSaleParameter(title, description, status, price, pubDate, sellerMail, null));
			sut.close();
			fail();
			
		} catch (SaleAlreadyExistException e ) { 
			assertTrue(true);
		} catch (ParamNullException  | MustBeLaterThanTodayException e ) { 
			e.printStackTrace();
		    System.out.println("Error: " + e.getMessage());
			fail();
		} catch (Exception e) {
			fail();
		}  
	}
}