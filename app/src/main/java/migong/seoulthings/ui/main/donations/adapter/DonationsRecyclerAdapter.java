package migong.seoulthings.ui.main.donations.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView.Adapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nullable;
import migong.seoulthings.R;
import migong.seoulthings.ui.main.donations.adapter.DonationsRecyclerViewHolder.OnClickListener;

public class DonationsRecyclerAdapter extends Adapter<DonationsRecyclerViewHolder>
    implements EventListener<QuerySnapshot> {

  private static final String TAG = DonationsRecyclerAdapter.class.getSimpleName();

  private Query mQuery;
  private ListenerRegistration mRegistration;
  @NonNull
  private OnClickListener mOnClickListener;
  @NonNull
  private final List<DocumentSnapshot> mSnapshots;

  public DonationsRecyclerAdapter(Query query, @NonNull OnClickListener onClickListener) {
    mQuery = query;
    mOnClickListener = onClickListener;
    mSnapshots = new ArrayList<>();
  }

  @NonNull
  @Override
  public DonationsRecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    final View view = LayoutInflater.from(parent.getContext())
        .inflate(R.layout.donation_griditem, parent, false);
    return new DonationsRecyclerViewHolder(view, mOnClickListener);
  }

  @Override
  public void onBindViewHolder(@NonNull DonationsRecyclerViewHolder holder, int position) {
    holder.bind(getSnapshot(position));
  }

  @Override
  public int getItemCount() {
    return mSnapshots.size();
  }

  @Override
  public void onEvent(@Nullable QuerySnapshot documentSnapshots,
      @Nullable FirebaseFirestoreException e) {
    if (e != null) {
      Log.e(TAG, "onEvent: error", e);
      return;
    }

    if (documentSnapshots == null) {
      Log.e(TAG, "onEvent: documentSnapshots is NULL.");
      return;
    }

    Log.d(TAG, "onEvent: numChanges is " + documentSnapshots.getDocumentChanges().size());
    for (DocumentChange change : documentSnapshots.getDocumentChanges()) {
      switch (change.getType()) {
        case ADDED:
          onDocumentAdded(change);
          break;
        case MODIFIED:
          onDocumentModified(change);
          break;
        case REMOVED:
          onDocumentRemoved(change);
          break;
      }
    }
  }

  public DocumentSnapshot getSnapshot(int position) {
    return mSnapshots.get(position);
  }

  public void setQuery(Query query) {
    stopListening();

    mSnapshots.clear();
    notifyDataSetChanged();

    mQuery = query;
    startListening();
  }

  public void startListening() {
    if (mQuery != null && mRegistration == null) {
      mRegistration = mQuery.addSnapshotListener(this);
    }
  }

  public void stopListening() {
    if (mRegistration != null) {
      mRegistration.remove();
      mRegistration = null;
    }

    mSnapshots.clear();
    notifyDataSetChanged();
  }

  private void onDocumentAdded(DocumentChange change) {
    Log.d(TAG, "onDocumentAdded() called with: change = [" + change + "]");

    mSnapshots.add(change.getNewIndex(), change.getDocument());
    notifyItemInserted(change.getNewIndex());
  }

  private void onDocumentModified(DocumentChange change) {
    Log.d(TAG, "onDocumentModified() called with: change = [" + change + "]");

    if (change.getOldIndex() == change.getNewIndex()) {
      mSnapshots.set(change.getOldIndex(), change.getDocument());
      notifyItemChanged(change.getOldIndex());
    } else {
      mSnapshots.remove(change.getOldIndex());
      mSnapshots.add(change.getNewIndex(), change.getDocument());
      notifyItemMoved(change.getOldIndex(), change.getNewIndex());
    }
  }

  private void onDocumentRemoved(DocumentChange change) {
    Log.d(TAG, "onDocumentRemoved() called with: change = [" + change + "]");

    mSnapshots.remove(change.getOldIndex());
    notifyItemRemoved(change.getOldIndex());
  }
}
