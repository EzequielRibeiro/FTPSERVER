package ezequiel.ftpserver.filesystem;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;

import org.apache.sshd.common.Session;
import org.apache.sshd.common.file.SshFile;

import java.util.List;

public class RoSafSshFile extends RoSafFile<SshFile> implements SshFile {

    private final Session session;

    public RoSafSshFile(
            ContentResolver contentResolver,
            Uri startUrl,
            String absPath,
            Session session) {
        super(contentResolver, startUrl, absPath);
        this.session = session;
    }

    public RoSafSshFile(
            ContentResolver contentResolver,
            Uri startUrl,
            String docId,
            String absPath,
            boolean exists,
            Session session) {
        super(contentResolver, startUrl, docId, absPath, exists);
        this.session = session;
    }

    public RoSafSshFile(
            ContentResolver contentResolver,
            Uri startUrl,
            Cursor cursor,
            String absPath,
            Session session) {
        super(contentResolver, startUrl, cursor, absPath);
        this.session = session;
    }

    @Override
    protected SshFile createFile(
            ContentResolver contentResolver,
            Uri startUrl,
            Cursor cursor,
            String absPath) {
        return new RoSafSshFile(contentResolver, startUrl, cursor, absPath, session);
    }

    @Override
    public boolean move(SshFile target) {
        logger.trace("move()");
        return super.move((RoSafFile)target);
    }

    @Override
    public String getOwner() {
        logger.trace("[{}] getOwner()", name);
        return session.getUsername();
    }

    @Override
    public SshFile getParentFile() {
        logger.trace("[{}] getParentFile()", name);
        return null;
    }

    @Override
    public List<SshFile> listSshFiles() {
        return listFiles();
    }
}
