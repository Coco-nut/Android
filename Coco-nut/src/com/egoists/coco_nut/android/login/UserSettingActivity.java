package com.egoists.coco_nut.android.login;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.egoists.coco_nut.android.BaasioConfig;
import com.egoists.coco_nut.android.R;
import com.egoists.coco_nut.android.board.card.Person;
import com.egoists.coco_nut.android.cache.ImageFetcher;
import com.egoists.coco_nut.android.util.AndLog;
import com.egoists.coco_nut.android.util.BaasioDialogFactory;
import com.egoists.coco_nut.android.util.RoundedImage;
import com.egoists.coco_nut.android.util.UniqueString;
import com.googlecode.androidannotations.annotations.AfterViews;
import com.googlecode.androidannotations.annotations.Click;
import com.googlecode.androidannotations.annotations.EActivity;
import com.googlecode.androidannotations.annotations.UiThread;
import com.googlecode.androidannotations.annotations.ViewById;
import com.kth.baasio.Baas;
import com.kth.baasio.callback.BaasioCallback;
import com.kth.baasio.callback.BaasioUploadAsyncTask;
import com.kth.baasio.callback.BaasioUploadCallback;
import com.kth.baasio.entity.file.BaasioFile;
import com.kth.baasio.entity.user.BaasioUser;
import com.kth.baasio.exception.BaasioException;
import com.kth.baasio.preferences.BaasioPreferences;

@EActivity(R.layout.activity_user_setting)
public class UserSettingActivity extends Activity {
    @ViewById
    EditText edTxtUserSettingName;
    @ViewById
    EditText edTxtUserSettingPhone;
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
        
    private static final String TEMP_PHOTO_FILE = "temp.png";       // 임시 저장파일
    private static final int REQ_CODE_PICK_IMAGE = 0;
    
    private File mTempIconFile = null;
    
    @AfterViews
    void initUserSettingForm() {
        mContext = this;
        ImageFetcher imageFetcher = new ImageFetcher(mContext);
        
        BaasioUser user = Baas.io().getSignedInUser(); 
        edTxtUserSettingName.setText(user.getName());
        edTxtUserSettingPhone.setText(user.getProperty(Person.ENTITY_NAME_PHONE).asText());
        String imageUrl = user.getPicture();
        if (imageUrl != null) {
            imageFetcher.loadImage(imageUrl, imgUserSettingPhoto, R.drawable.ic_group_templete_9);
        } else {
            imgUserSettingPhoto.setImageResource(R.drawable.ic_group_templete_0);
        }
    }
    
    @UiThread
    void refreshMyIcon(Bitmap bitmap) {
        imgUserSettingPhoto.setImageBitmap(bitmap);
    }
    
    @Click(R.id.btnUserSetting)
    void doUpdateUser() {
        String userName = edTxtUserSettingName.getText().toString();
        String phone = edTxtUserSettingPhone.getText().toString();
        String passwd = edTxtUserSettingPassword.getText().toString();
        
        if (phone == null || phone.length() == 0
                || userName == null || userName.length() == 0) {
            BaasioDialogFactory.createErrorDialog(this, R.string.err_empty_login_form).show();
            return;
        }
        
        // 패쓰워드를 입력하면 수정됨
        if (passwd != null && passwd.length() != 0) {
            // Passwd 체크
            if (false == passwd.equals(edTxtUserSettingConfirmPassword.getText().toString())) {
                AndLog.e("password is not equal.");
                BaasioDialogFactory.createErrorDialog(mContext, R.string.error_equaled_password).show();
                return;
            }
        }
        
        if (mTempIconFile != null) {
            // 사진 수정했으면 파일 업로드부터 수행
            doUploadMyIconfileByBaasio(userName, phone);
            return;
        }
        doUserUpdateByBaasio(userName, phone, null);
    }
    
    ///////////////////////////////////////////////////////
    //  BaasIO 관련 통신 처리부
    ///////////////////////////////////////////////////////
    
