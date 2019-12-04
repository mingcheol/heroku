package migong.seoulthings.ui.main.profile.reviews.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import io.reactivex.disposables.CompositeDisposable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import migong.seoulthings.R;
import migong.seoulthings.SeoulThingsConstants;
import migong.seoulthings.api.ThingAPI;
import migong.seoulthings.data.Review;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class MyReviewRecyclerAdapter extends RecyclerView.Adapter<MyReviewRecyclerViewHolder> {

  @NonNull
  private final FirebaseUser mUser;
  @NonNull
  private final Retrofit mRetrofit;
  @NonNull
  private final ThingAPI mThingAPI;
  @NonNull
  private final CompositeDisposable mCompositeDisposable;
  @NonNull
  private final MyReviewRecyclerViewHolder.ClickListener mViewHolderClickListener;
  @NonNull
  private final List<DocumentSnapshot> mSnapshots;

  public MyReviewRecyclerAdapter(@NonNull CompositeDisposable compositeDisposable,
      @NonNull MyReviewRecyclerViewHolder.ClickListener viewHolderClickListener) {
    mUser = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser());
    mRetrofit = new Retrofit.Builder()
        .baseUrl(SeoulThingsConstants.SEOULTHINGS_SERVER_BASE_URL)
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .addConverterFactory(GsonConverterFactory.create())
        .build();
    mThingAPI = mRetrofit.create(ThingAPI.class);
    mCompositeDisposable = compositeDisposable;
    mViewHolderClickListener = viewHolderClickListener;
    mSnapshots = new ArrayList<>();
  }

  @NonNull
  @Override
  public MyReviewRecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    final View view = LayoutInflater.from(parent.getContext())
        .inflate(R.layout.review_listitem, parent, false);
    return new MyReviewRecyclerViewHolder(view, mUser, mThingAPI, mCompositeDisposable,
        mViewHolderClickListener);
  }

  @Override
  public void onBindViewHolder(@NonNull MyReviewRecyclerViewHolder holder, int position) {
    DocumentSnapshot snapshot = mSnapshots.get(position);
    Review review = snapshot.toObject(Review.class);

    if (review != null) {
      review.setFirebaseId(snapshot.getId());

      holder.bind(review);
    } else {
      holder.clear();
    }
  }

  @Override
  public int getItemCount() {
    return mSnapshots.size();
  }

  public void clear() {
    mSnapshots.clear();
    notifyDataSetChanged();
  }

  public void addSnapshot(int index, QueryDocumentSnapshot snapshot) {
    mSnapshots.add(index, snapshot);
    notifyItemInserted(index);
  }

  public void modifySnapshot(int oldIndex, int newIndex, QueryDocumentSnapshot snapshot) {
    if (oldIndex == newIndex) {
      mSnapshots.set(oldIndex, snapshot);
      notifyItemChanged(oldIndex);
    } else {
      mSnapshots.remove(oldIndex);
      mSnapshots.add(newIndex, snapshot);
      notifyItemMoved(oldIndex, newIndex);
      notifyItemChanged(newIndex);
    }
  }

  public void removeSnapshot(int index) {
    mSnapshots.remove(index);
    notifyItemRemoved(index);
  }
}
