package com.mocha.service;

import java.nio.file.Paths;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.mocha.entity.EgDigest;

@Service
public class MochaService {

	private final Map<String, Map<Integer, EgDigest>> learningMap = new ConcurrentHashMap<>();
	private final Logger logger = LoggerFactory.getLogger(getClass());
	
	public void initDigest(String name) {
		
		if(learningMap.get(name) != null) {
			logger.info("already exist : digestName - ", name);
			return;
		}
		
		Map<Integer, EgDigest> digestMap = new ConcurrentHashMap<>();
		digestMap.clear();
		for (int i=0; i<24; i++) {
			digestMap.put(i, new EgDigest(100, Paths.get("name")));
		}
		learningMap.put(name, digestMap);
	}
	
	public boolean add(String name, int slot, double value) {
		
		boolean result = false;
		Map<Integer, EgDigest> digestMap = learningMap.get(name);
		digestMap.get(slot).add(value);
		
		return result;
	}
	
	public double getQuantile(String name, int slot, double q) {
		Map<Integer, EgDigest> digestMap = learningMap.get(name);
		return digestMap.get(slot).quantile(q);
		
	}
	
	public double getCdf(String name, int slot, double x) {
		Map<Integer, EgDigest> digestMap = learningMap.get(name);
		return digestMap.get(slot).cdf(x);
		
	}	
}
