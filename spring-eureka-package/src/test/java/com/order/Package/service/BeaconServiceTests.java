package com.order.Package.service;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

import com.order.Package.model.Beacon;
import com.order.Package.model.Store;
import com.order.Package.model.Stores;
import com.order.Package.repository.BeaconRepository;

@SpringBootTest
public class BeaconServiceTests {
	
	@Mock
	BeaconRepository repository;
	
	@InjectMocks
	BeaconService service;
	
	private Store store;
	private Beacon beacon;
	
	 @BeforeEach
	  public void setUp(){
	        store = new Store();
	        store.setStoreNumber("S113");  
	        beacon = new Beacon();
	        beacon.setUuid("1234");
	        beacon.setMajor("1");
	        beacon.setMinor("12");
	        beacon.setStore(store);
	    }

	 	@Test 
		public void test_getBeaconByStore() {
			Mockito.when(repository.findByStore(store)).thenReturn(Optional.ofNullable(beacon));
			// Method call
	        Beacon found = service.getBeaconByStore("S113");
	        assertTrue(found != null);

	        Mockito.verify(repository, Mockito.times(1)).findByStore(Mockito.anyObject());
	        Mockito.verifyNoMoreInteractions(repository);
		}
}
