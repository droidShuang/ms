package com.junova.ms.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.junova.ms.app.App;
import com.junova.ms.app.AppConfig;
import com.junova.ms.bean.Error;
import com.junova.ms.bean.MaintainRecord;
import com.junova.ms.bean.MissionDetail;
import com.junova.ms.bean.MissionTable;
import com.junova.ms.bean.Record;
import com.junova.ms.utils.CursorUtil;
import com.junova.ms.utils.DateUtil;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;

/**
 * Created by junova on 2017-02-17.
 */

public class MsDbManger {
    private DBHelper dbHelper;
    public static MsDbManger instance = null;
    private SQLiteDatabase db;

    public MsDbManger(Context context) {
        super();
        dbHelper = new DBHelper(context, AppConfig.DBNAME, null, 1);
    }

    public static synchronized MsDbManger getInstance() {
        return getInstance(App.getContext());
    }

    public static synchronized MsDbManger getInstance(Context context) {
        if (instance == null) {
            instance = new MsDbManger(context);
        }
        return instance;
    }

    public Flowable<List<MissionTable>> getMissionTable() {

        return Flowable.create(new FlowableOnSubscribe<List<MissionTable>>() {
            @Override
            public void subscribe(FlowableEmitter<List<MissionTable>> e) throws Exception {
                List<MissionTable> missionTables = selectMissionTable();
                if (missionTables.isEmpty()) {
                    e.onComplete();
                } else {
                    e.onNext(missionTables);
                }
            }
        }, BackpressureStrategy.BUFFER);
    }

    public Flowable<List<MissionDetail>> getMissionDetail(final String missionTableId) {
        return Flowable.create(new FlowableOnSubscribe<List<MissionDetail>>() {
            @Override
            public void subscribe(FlowableEmitter<List<MissionDetail>> e) throws Exception {
                List<MissionDetail> missionDetails = selectMissionDetailId(missionTableId);

                if (missionDetails.isEmpty()) {
                    e.onComplete();
                    Logger.d("getMissionDetail flowable onComplete");
                } else {
                    e.onNext(missionDetails);
                    Logger.d("getMissionDetail flowable onNext");
                }
            }
        }, BackpressureStrategy.BUFFER);
    }

    public Flowable<Record> getMissionRecord(final String missionDetailId) {
        return Flowable.create(new FlowableOnSubscribe<Record>() {
            @Override
            public void subscribe(FlowableEmitter<Record> e) throws Exception {
                Record record = selectRecord(missionDetailId);
                if (record == null) {
                    e.onComplete();
                } else {
                    e.onNext(record);

                }
            }
        }, BackpressureStrategy.BUFFER);
    }

    public Flowable<Record> getSportRecord(final String missionDetailId) {
        return Flowable.create(new FlowableOnSubscribe<Record>() {
            @Override
            public void subscribe(FlowableEmitter<Record> e) throws Exception {
                Record record = selectSportRecord(missionDetailId);
                if (record == null) {
                    e.onComplete();
                } else {
                    e.onNext(record);

                }
            }
        }, BackpressureStrategy.BUFFER);
    }


    public Flowable<List<Record>> getMissionRecords(final String missionTableId) {
        return Flowable.create(new FlowableOnSubscribe<List<Record>>() {
            @Override
            public void subscribe(FlowableEmitter<List<Record>> e) throws Exception {
                List<Record> records = slectRecords(missionTableId);
                if (records.isEmpty()) {
                    e.onComplete();
                } else {
                    e.onNext(records);
                }
            }
        }, BackpressureStrategy.DROP);
    }

    public Flowable<List<Record>> getSportRecords(final String missionTableId) {
        return Flowable.create(new FlowableOnSubscribe<List<Record>>() {
            @Override
            public void subscribe(FlowableEmitter<List<Record>> e) throws Exception {
                List<Record> records = selectSportRecords(missionTableId);
                if (records.isEmpty()) {
                    e.onComplete();
                } else {
                    e.onNext(records);
                }
            }
        }, BackpressureStrategy.DROP);
    }

    public Flowable<List<Record>> getMissionRecordsByStatus(final String status) {
        return Flowable.create(new FlowableOnSubscribe<List<Record>>() {
            @Override
            public void subscribe(FlowableEmitter<List<Record>> e) throws Exception {
                List<Record> records = selectRecordsByStatus(status);
                if (records.isEmpty()) {
                    e.onError(new Throwable("当前没有任何任务需要提交"));
                } else {
                    e.onNext(records);
                    e.onComplete();
                }
            }
        }, BackpressureStrategy.DROP);
    }

    public Flowable<List<Record>> getSportRecordsByStatus(final String status) {
        return Flowable.create(new FlowableOnSubscribe<List<Record>>() {
            @Override
            public void subscribe(FlowableEmitter<List<Record>> e) throws Exception {
                List<Record> records = selectRecordsByStatus(status);
                if (records.isEmpty()) {
                    e.onComplete();
                } else {
                    e.onNext(records);
                    e.onComplete();
                }
            }
        }, BackpressureStrategy.DROP);
    }

