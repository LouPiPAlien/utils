package com.ltchen.utils.string;

import static org.junit.Assert.*;

import org.junit.Test;

public class StringUtilTest {

	@Test
	public void testIsBlank() {
		assertEquals(true, StringUtil.isBlank(""));
	}

	@Test
	public void testIsNotBlank() {
		assertEquals(true, StringUtil.isNotBlank(""));
	}

}
