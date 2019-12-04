package migong.seoulthings.ui.main.profile.reminds.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import io.reactivex.disposables.CompositeDisposable;
import java.util.ArrayList;
import java.util.List;
import migong.seoulthings.R;
import migong.seoulthings.SeoulThingsConstants;
import migong.seoulthings.api.ThingAPI;
import migong.seoulthings.data.Remind;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class RemindRecyclerAdapter extends RecyclerView.Adapter<RemindRecyclerViewHolder> {

  @NonNull
  private final ThingAPI mThingAPI;
  @NonNull
  private final CompositeDisposable mCompositeDisposable;
  @NonNull
  private final RemindRecyclerViewHolder.ClickListener mRemindClickListener;
  @NonNull
  private final List<DocumentSnapshot> mSnapshots;

  public RemindRecyclerAdapter(@NonNull CompositeDisposable compositeDisposable,
      @NonNull RemindRecyclerViewHolder.ClickListener remindClickListener) {
    mThingAPI = new Retrofit.Builder()
        .baseUrl(SeoulThingsConstants.SEOULTHINGS_SERVER_BASE_URL)
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(ThingAPI.class);
    mCompositeDisposable = compositeDisposable;
    mRemindClickListener = remindClickListener;
    mSnapshots = new ArrayList<>();
  }

  @NonNull
  @Override
  public RemindRecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    final View view = LayoutInflater.from(parent.getContext())
        .inflate(R.layout.remind_listitem, parent, false);
    return new RemindRecyclerViewHolder(view, mThingAPI, mCompositeDisposable,
        mRemindClickListener);
  }

  @Override
  public void onBindViewHolder(@NonNull RemindRecyclerViewHolder holder, int position) {
    final DocumentSnapshot snapshot = mSnapshots.get(position);
    final Remind remind = snapshot.toObject(Remind.class);

    if (remind != null) {
      holder.bind(remind);
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

  public void add(int index, QueryDocumentSnapshot snapshot) {
    mSnapshots.add(index, snapshot);
    notifyItemInserted(index);
  }

  public void modify(int oldIndex, int newIndex, QueryDocumentSnapshot snapshot) {
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

  public void remove(int index) {
    mSnapshots.remove(index);
    notifyItemRemoved(index);
  }
}
