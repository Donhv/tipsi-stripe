package com.gettipsi.stripe;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;

import com.stripe.android.CustomerSession;
import com.stripe.android.EphemeralKeyProvider;
import com.stripe.android.EphemeralKeyUpdateListener;
import com.stripe.android.PaymentConfiguration;
import com.stripe.android.model.PaymentMethod;
import com.stripe.android.view.PaymentMethodsActivity;
import com.stripe.android.view.PaymentMethodsActivityStarter;

public class SelectCardActivity extends Activity {
    private static final int REQUEST_CODE_SELECT_SOURCE = 50;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_cart_activity);
        String stripePublishableKey = getIntent().getStringExtra("stripePublishableKey");
        final String stripeEphemeralKey = getIntent().getStringExtra("stripeEphemeralKey");
        PaymentConfiguration.init(stripePublishableKey);
        CustomerSession.initCustomerSession(this, new EphemeralKeyProvider() {
            @Override
            public void createEphemeralKey(@NonNull String apiVersion, @NonNull EphemeralKeyUpdateListener keyUpdateListener) {
                keyUpdateListener.onKeyUpdate(stripeEphemeralKey);
                init();
            }
        });
    }

    private void init(){
        new PaymentMethodsActivityStarter(this).startForResult(REQUEST_CODE_SELECT_SOURCE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == REQUEST_CODE_SELECT_SOURCE && resultCode == RESULT_OK){
            final PaymentMethod paymentMethod = data.getParcelableExtra(PaymentMethodsActivity.EXTRA_SELECTED_PAYMENT);
            StripeModule.getInstance().processSelectCard(paymentMethod);
            finish();
        } else {
            StripeModule.getInstance().processSelectCard(null);
            finish();
        }
    }
}
