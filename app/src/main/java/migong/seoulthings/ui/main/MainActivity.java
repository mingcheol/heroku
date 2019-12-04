package migong.seoulthings.ui.main;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import migong.seoulthings.R;

public class MainActivity extends AppCompatActivity implements MainView {

  private BottomNavigationView mBottomNavigation;

  private MainPresenter mPresenter;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.main_activity);

    mBottomNavigation = findViewById(R.id.main_bottom_navigation);
    mBottomNavigation.setOnNavigationItemSelectedListener(item -> {
      // 선택되지 않은 메뉴를 선택할 경우에만 동작 수행.
      return mPresenter.onBottomNavigationItemSelected(item);
    });
    mBottomNavigation.setOnNavigationItemReselectedListener(item -> {
      // 선택된 메뉴를 다시 선택할 경우 아무런 동작도 수행하지 않음.
    });

    mPresenter = new MainPresenter(this);
    mPresenter.onCreate(savedInstanceState);
  }

  @Override
  protected void onResume() {
    super.onResume();
    mPresenter.onResume();
  }

  @Override
  protected void onPause() {
    super.onPause();
    mPresenter.onPause();
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    mPresenter.onDestroy();
  }

  @Override
  public void addFragment(@NonNull Fragment fragment) {
    getSupportFragmentManager()
        .beginTransaction()
        .add(R.id.main_fragment_container, fragment)
        .commit();
  }

  @Override
  public void replaceFragment(@NonNull Fragment fragment) {
    getSupportFragmentManager()
        .beginTransaction()
        .replace(R.id.main_fragment_container, fragment)
        .commit();
  }
}
