package org.daisy.dotify.devtools.converters;

import org.daisy.dotify.devtools.converters.CodePointHelper.Mode;
import org.daisy.dotify.devtools.converters.CodePointHelper.Style;
import org.junit.Test;

public class CodePointHelperTest {

	@Test
	public void testToHexString_1() {
		String expected = "0001";
		String actual = CodePointHelper.toHexString(1, 4);
		org.junit.Assert.assertEquals(expected, actual);
	}

	@Test
	public void testToHexString_2() {
		String expected = "0010";
		String actual =  CodePointHelper.toHexString(16, 4);
		org.junit.Assert.assertEquals(expected, actual);
	}
	
}
