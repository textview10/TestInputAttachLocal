package com.zonekey.testinputattachlocal.activity;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ClipData;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.webkit.JsPromptResult;
import android.webkit.JsResult;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.zonekey.testinputattachlocal.R;
import com.zonekey.testinputattachlocal.util.PhotoUtils;

import java.io.File;

/**
 * Created by xu.wang
 * Date on  2018/9/20 19:09:06.
 *
 * @Desc
 */
public class WebViewActivity extends AppCompatActivity {
    //startActivityForResult()的参数
    protected final static int PHOTO_REQUEST = 100;
    protected final static int CAMERA_REQUEST = 130;
    protected final static int VIDEO_REQUEST = 120;
    protected File fileUri = new File(Environment.getExternalStorageDirectory().getPath() + "/" + SystemClock.currentThreadTimeMillis() + ".jpg");
    protected Uri imageUri;

    private WebView mWebView;
    private String url = "http://192.168.13.110:12095/wiseclass_evalute?type=student&subject=5";
    private static final String TAG = "WebViewActiviy";
    private boolean videoFlag = false;
    private ValueCallback<Uri[]> mUploadCallbackAboveL;
    private ValueCallback<Uri> mUploadMessage;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);
        initialView();
    }

    private void initialView() {
        mWebView = findViewById(R.id.web_view);
        WebSettings settings = mWebView.getSettings();
        settings.setUseWideViewPort(true);
        settings.setLoadWithOverviewMode(true);
        settings.setDomStorageEnabled(true);
        settings.setDefaultTextEncodingName("UTF-8");
        settings.setAllowContentAccess(true); // 是否可访问Content Provider的资源，默认值 true
        settings.setAllowFileAccess(true);    // 是否可访问本地文件，默认值 true
        // 是否允许通过file url加载的Javascript读取本地文件，默认值 false
        settings.setAllowFileAccessFromFileURLs(false);
        // 是否允许通过file url加载的Javascript读取全部资源(包括文件,http,https)，默认值 false
        settings.setAllowUniversalAccessFromFileURLs(false);
        settings.setDomStorageEnabled(true);

        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.loadUrl(url);
        mWebView.setWebChromeClient(new MyWebChromeClient());
    }

    class MyWebChromeClient extends WebChromeClient {
        // For Android 3.0-
        public void openFileChooser(ValueCallback<Uri> uploadMsg) {
            Log.d(TAG, "openFileChoose(ValueCallback<Uri> uploadMsg)");
            mUploadMessage = uploadMsg;
            if (videoFlag) {
                toRecordVideo();
            } else {
                takePhoto();
            }

        }

        // For Android 3.0+
        public void openFileChooser(ValueCallback uploadMsg, String acceptType) {
            Log.d(TAG, "openFileChoose( ValueCallback uploadMsg, String acceptType )");
            mUploadMessage = uploadMsg;
            if (videoFlag) {
                toRecordVideo();
            } else {
                takePhoto();
            }
        }

        //For Android 4.1
        public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType, String capture) {
            Log.d(TAG, "openFileChoose(ValueCallback<Uri> uploadMsg, String acceptType, String capture)");
            mUploadMessage = uploadMsg;
            if (videoFlag) {
                toRecordVideo();
            } else {
                takePhoto();
            }
        }

        @Override
        public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback, WebChromeClient.FileChooserParams fileChooserParams) {
            Log.d(TAG, "onShowFileChooser(ValueCallback<Uri> uploadMsg, String acceptType, String capture)");
            mUploadCallbackAboveL = filePathCallback;
            if (videoFlag) {
                toRecordVideo();
            } else {
                takePhoto();
            }
            return true;
        }

        @Override
        public void onProgressChanged(WebView webView, int newProgress) {
//            super.onProgressChanged(webView, newProgress);
////            LogUtil.e(TAG, "progress = " + newProgress);
//            if (newProgress == 100) {
//                if (progressBar.getVisibility() != View.INVISIBLE) {
//                    progressBar.setVisibility(View.INVISIBLE);
//                }
//                isLoadingSuccess = true;
//                showWebView();
//
//            } else {
//                if (progressBar.getVisibility() != View.VISIBLE) {
//                    progressBar.setVisibility(View.VISIBLE);
//                }
//                isLoadingSuccess = false;
//                progressBar.setProgress(newProgress);
//            }
        }

        @Override
        public boolean onJsAlert(WebView webView, String url, String message, final JsResult result) {
//            if (isFinishing()) {
//                return false;
//            }
//            new QMUIDialog.MessageDialogBuilder(QQWebViewActivity.this)
//                    .setTitle("提示")
//                    .setMessage(message)
//                    .addAction("知道了", new QMUIDialogAction.ActionListener() {
//                        @Override
//                        public void onClick(QMUIDialog dialog, int index) {
//                            dialog.dismiss();
//                            result.confirm();
//                        }
//                    }).show();
            return true;
        }

        @Override
        public boolean onJsConfirm(WebView view, String url, String message, final JsResult result) {
//            if (isFinishing()) {
//                return false;
//            }
//            new QMUIDialog.MessageDialogBuilder(QQWebViewActivity.this)
//                    .setTitle("提示")
//                    .setMessage(message)
//                    .addAction("取消", new QMUIDialogAction.ActionListener() {
//                        @Override
//                        public void onClick(QMUIDialog dialog, int index) {
//                            dialog.dismiss();
//                            result.cancel();
//                        }
//                    }).addAction("确定", new QMUIDialogAction.ActionListener() {
//                @Override
//                public void onClick(QMUIDialog dialog, int index) {
//                    dialog.dismiss();
//                    result.confirm();
//                }
//            }).show();
            return true;
        }

        @Override
        public boolean onJsPrompt(WebView view, String url, String message, String defaultValue, final JsPromptResult result) {
//            final View v = LayoutInflater.from(QQWebViewActivity.this).inflate(R.layout.dialog_prompt, null);
//            ((TextView) v.findViewById(R.id.tv_prompt_content)).setText(message);
//            ((EditText) v.findViewById(R.id.et_prompt)).setText(defaultValue);
//            AlertDialog.Builder b = new AlertDialog.Builder(QQWebViewActivity.this);
//            b.setTitle("提示");
//            b.setView(v);
//            b.setPositiveButton("确定", new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialog, int which) {
//                    String value = ((EditText) v.findViewById(R.id.et_prompt)).getText().toString();
//                    result.confirm(value);
//                }
//            });
//            b.setNegativeButton("取消", new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialog, int which) {
//                    result.cancel();
//                }
//            });
//            if (QQWebViewActivity.this.isFinishing()) {
//                return super.onJsPrompt(view, url, message, defaultValue, result);
//            }
//            b.create().show();
            return true;
        }
    }

    private void takePhoto() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("提示");
        builder.setMessage("拍照or相册");
        builder.setCancelable(false);
        builder.setNegativeButton("拍照", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                toCamera();
            }
        });
        builder.setPositiveButton("相册", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                toPhotoPick();
            }
        });
        if (isFinishing()) {
            return;
        }
        builder.show();
    }

    /**
     * 前往原生的录像界面
     */
    protected void toRecordVideo() {
        Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
        intent.putExtra(MediaStore.EXTRA_DURATION_LIMIT, 10);    //限制时长
        startActivityForResult(intent, VIDEO_REQUEST);                    //开启摄像机
    }

    /**
     * 前往原生的照片选择界面
     */
    protected void toPhotoPick() {
        Intent intent_album = new Intent(Intent.ACTION_PICK);
        intent_album.setType("image/*");
        startActivityForResult(intent_album, PHOTO_REQUEST);
    }

    protected void toCamera() {
        imageUri = Uri.fromFile(fileUri);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            imageUri = FileProvider.getUriForFile(WebViewActivity.this, getPackageName() + ".fileprovider", fileUri);//通过FileProvider创建一个content类型的Uri
        }
        PhotoUtils.takePicture(WebViewActivity.this, imageUri, CAMERA_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PHOTO_REQUEST) {
            if (null == mUploadMessage && null == mUploadCallbackAboveL) return;
            Uri result = data == null || resultCode != RESULT_OK ? null : data.getData();
            if (mUploadCallbackAboveL != null) {
                onActivityResultAboveL(requestCode, resultCode, data);
            } else if (mUploadMessage != null) {
                mUploadMessage.onReceiveValue(result);
                mUploadMessage = null;
            }
        } else if (requestCode == VIDEO_REQUEST) {
            if (null == mUploadMessage && null == mUploadCallbackAboveL) return;

            Uri result = data == null || resultCode != RESULT_OK ? null : data.getData();
            if (mUploadCallbackAboveL != null) {
                if (resultCode == RESULT_OK) {
                    mUploadCallbackAboveL.onReceiveValue(new Uri[]{result});
                    mUploadCallbackAboveL = null;
                } else {
                    mUploadCallbackAboveL.onReceiveValue(new Uri[]{});
                    mUploadCallbackAboveL = null;
                }

            } else if (mUploadMessage != null) {
                if (resultCode == RESULT_OK) {
                    mUploadMessage.onReceiveValue(result);
                    mUploadMessage = null;
                } else {
                    mUploadMessage.onReceiveValue(Uri.EMPTY);
                    mUploadMessage = null;
                }

            }
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void onActivityResultAboveL(int requestCode, int resultCode, Intent data) {
        if (requestCode != PHOTO_REQUEST || mUploadCallbackAboveL == null) {
            return;
        }
        Uri[] results = null;
        if (resultCode == Activity.RESULT_OK) {
            if (data == null) {
                results = new Uri[]{imageUri};
            } else {
                String dataString = data.getDataString();
                ClipData clipData = data.getClipData();
                if (clipData != null) {
                    results = new Uri[clipData.getItemCount()];
                    for (int i = 0; i < clipData.getItemCount(); i++) {
                        ClipData.Item item = clipData.getItemAt(i);
                        results[i] = item.getUri();
                    }
                }

                if (dataString != null)
                    results = new Uri[]{Uri.parse(dataString)};
            }
        }
        mUploadCallbackAboveL.onReceiveValue(results);
        mUploadCallbackAboveL = null;
    }

    @Override
    protected void onDestroy() {
        if (mWebView != null) {
            mWebView.destroy();
        }
        super.onDestroy();
    }
}
