package migong.seoulthings.ui.main.profile.adapter;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import migong.seoulthings.ui.main.profile.ProfileView;
import migong.seoulthings.ui.main.profile.donations.MyDonationsFragment;
import migong.seoulthings.ui.main.profile.reminds.MyRemindsFragment;
import migong.seoulthings.ui.main.profile.reviews.MyReviewsFragment;

public class ProfilePagerAdapter extends FragmentPagerAdapter {

  public ProfilePagerAdapter(FragmentManager fm) {
    super(fm);
  }

  @Override
  public Fragment getItem(int position) {
    switch (position) {
      case ProfileView.MY_THINGS_POSITION:
        return new MyRemindsFragment();
      case ProfileView.MY_DONATIONS_POSITION:
        return new MyDonationsFragment();
      case ProfileView.MY_REVIEWS_POSITION:
        return new MyReviewsFragment();
    }
    return null;
  }

  @Override
  public int getCount() {
    return 3;
  }

  @Nullable
  @Override
  public CharSequence getPageTitle(int position) {
    switch (position) {
      case ProfileView.MY_THINGS_POSITION:
        return "프로필";
      case ProfileView.MY_DONATIONS_POSITION:
        return "또또마켓";
      case ProfileView.MY_REVIEWS_POSITION:
        return "한 줄 후기";
    }
    return super.getPageTitle(position);
  }
}
