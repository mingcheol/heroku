package migong.seoulthings.ui.main.profile.donations.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import java.util.ArrayList;
import java.util.List;
import migong.seoulthings.R;
import migong.seoulthings.data.Donation;

public class MyDonationRecyclerAdapter extends RecyclerView.Adapter<MyDonationRecyclerViewHolder> {

  @NonNull
  private final MyDonationRecyclerViewHolder.ClickListener mViewHolderClickListener;
  @NonNull
  private final List<DocumentSnapshot> mSnapshots;

  public MyDonationRecyclerAdapter(
      @NonNull MyDonationRecyclerViewHolder.ClickListener viewHolderClickListener) {
    mViewHolderClickListener = viewHolderClickListener;
    mSnapshots = new ArrayList<>();
  }

  @NonNull
  @Override
  public MyDonationRecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    final View view = LayoutInflater.from(parent.getContext())
        .inflate(R.layout.donation_listitem, parent, false);
    return new MyDonationRecyclerViewHolder(view, mViewHolderClickListener);
  }

  @Override
  public void onBindViewHolder(@NonNull MyDonationRecyclerViewHolder holder, int position) {
    DocumentSnapshot snapshot = mSnapshots.get(position);
    Donation donation = snapshot.toObject(Donation.class);

    if (donation != null) {
      donation.setFirebaseId(snapshot.getId());

      holder.bind(donation);
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
