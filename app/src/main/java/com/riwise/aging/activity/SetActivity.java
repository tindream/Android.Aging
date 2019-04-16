package com.riwise.aging.activity;


import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.leon.lfilepickerlibrary.LFilePicker;
import com.riwise.aging.R;
import com.riwise.aging.data.SQLiteServer;
import com.riwise.aging.enums.FileType;
import com.riwise.aging.enums.IListListener;
import com.riwise.aging.enums.ILoadListener;
import com.riwise.aging.enums.LoadType;
import com.riwise.aging.info.loadInfo.LoadInfo;
import com.riwise.aging.info.loadInfo.SetInfo;
import com.riwise.aging.info.setInfo.AdapterInfo;
import com.riwise.aging.info.setInfo.LoaderInfo;
import com.riwise.aging.support.AsyncListView;
import com.riwise.aging.support.Config;
import com.riwise.aging.support.Method;
import com.riwise.aging.support.MyAdapter;
import com.riwise.aging.support.ViewHolder;
import com.riwise.aging.ui.ChildActivity;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.ObservableEmitter;

public class SetActivity extends ChildActivity implements IListListener, ILoadListener {
    int REQUESTCODE_FROM_ACTIVITY = 1000;
    String sdPath;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.layoutResID = R.layout.activity_set;
        super.onCreate(savedInstanceState);
        List<String> extPaths = Method.getExtSDCardPath();
        if (extPaths.size() > 0) sdPath = extPaths.get(0);
        else {
            Method.hit("外置SD卡不存在, 将使用内置SD卡");
            sdPath = Environment.getExternalStorageDirectory().getPath();
        }

