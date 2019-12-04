package migong.seoulthings.data;

import com.google.common.collect.Lists;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.Exclude;

import java.util.List;

public class Chat {

    @Exclude
    private String mFirebaseId;     // DB에 저장할 ID
    private String mLastMessage;    // 메시지
    private List<String> mChatters; // 이름
    private Timestamp mCreateAt;
    private Timestamp mUpdatedAt;

    public Chat() {
        // For FireStore deserialization.
    }

    public Chat(String sender, String receiver, String lastMessage) {
        mChatters = Lists.newArrayList(sender, receiver);
        mLastMessage = lastMessage;
        mCreateAt = Timestamp.now();
        mUpdatedAt = Timestamp.now();
    }

    public String getFirebaseId() {
        return mFirebaseId;
    }

    public void setFirebaseId(String firebaseId) {
        mFirebaseId = firebaseId;
    }

    public List<String> getChatters() {
        return mChatters;
    }

    public void setChatters(List<String> chatters) {
        mChatters = chatters;
    }

    public String getLastMessage() {
        return mLastMessage;
    }

    public void setLastMessage(String lastMessage) {
        mLastMessage = lastMessage;
    }

    public Timestamp getCreateAt() {
        return mCreateAt;
    }

    public void setCreateAt(Timestamp createAt) {
        mCreateAt = createAt;
    }

    public Timestamp getUpdatedAt() {
        return mUpdatedAt;
    }

    public void setUpdatedAt(Timestamp updatedAt) {
        mUpdatedAt = updatedAt;
    }
}
