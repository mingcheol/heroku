package migong.seoulthings.ui.signup;

public interface SignUpView {

  String getDisplayName();

  String getEmail();

  String getPassword();

  void startSignInActivity();

  void startSignUp();

  void finishSignUp();

  void showSignUpFailure();

  void showValidDisplayNameInputRequest();

  void showValidEmailInputRequest();

  void showValidPasswordInputRequest();
}
