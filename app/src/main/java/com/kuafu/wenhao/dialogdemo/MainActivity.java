package com.kuafu.wenhao.dialogdemo;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Dialog dialog;

    private View inflate;

    private TextView choosePhoto;

    private TextView takePhoto;

    private AdvancedWebView mWebView;

    private static final String URL = "file:///android_asset/up3.html";

    private TextView mCancel;

    private ImageView imageView;

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        dialog = initDialog();
        mWebView.loadUrl(URL);
        mWebView.setCustomListener(new AdvancedWebView.CustomListener() {
            @Override
            public void actionOne() {
                dialog.show();
            }

            @Override
            public void actionCallBack(Uri[] uris) {


            }
        });
    }


    private void initView() {
        mWebView = (AdvancedWebView) findViewById(R.id.webview);
        imageView = (ImageView) findViewById(R.id.imageView);
    }

    public void test(View v) {
        if (dialog == null) {
            dialog = initDialog();
        }
        dialog.show();//显示对话框
    }

    private Dialog initDialog() {
        Dialog dialog = new Dialog(this, R.style.ActionSheetDialogStyle);
        //填充对话框的布局
        inflate = LayoutInflater.from(this).inflate(R.layout.dialog_layout, null);
        //初始化控件
        choosePhoto = (TextView) inflate.findViewById(R.id.choosePhoto);
        takePhoto = (TextView) inflate.findViewById(R.id.takePhoto);
        mCancel = (TextView) inflate.findViewById(R.id.btn_cancel);
        choosePhoto.setOnClickListener(this);
        takePhoto.setOnClickListener(this);
        mCancel.setOnClickListener(this);
        //将布局设置给Dialog
        dialog.setContentView(inflate);
        dialog.setCancelable(false);
        //获取当前Activity所在的窗体
        Window dialogWindow = dialog.getWindow();
        //设置Dialog从窗体底部弹出
        dialogWindow.setGravity(Gravity.BOTTOM);
        //获得窗体的属性
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.y = 20;//设置Dialog距离底部的距离
//       将属性设置给窗体
        dialogWindow.setAttributes(lp);
        return dialog;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.takePhoto:
                if (mWebView != null) {
                    mWebView.openCameraAction();
                }
                break;
            case R.id.choosePhoto:
                if (mWebView != null) {
                    mWebView.defaultOpenFileAction();
                }
                break;
            case R.id.btn_cancel:
                if (mWebView != null) {
                    mWebView.cancle();
                }
                break;
        }
        dialog.dismiss();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (mWebView != null) {
            mWebView.onActivityResult(requestCode, resultCode, data);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
