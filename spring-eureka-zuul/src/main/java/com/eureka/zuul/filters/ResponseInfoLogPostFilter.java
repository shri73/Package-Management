package com.eureka.zuul.filters;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;

@Component
public class ResponseInfoLogPostFilter extends ZuulFilter{

	private static Logger logger = LoggerFactory.getLogger(ResponseInfoLogPostFilter.class);
	
	@Override
	public Object run() throws ZuulException {
		
		RequestContext context = RequestContext.getCurrentContext();
		logger.info("Post Filter: " + String.format("Response Status Code: %s", context.getResponse().getStatus()));	
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
		return "post";
	}

}