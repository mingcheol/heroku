package migong.seoulthings.ui.chat;

import com.google.firebase.firestore.DocumentSnapshot;

public interface ChatView {

  String KEY_CHAT_ID = "CHAT_ID";
  String KEY_CHATTER_ID = "CHATTER_ID";

  void setAppBarTitle(String appBarTitle);

  String getMessage();

  void clearSnapshots();

  void addSnapshot(int index, DocumentSnapshot snapshot);

  void modifySnapshot(int oldIndex, int newIndex, DocumentSnapshot snapshot);

  void removeSnapshot(int index);

  void startSending();

  void finishSending();
}
