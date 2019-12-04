package migong.seoulthings.ui.main.donations;

import android.support.annotation.NonNull;
import com.google.firebase.firestore.Query;

public interface DonationsView {

  int GRID_LAYOUT_SPAN_COUNT = 3;

  void startSearchActivity();

  void startDonateActivity();

  void startDonationActivity(@NonNull String donationId);

  void setQuery(Query query);

  void startListening();

  void stopListening();
}
