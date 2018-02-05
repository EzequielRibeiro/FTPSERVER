package ezequiel.ftpserver.filesystem;

import org.apache.ftpserver.ftplet.FtpFile;
import org.apache.ftpserver.ftplet.User;
import ezequiel.ftpserver.pojo.LsOutputBean;

import eu.chainfire.libsuperuser.Shell;

public class RootFtpFile extends RootFile<FtpFile> implements FtpFile {

    private final User user;

    public RootFtpFile(Shell.Interactive shell, LsOutputBean bean, String absPath, User user) {
        super(shell, bean, absPath);
        this.user = user;
    }

    protected RootFtpFile createFile(Shell.Interactive shell, LsOutputBean bean, String absPath) {
        return new RootFtpFile(shell, bean, absPath, user);
    }

    @Override
    public boolean move(FtpFile target) {
        logger.trace("move()");
        return super.move((RootFile)target);
    }

    @Override
    public String getOwnerName() {
        logger.trace("[{}] getOwnerName()", name);
        return user.getName();
    }

    @Override
    public String getGroupName() {
        logger.trace("[{}] getGroupName()", name);
        return user.getName();
    }
}
