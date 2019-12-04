package migong.seoulthings.ui.chat.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import java.util.ArrayList;
import java.util.List;
import migong.seoulthings.R;
import migong.seoulthings.data.Message;
import org.apache.commons.lang3.StringUtils;

public class MessageRecyclerAdapter extends RecyclerView.Adapter<MessageRecyclerViewHolder> {

  private static final int VIEW_TYPE_SENDER = 0x00000001;
  private static final int VIEW_TYPE_RECEIVER = 0x00000010;
  private final FirebaseUser mUser;
  private final List<DocumentSnapshot> mSnapshots;

  public MessageRecyclerAdapter() {
    mUser = FirebaseAuth.getInstance().getCurrentUser();
    mSnapshots = new ArrayList<>();
  }

  @NonNull
  @Override
  public MessageRecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    if (viewType == VIEW_TYPE_SENDER) {
      final View view = LayoutInflater.from(parent.getContext())
          .inflate(R.layout.message_listitem_send, parent, false);
      return new MessageRecyclerViewHolder(view);
    } else {
      final View view = LayoutInflater.from(parent.getContext())
          .inflate(R.layout.message_listitem_receive, parent, false);
      return new MessageRecyclerViewHolder(view);
    }
  }

  @Override
  public void onBindViewHolder(@NonNull MessageRecyclerViewHolder holder, int position) {
    DocumentSnapshot snapshot = mSnapshots.get(position);
    Message message = snapshot.toObject(Message.class);

    if (message != null) {
      holder.setMessage(message.getMessage());
    }
  }

  @Override
  public int getItemViewType(int position) {
    DocumentSnapshot snapshot = mSnapshots.get(position);
    String senderId = snapshot.getString("senderId");

    if (StringUtils.equals(mUser.getUid(), senderId)) {
      return VIEW_TYPE_SENDER;
    } else {
      return VIEW_TYPE_RECEIVER;
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
