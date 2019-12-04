package migong.seoulthings.ui.main.profile.reviews;

import android.support.annotation.NonNull;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import migong.seoulthings.data.Review;

public interface MyReviewsView {

  void showReviewDialog(@NonNull Review review);

  void clearSnapshots();

  void addSnapshot(int index, QueryDocumentSnapshot snapshot);

  void modifySnapshot(int oldIndex, int newIndex, QueryDocumentSnapshot snapshot);

  void removeSnapshot(int index);
}
