package mika.com.android.ac.quote.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import mika.com.android.ac.R;
import mika.com.android.ac.util.FragmentUtils;

public class QuoteActivity extends AppCompatActivity implements
        QuoteFragment.OnQuoteInteractionListener{

    private QuoteFragment quoteFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quote);

        quoteFragment = new QuoteFragment();
        FragmentUtils.add(quoteFragment, this, R.id.activity_quote);
    }
}
