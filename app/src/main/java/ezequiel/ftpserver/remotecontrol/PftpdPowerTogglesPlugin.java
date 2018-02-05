package ezequiel.ftpserver.remotecontrol;

import android.content.Context;
import android.content.SharedPreferences;

import ezequiel.ftpserver.PrefsBean;
import ezequiel.ftpserver.prefs.LoadPrefsUtil;
import ezequiel.ftpserver.util.ServicesStartStopUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PftpdPowerTogglesPlugin extends PowerTogglesPlugin {

    protected Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    protected void changeState(Context context, boolean newState) {
        if (newState) {
            SharedPreferences prefs = LoadPrefsUtil.getPrefs(context);
            PrefsBean prefsBean = LoadPrefsUtil.loadPrefs(logger, prefs);
            ServicesStartStopUtil.startServers(context, prefsBean, null);
        } else {
            ServicesStartStopUtil.stopServers(context);
        }
        sendStateUpdate(context, newState);
    }
}
