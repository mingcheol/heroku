package migong.seoulthings.ui.main.profile.reminds;

import android.support.annotation.NonNull;
import com.google.firebase.firestore.QueryDocumentSnapshot;

public interface MyRemindsView {

  void startThingActivity(@NonNull String thingId);

  void clearSnapshots();

  void addSnapshot(int index, QueryDocumentSnapshot snapshot);

  void modifySnapshot(int oldIndex, int newIndex, QueryDocumentSnapshot snapshot);

  void removeSnapshot(int index);
}
