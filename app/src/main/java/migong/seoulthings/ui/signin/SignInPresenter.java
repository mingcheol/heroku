package migong.seoulthings.ui.signin;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;
import migong.seoulthings.ui.Presenter;
import migong.seoulthings.util.AuthenticationUtils;

public class SignInPresenter implements Presenter {

  private static final String TAG = SignInPresenter.class.getSimpleName();

  private FirebaseAuth mAuth;

  @NonNull
  private final SignInView mView;

  public SignInPresenter(@NonNull SignInView view) {
    this.mView = view;
  }

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    Log.d(TAG, "onCreate() called with: savedInstanceState = [" + savedInstanceState + "]");

    mAuth = FirebaseAuth.getInstance();
  }

  @Override
  public void onResume() {
    Log.d(TAG, "onResume() called");

    if (isCurrentlySignedIn()) {
      mView.startMainActivity();
    }
  }

  @Override
  public void onPause() {
    Log.d(TAG, "onPause() called");
  }

  @Override
  public void onDestroy() {
    Log.d(TAG, "onDestroy() called");
  }

  public void onSignInButtonClicked() {
    final String email = mView.getEmail();
    final String password = mView.getPassword();

    if (!AuthenticationUtils.isValidEmailAddress(email)) {
      mView.showValidEmailInputRequest();
      return;
    }

    if (!AuthenticationUtils.isValidPassword(password)) {
      mView.showValidPasswordInputRequest();
      return;
    }

    mView.startSignIn();

    firebaseAuthWithEmailPassword(email, password);
  }

  public void onSignUpButtonClicked() {
    mView.startSignUpActivity();
  }

  public void onGoogleSignInButtonClicked() {
    mView.startGoogleSignInIntent();
  }

  public void completeGoogleSignIn(@Nullable Task<GoogleSignInAccount> task) {
    if (task == null) {
      Log.e(TAG, "completeGoogleSignIn: task is NULL.");
      return;
    }

    try {
      GoogleSignInAccount account = task.getResult(ApiException.class);
      Log.d(TAG, "completeGoogleSignIn: success, account.id=" + account.getId());

      firebaseAuthWithGoogle(account);
    } catch (ApiException e) {
      Log.w(TAG, "completeGoogleSignIn: failure.", e);
    }
  }

  private boolean isCurrentlySignedIn() {
    return mAuth.getCurrentUser() != null;
  }

  private void completeFirebaseAuth(@NonNull Task<AuthResult> task) {
    mView.finishSignIn();

    if (task.isSuccessful()) {
      if (mAuth.getCurrentUser() == null) {
        mView.showSignInFailure();
        Log.d(TAG, "completeFirebaseAuth: failure, currentUser is NULL.");
        return;
      }

      // TODO(@mincheol): 로그인 성공 처리
      Log.d(TAG, "completeSignIn: success, user.uid=" + mAuth.getCurrentUser().getUid());
      mView.startMainActivity();
    } else {
      mView.showSignInFailure();
      Log.w(TAG, "completeSignIn: failure.", task.getException());
    }
  }

  private void firebaseAuthWithEmailPassword(@NonNull String email, @NonNull String password) {
    mAuth.signInWithEmailAndPassword(email, password)
        .addOnCompleteListener(this::completeFirebaseAuth);
  }

  private void firebaseAuthWithGoogle(GoogleSignInAccount account) {
    AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
    mAuth.signInWithCredential(credential)
        .addOnCompleteListener(this::completeFirebaseAuth);
  }
}
