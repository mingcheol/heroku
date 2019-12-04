package migong.seoulthings.ui.main.category;

import android.support.annotation.NonNull;

public interface CategoryView {

  void startSearchActivity();

  void startThingsActivity(@NonNull String category);
}