    public Flowable<MaintainRecord> getMaintainRecord(final String maintainId) {
        return Flowable.create(new FlowableOnSubscribe<MaintainRecord>() {
            @Override
            public void subscribe(FlowableEmitter<MaintainRecord> e) throws Exception {
                db = dbHelper.getReadableDatabase();
                Cursor cursor = db.rawQuery("select * from " + AppConfig.MAINTAINRECORD + " where " + TableColumns.MaintainRecordTable.Maintain_Id + " = ?", new String[]{maintainId});
                if (cursor != null && cursor.moveToNext()) {
                    MaintainRecord record = new MaintainRecord(CursorUtil.getStringOfColumn(cursor, TableColumns.MaintainRecordTable.Maintain_Id),
                            CursorUtil.getStringOfColumn(cursor, TableColumns.MaintainRecordTable.Maintain_Image),
                            CursorUtil.getStringOfColumn(cursor, TableColumns.MaintainRecordTable.Maintain_Start_Time),
                            CursorUtil.getStringOfColumn(cursor, TableColumns.MaintainRecordTable.Maintain_End_Time),
                            CursorUtil.getStringOfColumn(cursor, TableColumns.MaintainRecordTable.Maintain_Description));
                    e.onNext(record);
                    e.onComplete();
                } else {
                    e.onComplete();
                }
            }
        }, BackpressureStrategy.DROP);

    }

    public List<Record> selectRecordsByStatus(String status) {
        db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from " + AppConfig.RECORD + " where " + TableColumns.RecordTable.Record_Status + "!= ?", new String[]{status});
        List<Record> recordList = new ArrayList<>();
        while (cursor != null && cursor.moveToNext()) {
            Record record = new Record(
                    CursorUtil.getStringOfColumn(cursor, TableColumns.RecordTable.Record_Detail_Id),
                    CursorUtil.getStringOfColumn(cursor, TableColumns.RecordTable.Record_Table_Id),
                    CursorUtil.getIntOfColum(cursor, TableColumns.RecordTable.Record_Status),
                    CursorUtil.getStringOfColumn(cursor, TableColumns.RecordTable.Record_Error_Des),
                    CursorUtil.getStringOfColumn(cursor, TableColumns.RecordTable.Record_Error_Image),
                    CursorUtil.getStringOfColumn(cursor, TableColumns.RecordTable.Record_Error_Id),
                    CursorUtil.getStringOfColumn(cursor, TableColumns.RecordTable.Record_Time),
                    CursorUtil.getStringOfColumn(cursor, TableColumns.RecordTable.Record_Value),
                    CursorUtil.getStringOfColumn(cursor, TableColumns.RecordTable.Record_IS_Up),
                    CursorUtil.getStringOfColumn(cursor, TableColumns.RecordTable.Record_Id));
            recordList.add(record);
        }
        cursor.close();
        closeDB();
        return recordList;
    }

    public List<Record> selectSportRecordsByStatus(String status) {
        db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from " + AppConfig.RECORD + " where " + TableColumns.RecordTable.Record_Status + "!= ?", new String[]{status});
        List<Record> recordList = new ArrayList<>();
        while (cursor != null && cursor.moveToNext()) {
            Record record = new Record(
                    CursorUtil.getStringOfColumn(cursor, TableColumns.RecordTable.Record_Detail_Id),
                    CursorUtil.getStringOfColumn(cursor, TableColumns.RecordTable.Record_Table_Id),
                    CursorUtil.getIntOfColum(cursor, TableColumns.RecordTable.Record_Status),
                    CursorUtil.getStringOfColumn(cursor, TableColumns.RecordTable.Record_Error_Des),
                    CursorUtil.getStringOfColumn(cursor, TableColumns.RecordTable.Record_Error_Image),
                    CursorUtil.getStringOfColumn(cursor, TableColumns.RecordTable.Record_Error_Id),
                    CursorUtil.getStringOfColumn(cursor, TableColumns.RecordTable.Record_Time),
                    CursorUtil.getStringOfColumn(cursor, TableColumns.RecordTable.Record_Value),
                    CursorUtil.getStringOfColumn(cursor, TableColumns.RecordTable.Record_Factory_Id),
                    CursorUtil.getStringOfColumn(cursor, TableColumns.RecordTable.Record_Factory_Name),
                    CursorUtil.getStringOfColumn(cursor, TableColumns.RecordTable.Record_Section_Id),
                    CursorUtil.getStringOfColumn(cursor, TableColumns.RecordTable.Record_Section_Name),
                    CursorUtil.getStringOfColumn(cursor, TableColumns.RecordTable.Record_Shop_Id),
                    CursorUtil.getStringOfColumn(cursor, TableColumns.RecordTable.Record_Shop_Name),
                    CursorUtil.getStringOfColumn(cursor, TableColumns.RecordTable.Record_Class_Id),
                    CursorUtil.getStringOfColumn(cursor, TableColumns.RecordTable.Record_Class_Name),
                    CursorUtil.getStringOfColumn(cursor, TableColumns.RecordTable.Record_Part_Id),
                    CursorUtil.getStringOfColumn(cursor, TableColumns.RecordTable.Record_To_User_Id),
                    CursorUtil.getStringOfColumn(cursor, TableColumns.RecordTable.Record_TO_User_Name),
                    CursorUtil.getStringOfColumn(cursor, TableColumns.RecordTable.Record_IS_Up));
        }
        cursor.close();
        closeDB();
        return recordList;
    }

