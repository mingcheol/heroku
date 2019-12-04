package migong.seoulthings.ui.search;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import java.util.List;
import migong.seoulthings.R;
import migong.seoulthings.data.Category;
import migong.seoulthings.ui.donation.DonationActivity;
import migong.seoulthings.ui.donation.DonationView;
import migong.seoulthings.ui.search.adapter.SearchResultRecyclerAdapter;
import migong.seoulthings.ui.thing.ThingActivity;
import migong.seoulthings.ui.thing.ThingView;
import org.apache.commons.lang3.StringUtils;

public class SearchActivity extends AppCompatActivity implements SearchView {

  private static final String TAG = SearchActivity.class.getSimpleName();

  private ImageButton mBackButton;
  private EditText mQueryEditText;
  private ImageButton mClearQueryButton;
  private ContentLoadingProgressBar mProgressBar;
  private TextView mEmptyView;
  private RecyclerView mResultRecyclerView;
  private SearchResultRecyclerAdapter mResultRecyclerAdapter;

  private String mScope;
  @Nullable
  private String mCategory;
  private SearchPresenter mPresenter;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.search_activity);

    final Intent intent = getIntent();
    final Bundle args = intent.getExtras();
    if (args == null) {
      Log.e(TAG, "onCreate: args is NULL.");
      finish();
      return;
    }

    mScope = args.getString(KEY_SCOPE);
    mCategory = args.getString(KEY_CATEGORY);
    if (StringUtils.isEmpty(mScope)) {
      Log.e(TAG, "onCreate: scope is empty. scope is " + mScope);
      finish();
      return;
    }

    setupQueryView();
    setupInteraction();
    setupRecycler();

    switch (mScope) {
      case SCOPE_THINGS:
        mPresenter = new SearchThingsPresenter(this, mCategory);
        mPresenter.onCreate(savedInstanceState);
        break;
      case SCOPE_DONATIONS:
        mPresenter = new SearchDonationsPresenter(this);
        mPresenter.onCreate(savedInstanceState);
        break;
      default:
        Log.e(TAG, "onCreate: invalid scope. scope is " + mScope);
        finish();
        break;
    }
  }

  @Override
  protected void onResume() {
    super.onResume();
    if (mPresenter != null) {
      mPresenter.onResume();
    }

    mQueryEditText.requestFocus();
  }

  @Override
  protected void onPause() {
    super.onPause();
    if (mPresenter != null) {
      mPresenter.onPause();
    }
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    if (mPresenter != null) {
      mPresenter.onDestroy();
    }
  }

  private void setupQueryView() {
    mBackButton = findViewById(R.id.search_back_button);
    mBackButton.setOnClickListener(v -> onBackPressed());

    mQueryEditText = findViewById(R.id.search_query_edittext);
    mQueryEditText.addTextChangedListener(new TextWatcher() {
      @Override
      public void beforeTextChanged(CharSequence s, int start, int count, int after) {

      }

      @Override
      public void onTextChanged(CharSequence s, int start, int before, int count) {

      }

      @Override
      public void afterTextChanged(Editable s) {
        if (s == null) {
          return;
        }

        mPresenter.onQueryChanged(s.toString());
      }
    });
    switch (mScope) {
      case SCOPE_THINGS:
        if (StringUtils.equals(Category.ALL, mCategory)) {
          mQueryEditText.setHint(R.string.msg_search_things);
        } else {
          final String category = Category.valueOf(this, mCategory);
          mQueryEditText.setHint(getString(R.string.msg_search_things_category, category));
        }
        break;
      case SCOPE_DONATIONS:
        mQueryEditText.setHint(R.string.msg_search_donations);
        break;
    }

    mClearQueryButton = findViewById(R.id.search_clear_query_button);
    mClearQueryButton.setOnClickListener(v -> mPresenter.onClearQueryButtonClicked());
  }

  private void setupInteraction() {
    mProgressBar = findViewById(R.id.search_result_progressbar);
    mProgressBar.hide();

    mEmptyView = findViewById(R.id.search_result_empty);
    mEmptyView.setVisibility(View.GONE);
  }

  private void setupRecycler() {
    mResultRecyclerView = findViewById(R.id.search_result_recycler);
    mResultRecyclerView.setHasFixedSize(true);
    mResultRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    mResultRecyclerView.addItemDecoration(new DividerItemDecoration(
        mResultRecyclerView.getContext(), LinearLayout.VERTICAL));

    mResultRecyclerAdapter = new SearchResultRecyclerAdapter(
        id -> mPresenter.onSearchResultRecyclerViewHolderClicked(id)
    );
    mResultRecyclerView.setAdapter(mResultRecyclerAdapter);
  }

  @Override
  public void startThingActivity(@NonNull String thingId) {
    Intent intent = new Intent(this, ThingActivity.class);

    Bundle args = new Bundle();
    args.putString(ThingView.KEY_THING_ID, thingId);
    intent.putExtras(args);

    startActivity(intent);
  }

  @Override
  public void startDonationActivity(@NonNull String donationId) {
    Intent intent = new Intent(this, DonationActivity.class);

    Bundle args = new Bundle();
    args.putString(DonationView.KEY_DONATION_ID, donationId);
    intent.putExtras(args);

    startActivity(intent);
  }

  @Override
  public void changeSearchResult(List<SearchResult> searchResults) {
    mResultRecyclerAdapter.changeDataSet(searchResults);
    mResultRecyclerAdapter.notifyDataSetChanged();
  }

  @Override
  public void clearSearchResult() {
    changeSearchResult(null);
  }

  @Override
  public void clearQuery() {
    mQueryEditText.setText(StringUtils.EMPTY);
  }

  @Override
  public void showClearQueryButton() {
    mClearQueryButton.setVisibility(View.VISIBLE);
  }

  @Override
  public void hideClearQueryButton() {
    mClearQueryButton.setVisibility(View.GONE);
  }

  @Override
  public void showProgressBar() {
    mProgressBar.show();
  }

  @Override
  public void hideProgressBar() {
    mProgressBar.hide();
  }

  @Override
  public void showEmptyView() {
    mEmptyView.setVisibility(View.VISIBLE);
  }

  @Override
  public void hideEmptyView() {
    mEmptyView.setVisibility(View.GONE);
  }
}
