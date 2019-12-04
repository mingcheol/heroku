package migong.seoulthings.ui.signup;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import migong.seoulthings.R;
import migong.seoulthings.ui.signin.SignInActivity;
import org.apache.commons.lang3.StringUtils;

public class SignUpActivity extends AppCompatActivity implements SignUpView {

  private EditText mDisplayNameEditText;
  private EditText mEmailEditText;
  private EditText mPasswordEditText;
  private Button mSignUpButton;
  private ContentLoadingProgressBar mSignUpProgressBar;

  private SignUpPresenter mPresenter;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.signup_activity);

    mDisplayNameEditText = findViewById(R.id.signup_display_name_edittext);
    mEmailEditText = findViewById(R.id.signup_email_edittext);
    mPasswordEditText = findViewById(R.id.signup_password_edittext);
    mSignUpButton = findViewById(R.id.signup_button);
    mSignUpButton.setOnClickListener(v -> mPresenter.onSignUpButtonClicked());
    mSignUpProgressBar = findViewById(R.id.signup_progressbar);
    mSignUpProgressBar.hide();

    mPresenter = new SignUpPresenter(this);
    mPresenter.onCreate(savedInstanceState);
  }

  @Override
  protected void onResume() {
    super.onResume();
    mPresenter.onResume();
  }

  @Override
  protected void onPause() {
    super.onPause();
    mPresenter.onPause();
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    mPresenter.onDestroy();
  }

  @Override
  public String getDisplayName() {
    return mDisplayNameEditText == null ? StringUtils.EMPTY
        : mDisplayNameEditText.getText().toString();
  }

  @Override
  public String getEmail() {
    return mEmailEditText == null ? StringUtils.EMPTY : mEmailEditText.getText().toString();
  }

  @Override
  public String getPassword() {
    return mPasswordEditText == null ? StringUtils.EMPTY : mPasswordEditText.getText().toString();
  }

  @Override
  public void startSignInActivity() {
    Intent intent = new Intent(SignUpActivity.this, SignInActivity.class);
    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
    startActivity(intent);
  }

  @Override
  public void startSignUp() {
    mSignUpButton.setVisibility(View.INVISIBLE);
    mSignUpProgressBar.show();
  }

  @Override
  public void finishSignUp() {
    mSignUpButton.setVisibility(View.VISIBLE);
    mSignUpProgressBar.hide();
  }

  @Override
  public void showSignUpFailure() {
    Snackbar.make(mSignUpButton, R.string.failed_to_signup, Snackbar.LENGTH_SHORT)
        .show();
  }

  @Override
  public void showValidDisplayNameInputRequest() {
    mDisplayNameEditText.requestFocus();
    Snackbar.make(mDisplayNameEditText, R.string.valid_display_name_input_request,
        Snackbar.LENGTH_SHORT).show();
  }

  @Override
  public void showValidEmailInputRequest() {
    mEmailEditText.requestFocus();
    Snackbar.make(mEmailEditText, R.string.valid_email_input_request, Snackbar.LENGTH_SHORT)
        .show();
  }

  @Override
  public void showValidPasswordInputRequest() {
    mPasswordEditText.requestFocus();
    Snackbar.make(mPasswordEditText, R.string.valid_password_input_request, Snackbar.LENGTH_SHORT)
        .show();
  }
}
