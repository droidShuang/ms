package com.junova.ms.api;

import com.junova.ms.bean.Alarm;
import com.junova.ms.database.DBManger;
import com.junova.ms.model.TaskItemInfoModel;
import com.junova.ms.model.TaskRecordModel;
import com.junova.ms.model.MissionTableInfoModel;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;

/**
 * Created by rider on 2016/7/8 0008 13:57.
 * Description :读取数据库缓存
 */
public class CacheApi {
//    public static Flowable<List<MissionTableInfoModel>> getTaskTable() {
//        return Flowable.create(new FlowableOnSubscribe<List<MissionTableInfoModel>>() {
//            @Override
//            public void subscribe(FlowableEmitter<List<MissionTableInfoModel>> e) throws Exception {
//                List<MissionTableInfoModel> list = DBManger.getInstance().readTaskTabkle();
//
//                if (list.size() == 0) {
//                    e.onComplete();
//                } else {
//                    Logger.d("TaskTableInfoModel    缓存");
//                    e.onNext(list);
//                }
//            }
//        }, BackpressureStrategy.BUFFER);
//    }

    public static Flowable<List<TaskItemInfoModel>> getTask(final String taskTableId, final String projectId) {
        return Flowable.create(new FlowableOnSubscribe<List<TaskItemInfoModel>>() {
            @Override
            public void subscribe(FlowableEmitter<List<TaskItemInfoModel>> e) throws Exception {
                List<TaskItemInfoModel> list = DBManger.getInstance().readTaskItem(taskTableId, projectId);
                if (list.size() == 0) {
                    e.onComplete();
                } else {
                    e.onNext(list);
                }
            }
        }, BackpressureStrategy.BUFFER);
    }

    public static Flowable<List<Alarm>> getAlarms(String args) {
        List<Alarm> lsit = new ArrayList<>();
        return Flowable.just(lsit);
    }

    public static Flowable<TaskRecordModel> getRecord(String taskItemId) {
        return Flowable.just(DBManger.getInstance().readRecord(taskItemId));
    }
}
