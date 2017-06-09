package com.junova.ms.api;


import android.content.Context;

import com.junova.ms.app.AppConfig;
import com.junova.ms.bean.Alarm;
import com.junova.ms.bean.Article;
import com.junova.ms.model.ArticleDetailModel;
import com.junova.ms.model.ErrorHistoryModel;
import com.junova.ms.model.HistoryInfoModel;
import com.junova.ms.model.MissionDetailModel;
import com.junova.ms.model.MissionTableInfoModel;
import com.junova.ms.model.ModuleModel;
import com.junova.ms.model.OperationInfo;
import com.junova.ms.model.OperationTip;
import com.junova.ms.model.PartModule;
import com.junova.ms.model.RepairModel;
import com.junova.ms.model.RepairsMissionDetailModel;
import com.junova.ms.model.SelecMissionModel;
import com.junova.ms.model.StatisticsInfoModle;
import com.junova.ms.model.LoginModel;
import com.junova.ms.model.MainInfoModel;
import com.junova.ms.model.UserModel;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import io.reactivex.Flowable;


/**
 * Created by rider on 2016/6/29 0029 10:12.
 * Description :调用服务器接口类
 */
public class MsApi extends BaseApi {
    public static class Holder {
        public static final MsApi msApi = new MsApi();
    }

    public static MsApi getInstance() {
        return Holder.msApi;
    }

    //登录
    public Flowable<LoginModel> login(Context context, Map<String, String> params) {
        return createObservable(context, AppConfig.LOGIN_URL, params, LoginModel.class).debounce(2000, TimeUnit.MILLISECONDS);
    }

    //获取文章列表
    public Flowable<List<Article>> getArticleList(Context context, Map<String, String> params) {
        return createListObservable(context, AppConfig.GETARTICLELIST, params, Article.class);
    }

    //获取文章详情
    public Flowable<ArticleDetailModel> getArticleDetail(Context context, Map<String, String> params) {
        return createObservable(context, AppConfig.GETARTICLEDETAIL, params, ArticleDetailModel.class);
    }

    //获取主页信息
    public Flowable<MainInfoModel> getMainInfo(Context context, Map<String, String> params) {
        return createObservable(context, AppConfig.GETMAININFO_URL, params, MainInfoModel.class);
    }

    //获取任务表列表
    public Flowable<MissionTableInfoModel> getMissionTableList(Context context, Map<String, String> params) {

        return createObservable(context, AppConfig.GETTASKTABLE_URL, params, MissionTableInfoModel.class);
    }

    //获取任务表列表
    public Flowable<SelecMissionModel> getSelctionMissionList(Context context, Map<String, String> params) {

        return createObservable(context, AppConfig.GETSELECTIONMISSION_URL, params, SelecMissionModel.class);
    }


    //获取任务
    public Flowable<MissionDetailModel> getMissionDetailList(Context context, Map<String, String> params) {
        return createObservable(context, AppConfig.GETTASKITEM_URL, params, MissionDetailModel.class);
    }

    //获取操作提示
    public Flowable<OperationTip> getTip(Context context, Map<String, String> params) {
        return createObservable(context, AppConfig.GETTASKTIP_URL, params, OperationTip.class);
    }

    //获取统计数据
    public Flowable<StatisticsInfoModle> getCount(Context context, Map<String, String> params) {
        return createObservable(context, AppConfig.GETCOUNT_URL, params, StatisticsInfoModle.class);
    }

    public Flowable<StatisticsInfoModle> getHistoryCount(Context context, Map<String, String> parmas) {
        return createObservable(context, AppConfig.GET_HISTORY_COUNT_URL, parmas, StatisticsInfoModle.class);
    }

    //获取组织列表
    public Flowable<PartModule> getPartList(Context context, Map<String, String> params) {
        return createObservable(context, AppConfig.GETTEAM_URL, params, PartModule.class);
    }

    public Flowable<UserModel> getUserList(Context context, Map<String, String> params) {
        return createObservable(context, AppConfig.GETUSER_URl, params, UserModel.class);
    }

    //获取管理项
    public Flowable<ModuleModel> getModuleList(Context context, Map<String, String> params) {
        return createObservable(context, AppConfig.GETPROJECT_URL, params, ModuleModel.class);
    }

    //获取历史数据
    public Flowable<HistoryInfoModel> getHistory(Context context, Map<String, String> params) {
        return createObservable(context, AppConfig.GETHISORY_URL, params, HistoryInfoModel.class);
    }

    //获取任务异常类型
    public Flowable<List<Alarm>> getErrorKind(Context context, Map<String, String> params) {
        return createListObservable(context, AppConfig.GETMAININFO_URL, params, Alarm.class);
    }

    public Flowable<List<OperationInfo>> getOperationInfo(Context context, Map<String, String> params) {
        return createListObservable(context, AppConfig.GETOPERATION_HOST, params, OperationInfo.class);
    }

    /**
     * @describe 获取任务维修列表
     * @author 杨爽
     * @time 2017-04-05  10:23
     * @version 1.0
     */
    public Flowable<RepairModel> getMaintainList(Context context, Map<String, String> params) {
        return createObservable(context, AppConfig.GETREPAIRSMISSION_URL, params, RepairModel.class);
    }

    //获取任务维修操作历史
    public Flowable<RepairsMissionDetailModel> getMaintainDetail(Context context, Map<String, String> params) {
        return createObservable(context, AppConfig.GETREPAIRSMISSIONDETAIL, params, RepairsMissionDetailModel.class);
    }


    public Flowable<ErrorHistoryModel> getErrorHistory(Context context, Map<String, String> params) {
        return createObservable(context, AppConfig.GETERRORLIST_URL, params, ErrorHistoryModel.class);
    }


    //上传任务检查记录
    public Flowable<String> uploadTaskRecord(Context context, Map<String, String> params) {
        return createStringObservable(context, AppConfig.UPLOADRECORD_URL, params);
    }

    public Flowable<String> getUpdataFile(Context context, Map<String, String> params) {
        return createStringObservable(context, AppConfig.GETUPDATAFILE_URL, params);
    }

    public Flowable<String> changePassword(Context context, Map<String, String> params) {
        return createStringObservable(context, AppConfig.CHANGE_PASSWORD_URL, params);
    }

    public Flowable<String> uploadTableId(Context context, Map<String, String> params) {
        return createStringObservable(context, AppConfig.UPTABLLEID_URL, params);
    }

    public Flowable<String> upScanCodeId(Context context, Map<String, String> params) {
        return createStringObservable(context, AppConfig.UPSCANCODE_URL, params);
    }

    public Flowable<String> dealRepairsMission(Context context, Map<String, String> params) {
        return createStringObservable(context, AppConfig.DEAL_MISSION, params);
    }

    public Flowable<String> repairs(Context context, Map<String, String> params) {
        return createStringObservable(context, AppConfig.REPAIRS_URL, params);

    }

    public Flowable<String> addOperation(Context context, Map<String, String> params) {
        return createStringObservable(context, AppConfig.RESOLVEPROBLEM, params);
    }


    public Flowable<String> uploadRandomMission(Context context, Map<String, String> params) {
        return createStringObservable(context, AppConfig.UPLOAD_RANDOM_MISSION_URL, params);
    }


}
