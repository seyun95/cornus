package com.mocha.service;

import java.nio.file.Paths;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import com.mocha.entity.EgDigest;

@Service
public class MochaService {

	@Autowired
	private Environment env;
	private final Map<String, Map<Integer, EgDigest>> learningMap = new ConcurrentHashMap<>();
	private final Logger logger = LoggerFactory.getLogger(getClass());
	
	public void initDigest(String name) {

		String path = env.getProperty("system.path");
		System.out.println(path);
		System.out.println(Paths.get(path, name));
		
		if(learningMap.get(name) != null) {
			logger.info("already exist : digestName - ", name);
			return;
		}
		
		Map<Integer, EgDigest> digestMap = new ConcurrentHashMap<>();
		digestMap.clear();
		for (int i=0; i<24; i++) {
			digestMap.put(i, new EgDigest(100, Paths.get(path, name)));
		}
		learningMap.put(name, digestMap);
	}
	
	public boolean add(String name, int slot, double value) {
		
		Map<Integer, EgDigest> digestMap = learningMap.get(name);
		digestMap.get(slot).add(value);
		
		return true;
	}
	
	public double getQuantile(String name, int slot, double q) {
		Map<Integer, EgDigest> digestMap = learningMap.get(name);
		return digestMap.get(slot).quantile(q);
		
	}
	
	public double getCdf(String name, int slot, double x) {
		Map<Integer, EgDigest> digestMap = learningMap.get(name);
		return digestMap.get(slot).cdf(x);
		
	}
	
	public double delete(String name, int slot, double x) {
		Map<Integer, EgDigest> digestMap = learningMap.get(name);
		return digestMap.get(slot).cdf(x);
		
	}

	public boolean save(String name, int slot) {
		Map<Integer, EgDigest> digestMap = learningMap.get(name);
		
		digestMap.get(slot).backup();
		return true;
		
	}

	public boolean load(String name, int slot) {
		Map<Integer, EgDigest> digestMap = learningMap.get(name);
		
		// TODO 로드시 기존 digest 정보를 삭제하고 다시 만들었던가?
		// TODO 이전 정보 확인해 보
		
		
		return true;
		
	}

}
