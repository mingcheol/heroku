package migong.seoulthings.ui.main.profile.reviews;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import io.reactivex.disposables.CompositeDisposable;
import migong.seoulthings.R;
import migong.seoulthings.data.Review;
import migong.seoulthings.ui.main.profile.reviews.adapter.MyReviewRecyclerAdapter;

public class MyReviewsFragment extends Fragment implements MyReviewsView {

  private static final String TAG = MyReviewsFragment.class.getSimpleName();

  private RecyclerView mReviewRecyclerView;
  private MyReviewRecyclerAdapter mReviewRecyclerAdapter;

  private final CompositeDisposable mCompositeDisposable = new CompositeDisposable();
  private MyReviewsPresenter mPresenter;

  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    return inflater.inflate(R.layout.my_reviews_fragment, container, false);
  }

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);

    setupReviewRecycler(view);
  }

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    mPresenter = new MyReviewsPresenter(this);
    mPresenter.onCreate(savedInstanceState);
  }

  @Override
  public void onResume() {
    super.onResume();
    mPresenter.onResume();
  }

  @Override
  public void onPause() {
    super.onPause();
    mPresenter.onPause();
  }

  @Override
  public void onDestroy() {
    super.onDestroy();
    mPresenter.onDestroy();
  }

  private void setupReviewRecycler(@NonNull View view) {
    mReviewRecyclerView = view.findViewById(R.id.my_reviews_recycler);
    mReviewRecyclerView.setHasFixedSize(true);
    mReviewRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    mReviewRecyclerView.addItemDecoration(
        new DividerItemDecoration(mReviewRecyclerView.getContext(), LinearLayout.VERTICAL));

    mReviewRecyclerAdapter = new MyReviewRecyclerAdapter(mCompositeDisposable,
        review -> mPresenter.onReviewRecyclerViewHolderClicked(review));
    mReviewRecyclerView.setAdapter(mReviewRecyclerAdapter);
  }

  @Override
  public void showReviewDialog(@NonNull Review review) {
    final LayoutInflater inflater = getLayoutInflater();
    final View view = inflater.inflate(R.layout.review_dialog, null);
    final AlertDialog.Builder builder = new AlertDialog.Builder(getContext())
        .setView(view);

    final RatingBar ratingBar = view.findViewById(R.id.review_dialog_rating_bar);
    final EditText contentsText = view.findViewById(R.id.review_dialog_contents);
    final Button dismissButton = view.findViewById(R.id.review_dialog_dismiss_button);
    final Button submitButton = view.findViewById(R.id.review_dialog_submit_button);
    final AlertDialog dialog = builder.create();
    dismissButton.setOnClickListener(v -> dialog.dismiss());

    ratingBar.setRating(review.getRating());
    contentsText.setText(review.getContents());
    submitButton.setOnClickListener(
        v -> mCompositeDisposable.add(
            mPresenter
                .modifyReview(review, contentsText.getText().toString(), ratingBar.getRating())
                .subscribe(dialog::dismiss, error -> Log.e(TAG, "modifyReview: error", error))
        )
    );
    dialog.show();
  }

  @Override
  public void clearSnapshots() {
    mReviewRecyclerAdapter.clear();
  }

  @Override
  public void addSnapshot(int index, QueryDocumentSnapshot snapshot) {
    mReviewRecyclerAdapter.addSnapshot(index, snapshot);
  }

  @Override
  public void modifySnapshot(int oldIndex, int newIndex, QueryDocumentSnapshot snapshot) {
    mReviewRecyclerAdapter.modifySnapshot(oldIndex, newIndex, snapshot);
  }

  @Override
  public void removeSnapshot(int index) {
    mReviewRecyclerAdapter.removeSnapshot(index);
  }
}
