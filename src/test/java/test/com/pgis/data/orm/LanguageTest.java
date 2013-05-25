package test.com.pgis.data.orm;

import static org.junit.Assert.*;

import org.junit.Test;

import com.pgis.bus.data.orm.Language;
import com.pgis.bus.data.orm.Schedule;
import com.pgis.bus.data.orm.type.LangEnum;

public class LanguageTest {

	@Test
	public void cloneTest() throws CloneNotSupportedException {
		Language language = new Language ();
		language.setId(LangEnum.c_en);
		language.setName("en");
		
		Language language_clone = language.clone();
		
		language.setId(LangEnum.c_ru);
		language.setName("ru");
		
		assertEquals(LangEnum.c_en,language_clone.getId());
		assertEquals("en",language_clone.getName());
	}
	
	@Test
	public void cloneTest2() throws CloneNotSupportedException
	{
		Language language = new Language();
		Language language_clone = language.clone();
		assertNotNull (language_clone);
	}

}