    public List<Record> slectRecords(String missionTableId) {
        db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from " + AppConfig.RECORD + " where " + TableColumns.RecordTable.Record_Table_Id + " = ?", new String[]{missionTableId});
        List<Record> recordList = new ArrayList<>();
        while (cursor != null && cursor.moveToNext()) {
            Record record = new Record(
                    CursorUtil.getStringOfColumn(cursor, TableColumns.RecordTable.Record_Detail_Id),
                    CursorUtil.getStringOfColumn(cursor, TableColumns.RecordTable.Record_Table_Id),
                    CursorUtil.getIntOfColum(cursor, TableColumns.RecordTable.Record_Status),
                    CursorUtil.getStringOfColumn(cursor, TableColumns.RecordTable.Record_Error_Des),
                    CursorUtil.getStringOfColumn(cursor, TableColumns.RecordTable.Record_Error_Image),
                    CursorUtil.getStringOfColumn(cursor, TableColumns.RecordTable.Record_Error_Id),
                    CursorUtil.getStringOfColumn(cursor, TableColumns.RecordTable.Record_Time),
                    CursorUtil.getStringOfColumn(cursor, TableColumns.RecordTable.Record_Value),
                    CursorUtil.getStringOfColumn(cursor, TableColumns.RecordTable.Record_IS_Up),
                    CursorUtil.getStringOfColumn(cursor, TableColumns.RecordTable.Record_Id));
            recordList.add(record);
        }
        cursor.close();
        closeDB();
        return recordList;
    }

    public List<Record> selectSportRecords(String missionTableId) {
        db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from " + AppConfig.RECORD + " where " + TableColumns.RecordTable.Record_Table_Id + " = ?", new String[]{missionTableId});
        List<Record> recordList = new ArrayList<>();
        while (cursor != null && cursor.moveToNext()) {
            Record record = new Record(
                    CursorUtil.getStringOfColumn(cursor, TableColumns.RecordTable.Record_Detail_Id),
                    CursorUtil.getStringOfColumn(cursor, TableColumns.RecordTable.Record_Table_Id),
                    CursorUtil.getIntOfColum(cursor, TableColumns.RecordTable.Record_Status),
                    CursorUtil.getStringOfColumn(cursor, TableColumns.RecordTable.Record_Error_Des),
                    CursorUtil.getStringOfColumn(cursor, TableColumns.RecordTable.Record_Error_Image),
                    CursorUtil.getStringOfColumn(cursor, TableColumns.RecordTable.Record_Error_Id),
                    CursorUtil.getStringOfColumn(cursor, TableColumns.RecordTable.Record_Time),
                    CursorUtil.getStringOfColumn(cursor, TableColumns.RecordTable.Record_Value),
                    CursorUtil.getStringOfColumn(cursor, TableColumns.RecordTable.Record_Factory_Id),
                    CursorUtil.getStringOfColumn(cursor, TableColumns.RecordTable.Record_Factory_Name),
                    CursorUtil.getStringOfColumn(cursor, TableColumns.RecordTable.Record_Section_Id),
                    CursorUtil.getStringOfColumn(cursor, TableColumns.RecordTable.Record_Section_Name),
                    CursorUtil.getStringOfColumn(cursor, TableColumns.RecordTable.Record_Shop_Id),
                    CursorUtil.getStringOfColumn(cursor, TableColumns.RecordTable.Record_Shop_Name),
                    CursorUtil.getStringOfColumn(cursor, TableColumns.RecordTable.Record_Class_Id),
                    CursorUtil.getStringOfColumn(cursor, TableColumns.RecordTable.Record_Class_Name),
                    CursorUtil.getStringOfColumn(cursor, TableColumns.RecordTable.Record_Part_Id),
                    CursorUtil.getStringOfColumn(cursor, TableColumns.RecordTable.Record_To_User_Id),
                    CursorUtil.getStringOfColumn(cursor, TableColumns.RecordTable.Record_TO_User_Name),
                    CursorUtil.getStringOfColumn(cursor, TableColumns.RecordTable.Record_IS_Up));
            recordList.add(record);
        }
        cursor.close();
        closeDB();
        return recordList;
    }

    public List<Error> selectError(String missionTableId) {
        db = dbHelper.getReadableDatabase();
        List<Error> errors = new ArrayList<>();
        Cursor errorCursor = db.rawQuery("SELECT * FROM " + AppConfig.ERRORTABLE + " where missionTableId = ?", new String[]{missionTableId});
        while (errorCursor != null && errorCursor.moveToNext()) {
            Error error = new Error(CursorUtil.getStringOfColumn(errorCursor, TableColumns.ErrorKindTable.Error_Name),
                    CursorUtil.getStringOfColumn(errorCursor, TableColumns.ErrorKindTable.Error_Id),
                    CursorUtil.getStringOfColumn(errorCursor, TableColumns.ErrorKindTable.Error_MissionTable_Id),
                    CursorUtil.getStringOfColumn(errorCursor, TableColumns.ErrorKindTable.Error_Parent_Id));
            errors.add(error);
        }
        errorCursor.close();
        closeDB();
        return errors;
    }

