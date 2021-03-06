package ezequiel.ftpserver.filesystem;

import org.apache.sshd.common.file.SshFile;

import java.io.IOException;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.Set;

class SshUtils {
    static Object getAttribute(SshFile sshFile, SshFile.Attribute attribute, boolean followLinks)
            throws IOException
    {
        switch (attribute) {
            case Size:
                return Long.valueOf(sshFile.getSize());
            case Uid:
                // TODO ssh uid
                return Integer.valueOf(1);
            case Owner:
                return sshFile.getOwner();
            case Gid:
                // TODO ssh gid
                return Integer.valueOf(1);
            case Group:
                return sshFile.getOwner();
            case IsDirectory:
                return Boolean.valueOf(sshFile.isDirectory());
            case IsRegularFile:
                return Boolean.valueOf(sshFile.isFile());
            case IsSymbolicLink:
                // as there is no proper sym link support in java 7, just return false, see GH issue #68
                return false;
            case Permissions:
                boolean read = sshFile.isReadable();
                boolean write = sshFile.isWritable();
                boolean exec = sshFile.isExecutable();
                Set<SshFile.Permission> tmp = new HashSet<>();
                if (read) {
                    tmp.add(SshFile.Permission.UserRead);
                    tmp.add(SshFile.Permission.GroupRead);
                    tmp.add(SshFile.Permission.OthersRead);
                }
                if (write) {
                    tmp.add(SshFile.Permission.UserWrite);
                    tmp.add(SshFile.Permission.GroupWrite);
                    tmp.add(SshFile.Permission.OthersWrite);
                }
                if (exec) {
                    tmp.add(SshFile.Permission.UserExecute);
                    tmp.add(SshFile.Permission.GroupExecute);
                    tmp.add(SshFile.Permission.OthersExecute);
                }
                return tmp.isEmpty()
                        ? EnumSet.noneOf(SshFile.Permission.class)
                        : EnumSet.copyOf(tmp);
            case CreationTime:
                // TODO ssh creation time
                return Long.valueOf(sshFile.getLastModified());
            case LastModifiedTime:
                return Long.valueOf(sshFile.getLastModified());
            case LastAccessTime:
                // TODO ssh access time
                return Long.valueOf(sshFile.getLastModified());
            default:
                return null;
        }
    }
}
