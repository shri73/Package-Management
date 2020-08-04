package com.eureka.zuul.filters;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;

@Component
public class RequestInfoLogPreFilter extends ZuulFilter{

	private static Logger logger = LoggerFactory.getLogger(RequestInfoLogPreFilter.class);
	
	@Override
	public Object run() throws ZuulException {
		
		RequestContext requestContext = RequestContext.getCurrentContext();
		HttpServletRequest request = requestContext.getRequest();
		// Logging request information
		logger.info("Pre Filter: " + String.format("%s request to %s", request.getMethod(), request.getRequestURL().toString()));
		 
		return null;
	}

	@Override
	public boolean shouldFilter() {
		return true;
	}

	@Override
	public int filterOrder() {
		return 1;
	}

	@Override
	public String filterType() {
		return "pre";
	}
	
}