package com.junova.ms.app;

import android.content.SharedPreferences;

/**
 * Created by yangshuang on 2016/3/25.
 * Description :
 */
public class AppConfig {
    //url 10.188.186.221:7001         http://10.1.10.107:8080 10.124.87.212:7001/cms
    public static String IP = "10.1.10.107:7001";//http://rap.taobao.org/mockjsdata/15302/          http://10.1.10.61:80/cms/app/   http://10.1.10.95:8080/cms/app/

    public static String host = "http://"+IP+"/cms/app/";   //http://10.188.186.214:7001/cms/   10.188.186.221:7001
    public static String IMAGEPATH = "http://"+IP+"/cms/uploadFiles/uploadImgs/";//10.188.184.188:80
    public static String UPDATA_URL = "http://"+IP+"/cms/uploadFiles/file/appVersion";

    public static String LOGIN_URL = host + "clientLogin";//登陆
    public static String GETMAININFO_URL = host + "getIndex";//获取主页信息
    public static String GETTASKTABLE_URL = host + "getCheckMission";//获取任务表列表
    public static String GETTASKTIP_URL = host + "A10004.do";//获取任务操作规范
    public static String GETTASKITEM_URL = host + "getMissionDetail";//获取点检详情项
    public static String GETREPAIRSMISSIONDETAIL = host + "getRepairsMissionDetail";
    public static String GETREPAIRSMISSION_URL = host + "getRepairsMission";//获取维修任务
    public static String UPLOADRECORD_URL = host + "uploadMission";//上传任务记录
    public static String RESOLVEPROBLEM = host + "A10008.do";//任务维修添加操作记录
    public static String GETCOUNT_URL = host + "getCountData";//获取统计数据
    public static String GETHISORY_URL = host + "A100010.do";//获取历史信息
    public static String GETTEAM_URL = host + "getPar" +
            "tList";//获取组织列表
    public static String GETUSER_URl = host + "getPartUser";//获取用户列表
    public static String GETERRORLIST_URL = host + "getErrorHistory";//获取异常统计
    public static String GETPROJECT_URL = host + "getModuleList";//获取管理项
    public static String GETOPERATION_HOST = host + "A100014";//获取操作历史
    public static String GETARTICLEDETAIL = host + "getArticalDetail";
    public static String GETARTICLELIST = host + "getArticalList";
    public static String UPTABLLEID_URL = host + "upMissionTableId";
    public static String UPSCANCODE_URL = host + "upScanBarCode";
    public static String GETSELECTIONMISSION_URL = host + "getSelectionMission";
    public static String REPAIRS_URL = host + "repairs";
    public static String DEAL_MISSION = host + "dealRepairsMission";
    public static String GET_SELECTION_MISSION = host + "getSelectionMission";
    public static String UPLOAD_RANDOM_MISSION_URL = host + "upRandomMission";//上传随机抽查
    public static String GET_HISTORY_COUNT_URL = host + "getHistoryData";
    public static String CHANGE_PASSWORD_URL = host + "changePassword";
    public static String GETUPDATAFILE_URL = host + "getUpdataFile";
    public static String UID = "";


    //SharedPreferences
    public static SharedPreferences appSharedPreferences = null;
    public static final String sharedPreferencesName = "huizhong.xml";//配置存储文件
    public static String DATE = "date";//用来判断日期是否发生变化，如果发生变化则清除本地缓存
    public static String PHONE = "PHONE";
    public static String USERID = "USER_ID";
    public static String USERNAME = "USERNAME";
    public static String TEAMNAME = "TEAMNAME";
    public static String TEAMID = "TEAMID";

    public static String TASKTABNUM = "tasktabnumber";
    public static String ERRORTASKNUM = "errortasknumber";


    public static String NAME = "NAME";

    public static String TEAMSNAME = "TEAMSNAME";
    public static String WORKSHOPANDTEAM = "WORKSHOPANDTEAM";
    public static String TASK = "TASK";
    public static String KEY = "KEY";

    //sqlitedatabase

    public static final String DBNAME = "MS.db";//数据库名
    public static final String MissionTable = "missiontable";//任务表 数据表
    public static final String MissionDetailTable = "missiondetail";//任务详情 数据表
    public static final String RECORD = "record";//任务记录 数据表
    public static final String ERRORTABLE = "errorkind";//异常类别数据表
    public static final String MAINTAINRECORD = "maintainrecord";
}
