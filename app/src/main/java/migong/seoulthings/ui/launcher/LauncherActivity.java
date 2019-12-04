package migong.seoulthings.ui.launcher;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import migong.seoulthings.R;
import migong.seoulthings.ui.main.MainActivity;
import migong.seoulthings.ui.signin.SignInActivity;

public class LauncherActivity extends AppCompatActivity implements LauncherView {

    private LauncherPresenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.launcher_activity);

        mPresenter = new LauncherPresenter(this);
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
    public void startSignInActivity() {
        Handler handler = new Handler();
        handler.postDelayed(() -> {
            Intent intent = new Intent(getBaseContext(), SignInActivity.class);
            startActivity(intent);
            finish();
        }, 2000);
    }
}
