package org.casual.civic.core.exception;

public class AbstractResourceNotFoundException extends PlatformException {
	
    public AbstractResourceNotFoundException(final String globalisationMessageCode, final String defaultUserMessage,
            final Object... defaultUserMessageArgs) {
    	super(globalisationMessageCode, defaultUserMessage, defaultUserMessageArgs);
    }

}
