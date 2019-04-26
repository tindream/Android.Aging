package com.riwise.aging.activity;


import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.leon.lfilepickerlibrary.LFilePicker;
import com.leon.lfilepickerlibrary.utils.Constant;
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
            long totalSpace = sdcard.getTotalSpace();
            Config.I32 = totalSpace < 1024 * 1024 * 1024 * 32.0;
            list.add(new SetInfo("内置SD卡 " + (Config.I32 ? "=" : ">") + " 32G", true));
            list.add(new SetInfo());
        } else {
            list.add(new SetInfo("SD卡未挂载"));
        }
        list.add(new SetInfo(getString(R.string.btn_0)));
        list.add(new SetInfo(getString(R.string.btn_10)));
        list.add(new SetInfo(getString(R.string.btn_18)));
        list.add(new SetInfo(getString(R.string.btn_24)));
        list.add(new SetInfo());

        list.add(new SetInfo(FileType.Images, Config.Admin.Images));
        list.add(new SetInfo(FileType.Audios, Config.Admin.Audios));
        list.add(new SetInfo(FileType.Videos, Config.Admin.Videos));
        list.add(new SetInfo(FileType.Apps, Config.Admin.Apps));
        list.add(new SetInfo(FileType.File4s, Config.Admin.File4s));
        list.add(new SetInfo(FileType.File8s, Config.Admin.File8s));
        list.add(new SetInfo(FileType.File128s, Config.Admin.File128s));

        list.add(new SetInfo());
        list.add(new SetInfo(getString(R.string.btn_log)));
        list.add(new SetInfo(getString(R.string.btn_about), Config.version, true));
        list.add(new SetInfo());
        list.add(new SetInfo());
        new AsyncListView().setListener(this, this).init(this, list, R.layout.item_set);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            List<String> list = data.getStringArrayListExtra(Constant.RESULT_INFO);
            String path = list.size() > 0 ? list.get(0) : null;
            String name = null;
            SQLiteServer server = new SQLiteServer();
            if (requestCode == getCode(FileType.Images)) {
                name = FileType.Images;
                Config.Admin.Images = path;
                server.updateAdmin("Images", Config.Admin.Images);
            } else if (requestCode == getCode(FileType.Audios)) {
                name = FileType.Audios;
                Config.Admin.Audios = path;
                server.updateAdmin("Audios", Config.Admin.Audios);
            } else if (requestCode == getCode(FileType.Videos)) {
                name = FileType.Videos;
                Config.Admin.Videos = path;
                server.updateAdmin("Videos", Config.Admin.Videos);
            } else if (requestCode == getCode(FileType.Apps)) {
                name = FileType.Apps;
                for (int i = 1; i < list.size(); i++) path += ";" + list.get(i);
                Config.Admin.Apps = path;
                server.updateAdmin("Apps", Config.Admin.Apps);
            } else if (requestCode == getCode(FileType.File4s)) {
                name = FileType.File4s;
                Config.Admin.File4s = path;
                server.updateAdmin("File4s", Config.Admin.File4s);
            } else if (requestCode == getCode(FileType.File8s)) {
                name = FileType.File8s;
                Config.Admin.File8s = path;
                server.updateAdmin("File8s", Config.Admin.File8s);
            } else if (requestCode == getCode(FileType.File128s)) {
                name = FileType.File128s;
                Config.Admin.File128s = path;
                server.updateAdmin("File128s", Config.Admin.File128s);
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

    private int getCode(String text) {
        int code = text.hashCode();
        while (code > 65535) code /= 2;
        return code;
    }

    private void onClick(String text) {
        switch (text) {
            case "老化测试模型":
            case "10个月老化模型":
            case "18个月老化模型":
            case "24个月老化模型":
                Intent intent = new Intent(this, AgingActivity.class);
                //将text框中的值传入
                intent.putExtra("title", text);
                startActivity(intent);
                return;
        }
        LFilePicker picker = new LFilePicker()
                .withActivity(this)
                .withTitle(text)
                .withMutilyMode(false)
                .withBackIcon(Constant.BACKICON_STYLETHREE);
        String path = null;
        switch (text) {
            case FileType.Images:
                path = Config.Admin.Images;
                break;
            case FileType.Audios:
                path = Config.Admin.Audios;
                break;
            case FileType.Videos:
                path = Config.Admin.Videos;
                break;
            case FileType.Apps:
                path = Config.Admin.Apps;
                if (path != null && path.contains(";")) path = path.split(";")[0];
                break;
            case FileType.File4s:
                path = Config.Admin.File4s;
                break;
            case FileType.File8s:
                path = Config.Admin.File8s;
                break;
            case FileType.File128s:
                path = Config.Admin.File128s;
                break;
        }
        if (Method.isEmpty(path)) path = sdPath;
        else path = new File(path).getParent();
        int code = getCode(text);
        picker = picker.withRequestCode(code).withStartPath(path);
        switch (text) {
            case FileType.Images:
            case FileType.Audios:
            case FileType.Videos:
                picker.start();
                break;
            case FileType.Apps:
                picker.withMutilyMode(true).withFileFilter(new String[]{".apk"}).start();
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
                loader.holder.getView(loader.id).setVisibility(loader.imageId == 0 ? View.GONE : View.VISIBLE);
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
