package migong.seoulthings.ui.search;

import android.support.annotation.NonNull;
import java.util.List;

public interface SearchView {

  String KEY_SCOPE = "SCOPE";
  String KEY_CATEGORY = "CATEGORY";
  String SCOPE_THINGS = "SCOPE_THINGS";
  String SCOPE_DONATIONS = "SCOPE_DONATIONS";

  void startThingActivity(@NonNull String thingId);

  void startDonationActivity(@NonNull String donationId);

  void changeSearchResult(List<SearchResult> searchResults);

  void clearSearchResult();

  void clearQuery();

  void showClearQueryButton();

  void hideClearQueryButton();

  void showProgressBar();

  void hideProgressBar();

  void showEmptyView();

  void hideEmptyView();
}
