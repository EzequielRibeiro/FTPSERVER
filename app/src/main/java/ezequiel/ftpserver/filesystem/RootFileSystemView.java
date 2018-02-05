package ezequiel.ftpserver.filesystem;

import ezequiel.ftpserver.pojo.LsOutputBean;
import ezequiel.ftpserver.pojo.LsOutputParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import eu.chainfire.libsuperuser.Shell;

public abstract class RootFileSystemView<T extends RootFile<X>, X> {

    protected final Logger logger = LoggerFactory.getLogger(getClass());

    protected final Shell.Interactive shell;

    public RootFileSystemView(Shell.Interactive shell) {
        this.shell = shell;
    }

    protected abstract T createFile(LsOutputBean bean, String absPath);

    protected abstract String absolute(String file);

    public T getFile(String file) {
        logger.trace("getFile({})", file);

        file = absolute(file);

        final LsOutputParser parser = new LsOutputParser();
        final LsOutputBean[] wrapper = new LsOutputBean[1];
        shell.addCommand("ls -lAd \"" + file + "\"", 0, new Shell.OnCommandLineListener() {
            @Override
            public void onLine(String s) {
                wrapper[0] = parser.parseLine(s);
            }
            @Override
            public void onCommandResult(int i, int i1) {
            }
        });
        shell.waitForIdle();
        LsOutputBean bean = wrapper[0];
        if (bean != null) {
            if (bean.isLink()) {
                bean = findFinalLinkTarget(bean, parser);
                // TODO make sym link target absolute
                file = bean.getName();
            }
            return createFile(bean, file);
        } else {
            // probably new
            String name;
            if (file.contains("/")) {
                name = file.substring(file.lastIndexOf('/') + 1, file.length());
            } else {
                name = file;
            }
            bean = new LsOutputBean(name);
            return createFile(bean, file);
        }
    }

    protected LsOutputBean findFinalLinkTarget(LsOutputBean bean, final LsOutputParser parser ) {
        LsOutputBean tmp = bean;
        final LsOutputBean[] wrapper = new LsOutputBean[1];
        int i=0;
        while (tmp.isLink()) {
            shell.addCommand("ls -lAd \"" + tmp.getLinkTarget() + "\"", 0, new Shell.OnCommandLineListener() {
                @Override
                public void onLine(String s) {
                    wrapper[0] = parser.parseLine(s);
                }
                @Override
                public void onCommandResult(int i, int i1) {
                }
            });
            shell.waitForIdle();

            tmp = wrapper[0];
            i++;
            if (i > 20) {
                break;
            }
        }
        return tmp;
    }

}
