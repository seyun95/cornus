package com.mocha.controller;

import java.util.Map;

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
	
	@RequestMapping("/info")
	public MochaResponse info(@RequestParam Map<String, String> param) {
		
		String msg = "Mocha Project, Version 0.2";
		
		MochaResponse response = new MochaResponse();
		response.setMessage(msg);
		response.setResult(true);
		
		return response;
	}
	
	@RequestMapping("/create")
	public MochaResponse create(@RequestParam Map<String, String> param) {
		
		String name = param.get("name");
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
		
		String name = param.get("name");
		int slot = Integer.parseInt(param.get("slot"));
		double q = Double.parseDouble(param.get("value"));

		MochaResponse response = new MochaResponse();
		response.setQuantine(mochaService.getQuantile(name, slot, q));
		response.setResult(true);
		return response;
	}
	
	@RequestMapping("/cdf")
	public MochaResponse cdf(@RequestParam Map<String, String> param) {
		
		String name = param.get("name");
		int slot = Integer.parseInt(param.get("slot"));
		double x = Double.parseDouble(param.get("value"));
		
		MochaResponse response = new MochaResponse();
		response.setCdf(mochaService.getCdf(name, slot, x));
		response.setResult(true);
		return response;
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
