package migong.seoulthings.ui.main.chats.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import io.reactivex.disposables.CompositeDisposable;
import java.util.ArrayList;
import java.util.List;
import migong.seoulthings.R;
import migong.seoulthings.SeoulThingsConstants;
import migong.seoulthings.api.FirebaseAPI;
import migong.seoulthings.data.Chat;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class ChatRecyclerAdapter extends RecyclerView.Adapter<ChatRecyclerViewHolder> {

  @NonNull
  private final CompositeDisposable mComponentDisposable;
  @NonNull
  private final ChatRecyclerViewHolder.ClickListener mChatClickListener;
  private final FirebaseUser mUser;
  @NonNull
  private final FirebaseAPI mFirebaseAPI;
  @NonNull
  private final List<DocumentSnapshot> mSnapshots;

  public ChatRecyclerAdapter(@NonNull CompositeDisposable compositeDisposable,
      @NonNull ChatRecyclerViewHolder.ClickListener chatClickListener) {
    mComponentDisposable = compositeDisposable;
    mChatClickListener = chatClickListener;

    mUser = FirebaseAuth.getInstance().getCurrentUser();
    mFirebaseAPI = new Retrofit.Builder()
        .baseUrl(SeoulThingsConstants.SEOULTHINGS_SERVER_BASE_URL)
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(FirebaseAPI.class);
    mSnapshots = new ArrayList<>();
  }

  @NonNull
  @Override
  public ChatRecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    final View view = LayoutInflater.from(parent.getContext())
        .inflate(R.layout.chat_listitem, parent, false);
    return new ChatRecyclerViewHolder(
        view, mUser, mFirebaseAPI, mComponentDisposable, mChatClickListener);
  }

  @Override
  public void onBindViewHolder(@NonNull ChatRecyclerViewHolder holder, int position) {
    final DocumentSnapshot snapshot = mSnapshots.get(position);
    final Chat chat = snapshot.toObject(Chat.class);

    if (chat != null) {
      chat.setFirebaseId(snapshot.getId());

      holder.bind(chat);
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

  public void add(int index, DocumentSnapshot snapshot) {
    mSnapshots.add(index, snapshot);
    notifyItemInserted(index);
  }

  public void modify(int oldIndex, int newIndex, DocumentSnapshot snapshot) {
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
