package migong.seoulthings.ui.thing.adapter;

import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ImageView;
import com.google.firebase.auth.FirebaseUser;
import com.makeramen.roundedimageview.RoundedTransformationBuilder;
import com.squareup.picasso.Picasso;
import migong.seoulthings.R;
import migong.seoulthings.data.Review;

public class ReviewRecyclerFormViewHolder extends ReviewRecyclerViewHolder {

  public interface ClickListener {

    void onClick();

  }

  private ImageView mProfilePhotoImage;
  private FirebaseUser mUser;
  @NonNull
  private ClickListener mClickListener;

  public ReviewRecyclerFormViewHolder(@NonNull View itemView, FirebaseUser user,
      @NonNull ClickListener clickListener) {
    super(itemView);
    mProfilePhotoImage = itemView.findViewById(R.id.review_listitem_form_profile_photo);
    mUser = user;
    mClickListener = clickListener;

    if (mUser != null) {
      Picasso.get()
          .load(mUser.getPhotoUrl())
          .fit()
          .transform(new RoundedTransformationBuilder()
              .borderColor(R.color.colorStroke)
              .borderWidthDp(0.1f)
              .oval(true)
              .build())
          .into(mProfilePhotoImage);
    }

    itemView.setOnClickListener(v -> mClickListener.onClick());
  }

  @Override
  public void bind(@NonNull Review review) {
    throw new UnsupportedOperationException();
  }

  @Override
  public void clear() {
    throw new UnsupportedOperationException();
  }
}
