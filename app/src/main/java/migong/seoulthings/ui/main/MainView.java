package migong.seoulthings.ui.main;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;

public interface MainView {

  void addFragment(@NonNull Fragment fragment);

  void replaceFragment(@NonNull Fragment fragment);
}
