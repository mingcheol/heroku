package migong.seoulthings.ui.donation;

import android.Manifest.permission;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ScrollView;
import android.widget.TextView;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import java.util.List;
import migong.seoulthings.R;
import migong.seoulthings.ui.chat.ChatActivity;
import migong.seoulthings.ui.chat.ChatView;
import migong.seoulthings.ui.donate.DonateActivity;
import migong.seoulthings.ui.donate.DonateView;
import migong.seoulthings.ui.donation.adapter.DonationImagePagerAdapter;
import org.apache.commons.lang3.StringUtils;

public class DonationActivity extends AppCompatActivity implements DonationView {

  private static final int PERMISSION_FOR_COARSE_LOCATION = 0x00000001;
  private static final int PERMISSION_FOR_FINE_LOCATION = 0x00000010;

  private ContentLoadingProgressBar mLoadingProgressBar;
  private ScrollView mDetailView;
  private ViewPager mImagePager;
  private DonationImagePagerAdapter mImagePagerAdapter;
  private TextView mUpdatedAtText;
  private TextView mTitleText;
  private TextView mAuthorText;
  private Button mAddressButton;
  private FrameLayout mGoogleMapContainer;
  private SupportMapFragment mGoogleMapFragment;
  private TextView mContentsText;
  private FloatingActionButton mFAB;

  private GoogleMap mGoogleMap;
  private Marker mGoogleMapMarker;
  private DonationPresenter mPresenter;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.donation_activity);

    final Intent intent = getIntent();
    final Bundle args = intent.getExtras();
    if (args == null) {
      finish();
      return;
    }

    final String donationId = args.getString(DonationView.KEY_DONATION_ID);
    if (StringUtils.isEmpty(donationId)) {
      finish();
      return;
    }

    setupAppBar();
    setupImagePager();
    setupDetailView();

    mPresenter = new DonationPresenter(this, donationId);
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
  public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
      @NonNull int[] grantResults) {
    switch (requestCode) {
      case PERMISSION_FOR_COARSE_LOCATION:
      case PERMISSION_FOR_FINE_LOCATION:
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
          setupGoogleMap();
        }
        break;
      default:
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
  }

  private void setupAppBar() {
    ImageButton backButton = findViewById(R.id.donation_back_button);
    backButton.setOnClickListener(v -> onBackPressed());
  }

  private void setupImagePager() {
    mImagePager = findViewById(R.id.donation_image_pager);
    mImagePagerAdapter = new DonationImagePagerAdapter(this, mImagePager);
    mImagePager.setAdapter(mImagePagerAdapter);
  }

  private void setupDetailView() {
    mLoadingProgressBar = findViewById(R.id.donation_detail_loading_progressbar);
    mLoadingProgressBar.show();
    mDetailView = findViewById(R.id.donation_detail);

    mUpdatedAtText = findViewById(R.id.donation_updatedat);
    mTitleText = findViewById(R.id.donation_title);
    mAuthorText = findViewById(R.id.donation_author);

    mAddressButton = findViewById(R.id.donation_address_button);
    mAddressButton.setOnClickListener(v -> mPresenter.onAddressButtonClicked());
    mGoogleMapContainer = findViewById(R.id.donation_map_container);
    mGoogleMapFragment = (SupportMapFragment) getSupportFragmentManager()
        .findFragmentById(R.id.donation_map);
    if (mGoogleMapFragment != null) {
      mGoogleMapFragment.getMapAsync(googleMap -> {
        mGoogleMap = googleMap;
        setupGoogleMap();
      });
    }

    mContentsText = findViewById(R.id.donation_contents);
    mFAB = findViewById(R.id.donation_fab);
    mFAB.setOnClickListener(v -> mPresenter.onFABClicked());
  }

  private void setupGoogleMap() {
    if (ActivityCompat.checkSelfPermission(this, permission.ACCESS_COARSE_LOCATION)
        != PackageManager.PERMISSION_GRANTED) {
      ActivityCompat.requestPermissions(this, new String[]{permission.ACCESS_COARSE_LOCATION},
          PERMISSION_FOR_COARSE_LOCATION);
      return;
    }
    if (ActivityCompat.checkSelfPermission(this, permission.ACCESS_FINE_LOCATION)
        != PackageManager.PERMISSION_GRANTED) {
      ActivityCompat.requestPermissions(this, new String[]{permission.ACCESS_FINE_LOCATION},
          PERMISSION_FOR_FINE_LOCATION);
      return;
    }

    mGoogleMap.setMyLocationEnabled(true);
  }

  @Override
  public void startChatActivity(@NonNull String chatterId) {
    Intent intent = new Intent(this, ChatActivity.class);

    Bundle args = new Bundle();
    args.putString(ChatView.KEY_CHATTER_ID, chatterId);
    intent.putExtras(args);

    startActivity(intent);
  }

  @Override
  public void startDonateActivity(String donationId) {
    Intent intent = new Intent(this, DonateActivity.class);

    Bundle args = new Bundle();
    args.putString(DonateView.KEY_DONATION_ID, donationId);
    intent.putExtras(args);

    startActivity(intent);
  }

  @Override
  public void finishLoading() {
    mDetailView.setVisibility(View.VISIBLE);
    mLoadingProgressBar.hide();
  }

  @Override
  public void setImages(List<Uri> images) {
    mImagePagerAdapter.setImages(images);
  }

  @Override
  public void addImage(Uri uri) {
    mImagePagerAdapter.addImage(uri);
  }

  @Override
  public void setTitle(String title) {
    mTitleText.setText(title);
  }

  @Override
  public void setContents(String contents) {
    mContentsText.setText(contents);
  }

  @Override
  public void setAuthor(String author) {
    mAuthorText.setText(author);
  }

  @Override
  public void setFABIcon(int iconResId) {
    mFAB.setImageDrawable(getResources().getDrawable(iconResId));
  }

  @Override
  public void disableFAB() {
    mFAB.setEnabled(false);
    mFAB.setFocusable(false);
    mFAB.setFocusableInTouchMode(false);
  }

  @Override
  public void setUpdatedAt(String updatedAt) {
    mUpdatedAtText.setText(updatedAt);
  }

  @Override
  public void setThoroughfare(String thoroughfare) {
    mAddressButton.setText(thoroughfare);
  }

  @Override
  public void setGoogleMapLocation(@NonNull LatLng latLng) {
    if (mGoogleMap == null) {
      return;
    }

    if (mGoogleMapMarker != null) {
      mGoogleMapMarker.remove();
    }
    mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 13.0f));
    mGoogleMapMarker = mGoogleMap.addMarker(new MarkerOptions().position(latLng));
  }

  @Override
  public void showGoogleMap() {
    mGoogleMapContainer.setVisibility(View.VISIBLE);
    mAddressButton.setCompoundDrawablesWithIntrinsicBounds(0, 0,
        R.drawable.ic_arrow_up_black_24, 0);
  }

  @Override
  public void hideGoogleMap() {
    mGoogleMapContainer.setVisibility(View.GONE);
    mAddressButton.setCompoundDrawablesWithIntrinsicBounds(0, 0,
        R.drawable.ic_arrow_down_black_24, 0);
  }
}
