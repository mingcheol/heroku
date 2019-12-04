package migong.seoulthings.ui.search.adapter;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.makeramen.roundedimageview.RoundedTransformationBuilder;
import com.squareup.picasso.Picasso;
import migong.seoulthings.R;
import migong.seoulthings.data.Category;
import migong.seoulthings.ui.search.SearchResult;
import migong.seoulthings.ui.search.SearchView;
import org.apache.commons.lang3.StringUtils;

public class SearchResultRecyclerViewHolder extends RecyclerView.ViewHolder {

  public interface ClickListener {

    void onClick(@NonNull String id);

  }

  private ImageView mIconImage;
  private TextView mTitleText;
  private TextView mContentsText;

  private String mId;
  private final ClickListener mClickListener;

  public SearchResultRecyclerViewHolder(@NonNull View itemView, ClickListener clickListener) {
    super(itemView);
    mIconImage = itemView.findViewById(R.id.search_result_listitem_icon);
    mTitleText = itemView.findViewById(R.id.search_result_listitem_title);
    mContentsText = itemView.findViewById(R.id.search_result_listitem_contents);
    mClickListener = clickListener;

    itemView.setOnClickListener(v -> {
      if (mClickListener != null && mId != null) {
        mClickListener.onClick(mId);
      }
    });
  }

  public void bind(@NonNull SearchResult result) {
    mId = result.getId();
    mIconImage.setVisibility(View.VISIBLE);
    if (StringUtils.equals(result.getScope(), SearchView.SCOPE_THINGS)) {
      if (result.getThing() != null) {
        mIconImage.setImageResource(Category.getIconRedId(result.getThing().getCategory()));
      }
    } else {
      if (result.getDonation() != null &&
          StringUtils.isNotEmpty(result.getDonation().getThumbnailUrl())) {
        Picasso.get()
            .load(Uri.parse(result.getDonation().getThumbnailUrl()))
            .centerCrop()
            .fit()
            .transform(new RoundedTransformationBuilder()
                .borderColor(R.color.colorStroke)
                .borderWidthDp(0.1f)
                .oval(true)
                .build())
            .into(mIconImage);
      }
    }
    mTitleText.setText(result.getTitle());
    mContentsText.setText(result.getContents());
  }

  public void clear() {
    mId = null;
    mIconImage.setVisibility(View.INVISIBLE);
    mTitleText.setText(R.string.msg_loading);
    mContentsText.setText(R.string.msg_loading);
  }
}
