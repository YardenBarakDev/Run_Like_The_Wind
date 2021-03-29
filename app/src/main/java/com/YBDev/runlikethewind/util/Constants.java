package com.YBDev.runlikethewind.util;

public class Constants {

      public interface KEYS{
            //permission
            int REQUEST_CODE_LOCATION_PERMISSION = 0;


            //service
            String ACTION_START_OR_RESUME_SERVICE = "ACTION_START_OR_RESUME_SERVICE";
            String ACTION_PAUSE_SERVICE = "ACTION_PAUSE_SERVICE";
            String ACTION_STOP_SERVICE = "ACTION_STOP_SERVICE";
            String ACTION_SHOW_TRACKING_FRAGMENT = "ACTION_SHOW_TRACKING_FRAGMENT";


            String NOTIFICATION_APP_NAME = "Run Like The Wind";
            String NOTIFICATION_CHANNEL_ID = "YBDev_tracking_run_app";
            String NOTIFICATION_CHANNEL_NAME = "Tracking";
            int NOTIFICATION_ID = 1;

            long LOCATION_UPDATE_INTERVAL = 5000L;
            long FASTEST_LOCATION_INTERVAL = 2000L;


            //MAP
            float POLYLINE_WIDTH = 8f;
            float MAP_ZOOM = 15f;

            //timer
            long TIMER_UPDATE_INTERVAL = 50L;

            //SP
            String FULL_NAME = "FULL_NAME";
            String USER_WEIGHT = "USER_WEIGHT";

      }
}
