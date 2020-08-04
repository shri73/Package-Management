package com.order.Package.service;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

import com.order.Package.model.Stores;
import com.order.Package.repository.StoreRespository;

@SpringBootTest
public class StoreServiceTests {
	@Mock
	StoreRespository repository;
	
	@InjectMocks
	StoreService service;
	
	private Stores store;
	
	 @BeforeEach
	  public void setUp(){
	        store = new Stores();
	        store.setStoreNumber("S113");   
	    }
	 
	@Test 
	public void testFindByStoreNumber() {
		Mockito.when(repository.findById("S113")).thenReturn(Optional.ofNullable(store));
		// Method call
        boolean found = service.StoreExists("S113");
        assertTrue(found);

        Mockito.verify(repository, Mockito.times(1)).findById(Mockito.anyString());
        Mockito.verifyNoMoreInteractions(repository);
	}
	 

}
