package org.casual.civic.service;

import java.util.Map;

import org.casual.civic.core.infra.CommandProcessingResult;
import org.casual.civic.core.infra.CommandProcessingResultBuilder;
import org.casual.civic.core.infra.JsonCommand;
import org.casual.civic.domain.Politician;
import org.casual.civic.domain.PoliticianRepository;
import org.casual.civic.exception.PoliticianNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PoliticianWritePlatformServiceImpl {
	
	private final PoliticianRepository politicianRepository;
	
	@Autowired
	public PoliticianWritePlatformServiceImpl(final PoliticianRepository politicianRepository) {
		this.politicianRepository = politicianRepository;
	}

	@Transactional
	public CommandProcessingResult createPolitician(final JsonCommand pol) {
		final Politician politician = Politician.fromJson(pol.getJsonCommand());
        this.politicianRepository.save(politician);
        return new CommandProcessingResultBuilder()
        		.withResourceIdAsString(politician.getId()).build();
	}
	
	@Transactional
	public CommandProcessingResult updatePolitician(final long politicianId, final JsonCommand pol) {
		final Politician politician = this.politicianRepository.findOne(politicianId);
        if (politician == null) { throw new PoliticianNotFoundException(politicianId); }

        final Map<String, Object> changes = politician.update(pol);
        if (!changes.isEmpty()) {
            this.politicianRepository.saveAndFlush(politician);
        }
        return new CommandProcessingResultBuilder()
			.withResourceIdAsString(politician.getId())
			.withChanges(changes).build();
	}
	
	@Transactional
	public CommandProcessingResult deletePolitician(final long politicianId) {
        this.politicianRepository.delete(politicianId);
        return CommandProcessingResult.empty();
	}
}
