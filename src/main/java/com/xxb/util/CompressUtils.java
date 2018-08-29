package com.xxb.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Collection;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.apache.commons.compress.archivers.zip.Zip64Mode;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveInputStream;
import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream;
import org.apache.commons.compress.compressors.gzip.GzipCompressorInputStream;
import org.apache.commons.compress.compressors.gzip.GzipCompressorOutputStream;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CompressUtils {

	private final static int BUFFER = 1048576;

	public static File packTar(String srcPathname, File target) {
		FileOutputStream fos = null;
		BufferedOutputStream bos = null;
		TarArchiveOutputStream taos = null;
		FileInputStream fis = null;
		File srcDir = new File(srcPathname);
		try {
			fos = new FileOutputStream(target);
			bos = new BufferedOutputStream(fos, BUFFER);
			taos = new TarArchiveOutputStream(bos);
			// 解决文件名过长问题
			taos.setLongFileMode(TarArchiveOutputStream.LONGFILE_GNU);
			Collection<File> files = FileUtils.listFiles(srcDir, null, true);
			for (File file : files) {
				String fileName = file.getAbsolutePath().replace(srcPathname, "");
				taos.putArchiveEntry(new TarArchiveEntry(file, fileName));
				fis = new FileInputStream(file);
				IOUtils.copy(fis, taos);
				taos.closeArchiveEntry();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (fis != null)
					fis.close();
				if (taos != null) {
					taos.finish();
					taos.flush();
					taos.close();
				}
				if (bos != null) {
					bos.flush();
					bos.close();
				}
				if (fos != null) {
					fos.flush();
					fos.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return target;
	}

	public static void unpackTar(File tarFile, String distDir) {
		if (tarFile==null) {
			throw new RuntimeException("tarFile is null");
		}
		FileInputStream fis = null;
		BufferedInputStream bis = null;
		TarArchiveInputStream tais = null;
		OutputStream out = null;
		try {
			fis = new FileInputStream(tarFile);
			bis = new BufferedInputStream(fis);
			tais = new TarArchiveInputStream(bis);
			TarArchiveEntry tae = null;
			while ((tae = tais.getNextTarEntry()) != null) {
				File tmpFile = new File(distDir,tae.getName());
				// 使用 mkdirs 可避免因文件路径过多而导致的文件找不到的异常
				new File(tmpFile.getParent()).mkdirs();
				out = new FileOutputStream(tmpFile);
				IOUtils.copy(tais, out);
//				int length = 0;
//				byte[] b = new byte[BUFFER];
//				while ((length = tais.read(b)) != -1) {
//					out.write(b, 0, length);
//				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
            try {
                if(tais != null)  tais.close();
                if(bis != null) bis.close();
                if(fis != null) fis.close();
                if(out != null){
                    out.flush();
                    out.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
	}
	
	public static File zip(String srcPathname, File target) {
		FileOutputStream fos = null;
		BufferedOutputStream bos = null;
		ZipArchiveOutputStream zaos = null;
		FileInputStream fis = null;
		File srcDir = new File(srcPathname);
		try {
			fos = new FileOutputStream(target);
			bos = new BufferedOutputStream(fos, BUFFER);
			zaos = new ZipArchiveOutputStream(bos);
			// 解决文件名过长问题
			zaos.setUseZip64(Zip64Mode.AsNeeded);
			Collection<File> files = FileUtils.listFiles(srcDir, null, true);
			for (File file : files) {
				String fileName = file.getAbsolutePath().replace(srcPathname, "");
				zaos.putArchiveEntry(new ZipArchiveEntry(file, fileName));
				fis = new FileInputStream(file);
				IOUtils.copy(fis, zaos);
				zaos.closeArchiveEntry();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (fis != null)
					fis.close();
				if (zaos != null) {
					zaos.finish();
					zaos.flush();
					zaos.close();
				}
				if (bos != null) {
					bos.flush();
					bos.close();
				}
				if (fos != null) {
					fos.flush();
					fos.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return target;
	}

	public static void unzip(File zipFile, String distDir) {
		FileInputStream fis = null;
		BufferedInputStream bis = null;
		ZipArchiveInputStream zais = null;
		OutputStream out = null;
		try {
			fis = new FileInputStream(zipFile);
			bis = new BufferedInputStream(fis);
			zais = new ZipArchiveInputStream(bis);
			ZipArchiveEntry zae = null;
			while ((zae = zais.getNextZipEntry()) != null) {
				File tmpFile = new File(distDir,zae.getName());
				// 使用 mkdirs 可避免因文件路径过多而导致的文件找不到的异常
				new File(tmpFile.getParent()).mkdirs();
				out = new FileOutputStream(tmpFile);
				IOUtils.copy(zais, out);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
            try {
                if(zais != null)  zais.close();
                if(bis != null) bis.close();
                if(fis != null) fis.close();
                if(out != null){
                    out.flush();
                    out.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
	}
	
	public void targz(File tarFile, File target) {
		BufferedInputStream bis = null;
		GzipCompressorOutputStream gcos = null;
		
		try {
			bis = new BufferedInputStream(new FileInputStream(tarFile));
			BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(target));
			gcos = new GzipCompressorOutputStream(bos);
			IOUtils.copy(bis, gcos);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
				try {
					if (bis != null) bis.close();
					if (gcos != null) gcos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
		}
	}
	
	public File untargz(File gzfile, File distDir) {
		File tarFile = null;
		GzipCompressorInputStream gcis = null;
		BufferedOutputStream bos = null;
		try {
			BufferedInputStream bis = new BufferedInputStream(new FileInputStream(gzfile));
			tarFile = new File(distDir, FilenameUtils.getPrefix(gzfile.getName()));
			bos = new BufferedOutputStream(new FileOutputStream(tarFile));
			gcis = new GzipCompressorInputStream(bis);
			IOUtils.copy(gcis, bos);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
				try {
					if (gcis != null) gcis.close();
					if (bos != null) bos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
		}
		return tarFile;
	}

	public static void main(String[] args) throws Exception {
		long start = System.currentTimeMillis();
		// zip("D:\\Temp\\test\\user-data\\xx-ide-template\\1.0\\minio",
		// "D:\\Temp\\test\\user-data\\xx-ide-template\\1.0\\minio\\aa.zip");
		// unzip("D:\\Temp\\test\\user-data\\xx-ide-template\\1.0\\minio\\aa.zip",
		// "D:\\Temp\\test\\minio\\xx-ide");
//		packTar("D:\\Temp\\test\\user-data\\xx-ide-template\\1.0\\minio",
//				new File("D:\\Temp\\test\\user-data\\aa.tar"));
//		unpackTar(new File("D:\\Temp\\test\\user-data\\aa.tar"), "D:\\Temp\\test\\minio\\xx-ide");
//		System.out.println(System.currentTimeMillis() - start);
		AtomicInteger count = new AtomicInteger(0);
		TaskThread t = new TaskThread(count, 10);
		t.start();
		for (int i=0;i<20;i++) {
			t.addTask(new CopyDirTask(String.valueOf(i),count));
		}
	}
	
	static class TaskThread extends Thread{
		
		AtomicInteger count;
		
		int limit;
		
		private ConcurrentLinkedDeque<CopyDirTask> queue = new ConcurrentLinkedDeque<CopyDirTask>();
		
		public TaskThread(AtomicInteger count, int limit) {
			this.count = count;
			this.limit = limit;
		}
		
		@Override
		public void run() {
			while(true){
				try {
					if (queue.isEmpty()) {
						Thread.sleep(200);
					}else {
						if (count.get()>limit) {
							System.out.println("线程达到上限，等待中...");
							Thread.sleep(200);
						} else {
							CopyDirTask task = queue.poll();
							Thread t = new Thread(task);
							t.setName("oss-async-Executor");
							t.start();
							count.incrementAndGet();
							System.out.println(task.getMsg()+" finished");
						}
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
		
		public void addTask(CopyDirTask task) {
			queue.offer(task);
		}
	}
	
	static class CopyDirTask implements Runnable {
		
		private static final Logger LOGGER = LoggerFactory.getLogger(CopyDirTask.class);
		
		String msg;
		
		AtomicInteger count;
		
		public CopyDirTask(String msg, AtomicInteger count) {
			this.msg = msg;
			this.count = count;
		}

		@Override
		public void run() {
			LOGGER.info("msg:{} start",msg);
			try {
				Thread.sleep(3000);
			} catch (Exception e) {
				LOGGER.error("copy file error...");
			}
			LOGGER.info("msg:{} end",msg);
			count.decrementAndGet();
		}
		
		public String getMsg() {
			return this.msg;
		}

	}
}
