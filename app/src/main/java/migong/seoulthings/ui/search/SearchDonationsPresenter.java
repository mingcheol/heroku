package migong.seoulthings.ui.search;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.Query.Direction;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import java.util.ArrayList;
import java.util.List;
import migong.seoulthings.data.Donation;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;

public class SearchDonationsPresenter extends SearchPresenter {

  private static final String TAG = SearchDonationsPresenter.class.getSimpleName();

  private FirebaseFirestore mFirestore;
  private Query mQuery;
  private ListenerRegistration mRegistration;

  public SearchDonationsPresenter(@NonNull SearchView view) {
    super(view);
  }

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    Log.d(TAG, "onCreate() called with: savedInstanceState = [" + savedInstanceState + "]");

    mFirestore = FirebaseFirestore.getInstance();

    mQuery = mFirestore.collection("donations")
        .orderBy("updatedAt", Direction.DESCENDING);
  }

  @Override
  public void onResume() {
    Log.d(TAG, "onResume() called");
  }

  @Override
  public void onPause() {
    Log.d(TAG, "onPause() called");

    stopListening();
  }

  @Override
  public void onDestroy() {
    Log.d(TAG, "onDestroy() called");
  }

  @Override
  public void onSearchResultRecyclerViewHolderClicked(@NonNull String id) {
    if (StringUtils.isEmpty(id)) {
      Log.e(TAG, "onSearchResultRecyclerViewHolderClicked: id is empty. id is " + id);
      return;
    }

    mView.startDonationActivity(id);
  }

  @Override
  protected void search(String query) {
    Log.d(TAG, "sesarch() called with: query = [" + query + "]");
    stopListening();
    mView.hideEmptyView();

    if (StringUtils.isEmpty(query)) {
      mView.clearSearchResult();
      return;
    }

    mView.showProgressBar();
    mRegistration = mQuery.addSnapshotListener((snapshot, e) -> {
      if (e != null) {
        Log.e(TAG, "onEvent: error", e);
        return;
      }

      if (snapshot == null) {
        Log.e(TAG, "onEvent: snapshot is NULL.");
        return;
      }

      List<SearchResult> searchResults = new ArrayList<>();
      for (QueryDocumentSnapshot document : snapshot) {
        final Donation donation = document.toObject(Donation.class);

        if (StringUtils.contains(donation.getTitle(), query) ||
            StringUtils.contains(donation.getContents(), query) ||
            StringUtils.contains(donation.getDong(), query)) {
          final SearchResult result = new SearchResult(donation, SearchView.SCOPE_DONATIONS,
              document.getId(), donation.getTitle(), donation.getContents(),
              new DateTime(donation.getUpdatedAt().toDate().getTime()));
          searchResults.add(result);
        }
      }

      mView.hideProgressBar();
      mView.changeSearchResult(searchResults);

      if (searchResults.size() == 0) {
        mView.showEmptyView();
      }
    });
  }

  private void stopListening() {
    if (mRegistration != null) {
      mRegistration.remove();
      mRegistration = null;
    }
  }
}
