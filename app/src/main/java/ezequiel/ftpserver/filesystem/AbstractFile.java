package ezequiel.ftpserver.filesystem;

import org.apache.sshd.common.file.SshFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public abstract class AbstractFile {

    protected final Logger logger = LoggerFactory.getLogger(getClass());

    protected boolean isDirectory;
    protected String absPath;

    protected String name;
    protected long lastModified;
    protected long size;
    protected boolean readable;
    protected boolean exists;

    public AbstractFile(String absPath, String name, long lastModified, long size, boolean readable, boolean exists, boolean isDirectory) {
        this.absPath = absPath;
        this.name = name;
        this.lastModified = lastModified;
        this.size = size;
        this.readable = readable;
        this.exists = exists;
        this.isDirectory = isDirectory;
    }

    public String getAbsolutePath() {
        logger.trace("[{}] getAbsolutePath() -> '{}'", name, absPath);
        return absPath;
    }

    public String getName() {
        logger.trace("[{}] getName()", name);
        return name;
    }

    public boolean isDirectory() {
        logger.trace("[{}] isDirectory() -> {}", name, isDirectory);
        return isDirectory;
    }

    public boolean doesExist() {
        logger.trace("[{}] doesExist() -> {}", name, exists);
        return exists;
    }

    public boolean isReadable() {
        logger.trace("[{}] isReadable() -> {}", name, readable);
        return readable;
    }

    public long getLastModified() {
        logger.trace("[{}] getLastModified() -> {}", name, lastModified);
        return lastModified;
    }

    public long getSize() {
        logger.trace("[{}] getSize() -> {}", name, size);
        return size;
    }

    ///////////////////////////////////////////////////////////////////////////
    // ftp
    ///////////////////////////////////////////////////////////////////////////

    public Object getPhysicalFile() {
        return this;
    }

    public int getLinkCount() {
        logger.trace("[{}] getLinkCount()", name);
        return 0;
    }

    public boolean isHidden() {
        logger.trace("[{}] isHidden()", name);
        return name.charAt(0) == '.';
    }

    ///////////////////////////////////////////////////////////////////////////
    // ssh
    ///////////////////////////////////////////////////////////////////////////

    public void handleClose() throws IOException {
        // TODO ssh handleClose
        logger.trace("[{}] handleClose()", name);
    }

    public void truncate() throws IOException {
        // TODO ssh truncate
        logger.trace("[{}] truncate()", name);
    }

    public String readSymbolicLink() throws IOException {
        logger.trace("[{}] readSymbolicLink()", name);
        //return null;
        // returning null causes issues with some clients, e.g. GH issue #121
        // let's try empty string and see what users report
        return "";
    }

    public void createSymbolicLink(SshFile arg0)
            throws IOException
    {
        // TODO ssh createSymbolicLink
        logger.trace("[{}] createSymbolicLink()", name);
    }

    public void setAttribute(SshFile.Attribute attribute, Object value) throws IOException {
        // TODO ssh setAttribute
        logger.trace("[{}] setAttribute()", name);
    }

    public void setAttributes(Map<SshFile.Attribute, Object> attributes) throws IOException {
        // TODO ssh setAttributes
        logger.trace("[{}] setAttributes()", name);
    }

    public Object getAttribute(SshFile.Attribute attribute, boolean followLinks)
            throws IOException
    {
        logger.trace("[{}] getAttribute({})", name, attribute);
        return SshUtils.getAttribute((SshFile)this, attribute, followLinks);
    }

    public Map<SshFile.Attribute, Object> getAttributes(boolean followLinks)
            throws IOException
    {
        logger.trace("[{}] getAttributes()", name);

        Map<SshFile.Attribute, Object> attributes = new HashMap<>();
        for (SshFile.Attribute attr : SshFile.Attribute.values()) {
            attributes.put(attr, getAttribute(attr, followLinks));
        }

        return attributes;
    }

    public boolean isExecutable() {
        logger.trace("[{}] isExecutable()", name);
        return false;
    }

    public boolean create() throws IOException {
        logger.trace("[{}] create()", name);
        // called e.g. when uploading a new file
        return true;
    }

}