    public Record selectRecord(String missionDetailId) {
        db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from " + AppConfig.RECORD + " where " + TableColumns.RecordTable.Record_Detail_Id + " = ?", new String[]{missionDetailId});

        if (cursor != null && cursor.moveToNext()) {
            Record record = new Record(
                    CursorUtil.getStringOfColumn(cursor, TableColumns.RecordTable.Record_Detail_Id),
                    CursorUtil.getStringOfColumn(cursor, TableColumns.RecordTable.Record_Table_Id),
                    CursorUtil.getIntOfColum(cursor, TableColumns.RecordTable.Record_Status),
                    CursorUtil.getStringOfColumn(cursor, TableColumns.RecordTable.Record_Error_Des),
                    CursorUtil.getStringOfColumn(cursor, TableColumns.RecordTable.Record_Error_Image),
                    CursorUtil.getStringOfColumn(cursor, TableColumns.RecordTable.Record_Error_Id),
                    CursorUtil.getStringOfColumn(cursor, TableColumns.RecordTable.Record_Time),
                    CursorUtil.getStringOfColumn(cursor, TableColumns.RecordTable.Record_Value),
                    CursorUtil.getStringOfColumn(cursor, TableColumns.RecordTable.Record_Factory_Id),
                    CursorUtil.getStringOfColumn(cursor, TableColumns.RecordTable.Record_Factory_Name),
                    CursorUtil.getStringOfColumn(cursor, TableColumns.RecordTable.Record_Section_Id),
                    CursorUtil.getStringOfColumn(cursor, TableColumns.RecordTable.Record_Section_Name),
                    CursorUtil.getStringOfColumn(cursor, TableColumns.RecordTable.Record_Shop_Id),
                    CursorUtil.getStringOfColumn(cursor, TableColumns.RecordTable.Record_Shop_Name),
                    CursorUtil.getStringOfColumn(cursor, TableColumns.RecordTable.Record_Class_Id),
                    CursorUtil.getStringOfColumn(cursor, TableColumns.RecordTable.Record_Class_Name),
                    CursorUtil.getStringOfColumn(cursor, TableColumns.RecordTable.Record_Part_Id),
                    CursorUtil.getStringOfColumn(cursor, TableColumns.RecordTable.Record_To_User_Id),
                    CursorUtil.getStringOfColumn(cursor, TableColumns.RecordTable.Record_TO_User_Name),
                    CursorUtil.getStringOfColumn(cursor, TableColumns.RecordTable.Record_IS_Up));
            cursor.close();
            closeDB();
            return record;
        }
        cursor.close();
        closeDB();

        return null;
    }

    public Record selectSportRecord(String missionDetailId) {
        Cursor cursor = db.rawQuery("select * from " + AppConfig.RECORD + " where " + TableColumns.RecordTable.Record_Detail_Id + " = ?", new String[]{missionDetailId});

        if (cursor != null && cursor.moveToNext()) {
            Record record = new Record(
                    CursorUtil.getStringOfColumn(cursor, TableColumns.RecordTable.Record_Detail_Id),
                    CursorUtil.getStringOfColumn(cursor, TableColumns.RecordTable.Record_Table_Id),
                    CursorUtil.getIntOfColum(cursor, TableColumns.RecordTable.Record_Status),
                    CursorUtil.getStringOfColumn(cursor, TableColumns.RecordTable.Record_Error_Des),
                    CursorUtil.getStringOfColumn(cursor, TableColumns.RecordTable.Record_Error_Image),
                    CursorUtil.getStringOfColumn(cursor, TableColumns.RecordTable.Record_Error_Id),
                    CursorUtil.getStringOfColumn(cursor, TableColumns.RecordTable.Record_Time),
                    CursorUtil.getStringOfColumn(cursor, TableColumns.RecordTable.Record_Value),
                    CursorUtil.getStringOfColumn(cursor, TableColumns.RecordTable.Record_Factory_Id),
                    CursorUtil.getStringOfColumn(cursor, TableColumns.RecordTable.Record_Factory_Name),
                    CursorUtil.getStringOfColumn(cursor, TableColumns.RecordTable.Record_Section_Id),
                    CursorUtil.getStringOfColumn(cursor, TableColumns.RecordTable.Record_Section_Name),
                    CursorUtil.getStringOfColumn(cursor, TableColumns.RecordTable.Record_Shop_Id),
                    CursorUtil.getStringOfColumn(cursor, TableColumns.RecordTable.Record_Shop_Name),
                    CursorUtil.getStringOfColumn(cursor, TableColumns.RecordTable.Record_Class_Id),
                    CursorUtil.getStringOfColumn(cursor, TableColumns.RecordTable.Record_Class_Name),
                    CursorUtil.getStringOfColumn(cursor, TableColumns.RecordTable.Record_Part_Id),
                    CursorUtil.getStringOfColumn(cursor, TableColumns.RecordTable.Record_To_User_Id),
                    CursorUtil.getStringOfColumn(cursor, TableColumns.RecordTable.Record_TO_User_Name),
                    CursorUtil.getStringOfColumn(cursor, TableColumns.RecordTable.Record_IS_Up));
            cursor.close();
            closeDB();
            return record;
        }
        cursor.close();
        closeDB();
        return null;
    }


