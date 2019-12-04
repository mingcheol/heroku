package migong.seoulthings.ui.main.profile.donations;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import migong.seoulthings.R;
import migong.seoulthings.ui.donation.DonationActivity;
import migong.seoulthings.ui.donation.DonationView;
import migong.seoulthings.ui.main.profile.donations.adapter.MyDonationRecyclerAdapter;

public class MyDonationsFragment extends Fragment implements MyDonationsView {

  private RecyclerView mDonationRecyclerView;
  private MyDonationRecyclerAdapter mDonationRecyclerAdapter;

  private MyDonationsPresenter mPresenter;

  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    return inflater.inflate(R.layout.my_donations_fragment, container, false);
  }

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);

    setupDonationRecycler(view);
  }

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    mPresenter = new MyDonationsPresenter(this);
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
  public void startDonationActivity(@NonNull String donationId) {
    Intent intent = new Intent(getContext(), DonationActivity.class);

    Bundle args = new Bundle();
    args.putString(DonationView.KEY_DONATION_ID, donationId);
    intent.putExtras(args);

    startActivity(intent);
  }

  @Override
  public void clearSnapshots() {
    mDonationRecyclerAdapter.clear();
  }

  @Override
  public void addSnapshot(int index, QueryDocumentSnapshot snapshot) {
    mDonationRecyclerAdapter.addSnapshot(index, snapshot);
  }

  @Override
  public void modifySnapshot(int oldIndex, int newIndex, QueryDocumentSnapshot snapshot) {
    mDonationRecyclerAdapter.modifySnapshot(oldIndex, newIndex, snapshot);
  }

  @Override
  public void removeSnapshot(int index) {
    mDonationRecyclerAdapter.removeSnapshot(index);
  }

  private void setupDonationRecycler(@NonNull View view) {
    mDonationRecyclerView = view.findViewById(R.id.my_donations_recycler);
    mDonationRecyclerView.setHasFixedSize(true);
    mDonationRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    mDonationRecyclerView.addItemDecoration(
        new DividerItemDecoration(mDonationRecyclerView.getContext(), LinearLayout.VERTICAL));

    mDonationRecyclerAdapter = new MyDonationRecyclerAdapter(
        donationId -> mPresenter.onDonationRecyclerViewHolderClicked(donationId)
    );
    mDonationRecyclerView.setAdapter(mDonationRecyclerAdapter);
  }
}
