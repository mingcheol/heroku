package migong.seoulthings.api;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface ThingAPI {

  @GET("/thing/{thingId}")
  Observable<ThingResponse> getThing(@Path("thingId") String thingId);
}
