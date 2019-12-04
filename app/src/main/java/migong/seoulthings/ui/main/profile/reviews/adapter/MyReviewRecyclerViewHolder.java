package migong.seoulthings.ui.main.profile.reviews.adapter;

import android.annotation.SuppressLint;
import android.support.annotation.NonNull;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;
import com.google.firebase.auth.FirebaseUser;
import com.makeramen.roundedimageview.RoundedImageView;
import com.makeramen.roundedimageview.RoundedTransformationBuilder;
import com.squareup.picasso.Picasso;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import java.text.SimpleDateFormat;
import migong.seoulthings.R;
import migong.seoulthings.api.ThingAPI;
import migong.seoulthings.data.Review;
import migong.seoulthings.data.Thing;

public class MyReviewRecyclerViewHolder extends RecyclerView.ViewHolder {

  public interface ClickListener {

    void onClick(@NonNull Review review);

  }

  @SuppressLint("SimpleDateFormat")
  private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy.MM.dd");
  private static final String TAG = MyReviewRecyclerViewHolder.class.getSimpleName();

  private ContentLoadingProgressBar mLoadingProgressBar;
  private RoundedImageView mProfilePhotoImage;
  private TextView mTitleText;
  private TextView mUpdatedAtText;
  private RatingBar mRatingBar;
  private TextView mContentsText;
  private Button mModifyButton;

  @NonNull
  private final FirebaseUser mUser;
  @NonNull
  private final ThingAPI mThingAPI;
  @NonNull
  private final CompositeDisposable mCompositeDisposable;
  @NonNull
  private final ClickListener mClickListener;

  public MyReviewRecyclerViewHolder(@NonNull View itemView, @NonNull FirebaseUser user,
      @NonNull ThingAPI thingAPI, @NonNull CompositeDisposable compositeDisposable,
      @NonNull ClickListener clickListener) {
    super(itemView);
    mLoadingProgressBar = itemView.findViewById(R.id.review_listitem_loading_progressbar);
    mProfilePhotoImage = itemView.findViewById(R.id.review_listitem_profile_photo);
    mTitleText = itemView.findViewById(R.id.review_listitem_title);
    mUpdatedAtText = itemView.findViewById(R.id.review_listitem_updated_at);
    mRatingBar = itemView.findViewById(R.id.review_listitem_ratingbar);
    mContentsText = itemView.findViewById(R.id.review_listitem_contents);
    mModifyButton = itemView.findViewById(R.id.review_listitem_modify_button);

    mUser = user;
    mThingAPI = thingAPI;
    mCompositeDisposable = compositeDisposable;
    mClickListener = clickListener;
    mLoadingProgressBar.show();
  }

  public void bind(@NonNull Review review) {
    mCompositeDisposable.add(
        mThingAPI.getThing(review.getThingId())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe(
                response -> {
                  final int size = response.getSize();
                  final Thing thing = response.getThing();

                  if (size == 0 || thing == null) {
                    Log.e(TAG, "Failed to get a thing of id " + review.getThingId());
                    return;
                  }

                  Picasso.get()
                      .load(mUser.getPhotoUrl())
                      .centerCrop(Gravity.TOP | Gravity.CENTER_HORIZONTAL)
                      .fit()
                      .transform(new RoundedTransformationBuilder()
                          .borderColor(R.color.colorStroke)
                          .borderWidthDp(0.1f)
                          .oval(true)
                          .build())
                      .into(mProfilePhotoImage);

                  mTitleText.setText(thing.getLocation().getName());
                  mUpdatedAtText.setText(DATE_FORMAT.format(review.getUpdatedAt().toDate()));
                  mRatingBar.setRating(review.getRating());
                  mContentsText.setText(review.getContents());
                  mModifyButton.setOnClickListener(
                      v -> mClickListener.onClick(review)
                  );

                  finishLoading();
                },
                error -> {
                  Log.e(TAG, "Failed to get a thing of id " + review.getThingId());
                }
            )
    );
  }

  public void clear() {
    startLoading();
  }

  private void startLoading() {
    mLoadingProgressBar.show();

    mProfilePhotoImage.setVisibility(View.GONE);
    mTitleText.setVisibility(View.GONE);
    mUpdatedAtText.setVisibility(View.GONE);
    mRatingBar.setVisibility(View.GONE);
    mContentsText.setVisibility(View.GONE);
    mModifyButton.setVisibility(View.GONE);
  }

  private void finishLoading() {
    mLoadingProgressBar.hide();

    mProfilePhotoImage.setVisibility(View.VISIBLE);
    mTitleText.setVisibility(View.VISIBLE);
    mUpdatedAtText.setVisibility(View.VISIBLE);
    mRatingBar.setVisibility(View.VISIBLE);
    mContentsText.setVisibility(View.VISIBLE);
    mModifyButton.setVisibility(View.VISIBLE);
  }
}
