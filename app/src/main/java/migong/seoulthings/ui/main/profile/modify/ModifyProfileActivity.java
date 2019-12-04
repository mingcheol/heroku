package migong.seoulthings.ui.main.profile.modify;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore.Images.Media;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import com.google.firebase.auth.FirebaseUser;
import com.makeramen.roundedimageview.RoundedImageView;
import com.makeramen.roundedimageview.RoundedTransformationBuilder;
import com.squareup.picasso.Picasso;
import migong.seoulthings.R;
import migong.seoulthings.ui.signin.SignInActivity;
import org.apache.commons.lang3.StringUtils;

public class ModifyProfileActivity extends AppCompatActivity implements ModifyProfileView {

  private static final int REQUEST_IMAGE_CAPTURE = 0x00000001;

  private Button mCompleteButton;
  private ContentLoadingProgressBar mCompleteProgressBar;
  private RoundedImageView mPhotoImage;
  private Button mChangePhotoButton;
  private TextInputEditText mDisplayNameEditText;
  private TextView mEmailText;

  private ModifyProfilePresenter mPresenter;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.modify_profile_activity);

    setupAppBar();
    setupPhoto();
    setupProfileInformation();
    setupSignOutButton();

    mPresenter = new ModifyProfilePresenter(this);
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
  protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
    switch (requestCode) {
      case REQUEST_IMAGE_CAPTURE:
        if (resultCode == RESULT_OK) {
          mPresenter.onRequestTakePhotoSuccess(data);
        }
        break;
      default:
        super.onActivityResult(requestCode, resultCode, data);
        break;
    }
  }

  @Override
  public void bindProfile(@NonNull FirebaseUser user) {
    changePhoto(user.getPhotoUrl());
    mDisplayNameEditText.setText(user.getDisplayName());
    mDisplayNameEditText.clearFocus();
    mEmailText.setText(user.getEmail());
  }

  @Override
  public void changePhoto(Uri photoUri) {
    Picasso.get()
        .load(photoUri)
        .centerCrop(Gravity.TOP | Gravity.CENTER_HORIZONTAL)
        .fit()
        .transform(new RoundedTransformationBuilder()
            .borderColor(R.color.colorStroke)
            .borderWidthDp(0.1f)
            .oval(true)
            .build())
        .into(mPhotoImage);
  }

  @Override
  public String getDisplayName() {
    if (mDisplayNameEditText == null || mDisplayNameEditText.getText() == null) {
      return StringUtils.EMPTY;
    } else {
      return mDisplayNameEditText.getText().toString();
    }
  }

  @Override
  public void startUpdateProfile() {
    mCompleteButton.setVisibility(View.INVISIBLE);
    mCompleteProgressBar.show();
  }

  @Override
  public void finishUpdateProfile() {
    mCompleteButton.setVisibility(View.VISIBLE);
    mCompleteProgressBar.hide();
  }

  @Override
  public void startSignInActivity() {
    Intent intent = new Intent(this, SignInActivity.class);
    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    startActivity(intent);
  }

  @Override
  public void startTakePhotoIntent() {
    Intent takePhotoIntent = new Intent(Intent.ACTION_PICK, Media.EXTERNAL_CONTENT_URI);
    if (takePhotoIntent.resolveActivity(getPackageManager()) != null) {
      startActivityForResult(takePhotoIntent, REQUEST_IMAGE_CAPTURE);
    }
  }

  private void setupAppBar() {
    ImageButton backButton = findViewById(R.id.modify_profile_back_button);
    backButton.setOnClickListener(v -> onBackPressed());

    mCompleteButton = findViewById(R.id.modify_profile_complete_button);
    mCompleteButton.setOnClickListener(v -> mPresenter.onCompleteButtonClicked());
    mCompleteProgressBar = findViewById(R.id.modify_profile_complete_progress_bar);
    mCompleteProgressBar.hide();
  }

  private void setupPhoto() {
    mPhotoImage = findViewById(R.id.modify_profile_photo);

    mChangePhotoButton = findViewById(R.id.modify_profile_change_photo_button);
    mChangePhotoButton.setOnClickListener(v -> mPresenter.onChangePhotoButtonClicked());
  }

  private void setupProfileInformation() {
    mDisplayNameEditText = findViewById(R.id.modify_profile_display_name_edittext);
    mEmailText = findViewById(R.id.modify_profile_email);
  }

  private void setupSignOutButton() {
    Button signOutButton = findViewById(R.id.modify_profile_signout_button);
    signOutButton.setOnClickListener(v -> mPresenter.onSignOutButtonClicked());
  }
}
