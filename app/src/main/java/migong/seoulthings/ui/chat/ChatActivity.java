package migong.seoulthings.ui.chat;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import com.google.firebase.firestore.DocumentSnapshot;
import migong.seoulthings.R;
import migong.seoulthings.ui.chat.adapter.MessageRecyclerAdapter;
import org.apache.commons.lang3.StringUtils;

public class ChatActivity extends AppCompatActivity implements ChatView {

  private TextView mAppBarTitleText;
  private RecyclerView mMessageRecyclerView;
  private MessageRecyclerAdapter mMessageRecyclerAdapter;
  private EditText mMessageEditText;
  private ImageButton mSendButton;

  private ChatPresenter mPresenter;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.chat_activity);

    final Intent intent = getIntent();
    final Bundle args = intent.getExtras();
    if (args == null) {
      finish();
      return;
    }

    final String chatId = args.getString(KEY_CHAT_ID);
    final String chatterId = args.getString(KEY_CHATTER_ID);
    if (StringUtils.isEmpty(chatterId)) {
      finish();
      return;
    }

    setupAppBar();
    setupMessageRecycler();
    setupMessageLayout();

    mPresenter = new ChatPresenter(this, chatId, chatterId);
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
  public void setAppBarTitle(String appBarTitle) {
    mAppBarTitleText.setText(appBarTitle);
  }

  @Override
  public String getMessage() {
    if (mMessageEditText == null || mMessageEditText.getText() == null) {
      return StringUtils.EMPTY;
    }
    return mMessageEditText.getText().toString();
  }

  @Override
  public void clearSnapshots() {
    mMessageRecyclerAdapter.clear();
  }

  @Override
  public void addSnapshot(int index, DocumentSnapshot snapshot) {
    mMessageRecyclerAdapter.add(index, snapshot);
    mMessageRecyclerView.smoothScrollToPosition(index);
  }

  @Override
  public void modifySnapshot(int oldIndex, int newIndex, DocumentSnapshot snapshot) {
    mMessageRecyclerAdapter.modify(oldIndex, newIndex, snapshot);
  }

  @Override
  public void removeSnapshot(int index) {
    mMessageRecyclerAdapter.remove(index);
  }

  @Override
  public void startSending() {
    mMessageEditText.setText(StringUtils.EMPTY);
  }

  @Override
  public void finishSending() {
    mMessageRecyclerView.smoothScrollToPosition(mMessageRecyclerAdapter.getItemCount());
  }

  private void setupAppBar() {
    ImageButton backButton = findViewById(R.id.chat_back_button);
    backButton.setOnClickListener(v -> onBackPressed());

    mAppBarTitleText = findViewById(R.id.chat_app_bar_title);
  }

  private void setupMessageRecycler() {
    mMessageRecyclerView = findViewById(R.id.chat_message_recycler);
    mMessageRecyclerView.setHasFixedSize(true);
    mMessageRecyclerView.setLayoutManager(new LinearLayoutManager(this));

    mMessageRecyclerAdapter = new MessageRecyclerAdapter();
    mMessageRecyclerView.setAdapter(mMessageRecyclerAdapter);
  }

  private void setupMessageLayout() {
    mMessageEditText = findViewById(R.id.chat_message_edittext);
    mSendButton = findViewById(R.id.chat_send_button);
    mSendButton.setOnClickListener(v -> mPresenter.onSendButtonClicked());
  }
}
