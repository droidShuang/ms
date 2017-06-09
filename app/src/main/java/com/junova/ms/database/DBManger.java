package com.junova.ms.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.junova.ms.app.App;
import com.junova.ms.app.AppConfig;
import com.junova.ms.model.TaskItemInfoModel;
import com.junova.ms.model.TaskRecordModel;
import com.junova.ms.model.MissionTableInfoModel;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by junova on 2016/9/29 0029.
 */

public class DBManger {

    private DBHelper helper;
    private SQLiteDatabase db;

    private DBManger(Context context) {
        helper = new DBHelper(context, AppConfig.DBNAME, null, 1);
    }

    private static class MangerHolder {
        public static DBManger manger = new DBManger(App.getContext());
    }

    public static DBManger getInstance() {
        return MangerHolder.manger;
    }


    //关闭数据库
    public void closeDB() {
        if (db != null && db.isOpen()) {
            db.close();
        }
    }

//    //写入任务表
//    public void writeTaskTable(List<MissionTableInfoModel> lists) {
//        db = helper.getWritableDatabase();
//
//        for (MissionTableInfoModel taskTableInfo :
//                lists) {
//            ContentValues contentValues = new ContentValues();
//            contentValues.put("title", taskTableInfo.getTITLE());
//            contentValues.put("date", taskTableInfo.getDATE());
//            contentValues.put("taskTableId", taskTableInfo.getTASKTABID());
//            contentValues.put("projectId", taskTableInfo.getPROJECTID());
//            contentValues.put("totalCount", taskTableInfo.getTOTALCOUNT());
//            contentValues.put("errorCount", "0");
//            contentValues.put("checkedCount", "0");
//            contentValues.put("lineCode", taskTableInfo.getLINECODE());
//            db.insert(AppConfig.MissionTable, null, contentValues);
//        }
//        //closeDB();
//    }
//
//    //获取tasktable list
//    public List<MissionTableInfoModel> readTaskTabkle() {
//        db = helper.getReadableDatabase();
//        List<MissionTableInfoModel> taskTables = new ArrayList<>();
//        Cursor cursor = db.rawQuery("select * from " + AppConfig.MissionTable, null, null);
//        while (cursor != null && cursor.moveToNext()) {
//
//            String title = cursor.getString(cursor.getColumnIndex("title"));
//            String date = cursor.getString(cursor.getColumnIndex("date"));
//            String taskTableId = cursor.getString(cursor.getColumnIndex("taskTableId"));
//            String projectId = cursor.getString(cursor.getColumnIndex("projectId"));
//            String totalCount = cursor.getString(cursor.getColumnIndex("totalCount"));
//            String errorCount = cursor.getString(cursor.getColumnIndex("errorCount"));
//            String checkedCount = cursor.getString(cursor.getColumnIndex("checkedCount"));
//            String lineCode = cursor.getString(cursor.getColumnIndex("lineCode"));
//            // TaskTableInfoModel taskTable = new TaskTableInfoModel(taskTableId,totalCount,date,projectId,lineCode,title);
//            MissionTableInfoModel taskTable = new MissionTableInfoModel(taskTableId, checkedCount, errorCount, lineCode, title, projectId, date, totalCount);
//            taskTables.add(taskTable);
//        }
//        if (cursor != null) {
//            cursor.close();
//        }
//        return taskTables;
//
//    }

    public int getUnCheckTaskTableCount() {
        db = helper.getReadableDatabase();
        int count = 0;
        Cursor cursor = db.rawQuery("select checkedCount from " + AppConfig.MissionTable, null, null);
        while (cursor != null && cursor.moveToNext()) {
            String strCount = cursor.getString(cursor.getColumnIndex("checkedCount"));
            int checkedCount = Integer.parseInt(strCount);
            if (checkedCount == 0) {
                count++;
            }
        }
        if (cursor != null) {
            cursor.close();
        }

        return count;
    }


    //更新指定tasktable的checkerCount已检查数，errorCount 检查异常数
    public void updateTaskTable(String taskTableId, String checkedCount, String errorCount) {
        db = helper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("checkedCount", checkedCount);
        contentValues.put("errorCount", errorCount);
        db.update(AppConfig.MissionTable, contentValues, "taskTableId=?", new String[]{taskTableId});
        closeDB();
    }

    //删除任务表
    public void deleteTaskTable() {
        db = helper.getWritableDatabase();
        db.execSQL("DELETE FROM " + AppConfig.MissionTable);
        closeDB();
    }

