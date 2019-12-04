package migong.seoulthings.ui.main.profile;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import migong.seoulthings.ui.Presenter;

public class ProfilePresenter implements Presenter {

  private static final String TAG = ProfilePresenter.class.getSimpleName();

  private FirebaseAuth mAuth;
  private FirebaseUser mUser;
  @NonNull
  private final ProfileView mView;

  public ProfilePresenter(@NonNull ProfileView view) {
    this.mView = view;
  }

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    Log.d(TAG, "onCreate() called with: savedInstanceState = [" + savedInstanceState + "]");

    mAuth = FirebaseAuth.getInstance();
    mUser = mAuth.getCurrentUser();
  }

  @Override
  public void onResume() {
    Log.d(TAG, "onResume() called");

    mView.setTitle(mUser.getDisplayName());
    mView.setPhoto(mUser.getPhotoUrl());
    mView.setEmail(mUser.getEmail());
  }

  @Override
  public void onPause() {
    Log.d(TAG, "onPause() called");
  }

  @Override
  public void onDestroy() {
    Log.d(TAG, "onDestroy() called");
  }

  public void onModifyButtonClicked() {
    mView.startModifyProfileActivity();
  }
}
