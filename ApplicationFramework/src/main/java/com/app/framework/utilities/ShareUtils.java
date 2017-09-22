package com.app.framework.utilities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;

import com.app.framework.R;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;
import com.app.framework.enums.Enum;

import java.util.List;

/**
 * Created by LJTat on 3/4/2017.
 */

public class ShareUtils {

    // social media packages
    private static final String FACEBOOK_PACKAGE = "com.facebook.katana";
    private static final String TWITTER_PACKAGE = "com.twitter";
    private static final String LINKEDIN_PACKAGE = "com.linkedin.android";
    private static final String LINKEDIN_DEEP_LINK_PACKAGE = "com.linkedin.android.infra.deeplink.DeepLinkHelperActivity";
    private static final String TWITTER_MSG_FORMAT = "https://twitter.com/intent/tweet?text=%s&amp;url=%s";

    // personal social media
    private static final String FACEBOOK_PERSONAL_PAGE = "https://www.facebook.com/drxeno02";
    private static final String TWITTER_PERSONAL_PAGE = "https://twitter.com/drxeno02";
    private static final String LINKEDIN_PERSONAL_PAGE = "http://www.linkedin.com/profile/view?id=leonard-tatum-768850105";
    private static final String FACEBOOK_PERSONAL_URI = "fb://facewebmodal/f?href=".concat(FACEBOOK_PERSONAL_PAGE);
    private static final String FACEBOOK_PERSONAL_URI_LEGACY = "fb://page/drxeno02";
    private static final String TWITTER_PERSONAL_URI = "twitter://user?drxeno02";
    private static final String LINKEDIN_PERSONAL_URI = "linkedin://leonard-tatum-768850105";

    // intent
    private static final String INTENT_TYPE_TEXT = "text/*";

    /**
     * Method is used to open social media via intents
     *
     * Facebook/Twitter profile id: drxeno02
     * Linkedin profile id: leonard-tatum-768850105
     *
     * @param socialMedia
     */
    public static void openSocialMediaViaIntent(Context context, Enum.SocialMedia socialMedia, boolean isPersonalSocialMedia) {
        // create intent object
        Intent intent = null;
        // create packageManager object
        final PackageManager packageManager = context.getPackageManager();
        try {
            if (isPersonalSocialMedia) {
                if (socialMedia.equals(Enum.SocialMedia.FB)) {
                    int versionCode = packageManager.getPackageInfo(FACEBOOK_PACKAGE, 0).versionCode;
                    String uri;
                    if (versionCode >= 3002850) { //newer versions of fb app
                        uri = FACEBOOK_PERSONAL_URI;
                    } else { //older versions of fb app
                        uri = FACEBOOK_PERSONAL_URI_LEGACY;
                    }
                    intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                } else if (socialMedia.equals(Enum.SocialMedia.TWITTER)) {
                    intent = new Intent(Intent.ACTION_VIEW, Uri.parse(TWITTER_PERSONAL_URI));
                    final List<ResolveInfo> matches = packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
                    if (!FrameworkUtils.checkIfNull(matches) && matches.size() > 0 && !matches.isEmpty()) {
                        // search for package and set intent package
                        for (ResolveInfo info : matches) {
                            if (info.activityInfo.packageName.toLowerCase().startsWith(TWITTER_PACKAGE)) {
                                intent.setPackage(info.activityInfo.packageName);
                            }
                        }
                    } else {
                        // otherwise open browser
                        intent = new Intent(Intent.ACTION_VIEW, Uri.parse(TWITTER_PERSONAL_PAGE));
                    }
                } else if (socialMedia.equals(Enum.SocialMedia.LINKEDIN)) {
                    intent = new Intent(Intent.ACTION_VIEW, Uri.parse(LINKEDIN_PERSONAL_URI));
                    final List<ResolveInfo> matches = packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
                    if (!FrameworkUtils.checkIfNull(matches) && matches.size() > 0 && !matches.isEmpty()) {
                        // search for package and set intent package
                        for (ResolveInfo info : matches) {
                            if (info.activityInfo.packageName.toLowerCase().startsWith(LINKEDIN_PACKAGE)) {
                                intent.setPackage(info.activityInfo.packageName);
                            }
                        }
                    } else {
                        // otherwise open browser
                        intent = new Intent(Intent.ACTION_VIEW, Uri.parse(LINKEDIN_PERSONAL_PAGE));
                    }
                }
            } else {
                if (socialMedia.equals(Enum.SocialMedia.FB)) {
                    // share dialog
                    ShareDialog shareDialog = new ShareDialog((Activity) context);
                    // share link content
                    ShareLinkContent shareLinkContent = new ShareLinkContent.Builder()
                            .setContentUrl(Uri.parse(context.getResources().getString(R.string.fb_share_msg)))
                            .build();
                    // display share dialog
                    shareDialog.show(shareLinkContent, ShareDialog.Mode.AUTOMATIC);
                } else if (socialMedia.equals(Enum.SocialMedia.TWITTER)) {
                    String tweetMsg = String.format(TWITTER_MSG_FORMAT,
                            FrameworkUtils.urlEncode(context.getResources().getString(R.string.twitter_share_msg)),
                            FrameworkUtils.urlEncode(context.getResources().getString(R.string.google_play_link)));
                    intent = new Intent(Intent.ACTION_SEND, Uri.parse(tweetMsg));
                    List<ResolveInfo> matches = packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
                    if (!FrameworkUtils.checkIfNull(matches) && matches.size() > 0 && !matches.isEmpty()) {
                        for (ResolveInfo info : matches) {
                            if (info.activityInfo.packageName.toLowerCase().startsWith(TWITTER_PACKAGE)) {
                                intent.setPackage(info.activityInfo.packageName);
                            }
                        }
                    }
                } else if (socialMedia.equals(Enum.SocialMedia.LINKEDIN)) {
                    intent = new Intent(Intent.ACTION_SEND);
                    intent.setClassName(LINKEDIN_PACKAGE, LINKEDIN_DEEP_LINK_PACKAGE);
                    intent.setType(INTENT_TYPE_TEXT);
                    intent.putExtra(android.content.Intent.EXTRA_TEXT,
                            context.getResources().getString(R.string.linkedin_share_msg));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (!FrameworkUtils.checkIfNull(intent)) {
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        }
    }
}