    //RxJavaInterop.toV2Flowable(db.createQuery(AppConfig.MissionTable, "SELECT * FROM " + AppConfig.MissionTable).mapToList(new Func1<Cursor, MissionTable>() {
    public List<MissionTable> selectMissionTable() {
        db = dbHelper.getReadableDatabase();
        List<MissionTable> missionTables = new ArrayList<>();
        Cursor cursor = db.rawQuery("SELECT * FROM " + AppConfig.MissionTable, new String[]{});
        while (cursor != null && cursor.moveToNext()) {
            MissionTable table = new MissionTable(CursorUtil.getStringOfColumn(cursor, "title"),
                    CursorUtil.getStringOfColumn(cursor, "missionId"),
                    CursorUtil.getStringOfColumn(cursor, "time"),
                    CursorUtil.getStringOfColumn(cursor, "startTime"),
                    CursorUtil.getStringOfColumn(cursor, "endTime"),
                    CursorUtil.getStringOfColumn(cursor, "partId"),
                    CursorUtil.getStringOfColumn(cursor, "moduleId"),
                    CursorUtil.getStringOfColumn(cursor, "kind"),
                    CursorUtil.getStringOfColumn(cursor, "identifyingCode"),
                    CursorUtil.getStringOfColumn(cursor, "totalCount"),
                    CursorUtil.getStringOfColumn(cursor, "errorCount"),
                    CursorUtil.getStringOfColumn(cursor, "normalCount"),
                    CursorUtil.getStringOfColumn(cursor, "status"));
            missionTables.add(table);

        }
        cursor.close();
        closeDB();
        return missionTables;
    }

    public List<MissionDetail> selectMissionDetailId(String missionTableId) {
        db = dbHelper.getReadableDatabase();
        List<MissionDetail> missionDetails = new ArrayList<>();

        Cursor cursor = db.rawQuery("SELECT * FROM " + AppConfig.MissionDetailTable + " where missionTableId = ?", new String[]{missionTableId});


        while (cursor != null && cursor.moveToNext()) {
            List<String> urls = new ArrayList<>();
            List<Error> errors = new ArrayList<>();
            String missionDetailId = CursorUtil.getStringOfColumn(cursor, TableColumns.MissionDetailTable.Detail_Id);
            for (String url :
                    CursorUtil.getStringOfColumn(cursor, TableColumns.MissionDetailTable.Detail_Operation_Image).split(";")) {
                urls.add(url);
            }

            MissionDetail missionDetail = new MissionDetail(CursorUtil.getIntOfColum(cursor, TableColumns.MissionDetailTable.Detail_Down_Value),
                    CursorUtil.getIntOfColum(cursor, TableColumns.MissionDetailTable.Detail_Is_Value),
                    missionDetailId,
                    CursorUtil.getStringOfColumn(cursor, TableColumns.MissionDetailTable.Detail_Operation_Text),
                    CursorUtil.getIntOfColum(cursor, TableColumns.MissionDetailTable.Detail_Status),
                    CursorUtil.getStringOfColumn(cursor, TableColumns.MissionDetailTable.Detail_Title),
                    CursorUtil.getIntOfColum(cursor, TableColumns.MissionDetailTable.Detail_Up_Value),
                    CursorUtil.getStringOfColumn(cursor, TableColumns.MissionDetailTable.Detail_Value),
                    urls,
                    CursorUtil.getStringOfColumn(cursor, TableColumns.MissionDetailTable.Detail_Record_Id),
                    CursorUtil.getStringOfColumn(cursor, TableColumns.MissionDetailTable.Detail_Is_Up));

            missionDetails.add(missionDetail);
        }
        Logger.d("get missionDetail list form db");
        cursor.close();
        closeDB();
        return missionDetails;
    }

    public void writeMaintainRecord(MaintainRecord record) {
        db = dbHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(TableColumns.MaintainRecordTable.Maintain_Description, record.getDescribletion());
        contentValues.put(TableColumns.MaintainRecordTable.Maintain_End_Time, record.getEndTime());
        contentValues.put(TableColumns.MaintainRecordTable.Maintain_Start_Time, record.getStartTime());
        String urls = "";
        for (String url : record.getMaintainImage().split(";")
                ) {
            urls = urls + url + ";";
        }
        contentValues.put(TableColumns.MaintainRecordTable.Maintain_Image, urls);
        contentValues.put(TableColumns.MaintainRecordTable.Maintain_Id, record.getMaintainId());
        db.insert(AppConfig.MAINTAINRECORD, null, contentValues);
        closeDB();

    }

    public void writeErrorKind(String missionTableId, List<Error> errorList) {
        //error
        db = dbHelper.getWritableDatabase();

        for (Error error :
                errorList) {
            ContentValues errorContent = new ContentValues();
            errorContent.put(TableColumns.ErrorKindTable.Error_Id, error.getErrorId());
            errorContent.put(TableColumns.ErrorKindTable.Error_Name, error.getErrorName());
            errorContent.put(TableColumns.ErrorKindTable.Error_MissionTable_Id, missionTableId);
            db.insert(AppConfig.ERRORTABLE, null, errorContent);
        }
        closeDB();

    }

