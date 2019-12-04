package migong.seoulthings.ui.main.chats;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.Query.Direction;
import migong.seoulthings.ui.Presenter;

public class ChatsPresenter implements Presenter {

  private static final String TAG = ChatsPresenter.class.getSimpleName();

  private FirebaseUser mUser;
  private FirebaseFirestore mFirestore;
  private Query mQuery;
  private ListenerRegistration mRegistration;
  @NonNull
  private ChatsView mView;

  public ChatsPresenter(@NonNull ChatsView view) {
    mView = view;
  }

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    Log.d(TAG, "onCreate() called with: savedInstanceState = [" + savedInstanceState + "]");

    mUser = FirebaseAuth.getInstance().getCurrentUser();
    mFirestore = FirebaseFirestore.getInstance();
    mQuery = mFirestore.collection("chats")
        .whereArrayContains("chatters", mUser.getUid())
        .orderBy("updatedAt", Direction.DESCENDING);
  }

  @Override
  public void onResume() {
    Log.d(TAG, "onResume() called");

    startListening();
  }

  @Override
  public void onPause() {
    Log.d(TAG, "onPause() called");

    stopListening();
  }

  @Override
  public void onDestroy() {
    Log.d(TAG, "onDestroy() called");
  }

  public void onChatClicked(@NonNull String chatId, @NonNull String chatterId) {
    mView.startChatActivity(chatId, chatterId);
  }

  private void startListening() {
    if (mQuery != null && mRegistration == null) {
      mRegistration = mQuery.addSnapshotListener((snapshot, e) -> {
        if (e != null) {
          Log.e(TAG, "onEvent: error", e);
          return;
        }

        if (snapshot == null) {
          Log.e(TAG, "onEvent: snapshot is NULL.");
          return;
        }

        Log.d(TAG, "onEvent: numChanges is " + snapshot.getDocumentChanges().size());
        for (DocumentChange change : snapshot.getDocumentChanges()) {
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
      });
    }
  }

  private void stopListening() {
    if (mRegistration != null) {
      mRegistration.remove();
      mRegistration = null;
    }

    mView.clearSnapshots();
  }

  private void onDocumentAdded(DocumentChange change) {
    mView.addSnapshot(change.getNewIndex(), change.getDocument());
  }

  private void onDocumentModified(DocumentChange change) {
    mView.modifySnapshot(change.getOldIndex(), change.getNewIndex(), change.getDocument());
  }

  private void onDocumentRemoved(DocumentChange change) {
    mView.removeSnapshot(change.getOldIndex());
  }
}
