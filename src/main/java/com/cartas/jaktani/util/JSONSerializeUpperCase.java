package com.cartas.jaktani.util;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

public class JSONSerializeUpperCase extends StdSerializer<String>{

	protected JSONSerializeUpperCase(Class<String> t) {
		super(t);
		// TODO Auto-generated constructor stub
	}
	
	public JSONSerializeUpperCase() { 
        this(null); 
    }

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public void serialize(String value, JsonGenerator gen, SerializerProvider provider) throws IOException {
		gen.writeString(value.toUpperCase());
		
	}

}
