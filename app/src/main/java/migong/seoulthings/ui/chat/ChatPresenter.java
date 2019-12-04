package migong.seoulthings.ui.chat;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.Query.Direction;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import migong.seoulthings.SeoulThingsConstants;
import migong.seoulthings.api.FirebaseAPI;
import migong.seoulthings.data.Chat;
import migong.seoulthings.data.Message;
import migong.seoulthings.ui.Presenter;
import org.apache.commons.lang3.StringUtils;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class ChatPresenter implements Presenter {

  private static final String TAG = ChatPresenter.class.getSimpleName();

  private FirebaseUser mUser;
  private FirebaseFirestore mFirestore;
  @Nullable
  private DocumentReference mChatReference;
  @Nullable
  private Query mMessageQuery;
  @Nullable
  private ListenerRegistration mMessageRegistration;
  private FirebaseAPI mFirebaseAPI;
  @Nullable
  private String mChatId;
  @NonNull
  private final String mChatterId;
  @NonNull
  private final ChatView mView;
  @NonNull
  private final CompositeDisposable mCompositeDisposable;

  public ChatPresenter(@NonNull ChatView view, @Nullable String chatId, @NonNull String chatterId) {
    mView = view;
    mChatId = chatId;
    mChatterId = chatterId;
    mCompositeDisposable = new CompositeDisposable();
  }

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    Log.d(TAG, "onCreate() called with: savedInstanceState = [" + savedInstanceState + "]");

    mUser = FirebaseAuth.getInstance().getCurrentUser();
    mFirestore = FirebaseFirestore.getInstance();

    if (StringUtils.isNotEmpty(mChatId)) {
      mChatReference = mFirestore.collection("chats")
          .document(mChatId);
      mMessageQuery = mFirestore.collection("messages")
          .whereEqualTo("chatId", mChatId)
          .orderBy("createdAt", Direction.ASCENDING);
    }

    mFirebaseAPI = new Retrofit.Builder()
        .baseUrl(SeoulThingsConstants.SEOULTHINGS_SERVER_BASE_URL)
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(FirebaseAPI.class);
    mCompositeDisposable.add(
        mFirebaseAPI.getFirebaseUser(mChatterId)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe(
                user -> {
                  if (user == null) {
                    Log.e(TAG, "bind: Firebase user is NULL.");
                    return;
                  }

                  mView.setAppBarTitle(user.getDisplayName());
                },
                error -> {
                  Log.e(TAG, "Failed to get a Firebase user of id " + mChatterId, error);
                }
            )
    );
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

  public void onSendButtonClicked() {
    final String message = mView.getMessage();
    if (StringUtils.isEmpty(message)) {
      return;
    }
    mView.startSending();

    if (mChatReference == null) {
      mFirestore.collection("chats")
          .add(new Chat(mUser.getUid(), mChatterId, message))
          .addOnFailureListener(error -> {
            Log.e(TAG, "Failed to add a chat document.", error);
          })
          .addOnSuccessListener(reference -> {
            mChatReference = reference;
            mChatId = reference.getId();
            mMessageQuery = mFirestore.collection("messages")
                .whereEqualTo("chatId", mChatId)
                .orderBy("createdAt", Direction.ASCENDING);
            startListening();
          })
          .continueWithTask(task -> Tasks.whenAllComplete(
              mChatReference.update("lastMessage", message),
              mFirestore.collection("messages").add(new Message(mChatId, mUser.getUid(), message))
          ))
          .addOnCompleteListener(task -> mView.finishSending())
          .addOnFailureListener(error -> {
            Log.e(TAG, "Failed to add a message document.", error);
          });
    } else {
      Tasks
          .whenAllComplete(
              mChatReference.update("lastMessage", message),
              mFirestore.collection("messages").add(new Message(mChatId, mUser.getUid(), message))
          )
          .addOnCompleteListener(task -> mView.finishSending())
          .addOnFailureListener(error -> {
            Log.e(TAG, "Failed to add a message document.", error);
          });
    }
  }

  private void startListening() {
    if (mMessageQuery != null && mMessageRegistration == null) {
      mMessageRegistration = mMessageQuery.addSnapshotListener((snapshot, e) -> {
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
    if (mMessageRegistration != null) {
      mMessageRegistration.remove();
      mMessageRegistration = null;
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
