package migong.seoulthings.ui.main.profile.reminds;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import io.reactivex.disposables.CompositeDisposable;
import migong.seoulthings.R;
import migong.seoulthings.ui.main.profile.reminds.adapter.RemindRecyclerAdapter;
import migong.seoulthings.ui.thing.ThingActivity;
import migong.seoulthings.ui.thing.ThingView;

public class MyRemindsFragment extends Fragment implements MyRemindsView {

  private RecyclerView mRemindRecyclerView;
  private RemindRecyclerAdapter mRemindRecyclerAdapter;

  @NonNull
  private MyRemindsPresenter mPresenter;
  @NonNull
  private final CompositeDisposable mCompositeDisposable = new CompositeDisposable();

  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    return inflater.inflate(R.layout.my_reminds_fragment, container, false);
  }

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);

    setupRemindRecycler(view);
  }

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    mPresenter = new MyRemindsPresenter(this);
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

  @Override
  public void startThingActivity(@NonNull String thingId) {
    Intent intent = new Intent(getContext(), ThingActivity.class);

    Bundle args = new Bundle();
    args.putString(ThingView.KEY_THING_ID, thingId);
    intent.putExtras(args);

    startActivity(intent);
  }

  @Override
  public void clearSnapshots() {
    mRemindRecyclerAdapter.clear();
  }

  @Override
  public void addSnapshot(int index, QueryDocumentSnapshot snapshot) {
    mRemindRecyclerAdapter.add(index, snapshot);
  }

  @Override
  public void modifySnapshot(int oldIndex, int newIndex, QueryDocumentSnapshot snapshot) {
    mRemindRecyclerAdapter.modify(oldIndex, newIndex, snapshot);
  }

  @Override
  public void removeSnapshot(int index) {
    mRemindRecyclerAdapter.remove(index);
  }

  private void setupRemindRecycler(@NonNull View view) {
    mRemindRecyclerView = view.findViewById(R.id.my_reminds_recycler);
    mRemindRecyclerView.setHasFixedSize(true);
    mRemindRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    mRemindRecyclerView.addItemDecoration(
        new DividerItemDecoration(mRemindRecyclerView.getContext(), LinearLayout.VERTICAL));

    mRemindRecyclerAdapter = new RemindRecyclerAdapter(mCompositeDisposable,
        thingId -> mPresenter.onRemindViewHolderClicked(thingId));
    mRemindRecyclerView.setAdapter(mRemindRecyclerAdapter);
  }
}
