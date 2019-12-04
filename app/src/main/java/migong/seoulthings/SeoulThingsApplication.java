package migong.seoulthings;

import android.app.Application;
import android.util.Log;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;

public class SeoulThingsApplication extends Application {

  private static final String TAG = SeoulThingsApplication.class.getSimpleName();

  @Override
  public void onCreate() {
    super.onCreate();
    Log.d(TAG, "onCreate() called");

    FirebaseFirestore.getInstance()
        .setFirestoreSettings(new FirebaseFirestoreSettings.Builder()
            .setTimestampsInSnapshotsEnabled(true)
            .build());
  }

  @Override
  public void onTerminate() {
    super.onTerminate();
    Log.d(TAG, "onTerminate() called");
  }
}
