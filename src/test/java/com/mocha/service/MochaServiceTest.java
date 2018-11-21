package com.mocha.service;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.Test;

public class MochaServiceTest {

	@Test
	public void testInitDigest() {
		Path path = Paths.get("name");
		String systemPath = System.getProperty("user.dir");
		System.out.println("path : " + path + ", systemPath : " + systemPath);
	}

}
