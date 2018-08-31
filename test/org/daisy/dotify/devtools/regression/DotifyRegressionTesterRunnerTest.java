package org.daisy.dotify.devtools.regression;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
@SuppressWarnings("javadoc")
public class DotifyRegressionTesterRunnerTest {

	@Test
	public void testGetArgumentKey() {
		assertEquals("--locale", DotifyRegressionTesterRunner.getArgumentKey("--locale=sv"));
		assertEquals("locale", DotifyRegressionTesterRunner.getArgumentKey("locale"));
		assertEquals("-locale", DotifyRegressionTesterRunner.getArgumentKey("-locale=sv"));
	}
}
