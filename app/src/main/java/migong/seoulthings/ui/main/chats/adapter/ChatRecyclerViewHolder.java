package migong.seoulthings.ui.main.chats.adapter;

import android.annotation.SuppressLint;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
import com.google.firebase.auth.FirebaseUser;
import com.makeramen.roundedimageview.RoundedImageView;
import com.makeramen.roundedimageview.RoundedTransformationBuilder;
import com.squareup.picasso.Picasso;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import java.text.SimpleDateFormat;
import migong.seoulthings.R;
import migong.seoulthings.api.FirebaseAPI;
import migong.seoulthings.data.Chat;
import org.apache.commons.lang3.StringUtils;

public class ChatRecyclerViewHolder extends RecyclerView.ViewHolder {

  public interface ClickListener {

    void onClick(@NonNull String chatId, @NonNull String chatterId);

  }

  @SuppressLint("SimpleDateFormat")
  private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy.MM.dd");
  private static final String TAG = ChatRecyclerViewHolder.class.getSimpleName();

  private ContentLoadingProgressBar mLoadingProgressBar;
  private RoundedImageView mProfilePhotoImage;
  private TextView mChatterText;
  private TextView mUpdatedAtText;
  private TextView mMessageText;

  private String mChatterId;
  private final FirebaseUser mUser;
  @NonNull
  private final FirebaseAPI mFirebaseAPI;
  @NonNull
  private final CompositeDisposable mComponentDisposable;
  @NonNull
  private final ClickListener mClickListener;

  public ChatRecyclerViewHolder(@NonNull View itemView, FirebaseUser user,
      @NonNull FirebaseAPI firebaseAPI, @NonNull CompositeDisposable compositeDisposable,
      @NonNull ClickListener clickListener) {
    super(itemView);
    mLoadingProgressBar = itemView.findViewById(R.id.chat_listitem_loading_progressbar);
    mLoadingProgressBar.show();
    mProfilePhotoImage = itemView.findViewById(R.id.chat_listitem_profile_photo);
    mChatterText = itemView.findViewById(R.id.chat_listitem_chatter);
    mUpdatedAtText = itemView.findViewById(R.id.chat_listitem_updated_at);
    mMessageText = itemView.findViewById(R.id.chat_listitem_message);

    mUser = user;
    mFirebaseAPI = firebaseAPI;
    mComponentDisposable = compositeDisposable;
    mClickListener = clickListener;
  }

  public void bind(@NonNull Chat chat) {
    if (chat.getChatters().size() != 2) {
      Log.e(TAG, "bind: the number of chatter must be 2. chat is " + chat);
      return;
    }

    if (StringUtils.equals(mUser.getUid(), chat.getChatters().get(0))) {
      mChatterId = chat.getChatters().get(1);
    } else if (StringUtils.equals(mUser.getUid(), chat.getChatters().get(1))) {
      mChatterId = chat.getChatters().get(0);
    } else {
      Log.e(TAG, "bind: invalid. chat is " + chat + " with user " + mUser.getUid());
      return;
    }

    mComponentDisposable.add(
        mFirebaseAPI.getFirebaseUser(mChatterId)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe(
                user -> {
                  if (user == null) {
                    Log.e(TAG, "bind: Firebase user is NULL.");
                    return;
                  }

                  if (StringUtils.isNotEmpty(user.getPhotoURL())) {
                    Picasso.get()
                        .load(Uri.parse(user.getPhotoURL()))
                        .centerCrop(Gravity.TOP | Gravity.CENTER_HORIZONTAL)
                        .fit()
                        .transform(new RoundedTransformationBuilder()
                            .borderColor(R.color.colorStroke)
                            .borderWidthDp(0.1f)
                            .oval(true)
                            .build())
                        .into(mProfilePhotoImage);
                  }
                  mChatterText.setText(user.getDisplayName());
                  mUpdatedAtText.setText(DATE_FORMAT.format(chat.getUpdatedAt().toDate()));
                  mMessageText.setText(chat.getLastMessage());
                  itemView.setOnClickListener(
                      v -> mClickListener.onClick(chat.getFirebaseId(), mChatterId)
                  );

                  finishLoading();
                },
                error -> {
                  Log.e(TAG, "Failed to get a Firebase user of id " + mChatterId, error);
                }
            )
    );
  }

  public void clear() {
    startLoading();
  }

  private void startLoading() {
    mLoadingProgressBar.show();

    mProfilePhotoImage.setVisibility(View.GONE);
    mChatterText.setVisibility(View.GONE);
    mUpdatedAtText.setVisibility(View.GONE);
    mMessageText.setVisibility(View.GONE);
  }

  private void finishLoading() {
    mLoadingProgressBar.hide();

    mProfilePhotoImage.setVisibility(View.VISIBLE);
    mChatterText.setVisibility(View.VISIBLE);
    mUpdatedAtText.setVisibility(View.VISIBLE);
    mMessageText.setVisibility(View.VISIBLE);
  }
}
