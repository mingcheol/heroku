package migong.seoulthings.ui.search;

import android.support.annotation.NonNull;
import android.util.Log;
import migong.seoulthings.ui.Presenter;
import org.apache.commons.lang3.StringUtils;

public abstract class SearchPresenter implements Presenter {

  private static final String TAG = SearchPresenter.class.getSimpleName();

  @NonNull
  protected final SearchView mView;

  protected SearchPresenter(@NonNull SearchView view) {
    mView = view;
  }

  public abstract void onSearchResultRecyclerViewHolderClicked(@NonNull String id);

  protected abstract void search(String query);

  public void onQueryChanged(String query) {
    Log.d(TAG, "onQueryChanged() called with: query = [" + query + "]");

    if (StringUtils.isEmpty(query)) {
      mView.hideClearQueryButton();
    } else {
      mView.showClearQueryButton();
    }
    search(query);
  }

  public void onClearQueryButtonClicked() {
    mView.clearQuery();
  }
}
