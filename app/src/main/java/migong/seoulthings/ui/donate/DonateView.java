package migong.seoulthings.ui.donate;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import com.google.android.gms.maps.model.LatLng;
import java.util.List;

public interface DonateView {

  String KEY_DONATION_ID = "KEY_DONATION_ID";

  Context getContext();

  void finish();

  String getDonationTitle();

  void setDonationTitle(String donationTitle);

  String getDonationContents();

  void setDonationContents(String donationContents);

  List<Uri> getDonationImages();

  void setDonationImages(List<Uri> images);

  void startPickPhotoIntent();

  void addImage(@NonNull Uri imageUri);

  void startAddressLoading();

  void finishAddressLoading();

  void changeAddressButtonText(String address);

  void showGoogleMap();

  void hideGoogleMap();

  void setMarkerOnGoogleMap(@NonNull LatLng latLng);

  void showCompleteButton();

  void showSnackBar(@StringRes int messageResId);

  void startSubmit();

  void finishSubmit();
}
