package com.riwise.aging.support;

import android.Manifest;
import android.content.ContentProviderOperation;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Environment;
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

import java.io.BufferedInputStream;
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
        String name = null;
        File testPath = new File(Config.file.getPath(), "Test");
        if (!testPath.exists()) testPath.mkdirs();
        try {
            execFile(emitter, name = "碎片化", testPath, aging);
            if (!iStop) execStart(emitter, name = "碎片化", testPath, aging.File);
            if (!iStop) copyFile(emitter, name = "图片", testPath, Config.Admin.Images, aging.Image);
            if (!iStop) copyFile(emitter, name = "音频", testPath, Config.Admin.Audios, aging.Audio);
            if (!iStop) copyFile(emitter, name = "视频", testPath, Config.Admin.Videos, aging.Video);
            String phoneFormat = "199%08d";
            if (!iStop) execContact(emitter, name = "联系人", phoneFormat, aging.Contact);
            if (!iStop) execSms(emitter, name = "信息", phoneFormat, aging.Sms);
            if (!iStop) execCall(emitter, name = "通话记录", phoneFormat, aging.Call);
            if (!iStop) execApp(emitter, name = "三方应用", aging.App);
            if (!iStop) execFileBig(emitter, name = "填充大文件", testPath, aging.Last);
            if (!iStop) emitter.onNext(new LoadInfo(LoadType.complete));
        } catch (Exception e) {
            emitter.onNext(new ProgressInfo(name, e.getMessage(), false));
            throw e;
        } finally {
            if (iStop) emitter.onNext(new LoadInfo(LoadType.cancel));
        }
    }

    private void execFileBig(ObservableEmitter<LoadInfo> emitter, String name, File testPath, double last) throws Exception {
        File sdcard = Environment.getExternalStorageDirectory();//得到sdcard的目录作为一个文件对象
        long usable = sdcard.getUsableSpace();//获取文件目录对象剩余空间
        double space = usable - last * 1024 * 1024 * 1024;//填充空间
        File file = new File(testPath.getPath(), "big.txt");
        FileOutputStream output = new FileOutputStream(file);
        byte[] buffer = new byte[4096];
        for (int i = 0; i < buffer.length; i++)
            buffer[i] = (byte) Method.round(128);
        int count = (int) (space / 4096);
        try {
            for (int index = 0; index < count; index++) {
                output.write(buffer, 0, 4096);
                if (index % 10 == 0) {
                    if (iStop) return;
                    double value = index / 256.0;
                    String desc = String.format("%.1fM", value);
                    if (value > 1024) {
                        value /= 1024;
                        desc = String.format("%.2fG", value);
                    }
                    emitter.onNext(new ProgressInfo(name, desc, 100 * index / count));
                }
            }
            output.flush();
        } finally {
            output.close();
        }
        emitter.onNext(new ProgressInfo(name, "完成"));
    }

    private void execStart(ObservableEmitter<LoadInfo> emitter, String name, File testPath, String files) {
        File path = new File(testPath.getPath(), name);
        if (!path.exists()) path.mkdirs();
        int count = 0;
        File[] allFiles = path.listFiles();
        for (int i = 0; i < allFiles.length; i++) {
            File file = allFiles[i];
            String end = file.getName();
            end = end.substring(end.length() - 1);
            if (files.contains(end)) {
                file.delete();
                count++;
                if (count % 10 == 0) {
                    if (iStop) return;
                    emitter.onNext(new ProgressInfo(name, count + "(" + files + ")", 100 * i / allFiles.length));
                }
            }
        }
        emitter.onNext(new ProgressInfo(name, "完成"));
    }

    private void execFile(ObservableEmitter<LoadInfo> emitter, String name, File testPath, AgingInfo aging) throws Exception {
        File path = new File(testPath.getPath(), name);
        if (!path.exists()) path.mkdirs();
        emitter.onNext(new ProgressInfo(name, "删除", 0));
        File big = new File(testPath.getPath(), "big.txt");
        big.delete();
        double last = aging.Last * 1024 * 1024 * 1024;//填满,预留与剩余空间一致
        if (!Method.isEmpty(Config.Admin.File4s)) {
            try {
                File file = new File(Config.Admin.File4s);
                FileInputStream reader = new FileInputStream(Config.Admin.File4s);
                Config.Admin.File4sf = new byte[(int) file.length()];
                reader.read(Config.Admin.File4sf);
                reader.close();
            } catch (Exception e) {
            }
        } else Config.Admin.File4sf = null;
        if (!Method.isEmpty(Config.Admin.File8s)) {
            try {
                File file = new File(Config.Admin.File8s);
                FileInputStream reader = new FileInputStream(Config.Admin.File8s);
                Config.Admin.File8sf = new byte[(int) file.length()];
                reader.read(Config.Admin.File4sf);
                reader.close();
            } catch (Exception e) {
            }
        } else Config.Admin.File8sf = null;
        if (!Method.isEmpty(Config.Admin.File128s)) {
            try {
                File file = new File(Config.Admin.File128s);
                FileInputStream reader = new FileInputStream(Config.Admin.File128s);
                Config.Admin.File128sf = new byte[(int) file.length()];
                reader.read(Config.Admin.File4sf);
                reader.close();
            } catch (Exception e) {
            }
        } else Config.Admin.File128sf = null;
        File sdcard = Environment.getExternalStorageDirectory();//得到sdcard的目录作为一个文件对象
        long usable = sdcard.getUsableSpace();//获取文件目录对象剩余空间
        double space = usable - last;//填充空间
        if (space > 0) {
            int index = 0;
            long onceSpace = 0;
            if (!Method.isEmpty(Config.Admin.File4s)) {
                File file = new File(Config.Admin.File4s);
                long length = (int) file.length() / 4096;
                if (file.length() % 4096 > 0) length += 1;
                onceSpace += length * 4096 * aging.File4;
            }
            if (!Method.isEmpty(Config.Admin.File8s)) {
                File file = new File(Config.Admin.File8s);
                long length = (int) file.length() / 4096;
                if (file.length() % 4096 > 0) length += 1;
                onceSpace += length * 4096 * aging.File8;
            }
            if (!Method.isEmpty(Config.Admin.File128s)) {
                File file = new File(Config.Admin.File128s);
                long length = (int) file.length() / 4096;
                if (file.length() % 4096 > 0) length += 1;
                onceSpace += length * 4096 * aging.File128;
            }
            if (onceSpace == 0) {
                emitter.onNext(new ProgressInfo(name, "未设置", false));
            } else {
                String target = String.format("%.1fG", space / 1024 / 1024 / 1024);
                int count = (int) (space / onceSpace);
                for (int i = 0; i < count; i++) {
                    if (iStop) return;
                    if (Config.Admin.File4sf != null)
                        index += copyFile(Config.Admin.File4sf, path.getPath(), aging.File4, index);
                    if (Config.Admin.File8sf != null)
                        index += copyFile(Config.Admin.File8sf, path.getPath(), aging.File8, index);
                    if (Config.Admin.File128sf != null)
                        index += copyFile(Config.Admin.File128sf, path.getPath(), aging.File128, index);
                    double value = onceSpace * i / 1024.0;
                    String desc = value + "K";
                    if (value > 1024) {
                        value /= 1024;
                        desc = String.format("%.1fM", value);
                    }
                    if (value > 1024) {
                        value /= 1024;
                        desc = String.format("%.2fG", value);
                    }
                    emitter.onNext(new ProgressInfo(name, index + "(" + desc + "/" + target + ")", i * 100 / count));
                }
            }
        }
        emitter.onNext(new ProgressInfo(name, "填充完成", 100));
    }

    private int copyFile(byte[] buffer, String path, int count, int index) throws Exception {
        for (int i = 1; i <= count; i++) {
            copyFile(buffer, path, "%09d", index + i);
        }
        return count;
    }

    private void copyFile(byte[] buffer, String path, String format, int index) throws Exception {
        File imageFile = new File(path, String.format(format, index));
        FileOutputStream output = new FileOutputStream(imageFile);
        output.write(buffer, 0, buffer.length);
        output.flush();
        output.close();
    }

    private void execApp(ObservableEmitter<LoadInfo> emitter, String name, int count) throws Exception {
        if (Method.isEmpty(Config.Admin.Apps)) {
            emitter.onNext(new ProgressInfo(name, "未设置", false));
            return;
        }
        String[] apps = Config.Admin.Apps.split(";");
        int length = apps.length;
        if (length > count) length = count;
        for (int i = 1; i < length; i++) {
            if (iStop) return;
            addApp(apps[i - 1]);
            emitter.onNext(new ProgressInfo(name, i + "/" + length, 100 * i / length));
        }
        emitter.onNext(new ProgressInfo(name, length + "/" + count));
    }

    private boolean addApp(String apkPath) throws Exception {
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
            errorMsg.append(e.getMessage());
        } finally {
            if (successResult != null) successResult.close();
            if (errorResult != null) errorResult.close();
            if (process != null) process.destroy();
        }
        Method.hit(errorMsg.toString());
        //如果含有“success”单词则认为安装成功
        return successMsg.toString().equalsIgnoreCase("success");
    }

    private void execCall(ObservableEmitter<LoadInfo> emitter, String name, String phoneFormat, int count) {
        for (int i = 1; i <= count; i++) {
            if (iStop) return;
            addCall(String.format(phoneFormat, i));
            emitter.onNext(new ProgressInfo(name, i + "/" + count, 100 * i / count));
        }
        emitter.onNext(new ProgressInfo(name, count + ""));
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

    private void execSms(ObservableEmitter<LoadInfo> emitter, String name, String phoneFormat, int count) {
        if (!Config.ISMS) {
            emitter.onNext(new ProgressInfo(name, "未授权", false));
            return;
        }
        String format = "测试短信%0" + (count + "").length() + "d[Aging]";
        for (int i = 1; i <= count; i++) {
            if (iStop) return;
            addSms(String.format(phoneFormat, i), String.format(format, i));
            emitter.onNext(new ProgressInfo(name, i + "/" + count, 100 * i / count));
        }
        emitter.onNext(new ProgressInfo(name, count + ""));
    }

    private void addSms(String address, String body) {
        ContentValues values = new ContentValues();
        values.put("address", address);
        values.put("type", 1);
        values.put("date", System.currentTimeMillis());
        values.put("body", body);
        Config.context.getContentResolver().insert(Uri.parse("content://sms/"), values);
    }

    private void execContact(ObservableEmitter<LoadInfo> emitter, String name, String phoneFormat, int count) throws Exception {
        String format = "Aging%0" + (count + "").length() + "d";
        for (int i = 1; i <= count; i++) {
            if (iStop) return;
            addContact(String.format(format, i), String.format(phoneFormat, i));
            emitter.onNext(new ProgressInfo(name, i + "/" + count, 100 * i / count));
        }
        emitter.onNext(new ProgressInfo(name, count + ""));
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

    private void copyFile(ObservableEmitter<LoadInfo> emitter, String name, File testPath, String path, int count) throws Exception {
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
        BufferedInputStream reader = new BufferedInputStream(new FileInputStream(file));
        String format = "%0" + (count + "").length() + "d";
        for (int i = 1; i <= count; i++) {
            if (iStop) return;

            reader.mark((int) file.length());
            copyFile(reader, imagePath.getPath(), format, i);
            emitter.onNext(new ProgressInfo(name, i + "/" + count, 100 * i / count));
        }
        reader.close();
        emitter.onNext(new ProgressInfo(name, count + ""));
    }

    private void copyFile(BufferedInputStream reader, String path, String format, int index) throws Exception {
        File imageFile = new File(path, String.format(format, index));
        FileOutputStream output = new FileOutputStream(imageFile);
        byte[] bytes = new byte[4096];
        int read;
        while ((read = reader.read(bytes)) != -1) {
            output.write(bytes, 0, read);
        }
        output.flush();
        output.close();
        reader.reset();
    }
}
