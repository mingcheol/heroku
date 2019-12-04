package migong.seoulthings.datasource;

import android.arch.lifecycle.MutableLiveData;
import android.arch.paging.DataSource;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import io.reactivex.disposables.CompositeDisposable;
import migong.seoulthings.data.Thing;

public class ThingsDataSourceFactory extends DataSource.Factory<Integer, Thing> {

  @Nullable
  private String mCategory;
  @NonNull
  private final CompositeDisposable mCompositeDisposable;
  @NonNull
  private final MutableLiveData<ThingsDataSource> mThingsDataSourceLiveData;

  public ThingsDataSourceFactory(@NonNull CompositeDisposable compositeDisposable) {
    this.mCompositeDisposable = compositeDisposable;
    this.mThingsDataSourceLiveData = new MutableLiveData<>();
  }

  @Override
  public DataSource<Integer, Thing> create() {
    final ThingsDataSource dataSource = new ThingsDataSource(mCompositeDisposable, mCategory);
    mThingsDataSourceLiveData.postValue(dataSource);
    return dataSource;
  }

  public void setCategory(@NonNull String category) {
    mCategory = category;
    if (mThingsDataSourceLiveData.getValue() != null) {
      mThingsDataSourceLiveData.getValue().setCategory(category);
    }
  }
}
