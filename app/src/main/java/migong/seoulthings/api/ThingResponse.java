package migong.seoulthings.api;

import migong.seoulthings.data.Thing;

public class ThingResponse {

  private int size;
  private Thing thing;

  public int getSize() {
    return size;
  }

  public Thing getThing() {
    return thing;
  }

  @Override
  public String toString() {
    final StringBuilder sb = new StringBuilder("ThingResponse{");
    sb.append("size=").append(size);
    sb.append(", thing=").append(thing);
    sb.append('}');
    return sb.toString();
  }
}
