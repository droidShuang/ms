package com.junova.ms.utils;

import com.junova.ms.app.AppConfig;

/**
 * Created by rees_yang on 2017/6/8.
 */

public class IpConfigUtil {
    public static void updataIp(String ip){
        AppConfig.IP = ip;//http://rap.taobao.org/mockjsdata/15302/          http://10.1.10.61:80/cms/app/   http://10.1.10.95:8080/cms/app/

        AppConfig.host = "http://"+AppConfig.IP+"/cms/app/";   //http://10.188.186.214:7001/cms/   10.188.186.221:7001
        AppConfig.IMAGEPATH = "http://"+AppConfig.IP+"/cms/uploadFiles/uploadImgs/";//10.188.184.188:80
        AppConfig.UPDATA_URL = "http://"+AppConfig.IP+"/cms/uploadFiles/file/appVersion";

        AppConfig.LOGIN_URL = AppConfig.host + "clientLogin";//登陆
        AppConfig.GETMAININFO_URL = AppConfig.host + "getIndex";//获取主页信息
        AppConfig.GETTASKTABLE_URL = AppConfig.host + "getCheckMission";//获取任务表列表
        AppConfig.GETTASKTIP_URL = AppConfig.host + "A10004.do";//获取任务操作规范
        AppConfig.GETTASKITEM_URL = AppConfig.host + "getMissionDetail";//获取点检详情项
        AppConfig.GETREPAIRSMISSIONDETAIL = AppConfig.host + "getRepairsMissionDetail";
        AppConfig.GETREPAIRSMISSION_URL = AppConfig.host + "getRepairsMission";//获取维修任务
        AppConfig.UPLOADRECORD_URL = AppConfig.host + "uploadMission";//上传任务记录
        AppConfig.RESOLVEPROBLEM = AppConfig.host + "A10008.do";//任务维修添加操作记录
        AppConfig.GETCOUNT_URL = AppConfig.host + "getCountData";//获取统计数据
        AppConfig.GETHISORY_URL = AppConfig.host + "A100010.do";//获取历史信息
        AppConfig.GETTEAM_URL = AppConfig.host + "getPar" +
                "tList";//获取组织列表
        AppConfig.GETUSER_URl = AppConfig.host + "getPartUser";//获取用户列表
        AppConfig.GETERRORLIST_URL = AppConfig.host + "getErrorHistory";//获取异常统计
        AppConfig.GETPROJECT_URL =  AppConfig.host +"getModuleList";//获取管理项
        AppConfig.GETOPERATION_HOST = AppConfig.host + "A100014";//获取操作历史
        AppConfig.GETARTICLEDETAIL = AppConfig.host + "getArticalDetail";
        AppConfig.GETARTICLELIST = AppConfig.host + "getArticalList";
        AppConfig.UPTABLLEID_URL = AppConfig.host + "upMissionTableId";
        AppConfig.UPSCANCODE_URL = AppConfig.host + "upScanBarCode";
        AppConfig.GETSELECTIONMISSION_URL = AppConfig.host + "getSelectionMission";
        AppConfig.REPAIRS_URL = AppConfig.host + "repairs";
        AppConfig.DEAL_MISSION = AppConfig.host + "dealRepairsMission";
        AppConfig.GET_SELECTION_MISSION = AppConfig.host + "getSelectionMission";
        AppConfig.UPLOAD_RANDOM_MISSION_URL = AppConfig.host + "upRandomMission";//上传随机抽查
        AppConfig.GET_HISTORY_COUNT_URL = AppConfig.host + "getHistoryData";
        AppConfig.CHANGE_PASSWORD_URL = AppConfig.host + "changePassword";
        AppConfig.GETUPDATAFILE_URL = AppConfig.host + "getUpdataFile";
    }
}
