package migong.seoulthings.ui.donation.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;
import java.util.List;
import migong.seoulthings.R;

public class DonationImagePagerAdapter extends PagerAdapter {

  @NonNull
  private final Context mContext;
  @NonNull
  private final ViewPager mViewPager;
  @NonNull
  private final List<Uri> mImages;

  public DonationImagePagerAdapter(@NonNull Context context, @NonNull ViewPager viewPager) {
    mContext = context;
    mViewPager = viewPager;
    mImages = new ArrayList<>();
  }

  @NonNull
  @Override
  public Object instantiateItem(@NonNull ViewGroup container, int position) {
    final View view = getImageView(container, position);
    mViewPager.addView(view);
    return view;
  }

  @Override
  public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
  }

  @Override
  public int getCount() {
    return mImages.size();
  }

  @Override
  public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
    return view.equals(o);
  }

  public void setImages(List<Uri> images) {
    mImages.clear();
    mImages.addAll(images);
    notifyDataSetChanged();
  }

  public void addImage(@NonNull Uri imageUri) {
    mImages.add(imageUri);
    notifyDataSetChanged();
  }

  private Uri getImageUri(int position) {
    return mImages.get(position);
  }

  private View getImageView(@NonNull ViewGroup container, int position) {
    final View view = LayoutInflater.from(mContext)
        .inflate(R.layout.donate_image, container, false);
    ImageButton imageButton = view.findViewById(R.id.donate_image);
    Picasso.get()
        .load(getImageUri(position))
        .fit()
        .into(imageButton);
    return view;
  }
}
