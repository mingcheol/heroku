package migong.seoulthings.ui.main.category.things;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.arch.paging.LivePagedListBuilder;
import android.arch.paging.PagedList;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import io.reactivex.disposables.CompositeDisposable;
import migong.seoulthings.data.Thing;
import migong.seoulthings.datasource.ThingsDataSourceFactory;

public class ThingsViewModel extends ViewModel {

  private static final int DEFAULT_PAGE_SIZE = 20;
  private static final int DEFAULT_INITIAL_LOAD_SIZE_HINT = DEFAULT_PAGE_SIZE * 2;

  @Nullable
  private String mCategory;
  @NonNull
  private final CompositeDisposable mCompositeDisposable;
  @NonNull
  private final ThingsDataSourceFactory mDataSourceFactory;
  @NonNull
  private final LiveData<PagedList<Thing>> mThings;

  public ThingsViewModel() {
    this.mCompositeDisposable = new CompositeDisposable();
    this.mDataSourceFactory = new ThingsDataSourceFactory(mCompositeDisposable);

    final PagedList.Config config = new PagedList.Config.Builder()
        .setPageSize(DEFAULT_PAGE_SIZE)
        .setInitialLoadSizeHint(DEFAULT_INITIAL_LOAD_SIZE_HINT)
        .setEnablePlaceholders(false)
        .build();
    this.mThings = new LivePagedListBuilder<>(mDataSourceFactory, config).build();
  }

  @Override
  protected void onCleared() {
    super.onCleared();
    mCompositeDisposable.dispose();
  }

  public void setCategory(@NonNull String category) {
    mCategory = category;
    mDataSourceFactory.setCategory(category);
  }

  @NonNull
  public LiveData<PagedList<Thing>> getThings() {
    return mThings;
  }
}