    public void writeMissionDetail(List<MissionDetail> missionDetails, String missionTableId, boolean verfice) {
        db = dbHelper.getWritableDatabase();

        for (MissionDetail missionDetail :
                missionDetails) {
            ContentValues contentValues = new ContentValues();
            contentValues.put(TableColumns.MissionDetailTable.Detail_Id, missionDetail.getMissionDetailId());
            contentValues.put(TableColumns.MissionDetailTable.Detail_Value, missionDetail.getValue());
            contentValues.put(TableColumns.MissionDetailTable.Detail_Down_Value, missionDetail.getDownValue());
            contentValues.put(TableColumns.MissionDetailTable.Detail_Is_Value, missionDetail.getIsValue());
            contentValues.put(TableColumns.MissionDetailTable.Detail_Up_Value, missionDetail.getUpValue());
            contentValues.put(TableColumns.MissionDetailTable.Detail_Operation_Text, missionDetail.getOperationTipText());
            contentValues.put(TableColumns.MissionDetailTable.Detail_Status, missionDetail.getStatus());
            if (missionDetail.getStatus() == 3) {
                if (verfice) {
                    contentValues.put(TableColumns.MissionDetailTable.Detail_Status, 2);
                } else {
                    contentValues.put(TableColumns.MissionDetailTable.Detail_Status, 0);
                }
            } else {
                contentValues.put(TableColumns.MissionDetailTable.Detail_Status, missionDetail.getStatus());
            }
            contentValues.put(TableColumns.MissionDetailTable.Detail_Title, missionDetail.getTitle());
            contentValues.put(TableColumns.MissionDetailTable.Detail_Parent_Id, missionTableId);
            if (missionDetail.getStatus() == 3) {
                contentValues.put(TableColumns.MissionDetailTable.Detail_Is_Up, "0");
            } else {
                contentValues.put(TableColumns.MissionDetailTable.Detail_Is_Up, missionDetail.getStatus());
            }

            contentValues.put(TableColumns.MissionDetailTable.Detail_Record_Id, missionDetail.getRecordId());
            //record
            ContentValues recordContentValue = new ContentValues();
            recordContentValue.put(TableColumns.RecordTable.Record_Id, missionDetail.getRecordId());
            recordContentValue.put(TableColumns.RecordTable.Record_Detail_Id, missionDetail.getMissionDetailId());
            recordContentValue.put(TableColumns.RecordTable.Record_Table_Id, missionTableId);
            recordContentValue.put(TableColumns.RecordTable.Record_Time, DateUtil.getTimestamp());
            if (missionDetail.getStatus() != 3) {
                recordContentValue.put(TableColumns.RecordTable.Record_Status, "2");
            } else {
                if (verfice) {
                    recordContentValue.put(TableColumns.RecordTable.Record_Status, "2");
                } else {
                    recordContentValue.put(TableColumns.RecordTable.Record_Status, "0");
                }
            }

            db.insert(AppConfig.RECORD, null, recordContentValue);

            String urls = "";
            for (String url : missionDetail.getOperationTipPic()
                    ) {
                urls = urls + url + ";";
            }
            contentValues.put(TableColumns.MissionDetailTable.Detail_Operation_Image, urls);
            db.insert(AppConfig.MissionDetailTable, null, contentValues);
        }
        closeDB();
        Logger.d("write mission detail");
    }

    public void writeMissionRecord(List<Record> recordList) {
        db = dbHelper.getWritableDatabase();
        for (Record record :
                recordList) {
            ContentValues contentValues = new ContentValues();
            contentValues.put(TableColumns.RecordTable.Record_Detail_Id, record.getMissionItemId());
            contentValues.put(TableColumns.RecordTable.Record_Error_Des, record.getErrorDes());
            contentValues.put(TableColumns.RecordTable.Record_Error_Id, record.getErrorId());
            contentValues.put(TableColumns.RecordTable.Record_Error_Image, record.getErrorImage());
            contentValues.put(TableColumns.RecordTable.Record_Status, record.getStatus());
            contentValues.put(TableColumns.RecordTable.Record_Table_Id, record.getMissionTableId());
            contentValues.put(TableColumns.RecordTable.Record_Time, record.getTime());
            contentValues.put(TableColumns.RecordTable.Record_Value, record.getValue());
            contentValues.put(TableColumns.MissionTable.Mission_ISUP, "1");
            db.insert(AppConfig.RECORD, null, contentValues);
        }
        closeDB();
    }

    public void deleteAll() {
        db = dbHelper.getWritableDatabase();


        db.execSQL("delete from " + AppConfig.MissionTable);

        db.execSQL("delete from " + AppConfig.RECORD);
        db.execSQL("delete from " + AppConfig.MissionDetailTable);
        db.execSQL("delete from " + AppConfig.ERRORTABLE);
        db.execSQL("delete from " + AppConfig.MAINTAINRECORD);
        db.close();
    }

