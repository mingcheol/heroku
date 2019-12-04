package migong.seoulthings.data;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.Exclude;

import java.util.List;

public class Donation {

    @Exclude
    private String mFirebaseId;
    private boolean mComplete;
    private String mAuthorId;
    private String mTitle;
    private String mContents;
    private String mDong;
    private double mLatitude;
    private double mLongitude;
    private String mThumbnailUrl;
    private List<String> mImageUrls;
    private Timestamp mCreatedAt;
    private Timestamp mUpdatedAt;

    public Donation() {
        // For FireStore deserialization.
    }

    public Donation(String authorId, String title, String contents, String thoroughfare, LatLng latLng) {
        mAuthorId = authorId;
        mTitle = title;
        mContents = contents;
        mDong = thoroughfare;
        mLatitude = latLng.latitude;
        mLongitude = latLng.longitude;
        mCreatedAt = Timestamp.now();
        mUpdatedAt = Timestamp.now();
    }

    public String getFirebaseId() {
        return mFirebaseId;
    }

    public void setFirebaseId(String firebaseId) {
        mFirebaseId = firebaseId;
    }

    public boolean isComplete() {
        return mComplete;
    }

    public void setComplete(boolean complete) {
        mComplete = complete;
    }

    public String getAuthorId() {
        return mAuthorId;
    }

    public void setAuthorId(String authorId) {
        mAuthorId = authorId;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public String getContents() {
        return mContents;
    }

    public void setContents(String contents) {
        mContents = contents;
    }

    public String getDong() {
        return mDong;
    }

    public void setDong(String dong) {
        mDong = dong;
    }

    public double getLatitude() {
        return mLatitude;
    }

    public void setLatitude(double latitude) {
        mLatitude = latitude;
    }

    public double getLongitude() {
        return mLongitude;
    }

    public void setLongitude(double longitude) {
        mLongitude = longitude;
    }

    public String getThumbnailUrl() {
        return mThumbnailUrl;
    }

    public void setThumbnailUrl(String thumbnailUrl) {
        mThumbnailUrl = thumbnailUrl;
    }

    public List<String> getImageUrls() {
        return mImageUrls;
    }

    public void setImageUrls(List<String> imageUrls) {
        mImageUrls = imageUrls;
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
        final StringBuilder sb = new StringBuilder("Donation{");
        sb.append("complete=").append(mComplete);
        sb.append(", authorId='").append(mAuthorId).append('\'');
        sb.append(", title='").append(mTitle).append('\'');
        sb.append(", contents='").append(mContents).append('\'');
        sb.append(", dong='").append(mDong).append('\'');
        sb.append(", latitude=").append(mLatitude);
        sb.append(", longitude=").append(mLongitude);
        sb.append(", thumbnailUrl='").append(mThumbnailUrl).append('\'');
        sb.append(", imageUrls=").append(mImageUrls);
        sb.append(", createdAt=").append(mCreatedAt);
        sb.append(", updatedAt=").append(mUpdatedAt);
        sb.append('}');
        return sb.toString();
    }
}
