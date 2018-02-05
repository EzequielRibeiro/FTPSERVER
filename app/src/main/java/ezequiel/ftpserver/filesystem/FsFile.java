package ezequiel.ftpserver.filesystem;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;

public abstract class FsFile<T> extends AbstractFile {

	protected final File file;

	public FsFile(File file) {
		super(
				file.getAbsolutePath(),
				file.getName(),
				file.lastModified(),
				file.length(),
				file.canRead(),
				file.exists(),
				file.isDirectory());
		this.file = file;
		this.name = file.getName();
	}

	protected abstract T createFile(File file);

	public boolean isFile() {
		boolean isFile = file.isFile();
		logger.trace("[{}] isFile() -> {}", name, isFile);
		return isFile;
	}

	public boolean doesExist() {
		boolean exists = file.exists();
		boolean existsChecked = exists;
		if (!exists) {
			// exists may be false when we don't have read permission
			// try to figure out if it really does not exist
			File parentFile = file.getParentFile();
			File[] children = parentFile.listFiles();
			if (children != null) {
				for (File child : children) {
					if (file.equals(child)) {
						existsChecked = true;
						break;
					}
				}
			}
		}
		logger.trace(
			"[{}] doesExist(), ({}): orig val: {}, checked val: {}",
			new Object[]{
				name,
				file.getAbsolutePath(),
				Boolean.valueOf(exists),
				Boolean.valueOf(existsChecked)
			});
		return existsChecked;
	}

	public boolean isReadable() {
		boolean canRead = file.canRead();
		logger.trace("[{}] isReadable() -> {}", name, canRead);
		return canRead;
	}

	public boolean isWritable() {
		logger.trace("[{}] writable: {}, exists: {}, file: '{}'",
			new Object[]{
				name,
				file.canWrite(),
				file.exists(),
				file.getName()
			});

		if (file.exists()) {
			return file.canWrite();
		}

		// file does not exist, probably an upload of a new file, check parent
		// must be done in loop as some clients to not issue mkdir commands
		// like filezilla
		File parent = file.getParentFile();
		while (parent != null) {
			if (parent.exists()) {
				return parent.canWrite();
			}
			parent = parent.getParentFile();
		}
		return false;
	}

	public boolean isRemovable() {
		logger.trace("[{}] isRemovable()", name);
		return file.canWrite();
	}

	public boolean setLastModified(long time) {
		logger.trace("[{}] setLastModified({})", name, Long.valueOf(time));
		return file.setLastModified(time);
	}

	public boolean mkdir() {
		logger.trace("[{}] mkdir()", name);
		return file.mkdir();
	}

	public boolean delete() {
		logger.trace("[{}] delete()", name);
		return file.delete();
	}

	public boolean move(FsFile<T> destination) {
		logger.trace("[{}] move({})", name, destination.getAbsolutePath());
		return file.renameTo(new File(destination.getAbsolutePath()));
	}

	public List<T> listFiles() {
		logger.trace("[{}] listFiles()", name);
		File[] filesArray = file.listFiles();
		if (filesArray != null) {
			List<T> files = new ArrayList<>(filesArray.length);
			for (File file : filesArray) {
				files.add(createFile(file));
			}
			return files;
		}
		logger.debug("file.listFiles() returned null. Path: {}", file.getAbsolutePath());
		return new ArrayList<>(0);
	}

	public static final int BUFFER_SIZE = 1024 * 1024;

	public OutputStream createOutputStream(long offset) throws IOException {
		logger.trace("[{}] createOutputStream({})", name, offset);

		// may be necessary to create dirs
		// see isWritable()
		File parent = file.getParentFile();
		if (!parent.exists()) {
			parent.mkdirs();
		}

		// now create out stream
		OutputStream os = null;
		if (offset == 0) {
			os = new FileOutputStream(file);
		} else if (offset == this.file.length()) {
			os = new FileOutputStream(file, true);
		} else {
			final RandomAccessFile raf = new RandomAccessFile(this.file, "rw");
			raf.seek(offset);
			os = new OutputStream() {
				@Override
				public void write(int oneByte) throws IOException {
					raf.write(oneByte);
				}
				@Override
				public void close() throws IOException {
					raf.close();
				}
			};
		}

		return new BufferedOutputStream(os, BUFFER_SIZE);
	}

	public InputStream createInputStream(long offset) throws IOException {
		logger.trace("[{}] createInputStream(), offset: {}, file: {}",
				new Object []{
						name,
						offset,
						file.getAbsolutePath()
		});
		FileInputStream fis = new FileInputStream(file);
		fis.skip(offset);
		return new BufferedInputStream(fis, BUFFER_SIZE);
	}
}
