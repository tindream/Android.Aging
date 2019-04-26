package com.riwise.aging.support;

import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import com.riwise.aging.enums.IListener;
import com.riwise.aging.view.View_About;
import com.riwise.aging.view.View_Ask;
import com.riwise.aging.view.View_Confirm;

public class Method {
    private static Toast toast;

    //随机整数
    public static int round(int max) {
        return new Random().nextInt(max);
    }

    //获取外置SD卡路径
    public static List<String> getExtSDCardPath() {
        List<String> lResult = new ArrayList<>();
        try {
            Process process = Runtime.getRuntime().exec("mount");
            InputStream is = process.getInputStream();
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);
            String line;
            while ((line = br.readLine()) != null) {
                if (line.contains("extSdCard")) {
                    String[] arr = line.split(" ");
                    String path = arr[1];
                    File file = new File(path);
                    if (file.isDirectory()) {
                        lResult.add(path);
                    }
                }
            }
            isr.close();
        } catch (Exception e) {
            log(e);
        }
        return lResult;
    }

    //询问权限
    public static void requestPower(String[] permissions) {
        List list = new ArrayList();
        for (int i = 0; i < permissions.length; i++) {
            //判断是否已经赋予权限
            if (ContextCompat.checkSelfPermission(Config.context, permissions[i]) != PackageManager.PERMISSION_GRANTED) {
                //如果应用之前请求过此权限但用户拒绝了请求，此方法将返回 true。
                if (ActivityCompat.shouldShowRequestPermissionRationale(Config.context, permissions[i])) {
                    //这里可以写个对话框之类的项向用户解释为什么要申请权限，并在对话框的确认键后续再次申请权限
                } else {
                    //申请权限，字符串数组内是一个或多个要申请的权限，1是申请权限结果的返回参数，在onRequestPermissionsResult可以得知申请结果
                    //ActivityCompat.requestPermissions(Config.context, new String[]{permissions[i]}, 1);
                    list.add(permissions[i]);
                }
            }
        }
        if (list.size() > 0)
            ActivityCompat.requestPermissions(Config.context, (String[]) list.toArray(new String[0]), 1);
    }

    public static IListener ask(Activity activity, String msg) {
        View_Ask view_ask = new View_Ask();
        view_ask.init(activity);
        view_ask.show(msg);
        return view_ask;
    }

    public static IListener confirm(Activity activity, String msg) {
        View_Confirm view_confirm = new View_Confirm();
        view_confirm.init(activity);
        view_confirm.show(msg);
        return view_confirm;
    }

    public static void show(Activity activity) {
        View_About view_about = new View_About();
        view_about.init(activity);
        view_about.show();
    }

    public static void show(Activity activity, String msg) {
        View_About view_about = new View_About();
        view_about.init(activity);
        view_about.show(msg);
    }

    public static void loadLanguage() {
        Resources resources = Config.context.getResources();
        Configuration configuration = resources.getConfiguration();
        resources.updateConfiguration(configuration, resources.getDisplayMetrics());
    }

    public static void setSize(View view, int width, double height) {
        ViewGroup.LayoutParams params = view.getLayoutParams();
        if (width != 0) params.width = width;
        if (height != 0) params.height = (int) height;
        view.setLayoutParams(params);
    }

    public static void setMargins(View view, double left, double top, double right, double bottom) {
        if (view.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
            ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
            params.setMargins((int) left, (int) top, (int) right, (int) bottom);
            view.requestLayout();
        }
    }

    public static DisplayMetrics getDisplay(Activity activity) {
        WindowManager wm = (WindowManager) activity.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(dm);

        return dm;
    }

    //检查空字符串
    public static boolean isEmpty(String s) {
        return s == null || s.equals("");
    }

    public static void hit(String msg) {
        hit(Config.context, msg);
    }

    public static void hit(Context context, String msg) {
        if (toast != null) {
            toast.cancel();
        }
        toast = Toast.makeText(context, msg, Toast.LENGTH_SHORT);
        toast.show();
    }

    public static void clear() {
        try {
            File file = new File(Config.file.getPath(), "log.txt");
            file.delete();
            log("clear");
        } catch (Exception e) {
            Log.e(Config.Text, msg(e));
        }
    }

    public static void log(Object msg) {
        Log.e(Config.Text, msg.toString());
        FileWriter writer = null;
        try {
            File file = new File(Config.file.getPath(), "log.txt");
            writer = new FileWriter(file, true);
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            writer.write(format.format(new Date(System.currentTimeMillis())) + ": " + msg + "\r\n");
            writer.flush();
        } catch (Exception e) {
            Log.e(Config.Text, msg(e));
        } finally {
            try {
                if (writer != null) {
                    writer.close();
                }
            } catch (IOException e) {
            }
        }
    }

    public static void log(Object msg, Throwable e) {
        log(msg + msg(e));
    }

    public static void log(Throwable e) {
        log(msg(e));
    }

    private static String msg(Throwable e) {
        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        e.printStackTrace(writer);
        StringBuffer buffer = stringWriter.getBuffer();
        return buffer.toString();
    }
}