        List<SetInfo> list = new ArrayList();
        list.add(new SetInfo());
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            //判断sdcard存储空间是否满足文件的存储
            File sdcard = Environment.getExternalStorageDirectory();//得到sdcard的目录作为一个文件对象
            long usableSpace = sdcard.getUsableSpace();//获取文件目录对象剩余空间
            long totalSpace = sdcard.getTotalSpace();
            boolean i32 = totalSpace > 1024 * 1024 * 1024 * 32;
            list.add(new SetInfo("内置SD卡 " + (i32 ? ">" : "=") + " 32G", true));
            list.add(new SetInfo());
        } else {
            list.add(new SetInfo("SD卡未挂载"));
        }

        list.add(new SetInfo(FileType.Images, Config.Admin.Images));
        list.add(new SetInfo(FileType.Audios, Config.Admin.Audios));
        list.add(new SetInfo(FileType.Videos, Config.Admin.Videos));
        list.add(new SetInfo(FileType.Contacts, Config.Admin.Contacts));
        list.add(new SetInfo(FileType.Smss, Config.Admin.Smss));
        list.add(new SetInfo(FileType.Calls, Config.Admin.Calls));
        list.add(new SetInfo(FileType.Apps, Config.Admin.Apps));
        list.add(new SetInfo(FileType.File4s, Config.Admin.File4s));
        list.add(new SetInfo(FileType.File8s, Config.Admin.File8s));
        list.add(new SetInfo(FileType.File128s, Config.Admin.File128s));

        list.add(new SetInfo());
        list.add(new SetInfo(getString(R.string.btn_10)));
        list.add(new SetInfo(getString(R.string.btn_18)));
        list.add(new SetInfo(getString(R.string.btn_24)));
        list.add(new SetInfo());
        list.add(new SetInfo(getString(R.string.btn_log)));
        list.add(new SetInfo(getString(R.string.btn_about), getString(R.string.version), true));
        new AsyncListView().setListener(this, this).init(this, list, R.layout.item_set);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            List<String> list = data.getStringArrayListExtra("paths");
            String path = list.size() > 0 ? list.get(0) : null;
            int code = requestCode - REQUESTCODE_FROM_ACTIVITY;
            String name = null;
            switch (code) {
                case FileType.Image:
                    name = FileType.Images;
                    Config.Admin.Images = path;
                    new SQLiteServer().updateAdmin("Images", Config.Admin.Images);
                    break;
                case FileType.Audio:
                    name = FileType.Audios;
                    Config.Admin.Audios = path;
                    new SQLiteServer().updateAdmin("Audios", Config.Admin.Audios);
                    break;
                case FileType.Video:
                    name = FileType.Videos;
                    Config.Admin.Videos = path;
                    new SQLiteServer().updateAdmin("Videos", Config.Admin.Videos);
                    break;
                case FileType.Contact:
                    name = FileType.Contacts;
                    Config.Admin.Contacts = path;
                    new SQLiteServer().updateAdmin("Contacts", Config.Admin.Contacts);
                    break;
                case FileType.Sms:
                    name = FileType.Smss;
                    Config.Admin.Smss = path;
                    new SQLiteServer().updateAdmin("Smss", Config.Admin.Smss);
                    break;
                case FileType.Call:
                    name = FileType.Calls;
                    Config.Admin.Calls = path;
                    new SQLiteServer().updateAdmin("Calls", Config.Admin.Calls);
                    break;
                case FileType.App:
                    name = FileType.Apps;
                    Config.Admin.Apps = path;
                    new SQLiteServer().updateAdmin("Apps", Config.Admin.Apps);
                    break;
                case FileType.File4:
                    name = FileType.File4s;
                    Config.Admin.File4s = path;
                    new SQLiteServer().updateAdmin("File4s", Config.Admin.File4s);
                    break;
                case FileType.File8:
                    name = FileType.File8s;
                    Config.Admin.File8s = path;
                    new SQLiteServer().updateAdmin("File8s", Config.Admin.File8s);
                    break;
                case FileType.File128:
                    name = FileType.File128s;
                    Config.Admin.File128s = path;
                    new SQLiteServer().updateAdmin("File128s", Config.Admin.File128s);
                    break;
            }
            ListView listView = findViewById(R.id.set_listView);
            MyAdapter info = (MyAdapter) listView.getAdapter();
            List infoList = info.get();
            for (int i = 0; i < infoList.size(); i++) {
                SetInfo setInfo = (SetInfo) infoList.get(i);
                if (setInfo.Message == name) {
                    setInfo.updateDesc(path);
                    info.notifyDataSetChanged();
                    break;
                }
            }
        }
    }

    private void onClick(String text) {
        if (Method.isEmpty(sdPath)) {
            Method.hit("外置SD卡不存在");
            return;
        }
        LFilePicker picker = new LFilePicker()
                .withActivity(this)
                .withTitle(text)
                .withMutilyMode(false);
        int code = REQUESTCODE_FROM_ACTIVITY;
        String path = null;
        switch (text) {
            case FileType.Images:
                code += FileType.Image;
                path = Config.Admin.Images;
                break;
            case FileType.Audios:
                code += FileType.Audio;
                path = Config.Admin.Audios;
                break;
            case FileType.Videos:
                code += FileType.Video;
                path = Config.Admin.Videos;
                break;
            case FileType.Contacts:
                code += FileType.Contact;
                path = Config.Admin.Contacts;
                break;
            case FileType.Smss:
                code += FileType.Sms;
                path = Config.Admin.Smss;
                break;
            case FileType.Calls:
                code += FileType.Call;
                path = Config.Admin.Calls;
                break;
            case FileType.Apps:
                code += FileType.App;
                path = Config.Admin.Apps;
                break;
            case FileType.File4s:
                code += FileType.File4;
                path = Config.Admin.File4s;
                break;
            case FileType.File8s:
                code += FileType.File8;
                path = Config.Admin.File8s;
                break;
            case FileType.File128s:
                code += FileType.File128;
                path = Config.Admin.File128s;
                break;
        }
        if (Method.isEmpty(path)) path = sdPath;
        else path = new File(path).getParent();
        picker = picker.withStartPath(path).withRequestCode(code);
        switch (text) {
            case FileType.Images:
                picker.start();
//                picker.withFileFilter(new String[]{".jpg", ".png", ".bmp"}).start();
                break;
            case FileType.Audios:
                picker.start();
//                picker.withFileFilter(new String[]{".avi", ".mp4", ".wav"}).start();
                break;
            case FileType.Videos:
                picker.start();
//                picker.withFileFilter(new String[]{".avi", ".mp4", ".rmvb", ".wmv"}).start();
                break;
            case FileType.Contacts:
                picker.withFileFilter(new String[]{".csv"}).start();
                break;
            case FileType.Smss:
                picker.withFileFilter(new String[]{".csv"}).start();
                break;
            case FileType.Calls:
                picker.withFileFilter(new String[]{".csv"}).start();
                break;
            case FileType.Apps:
                picker.withFileFilter(new String[]{".apk"}).start();
                break;
            case FileType.File4s:
            case FileType.File8s:
            case FileType.File128s:
                picker.start();
                break;
        }
    }

    @Override
    public <T> void onReady(ObservableEmitter<LoadInfo> emitter, ViewHolder holder, T object) {
        if (!(object instanceof SetInfo)) return;
        SetInfo obj = (SetInfo) object;

        emitter.onNext(new LoaderInfo(LoadType.setText, holder, R.id.set_name, obj.Message));
        emitter.onNext(new LoaderInfo(LoadType.setText, holder, R.id.set_desc, obj.desc));
        emitter.onNext(new LoaderInfo(LoadType.setImageId, holder, R.id.set_img, obj.imageId));

        emitter.onNext(new LoaderInfo(LoadType.setLine, holder, R.id.set_name, obj.iHeard + ""));
        emitter.onNext(new LoaderInfo(LoadType.setLine, holder, R.id.set_desc, obj.iHeard + ""));
        emitter.onNext(new LoaderInfo(LoadType.setNoRight, holder, R.id.set_right, obj.noRight + ""));
    }

    @Override
    public void onReady(LoadInfo info) {
        switch (info.Types) {
            case setLine:
                LoaderInfo loader = (LoaderInfo) info;
                boolean iHeard = Boolean.parseBoolean(loader.msg);
                int height = iHeard ? 0 : (int) (15 * Config.display.density);
                TextView textView = loader.holder.getView(loader.id);
                textView.setPadding(height, height, height, height);
                textView.setBackgroundColor(iHeard ? getResources().getColor(R.color.colorGray) : 0);
                Method.setSize(textView, 0, (iHeard ? 10 : 54) * Config.display.density);
                break;
            case setNoRight:
                loader = (LoaderInfo) info;
                boolean noRight = Boolean.parseBoolean(loader.msg);
                loader.holder.getView(loader.id).setVisibility(noRight ? View.GONE : View.VISIBLE);
                break;
            case setImageId:
                loader = (LoaderInfo) info;
                if (loader.imageId == 0) {
                    loader.holder.getView(loader.id).setVisibility(View.GONE);
                }
                break;
            case setAdapter:
                ListView listView = findViewById(R.id.set_listView);
                //设置listView的Adapter
                listView.setAdapter(((AdapterInfo) info).adapter);
                listView.setOnItemClickListener((AdapterView<?> parent, View view, int position, long id) -> {
                    //我们需要的内容，跳转页面或显示详细信息
                    SetInfo setInfo = (SetInfo) ((AdapterInfo) info).list.get(position);
                    if (setInfo.iHeard) return;
                    switch (setInfo.Message) {
                        case "日志":
                            Intent intent = new Intent(this, LogActivity.class);
                            //将text框中的值传入
                            intent.putExtra("title", "日志");
                            File file = new File(Config.file.getPath(), "log.txt");
                            intent.putExtra("file", "file://" + file.getPath());
                            startActivity(intent);
                            break;
                        case "关于":
                            Method.show(this);
                            break;
                        default:
                            onClick(setInfo.Message);
                            break;
                    }
                });
                break;
        }
    }
}
