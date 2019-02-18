package com.mocha.manager;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tdunning.math.stats.AVLTreeDigest;
import com.tdunning.math.stats.TDigest;

public class TDigestManager {
	private final Map<Integer, TDigest> digestCache = new ConcurrentHashMap<>();
	
	/** Logger */
	private static final Logger logger = LoggerFactory.getLogger("TdigestManager");
	
	public void createDigestCache(String[] timeSlice, double compression) {
		Arrays.asList(timeSlice).stream().forEach(time -> {
			this.digestCache.put(NumberUtils.toInt(time), TDigest.createDigest(compression));			
		});		
	}
	
	public void addDigestCache(String timeSlice, TDigest digest) {
		this.digestCache.put(NumberUtils.toInt(timeSlice), digest);
	}
	
	public Map<Integer, TDigest> getDigestCache() {
		return digestCache;
	}
	
	public TDigest getDigest(int key) {
		return digestCache.get(key);
	}

	public void save(TDigest digest, Path filePath) {
		ByteBuffer buf = ByteBuffer.allocate(digest.byteSize());
		digest.asBytes(buf);
		
		File outFile = filePath.toFile();
		
		try(FileChannel channel = new FileOutputStream(outFile, false).getChannel()) {
			buf.flip();
			channel.write(buf);
			logger.debug("success save to {}", outFile.getAbsolutePath());
		} catch (Exception e) {
			logger.error("fail save. msg: {}", e.getMessage());
		}
	}
	
	/**
	 * backup된 tdigest 파일을 사용하여 생성
	 * 단, load 실패시 새롭게 생성
	 * */
	public TDigest load(double compression, Path filePath) {
		File inFile = filePath.toFile();
		ByteBuffer readBuf = ByteBuffer.allocate((int)inFile.length());
		try(InputStream is = new FileInputStream(inFile)) {
			int b;
			while((b=is.read()) != -1) {
				readBuf.put((byte)b);
			}
			readBuf.flip();
			logger.debug("success restore from {}", inFile.getAbsolutePath());
			return AVLTreeDigest.fromBytes(readBuf);
			
		} catch (Exception e) {
			logger.info("fail restore. msg: {}", e.getMessage());
			return TDigest.createAvlTreeDigest(compression);
		}
	}	
}
