package migong.seoulthings.ui.search.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import java.util.List;
import migong.seoulthings.R;
import migong.seoulthings.ui.search.SearchResult;

public class SearchResultRecyclerAdapter
    extends RecyclerView.Adapter<SearchResultRecyclerViewHolder> {

  private List<SearchResult> mDataSet;
  private SearchResultRecyclerViewHolder.ClickListener mViewHolderClickListener;

  public SearchResultRecyclerAdapter(
      SearchResultRecyclerViewHolder.ClickListener viewHolderClickListener) {
    super();
    mViewHolderClickListener = viewHolderClickListener;
  }

  @NonNull
  @Override
  public SearchResultRecyclerViewHolder onCreateViewHolder(
      @NonNull ViewGroup parent, int viewType) {
    final View itemView = LayoutInflater.from(parent.getContext())
        .inflate(R.layout.search_result_listitem, parent, false);
    return new SearchResultRecyclerViewHolder(itemView, mViewHolderClickListener);
  }

  @Override
  public void onBindViewHolder(@NonNull SearchResultRecyclerViewHolder holder, int position) {
    final SearchResult result = mDataSet.get(position);
    if (result != null) {
      holder.bind(result);
    } else {
      holder.clear();
    }
  }

  @Override
  public int getItemCount() {
    return mDataSet == null ? 0 : mDataSet.size();
  }

  public void changeDataSet(List<SearchResult> dataSet) {
    mDataSet = dataSet;
  }
}
