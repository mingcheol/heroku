package migong.seoulthings.datasource;

import android.arch.paging.PositionalDataSource;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import io.reactivex.disposables.CompositeDisposable;
import migong.seoulthings.SeoulThingsConstants;
import migong.seoulthings.api.ThingsAPI;
import migong.seoulthings.data.Thing;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class ThingsDataSource extends PositionalDataSource<Thing> {

  @Nullable
  private String mCategory;
  @NonNull
  private final CompositeDisposable mCompositeDisposable;
  @NonNull
  private final ThingsAPI mThingsAPI;

  public ThingsDataSource(@NonNull CompositeDisposable compositeDisposable,
      @Nullable String category) {
    this.mCompositeDisposable = compositeDisposable;
    this.mThingsAPI = new Retrofit.Builder()
        .baseUrl(SeoulThingsConstants.SEOULTHINGS_SERVER_BASE_URL)
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(ThingsAPI.class);
    this.mCategory = category;
  }

  @Override
  public void loadInitial(@NonNull LoadInitialParams params,
      @NonNull LoadInitialCallback<Thing> callback) {
    mCompositeDisposable.add(
        mThingsAPI.getThings(mCategory, 1, params.pageSize)
            .subscribe(
                response -> callback.onResult(response.getThings(), params.requestedStartPosition)
            )
    );
  }

  @Override
  public void loadRange(@NonNull LoadRangeParams params,
      @NonNull LoadRangeCallback<Thing> callback) {
    mCompositeDisposable.add(
        mThingsAPI.getThings(mCategory, params.startPosition, params.loadSize)
            .subscribe(response -> callback.onResult(response.getThings()))
    );
  }

  public void setCategory(@NonNull String category) {
    mCategory = category;
  }
}