    //写入任务详情
    public void writeTaskItem(List<TaskItemInfoModel> list, String taskTableId, String projectId) {
        db = helper.getWritableDatabase();
        for (TaskItemInfoModel item :
                list) {
            ContentValues contentValues = new ContentValues();
            contentValues.put("title", item.getTITLE());
            contentValues.put("taskItemId", item.getITEMID());
            contentValues.put("taskTableId", taskTableId);
            db.insert(AppConfig.MissionDetailTable, null, contentValues);
        }
        for (TaskItemInfoModel.ErrorKind error :
                list.get(0).getERRORKINDLIST()) {
            ContentValues errorValuse = new ContentValues();
            errorValuse.put("projectId", projectId);
            errorValuse.put("errorName", error.getERRORNAME());
            errorValuse.put("errorKindId", error.getERRORID());
            db.insert(AppConfig.ERRORTABLE, null, errorValuse);
        }

        //   closeDB();
    }

    //读取任务详情
    public List<TaskItemInfoModel> readTaskItem(String taskTableId, String projectId) {
        db = helper.getReadableDatabase();
        List<TaskItemInfoModel> list = new ArrayList<>();
        List<TaskItemInfoModel.ErrorKind> errorKindList = new ArrayList<>();

        //   Cursor cursor=db.rawQuery("select * from "+AppConfig.MissionDetailTable +" where taskTableId ='"+taskTableId+"'",null);
        Cursor cursor = db.rawQuery("select title,taskItemId from " + AppConfig.MissionDetailTable + " where taskTableId='" + taskTableId + "'", null);
        Cursor errorCursor = db.rawQuery("select * from " + AppConfig.ERRORTABLE + " where projectId ='" + projectId + "'", null);
        while (errorCursor != null && errorCursor.moveToNext()) {
            String errorName = errorCursor.getString(errorCursor.getColumnIndex("errorName"));
            String errorKindId = errorCursor.getString(errorCursor.getColumnIndex("errorKindId"));
            TaskItemInfoModel.ErrorKind errorKind = new TaskItemInfoModel.ErrorKind();
            errorKind.setERRORID(errorKindId);
            errorKind.setERRORNAME(errorName);
            errorKindList.add(errorKind);
        }
        while (cursor != null && cursor.moveToNext()) {
            String title = cursor.getString(cursor.getColumnIndex("title"));
            String taskItemId = cursor.getString(cursor.getColumnIndex("taskItemId"));
            TaskItemInfoModel taskItemInfoModel = new TaskItemInfoModel();
            taskItemInfoModel.setTITLE(title);
            taskItemInfoModel.setITEMID(taskItemId);
            taskItemInfoModel.setERRORKINDLIST(errorKindList);
            list.add(taskItemInfoModel);
        }
        //     closeDB();
        if (cursor != null) {
            cursor.close();
        }
        if (errorCursor != null) {
            errorCursor.close();
        }

        return list;
    }

    //删除任务详情表
    public void deleteTaskItem() {
        db = helper.getWritableDatabase();
        db.execSQL("DELETE FROM " + AppConfig.MissionDetailTable);
        closeDB();
    }

    //写入异常
    public void writeError() {
        //      db = helper.getWritableDatabase();


    }

    //读取异常
    public void readError(String projectId) {

    }

    public void deleteError() {
        db = helper.getWritableDatabase();
        db.execSQL("DELETE FROM " + AppConfig.ERRORTABLE);
        //       closeDB();
    }

    //写入任务记录
    public void writeRecord(List<TaskRecordModel> list) {
        db = helper.getWritableDatabase();

        for (TaskRecordModel record :
                list) {

            //如果记录不存在，则插入记录，否则更新记录
            Cursor cursor = db.rawQuery("select taskItemId from " + AppConfig.RECORD + " where taskItemId = ? ", new String[]{record.getTaskItemId()}, null);

            if (cursor.getCount() == 0) {
                ContentValues contentValuse = new ContentValues();
                contentValuse.put("taskItemId", record.getTaskItemId());
                contentValuse.put("taskTableId", record.getTaskTableId());
                contentValuse.put("projectId", record.getProjectId());
                contentValuse.put("errorKindId", record.getErrorId());
                contentValuse.put("description", record.getDescription());
                contentValuse.put("date", record.getDate());
                contentValuse.put("status", record.getStatus());
                contentValuse.put("isUpload", record.getIsUpload());

                contentValuse.put("imagePath", record.getImagePath());
                contentValuse.put("errorName", record.getErrorName());

                db.insert(AppConfig.RECORD, null, contentValuse);
                Logger.d("写入任务记录 id " + record.getTaskItemId());
            } else {

                ContentValues contentValuse = new ContentValues();
                contentValuse.put("errorKindId", record.getErrorId());
                contentValuse.put("description", record.getDescription());
                contentValuse.put("date", record.getDate());
                contentValuse.put("status", record.getStatus());
                contentValuse.put("imagePath", record.getImagePath());
                contentValuse.put("errorName", record.getErrorName());
                db.update(AppConfig.RECORD, contentValuse, "taskItemId= ?", new String[]{record.getTaskItemId()});
                Logger.d("更新任务记录 id " + record.getTaskItemId());
            }
            cursor.close();

        }

    }

