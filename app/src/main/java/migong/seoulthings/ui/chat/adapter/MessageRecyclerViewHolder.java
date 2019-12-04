package migong.seoulthings.ui.chat.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import migong.seoulthings.R;

public class MessageRecyclerViewHolder extends RecyclerView.ViewHolder {

  private TextView mMessageText;

  public MessageRecyclerViewHolder(@NonNull View itemView) {
    super(itemView);
    mMessageText = itemView.findViewById(R.id.message_listitem_message);
  }

  public void setMessage(String message) {
    mMessageText.setText(message);
  }
}
