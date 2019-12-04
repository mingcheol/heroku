package migong.seoulthings.api;

import java.util.List;
import migong.seoulthings.data.Thing;

public class ThingsResponse {

  private List<Thing> things;

  public List<Thing> getThings() {
    return things;
  }

  @Override
  public String toString() {
    final StringBuilder sb = new StringBuilder("ThingsResponse{");
    sb.append("things=").append(things);
    sb.append('}');
    return sb.toString();
  }
}
