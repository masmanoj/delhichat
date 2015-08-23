package org.casual.civic.exception;

import org.casual.civic.core.exception.AbstractResourceNotFoundException;

public class PoliticianNotFoundException extends AbstractResourceNotFoundException {
	
	public PoliticianNotFoundException(final Long id) {
        super("error.msg.politician.id.invalid", "Politician with identifier " + id + " does not exist", id);
    }

}
