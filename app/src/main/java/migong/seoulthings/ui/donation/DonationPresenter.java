package migong.seoulthings.ui.donation;

import android.annotation.SuppressLint;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import com.google.android.gms.maps.model.LatLng;
import com.google.common.collect.Lists;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import java.text.SimpleDateFormat;
import migong.seoulthings.R;
import migong.seoulthings.SeoulThingsConstants;
import migong.seoulthings.api.FirebaseAPI;
import migong.seoulthings.data.Donation;
import migong.seoulthings.ui.Presenter;
import org.apache.commons.lang3.StringUtils;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class DonationPresenter implements Presenter {

  private static final String TAG = DonationPresenter.class.getSimpleName();
  @SuppressLint("SimpleDateFormat")
  private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy.MM.dd");

  private boolean mMyDonation;
  private boolean mDonationCompleted;
  private boolean mShowGoogleMap;
  private FirebaseAPI mFirebaseAPI;
  private FirebaseUser mUser;
  private FirebaseFirestore mFirestore;
  private DocumentReference mReference;
  @Nullable
  private String mAuthorId;
  @NonNull
  private final String mDonationId;
  @NonNull
  private final CompositeDisposable mCompositeDisposable;
  @NonNull
  private final DonationView mView;

  public DonationPresenter(@NonNull DonationView view, @NonNull String donationId) {
    this.mView = view;
    this.mDonationId = donationId;
    mCompositeDisposable = new CompositeDisposable();
  }

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    Log.d(TAG, "onCreate() called with: savedInstanceState = [" + savedInstanceState + "]");
    Log.d(TAG, "onCreate: donationId is " + mDonationId);

    mFirebaseAPI = new Retrofit.Builder()
        .baseUrl(SeoulThingsConstants.SEOULTHINGS_SERVER_BASE_URL)
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(FirebaseAPI.class);

    mUser = FirebaseAuth.getInstance().getCurrentUser();
    mFirestore = FirebaseFirestore.getInstance();
    mReference = mFirestore.collection("donations").document(mDonationId);
  }

  @Override
  public void onResume() {
    Log.d(TAG, "onResume() called");
    mReference.get()
        .addOnFailureListener(error -> Log.e(TAG, "Failed to get snapshot.", error))
        .addOnSuccessListener(snapshot -> {
          final Donation donation = snapshot.toObject(Donation.class);
          if (donation == null) {
            Log.e(TAG, "onResume: donation is NULL.");
            return;
          }
          Log.d(TAG, "onResume: donation is " + donation);

          mAuthorId = donation.getAuthorId();
          mView.setTitle(donation.getTitle());
          mView.setContents(donation.getContents());
          if (donation.getImageUrls() != null) {
            mView.setImages(Lists.transform(donation.getImageUrls(), Uri::parse));
          }
          if (donation.getUpdatedAt() != null) {
            mView.setUpdatedAt(DATE_FORMAT.format(donation.getUpdatedAt().toDate()));
          }
          mView.setThoroughfare(donation.getDong());
          mView.setGoogleMapLocation(new LatLng(donation.getLatitude(), donation.getLongitude()));
          if (StringUtils.equals(mUser.getUid(), donation.getAuthorId())) {
            mMyDonation = true;
            mView.setAuthor(mUser.getDisplayName());
            if (donation.isComplete()) {
              mDonationCompleted = true;
              mView.setFABIcon(R.drawable.ic_done_white_24);
              mView.disableFAB();
            } else {
              mView.setFABIcon(R.drawable.ic_edit_white_24);
            }
            mView.finishLoading();
          } else {
            mCompositeDisposable.add(
                mFirebaseAPI.getFirebaseUser(donation.getAuthorId())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe(
                        user -> {
                          mMyDonation = false;
                          mView.setAuthor(user.getDisplayName());
                          if (donation.isComplete()) {
                            mDonationCompleted = true;
                            mView.setFABIcon(R.drawable.ic_done_white_24);
                            mView.disableFAB();
                          } else {
                            mView.setFABIcon(R.drawable.ic_edit_white_24);
                          }
                          mView.finishLoading();
                        },
                        error -> {
                          Log.e(TAG, "Failed to get Firebase user.", error);
                        }
                    )
            );
          }
        });
  }

  @Override
  public void onPause() {
    Log.d(TAG, "onPause() called");
  }

  @Override
  public void onDestroy() {
    Log.d(TAG, "onDestroy() called");
  }

  public void onAddressButtonClicked() {
    if (mDonationCompleted) {
      return;
    }

    if (mShowGoogleMap) {
      mView.hideGoogleMap();
      mShowGoogleMap = false;
    } else {
      mView.showGoogleMap();
      mShowGoogleMap = true;
    }
  }

  public void onFABClicked() {
    if (mMyDonation) {
      mView.startDonateActivity(mDonationId);
    } else {
      if (StringUtils.isNotEmpty(mAuthorId)) {
        mView.startChatActivity(mAuthorId);
      }
    }
  }
}
