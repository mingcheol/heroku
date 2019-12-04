package migong.seoulthings.ui.main.donations;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.Query.Direction;
import migong.seoulthings.ui.Presenter;

public class DonationsPresenter implements Presenter {

  private static final String TAG = DonationsPresenter.class.getSimpleName();

  private FirebaseFirestore mFirestore;
  private Query mQuery;
  @NonNull
  private final DonationsView mView;

  public DonationsPresenter(@NonNull DonationsView view) {
    this.mView = view;
  }

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    Log.d(TAG, "onCreate() called with: savedInstanceState = [" + savedInstanceState + "]");

    mFirestore = FirebaseFirestore.getInstance();

    mQuery = mFirestore.collection("donations")
        .whereEqualTo("complete", false)
        .orderBy("updatedAt", Direction.DESCENDING);
    mView.setQuery(mQuery);
  }

  @Override
  public void onResume() {
    Log.d(TAG, "onResume() called");

    mView.startListening();
  }

  @Override
  public void onPause() {
    Log.d(TAG, "onPause() called");

    mView.stopListening();
  }

  @Override
  public void onDestroy() {
    Log.d(TAG, "onDestroy() called");
  }

  public void onSearchButtonClicked() {
    mView.startSearchActivity();
  }

  public void onFABClicked() {
    mView.startDonateActivity();
  }

  public void onRecyclerViewHolderClicked(@NonNull String donationId) {
    mView.startDonationActivity(donationId);
  }
}
