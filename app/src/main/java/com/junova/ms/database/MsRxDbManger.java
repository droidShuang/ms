package com.junova.ms.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.junova.ms.app.AppConfig;
import com.junova.ms.bean.Error;
import com.junova.ms.bean.MissionDetail;
import com.junova.ms.bean.MissionItem;
import com.junova.ms.bean.MissionTable;
import com.junova.ms.bean.Record;
import com.junova.ms.utils.CursorUtil;
import com.squareup.sqlbrite.BriteDatabase;
import com.squareup.sqlbrite.SqlBrite;

import org.reactivestreams.Publisher;

import java.util.ArrayList;
import java.util.List;

import hu.akarnokd.rxjava.interop.RxJavaInterop;
import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Function;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by junova on 2017-02-17.
 */

public class MsRxDbManger {
    public static MsRxDbManger instance = null;

    public BriteDatabase db = null;

    public MsRxDbManger(DBHelper dbHelper) {
        super();
        SqlBrite sqlBrite = new SqlBrite.Builder().build();
        db = sqlBrite.wrapDatabaseHelper(dbHelper, Schedulers.io());
    }

    public static synchronized MsRxDbManger getInstance(Context context) {
        if (instance == null) {
            instance = new MsRxDbManger(new DBHelper(context, AppConfig.DBNAME, null, 1));
        }
        return instance;
    }

    /**
     * @author 杨爽
     * @time 2017-02-17  13:39
     * @describe 插入单条
     * @version 1.0
     */
    public long writeMissionTable(MissionTable missionTable) {
        return db.insert(AppConfig.TASK, missionTableConvertToContentValues(missionTable));
    }

    /**
     * @author 杨爽
     * @time 2017-02-17  13:40
     * @describe 插入多条
     * @version 1.0
     */
    public void writeMissionTableList(List<MissionTable> missionTables) {
        for (MissionTable missionTable :
                missionTables) {
            writeMissionTable(missionTable);
        }
    }

    public void deleteMissionTable(String missionTableId) {
        db.delete(AppConfig.MissionTable, "", "");
    }

    public void deleteMissionTableList(List<String> missionTableIdList) {
        for (String missionTableId :
                missionTableIdList) {
            deleteMissionTable(missionTableId);
        }
    }


