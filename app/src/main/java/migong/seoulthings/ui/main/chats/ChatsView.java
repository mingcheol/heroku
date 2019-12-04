package migong.seoulthings.ui.main.chats;

import android.support.annotation.NonNull;
import com.google.firebase.firestore.DocumentSnapshot;

public interface ChatsView {

  void startChatActivity(@NonNull String chatId, @NonNull String chatterId);

  void clearSnapshots();

  void addSnapshot(int index, DocumentSnapshot snapshot);

  void modifySnapshot(int oldIndex, int newIndex, DocumentSnapshot snapshot);

  void removeSnapshot(int index);
}
