package com.riwise.aging.support;

import io.reactivex.ObservableEmitter;

import com.riwise.aging.enums.LoadType;
import com.riwise.aging.info.loadInfo.LoadInfo;
import com.riwise.aging.info.loadInfo.ProgressInfo;
import com.riwise.aging.info.loadInfo.TestInfo;
import com.riwise.aging.info.sqlInfo.AgingInfo;

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
        try {
            {
                for (; progress <= 5; progress++) {
                    if (iStop) return;
                    Thread.sleep(30);
                    emitter.onNext(new ProgressInfo("碎片化", "处理中", progress));
                }
                emitter.onNext(new ProgressInfo("碎片化", null));
            }
            {
                for (int i = 1; i <= aging.Image; i++) {
                    if (iStop) return;
                    Thread.sleep(5);
                    progress = 5 + i * 5 / aging.Image;
                    emitter.onNext(new ProgressInfo("图片", i + "/" + aging.Image, progress));
                }
                emitter.onNext(new ProgressInfo("图片", aging.Image + ""));
            }
            {
                for (int i = 1; i <= aging.Audio; i++) {
                    if (iStop) return;
                    Thread.sleep(5);
                    progress = 10 + i * 5 / aging.Audio;
                    emitter.onNext(new ProgressInfo("音频", i + "/" + aging.Audio, progress));
                }
                emitter.onNext(new ProgressInfo("音频", aging.Audio + ""));
            }
            {
                for (int i = 1; i <= aging.Video; i++) {
                    if (iStop) return;
                    Thread.sleep(5);
                    progress = 15 + i * 5 / aging.Video;
                    emitter.onNext(new ProgressInfo("视频", i + "/" + aging.Video, progress));
                }
                emitter.onNext(new ProgressInfo("视频", aging.Video + ""));
            }
            {
                for (int i = 1; i <= aging.Contact; i++) {
                    if (iStop) return;
                    Thread.sleep(1);
                    progress = 20 + i * 5 / aging.Contact;
                    emitter.onNext(new ProgressInfo("联系人", i + "/" + aging.Contact, progress));
                }
                emitter.onNext(new ProgressInfo("联系人", aging.Contact + ""));
            }
            {
                for (int i = 1; i <= aging.Sms; i++) {
                    if (iStop) return;
                    Thread.sleep(1);
                    progress = 25 + i * 5 / aging.Sms;
                    emitter.onNext(new ProgressInfo("信息", i + "/" + aging.Sms, progress));
                }
                emitter.onNext(new ProgressInfo("信息", aging.Sms + ""));
            }
            {
                for (int i = 1; i <= aging.Call; i++) {
                    if (iStop) return;
                    Thread.sleep(1);
                    progress = 30 + i * 5 / aging.Call;
                    emitter.onNext(new ProgressInfo("通话记录", i + "/" + aging.Call, progress));
                }
                emitter.onNext(new ProgressInfo("通话记录", aging.Call + ""));
            }
            {
                for (int i = 1; i <= aging.App; i++) {
                    if (iStop) return;
                    Thread.sleep(1);
                    progress = 35 + i * 5 / aging.App;
                    emitter.onNext(new ProgressInfo("三方应用", i + "/" + aging.App, progress));
                }
                emitter.onNext(new ProgressInfo("三方应用", aging.App + ""));
            }
            {
                for (int i = 1; i <= aging.Video; i++) {
                    if (iStop) return;
                    Thread.sleep(1);
                    progress = 15 + i * 5 / aging.Video;
                    emitter.onNext(new ProgressInfo("填充文件", i + "/" + aging.Video, progress));
                }
                emitter.onNext(new ProgressInfo("填充文件", aging.File4 + ":" + aging.File8 + ":" + aging.File128));
            }
        } finally {
            emitter.onNext(new LoadInfo(iStop ? LoadType.cancel : LoadType.complete));
        }
    }
}
