package com.mocha.service;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = { MochaService.class })
@EnableConfigurationProperties
public class MochaServiceTest {
	
	@Autowired
	MochaService mochaService;
	
	@Test
	public void testInitDigest() {
		Path path = Paths.get("name");
		String systemPath = System.getProperty("user.dir");
		System.out.println("path : " + path + ", systemPath : " + systemPath);
	}
	
	@Test
	public void testAdd() {
		
		String digestName = "test";
		mochaService.initDigest(digestName);
		mochaService.add(digestName, 0, 10);
		mochaService.add(digestName, 0, 10);
		mochaService.add(digestName, 0, 10);
		mochaService.add(digestName, 0, 20);
		mochaService.add(digestName, 0, 20);
		mochaService.add(digestName, 0, 60);
		mochaService.add(digestName, 0, 60);
		mochaService.add(digestName, 0, 60);
		mochaService.add(digestName, 0, 60);
		mochaService.add(digestName, 0, 60);
		mochaService.add(digestName, 0, 70);
		mochaService.add(digestName, 0, 100);
		mochaService.add(digestName, 0, 100);
		mochaService.add(digestName, 0, 100);
		mochaService.add(digestName, 0, 100);
		mochaService.add(digestName, 0, 100);
		mochaService.add(digestName, 0, 100);
		mochaService.add(digestName, 0, 100);
		mochaService.add(digestName, 0, 1000);
		mochaService.add(digestName, 0, 1000);
		mochaService.add(digestName, 0, 10000);
		
		double q = mochaService.getQuantile(digestName, 0, 0.5);
		System.out.println("name : " + digestName +  ", quantile: " + q);
	}

}
