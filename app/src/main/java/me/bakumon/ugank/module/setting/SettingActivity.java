package me.bakumon.ugank.module.setting;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.CompoundButton;

import com.afollestad.materialdialogs.MaterialDialog;

import es.dmoral.toasty.Toasty;
import me.bakumon.ugank.R;
import me.bakumon.ugank.base.BaseActivity;
import me.bakumon.ugank.databinding.ActivitySettingBinding;
import me.bakumon.ugank.utills.AlipayZeroSdk;
import me.bakumon.ugank.utills.MDTintUtil;

/**
 * 设置
 *
 * @author bakumon https://bakumon.me
 */
public class SettingActivity extends BaseActivity implements SettingContract.View, CompoundButton.OnCheckedChangeListener, View.OnClickListener {

    private SettingPresenter mSettingPresenter = new SettingPresenter(this);

    private ActivitySettingBinding binding;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_setting;
    }

    @Override
    protected void onInit(@Nullable Bundle savedInstanceState) {
        binding = getDataBinding();
        setSupportActionBar(binding.toolbarSetting);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        binding.toolbarSetting.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        binding.switchSetting.setOnCheckedChangeListener(this);
        binding.switchSettingShowLauncherImg.setOnCheckedChangeListener(this);
        binding.switchSettingAlwaysShowLauncherImg.setOnCheckedChangeListener(this);

        binding.llIsShowListImg.setOnClickListener(this);
        binding.llIsShowLauncherImg.setOnClickListener(this);
        binding.llIsAlwaysShowLauncherImg.setOnClickListener(this);
        binding.llSettingImageQuality.setOnClickListener(this);
        binding.llSettingAbout.setOnClickListener(this);
        binding.llSettingCleanCache.setOnClickListener(this);
        binding.llSettingIssues.setOnClickListener(this);
        binding.llSettingPay.setOnClickListener(this);

        mSettingPresenter.subscribe();
    }

    @Override
    protected View[] setImmersiveView() {
        return new View[]{binding.appbarSetting};
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
        switch (compoundButton.getId()) {
            case R.id.switch_setting:
                mSettingPresenter.saveIsListShowImg(isChecked);
                break;
            case R.id.switch_setting_show_launcher_img:
                mSettingPresenter.saveIsLauncherShowImg(isChecked);
                break;
            case R.id.switch_setting_always_show_launcher_img:
                mSettingPresenter.saveIsLauncherAlwaysShowImg(isChecked);
                break;
            default:
                break;
        }
    }

    @Override
    public void setToolbarBackgroundColor(int color) {
        binding.appbarSetting.setBackgroundColor(color);
    }

    @Override
    public void changeSwitchState(boolean isChecked) {
        binding.switchSetting.setChecked(isChecked);
    }

    @Override
    public void changeIsShowLauncherImgSwitchState(boolean isChecked) {
        binding.switchSettingShowLauncherImg.setChecked(isChecked);
    }

    @Override
    public void changeIsAlwaysShowLauncherImgSwitchState(boolean isChecked) {
        binding.switchSettingAlwaysShowLauncherImg.setChecked(isChecked);
    }

    @Override
    public void setSwitchCompatsColor(int color) {
        MDTintUtil.setTint(binding.switchSetting, color);
        MDTintUtil.setTint(binding.switchSettingShowLauncherImg, color);
        MDTintUtil.setTint(binding.switchSettingAlwaysShowLauncherImg, color);
    }

    @Override
    public void setAppVersionNameInTv(String versionName) {
        binding.tvSettingVersionName.setText(versionName);
    }

    @Override
    public void setImageQualityChooseUnEnable() {
        binding.llSettingImageQuality.setClickable(false);
        binding.tvSettingImageQualityTitle.setTextColor(getResources().getColor(R.color.colorTextUnEnable));
        binding.tvSettingImageQualityContent.setTextColor(getResources().getColor(R.color.colorTextUnEnable));
        binding.tvSettingImageQualityTip.setTextColor(getResources().getColor(R.color.colorTextUnEnable));
    }

    @Override
    public void setImageQualityChooseEnable() {
        binding.llSettingImageQuality.setClickable(true);
        binding.tvSettingImageQualityTitle.setTextColor(getResources().getColor(R.color.colorTextEnable));
        binding.tvSettingImageQualityContent.setTextColor(getResources().getColor(R.color.colorTextEnableGary));
        binding.tvSettingImageQualityTip.setTextColor(getResources().getColor(R.color.colorTextEnableGary));
    }

    @Override
    public void setLauncherImgProbabilityUnEnable() {
        binding.llIsAlwaysShowLauncherImg.setClickable(false);
        binding.switchSettingAlwaysShowLauncherImg.setClickable(false);
        binding.tvIsAlwaysShowLauncherImgTitle.setTextColor(getResources().getColor(R.color.colorTextUnEnable));
        binding.tvIsAlwaysShowLauncherImgContent.setTextColor(getResources().getColor(R.color.colorTextUnEnable));
    }

    @Override
    public void setLauncherImgProbabilityEnable() {
        binding.llIsAlwaysShowLauncherImg.setClickable(true);
        binding.switchSettingAlwaysShowLauncherImg.setClickable(true);
        binding.tvIsAlwaysShowLauncherImgTitle.setTextColor(getResources().getColor(R.color.colorTextEnable));
        binding.tvIsAlwaysShowLauncherImgContent.setTextColor(getResources().getColor(R.color.colorTextEnableGary));
    }

    @Override
    public void setThumbnailQualityInfo(int quality) {
        String thumbnailQuality = "";
        switch (quality) {
            case 0:
                thumbnailQuality = "原图";
                break;
            case 1:
                thumbnailQuality = "默认";
                break;
            case 2:
                thumbnailQuality = "省流";
                break;
            default:
                thumbnailQuality = "默认";
                break;
        }
        binding.tvSettingImageQualityContent.setText(thumbnailQuality);
    }

    @Override
    public void showCacheSize(String cache) {
        binding.tvSettingCleanCache.setText(cache);
    }

    @Override
    public void showSuccessTip(String msg) {
        Toasty.success(this, msg).show();
    }

    @Override
    public void showFailTip(String msg) {
        Toasty.error(this, msg).show();
    }

    @Override
    public void setShowLauncherTip(String tip) {
        binding.tvIsAlwaysShowLauncherImgContent.setText(tip);
    }

    @Override
    public void setAlwaysShowLauncherTip(String tip) {
        binding.tvIsAlwaysShowLauncherImgContent.setText(tip);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_setting_clean_cache:
                cleanCache();
                break;
            case R.id.ll_setting_issues:
                issues();
                break;
            case R.id.ll_setting_pay:
                pay();
                break;
            case R.id.ll_is_show_list_img:
                changSwitchState();
                break;
            case R.id.ll_is_show_launcher_img:
                isShowLauncherImg();
                break;
            case R.id.ll_is_always_show_launcher_img:
                isAlwaysShowLauncherImg();
                break;
            case R.id.ll_setting_image_quality:
                chooseThumbnailQuality();
                break;
            default:
                break;
        }
    }

    public void cleanCache() {
        mSettingPresenter.cleanCache();
    }

    public void issues() {
        Uri uri = Uri.parse("https://github.com/Bakumon/UGank/issues");
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }

    public void pay() {
        // https://fama.alipay.com/qrcode/qrcodelist.htm?qrCodeType=P  二维码地址
        // http://cli.im/deqr/ 解析二维码
        // aex01251c8foqaprudcp503
        if (AlipayZeroSdk.hasInstalledAlipayClient(this)) {
            AlipayZeroSdk.startAlipayClient(this, "aex01251c8foqaprudcp503");
        } else {
            Toasty.info(this, "谢谢，您没有安装支付宝客户端").show();
        }
    }

    public void changSwitchState() {
        binding.switchSetting.setChecked(!binding.switchSetting.isChecked());
    }

    public void isShowLauncherImg() {
        binding.switchSettingShowLauncherImg.setChecked(!binding.switchSettingShowLauncherImg.isChecked());
    }

    public void isAlwaysShowLauncherImg() {
        binding.switchSettingAlwaysShowLauncherImg.setChecked(!binding.switchSettingAlwaysShowLauncherImg.isChecked());
    }

    public void chooseThumbnailQuality() {
        new MaterialDialog.Builder(this)
                .title("缩略图质量")
                .items("原图", "默认", "省流")
                .widgetColor(mSettingPresenter.getColorPrimary())
                .alwaysCallSingleChoiceCallback()
                .itemsCallbackSingleChoice(mSettingPresenter.getThumbnailQuality(), new MaterialDialog.ListCallbackSingleChoice() {
                    @Override
                    public boolean onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                        mSettingPresenter.setThumbnailQuality(which);
                        dialog.dismiss();
                        return true;
                    }
                })
                .positiveText("取消")
                .positiveColor(mSettingPresenter.getColorPrimary())
                .show();
    }

    @Override
    public void onBackPressed() {
        if (mSettingPresenter.isThumbnailSettingChanged()) {
            // 显示缩略图设置项改变
            setResult(RESULT_OK);
        }
        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mSettingPresenter.unsubscribe();
    }

}

