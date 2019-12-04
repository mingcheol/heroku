package migong.seoulthings.ui.main.chats;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.google.firebase.firestore.DocumentSnapshot;

import io.reactivex.disposables.CompositeDisposable;
import migong.seoulthings.R;
import migong.seoulthings.ui.chat.ChatActivity;
import migong.seoulthings.ui.chat.ChatView;
import migong.seoulthings.ui.main.chats.adapter.ChatRecyclerAdapter;

public class ChatsFragment extends Fragment implements ChatsView {

    private RecyclerView mChatRecyclerView;
    private ChatRecyclerAdapter mChatRecyclerAdapter;

    private ChatsPresenter mPresenter;
    private final CompositeDisposable mCompositeDisposable = new CompositeDisposable();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.chats_fragment, null);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setupChatRecycler(view);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mPresenter = new ChatsPresenter(this);
        mPresenter.onCreate(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mPresenter.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPresenter.onDestroy();
    }

    @Override
    public void startChatActivity(@NonNull String chatId, @NonNull String chatterId) {
        Intent intent = new Intent(getContext(), ChatActivity.class);

        Bundle args = new Bundle();
        args.putString(ChatView.KEY_CHAT_ID, chatId);
        args.putString(ChatView.KEY_CHATTER_ID, chatterId);
        intent.putExtras(args);

        startActivity(intent);
    }

    @Override
    public void clearSnapshots() {
        mChatRecyclerAdapter.clear();
    }

    @Override
    public void addSnapshot(int index, DocumentSnapshot snapshot) {
        mChatRecyclerAdapter.add(index, snapshot);
    }

    @Override
    public void modifySnapshot(int oldIndex, int newIndex, DocumentSnapshot snapshot) {
        mChatRecyclerAdapter.modify(oldIndex, newIndex, snapshot);
    }

    @Override
    public void removeSnapshot(int index) {
        mChatRecyclerAdapter.remove(index);
    }

    private void setupChatRecycler(@NonNull View view) {
        mChatRecyclerView = view.findViewById(R.id.chats_recycler);
        mChatRecyclerView.setHasFixedSize(true);
        mChatRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mChatRecyclerView.addItemDecoration(
                new DividerItemDecoration(mChatRecyclerView.getContext(), LinearLayout.VERTICAL));

        mChatRecyclerAdapter = new ChatRecyclerAdapter(mCompositeDisposable,
                (chatId, chatterId) -> mPresenter.onChatClicked(chatId, chatterId));
        mChatRecyclerView.setAdapter(mChatRecyclerAdapter);
    }
}
