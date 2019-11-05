package com.gettipsi.stripe;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.customtabs.CustomTabsIntent;

/**
 * Created by remer on 16/11/17.
 */
public class OpenBrowserActivity extends Activity {
  final static String EXTRA_URL = "url";

  private String url;
  private boolean shouldFinish = true;

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    shouldFinish = false;

//    custom
    url = getIntent().getStringExtra(EXTRA_URL);
    CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
    CustomTabsIntent customTabsIntent = builder.build();
    customTabsIntent.launchUrl(this, Uri.parse(url));
//    custom
  }

  @Override
  protected void onResume() {
    super.onResume();
    if (shouldFinish) {
      StripeModule.getInstance().processRedirect(null);
      finish();
    }
    shouldFinish = true;
  }
  // custom
  @Override
  protected void onNewIntent(Intent intent) {
    StripeModule.getInstance().processRedirect(intent.getData());
    finish();
  }
//  custom
}
