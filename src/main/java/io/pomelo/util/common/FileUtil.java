package io.pomelo.util.common;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Paths;
import java.util.Collection;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveInputStream;
import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.filefilter.DirectoryFileFilter;
import org.apache.commons.io.filefilter.FileFilterUtils;
import org.apache.commons.lang3.StringUtils;

/**
 * <p>
 * 文件处理工具类<br>
 * File util
 * </p>
 * 
 * @ClassName FileUtil.java
 * @author PomeloMan
 */
public class FileUtil {

	/**
	 * <p>
	 * 文件分隔符<br>
	 * File separator
	 * </p>
	 * 
	 * <pre>
	 * Windows -> \
	 * UNIX -> /
	 * </pre>
	 */
	public final static String FILE_SEPARATOR = System.getProperty("file.separator");
	/**
	 * <p>
	 * 项目根目录<br>
	 * Project base path
	 * </p>
	 */
	public final static String USER_DIR = System.getProperty("user.dir");

	public final static String DOT = ".";
	public final static String UNDERLINE = "_";
	public final static String SLASH = "/";
	public final static String BACKSLASH = "\\";

	/**
	 * <p>
	 * 获取文件，如果文件不存在则生成文件父目录。 @Link Paths.get(String first, String... more) <br>
	 * Get the file, if the file does not exist then generate the file parent
	 * directory.
	 * </p>
	 * 
	 * <pre>
	 * </pre>
	 * 
	 * @param first
	 * @param more
	 * @return
	 */
	public static File getFile(String first, String... more) {
		File file = Paths.get(first, more).toAbsolutePath().toFile();
		if (!file.exists()) {
			file.getParentFile().mkdirs();
		}
		return file;
	}

	/**
	 * <p>
	 * 获取别名文件<br>
	 * Get an alias file
	 * </p>
	 * 
	 * <pre>
	 * x.doc -> x_timestamp.doc
	 * a.txt -> a_1509507765990.txt
	 * b.xlsx -> b_1509507765990.xlsx
	 * x -> x/timestamp
	 * a -> a/1509507765990
	 * b -> b/1509507765990
	 * </pre>
	 * 
	 * @param file
	 * @return
	 * @throws FileNotFoundException
	 */
	public static File getAlias(File file) throws FileNotFoundException {
		String path = null;
		if (file.exists()) {
			if (file.isFile()) {
				path = FilenameUtils.getFullPath(file.getName()) + FilenameUtils.getBaseName(file.getName()) + UNDERLINE
						+ DateUtil.now().getTimeInMillis() + DOT + FilenameUtils.getExtension(file.getName());
			} else {
				path += FILE_SEPARATOR + DateUtil.now().getTimeInMillis();
			}
		} else {
			throw new FileNotFoundException();
		}
		return Paths.get(path).toAbsolutePath().toFile();
	}

	/**
	 * <p>
	 * 获取当前工程路径<br>
	 * Get the current project path
	 * </p>
	 * 
	 * @return
	 */
	public static String getCurrentProjectDirection() {
		return USER_DIR + FILE_SEPARATOR;
	}

	/**
	 * <p>
	 * 文件压缩<br>
	 * Zip file
	 * </p>
	 * 
	 * <pre>
	 * ("C:/a.zip", "C:/a/b/c.txt", true) -> C:/a.zip/a/b/c.txt -> C:/a.zip
	 * ("C:/a.zip", "C:/a/b/c.txt", false) -> C:/a.zip/c.txt -> C:/a.zip
	 * </pre>
	 * 
	 * @param dir
	 *                <p>
	 *                .zip 文件路径<br>
	 *                .zip file path
	 *                </p>
	 * @param path
	 *                <p>
	 *                被压缩文件/目录<br>
	 *                Compressed file / directory
	 *                </p>
	 * @param reserve
	 *                <p>
	 *                是否保留内部结构<br>
	 *                Whether to retain the internal structure
	 *                </p>
	 * @return
	 * @throws IOException
	 */
	public static File zip(String dir, String path, boolean reserve) throws IOException {
		try (OutputStream output = new FileOutputStream(dir);
				ZipArchiveOutputStream zipOutput = new ZipArchiveOutputStream(output)) {
			File src = getFile(path);
			if (reserve || src.isFile()) {
				zip(zipOutput, src, null);
			} else {
				Collection<File> files = FileUtils.listFiles(src, FileFilterUtils.fileFileFilter(),
						DirectoryFileFilter.INSTANCE);
				for (File file : files)
					zip(zipOutput, file, null);
			}
		}
		return getFile(dir);
	}

