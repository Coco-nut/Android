package com.egoists.coco_nut.android.login;

import java.io.File;
import java.io.IOException;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.egoists.coco_nut.android.R;
import com.egoists.coco_nut.android.util.AndLog;
import com.egoists.coco_nut.android.util.BaasioDialogFactory;
import com.egoists.coco_nut.android.util.MyAndroidInfo;
import com.egoists.coco_nut.android.util.RoundedImage;
import com.egoists.coco_nut.android.util.UniqueString;
import com.googlecode.androidannotations.annotations.AfterViews;
import com.googlecode.androidannotations.annotations.Click;
import com.googlecode.androidannotations.annotations.EActivity;
import com.googlecode.androidannotations.annotations.UiThread;
import com.googlecode.androidannotations.annotations.ViewById;
import com.kth.baasio.callback.BaasioUploadAsyncTask;
import com.kth.baasio.callback.BaasioUploadCallback;
import com.kth.baasio.entity.file.BaasioFile;
import com.kth.baasio.exception.BaasioException;

@EActivity(R.layout.activity_user_setting)
public class UserSettingActivity extends Activity {

    @ViewById
    EditText edTxtUserSettingId;
    @ViewById
    EditText edTxtUserSettingName;
    @ViewById
    EditText edTxtUserSettingPassword;
    @ViewById
    EditText edTxtUserSettingConfirmPassword;
    @ViewById
    TextView txtUserSettingMessage;
    @ViewById
    ImageView imgUserSettingPhoto;
    
    private Context mContext;
    private ProgressDialog mDialog;
        
    private static final String TEMP_PHOTO_FILE = "temp.jpg";       // 임시 저장파일
    private static final int REQ_CODE_PICK_IMAGE = 0;
    
    private File mTempIconFile = null;
    
    @AfterViews
    void initUserSettingForm() {
        mContext = this;
        edTxtUserSettingId.setText(MyAndroidInfo.getMyIdFromEmail(this));
    }
    
    @UiThread
    void refreshMyIcon(Bitmap bitmap) {
        RoundedImage roundedImg = new RoundedImage();
        Bitmap bm = roundedImg.getRoundedShape(bitmap);
        imgUserSettingPhoto.setImageBitmap(bm);
    }
    
    @Click(R.id.btnUserSetting)
    void doUserSetting() {
        String userId = edTxtUserSettingId.getText().toString();
        String passwd = edTxtUserSettingPassword.getText().toString();
        String userName = edTxtUserSettingName.getText().toString();
        
        if (userId == null || userId.length() == 0
                || passwd == null || passwd.length() == 0
                || userName == null || userName.length() == 0) {
            AndLog.e("Empty Id or Password");
            BaasioDialogFactory.createErrorDialog(this, R.string.err_empty_login_form).show();
            return;
        }
        
        // Passwd 체크
        if (false == passwd.equals(edTxtUserSettingConfirmPassword.getText().toString())) {
            AndLog.e("password is not equal.");
            BaasioDialogFactory.createErrorDialog(mContext, R.string.error_equaled_password).show();
            return;
        }
    }
    

    /**
     * 사진 업로드는 회원 정보 수정에서...
     */
    void doUploadMyIconfileByBaasio() {
        // TODO
        BaasioFile uploadFile = new BaasioFile();

        BaasioUploadAsyncTask mUploadFileAsyncTask = uploadFile.fileUploadInBackground(
                mTempIconFile.getPath()             // 업로드하려는 파일 경로
                , UniqueString.generate()           // 설정하려는 파일 이름
                , new BaasioUploadCallback() {

                    @Override
                    public void onResponse(BaasioFile response) {
                        // 성공
                    }

                    @Override
                    public void onProgress(long total, long current) {
                        // 진행 상황
                    }

                    @Override
                    public void onException(BaasioException e) {
                        // 실패
                        BaasioDialogFactory.createErrorDialog(mContext, e).show();
                    }
                });
    }
        
    ////////////////////////////////////////////////////////////////////
    // 사진 관련
    ////////////////////////////////////////////////////////////////////
    @Click(R.id.imgSignUpPhoto)
    void getPicture() {
        Intent intent = new Intent(
                Intent.ACTION_GET_CONTENT,      // 또는 ACTION_PICK
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");              // 모든 이미지
        intent.putExtra("crop", "true");        // Crop기능 활성화
        intent.putExtra(MediaStore.EXTRA_OUTPUT, getTempUri());     // 임시파일 생성
        intent.putExtra("outputFormat",         // 포맷방식
                Bitmap.CompressFormat.JPEG.toString());

        startActivityForResult(intent, REQ_CODE_PICK_IMAGE);
        // REQ_CODE_PICK_IMAGE == requestCode
    }
    
    /** 임시 저장 파일의 경로를 반환 */
    private Uri getTempUri() {
        mTempIconFile = getTempFile();
        return Uri.fromFile(mTempIconFile);
    }
    
    /** 외장메모리에 임시 이미지 파일을 생성하여 그 파일의 경로를 반환  */
    private File getTempFile() {
        if (isSDCARDMOUNTED()) {
            File f = new File(Environment.getExternalStorageDirectory(), // 외장메모리 경로
                    TEMP_PHOTO_FILE);
            try {
                f.createNewFile();      // 외장메모리에 temp.jpg 파일 생성
            } catch (IOException e) {
            }
 
            return f;
        } else
            return null;
    }
 
    /** SD카드가 마운트 되어 있는지 확인 */
    private boolean isSDCARDMOUNTED() {
        String status = Environment.getExternalStorageState();
        if (status.equals(Environment.MEDIA_MOUNTED))
            return true;
 
        return false;
    }
 
    /** 다시 액티비티로 복귀하였을때 이미지를 셋팅 */
    protected void onActivityResult(int requestCode, int resultCode,
            Intent imageData) {
        super.onActivityResult(requestCode, resultCode, imageData);
 
        switch (requestCode) {
        case REQ_CODE_PICK_IMAGE:
            if (resultCode == RESULT_OK) {
                if (imageData != null) {
                    String filePath = Environment.getExternalStorageDirectory()
                            + "/temp.jpg";
 
                    System.out.println("path" + filePath); // logCat으로 경로확인.
 
                    Bitmap selectedImage = BitmapFactory.decodeFile(filePath);
                    // temp.jpg파일을 Bitmap으로 디코딩한다.
                    refreshMyIcon(selectedImage);
                }
            }
            break;
        }
    }

}
