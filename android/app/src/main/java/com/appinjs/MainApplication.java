package com.appinjs;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.media.AudioAttributes;
import android.net.Uri;
import android.os.Build;

import androidx.annotation.RequiresApi;

import com.facebook.react.PackageList;
import com.facebook.react.ReactApplication;
import com.facebook.react.ReactInstanceManager;
import com.facebook.react.ReactNativeHost;
import com.facebook.react.ReactPackage;
import com.facebook.react.config.ReactFeatureFlags;
import com.facebook.soloader.SoLoader;
import com.appinjs.newarchitecture.MainApplicationReactNativeHost;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

import com.moengage.core.LogLevel;
import com.moengage.core.config.LogConfig;
import com.moengage.core.config.NotificationConfig;
import com.moengage.plugin.base.push.PluginPushCallback;
import com.moengage.pushbase.MoEPushHelper;
import com.moengage.react.MoEReactPackage;
import com.moengage.core.MoEngage;
import com.moengage.react.MoEInitializer;
import com.moengage.react.inbox.MoengageInboxPackage;


public class MainApplication extends Application implements ReactApplication {

  private final ReactNativeHost mReactNativeHost =
      new ReactNativeHost(this) {
        @Override
        public boolean getUseDeveloperSupport() {
          return BuildConfig.DEBUG;
        }

        @Override
        protected List<ReactPackage> getPackages() {
          @SuppressWarnings("UnnecessaryLocalVariable")
          List<ReactPackage> packages = new PackageList(this).getPackages();
          // Packages that cannot be autolinked yet can be added manually here, for example:
          // packages.add(new MyReactNativePackage());
//          packages.add(new MoengageInboxPackage());
          return packages;
        }

        @Override
        protected String getJSMainModuleName() {
          return "index";
        }
      };

  private final ReactNativeHost mNewArchitectureNativeHost =
      new MainApplicationReactNativeHost(this);

  @Override
  public ReactNativeHost getReactNativeHost() {
    if (BuildConfig.IS_NEW_ARCHITECTURE_ENABLED) {
      return mNewArchitectureNativeHost;
    } else {
      return mReactNativeHost;
    }
  }

  @Override
  @RequiresApi(api = Build.VERSION_CODES.O)
  public void onCreate() {
    super.onCreate();
    // If you opted-in for the New Architecture, we enable the TurboModule system
    ReactFeatureFlags.useTurboModules = BuildConfig.IS_NEW_ARCHITECTURE_ENABLED;
    SoLoader.init(this, /* native exopackage */ false);
    initializeFlipper(this, getReactNativeHost().getReactInstanceManager());

    MoEngage.Builder moengage = new MoEngage.Builder(this, "8SIW681S80Z08KSHQFSTIZ8T");
    moengage.configureNotificationMetaData(new NotificationConfig(R.mipmap.ic_launcher_round, R.mipmap.ic_launcher_round));
    moengage.configureLogs(new LogConfig(LogLevel.VERBOSE, true));
    MoEInitializer.INSTANCE.initializeDefaultInstance(getApplicationContext(), moengage);

    MoEPushHelper.getInstance().registerMessageListener(new MyPluginPushCallback());

    createCustomNotificationChannel("SoundReal");
    // MoEInitializer.INSTANCE.initializeDefaultInstance(getApplicationContext(), moengage);
  }

  @RequiresApi(api = Build.VERSION_CODES.O)
  void createCustomNotificationChannel(String channelName){
    NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
    NotificationChannel mySoundChannel = null;
    mySoundChannel = manager.getNotificationChannel(channelName);

    if(mySoundChannel == null)
    {
      mySoundChannel = new NotificationChannel(channelName, channelName, NotificationManager.IMPORTANCE_HIGH);

      Uri soundUri = Uri.parse("android.resource://" + MainApplication.this.getPackageName() + "/" + R.raw.knock_knock);

      if(soundUri != null) {
        AudioAttributes attrib = new AudioAttributes.Builder()
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .setUsage(AudioAttributes.USAGE_ALARM)
                .build();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
          mySoundChannel.setSound(soundUri, attrib);
        }

        manager.createNotificationChannel(mySoundChannel);
      }

    }
  }

  /**
   * Loads Flipper in React Native templates. Call this in the onCreate method with something like
   * initializeFlipper(this, getReactNativeHost().getReactInstanceManager());
   *
   * @param context
   * @param reactInstanceManager
   */
  private static void initializeFlipper(
      Context context, ReactInstanceManager reactInstanceManager) {
    if (BuildConfig.DEBUG) {
      try {
        /*
         We use reflection here to pick up the class that initializes Flipper,
        since Flipper library is not available in release mode
        */
        Class<?> aClass = Class.forName("com.appinjs.ReactNativeFlipper");
        aClass
            .getMethod("initializeFlipper", Context.class, ReactInstanceManager.class)
            .invoke(null, context, reactInstanceManager);
      } catch (ClassNotFoundException e) {
        e.printStackTrace();
      } catch (NoSuchMethodException e) {
        e.printStackTrace();
      } catch (IllegalAccessException e) {
        e.printStackTrace();
      } catch (InvocationTargetException e) {
        e.printStackTrace();
      }
    }
  }
}
