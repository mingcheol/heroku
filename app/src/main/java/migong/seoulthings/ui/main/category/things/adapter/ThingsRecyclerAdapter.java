package migong.seoulthings.ui.main.category.things.adapter;

import android.arch.paging.PagedListAdapter;
import android.support.annotation.NonNull;
import android.support.v7.util.DiffUtil;
import android.support.v7.util.DiffUtil.ItemCallback;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import migong.seoulthings.R;
import migong.seoulthings.data.Thing;
import org.apache.commons.lang3.StringUtils;

public class ThingsRecyclerAdapter extends PagedListAdapter<Thing, ThingsRecyclerViewHolder> {

  private ThingsRecyclerViewHolder.ClickListener mViewHolderClickListener;

  public ThingsRecyclerAdapter(ThingsRecyclerViewHolder.ClickListener viewHolderClickListener) {
    super(DIFF_CALLBACK);
    mViewHolderClickListener = viewHolderClickListener;
  }

  @NonNull
  @Override
  public ThingsRecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    final View itemView = LayoutInflater.from(parent.getContext())
        .inflate(R.layout.thing_listitem, parent, false);
    return new ThingsRecyclerViewHolder(itemView, mViewHolderClickListener);
  }

  @Override
  public void onBindViewHolder(@NonNull ThingsRecyclerViewHolder holder, int position) {
    final Thing thing = getItem(position);
    if (thing != null) {
      holder.bind(thing);
    } else {
      holder.clear();
    }
  }

  private static DiffUtil.ItemCallback<Thing> DIFF_CALLBACK = new ItemCallback<Thing>() {
    @Override
    public boolean areItemsTheSame(@NonNull Thing t1, @NonNull Thing t2) {
      return StringUtils.equals(t1.getId(), t2.getId());
    }

    @Override
    public boolean areContentsTheSame(@NonNull Thing t1, @NonNull Thing t2) {
      return t1.equals(t2);
    }
  };
}
