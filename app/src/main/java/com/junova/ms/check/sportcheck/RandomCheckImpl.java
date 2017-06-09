package com.junova.ms.check.sportcheck;

import android.content.Context;
import android.util.Base64;
import android.widget.TextView;

import com.junova.ms.api.MsApi;
import com.junova.ms.bean.Part;
import com.junova.ms.bean.User;
import com.junova.ms.model.PartModule;
import com.junova.ms.model.UserModel;
import com.junova.ms.utils.ImageUtil;
import com.junova.ms.utils.PrefUtils;
import com.orhanobut.logger.Logger;

import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.Map;

import es.dmoral.toasty.Toasty;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by junova on 2017-03-06.
 */

public class RandomCheckImpl implements RandomCheckContract.prensenter {
    private RandomCheckContract.view randomCheckView;
    private Context context;

    public RandomCheckImpl(RandomCheckContract.view randomCheckView) {
        this.randomCheckView = randomCheckView;
        context = (Context) randomCheckView;
    }

    @Override
    public void upload(Map<String, String> params) {
        randomCheckView.showLoadingProgress();

        if (!params.get("image").isEmpty()) {
            String paths = "";
            for (String path :
                    params.get("image").split(";")) {
                paths = paths + Base64.encodeToString(ImageUtil.getSmallBitmap(path), Base64.DEFAULT) + ";";

            }
            params.put("image", paths);
        }
        MsApi.getInstance().uploadRandomMission(context, params).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<String>() {
            @Override
            public void accept(@NonNull String s) throws Exception {
                org.json.JSONObject jsonObject = new org.json.JSONObject(s);
                int status = jsonObject.getInt("status");
                if (status == 1) {

                    randomCheckView.hideLoadingProgress();
                    PrefUtils.putBoolean(context, "hasRandom", false);
                    Toasty.success(context, "上传成功").show();
                } else {
                    Toasty.error(context, "上传失败").show();
                }
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(@NonNull Throwable throwable) throws Exception {
                randomCheckView.hideLoadingProgress();
                Toasty.error(context, "上传失败").show();
            }
        });
    }

    @Override
    public void save(String title, String describe, String imagePath, Part factoryPart, Part shopPart, Part sectionPart, Part classPart, User user) {
        PrefUtils.putBoolean(context, "hasRandom", true);
        PrefUtils.putString(context, "randomTitle", title);
        PrefUtils.putString(context, "randomDescribe", describe);
        PrefUtils.putString(context, "randomFactoryId", factoryPart.getPartId());
        PrefUtils.putString(context, "randomFactoryName", factoryPart.getPartName());
        PrefUtils.putString(context, "randomShopId", shopPart.getPartId());
        PrefUtils.putString(context, "randomShopName", shopPart.getPartName());
        PrefUtils.putString(context, "randomSectionId", sectionPart.getPartId());
        PrefUtils.putString(context, "randomSectionId", sectionPart.getPartName());
        PrefUtils.putString(context, "randomClassId", classPart.getPartId());
        PrefUtils.putString(context, "randomClassId", classPart.getPartName());
        PrefUtils.putString(context, "randomUserId", user.getUserId());
        PrefUtils.putString(context, "randomUserName", user.getUserName());
        PrefUtils.putString(context, "randomImages", imagePath);
    }

    @Override
    public void getParts(String partId, final TextView tx) {
        randomCheckView.showLoadingProgress();
        Map<String, String> params = new HashMap<>();
        params.put("partId", partId);
        MsApi.getInstance().getPartList(context, params).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe(new Consumer<PartModule>() {
            @Override
            public void accept(@NonNull PartModule partModule) throws Exception {
                randomCheckView.hideLoadingProgress();
                randomCheckView.showPart(partModule.getPartList(), tx);
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(@NonNull Throwable throwable) throws Exception {
                Logger.d(throwable.getMessage());
                randomCheckView.hideLoadingProgress();
            }
        });
    }

    @Override
    public void getUsers(String partId, final TextView tx) {
        randomCheckView.showLoadingProgress();
        Map<String, String> params = new HashMap<>();
        params.put("partId", partId);
        MsApi.getInstance().getUserList(context, params).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe(new Consumer<UserModel>() {
            @Override
            public void accept(@NonNull UserModel userModel) throws Exception {
                randomCheckView.hideLoadingProgress();
                randomCheckView.showUser(userModel.getUsers(), tx);
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(@NonNull Throwable throwable) throws Exception {
                randomCheckView.hideLoadingProgress();
                Logger.d(throwable.getMessage());
            }
        });

    }

    @Override
    public void upLoadScanCode(String scanCode) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("barCode", scanCode);
        params.put("userId", PrefUtils.getString(context, "userId", ""));
        MsApi.getInstance().upScanCodeId(context, params).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<String>() {
            @Override
            public void accept(@NonNull String s) throws Exception {
                Logger.e("1111111111111111111");

                JSONObject jsonObject = new JSONObject(s);
                int status = jsonObject.getInt("status");
                if (status == 1) {
                    String missionId = jsonObject.getJSONObject("data").getString("missionId");
                    randomCheckView.toMissionDetailActivity(missionId);

                } else {
                    Toasty.error(context, "up scanCode result   error " + jsonObject.toString()).show();
                    Logger.e("up scanCode result   error " + jsonObject.toString());

                }
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(@NonNull Throwable throwable) throws Exception {
                if (throwable != null) {
                    Toasty.error(context, throwable.getMessage()).show();
                }
            }
        });
    }

    @Override
    public void subscribe() {

    }

    @Override
    public void unSubscribe() {

    }
}
