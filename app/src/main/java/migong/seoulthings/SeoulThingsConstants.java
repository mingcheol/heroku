package migong.seoulthings;

public final class SeoulThingsConstants {

  public static final int SPLASH_TIME_IN_MILLIS = 500;

  public static final int DISPLAY_NAME_MIN_LENGTH = 2;
  public static final int DISPLAY_NAME_MAX_LENGTH = 8;
  public static final int PASSWORD_MIN_LENGTH = 4; // TODO(@gihwan): 8로 설정. 현재 테스트용
  public static final int PASSWORD_MAX_LENGTH = 24;

  public static final String SEOULTHINGS_SERVER_BASE_URL = "https://seoulthings.herokuapp.com";

  public static final int THUMBNAIL_SIZE = 256;
  public static final int PROFILE_PHOTO_SIZE = 512;

  private SeoulThingsConstants() {
    // nothing
  }
}
