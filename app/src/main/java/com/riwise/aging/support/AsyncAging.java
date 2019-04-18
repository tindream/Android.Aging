package com.riwise.aging.support;

import android.Manifest;
import android.content.ContentProviderOperation;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.CallLog;
import android.provider.ContactsContract;
import android.support.v4.app.ActivityCompat;
import android.telephony.SmsManager;

import io.reactivex.ObservableEmitter;

import com.riwise.aging.enums.LoadType;
import com.riwise.aging.info.loadInfo.LoadInfo;
import com.riwise.aging.info.loadInfo.ProgressInfo;
import com.riwise.aging.info.loadInfo.TestInfo;
import com.riwise.aging.info.sqlInfo.AgingInfo;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class AsyncAging extends AsyncBase {
    private boolean iStop;

    public AsyncAging(AgingInfo info) {
        initData(new TestInfo(info));
    }

    public void stop() {
        this.iStop = true;
    }

    @Override
    protected void onLoadData(ObservableEmitter<LoadInfo> emitter, LoadInfo info) throws Exception {
        AgingInfo aging = ((TestInfo) info).info;
        int progress = 0;
        String name = null;
        File testPath = new File(Config.file.getPath(), "Test");
        if (!testPath.exists()) testPath.mkdirs();
        try {
            {
                name = "碎片化";
                for (int i = 1; i <= 100; i++) {
                    if (iStop) return;
                    Thread.sleep(20);
                    emitter.onNext(new ProgressInfo(name, "不处理", progress + i * 30 / 100, i));
                }
                emitter.onNext(new ProgressInfo(name, "未处理"));
            }
            copyFile(emitter, testPath, name = "图片", Config.Admin.Images, aging.Image, 30);
            copyFile(emitter, testPath, name = "音频", Config.Admin.Audios, aging.Audio, 35);
            copyFile(emitter, testPath, name = "视频", Config.Admin.Videos, aging.Video, 40);
            String phoneFormat = "199%08d";
            {
                name = "联系人";
                String format = "Aging%0" + (aging.Contact + "").length() + "d";
                for (int i = 1; i <= aging.Contact; i++) {
                    if (iStop) return;
                    addContact(String.format(format, i), String.format(phoneFormat, i));
                    progress = 45 + i * 5 / aging.Contact;
                    emitter.onNext(new ProgressInfo(name, i + "/" + aging.Contact, progress, i * 100 / aging.Contact));
                }
                emitter.onNext(new ProgressInfo(name, aging.Contact + ""));
            }
            {
                name = "信息";
                if (!Config.ISMS) {
                    emitter.onNext(new ProgressInfo(name, "未授权", false));
                } else {
                    String format = "测试短信%0" + (aging.Sms + "").length() + "d[Aging]";
                    for (int i = 1; i <= aging.Sms; i++) {
                        if (iStop) return;
                        addSMS(String.format(phoneFormat, i), String.format(format, i));
                        progress = 50 + i * 5 / aging.Sms;
                        emitter.onNext(new ProgressInfo(name, i + "/" + aging.Sms, progress, i * 100 / aging.Sms));
                    }
                    emitter.onNext(new ProgressInfo(name, aging.Sms + ""));
                }
            }
            {
                name = "通话记录";
                for (int i = 1; i <= aging.Call; i++) {
                    if (iStop) return;
                    addCall(String.format(phoneFormat, i));
                    progress = 55 + i * 5 / aging.Call;
                    emitter.onNext(new ProgressInfo(name, i + "/" + aging.Call, progress, i * 100 / aging.Call));
                }
                emitter.onNext(new ProgressInfo(name, aging.Call + ""));
            }
            {
                name = "三方应用";
                String[] apps = Config.Admin.Apps.split(";");
                int count = apps.length;
                if (count > aging.App) count = aging.App;
                for (int i = 1; i < count; i++) {
                    if (iStop) return;
                    installApp(apps[i - 1]);
                    progress = 60 + i * 5 / count;
                    emitter.onNext(new ProgressInfo(name, i + "/" + count, progress, i * 100 / count));
                }
                emitter.onNext(new ProgressInfo(name, count + "/" + aging.App));
            }
            {
                name = "填充文件";
                for (int i = 1; i <= 100; i++) {
                    if (iStop) return;
                    Thread.sleep(1);
                    progress = 65 + i * 35 / 100;
                    emitter.onNext(new ProgressInfo(name, i + "/" + 100, progress, i * 100 / 100));
                }
                emitter.onNext(new ProgressInfo(name, aging.File4 + ":" + aging.File8 + ":" + aging.File128));
            }
            emitter.onNext(new LoadInfo(LoadType.complete));
        } catch (Exception e) {
            emitter.onNext(new ProgressInfo(name, e.getMessage(), false));
            throw e;
        } finally {
            if (iStop) emitter.onNext(new LoadInfo(LoadType.cancel));
        }
    }

    public static boolean installApp(String apkPath) throws Exception {
        Process process = null;
        BufferedReader successResult = null;
        BufferedReader errorResult = null;
        StringBuilder successMsg = new StringBuilder();
        StringBuilder errorMsg = new StringBuilder();
        try {
            process = new ProcessBuilder("pm", "install", "-i", "com.riwise.aging", "-r", apkPath).start();
            successResult = new BufferedReader(new InputStreamReader(process.getInputStream()));
            errorResult = new BufferedReader(new InputStreamReader(process.getErrorStream()));
            String s;
            while ((s = successResult.readLine()) != null) successMsg.append(s);
            while ((s = errorResult.readLine()) != null) errorMsg.append(s);
        } catch (Exception e) {

        } finally {
            if (successResult != null) successResult.close();
            if (errorResult != null) errorResult.close();
            if (process != null) process.destroy();
        }
        Method.hit(errorMsg.toString());
        //如果含有“success”单词则认为安装成功
        return successMsg.toString().equalsIgnoreCase("success");
    }

    /**
     * 插入一条通话记录
     *
     * @param number 通话号码
     *               duration 通话时长（响铃时长）以秒为单位 1分30秒则输入90
     *               type  通话类型  1呼入 2呼出 3未接
     *               isNew 是否已查看    0已看1未看
     */
    private void addCall(String number) {
        ContentValues values = new ContentValues();
        values.put(CallLog.Calls.NUMBER, number);
        values.put(CallLog.Calls.DATE, System.currentTimeMillis());
        values.put(CallLog.Calls.DURATION, 60);
        values.put(CallLog.Calls.TYPE, 2);
        values.put(CallLog.Calls.NEW, 0);
        Config.context.getContentResolver().insert(CallLog.Calls.CONTENT_URI, values);
    }

    private void addSMS(String address, String body) {
        ContentValues values = new ContentValues();
        values.put("address", address);
        values.put("type", 1);
        values.put("date", System.currentTimeMillis());
        values.put("body", body);
        Config.context.getContentResolver().insert(Uri.parse("content://sms/"), values);
    }

    private void addContact(String userName, String phoneNumber) throws Exception {
        ArrayList<ContentProviderOperation> ops = new ArrayList<>();
        ops.add(ContentProviderOperation.newInsert(ContactsContract.RawContacts.CONTENT_URI)
                .withValue(ContactsContract.RawContacts.ACCOUNT_NAME, null)  // 此处传入null添加一个raw_contact空数据
                .build());
        ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)  // RAW_CONTACT_ID是第一个事务添加得到的，因此这里传入0，applyBatch返回的ContentProviderResult[]数组中第一项
                .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE)
                .withValue(ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME, userName)
                .build());
        ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
                .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, phoneNumber)
                .withValue(ContactsContract.CommonDataKinds.Phone.TYPE, ContactsContract.CommonDataKinds.Phone.TYPE_WORK)
                .build());

        Config.context.getContentResolver().applyBatch(ContactsContract.AUTHORITY, ops);
    }

    private void copyFile(ObservableEmitter<LoadInfo> emitter, File testPath, String name, String path, int count, int progress) throws Exception {
        if (Method.isEmpty(path)) {
            emitter.onNext(new ProgressInfo(name, "未设置", false));
            return;
        }
        File file = new File(path);
        if (!file.exists()) {
            emitter.onNext(new ProgressInfo(name, "文件不存在", false));
            return;
        }

        File imagePath = new File(testPath.getPath(), name);
        if (!imagePath.exists()) imagePath.mkdirs();
        String ex = Method.getExtensionName(file);
        BufferedReader reader = new BufferedReader(new FileReader(file));
        String format = "%0" + (count + "").length() + "d." + ex;
        for (int i = 1; i <= count; i++) {
            if (iStop) return;

            File imageFile = new File(imagePath.getPath(), String.format(format, i));
            imageFile.delete();
            FileWriter writer = new FileWriter(imageFile, true);

            reader.mark((int) file.length());
            String content;
            while ((content = reader.readLine()) != null) {
                writer.write(content);
            }
            writer.close();
            reader.reset();

            emitter.onNext(new ProgressInfo(name, i + "/" + count, progress + i * 5 / count, i * 100 / count));
        }
        reader.close();
        emitter.onNext(new ProgressInfo(name, count + ""));
    }
}
