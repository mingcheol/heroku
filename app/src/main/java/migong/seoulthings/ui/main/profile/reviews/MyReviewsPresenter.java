package migong.seoulthings.ui.main.profile.reviews;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.Query.Direction;
import io.reactivex.Completable;
import migong.seoulthings.data.Review;
import migong.seoulthings.ui.Presenter;

public class MyReviewsPresenter implements Presenter {

  private static final String TAG = MyReviewsPresenter.class.getSimpleName();

  private FirebaseUser mUser;
  private FirebaseFirestore mFirestore;
  private Query mQuery;
  private ListenerRegistration mRegistration;
  @NonNull
  private final MyReviewsView mView;

  public MyReviewsPresenter(@NonNull MyReviewsView view) {
    mView = view;
  }

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    Log.d(TAG, "onCreate() called with: savedInstanceState = [" + savedInstanceState + "]");

    mUser = FirebaseAuth.getInstance().getCurrentUser();
    mFirestore = FirebaseFirestore.getInstance();
    mQuery = mFirestore.collection("reviews")
        .whereEqualTo("authorId", mUser.getUid())
        .orderBy("updatedAt", Direction.DESCENDING);
  }

  @Override
  public void onResume() {
    Log.d(TAG, "onResume() called");

    startListening();
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

  public void onReviewRecyclerViewHolderClicked(@NonNull Review review) {
    mView.showReviewDialog(review);
  }

  public Completable modifyReview(@NonNull Review review, String contents, float rating) {
    return Completable.create(
        emitter -> mFirestore.collection("reviews")
            .document(review.getFirebaseId())
            .update(
                "contents", contents,
                "rating", rating,
                "updatedAt", Timestamp.now()
            )
            .addOnSuccessListener(v -> emitter.onComplete())
            .addOnFailureListener(emitter::onError)
    );
  }

  private void startListening() {
    if (mQuery != null && mRegistration == null) {
      mRegistration = mQuery.addSnapshotListener((snapshot, e) -> {
        if (e != null) {
          Log.e(TAG, "onEvent: error", e);
          return;
        }

        if (snapshot == null) {
          Log.e(TAG, "onEvent: snapshot is NULL.");
          return;
        }

        Log.d(TAG, "onEvent: numChanges is " + snapshot.getDocumentChanges().size());
        for (DocumentChange change : snapshot.getDocumentChanges()) {
          switch (change.getType()) {
            case ADDED:
              onDocumentAdded(change);
              break;
            case MODIFIED:
              onDocumentModified(change);
              break;
            case REMOVED:
              onDocumentRemoved(change);
              break;
          }
        }
      });
    }
  }

  private void stopListening() {
    if (mRegistration != null) {
      mRegistration.remove();
      mRegistration = null;
    }

    mView.clearSnapshots();
  }

  private void onDocumentAdded(DocumentChange change) {
    mView.addSnapshot(change.getNewIndex(), change.getDocument());
  }

  private void onDocumentModified(DocumentChange change) {
    mView.modifySnapshot(change.getOldIndex(), change.getNewIndex(), change.getDocument());
  }

  private void onDocumentRemoved(DocumentChange change) {
    mView.removeSnapshot(change.getOldIndex());
  }
}
