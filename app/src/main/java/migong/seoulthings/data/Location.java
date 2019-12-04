package migong.seoulthings.data;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.annotations.SerializedName;
import java.util.UUID;

public class Location {

  @NonNull
  @SerializedName("id")
  private final UUID mId;

  @NonNull
  @SerializedName("name")
  private final String mName;

  @Nullable
  @SerializedName("address")
  private final String mAddress;

  @Nullable
  @SerializedName("contact")
  private final String mContact;

  @Nullable
  @SerializedName("web")
  private final String mWeb;

  @Nullable
  @SerializedName("latitude")
  private final Double mLatitude;

  @Nullable
  @SerializedName("longitude")
  private final Double mLongitude;

  public Location(@NonNull UUID id, @NonNull String name, @Nullable String address,
      @Nullable String contact, @Nullable String web, @Nullable LatLng latLng) {
    mId = id;
    mName = name;
    mAddress = address;
    mContact = contact;
    mWeb = web;
    if (latLng != null) {
      mLatitude = latLng.latitude;
      mLongitude = latLng.longitude;
    } else {
      mLatitude = null;
      mLongitude = null;
    }
  }

  @NonNull
  public String getId() {
    return mId.toString();
  }

  @NonNull
  public String getName() {
    return mName;
  }

  @Nullable
  public String getAddress() {
    return mAddress;
  }

  @Nullable
  public String getContact() {
    return mContact;
  }

  @Nullable
  public String getWeb() {
    return mWeb;
  }

  @Nullable
  public Double getLatitude() {
    return mLatitude;
  }

  @Nullable
  public Double getLongitude() {
    return mLongitude;
  }

  @Override
  public String toString() {
    final StringBuilder sb = new StringBuilder("Location{");
    sb.append("id=").append(mId);
    sb.append(", name='").append(mName).append('\'');
    sb.append(", address='").append(mAddress).append('\'');
    sb.append(", contact='").append(mContact).append('\'');
    sb.append(", web='").append(mWeb).append('\'');
    sb.append(", latitude=").append(mLatitude);
    sb.append(", longitude=").append(mLongitude);
    sb.append('}');
    return sb.toString();
  }
}
