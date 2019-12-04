package migong.seoulthings.api;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface SearchAPI {

  @GET("/search/things")
  Observable<ThingsResponse> searchThings(@Query("keyword") String keyword);

  @GET("/search/things/{category}")
  Observable<ThingsResponse> searchThings(
      @Path("category") String category,
      @Query("keyword") String keyword
  );
}
