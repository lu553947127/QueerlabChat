package com.queerlab.chat.view.group.user;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.blankj.utilcode.util.BarUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.queerlab.chat.R;
import com.queerlab.chat.base.BaseActivity;
import com.queerlab.chat.event.UserEvent;
import com.queerlab.chat.tencent.TUIKitUtil;
import com.queerlab.chat.utils.CropUtils;
import com.queerlab.chat.utils.PermissionUtils;
import com.queerlab.chat.utils.PictureUtils;
import com.queerlab.chat.viewmodel.UserViewModel;
import com.queerlab.chat.widget.CircleImageView;
import com.yalantis.ucrop.UCrop;
import com.zhihu.matisse.Matisse;

import org.greenrobot.eventbus.EventBus;

import java.util.Objects;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @ProjectName: QueerlabChat
 * @Package: com.queerlab.chat.view.group.user
 * @ClassName: UpdateAvatarActivity
 * @Description: 编辑头像页面
 * @Author: 鹿鸿祥
 * @CreateDate: 5/31/21 11:45 AM
 * @UpdateUser: 更新者
 * @UpdateDate: 5/31/21 11:45 AM
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
public class UpdateAvatarActivity extends BaseActivity {
    @BindView(R.id.fake_status_bar)
    View fakeStatusBar;
    @BindView(R.id.iv_avatar)
    CircleImageView ivAvatar;
    //图片本地路径
    private String path = "";
    private UserViewModel userViewModel;

    @Override
    protected int initLayoutRes() {
        return R.layout.activity_update_avatar;
    }

    @Override
    public boolean isUseEventBus() {
        return false;
    }

    @Override
    protected void initDataAndEvent(Bundle savedInstanceState) {
        BarUtils.setStatusBarColor(fakeStatusBar, getResources().getColor(R.color.white));
        PictureUtils.setImage(activity, getIntent().getStringExtra("avatar"), ivAvatar);

        userViewModel = getViewModel(UserViewModel.class);

        //编辑头像成功返回结果
        userViewModel.uploadFileLiveData.observe(activity, uploadFileBean -> {
            LogUtils.e(uploadFileBean.getUserPortraitKey());
            ToastUtils.showShort("编辑头像成功");
            EventBus.getDefault().post(new UserEvent("avatar"));
            TUIKitUtil.updateProfileAvatar(activity, uploadFileBean.getUserPortraitKey());
            finish();
        });

        //加载动画返回结果
        userViewModel.pageStateLiveData.observe(activity, s -> {
            showPageState(s);
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //从相册选择图片返回
        if (requestCode == PictureUtils.ALBUM_CODE & resultCode == RESULT_OK) {
            LogUtils.e(Matisse.obtainResult(data).get(0));
            //裁剪完毕的图片存放路径
            CropUtils.cropUriPicture(activity, Matisse.obtainResult(data).get(0));
//            if (MyApplication.isAndroidQ) {
//                LogUtils.e(Matisse.obtainResult(Objects.requireNonNull(data)).get(0));
//                //裁剪完毕的图片存放路径
//                CropUtils.cropUriPicture(activity, Matisse.obtainResult(Objects.requireNonNull(data)).get(0));
//            }else {
//                LogUtils.e(Matisse.obtainResult(Objects.requireNonNull(data)).get(0));
//                LogUtils.e(Matisse.obtainPathResult(Objects.requireNonNull(data)).get(0));
//                path = Matisse.obtainPathResult(Objects.requireNonNull(data)).get(0);
//                ivAvatar.setImageBitmap(BitmapFactory.decodeFile(path));
//            }
            //图片裁剪成功
        }else if (resultCode == RESULT_OK && requestCode == UCrop.REQUEST_CROP){
            final Uri resultUri = UCrop.getOutput(data);
            LogUtils.e(resultUri);
            ivAvatar.setImageURI(resultUri);
            path = PictureUtils.getUriFile(resultUri);
            //图片裁剪出错出错时进入该分支
        } else if (resultCode == UCrop.RESULT_ERROR) {
            final Throwable cropError = UCrop.getError(data);
            LogUtils.e(cropError);
            ToastUtils.showShort("图片裁剪失败: " + cropError.getMessage());
        }
    }

    @OnClick({R.id.iv_bar_back, R.id.iv_avatar, R.id.tv_ok})
    void onClick(View view) {
        switch (view.getId()){
            case R.id.iv_bar_back:
                finish();
                break;
            case R.id.iv_avatar://打开相册
                PermissionUtils.openStoragePermission(activity, 1);
                break;
            case R.id.tv_ok://上传
                if (TextUtils.isEmpty(path)){
                    ToastUtils.showShort("图片不能为空");
                    return;
                }

                userViewModel.uploadPhoto(path);
                break;
        }
    }
}
