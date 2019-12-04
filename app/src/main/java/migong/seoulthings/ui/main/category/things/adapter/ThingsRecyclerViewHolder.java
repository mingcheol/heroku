package migong.seoulthings.ui.main.category.things.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import migong.seoulthings.R;
import migong.seoulthings.data.Category;
import migong.seoulthings.data.Thing;

public class ThingsRecyclerViewHolder extends RecyclerView.ViewHolder {

  public interface ClickListener {

    void onClick(@NonNull String thingId);

  }

  private ImageView mIconImageView;
  private TextView mLocationTextView;
  private TextView mContentsTextView;

  private String mThingId;
  private final ClickListener mClickListener;

  public ThingsRecyclerViewHolder(@NonNull View itemView, ClickListener clickListener) {
    super(itemView);
    mIconImageView = itemView.findViewById(R.id.thing_listitem_icon);
    mLocationTextView = itemView.findViewById(R.id.thing_listitem_location);
    mContentsTextView = itemView.findViewById(R.id.thing_listitem_contents);
    mClickListener = clickListener;

    itemView.setOnClickListener(v -> {
      if (mClickListener != null && mThingId != null) {
        mClickListener.onClick(mThingId);
      }
    });
  }

  public void bind(@NonNull Thing thing) {
    mThingId = thing.getId();
    mIconImageView.setVisibility(View.VISIBLE);
    mIconImageView.setImageResource(Category.getIconRedId(thing.getCategory()));
    mLocationTextView.setText(thing.getLocation().getName());
    mContentsTextView.setText(thing.getContents());
  }

  public void clear() {
    mThingId = null;
    mIconImageView.setVisibility(View.INVISIBLE);
    mLocationTextView.setText(R.string.msg_loading);
    mContentsTextView.setText(R.string.msg_loading);
  }
}
