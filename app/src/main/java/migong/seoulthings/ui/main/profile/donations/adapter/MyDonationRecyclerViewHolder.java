package migong.seoulthings.ui.main.profile.donations.adapter;

import android.annotation.SuppressLint;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.makeramen.roundedimageview.RoundedImageView;
import com.makeramen.roundedimageview.RoundedTransformationBuilder;
import com.squareup.picasso.Picasso;
import java.text.SimpleDateFormat;
import migong.seoulthings.R;
import migong.seoulthings.data.Donation;
import org.apache.commons.lang3.StringUtils;

public class MyDonationRecyclerViewHolder extends RecyclerView.ViewHolder {

  public interface ClickListener {

    void onClick(@NonNull String donationId);

  }

  @SuppressLint("SimpleDateFormat")
  private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy.MM.dd");

  private ContentLoadingProgressBar mLoadingProgressBar;
  private RoundedImageView mThumbnailImage;
  private ImageButton mDetailButton;
  private LinearLayout mDongLayout;
  private TextView mDongText;
  private TextView mTitleText;
  private TextView mUpdatedAtText;

  @NonNull
  private final ClickListener mClickListener;

  public MyDonationRecyclerViewHolder(@NonNull View itemView,
      @NonNull ClickListener clickListener) {
    super(itemView);
    mLoadingProgressBar = itemView.findViewById(R.id.donation_listitem_loading_progressbar);
    mThumbnailImage = itemView.findViewById(R.id.donation_listitem_thumbnail);
    mDetailButton = itemView.findViewById(R.id.donation_listitem_detail_button);
    mDongLayout = itemView.findViewById(R.id.donation_listitem_dong_layout);
    mDongText = itemView.findViewById(R.id.donation_listitem_dong);
    mTitleText = itemView.findViewById(R.id.donation_listitem_title);
    mUpdatedAtText = itemView.findViewById(R.id.donation_listitem_updatedat);

    mClickListener = clickListener;
    mLoadingProgressBar.show();
  }

  public void bind(@NonNull Donation donation) {
    mDongText.setText(donation.getDong());
    mTitleText.setText(donation.getTitle());
    mUpdatedAtText.setText(DATE_FORMAT.format(donation.getUpdatedAt().toDate()));

    if (StringUtils.isNotEmpty(donation.getThumbnailUrl())) {
      Picasso.get()
          .load(Uri.parse(donation.getThumbnailUrl()))
          .centerCrop()
          .fit()
          .transform(new RoundedTransformationBuilder()
              .borderColor(R.color.colorStroke)
              .borderWidthDp(0.1f)
              .oval(true)
              .build())
          .into(mThumbnailImage);
    } else {
      mThumbnailImage.setVisibility(View.GONE);
    }

    itemView.setOnClickListener(v -> mClickListener.onClick(donation.getFirebaseId()));
    mDetailButton.setOnClickListener(v -> mClickListener.onClick(donation.getFirebaseId()));

    finishLoading();
  }

  public void clear() {
    startLoading();
  }

  private void startLoading() {
    mLoadingProgressBar.show();

    mThumbnailImage.setVisibility(View.GONE);
    mDetailButton.setVisibility(View.GONE);
    mDongLayout.setVisibility(View.GONE);
    mTitleText.setVisibility(View.GONE);
    mUpdatedAtText.setVisibility(View.GONE);
  }

  private void finishLoading() {
    mLoadingProgressBar.hide();

    mThumbnailImage.setVisibility(View.VISIBLE);
    mDetailButton.setVisibility(View.VISIBLE);
    mDongLayout.setVisibility(View.VISIBLE);
    mTitleText.setVisibility(View.VISIBLE);
    mUpdatedAtText.setVisibility(View.VISIBLE);
  }
}
