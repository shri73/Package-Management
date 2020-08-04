package com.eureka.zuul.filters;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;

/**
 *	You can test the execution of this filter by shutting down one of the services (course/topic) and invoking the Zuul URL
 */
@Component
public class ErrorInfoLogErrorFilter extends ZuulFilter {

	private static Logger logger = LoggerFactory.getLogger(ErrorInfoLogErrorFilter.class);
	
	@Override
	public Object run() throws ZuulException {
		
		RequestContext context = RequestContext.getCurrentContext();
		logger.info("Error Filter: " + String.format("Error response Status Code: %s", context.getResponse().getStatus()));
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
		return "error";
	}
	
}
