/*  Copyright (C) 2015-2018 Andreas Shimokawa, Carsten Pfeiffer, Lem Dulfo

    This file is part of Gadgetbridge.

    Gadgetbridge is free software: you can redistribute it and/or modify
    it under the terms of the GNU Affero General Public License as published
    by the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    Gadgetbridge is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Affero General Public License for more details.

    You should have received a copy of the GNU Affero General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>. */
package nodomain.knu2018.bandutils.activities;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;

import java.util.Locale;

import nodomain.knu2018.bandutils.GBApplication;
import nodomain.knu2018.bandutils.R;
import nodomain.knu2018.bandutils.util.AndroidUtils;


/**
 * The type Abstract gb activity.
 */
public abstract class AbstractGBActivity extends AppCompatActivity implements GBActivity {
    private boolean isLanguageInvalid = false;

    /**
     * The constant NONE.
     */
    public static final int NONE = 0;
    /**
     * The constant NO_ACTIONBAR.
     */
    public static final int NO_ACTIONBAR = 1;

    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action == null) {
                return;
            }
            switch (action) {
                case GBApplication.ACTION_LANGUAGE_CHANGE:
                    setLanguage(GBApplication.getLanguage(), true);
                    break;
                case GBApplication.ACTION_QUIT:
                    finish();
                    break;
            }
        }
    };

    public void setLanguage(Locale language, boolean invalidateLanguage) {
        if (invalidateLanguage) {
            isLanguageInvalid = true;
        }
        AndroidUtils.setLanguage(this, language);
    }

    /**
     * Init.
     *
     * @param activity the activity
     */
    public static void init(GBActivity activity) {
        init(activity, NONE);
    }

    /**
     * Init.
     *
     * @param activity the activity
     * @param flags    the flags
     */
    public static void init(GBActivity activity, int flags) {
        if (GBApplication.isDarkThemeEnabled()) {
            if ((flags & NO_ACTIONBAR) != 0) {
                activity.setTheme(R.style.GadgetbridgeThemeDark_NoActionBar);
            } else {
                activity.setTheme(R.style.GadgetbridgeThemeDark);
            }
        } else {
            if ((flags & NO_ACTIONBAR) != 0) {
                activity.setTheme(R.style.GadgetbridgeTheme_NoActionBar);
            } else {
                activity.setTheme(R.style.GadgetbridgeTheme);
            }
        }
        activity.setLanguage(GBApplication.getLanguage(), false);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        IntentFilter filterLocal = new IntentFilter();
        filterLocal.addAction(GBApplication.ACTION_QUIT);
        filterLocal.addAction(GBApplication.ACTION_LANGUAGE_CHANGE);
        LocalBroadcastManager.getInstance(this).registerReceiver(mReceiver, filterLocal);

        init(this);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isLanguageInvalid) {
            isLanguageInvalid = false;
            recreate();
        }
    }

    @Override
    protected void onDestroy() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mReceiver);
        super.onDestroy();
    }
}