    public void writeMissionTable(List<MissionTable> missionTables) {
        db = dbHelper.getWritableDatabase();

        //	db.execSQL("delete from " + AppConfig.DEVICE + " where userId=" + userId);
        //    db.delete(AppConfig.CHECK_ITEM, "userId=?", new String[]{userId});
        db.execSQL("delete from " + AppConfig.MissionTable);

        for (MissionTable missionTable :
                missionTables) {
            ContentValues contentValues = new ContentValues();
            contentValues.put(TableColumns.MissionTable.Mission_Title, missionTable.getTitle());
            contentValues.put(TableColumns.MissionTable.Mission_Id, missionTable.getMissionTableId());
            contentValues.put(TableColumns.MissionTable.Mission_Time, missionTable.getTime());
            contentValues.put(TableColumns.MissionTable.Mission_Start_Time, missionTable.getStartTime());
            contentValues.put(TableColumns.MissionTable.Mission_End_Time, missionTable.getEndTime());
            contentValues.put(TableColumns.MissionTable.Mission_Part_Id, missionTable.getPartId());
            contentValues.put(TableColumns.MissionTable.Mission_Module_Id, missionTable.getModuleId());
            contentValues.put(TableColumns.MissionTable.Mission_Identify_Code, missionTable.getIdentifyingCode());
            contentValues.put(TableColumns.MissionTable.Mission_TotalCount, missionTable.getTotalCount());
            contentValues.put(TableColumns.MissionTable.Mission_ErrorCount, missionTable.getErrorCount());
            contentValues.put(TableColumns.MissionTable.Mission_NoralCount, missionTable.getNormalCount());
            contentValues.put(TableColumns.MissionTable.Mission_Kind, missionTable.getKind());
            contentValues.put(TableColumns.MissionTable.Mission_Status, missionTable.getStatus());
            contentValues.put(TableColumns.MissionTable.Mission_ISUP, "1");
            db.insert(AppConfig.MissionTable, null, contentValues);
        }
        closeDB();

    }

    public void writeMissionRecord(Record record) {
        db = dbHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(TableColumns.RecordTable.Record_Detail_Id, record.getMissionItemId());
        contentValues.put(TableColumns.RecordTable.Record_Error_Des, record.getErrorDes());
        contentValues.put(TableColumns.RecordTable.Record_Error_Id, record.getErrorId());
        contentValues.put(TableColumns.RecordTable.Record_Error_Image, record.getErrorImage());
        contentValues.put(TableColumns.RecordTable.Record_Status, record.getStatus());
        contentValues.put(TableColumns.RecordTable.Record_Table_Id, record.getMissionTableId());
        contentValues.put(TableColumns.RecordTable.Record_Time, record.getTime());
        contentValues.put(TableColumns.RecordTable.Record_Value, record.getValue());
        contentValues.put(TableColumns.RecordTable.Record_IS_Up, "1");
        db.insert(AppConfig.RECORD, null, contentValues);
        closeDB();
    }

    public void writeSportMissionRecord(Record record) {
        db = dbHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(TableColumns.RecordTable.Record_Detail_Id, record.getMissionItemId());
        contentValues.put(TableColumns.RecordTable.Record_Error_Des, record.getErrorDes());
        contentValues.put(TableColumns.RecordTable.Record_Error_Id, record.getErrorId());
        contentValues.put(TableColumns.RecordTable.Record_Error_Image, record.getErrorImage());
        contentValues.put(TableColumns.RecordTable.Record_Status, record.getStatus());
        contentValues.put(TableColumns.RecordTable.Record_Table_Id, record.getMissionTableId());
        contentValues.put(TableColumns.RecordTable.Record_Time, record.getTime());
        contentValues.put(TableColumns.RecordTable.Record_Value, record.getValue());
        contentValues.put(TableColumns.RecordTable.Record_Factory_Id, record.getMissionTableId());
        contentValues.put(TableColumns.RecordTable.Record_Factory_Name, record.getTime());
        contentValues.put(TableColumns.RecordTable.Record_Section_Id, record.getValue());
        contentValues.put(TableColumns.RecordTable.Record_Section_Name, record.getMissionTableId());
        contentValues.put(TableColumns.RecordTable.Record_Shop_Id, record.getTime());
        contentValues.put(TableColumns.RecordTable.Record_Shop_Name, record.getValue());
        contentValues.put(TableColumns.RecordTable.Record_Class_Id, record.getMissionTableId());
        contentValues.put(TableColumns.RecordTable.Record_Class_Name, record.getTime());
        contentValues.put(TableColumns.RecordTable.Record_Part_Id, record.getValue());
        db.insert(AppConfig.RECORD, null, contentValues);
        closeDB();
    }

