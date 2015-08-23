package org.casual.civic.domain;

import java.util.LinkedHashMap;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.apache.commons.lang3.StringUtils;
import org.casual.civic.core.infra.JsonCommand;
import org.springframework.data.jpa.domain.AbstractPersistable;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

@Entity
@Table(name = "c_politician")
public class Politician extends AbstractPersistable<Long> {
	
	@Column(name = "name")
    private String name;

	public static Politician fromJson(final String object) {
		final JsonObject politician = new JsonParser().parse(object).getAsJsonObject();
        final String name = politician.
        		get("name").getAsString();

        return new Politician(name);
    }

    protected Politician() {
        //
    }

    private Politician(final String name) {
        this.name = name;
    }
    
    public Map<String, Object> update(final JsonCommand command) {
    	final Map<String, Object> actualChanges = new LinkedHashMap<>();

    	final String nameParamName = "name";
        if (command.isChangeInStringParameterNamed(nameParamName, this.name)) {
            final String newValue = command.stringValueOfParameterNamed(nameParamName);
            actualChanges.put(nameParamName, newValue);
            this.name = StringUtils.defaultIfEmpty(newValue, null);
        }
    	return actualChanges;
    }
	
}
