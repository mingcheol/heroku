package migong.seoulthings.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;

public interface Presenter {

  void onCreate(@Nullable Bundle savedInstanceState);

  void onResume();

  void onPause();

  void onDestroy();

}
