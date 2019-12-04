package migong.seoulthings.api;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface ThingsAPI {

  @GET("/category/{category}/from/{offset}/to/{limit}")
  Observable<ThingsResponse> getThings(
      @Path("category") String category,
      @Path("offset") int offset,
      @Path("limit") int limit
  );
}
