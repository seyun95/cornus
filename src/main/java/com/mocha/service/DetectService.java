package com.mocha.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.mocha.manager.TDigestManager;
import com.tdunning.math.stats.Centroid;

@Service
public class DetectService {
	
	private final Map<String, TDigestManager> tdigestManagerCache = new ConcurrentHashMap<>();
	
	private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
	private static final DateTimeFormatter LEARNING_KEY_FORMAT = DateTimeFormatter.ofPattern("HH");
	
	/** Logger */
	private static final Logger logger = LoggerFactory.getLogger("DetectService");
	
	public Map<String, TDigestManager> getCache() {
		return tdigestManagerCache;
	}
	
	/**
	 * 해당 이름에 대한 TDigestManager가 없는 경우 최초 생성을 하고 해당 시간에 대한 Tdigest를 생성한다
	 * 해당 이름에 대한 TDigestManager가 있는 경우 TDigestManager를 불러오고 해당 시간에 대한 Tdigest를 생성한다
	 * */
	public void create(String name, String[] timeSlice, double compression) {
		TDigestManager tdigestManager = tdigestManagerCache.get(name);
		
		if (Objects.isNull(tdigestManager)) {
			tdigestManager = new TDigestManager();
			tdigestManager.createDigestCache(timeSlice, compression);
		} else {
			tdigestManager.createDigestCache(timeSlice, compression);
		}
		
		tdigestManagerCache.put(name, tdigestManager);		
	}
	
	public void add(String name, String time, long count) {
		LocalDateTime dateTime = LocalDateTime.parse(time, formatter);

		int key = NumberUtils.toInt(dateTime.format(LEARNING_KEY_FORMAT));
		
		TDigestManager tdigestManager = tdigestManagerCache.get(name);
		if (!Objects.isNull(tdigestManager.getDigest(key))) {
			tdigestManager.getDigest(key).add(count);
		}
	}
	
	/**
	 * TODO cache null 일때 처리하
	 * */
	public List<Long> quantile(String name, String time, double[] cdf) {
		LocalDateTime dateTime = LocalDateTime.parse(time, formatter);

		int key = NumberUtils.toInt(dateTime.format(LEARNING_KEY_FORMAT));
		
		TDigestManager tdigestManager = tdigestManagerCache.get(name);
		
		List<Long> quantile = new ArrayList<>();
		Arrays.stream(cdf).forEach(result -> {
			if (!Objects.isNull(tdigestManager.getDigest(key))) {
				quantile.add((long) tdigestManager.getDigest(key).quantile(result));
			}			
		});		
		
		return quantile;
	}

	public Collection<Centroid> centroid(String name, String time) {
		LocalDateTime dateTime = LocalDateTime.parse(time, formatter);

		int key = NumberUtils.toInt(dateTime.format(LEARNING_KEY_FORMAT));
		
		TDigestManager tdigestManager = tdigestManagerCache.get(name);
		
		Collection<Centroid> centroids = null;
		if (!Objects.isNull(tdigestManager.getDigest(key))) {
			centroids = tdigestManager.getDigest(key).centroids();
		}
		
		return centroids;
	}

	public void save(String name) {
		Path path = Paths.get("./src/main/resources/model", name);
		File dir = path.toFile();
		if(!dir.exists()) {
			dir.mkdirs();
		}

		TDigestManager tdigestManager = tdigestManagerCache.get(name);
		
		if (!Objects.isNull(tdigestManager)) {
			logger.info("start save to {}", path);
			
			tdigestManager.getDigestCache().entrySet().forEach(digest -> {
				tdigestManager.save(digest.getValue(), path.resolve(String.valueOf(digest.getKey())));
			});
			
			logger.info("end save to {}", path);
		} else {
			logger.info("fail save. msg: {}", "no data");
		}				
	}	
	
	public Map<String, TDigestManager> load(String name, double compression) {
		Path path = Paths.get("./src/main/resources/model", name);
		File dir = path.toFile();
		if(!dir.exists()) {
			dir.mkdir();
		}
		TDigestManager tdigestManager = new TDigestManager();		
		
		logger.info("start load from {}", path);
		
		try {
			Files.list(path).forEach(filePath -> {
				if (!StringUtils.contains(filePath.getFileName().toString(), "DS_Store")) {
					tdigestManager.addDigestCache(filePath.getFileName().toString(), tdigestManager.load(compression, filePath));					
				}
			});
		} catch (IOException e) {
			logger.error("fail restore. msg: {}", e.getMessage());
		} 

		tdigestManagerCache.put(name, tdigestManager);
		
		logger.info("end load from {}", path);
		
		return tdigestManagerCache;
	}
		
}
