package migong.seoulthings.ui.thing;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.Query.Direction;
import io.reactivex.Completable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import migong.seoulthings.SeoulThingsConstants;
import migong.seoulthings.api.ThingAPI;
import migong.seoulthings.data.Remind;
import migong.seoulthings.data.Review;
import migong.seoulthings.data.Thing;
import migong.seoulthings.ui.Presenter;
import org.joda.time.LocalDate;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class ThingPresenter implements Presenter {

  private static final String TAG = ThingPresenter.class.getSimpleName();

  private Retrofit mRetrofit;
  private ThingAPI mThingAPI;
  private Thing mThing;
  private FirebaseUser mUser;
  private FirebaseFirestore mFirestore;
  private CollectionReference mRemindCollectionRef;
  private Query mQuery;
  private ListenerRegistration mRegistration;
  @NonNull
  private final String mThingId;
  @NonNull
  private final CompositeDisposable mCompositeDisposable;
  @NonNull
  private final ThingView mView;

  public ThingPresenter(@NonNull ThingView view, @NonNull String thingId) {
    mView = view;
    mThingId = thingId;
    mCompositeDisposable = new CompositeDisposable();
  }

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    Log.d(TAG, "onCreate() called with: savedInstanceState = [" + savedInstanceState + "]");
    Log.d(TAG, "onCreate: thingId is " + mThingId);

    mRetrofit = new Retrofit.Builder()
        .baseUrl(SeoulThingsConstants.SEOULTHINGS_SERVER_BASE_URL)
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .addConverterFactory(GsonConverterFactory.create())
        .build();
    mThingAPI = mRetrofit.create(ThingAPI.class);

    mUser = FirebaseAuth.getInstance().getCurrentUser();
    mFirestore = FirebaseFirestore.getInstance();
    mRemindCollectionRef = mFirestore.collection("reminds");
    mQuery = mFirestore.collection("reviews")
        .whereEqualTo("thingId", mThingId)
        .orderBy("updatedAt", Direction.DESCENDING);
  }

  @Override
  public void onResume() {
    Log.d(TAG, "onResume() called");

    mCompositeDisposable.add(
        mThingAPI.getThing(mThingId)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe(
                response -> {
                  final int size = response.getSize();
                  final Thing thing = response.getThing();

                  if (size == 0 || thing == null) {
                    Log.e(TAG, "Failed to get a thing of id " + mThingAPI);
                  } else {
                    mThing = thing;
                    mView.finishLoading();

                    mView.setTitle(mThing.getLocation().getName());
                    if (mThing.getLocation().getLatitude() != null &&
                        mThing.getLocation().getLongitude() != null) {
                      final double latitude = mThing.getLocation().getLatitude();
                      final double longitude = mThing.getLocation().getLongitude();
                      mView.setGoogleMap(mThing.getLocation().getName(), latitude, longitude);
                    } else {
                      mView.hideGoogleMap();
                    }

                    mView.bindThing(mThing);
                  }
                },
                error -> {
                  Log.e(TAG, "Failed to get a thing.", error);
                }
            )
    );

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

  public void onRemindButtonClicked() {
    mView.showDatePickerDialog(((view, year, month, dayOfMonth) -> {
      final LocalDate dueDate = new LocalDate(year, month + 1, dayOfMonth);
      if (LocalDate.now().minusDays(1).isAfter(dueDate)) {
        onRemindButtonClicked();
        return;
      }

      final Timestamp due = new Timestamp(dueDate.toDate());
      final Remind remind = new Remind(mUser.getUid(), mThingId, due);
      mRemindCollectionRef.add(remind);
    }));
  }

  public void onMakeReviewSuggestionClicked() {
    mView.showReviewDialog(null);
  }

  public void onModifyReviewButtonClicked(@NonNull Review review) {
    mView.showReviewDialog(review);
  }

  public Completable createReview(String contents, float rating) {
    return Completable.create(
        emitter -> mFirestore.collection("reviews")
            .add(new Review(mThingId, mUser.getUid(), contents, rating))
            .addOnSuccessListener(v -> emitter.onComplete())
            .addOnFailureListener(emitter::onError)
    );
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
  }

  private void onDocumentAdded(DocumentChange change) {
    Log.d(TAG, "onDocumentAdded() called with: change = [" + change + "]");

    mView.addSnapshot(change.getNewIndex(), change.getDocument());
  }

  private void onDocumentModified(DocumentChange change) {
    Log.d(TAG, "onDocumentModified() called with: change = [" + change + "]");

    mView.modifySnapshot(change.getOldIndex(), change.getNewIndex(), change.getDocument());
  }

  private void onDocumentRemoved(DocumentChange change) {
    Log.d(TAG, "onDocumentRemoved() called with: change = [" + change + "]");

    mView.removeSnapshot(change.getOldIndex());
  }
}
