package migong.seoulthings.ui.main.profile.modify;

import android.net.Uri;
import android.support.annotation.NonNull;
import com.google.firebase.auth.FirebaseUser;

public interface ModifyProfileView {

  void finish();

  void bindProfile(@NonNull FirebaseUser user);

  void changePhoto(Uri photoUri);

  String getDisplayName();

  void startUpdateProfile();

  void finishUpdateProfile();

  void startSignInActivity();

  void startTakePhotoIntent();
}
