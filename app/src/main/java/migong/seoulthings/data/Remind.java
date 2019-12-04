package migong.seoulthings.data;

import com.google.firebase.Timestamp;

public class Remind {

  private String mAuthorId;
  private String mThingId;
  private Timestamp mDue;

  public Remind() {

  }

  public Remind(String authorId, String thingId, Timestamp due) {
    mAuthorId = authorId;
    mThingId = thingId;
    mDue = due;
  }

  public String getAuthorId() {
    return mAuthorId;
  }

  public void setAuthorId(String authorId) {
    mAuthorId = authorId;
  }

  public String getThingId() {
    return mThingId;
  }

  public void setThingId(String thingId) {
    mThingId = thingId;
  }

  public Timestamp getDue() {
    return mDue;
  }

  public void setDue(Timestamp due) {
    mDue = due;
  }
}
