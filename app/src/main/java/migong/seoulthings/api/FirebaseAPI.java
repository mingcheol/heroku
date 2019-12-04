package migong.seoulthings.api;

import io.reactivex.Observable;
import migong.seoulthings.data.User;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface FirebaseAPI {

  @GET("/firebase/authentication/user/{uid}")
  Observable<User> getFirebaseUser(@Path("uid") String uid);
}