	/**
	 * <p>
	 * 文件压缩<br>
	 * Zip file
	 * </p>
	 * 
	 * <pre>
	 * ("C:/a.zip", files, true) -> C:/a.zip/a/b/c.txt -> C:/a.zip
	 * ("C:/a.zip", files, false) -> C:/a.zip/c.txt -> C:/a.zip
	 * </pre>
	 * 
	 * @param dir
	 *                <p>
	 *                .zip 文件路径<br>
	 *                .zip file path
	 *                </p>
	 * @param files
	 *                <p>
	 *                被压缩文件集合<br>
	 *                Compressed file collection
	 *                </p>
	 * @param reserve
	 *                <p>
	 *                是否保留内部结构<br>
	 *                Whether to retain the internal structure
	 *                </p>
	 * @return
	 * @throws IOException
	 */
	public static File zip(String dir, Collection<File> files, boolean reserve) throws IOException {
		try (OutputStream output = new FileOutputStream(dir);
				ZipArchiveOutputStream zipOutput = new ZipArchiveOutputStream(output)) {
			for (File file : files) {
				String path = "";
				if (reserve) {
					path = StringUtils.substringBetween(file.getAbsolutePath(), Paths.get(dir).toFile().getParent(),
							file.getName());
				}
				zip(zipOutput, file, path);
			}
		}
		return getFile(dir);
	}

	/**
	 * <p>
	 * 文件压缩<br>
	 * Zip file
	 * </p>
	 * 
	 * @param zipOutput
	 * @param src
	 *                  <p>
	 *                  源文件<br>
	 *                  Source file
	 *                  </p>
	 * @param path
	 *                  <p>
	 *                  压缩文件相对结构<br>
	 *                  Compressed file relative structure
	 *                  </p>
	 * @throws IOException
	 */
	public static void zip(ZipArchiveOutputStream zipOutput, File src, String path) throws IOException {
		try (InputStream input = new FileInputStream(src)) {
			path = path != null ? path : "";
			if (StringUtils.startsWith(path, SLASH) || StringUtils.startsWith(path, BACKSLASH))
				path = StringUtils.substring(path, 1);
			if (src.isFile()) {
				path += src.getName();
				zipOutput.putArchiveEntry(new ZipArchiveEntry(path));
				IOUtils.copy(input, zipOutput);
				zipOutput.closeArchiveEntry();
			} else if (src.isDirectory()) {
				path += src.getName() + File.separator;
				File[] _files = src.listFiles();
				if (_files.length == 0)
					zipOutput.putArchiveEntry(new ZipArchiveEntry(path));
				else {
					for (File _f : _files) {
						zip(zipOutput, _f, path);
					}
				}
			} else
				throw new IllegalArgumentException(src.toURI() + " is not a File nor a Directory.");
		}
	}

	/**
	 * <p>
	 * 解压文件<br>
	 * Unzip file
	 * </p>
	 * 
	 * @param dir
	 *             <p>
	 *             被解压文件路径<br>
	 *             Extracted file path
	 *             </p>
	 * @param path
	 *             <p>
	 *             解压文件根路径<br>
	 *             Unzip the file root path
	 *             </p>
	 * @return
	 * @throws IOException
	 */
	public static Collection<File> unzip(String dir, String path) throws IOException {
		unzip(getFile(dir), path, true);
		return FileUtils.listFiles(getFile(path), FileFilterUtils.fileFileFilter(), DirectoryFileFilter.INSTANCE);
	}

	/**
	 * <p>
	 * 解压文件<br>
	 * Unzip file
	 * </p>
	 * 
	 * @param src
	 *                <p>
	 *                源文件<br>
	 *                Source file
	 *                </p>
	 * @param path
	 *                <p>
	 *                解压文件根路径<br>
	 *                Unzip the file root path
	 *                </p>
	 * @param reserve
	 *                <p>
	 *                是否保留内部结构<br>
	 *                Whether to retain the internal structure
	 *                </p>
	 * @throws IOException
	 */
	public static void unzip(File src, String path, boolean reserve) throws IOException {
		try (InputStream input = new FileInputStream(src);
				ZipArchiveInputStream zipInput = new ZipArchiveInputStream(input);) {
			if (src.isFile()) {
				ArchiveEntry archiveEntry = null;
				while ((archiveEntry = zipInput.getNextEntry()) != null) {
					String name = archiveEntry.getName();
					if (!reserve) {
						if (name.indexOf(BACKSLASH) > -1)
							name = StringUtils.substringAfterLast(name, BACKSLASH);
						if (name.indexOf(SLASH) > -1)
							name = StringUtils.substringAfterLast(name, SLASH);
					}
					File file = getFile(path, name);
					if (file.exists())
						file = getAlias(file);
					IOUtils.copy(zipInput, new FileOutputStream(file));
				}
			} else if (src.isDirectory()) {
				Collection<File> files = FileUtils.listFiles(src, FileFilterUtils.fileFileFilter(),
						DirectoryFileFilter.INSTANCE);
				files.stream().forEach(f -> {
					try {
						unzip(f, path + FILE_SEPARATOR + FilenameUtils.getBaseName(f.getName()), reserve);
					} catch (Exception e) {
						e.printStackTrace();
					}
				});
			} else {
				throw new IllegalArgumentException(src.toURI() + " is not a File nor a Directory.");
			}
		}
	}
}
