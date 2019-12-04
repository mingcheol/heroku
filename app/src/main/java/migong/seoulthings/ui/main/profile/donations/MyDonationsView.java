package migong.seoulthings.ui.main.profile.donations;

import android.support.annotation.NonNull;
import com.google.firebase.firestore.QueryDocumentSnapshot;

public interface MyDonationsView {

  void startDonationActivity(@NonNull String donationId);

  void clearSnapshots();

  void addSnapshot(int index, QueryDocumentSnapshot snapshot);

  void modifySnapshot(int oldIndex, int newIndex, QueryDocumentSnapshot snapshot);

  void removeSnapshot(int index);
}
