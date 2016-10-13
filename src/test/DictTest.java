import java.util.*;

import main.model.dict.*;

import org.junit.Test;
import static org.junit.Assert.*;

public class DictTest {

	@Test
	public void testPrecise() {
		Dict d = Dict.createDict();

		d.insert("eric", null);
		d.insert("arya", null);
		d.insert("frank", null);
		d.insert("price", null);
		d.insert("chuck", null);

		assertTrue(d.searchWithCommonPrefix("eric").contains("eric"));
		assertTrue(d.searchWithCommonPrefix("arya").contains("arya"));
		assertTrue(d.searchWithCommonPrefix("frank").contains("frank"));
		assertTrue(d.searchWithCommonPrefix("price").contains("price"));
		assertTrue(d.searchWithCommonPrefix("chuck").contains("chuck"));

		assertTrue(d.searchWithEditDist("eric", 0).contains("eric"));
		assertTrue(d.searchWithEditDist("arya", 0).contains("arya"));
		assertTrue(d.searchWithEditDist("frank", 0).contains("frank"));
		assertTrue(d.searchWithEditDist("price", 0).contains("price"));
		assertTrue(d.searchWithEditDist("chuck", 0).contains("chuck"));
	}

	@Test
	public void testCommonPrefix() {
		Dict d = Dict.createDict();

		d.insert("hello", null);
		d.insert("hello there", null);
		d.insert("hello world", null);
		d.insert("hello eric", null);
		d.insert("hello arya", null);

		d.insert("ello", null);
		d.insert("hell", null);
		d.insert("hella", null);
		d.insert("hallo", null);
		d.insert("olleh", null);

		ArrayList<String> result = d.searchWithCommonPrefix("hello");
		assertTrue(result.contains("hello"));
		assertTrue(result.contains("hello there"));
		assertTrue(result.contains("hello world"));
		assertTrue(result.contains("hello eric"));
		assertTrue(result.contains("hello arya"));
		assertFalse(result.contains("ello"));
		assertFalse(result.contains("hell"));
		assertFalse(result.contains("hella"));
		assertFalse(result.contains("hallo"));
		assertFalse(result.contains("olleh"));
	}

}
