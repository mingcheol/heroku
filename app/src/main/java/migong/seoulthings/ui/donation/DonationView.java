package migong.seoulthings.ui.donation;

import android.net.Uri;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import com.google.android.gms.maps.model.LatLng;
import java.util.List;

public interface DonationView {

  String KEY_DONATION_ID = "DONATION_ID";

  void startChatActivity(@NonNull String chatterId);

  void startDonateActivity(String donationId);

  void finishLoading();

  void setImages(List<Uri> images);

  void addImage(Uri uri);

  void setTitle(String title);

  void setContents(String contents);

  void setAuthor(String author);

  void setFABIcon(@DrawableRes int iconResId);

  void disableFAB();

  void setUpdatedAt(String updatedAt);

  void setThoroughfare(String thoroughfare);

  void setGoogleMapLocation(@NonNull LatLng latLng);

  void showGoogleMap();

  void hideGoogleMap();
}
