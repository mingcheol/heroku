package migong.seoulthings.ui.main.profile.reminds.adapter;

import android.annotation.SuppressLint;
import android.support.annotation.NonNull;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import com.squareup.picasso.Picasso;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import java.text.SimpleDateFormat;
import migong.seoulthings.R;
import migong.seoulthings.api.ThingAPI;
import migong.seoulthings.data.Category;
import migong.seoulthings.data.Remind;
import migong.seoulthings.data.Thing;

public class RemindRecyclerViewHolder extends RecyclerView.ViewHolder {

  public interface ClickListener {

    void onClick(@NonNull String thingId);

  }

  @SuppressLint("SimpleDateFormat")
  private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy년 MM월 dd일");
  private static final String TAG = RemindRecyclerViewHolder.class.getSimpleName();

  private ContentLoadingProgressBar mLoadingProgressBar;
  private ImageView mThingIconImage;
  private ImageButton mDetailButton;
  private TextView mThingNameText;
  private TextView mDueLabel;
  private TextView mDueText;

  @NonNull
  private final ThingAPI mThingAPI;
  @NonNull
  private final CompositeDisposable mCompositeDisposable;
  @NonNull
  private final ClickListener mClickListener;

  public RemindRecyclerViewHolder(@NonNull View itemView, @NonNull ThingAPI thingAPI,
      @NonNull CompositeDisposable compositeDisposable, @NonNull ClickListener clickListener) {
    super(itemView);
    mLoadingProgressBar = itemView.findViewById(R.id.remind_listitem_loading_progressbar);
    mLoadingProgressBar.show();
    mThingIconImage = itemView.findViewById(R.id.remind_listitem_thing_icon);
    mDetailButton = itemView.findViewById(R.id.remind_listitem_detail_button);
    mThingNameText = itemView.findViewById(R.id.remind_listitem_thing_name);
    mDueLabel = itemView.findViewById(R.id.remind_listitem_due_label);
    mDueText = itemView.findViewById(R.id.remind_listitem_due);

    mThingAPI = thingAPI;
    mCompositeDisposable = compositeDisposable;
    mClickListener = clickListener;
  }

  public void bind(@NonNull Remind remind) {
    mCompositeDisposable.add(
        mThingAPI.getThing(remind.getThingId())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe(
                response -> {
                  final int size = response.getSize();
                  final Thing thing = response.getThing();

                  if (size == 0 || thing == null) {
                    Log.e(TAG, "Failed to get a thing of id " + remind.getThingId());
                    return;
                  }

                  Picasso.get()
                      .load(Category.getIconRedId(thing.getCategory()))
                      .into(mThingIconImage);
                  mThingNameText.setText(thing.getLocation().getName());
                  mDueText.setText(DATE_FORMAT.format(remind.getDue().toDate()));
                  mDetailButton.setOnClickListener(
                      v -> mClickListener.onClick(remind.getThingId())
                  );

                  finishLoading();
                },
                error -> {
                  Log.e(TAG, "Failed to get a thing of id " + remind.getThingId(), error);
                }
            )
    );
  }

  public void clear() {
    startLoading();
  }

  private void startLoading() {
    mLoadingProgressBar.show();

    mThingIconImage.setVisibility(View.GONE);
    mDetailButton.setVisibility(View.GONE);
    mThingNameText.setVisibility(View.GONE);
    mDueLabel.setVisibility(View.GONE);
    mDueText.setVisibility(View.GONE);
  }

  private void finishLoading() {
    mLoadingProgressBar.hide();

    mThingIconImage.setVisibility(View.VISIBLE);
    mDetailButton.setVisibility(View.VISIBLE);
    mThingNameText.setVisibility(View.VISIBLE);
    mDueLabel.setVisibility(View.VISIBLE);
    mDueText.setVisibility(View.VISIBLE);
  }
}
