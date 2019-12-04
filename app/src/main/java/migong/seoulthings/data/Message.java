package migong.seoulthings.data;

import com.google.firebase.Timestamp;

public class Message {

  private String mChatId;
  private String mSenderId;
  private String mMessage;
  private Timestamp mCreatedAt;

  public Message() {
    // For FireStore deserialization.
  }

  public Message(String chatId, String senderId, String message) {
    mChatId = chatId;
    mSenderId = senderId;
    mMessage = message;
    mCreatedAt = Timestamp.now();
  }

  public String getChatId() {
    return mChatId;
  }

  public void setChatId(String chatId) {
    mChatId = chatId;
  }

  public String getSenderId() {
    return mSenderId;
  }

  public void setSenderId(String senderId) {
    mSenderId = senderId;
  }

  public String getMessage() {
    return mMessage;
  }

  public void setMessage(String message) {
    mMessage = message;
  }

  public Timestamp getCreatedAt() {
    return mCreatedAt;
  }

  public void setCreatedAt(Timestamp createdAt) {
    mCreatedAt = createdAt;
  }

  @Override
  public String toString() {
    final StringBuilder sb = new StringBuilder("Message{");
    sb.append("chatId='").append(mChatId).append('\'');
    sb.append(", senderId='").append(mSenderId).append('\'');
    sb.append(", message='").append(mMessage).append('\'');
    sb.append(", createdAt=").append(mCreatedAt);
    sb.append('}');
    return sb.toString();
  }
}