    //读取任务记录
    public TaskRecordModel readRecord(String taskItemId) {
        db = helper.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from " + AppConfig.RECORD + " where taskItemId = ? ", new String[]{taskItemId}, null);
        TaskRecordModel record = null;
        while (cursor != null && cursor.moveToNext()) {
            String taskTableId = cursor.getString(cursor.getColumnIndex("taskTableId"));
            String projectId = cursor.getString(cursor.getColumnIndex("projectId"));
            String errorKindId = cursor.getString(cursor.getColumnIndex("errorKindId"));
            String description = cursor.getString(cursor.getColumnIndex("description"));
            String date = cursor.getString(cursor.getColumnIndex("date"));
            String status = cursor.getString(cursor.getColumnIndex("status"));
            String isUpload = cursor.getString(cursor.getColumnIndex("isUpload"));
            String imagePath = cursor.getString(cursor.getColumnIndex("imagePath"));
            String errorName = cursor.getString(cursor.getColumnIndex("errorName"));
            record = new TaskRecordModel(taskItemId, errorName, taskTableId, errorKindId, projectId, description, date, status, imagePath, isUpload);
        }
        if (cursor != null) {
            cursor.close();
        }
        return record;
    }

    //读取任务记录list
    public List<TaskRecordModel> readRecordList() {
        db = helper.getReadableDatabase();
        Logger.d("开始读取");
        Cursor cursor = db.rawQuery("select * from " + AppConfig.RECORD, null, null);
        Logger.d("读取成功");
        List<TaskRecordModel> list = new ArrayList<>();
        while (cursor.moveToNext()) {
            String taskItemId = cursor.getString(cursor.getColumnIndex("taskItemId"));
            String taskTableId = cursor.getString(cursor.getColumnIndex("taskTableId"));
            String projectId = cursor.getString(cursor.getColumnIndex("projectId"));
            String errorKindId = cursor.getString(cursor.getColumnIndex("errorKindId"));
            String description = cursor.getString(cursor.getColumnIndex("description"));
            String date = cursor.getString(cursor.getColumnIndex("date"));
            String status = cursor.getString(cursor.getColumnIndex("status"));
            String isUpload = cursor.getString(cursor.getColumnIndex("isUpload"));
            String imagePath = cursor.getString(cursor.getColumnIndex("imagePath"));
            String errorName = cursor.getString(cursor.getColumnIndex("errorName"));
            TaskRecordModel record = new TaskRecordModel(taskItemId, errorName, taskTableId, errorKindId, projectId, description, date, status, imagePath, isUpload);
            list.add(record);
        }
        Logger.d("读取任务记录list size " + list.size());
        cursor.close();
        //   closeDB();
        return list;
    }

    public void deleteRecord() {
        db = helper.getWritableDatabase();
        db.execSQL("DELETE FROM " + AppConfig.RECORD);

        //    closeDB();
    }

    public void deleteAll() {
        db = helper.getWritableDatabase();
        db.execSQL("DELETE FROM " + AppConfig.RECORD);
        db.execSQL("DELETE FROM " + AppConfig.MissionTable);
        db.execSQL("DELETE FROM " + AppConfig.ERRORTABLE);
        db.execSQL("DELETE FROM " + AppConfig.MissionDetailTable);
        //       closeDB();
    }

    public boolean checkIfNeedUpload() {
        int checkedCount = 0;
        int tableCount = -1;
        db = helper.getReadableDatabase();
        Cursor cursor = db.rawQuery("select count(*) from " + AppConfig.MissionTable + " where checkedCount=totalCount", null);
        while (cursor != null && cursor.moveToNext()) {
            checkedCount = cursor.getInt(0);
        }
        Cursor tableCursor = db.rawQuery("select count(*) from " + AppConfig.MissionTable, null);
        while (tableCursor != null && tableCursor.moveToNext()) {
            tableCount = tableCursor.getInt(0);
        }
        Logger.d("checkedCount  " + checkedCount + "    tableCount  " + tableCount);
        if(cursor!=null){
            cursor.close();
        }
        if(tableCursor!=null){
            tableCursor.close();
        }

        return tableCount != 0 && checkedCount == tableCount;

    }

}
