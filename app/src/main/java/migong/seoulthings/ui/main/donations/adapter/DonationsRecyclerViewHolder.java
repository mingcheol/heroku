package migong.seoulthings.ui.main.donations.adapter;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.firestore.DocumentSnapshot;
import com.squareup.picasso.Picasso;

import migong.seoulthings.R;
import migong.seoulthings.data.Donation;

import org.apache.commons.lang3.StringUtils;

public class DonationsRecyclerViewHolder extends ViewHolder {

    private static final String TAG = DonationsRecyclerViewHolder.class.getSimpleName();

    public interface OnClickListener {

        void onClick(@NonNull String donationId);

    }

    private ImageView mThumbnailImage;
    private TextView mTitleView;
    private TextView mLocationView;
    private String mDonationId;
    @NonNull
    private final OnClickListener mOnClickListener;

    public DonationsRecyclerViewHolder(@NonNull View itemView,
                                       @NonNull OnClickListener onClickListener) {
        super(itemView);
        mThumbnailImage = itemView.findViewById(R.id.donation_griditem_thumbnail);
        mTitleView = itemView.findViewById(R.id.donation_griditem_title);
        mLocationView = itemView.findViewById(R.id.donation_griditem_location);
        mOnClickListener = onClickListener;

        itemView.setOnClickListener(v -> {
            if (StringUtils.isNotEmpty(mDonationId)) {
                mOnClickListener.onClick(mDonationId);
            }
        });
    }

    public void bind(final DocumentSnapshot snapshot) {
        final Donation donation = snapshot.toObject(Donation.class);
        if (donation == null) {
            Log.e(TAG, "bind: donation is NULL.");
            return;
        }
        Log.d(TAG, "bind() called with: donation = [" + donation + "]");

        mDonationId = snapshot.getId();
        mTitleView.setText(donation.getTitle());
        mLocationView.setText(donation.getDong());
        if (StringUtils.isNotEmpty(donation.getThumbnailUrl())) {
            Picasso.get()
                    .load(Uri.parse(donation.getThumbnailUrl()))
                    .centerCrop()
                    .fit()
                    .into(mThumbnailImage);
        }
    }
}