    void doUserUpdateByBaasio(String userName, String phone, String profileImgUuid) {
        mDialog = ProgressDialog.show(mContext, "", "업데이트 중", true);
        final BaasioUser user = Baas.io().getSignedInUser();
        
        if (userName != null && userName.length() != 0) {
            user.setName(userName);
        }
        if (phone != null && phone.length() != 0) {
            user.setProperty(Person.ENTITY_NAME_PHONE, phone);
        }
        
        if (profileImgUuid != null) {
            String picture = "https://blob.baas.io/" + BaasioConfig.BAASIO_ID + "/" + BaasioConfig.APPLICATION_ID + "/files/" + profileImgUuid;
            user.setProperty(Person.ENTITY_NAME_PICTURE, picture);    //추가 정보
        }
        
        user.updateInBackground(mContext,
            new BaasioCallback<BaasioUser>() {
                @Override
                public void onException(BaasioException e) {
                    mDialog.dismiss();
                    BaasioDialogFactory.createErrorDialog(mContext, e).show();
                }

                @Override
                public void onResponse(BaasioUser response) {
                    mDialog.dismiss();
                    if (response != null) {
                        //현재 로그인한 사용자의 프로필이 바뀐것이므로 다시 설정을 해줘야함.
                        BaasioPreferences.setUserString(mContext, user.toString());
                        Baas.io().setSignedInUser(user);
                        
                        BaasioDialogFactory.createOneButtonDialog(mContext, R.string.title_succeed, response.getUuid().toString()).show();
                    }
                }
            });
    }
    

    /**
     * 사진 업로드는 회원 정보 수정에서...
     */
    void doUploadMyIconfileByBaasio(final String userName, final String phone) {
        mDialog = ProgressDialog.show(mContext, "", "프로필 업데이트 중", true);

        BaasioFile uploadFile = new BaasioFile();
        BaasioUploadAsyncTask mUploadFileAsyncTask = uploadFile.fileUploadInBackground(
                mTempIconFile.getPath()             // 업로드하려는 파일 경로
                , UniqueString.generate()           // 설정하려는 파일 이름
                , new BaasioUploadCallback() {

                    @Override
                    public void onResponse(BaasioFile response) {
                        // 성공
                        mDialog.dismiss();
                        doUserUpdateByBaasio(userName, phone, response.getUuid().toString());
                    }

                    @Override
                    public void onProgress(long total, long current) {
                        // 진행 상황
                    }

                    @Override
                    public void onException(BaasioException e) {
                        mDialog.dismiss();
                        BaasioDialogFactory.createErrorDialog(mContext, e).show();
                    }
                });
    }
        
    ////////////////////////////////////////////////////////////////////
    // 사진 관련
    ////////////////////////////////////////////////////////////////////
    @Click(R.id.imgUserSettingPhoto)
    void getPicture() {
        Intent intent = new Intent(
                Intent.ACTION_GET_CONTENT,      // 또는 ACTION_PICK
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");              // 모든 이미지
        intent.putExtra("crop", "true");        // Crop기능 활성화
        intent.putExtra(MediaStore.EXTRA_OUTPUT, getTempUri());     // 임시파일 생성
        intent.putExtra("outputFormat",         // 포맷방식
                Bitmap.CompressFormat.PNG.toString());

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
                f.createNewFile();      // 외장메모리에 temp.png 파일 생성
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
                    String filePath = Environment.getExternalStorageDirectory() + "/" + TEMP_PHOTO_FILE;
                    AndLog.d("temp profile img : " + filePath);
 
                    // 받은 파일을 변환하여 다시 임시파일에 쓴다
                    Bitmap selectedImage = BitmapFactory.decodeFile(filePath);
                    RoundedImage roundedImg = new RoundedImage();
                    Bitmap bitmap = roundedImg.getRoundedShape(selectedImage);
                    
                    ReWriteTempFile(bitmap);
                }
            }
            break;
        }
    }

    void ReWriteTempFile(Bitmap bitmap) {
        // 다시 임시파일에 쓰기
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bitmap.compress(CompressFormat.PNG, 0, bos);
        byte[] bitmapdata = bos.toByteArray();

        //write the bytes in file
        FileOutputStream fos;
        try {
            fos = new FileOutputStream(mTempIconFile);
            fos.write(bitmapdata);
        } catch (IOException e) {
        } finally {
            try {
                bos.flush();
                bos.close();
            } catch (IOException e) {
            }
            
        }
        
        refreshMyIcon(bitmap);
    }
}
