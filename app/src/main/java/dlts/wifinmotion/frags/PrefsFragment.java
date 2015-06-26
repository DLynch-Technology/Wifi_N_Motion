package dlts.wifinmotion.frags;

import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceScreen;

import dlts.wifinmotion.R;

public class PrefsFragment extends PreferenceFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Load the preferences from an XML resource
        addPreferencesFromResource(R.xml.preferences);

        PreferenceScreen ps = (PreferenceScreen) findPreference(getResources().getString(R.string.preffrag));
        Preference chbox1 = (Preference) findPreference(getResources().getString(R.string.is_service));

    }
}