package ezequiel.ftpserver;

import org.apache.ftpserver.ftplet.DefaultFtplet;
import org.apache.ftpserver.ftplet.FtpException;
import org.apache.ftpserver.ftplet.FtpReply;
import org.apache.ftpserver.ftplet.FtpRequest;
import org.apache.ftpserver.ftplet.FtpSession;
import org.apache.ftpserver.ftplet.FtpletResult;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static ezequiel.ftpserver.MyGetAppContext.getContext;

/**
 * Created by Ezequiel on 15/02/2018.
 */

public class RequestFtplet extends DefaultFtplet {

    /*
    public FtpletResult beforeCommand(FtpSession session, FtpRequest request)
            throws FtpException, IOException {
        String command = request.getCommand().toUpperCase();

        if ("DELE".equals(command)) {
            return onDeleteStart(session, request);
        } else if ("STOR".equals(command)) {
            return onUploadStart(session, request);
        } else if ("RETR".equals(command)) {
            return onDownloadStart(session, request);
        } else if ("RMD".equals(command)) {
            return onRmdirStart(session, request);
        } else if ("MKD".equals(command)) {
            return onMkdirStart(session, request);
        } else if ("APPE".equals(command)) {
            return onAppendStart(session, request);
        } else if ("STOU".equals(command)) {
            return onUploadUniqueStart(session, request);
        } else if ("RNTO".equals(command)) {
            return onRenameStart(session, request);
        } else if ("SITE".equals(command)) {
            return onSite(session, request);
        } else {
            // TODO should we call a catch all?
            return null;
        }
    }
        */
    public FtpletResult afterCommand(FtpSession session, FtpRequest request, FtpReply reply)
            throws FtpException, IOException {


        SimpleDateFormat formatter = new SimpleDateFormat("d MMM yyyy HH:mm:ss:SS");
        String resultTime = formatter.format(new Date());

        String address = session.getClientAddress().getAddress().toString().replace("/","");
        String messageServer = reply.getMessage();



            new DbHandle(getContext()).onInsertLog(resultTime,
                    address + " " + messageServer);

        String command = request.getCommand().toUpperCase();

        if ("PASS".equals(command)) {
            return onLogin(session, request);
        } else if ("DELE".equals(command)) {
            return onDeleteEnd(session, request);
        } else if ("STOR".equals(command)) {
            return onUploadEnd(session, request);
        } else if ("RETR".equals(command)) {
            return onDownloadEnd(session, request);
        } else if ("RMD".equals(command)) {
            return onRmdirEnd(session, request);
        } else if ("MKD".equals(command)) {
            return onMkdirEnd(session, request);
        } else if ("APPE".equals(command)) {
            return onAppendEnd(session, request);
        } else if ("STOU".equals(command)) {
            return onUploadUniqueEnd(session, request);
        } else if ("RNTO".equals(command)) {
            return onRenameEnd(session, request);
        } else {
            // TODO should we call a catch all?
            return null;
        }
    }




    @Override
    public FtpletResult onLogin(FtpSession session, FtpRequest request) throws FtpException, IOException {
       return super.onLogin(session, request);

    }

    @Override
    public FtpletResult onConnect(FtpSession session) throws FtpException, IOException {

        return FtpletResult.DEFAULT;
    }
    @Override
    public FtpletResult onDisconnect(FtpSession session) throws FtpException, IOException {

         return FtpletResult.DEFAULT;
    }
    @Override
    public FtpletResult onAppendStart(FtpSession session, FtpRequest request) throws FtpException, IOException {
       // logger.info("[FTP] appendStart " + request.getRequestLine());
        return FtpletResult.DEFAULT;
    }
    @Override
    public FtpletResult onDeleteStart(FtpSession session, FtpRequest request) throws FtpException, IOException {
       // logger.info("[FTP] deleteStart " + request.getRequestLine());
        return FtpletResult.DEFAULT;
    }
    @Override
    public FtpletResult onMkdirStart(FtpSession session, FtpRequest request) throws FtpException, IOException {
       // logger.info("[FTP] mkdirStart " + request.getRequestLine());
        return FtpletResult.DEFAULT;
    }
    @Override
    public FtpletResult onRenameStart(FtpSession session, FtpRequest request) throws FtpException, IOException {
       // logger.info("[FTP] renameStart " + request.getRequestLine());
        return FtpletResult.DEFAULT;
    }
    @Override
    public FtpletResult onRmdirStart(FtpSession session, FtpRequest request) throws FtpException, IOException {
       // logger.info("[FTP] rmdirStart " + request.getRequestLine());
        return FtpletResult.DEFAULT;
    }
    @Override
    public FtpletResult onUploadStart(FtpSession session, FtpRequest request) throws FtpException, IOException {

        SimpleDateFormat formatter = new SimpleDateFormat("d MMM yyyy HH:mm:ss:SS");
        String resultTime = formatter.format(new Date());

        String address = session.getClientAddress().getAddress().toString().replace("/","");
        String messageServer = request.getRequestLine();


        new DbHandle(getContext()).onInsertLog(resultTime,
                address + " Upload started " + messageServer);

        return FtpletResult.DEFAULT;
    }
    @Override
    public FtpletResult onUploadEnd(FtpSession session, FtpRequest request) throws FtpException, IOException {

        SimpleDateFormat formatter = new SimpleDateFormat("d MMM yyyy HH:mm:ss:SS");
        String resultTime = formatter.format(new Date());

        String address = session.getClientAddress().getAddress().toString().replace("/","");
        String messageServer = request.getRequestLine();


        new DbHandle(getContext()).onInsertLog(resultTime,
                address + " Upload finished " + messageServer);

       return FtpletResult.DEFAULT;
    }
    @Override
    public FtpletResult onUploadUniqueStart(FtpSession session, FtpRequest request) throws FtpException, IOException {
       // logger.info("[FTP] uploaduniqueStart " + request.getRequestLine());

        return FtpletResult.DEFAULT;
    }
    @Override
    public FtpletResult onUploadUniqueEnd(FtpSession session, FtpRequest request) throws FtpException, IOException {
       // logger.info("[FTP] uploaduniqueEnd " + request.getRequestLine());
        return FtpletResult.DEFAULT;
    }
    @Override
    public FtpletResult onDownloadStart(FtpSession session, FtpRequest request) throws FtpException, IOException {

        SimpleDateFormat formatter = new SimpleDateFormat("d MMM yyyy HH:mm:ss:SS");
        String resultTime = formatter.format(new Date());

        String address = session.getClientAddress().getAddress().toString().replace("/","");
        String messageServer = request.getRequestLine();


        new DbHandle(getContext()).onInsertLog(resultTime,
                address + " Download started " + messageServer);

        return FtpletResult.DEFAULT;
    }
    @Override
    public FtpletResult onDownloadEnd(FtpSession session, FtpRequest request) throws FtpException, IOException {


        SimpleDateFormat formatter = new SimpleDateFormat("d MMM yyyy HH:mm:ss:SS");
        String resultTime = formatter.format(new Date());

        String address = session.getClientAddress().getAddress().toString().replace("/","");
        String messageServer = request.getRequestLine();

        new DbHandle(getContext()).onInsertLog(resultTime,
                address + " Download finished " + messageServer);

        return FtpletResult.DEFAULT;
    }


}
