package ezequiel.ftpserver.log;

import ezequiel.ftpserver.prefs.Logging;
import org.slf4j.ILoggerFactory;
import org.slf4j.helpers.NOPLoggerFactory;
import ezequiel.ftpserver.slf4j.src.org.src.impl.AndroidLoggerFactory;
import org.slf4j.spi.LoggerFactoryBinder;

public class PrimFtpdLoggerBinder implements LoggerFactoryBinder
{
	private static Logging loggingPref = Logging.NONE;

	private final ILoggerFactory loggerFactoryNone;
	private final ILoggerFactory loggerFactoryAndroid;
	private final ILoggerFactory loggerFactoryText;

	protected PrimFtpdLoggerBinder()
	{
		loggerFactoryNone = new NOPLoggerFactory();
		loggerFactoryAndroid = new AndroidLoggerFactory();
		loggerFactoryText = new CsvLoggerFactory(loggerFactoryNone);
	}

	@Override
	public ILoggerFactory getLoggerFactory()
	{
		switch (loggingPref)
		{
		case NONE:
			return loggerFactoryNone;
		case ANDROID:
			return loggerFactoryAndroid;
		case TEXT:
			return loggerFactoryText;
		}
		return loggerFactoryAndroid;
	}

	@Override
	public String getLoggerFactoryClassStr()
	{
		return getLoggerFactory().getClass().getName();
	}

	public static void setLoggingPref(Logging loggingPref)
	{
		PrimFtpdLoggerBinder.loggingPref = loggingPref;
	}

	public static String REQUESTED_API_VERSION = "1.6"; // !final, see StaticLoggerBinder

	private static final PrimFtpdLoggerBinder SINGLETON = new PrimFtpdLoggerBinder();

	public static final PrimFtpdLoggerBinder getSingleton()
	{
		return SINGLETON;
	}
}
