package migong.seoulthings.data;

import com.google.gson.annotations.SerializedName;

public class User {

  @SerializedName("uid")
  private String mUid;

  @SerializedName("email")
  private String mEmail;

  @SerializedName("displayName")
  private String mDisplayName;

  @SerializedName("photoURL")
  private String mPhotoURL;

  public String getUid() {
    return mUid;
  }

  public void setUid(String uid) {
    mUid = uid;
  }

  public String getEmail() {
    return mEmail;
  }

  public void setEmail(String email) {
    mEmail = email;
  }

  public String getDisplayName() {
    return mDisplayName;
  }

  public void setDisplayName(String displayName) {
    mDisplayName = displayName;
  }

  public String getPhotoURL() {
    return mPhotoURL;
  }

  public void setPhotoURL(String photoURL) {
    mPhotoURL = photoURL;
  }

  @Override
  public String toString() {
    final StringBuilder sb = new StringBuilder("User{");
    sb.append("uid='").append(mUid).append('\'');
    sb.append(", email='").append(mEmail).append('\'');
    sb.append(", displayName='").append(mDisplayName).append('\'');
    sb.append(", photoURL='").append(mPhotoURL).append('\'');
    sb.append('}');
    return sb.toString();
  }
}
