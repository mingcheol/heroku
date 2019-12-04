package migong.seoulthings.ui.main.profile;

import android.net.Uri;

public interface ProfileView {

  int MY_THINGS_POSITION = 0;
  int MY_DONATIONS_POSITION = 1;
  int MY_REVIEWS_POSITION = 2;

  void setTitle(String title);

  void setPhoto(Uri uri);

  void setEmail(String email);

  void startModifyProfileActivity();
}
