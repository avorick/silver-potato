package com.app.framework.utilities;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.framework.R;
import com.app.framework.constants.Constants;
import com.app.framework.sharedpref.SharedPref;

/**
 * Created by LJTat on 2/23/2017.
 */
public class AppRaterUtil {

    private static final int DAYS_UNTIL_PROMPT = 3;
    private static final int LAUNCHES_UNTIL_PROMPT = 7;
    private Context mContext;

    public AppRaterUtil(Context context) {
        mContext = context;
        recordAppLaunchDate();
    }

    private void recordAppLaunchDate() {
        SharedPref prefs = new SharedPref(mContext, Constants.PREF_FILE_NAME);
        if (prefs.getBooleanPref(Constants.KEY_APP_LAUNCH, false)) {
            return;
        }

        // Increment launch counter
        long launchCount = prefs.getLongPref(Constants.KEY_APP_LAUNCH_COUNT, 0) + 1;
        prefs.setPref(Constants.KEY_APP_LAUNCH_COUNT, launchCount);

        // Get date of first launch
        Long dateFirstLaunch = prefs.getLongPref(Constants.KEY_APP_LAUNCH_DATE, 0);
        if (dateFirstLaunch == 0) {
            dateFirstLaunch = System.currentTimeMillis();
            prefs.setPref(Constants.KEY_APP_LAUNCH_DATE, dateFirstLaunch);
        }

        // Wait at least n days before opening
        if (launchCount >= LAUNCHES_UNTIL_PROMPT) {
            if (System.currentTimeMillis() >= dateFirstLaunch
                    + (DAYS_UNTIL_PROMPT * 24 * 60 * 60 * 1000)) {
                showRateDialog();
            }
        }
    }

    private void showRateDialog() {
        final Dialog dialog = new Dialog(mContext);
        dialog.setTitle("Rate ".concat(mContext.getResources().getString(R.string.app_name)));

        LinearLayout ll = new LinearLayout(mContext);
        ll.setOrientation(LinearLayout.VERTICAL);

        TextView tv = new TextView(mContext);
        tv.setText("If you enjoy using ".concat(mContext.getResources().getString(R.string.app_name)).concat(
                ", please take a moment to rate it. Thanks for your support!\n\n"));
        Color.parseColor("#FFFFFF");
        tv.setBackgroundColor(Color.parseColor("#FFFFFF"));
        tv.setWidth(340);
        tv.setPadding(6, 0, 6, 12);
        ll.addView(tv);

        Button b1 = new Button(mContext);
        b1.setText("Rate ".concat(mContext.getResources().getString(R.string.app_name)));
        b1.setBackgroundResource(R.drawable.custom_button);
        b1.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                mContext.startActivity(new Intent(Intent.ACTION_VIEW, Uri
                        .parse("market://details?id=".concat(mContext.getPackageName()))));
                dialog.dismiss();
            }
        });
        ll.addView(b1);

        Button b2 = new Button(mContext);
        b2.setText("Remind me later");
        b2.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        ll.addView(b2);

        Button b3 = new Button(mContext);
        b3.setText("No, thanks");
        b3.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        ll.addView(b3);
        dialog.setContentView(ll);
        dialog.show();
    }
}
