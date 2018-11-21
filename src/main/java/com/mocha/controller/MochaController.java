package com.mocha.controller;

import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

import org.apache.tomcat.util.buf.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mocha.entity.MochaResponse;
import com.mocha.service.MochaService;

@RestController
public class MochaController {

	@Autowired
	MochaService mochaService;
	
	@RequestMapping("/create")
	public MochaResponse create(@RequestParam(value="name", defaultValue="World") String name) {
		
		mochaService.initDigest(name);
		
		return new MochaResponse(true);
	}
	
	@RequestMapping("/add")
	public MochaResponse add(@RequestParam Map<String, String> param) {
		
		String name = param.get("name");
		int slot = Integer.parseInt(param.get("slot"));
		double value = Double.parseDouble(param.get("value"));
		
		boolean bRet = mochaService.add(name, slot, value);
		return new MochaResponse(bRet);
	}
	
	@RequestMapping("/quantile")
	public MochaResponse quantile(@RequestParam Map<String, String> param) {
		return new MochaResponse(true);
	}
	
	@RequestMapping("/centroids")
	public MochaResponse centroids(@RequestParam Map<String, String> param) {
		return new MochaResponse(true);
	}
	
	@RequestMapping("/delete")
	public MochaResponse delete(@RequestParam Map<String, String> param) {
		return new MochaResponse(true);
	}
	
	@RequestMapping("/save")
	public MochaResponse save(@RequestParam Map<String, String> param) {
		return new MochaResponse(true);
	}
	
	@RequestMapping("/load")
	public MochaResponse load(@RequestParam Map<String, String> param) {
		return new MochaResponse(true);
	}
	
}
