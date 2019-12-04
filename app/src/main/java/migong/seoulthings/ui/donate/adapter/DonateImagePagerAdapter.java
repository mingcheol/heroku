package migong.seoulthings.ui.donate.adapter;

import android.content.Context;
import android.content.res.Resources;
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

public class DonateImagePagerAdapter extends PagerAdapter {

  public interface AddPhotoButtonClickListener {

    void onClick();

  }

  @NonNull
  private final Context mContext;
  @NonNull
  private final ViewPager mViewPager;
  @NonNull
  private final AddPhotoButtonClickListener mAddPhotoButtonClickListener;
  @NonNull
  private final List<Uri> mImages;

  public DonateImagePagerAdapter(@NonNull Context context, @NonNull ViewPager viewPager,
      @NonNull AddPhotoButtonClickListener addPhotoButtonClickListener) {
    mContext = context;
    mViewPager = viewPager;
    mAddPhotoButtonClickListener = addPhotoButtonClickListener;
    mImages = new ArrayList<>();
  }

  @NonNull
  @Override
  public Object instantiateItem(@NonNull ViewGroup container, int position) {
    final View view;
    if (position == 0) {
      view = getAddPhotoImageButton(container);
    } else {
      view = getImageView(container, position);
    }

    mViewPager.addView(view, position);
    return view;
  }

  @Override
  public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
  }

  @Override
  public int getCount() {
    return mImages.size() + 1;
  }

  @Override
  public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
    return view.equals(o);
  }

  public List<Uri> getImages() {
    return mImages;
  }

  public void setImages(@NonNull List<Uri> images) {
    mImages.clear();
    mImages.addAll(images);
    notifyDataSetChanged();
  }

  public void addImage(@NonNull Uri imageUri) {
    mImages.add(imageUri);
    notifyDataSetChanged();

    mViewPager.setCurrentItem(mImages.size());
  }

  private Uri getImageUri(int position) {
    return mImages.get(position - 1);
  }

  private View getAddPhotoImageButton(@NonNull ViewGroup container) {
    final Resources resources = mContext.getResources();
    final View view = LayoutInflater.from(mContext)
        .inflate(R.layout.donate_image, container, false);
    ImageButton imageButton = view.findViewById(R.id.donate_image);
    imageButton.setImageDrawable(resources.getDrawable(R.drawable.ic_add_image_black_48));
    imageButton.setOnClickListener(v -> mAddPhotoButtonClickListener.onClick());
    return view;
  }

  private View getImageView(@NonNull ViewGroup container, int position) {
    final View view = LayoutInflater.from(mContext)
        .inflate(R.layout.donate_image, container, false);
    ImageButton imageButton = view.findViewById(R.id.donate_image);
    Picasso.get()
        .load(getImageUri(position))
        .centerCrop()
        .fit()
        .into(imageButton);
    return view;
  }
}
