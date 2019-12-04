package migong.seoulthings.ui.signup;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import migong.seoulthings.ui.Presenter;
import migong.seoulthings.util.AuthenticationUtils;

public class SignUpPresenter implements Presenter {

  private static final String TAG = SignUpPresenter.class.getSimpleName();

  private FirebaseAuth mAuth;

  @NonNull
  private final SignUpView mView;

  public SignUpPresenter(@NonNull SignUpView view) {
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
  }

  @Override
  public void onPause() {
    Log.d(TAG, "onPause() called");
  }

  @Override
  public void onDestroy() {
    Log.d(TAG, "onDestroy() called");
  }

  public void onSignUpButtonClicked() {
    final String displayName = mView.getDisplayName();
    final String email = mView.getEmail();
    final String password = mView.getPassword();

    if (!AuthenticationUtils.isValidDisplayName(displayName)) {
      mView.showValidDisplayNameInputRequest();
      return;
    }

    if (!AuthenticationUtils.isValidEmailAddress(email)) {
      mView.showValidEmailInputRequest();
      return;
    }

    if (!AuthenticationUtils.isValidPassword(password)) {
      mView.showValidPasswordInputRequest();
      return;
    }

    mView.startSignUp();
    mAuth.createUserWithEmailAndPassword(email, password)
        .addOnCompleteListener(task -> this.completeSignUp(task, displayName));
  }

  public void onSignInButtonClicked() {
    mView.startSignInActivity();
  }

  private void completeSignUp(@NonNull Task<AuthResult> task, String displayName) {
    if (task.isSuccessful()) {
      final FirebaseUser user = mAuth.getCurrentUser();
      if (user == null) {
        mView.showSignUpFailure();
        return;
      }

      user.updateProfile(
          new UserProfileChangeRequest.Builder()
              .setDisplayName(displayName)
              .build())
          .addOnCompleteListener(t -> mView.finishSignUp())
          .addOnFailureListener(error -> {
            mView.showSignUpFailure();
            Log.w(TAG, "updateProfile: failure", error);
          })
          .addOnSuccessListener(v -> {
            Log.d(TAG, "completeSignUp: success, user=" + user);
            mView.startSignInActivity();
          });
    } else {
      mView.finishSignUp();
      mView.showSignUpFailure();
      Log.w(TAG, "completeSignUp: failure", task.getException());
    }
  }
}
