package org.casual.civic.core.infra;

import java.util.Collection;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.springframework.stereotype.Component;

import com.google.gson.GsonBuilder;

@Component
public final class ApiSerializer<T> {
	
	    public String serialize(final Collection<T> collection) {
	        return setBuilder().create().toJson(collection);
	    }
	    
	    public String serialize(final T object) {
	        return setBuilder().create().toJson(object);
	    }
	    
	    private GsonBuilder setBuilder() {
	    	final GsonBuilder builder = new GsonBuilder();
	    	builder.registerTypeAdapter(LocalDate.class, new JodaLocalDateAdapter());
	        builder.registerTypeAdapter(DateTime.class, new JodaDateTimeAdapter());
            builder.setPrettyPrinting();
            return builder;
	    }

}
