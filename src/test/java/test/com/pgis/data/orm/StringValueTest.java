package test.com.pgis.data.orm;

import static org.junit.Assert.*;

import org.junit.Test;

import com.pgis.bus.data.orm.Route;
import com.pgis.bus.data.orm.StringValue;
import com.pgis.bus.data.orm.type.LangEnum;

public class StringValueTest {

	@Test
	public void cloneTest() throws CloneNotSupportedException {
		StringValue stringValue = new StringValue();
				
		stringValue.setId(48);
		stringValue.setKeyID(20);
		stringValue.setLangID(LangEnum.c_en);
		stringValue.setValue("styd");
		
		StringValue stringValue_clone = stringValue.clone();
		
		stringValue.setId(92);
		stringValue.setKeyID(23);
		stringValue.setLangID(LangEnum.c_ru);
		stringValue.setValue("styddd");
		
		assertEquals(48, (int)stringValue_clone.getId() );
		assertEquals(20, (int)stringValue_clone.getKeyID());
		assertEquals(LangEnum.c_en, stringValue_clone.getLangID());
		assertEquals("styd", stringValue_clone.getValue() );
		
	}
	
	@Test
	public void cloneTest2() throws CloneNotSupportedException
	{
		StringValue stringValue = new StringValue();
		StringValue stringValue_clone = stringValue.clone();
		assertNotNull (stringValue_clone);
	}

}
