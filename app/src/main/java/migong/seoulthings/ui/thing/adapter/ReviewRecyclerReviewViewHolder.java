package migong.seoulthings.ui.thing.adapter;

import android.annotation.SuppressLint;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;
import com.makeramen.roundedimageview.RoundedImageView;
import com.makeramen.roundedimageview.RoundedTransformationBuilder;
import com.squareup.picasso.Picasso;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import java.text.SimpleDateFormat;
import migong.seoulthings.R;
import migong.seoulthings.api.FirebaseAPI;
import migong.seoulthings.data.Review;
import org.apache.commons.lang3.StringUtils;

public class ReviewRecyclerReviewViewHolder extends ReviewRecyclerViewHolder {

  public interface ClickListener {

    void onClick(@NonNull Review review);

  }

  private static final String TAG = ReviewRecyclerReviewViewHolder.class.getSimpleName();
  @SuppressLint("SimpleDateFormat")
  private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy.MM.dd");

  private ContentLoadingProgressBar mLoadingProgressBar;
  private RoundedImageView mProfilePhotoImage;
  private TextView mProfileDisplayNameText;
  private TextView mUpdatedAtText;
  private RatingBar mRatingBar;
  private TextView mContentsText;
  private Button mModifyButton;

  private String mUid;
  @NonNull
  private Review mReview;
  @NonNull
  private final FirebaseAPI mFirebaseAPI;
  @NonNull
  private final CompositeDisposable mCompositeDisposable;
  @NonNull
  private final ClickListener mClickListener;

  public ReviewRecyclerReviewViewHolder(@NonNull View itemView, String uid,
      @NonNull FirebaseAPI firebaseAPI, @NonNull CompositeDisposable compositeDisposable,
      @NonNull ClickListener clickListener) {
    super(itemView);
    mLoadingProgressBar = itemView.findViewById(R.id.review_listitem_loading_progressbar);
    mProfilePhotoImage = itemView.findViewById(R.id.review_listitem_profile_photo);
    mProfileDisplayNameText = itemView.findViewById(R.id.review_listitem_title);
    mUpdatedAtText = itemView.findViewById(R.id.review_listitem_updated_at);
    mRatingBar = itemView.findViewById(R.id.review_listitem_ratingbar);
    mContentsText = itemView.findViewById(R.id.review_listitem_contents);
    mModifyButton = itemView.findViewById(R.id.review_listitem_modify_button);

    mUid = uid;
    mFirebaseAPI = firebaseAPI;
    mCompositeDisposable = compositeDisposable;
    mClickListener = clickListener;
    mLoadingProgressBar.show();
  }

  @Override
  public void bind(@NonNull Review review) {
    Log.d(TAG, "bind() called with: review = [" + review + "]");
    mReview = review;

    mCompositeDisposable.add(
        mFirebaseAPI.getFirebaseUser(review.getAuthorId())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe(
                user -> {
                  if (user == null) {
                    Log.e(TAG, "bind: user is NULL.");
                    return;
                  }
                  finishLoading();

                  mProfileDisplayNameText.setText(user.getDisplayName());
                  mUpdatedAtText.setText(DATE_FORMAT.format(review.getUpdatedAt().toDate()));
                  mRatingBar.setRating(review.getRating());
                  mContentsText.setText(review.getContents());

                  if (StringUtils.isNotEmpty(user.getPhotoURL())) {
                    Uri photoUri = Uri.parse(user.getPhotoURL());
                    Picasso.get()
                        .load(photoUri)
                        .fit()
                        .transform(new RoundedTransformationBuilder()
                            .borderColor(R.color.colorStroke)
                            .borderWidthDp(0.1f)
                            .oval(true)
                            .build())
                        .into(mProfilePhotoImage);
                  }
                  if (StringUtils.equals(mUid, review.getAuthorId())) {
                    mModifyButton.setVisibility(View.VISIBLE);
                    mModifyButton.setOnClickListener(v -> mClickListener.onClick(mReview));
                  }
                },
                error -> {
                  Log.e(TAG, "Failed to get user.", error);
                }
            )
    );
  }

  @Override
  public void clear() {
    startLoading();
  }

  private void startLoading() {
    mLoadingProgressBar.show();

    mProfilePhotoImage.setVisibility(View.GONE);
    mProfileDisplayNameText.setVisibility(View.GONE);
    mUpdatedAtText.setVisibility(View.GONE);
    mRatingBar.setVisibility(View.GONE);
    mContentsText.setVisibility(View.GONE);
    mModifyButton.setVisibility(View.GONE);
  }

  private void finishLoading() {
    mLoadingProgressBar.hide();

    mProfilePhotoImage.setVisibility(View.VISIBLE);
    mProfileDisplayNameText.setVisibility(View.VISIBLE);
    mUpdatedAtText.setVisibility(View.VISIBLE);
    mRatingBar.setVisibility(View.VISIBLE);
    mContentsText.setVisibility(View.VISIBLE);
  }
}
