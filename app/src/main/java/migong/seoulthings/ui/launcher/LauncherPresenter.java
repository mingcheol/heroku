package migong.seoulthings.ui.launcher;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import migong.seoulthings.SeoulThingsConstants;
import migong.seoulthings.ui.Presenter;

public class LauncherPresenter implements Presenter {

  private static final String TAG = LauncherPresenter.class.getSimpleName();

  @NonNull
  private final LauncherView mView;

  public LauncherPresenter(@NonNull LauncherView view) {
    this.mView = view;
  }

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    Log.d(TAG, "onCreate() called with: savedInstanceState = [" + savedInstanceState + "]");
  }

  @Override
  public void onResume() {
    Log.d(TAG, "onResume() called");

    new Handler()
        .postDelayed(mView::startSignInActivity, SeoulThingsConstants.SPLASH_TIME_IN_MILLIS);
  }

  @Override
  public void onPause() {
    Log.d(TAG, "onPause() called");
  }

  @Override
  public void onDestroy() {
    Log.d(TAG, "onDestroy() called");
  }
}
