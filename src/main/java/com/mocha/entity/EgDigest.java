package com.mocha.entity;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tdunning.math.stats.AVLTreeDigest;
import com.tdunning.math.stats.TDigest;

public class EgDigest {

	private TDigest digest;
    private final Map<String, TDigest> subDigest = new ConcurrentHashMap<>();
    private Path backupPath;
    
	private static final Logger logger = LoggerFactory.getLogger("scheduler");

    public EgDigest(double compression, Path backupPath) {
    	this.backupPath = backupPath;
    	
    	load(compression);
    }
    
    public void add(double x) {
    	this.digest.add(x);
    }
    
    public TDigest getDigest() {
    	return this.digest;
    }
    
    public double quantile(double q) {
    	return this.digest.quantile(q);
    }

    public double cdf(double x) {
    	return this.digest.cdf(x);
    }

    public void addSubDigest(String key, double value) {
    	
		TDigest curDigest = subDigest.get(key);
		if(curDigest == null) {
			// compression은 10으로.
			curDigest = TDigest.createAvlTreeDigest(10);
			curDigest.add(value);
			subDigest.put(key, curDigest);
			
		} else {
			curDigest.add(value);
		}
    }
    
    public void addSubDigest(Map<String, Object> subMap) {
    	
    	subMap.entrySet().stream().forEach(item -> {
    		addSubDigest(item.getKey(), (double)item.getValue());
    	});
    }
    
    public Map<String, TDigest> getSubDigest() {
    	return this.subDigest;
    }
    
    public Path getBackupPath() {
		return backupPath;
	}

	public void setBackupPath(Path backupPath) {
		this.backupPath = backupPath;
	}

    // 현재의 tdigest 정보를 저장
    public void backup() {
		
    	Path subPath = this.backupPath.resolve("sub");
    	File subDir = subPath.toFile();
   		if(!subDir.exists()) {
   			subDir.mkdirs();
   		}
   		
   		logger.info("start backup to {}", subPath);
   		
   		// backup count digest
   		backup(this.digest, this.backupPath.resolve("countDigest"));
   		
   		// backup sub. digest
    	this.subDigest.entrySet().forEach(sub -> {
    		//backup(sub.getValue(), this.backupPath.resolve("sub").resolve(sub.getKey()));
    		backup(sub.getValue(), subPath.resolve(sub.getKey()));
    	});

   		logger.debug("end backup to {}", subPath);
	}
    
    private void backup(TDigest digest, Path filePath) {
   		
    	ByteBuffer buf = ByteBuffer.allocate(digest.byteSize());
   		digest.asBytes(buf);
		
   		File outFile = filePath.toFile();
   			
   		try(FileChannel channel = new FileOutputStream(outFile, false).getChannel()) {
   			buf.flip();
   			channel.write(buf);
   			logger.debug("success backup to {}", outFile.getAbsolutePath());
		} catch (Exception e) {
			logger.error("fail backup. msg: {}", e.getMessage());
		}
    }

	private void load(double compression) {

		logger.info("start load from {}", this.backupPath);
		
		// load main digest
		this.digest = load(compression, this.backupPath.resolve("countDigest"));
		
		// load sub. digest
		try {
			Files.list(this.backupPath.resolve("sub")).forEach( path -> {
				subDigest.put(path.getFileName().toString(), load(compression, path));
			});
		} catch (IOException e) {
			logger.error("fail restore. msg: {}", e.getMessage());
		} 

		logger.debug("end load from {}", this.backupPath);
	}    
    
	// 
    /**
     * backup된 tdigest 파일을 사용하여 생성
     * 단, load 실패시 새롭게 생성
     * @param backupPath
     */
	private TDigest load(double compression, Path filePath) {

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
