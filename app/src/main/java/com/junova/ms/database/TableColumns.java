package com.junova.ms.database;

/**
 * Created by junova on 2017-02-16.
 */

public final class TableColumns {
    //任务表列
    interface MissionTable {
        String Mission_Title = "title";//标题
        String Mission_Id = "missionId";//任务表Id
        String Mission_Time = "time";//下发时间
        String Mission_Start_Time = "startTime";//任务开始执行时间
        String Mission_End_Time = "endTime";//任务结束时间
        String Mission_Module_Id = "moduleId";//所属模块id
        String Mission_Part_Id = "partId";//所属部门id
        String Mission_Kind = "kind";//任务类型，每日/月度
        String Mission_Identify_Code = "identifyingCode";//条形验证码
        String Mission_TotalCount = "totalCount";
        String Mission_ErrorCount = "errorCount";
        String Mission_NoralCount = "normalCount";
        String Mission_Status = "status";
        String Mission_ISUP = "isUp";//0已上传1未上传
    }

    interface MissionDetailTable {
        String Detail_Id = "missionDetailId";//检测项id
        String Detail_Parent_Id = "missionTableId";//所属任务表id
        String Detail_Title = "title";//检测项标题
        String Detail_Is_Value = "isValue";//是否为数值检测
        String Detail_Up_Value = "upValue";//数值上限
        String Detail_Down_Value = "downValue";//数值下限
        String Detail_Operation_Text = "operationTipText";//文字操作提示
        String Detail_Operation_Image = "operationTipImage";//图片操作提示
        String Detail_Value = "value";
        String Detail_Status = "status";
        String Detail_Is_Up = "isUp";//0已上传1未上传
        String Detail_Record_Id = "recordId";
    }

    interface RecordTable {
        String Record_Detail_Id = "missionDetailId";//检测项id
        String Record_Table_Id = "missionTableId";//任务表id
        String Record_Status = "status";//状态 0 正常 1 异常 2 已上传或者需验证
        String Record_Error_Des = "errorDes";//异常描述
        String Record_Error_Image = "errorImage";//异常图片
        String Record_Error_Id = "errorId";//异常类型id
        String Record_Time = "time";//检查时间
        String Record_Factory_Id = "factoryId";
        String Record_Factory_Name = "facotryName";
        String Record_Section_Id = "sectionId";
        String Record_Section_Name = "sectionName";
        String Record_Shop_Id = "shopId";
        String Record_Shop_Name = "shopName";
        String Record_Class_Id = "classId";
        String Record_Class_Name = "className";
        String Record_Part_Id = "partId";
        String Record_Value = "value";
        String Record_To_User_Id = "toUserId";
        String Record_TO_User_Name = "toUserName";
        String Record_IS_Up = "isUp";
        String Record_Id = "recordId";
    }

    interface ErrorKindTable {
        String Error_MissionTable_Id = "missionTableId";
        String Error_Name = "errorName";//异常名称
        String Error_Id = "errorId";//异常类型id
        String Error_Parent_Id = "missionDetailId";//检测项id
    }

    interface MaintainRecordTable {
        String Maintain_Image = "maintainImage";
        String Maintain_Start_Time = "startTime";
        String Maintain_End_Time = "endTime";
        String Maintain_Description = "description";
        String Maintain_Id = "maintainId";
    }


}