    public void updataSportMissionRecord(String missionDetailId, Record record) {
        db = dbHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(TableColumns.RecordTable.Record_Error_Des, record.getErrorDes());
        contentValues.put(TableColumns.RecordTable.Record_Error_Id, record.getErrorId());
        contentValues.put(TableColumns.RecordTable.Record_Error_Image, record.getErrorImage());
        contentValues.put(TableColumns.RecordTable.Record_Status, record.getStatus());
        contentValues.put(TableColumns.RecordTable.Record_Time, record.getTime());
        contentValues.put(TableColumns.RecordTable.Record_Value, record.getValue());
        contentValues.put(TableColumns.RecordTable.Record_Factory_Id, record.getMissionTableId());
        contentValues.put(TableColumns.RecordTable.Record_Factory_Name, record.getTime());
        contentValues.put(TableColumns.RecordTable.Record_Section_Id, record.getValue());
        contentValues.put(TableColumns.RecordTable.Record_Section_Name, record.getMissionTableId());
        contentValues.put(TableColumns.RecordTable.Record_Shop_Id, record.getTime());
        contentValues.put(TableColumns.RecordTable.Record_Shop_Name, record.getValue());
        contentValues.put(TableColumns.RecordTable.Record_Class_Id, record.getMissionTableId());
        contentValues.put(TableColumns.RecordTable.Record_Class_Name, record.getTime());
        contentValues.put(TableColumns.RecordTable.Record_Part_Id, record.getValue());
        db.update(AppConfig.RECORD, contentValues, TableColumns.RecordTable.Record_Detail_Id + " = ?", new String[]{missionDetailId});
    }

    public void updataRecordUploadSuccess() {
        db = dbHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(TableColumns.RecordTable.Record_Status, "2");
        db.update(AppConfig.RECORD, contentValues, TableColumns.RecordTable.Record_Status + "!= ?", new String[]{"2"});
        ContentValues detailontentValues = new ContentValues();
        detailontentValues.put(TableColumns.MissionDetailTable.Detail_Is_Up, 0);

        db.update(AppConfig.MissionDetailTable, detailontentValues, TableColumns.MissionDetailTable.Detail_Is_Up + "!=?", new String[]{"0"});
        closeDB();
    }

    public void updataRecord(String missionTableId, String status) {
        db = dbHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(TableColumns.RecordTable.Record_Status, status);
        db.update(AppConfig.RECORD, contentValues, TableColumns.RecordTable.Record_Table_Id + " = ?", new String[]{missionTableId});
        ContentValues tableContentValues = new ContentValues();
        tableContentValues.put(TableColumns.MissionDetailTable.Detail_Is_Up, 0);
        tableContentValues.put(TableColumns.MissionDetailTable.Detail_Status, status);
        db.update(AppConfig.MissionDetailTable, tableContentValues, TableColumns.MissionDetailTable.Detail_Parent_Id + "=?", new String[]{missionTableId});
        closeDB();
    }

    public void updataRecord(String missionDetailId, Record record) {
        db = dbHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(TableColumns.RecordTable.Record_Error_Des, record.getErrorDes());
        contentValues.put(TableColumns.RecordTable.Record_Error_Id, record.getErrorId());
        contentValues.put(TableColumns.RecordTable.Record_Error_Image, record.getErrorImage());
        contentValues.put(TableColumns.RecordTable.Record_Status, record.getStatus());
        contentValues.put(TableColumns.RecordTable.Record_Time, record.getTime());
        contentValues.put(TableColumns.RecordTable.Record_Value, record.getValue());

        contentValues.put(TableColumns.RecordTable.Record_Factory_Name, record.getFactoryName());
        contentValues.put(TableColumns.RecordTable.Record_Factory_Id, record.getFactoryId());
        contentValues.put(TableColumns.RecordTable.Record_Shop_Name, record.getShopName());
        contentValues.put(TableColumns.RecordTable.Record_Shop_Id, record.getShopId());
        contentValues.put(TableColumns.RecordTable.Record_Section_Id, record.getSectionId());
        contentValues.put(TableColumns.RecordTable.Record_Section_Name, record.getSectionName());
        contentValues.put(TableColumns.RecordTable.Record_Class_Id, record.getClassId());
        contentValues.put(TableColumns.RecordTable.Record_Class_Name, record.getClassName());
        contentValues.put(TableColumns.RecordTable.Record_To_User_Id, record.getToUserId());
        contentValues.put(TableColumns.RecordTable.Record_TO_User_Name, record.getToUserName());


        db.update(AppConfig.RECORD, contentValues, TableColumns.RecordTable.Record_Detail_Id + " = ?", new String[]{missionDetailId});
        closeDB();
    }

    public void updataMissionDetail(String missionDetailId, int status, String value) {
        db = dbHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(TableColumns.MissionDetailTable.Detail_Status, status);
        contentValues.put(TableColumns.MissionDetailTable.Detail_Value, value);
        db.update(AppConfig.MissionDetailTable, contentValues, TableColumns.MissionDetailTable.Detail_Id + "= ?", new String[]{missionDetailId});
        closeDB();
    }

    public void updataMissionDetailAfterUpload(String missionTableId, String isUp) {
        db = dbHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(TableColumns.MissionDetailTable.Detail_Is_Up, isUp);
        db.update(AppConfig.MissionDetailTable, contentValues, TableColumns.MissionDetailTable.Detail_Parent_Id + " = ?", new String[]{missionTableId});
        closeDB();
    }

    public void updataMissionDetails(String missionTableId, int status) {
        db = dbHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(TableColumns.MissionDetailTable.Detail_Status, status);
        db.update(AppConfig.MissionDetailTable, contentValues, TableColumns.MissionDetailTable.Detail_Parent_Id + " = ?", new String[]{missionTableId});
        closeDB();
    }


    //关闭数据库
    public void closeDB() {
        if (db != null && db.isOpen()) {
            db.close();
        }
    }
}
