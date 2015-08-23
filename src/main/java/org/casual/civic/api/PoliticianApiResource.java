package org.casual.civic.api;

import java.util.Collection;

import org.casual.civic.core.infra.ApiSerializer;
import org.casual.civic.core.infra.CommandProcessingResult;
import org.casual.civic.core.infra.FromJsonHelper;
import org.casual.civic.core.infra.JsonCommand;
import org.casual.civic.data.PoliticianData;
import org.casual.civic.service.PoliticianReadPlatformService;
import org.casual.civic.service.PoliticianWritePlatformServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.JsonParser;

@RestController  
@RequestMapping("/politicians") 
public class PoliticianApiResource {
	
	private final PoliticianReadPlatformService politicianReadPlatformService;
	private final PoliticianWritePlatformServiceImpl politicianWritePlatformService;
	private final ApiSerializer<PoliticianData> jsonSerializer;
	private final FromJsonHelper fromApiJsonHelper;
	
	@Autowired
    public PoliticianApiResource(final PoliticianReadPlatformService politicianReadPlatformService,
    		final PoliticianWritePlatformServiceImpl politicianWritePlatformService,
    		final ApiSerializer<PoliticianData> jsonSerializer,
    		final FromJsonHelper fromApiJsonHelper) {
		this.politicianReadPlatformService = politicianReadPlatformService;
		this.politicianWritePlatformService = politicianWritePlatformService;
		this.jsonSerializer = jsonSerializer;
		this.fromApiJsonHelper = fromApiJsonHelper;
	}
	
	@RequestMapping(method = RequestMethod.GET)  
	@Transactional(readOnly = true)
    public String retrievePoliticians() {
        final Collection<PoliticianData> politicians = this.politicianReadPlatformService.retrieveAll();
        return this.jsonSerializer.serialize(politicians);
    }
	
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)  
	@Transactional(readOnly = true)
    public String retrievePolitician(@PathVariable("id") long politicianId) {
        final PoliticianData politician = this.politicianReadPlatformService.retrieveOne(politicianId);
        return this.jsonSerializer.serialize(politician);
    }
	
	@RequestMapping(method = RequestMethod.POST)
	@ResponseBody
    public CommandProcessingResult createPolitician(@RequestBody final  String politician) {
		return this.politicianWritePlatformService.createPolitician(JsonCommand.from(politician,
				new JsonParser().parse(politician), fromApiJsonHelper));
    }
	
	@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
	@ResponseBody
    public CommandProcessingResult updatePolitician(@PathVariable("id") long politicianId,
    		@RequestBody final  String politician) {
		return this.politicianWritePlatformService.updatePolitician(politicianId,
				JsonCommand.from(politician, new JsonParser().parse(politician),
						fromApiJsonHelper));

    }
	
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	@ResponseBody
    public CommandProcessingResult deletePolitician(@PathVariable("id") long politicianId) {
		return this.politicianWritePlatformService.deletePolitician(politicianId);

    }

}
