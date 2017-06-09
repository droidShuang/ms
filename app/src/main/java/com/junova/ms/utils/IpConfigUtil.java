package com.junova.ms.utils;

import com.junova.ms.app.AppConfig;

/**
 * Created by rees_yang on 2017/6/8.
 */

public class IpConfigUtil {
    public static void updataIp(String ip){
        AppConfig.IP = "10.1.10.107:7001";//http://rap.taobao.org/mockjsdata/15302/          http://10.1.10.61:80/cms/app/   http://10.1.10.95:8080/cms/app/

        AppConfig.host = "http://"+AppConfig.IP+"/cms/app/";   //http://10.188.186.214:7001/cms/   10.188.186.221:7001
        AppConfig.IMAGEPATH = "http://"+AppConfig.IP+"/cms/uploadFiles/uploadImgs/";//10.188.184.188:80
        AppConfig.UPDATA_URL = "http://"+AppConfig.IP+"/cms/uploadFiles/file/appVersion";

        AppConfig.LOGIN_URL = ip + "clientLogin";//登陆
        AppConfig.GETMAININFO_URL = ip + "getIndex";//获取主页信息
        AppConfig.GETTASKTABLE_URL = ip + "getCheckMission";//获取任务表列表
        AppConfig.GETTASKTIP_URL = ip + "A10004.do";//获取任务操作规范
        AppConfig.GETTASKITEM_URL = ip + "getMissionDetail";//获取点检详情项
        AppConfig.GETREPAIRSMISSIONDETAIL = ip + "getRepairsMissionDetail";
        AppConfig.GETREPAIRSMISSION_URL = ip + "getRepairsMission";//获取维修任务
        AppConfig.UPLOADRECORD_URL = ip + "uploadMission";//上传任务记录
        AppConfig.RESOLVEPROBLEM = ip + "A10008.do";//任务维修添加操作记录
        AppConfig.GETCOUNT_URL = ip + "getCountData";//获取统计数据
        AppConfig.GETHISORY_URL = ip + "A100010.do";//获取历史信息
        AppConfig.GETTEAM_URL = ip + "getPar" +
                "tList";//获取组织列表
        AppConfig.GETUSER_URl = ip + "getPartUser";//获取用户列表
        AppConfig.GETERRORLIST_URL = ip + "getErrorHistory";//获取异常统计
        AppConfig.GETPROJECT_URL = ip + "getModuleList";//获取管理项
        AppConfig.GETOPERATION_HOST = ip + "A100014";//获取操作历史
        AppConfig.GETARTICLEDETAIL = ip + "getArticalDetail";
        AppConfig.GETARTICLELIST = ip + "getArticalList";
        AppConfig.UPTABLLEID_URL = ip + "upMissionTableId";
        AppConfig.UPSCANCODE_URL = ip + "upScanBarCode";
        AppConfig.GETSELECTIONMISSION_URL = ip + "getSelectionMission";
        AppConfig.REPAIRS_URL = ip + "repairs";
        AppConfig.DEAL_MISSION = ip + "dealRepairsMission";
        AppConfig.GET_SELECTION_MISSION = ip + "getSelectionMission";
        AppConfig.UPLOAD_RANDOM_MISSION_URL = ip + "upRandomMission";//上传随机抽查
        AppConfig.GET_HISTORY_COUNT_URL = ip + "getHistoryData";
        AppConfig.CHANGE_PASSWORD_URL = ip + "changePassword";
        AppConfig.GETUPDATAFILE_URL = ip + "getUpdataFile";
    }
}