    /**
     * @author 杨爽
     * @time 2017-02-17  13:40
     * @describe MissionTable实体类转ContentValues
     * @version 1.0
     */
    private ContentValues missionTableConvertToContentValues(MissionTable missionTable) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("title", missionTable.getTitle());
        contentValues.put("missionId", missionTable.getMissionTableId());
        contentValues.put("time", missionTable.getTime());
        contentValues.put("startTime", missionTable.getStartTime());
        contentValues.put("endTime", missionTable.getEndTime());
        contentValues.put("moduleId", missionTable.getModuleId());
        contentValues.put("userId", missionTable.getPartId());
        contentValues.put("kind", missionTable.getKind());
        return contentValues;
    }

    /**
     * @author 杨爽
     * @time 2017-02-17  13:49
     * @describe currsor转MissionTable实体类
     * @version 1.0
     */
    private MissionTable cursorConvertToMissionTable(Cursor cursor) {
        return new MissionTable(CursorUtil.getStringOfColumn(cursor, "title"),
                CursorUtil.getStringOfColumn(cursor, "missionId"),
                CursorUtil.getStringOfColumn(cursor, "time"),
                CursorUtil.getStringOfColumn(cursor, "startTime"),
                CursorUtil.getStringOfColumn(cursor, "endTime"),
                CursorUtil.getStringOfColumn(cursor, "userId"),
                CursorUtil.getStringOfColumn(cursor, "moduleId"),
                CursorUtil.getStringOfColumn(cursor, "kind"),
                CursorUtil.getStringOfColumn(cursor, "identifyingCode"),
                CursorUtil.getStringOfColumn(cursor, "totalCount"),
                CursorUtil.getStringOfColumn(cursor, "errorCount"),
                CursorUtil.getStringOfColumn(cursor, "normalCount")

        );
    }

    public Flowable<List<MissionTable>> getMissionTables() {
        return RxJavaInterop.toV2Flowable(db.createQuery(AppConfig.MissionTable, "SELECT * FROM " + AppConfig.MissionTable).mapToList(new Func1<Cursor, MissionTable>() {
            @Override
            public MissionTable call(Cursor cursor) {
                return cursorConvertToMissionTable(cursor);
            }
        }));
//        .flatMap(new Func1<List<MissionTable>, Observable<List<MissionTable>>>() {
//            @Override
//            public Observable<List<MissionTable>> call(final List<MissionTable> missionTables) {
//                return Observable.create(new Observable.OnSubscribe<List<MissionTable>>() {
//                    @Override
//                    public void call(Subscriber<? super List<MissionTable>> subscriber) {
////                    subscriber.onNext(missionTables);
//                        if (missionTables.isEmpty()) {
//                            subscriber.onCompleted();
//                            Logger.d("数据库为空");
//                        } else {
//                            subscriber.onNext(missionTables);
//                            Logger.d("从数据库中获取");
//                        }
//                    }
//                });
//            }
//        })

    }

    /**
     * @author 杨爽
     * @time 2017-02-17  14:21
     * @describe MissionDetail实体类转ContentValues
     * @version 1.0
     */
    private ContentValues missionDetailConvertToContentValue(MissionDetail missionDetail) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("missionDetailId", missionDetail.getMissionDetailId());
        contentValues.put("missionTableId", missionDetail.getMissionDetailId());
        contentValues.put("title", missionDetail.getTitle());
        contentValues.put("isValue", missionDetail.getIsValue());
        contentValues.put("upValue", missionDetail.getUpValue());
        contentValues.put("downValue", missionDetail.getDownValue());
        contentValues.put("operationTipText", missionDetail.getOperationTipText());
        contentValues.put("operationTipImage", "");
        return contentValues;
    }

    public Flowable<List<MissionItem>> getMissionItemList() {
        List<String> tableList = new ArrayList<>();
        tableList.add("a");
        return RxJavaInterop.toV2Flowable(db.createQuery(tableList, "s", "s").mapToList(new Func1<Cursor, MissionItem>() {
            @Override
            public MissionItem call(Cursor cursor) {
                return cursurConvertToMissionItem(cursor);
            }
        }));
    }

    private MissionItem cursurConvertToMissionItem(Cursor cursor) {
        return new MissionItem(CursorUtil.getStringOfColumn(cursor, "missionDetailId"),
                CursorUtil.getStringOfColumn(cursor, "time"),
                CursorUtil.getStringOfColumn(cursor, "missionTableId"),
                CursorUtil.getStringOfColumn(cursor, "isValue"),
                CursorUtil.getStringOfColumn(cursor, "upValue"),
                CursorUtil.getStringOfColumn(cursor, "downValue"),
                CursorUtil.getStringOfColumn(cursor, "operationTipText"),
                CursorUtil.getStringOfColumn(cursor, "operationTipImage"),
                CursorUtil.getStringOfColumn(cursor, "status"),
                CursorUtil.getStringOfColumn(cursor, "errorDes"),
                CursorUtil.getStringOfColumn(cursor, "errorImage"),
                CursorUtil.getStringOfColumn(cursor, "errorId"),
                CursorUtil.getStringOfColumn(cursor, "title"));
    }

    /**
     * @author 杨爽
     * @time 2017-02-17  14:22
     * @describe cursur转MissionDetail实体类
     * @version 1.0
     */
    private MissionDetail cursurConvertToMissionDetail(Cursor cursor) {
        return new MissionDetail();
    }

    /**
     * @author 杨爽
     * @time 2017-02-17  14:22
     * @describe 插入单条MissionDetail
     * @version 1.0
     */
    public long writeMissionDetail(MissionDetail missionDetail) {
        return db.insert(AppConfig.MissionDetailTable, missionDetailConvertToContentValue(missionDetail));
    }

    public Flowable<Integer> countMissionDetail(final String missionTableId) {
        return Flowable.create(new FlowableOnSubscribe<Integer>() {
            @Override
            public void subscribe(FlowableEmitter<Integer> e) throws Exception {
                Cursor cursor = db.query("select count(*) from " + AppConfig.MissionDetailTable + "  where missionTableId = ?", missionTableId);
                e.onNext(cursor.getCount());
            }
        }, BackpressureStrategy.DROP);

    }

    /**
     * @author 杨爽
     * @time 2017-02-17  14:23
     * @describe 插入missionDetailList
     * @version 1.0
     */
    public boolean writeMissionDetailList(List<MissionDetail> missionDetailList) {
        long rowId = 0;
        for (MissionDetail missionDetail :
                missionDetailList) {
            rowId = writeMissionDetail(missionDetail);
        }
        return rowId > 0;
    }

    public Flowable<List<MissionDetail>> getMissionDetails() {

        return RxJavaInterop.toV2Flowable(db.createQuery(AppConfig.MissionDetailTable, "SELECT * FROM " + AppConfig.MissionDetailTable).mapToList(new Func1<Cursor, MissionDetail>() {
            @Override
            public MissionDetail call(Cursor cursor) {
                return cursurConvertToMissionDetail(cursor);
            }
        })).flatMap(new Function<List<MissionDetail>, Publisher<List<MissionDetail>>>() {
            @Override
            public Publisher<List<MissionDetail>> apply(@NonNull List<MissionDetail> missionDetails) throws Exception {
                return null;
            }
        });
    }

    public void deleteMissionDetailList(String missionTableId) {
        db.delete(AppConfig.TASK, "", "");
    }


    /**
     * @author 杨爽
     * @time 2017-02-17  14:56
     * @describe Record实体类转ContentValues
     * @version 1.0
     */
    private ContentValues recordConvertToContentValues(Record record) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("missionDetailId", record.getMissionItemId());
        contentValues.put("missionTableId", record.getMissionTableId());
        contentValues.put("status", record.getStatus());
        contentValues.put("errorDes", record.getErrorDes());
        contentValues.put("errorImage", record.getErrorImage());
        contentValues.put("errorId", record.getErrorId());
        contentValues.put("time", record.getTime());
        return contentValues;
    }

    /**
     * @author 杨爽
     * @time 2017-02-17  14:56
     * @describe cursor转record实体类
     * @version 1.0
     */
    private Record cursorContvertToRecord(Cursor cursor) {
        return new Record(CursorUtil.getStringOfColumn(cursor, "missionDetailId"),
                CursorUtil.getStringOfColumn(cursor, "missionTableId"),
                CursorUtil.getIntOfColum(cursor, TableColumns.RecordTable.Record_Status),
                CursorUtil.getStringOfColumn(cursor, "errorDes"),
                CursorUtil.getStringOfColumn(cursor, "errorImage"),
                CursorUtil.getStringOfColumn(cursor, "errorId"),
                CursorUtil.getStringOfColumn(cursor, "time"),"",
                CursorUtil.getStringOfColumn(cursor,"isUp"),CursorUtil.getStringOfColumn(cursor, TableColumns.RecordTable.Record_Id));
    }

    /**
     * @author 杨爽
     * @time 2017-02-17  14:57
     * @describe 写入单个record
     * @version 1.0
     */
    private long writeRecord(Record record) {
        return db.insert(AppConfig.RECORD, recordConvertToContentValues(record));
    }

    /**
     * @author 杨爽
     * @time 2017-02-17  14:57
     * @describe 写入recordlist
     * @version 1.0
     */

    public void writeRecordList(List<Record> recordList) {
        for (Record record :
                recordList) {
            writeRecord(record);
        }
    }

    public Flowable<Integer> countErrorRecord(final String missionTableId, final String status) {
        return Flowable.create(new FlowableOnSubscribe<Integer>() {
            @Override
            public void subscribe(FlowableEmitter<Integer> e) throws Exception {
                Cursor cursor = db.query("select count(*) from " + AppConfig.RECORD + "  where missionTableId = ? and status = ? ", missionTableId, status);
                e.onNext(cursor.getCount());
            }
        }, BackpressureStrategy.DROP);

    }

    public void updataRecord(Record record) {
        db.update(AppConfig.RECORD, null, "", "");
    }


    public Flowable<List<Record>> getRecordList() {
        return RxJavaInterop.toV2Flowable(db.createQuery(AppConfig.RECORD, "SELECT * FROM " + AppConfig.RECORD).mapToList(new Func1<Cursor, Record>() {
            @Override
            public Record call(Cursor cursor) {
                return cursorContvertToRecord(cursor);
            }
        }));

    }

    public void deleteRecord(String missionTableId) {
        db.delete("", "");
    }

    public ContentValues errorConvertToContentValues(Error error) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("errorId", error.getErrorId());
        contentValues.put("missionTableId", error.getMissionTableId());
        contentValues.put("missionDetailId", error.getMissionDetailId());
        contentValues.put("errorName", error.getErrorName());
        return contentValues;
    }

    public Error cursurConvertError(Cursor cursor) {
        return new Error(CursorUtil.getStringOfColumn(cursor, "errorId"),
                CursorUtil.getStringOfColumn(cursor, "errorName"),
                CursorUtil.getStringOfColumn(cursor, "missionTableId"),
                CursorUtil.getStringOfColumn(cursor, "missionDetailId"));
    }

    public long writeError(Error error) {
        return db.insert(AppConfig.ERRORTABLE, errorConvertToContentValues(error));
    }

    public void deleteError(String MissionTableId) {
        db.delete("", "");
    }

    public void writeErrorList(List<Error> errorList) {
        for (Error error :
                errorList) {
            writeError(error);
        }
    }

    public Flowable<List<Error>> getErrorList() {

        return RxJavaInterop.toV2Flowable(db.createQuery(AppConfig.ERRORTABLE, "SELECT * FROM " + AppConfig.ERRORTABLE).mapToList(new Func1<Cursor, Error>() {
            @Override
            public Error call(Cursor cursor) {
                return cursurConvertError(cursor);
            }
        }));
    }

    /**
     * @param missionTableId missionTableId 任务表id
     * @author 杨爽
     * @time 2017-02-21  13:46
     * @describe 计算任务表的总任务数，以及检查异常数
     * @version 1.0
     */
    public Flowable<String> countTotalAndErrorMissionDetail(String missionTableId) {
        return Flowable.zip(countMissionDetail(missionTableId), countErrorRecord(missionTableId, ""), new BiFunction<Integer, Integer, String>() {
            @Override
            public String apply(@NonNull Integer integer, @NonNull Integer integer2) throws Exception {

                return integer + "/" + integer2 + "/" + (integer - integer2);
            }
        });
    }


}
