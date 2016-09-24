package org.spring.api;


import java.util.Collection;

import org.spring.core.infra.FromJsonHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;


@RestController  
@RequestMapping("/address")
public class DummyApiResource {
	
	private final FromJsonHelper fromApiJsonHelper;
	
	
	
	@Autowired
	public DummyApiResource(
			final FromJsonHelper fromApiJsonHelper) {
		super();
		this.fromApiJsonHelper = fromApiJsonHelper;
	}

	@RequestMapping(method = RequestMethod.POST)
	@ResponseBody
    public String createAddress(@RequestBody final  String reqBody) {
		
			return null;
    }
	
	
	@RequestMapping(method = RequestMethod.GET)
	@ResponseBody
    public String getall() {
		
		Collection<String>  result  = null;
		
		return null;
    }
	
}
