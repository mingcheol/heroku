package migong.seoulthings.data;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.Exclude;

public class Review {

  @Exclude
  private String mFirebaseId;
  private String mThingId;
  private String mAuthorId;
  private String mContents;
  private Float mRating;
  private Timestamp mCreatedAt;
  private Timestamp mUpdatedAt;

  public Review() {

  }

  public Review(String thingId, String authorId, String contents, float rating) {
    mThingId = thingId;
    mAuthorId = authorId;
    mContents = contents;
    mRating = rating;
    mCreatedAt = Timestamp.now();
    mUpdatedAt = mCreatedAt;
  }

  public String getFirebaseId() {
    return mFirebaseId;
  }

  public void setFirebaseId(String firebaseId) {
    mFirebaseId = firebaseId;
  }

  public String getThingId() {
    return mThingId;
  }

  public void setThingId(String thingId) {
    mThingId = thingId;
  }

  public String getAuthorId() {
    return mAuthorId;
  }

  public void setAuthorId(String authorId) {
    mAuthorId = authorId;
  }

  public String getContents() {
    return mContents;
  }

  public void setContents(String contents) {
    mContents = contents;
  }

  public Float getRating() {
    return mRating;
  }

  public void setRating(Float rating) {
    mRating = rating;
  }

  public Timestamp getCreatedAt() {
    return mCreatedAt;
  }

  public void setCreatedAt(Timestamp createdAt) {
    mCreatedAt = createdAt;
  }

  public Timestamp getUpdatedAt() {
    return mUpdatedAt;
  }

  public void setUpdatedAt(Timestamp updatedAt) {
    mUpdatedAt = updatedAt;
  }

  @Override
  public String toString() {
    final StringBuilder sb = new StringBuilder("Review{");
    sb.append("thingId='").append(mThingId).append('\'');
    sb.append(", authorId='").append(mAuthorId).append('\'');
    sb.append(", contents='").append(mContents).append('\'');
    sb.append(", rating=").append(mRating);
    sb.append(", createdAt=").append(mCreatedAt);
    sb.append(", updatedAt=").append(mUpdatedAt);
    sb.append('}');
    return sb.toString();
  }
}
