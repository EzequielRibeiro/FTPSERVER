package ezequiel.ftpserver.filepicker;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nononsenseapps.filepicker.FilePickerFragment;

import ezequiel.ftpserver.R;
import ezequiel.ftpserver.util.Defaults;

public class ResettingFilePickerFragment extends FilePickerFragment {

    protected View inflateRootView(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.resetting_filepicker, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        view.findViewById(R.id.button_reset).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToDir(Defaults.HOME_DIR);
            }
        });
    }
}
