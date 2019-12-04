package migong.seoulthings.ui.main.donations;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import com.google.firebase.firestore.Query;
import migong.seoulthings.R;
import migong.seoulthings.ui.donate.DonateActivity;
import migong.seoulthings.ui.donation.DonationActivity;
import migong.seoulthings.ui.donation.DonationView;
import migong.seoulthings.ui.main.donations.adapter.DonationsRecyclerAdapter;
import migong.seoulthings.ui.search.SearchActivity;
import migong.seoulthings.ui.search.SearchView;

public class DonationsFragment extends Fragment implements DonationsView {

  private RecyclerView mRecyclerView;
  private DonationsRecyclerAdapter mRecyclerAdapter;

  private Query mQuery;
  private DonationsPresenter mPresenter;

  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    return inflater.inflate(R.layout.donations_fragment, container, false);
  }

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);

    setupAppBar(view);
    setupFAB(view);
    setupRecycler(view);
  }

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    mPresenter = new DonationsPresenter(this);
    mPresenter.onCreate(savedInstanceState);
  }

  @Override
  public void onResume() {
    super.onResume();
    mPresenter.onResume();
  }

  @Override
  public void onPause() {
    super.onPause();
    mPresenter.onPause();
  }

  @Override
  public void onDestroy() {
    super.onDestroy();
    mPresenter.onDestroy();
  }

  @Override
  public void startSearchActivity() {
    Intent intent = new Intent(getContext(), SearchActivity.class);

    Bundle args = new Bundle();
    args.putString(SearchView.KEY_SCOPE, SearchView.SCOPE_DONATIONS);
    intent.putExtras(args);

    startActivity(intent);
  }

  @Override
  public void startDonateActivity() {
    Intent intent = new Intent(getContext(), DonateActivity.class);
    startActivity(intent);
  }

  @Override
  public void startDonationActivity(@NonNull String donationId) {
    Intent intent = new Intent(getContext(), DonationActivity.class);

    Bundle args = new Bundle();
    args.putString(DonationView.KEY_DONATION_ID, donationId);
    intent.putExtras(args);

    startActivity(intent);
  }

  @Override
  public void setQuery(Query query) {
    mQuery = query;

    if (mRecyclerAdapter != null) {
      mRecyclerAdapter.setQuery(query);
    }
  }

  @Override
  public void startListening() {
    if (mRecyclerAdapter != null) {
      mRecyclerAdapter.startListening();
    }
  }

  @Override
  public void stopListening() {
    if (mRecyclerAdapter != null) {
      mRecyclerAdapter.stopListening();
    }
  }

  private void setupAppBar(@NonNull View view) {
    ImageButton searchButton = view.findViewById(R.id.donations_search_button);
    searchButton.setOnClickListener(v -> mPresenter.onSearchButtonClicked());
  }

  private void setupFAB(@NonNull View view) {
    FloatingActionButton fab = view.findViewById(R.id.donations_fab);
    fab.setOnClickListener(v -> mPresenter.onFABClicked());
  }

  private void setupRecycler(@NonNull View view) {
    mRecyclerView = view.findViewById(R.id.donations_recycler);
    mRecyclerView.setHasFixedSize(true);
    mRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), GRID_LAYOUT_SPAN_COUNT));
    mRecyclerAdapter = new DonationsRecyclerAdapter(mQuery,
        mPresenter::onRecyclerViewHolderClicked);
    mRecyclerView.setAdapter(mRecyclerAdapter);
  }
}
