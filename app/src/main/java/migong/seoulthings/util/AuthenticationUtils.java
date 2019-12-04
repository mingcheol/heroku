package migong.seoulthings.util;

import android.support.annotation.NonNull;
import android.util.Patterns;
import migong.seoulthings.SeoulThingsConstants;
import org.apache.commons.lang3.StringUtils;

public class AuthenticationUtils {

  public static boolean isValidDisplayName(@NonNull String displayName) {
    if (StringUtils.isEmpty(displayName)) {
      return false;
    } else {
      return displayName.length() >= SeoulThingsConstants.DISPLAY_NAME_MIN_LENGTH &&
          displayName.length() <= SeoulThingsConstants.DISPLAY_NAME_MAX_LENGTH;
    }
  }

  public static boolean isValidEmailAddress(@NonNull String email) {
    if (StringUtils.isEmpty(email)) {
      return false;
    } else {
      return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
  }

  public static boolean isValidPassword(@NonNull String password) {
    if (StringUtils.isEmpty(password)) {
      return false;
    } else {
      return password.length() >= SeoulThingsConstants.PASSWORD_MIN_LENGTH &&
          password.length() <= SeoulThingsConstants.PASSWORD_MAX_LENGTH;
    }
  }

  private AuthenticationUtils() {
    // nothing
  }
}
