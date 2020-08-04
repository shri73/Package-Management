package com.order.Package.controller;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.order.Package.PackageApplication;
import com.order.Package.model.Package;
import com.order.Package.repository.PackageRepository;
import com.order.Package.service.PackageService;
import com.order.Package.service.StoreService;

@ContextConfiguration(classes=PackageApplication.class)
@AutoConfigureMockMvc
@WebMvcTest(PackageController.class)
public class PackageControllerTest {
	
	@Autowired
	private MockMvc mockMvc;
	
	@MockBean
	PackageService service;
	
	@MockBean
	StoreService storeService;
	
	@MockBean
	PackageRepository packageRepo;
	
	
	private final static String URI_ORDER = "/package/order";
	
	
	@Test
	public void testGetDepartment() throws Exception {

		// given

		Package toBeSaved = new Package();

		toBeSaved.setUsername("test@test.com");
		toBeSaved.setStoreId("S113");

		Optional<Package> optionalPackage = Optional.of(toBeSaved);

		when(service.savePackage(toBeSaved)).thenReturn(optionalPackage.get());
		when(storeService.StoreExists(toBeSaved.getStoreId())).thenReturn(true);
		when(packageRepo.save(toBeSaved)).thenReturn(toBeSaved);
		
		RequestBuilder requestBuilder = MockMvcRequestBuilders
				.post(URI_ORDER)
				.content(toBeSaved.toString())
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON);

		MvcResult result = mockMvc.perform(requestBuilder).andReturn();

		MockHttpServletResponse response = result.getResponse();

		assertEquals(HttpStatus.CREATED.value(), response.getStatus());
	}

}
