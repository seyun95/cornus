package com.mocha.controller;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mocha.manager.TDigestManager;
import com.mocha.service.DetectService;
import com.tdunning.math.stats.Centroid;

@RestController
@RequestMapping("/detect")
public class DetectController {	
	@Autowired
	private DetectService detectService;

	/**
	 * create : tdigest 생성
	 * @param name
	 * @param time
	 * @return
	 * */
	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ResponseEntity<Map<String, String>> create(@RequestParam(value="name") String name, @RequestParam(value="timeSlice") String[] timeSlice, @RequestParam(value="compression") double compression) {
		detectService.create(name, timeSlice, compression);
		
		Map<String, String> map = new LinkedHashMap<>();
		map.put("status", "success");
		map.put("name", name);
		
		return ResponseEntity.ok(map);
	}
	
	/**
	 * add : tdigest 데이터 추가
	 * @param name
	 * @param time
	 * @param value
	 * @return
	 * */
	@RequestMapping(value = "/add", method = RequestMethod.GET)
	public ResponseEntity<Map<String, String>> add(@RequestParam(value="name") String name, @RequestParam(value="time") String time, @RequestParam(value="value") long value) {
		detectService.add(name, time, value);
		
		Map<String, String> map = new LinkedHashMap<>();
		map.put("status", "success");
		map.put("name", name);
		
		return ResponseEntity.ok(map);
	}
	
	/**
	 * quantile : quantile 값 얻어오기
	 * */
	@RequestMapping(value = "/quantile", method = RequestMethod.GET)
	public ResponseEntity<Map<String, String>> quantile(@RequestParam(value="name") String name, @RequestParam(value="time") String time, @RequestParam(value="cdf") double[] cdf) {
		List<Long> quantileList = detectService.quantile(name, time, cdf);
				
		Map<String, String> map = new LinkedHashMap<>();
		map.put("status", "success");
		map.put("quantile", StringUtils.join(quantileList, ","));
		
		return ResponseEntity.ok(map);
	}
	
	/**
	 * centroids : centroid 값 얻어오기
	 * */
	@RequestMapping(value = "/centroid", method = RequestMethod.GET)
	public ResponseEntity<Map<String, String>> centroid(@RequestParam(value="name", defaultValue="centroid") String name, @RequestParam(value="time") String time) {
		Collection<Centroid> centroid = detectService.centroid(name, time);
		
		Map<String, String> map = new LinkedHashMap<>();
		map.put("status", "success");
		map.put("name", name);
		map.put("quantile", String.valueOf(centroid));
		
		return ResponseEntity.ok(map);	
	}
	
	/**
	 * save : tdigest 저장하기
	 * */
	@RequestMapping(value = "/save", method = RequestMethod.GET)
	public ResponseEntity<Map<String, String>> save(@RequestParam(value="name") String name) {
		detectService.save(name);
		
		Map<String, String> map = new LinkedHashMap<>();
		map.put("status", "success");
		map.put("name", name);
		
		return ResponseEntity.ok(map);
	}
	
	/**
	 * load : tdigest 불러오기
	 * */
	@RequestMapping(value = "/load", method = RequestMethod.GET)
	public ResponseEntity<Map<String, String>> load(@RequestParam(value="name", defaultValue="load") String name, @RequestParam(value="compression") double compression) {
		Map<String, TDigestManager> tdigestManagerCache = detectService.load(name, compression);
		int count = tdigestManagerCache.get(name).getDigestCache().size();
		
		Map<String, String> map = new LinkedHashMap<>();
		map.put("status", "success");
		map.put("name", name);
		map.put("compression", String.valueOf(compression));
		map.put("count", String.valueOf(count));
		
		return ResponseEntity.ok(map);
	}
	
	/**
	 * delete : tdigest 삭제하기
	 * */
	@RequestMapping(value = "/delete", method = RequestMethod.GET)
	public void delete(@RequestParam(value="name", defaultValue="delete") String name) {
	}
	
	/**
	 * update : tdigest 데이터 보정
	 * */
	@RequestMapping(value = "/update", method = RequestMethod.GET)
	public void update(@RequestParam(value="name", defaultValue="update") String name) {
	}
	
}
