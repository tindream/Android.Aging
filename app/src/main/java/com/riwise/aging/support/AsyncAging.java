package com.riwise.aging.support;

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

public class AsyncAging extends AsyncBase {
    private boolean iStop;

    public AsyncAging(AgingInfo info) {
        initData(new TestInfo(info));
    }

    public void stop() {
        this.iStop = true;
    }

    private void copyFile(ObservableEmitter<LoadInfo> emitter, File testPath, String name, String path, int count, int progress) throws Exception {
        try {
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
            for (int i = 1; i <= count; i++) {
                if (iStop) return;
                File imageFile = new File(imagePath.getPath(), i + "." + ex);
                imageFile.delete();
                FileWriter writer = new FileWriter(imageFile, true);

                reader.mark((int) file.length());
                String content;
                while ((content = reader.readLine()) != null) {
                    writer.write(content);
                }
                writer.close();
                reader.reset();

                emitter.onNext(new ProgressInfo(name, i + "/" + count, progress + i * 5 / count));
            }
            reader.close();
            emitter.onNext(new ProgressInfo(name, count + "", true));
        } catch (Exception e) {
            emitter.onNext(new ProgressInfo(name, e.getMessage(), false));
            throw e;
        }
    }

    @Override
    protected void onLoadData(ObservableEmitter<LoadInfo> emitter, LoadInfo info) throws Exception {
        AgingInfo aging = ((TestInfo) info).info;
        int progress = 0;
        File testPath = new File(Config.file.getPath(), "Test");
        if (!testPath.exists()) testPath.mkdirs();
        try {
            {
                for (; progress <= 5; progress++) {
                    if (iStop) return;
                    Thread.sleep(30);
                    emitter.onNext(new ProgressInfo("碎片化", "处理中", progress));
                }
                emitter.onNext(new ProgressInfo("碎片化", null, true));
            }
            copyFile(emitter, testPath, "图片", Config.Admin.Images, aging.Image, 5);
            copyFile(emitter, testPath, "音频", Config.Admin.Audios, aging.Audio, 10);
            copyFile(emitter, testPath, "视频", Config.Admin.Videos, aging.Video, 15);
            {
                for (int i = 1; i <= aging.Contact; i++) {
                    if (iStop) return;
                    Thread.sleep(1);
                    progress = 20 + i * 5 / aging.Contact;
                    emitter.onNext(new ProgressInfo("联系人", i + "/" + aging.Contact, progress));
                }
                emitter.onNext(new ProgressInfo("联系人", aging.Contact + "", true));
            }
            {
                for (int i = 1; i <= aging.Sms; i++) {
                    if (iStop) return;
                    Thread.sleep(1);
                    progress = 25 + i * 5 / aging.Sms;
                    emitter.onNext(new ProgressInfo("信息", i + "/" + aging.Sms, progress));
                }
                emitter.onNext(new ProgressInfo("信息", aging.Sms + "", true));
            }
            {
                for (int i = 1; i <= aging.Call; i++) {
                    if (iStop) return;
                    Thread.sleep(1);
                    progress = 30 + i * 5 / aging.Call;
                    emitter.onNext(new ProgressInfo("通话记录", i + "/" + aging.Call, progress));
                }
                emitter.onNext(new ProgressInfo("通话记录", aging.Call + "", true));
            }
            {
                for (int i = 1; i <= aging.App; i++) {
                    if (iStop) return;
                    Thread.sleep(1);
                    progress = 35 + i * 5 / aging.App;
                    emitter.onNext(new ProgressInfo("三方应用", i + "/" + aging.App, progress));
                }
                emitter.onNext(new ProgressInfo("三方应用", aging.App + "", true));
            }
            {
                for (int i = 1; i <= aging.Video; i++) {
                    if (iStop) return;
                    Thread.sleep(1);
                    progress = 15 + i * 5 / aging.Video;
                    emitter.onNext(new ProgressInfo("填充文件", i + "/" + aging.Video, progress));
                }
                emitter.onNext(new ProgressInfo("填充文件", aging.File4 + ":" + aging.File8 + ":" + aging.File128, true));
            }
        } finally {
            emitter.onNext(new LoadInfo(iStop ? LoadType.cancel : LoadType.complete));
        }
    }
}
