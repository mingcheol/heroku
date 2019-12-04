package migong.seoulthings.ui.thing.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import migong.seoulthings.data.Review;

public abstract class ReviewRecyclerViewHolder extends RecyclerView.ViewHolder {

  public static final int VIEW_TYPE_FORM = 0x00000001;
  public static final int VIEW_TYPE_REVIEW = 0x00000010;

  protected ReviewRecyclerViewHolder(@NonNull View itemView) {
    super(itemView);
  }

  public abstract void bind(@NonNull Review review);

  public abstract void clear();
}
