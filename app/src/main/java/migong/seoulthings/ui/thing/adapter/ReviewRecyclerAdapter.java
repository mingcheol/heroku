package migong.seoulthings.ui.thing.adapter;

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
import migong.seoulthings.R;
import migong.seoulthings.SeoulThingsConstants;
import migong.seoulthings.api.FirebaseAPI;
import migong.seoulthings.data.Review;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class ReviewRecyclerAdapter extends RecyclerView.Adapter<ReviewRecyclerViewHolder> {

  @NonNull
  private ReviewRecyclerFormViewHolder.ClickListener mFormViewHolderClickListener;
  @NonNull
  private ReviewRecyclerReviewViewHolder.ClickListener mReviewViewHolderClickListener;
  @NonNull
  private final FirebaseAuth mAuth;
  private final FirebaseUser mUser;
  @NonNull
  private final Retrofit mRetrofit;
  @NonNull
  private final FirebaseAPI mFirebaseAPI;
  @NonNull
  private final CompositeDisposable mCompositeDisposable;
  @NonNull
  private final List<DocumentSnapshot> mSnapshots;

  public ReviewRecyclerAdapter(@NonNull CompositeDisposable compositeDisposable,
      @NonNull ReviewRecyclerFormViewHolder.ClickListener formViewHolderClickListener,
      @NonNull ReviewRecyclerReviewViewHolder.ClickListener reviewViewHolderClickListener) {
    super();
    mAuth = FirebaseAuth.getInstance();
    mUser = mAuth.getCurrentUser();
    mRetrofit = new Retrofit.Builder()
        .baseUrl(SeoulThingsConstants.SEOULTHINGS_SERVER_BASE_URL)
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .addConverterFactory(GsonConverterFactory.create())
        .build();
    mFirebaseAPI = mRetrofit.create(FirebaseAPI.class);
    mCompositeDisposable = compositeDisposable;
    mFormViewHolderClickListener = formViewHolderClickListener;
    mReviewViewHolderClickListener = reviewViewHolderClickListener;
    mSnapshots = new ArrayList<>();
  }

  @NonNull
  @Override
  public ReviewRecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    if (viewType == ReviewRecyclerViewHolder.VIEW_TYPE_FORM) {
      final View view = LayoutInflater.from(parent.getContext())
          .inflate(R.layout.review_suggestion, parent, false);
      return new ReviewRecyclerFormViewHolder(view, mUser, mFormViewHolderClickListener);
    } else {
      final View view = LayoutInflater.from(parent.getContext())
          .inflate(R.layout.review_listitem, parent, false);
      return new ReviewRecyclerReviewViewHolder(view, mAuth.getUid(), mFirebaseAPI,
          mCompositeDisposable, mReviewViewHolderClickListener);
    }
  }

  @Override
  public void onBindViewHolder(@NonNull ReviewRecyclerViewHolder holder, int position) {
    if (position > 0) {
      DocumentSnapshot snapshot = getSnapshot(position);
      Review review = snapshot.toObject(Review.class);

      if (review != null) {
        review.setFirebaseId(snapshot.getId());

        holder.bind(review);
      } else {
        holder.clear();
      }
    }
  }

  @Override
  public int getItemCount() {
    return mSnapshots.size() + 1;
  }

  @Override
  public int getItemViewType(int position) {
    if (position == 0) {
      return ReviewRecyclerViewHolder.VIEW_TYPE_FORM;
    } else {
      return ReviewRecyclerViewHolder.VIEW_TYPE_REVIEW;
    }
  }

  public void addSnapshot(int index, QueryDocumentSnapshot snapshot) {
    mSnapshots.add(index, snapshot);
    notifyItemInserted(index + 1);
  }

  public void modifySnapshot(int oldIndex, int newIndex, QueryDocumentSnapshot snapshot) {
    if (oldIndex == newIndex) {
      mSnapshots.set(oldIndex, snapshot);
      notifyItemChanged(oldIndex + 1);
    } else {
      mSnapshots.remove(oldIndex);
      mSnapshots.add(newIndex, snapshot);
      notifyItemMoved(oldIndex + 1, newIndex + 1);
      notifyItemChanged(newIndex + 1);
    }
  }

  public void removeSnapshot(int index) {
    mSnapshots.remove(index);
    notifyItemRemoved(index + 1);
  }

  private DocumentSnapshot getSnapshot(int position) {
    return mSnapshots.get(position - 1);
  }
}
