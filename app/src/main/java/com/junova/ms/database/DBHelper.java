package com.junova.ms.database;

import android.app.ActionBar;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.junova.ms.app.AppConfig;

/**
 * Created by junova on 2016/9/28 0028.
 */

public class DBHelper extends SQLiteOpenHelper {
    private static String CREATE_MISSIONTABLE_TABLE = "CREATE TABLE IF NOT EXISTS " + AppConfig.MissionTable + "("
            + TableColumns.MissionTable.Mission_Id + " TEXT,"
            + TableColumns.MissionTable.Mission_Module_Id + " TEXT,"
            + TableColumns.MissionTable.Mission_Part_Id + " TEXT,"
            + TableColumns.MissionTable.Mission_Title + " TEXT,"
            + TableColumns.MissionTable.Mission_Kind + " TEXT,"
            + TableColumns.MissionTable.Mission_Start_Time + " TEXT,"
            + TableColumns.MissionTable.Mission_End_Time + " TEXT,"
            + TableColumns.MissionTable.Mission_Identify_Code + " TEXT,"
            + TableColumns.MissionTable.Mission_Time + " TEXT,"
            + TableColumns.MissionTable.Mission_ErrorCount + " TEXT,"
            + TableColumns.MissionTable.Mission_TotalCount + " TEXT,"
            + TableColumns.MissionTable.Mission_NoralCount + " TEXT,"
            + TableColumns.MissionTable.Mission_ISUP + " TEXT,"
            + TableColumns.MissionTable.Mission_Status + " TEXT"
            + ")";
    private static String CREATE_MISSIONDETAIL_TABLE = "CREATE TABLE IF NOT EXISTS " + AppConfig.MissionDetailTable + "("
            + TableColumns.MissionDetailTable.Detail_Id + " TEXT,"
            + TableColumns.MissionDetailTable.Detail_Parent_Id + " TEXT,"
            + TableColumns.MissionDetailTable.Detail_Title + " TEXT,"
            + TableColumns.MissionDetailTable.Detail_Is_Value + " TEXT,"
            + TableColumns.MissionDetailTable.Detail_Up_Value + " TEXT,"
            + TableColumns.MissionDetailTable.Detail_Down_Value + " TEXT,"
            + TableColumns.MissionDetailTable.Detail_Operation_Text + " TEXT,"
            + TableColumns.MissionDetailTable.Detail_Operation_Image + " TEXT,"
            + TableColumns.MissionDetailTable.Detail_Value + " TEXT,"
            + TableColumns.MissionDetailTable.Detail_Status + " TEXT,"
            + TableColumns.MissionDetailTable.Detail_Record_Id + " TEXT,"

            + TableColumns.MissionDetailTable.Detail_Is_Up + " TEXT"
            + ")";

    private static String CREATE_RECORD_TABLE = "CREATE TABLE IF NOT EXISTS " + AppConfig.RECORD + "("
            + TableColumns.RecordTable.Record_Detail_Id + " TEXT,"
            + TableColumns.RecordTable.Record_Table_Id + " TEXT,"
            + TableColumns.RecordTable.Record_Error_Id + " TEXT,"
            + TableColumns.RecordTable.Record_Error_Des + " TEXT,"
            + TableColumns.RecordTable.Record_Error_Image + " TEXT,"
            + TableColumns.RecordTable.Record_Status + " TEXT,"
            + TableColumns.RecordTable.Record_Time + " TEXT,"
            + TableColumns.RecordTable.Record_Factory_Id + " TEXT,"
            + TableColumns.RecordTable.Record_Factory_Name + " TEXT,"
            + TableColumns.RecordTable.Record_Section_Id + " TEXT,"
            + TableColumns.RecordTable.Record_Section_Name + " TEXT,"
            + TableColumns.RecordTable.Record_Shop_Id + " TEXT,"
            + TableColumns.RecordTable.Record_Shop_Name + " TEXT,"
            + TableColumns.RecordTable.Record_Class_Id + " TEXT,"
            + TableColumns.RecordTable.Record_Class_Name + " TEXT,"
            + TableColumns.RecordTable.Record_Part_Id + " TEXT,"
            + TableColumns.RecordTable.Record_To_User_Id + " TEXT,"
            + TableColumns.RecordTable.Record_Value + " TEXT ,"
            + TableColumns.RecordTable.Record_Id + " TEXT ,"
            + TableColumns.RecordTable.Record_TO_User_Name+ " TEXT ,"
            + TableColumns.RecordTable.Record_IS_Up + " TEXT"


            + ")";
    private static String CREATE_ERRORKIND_TABLE = "CREATE TABLE IF NOT EXISTS " + AppConfig.ERRORTABLE + "("
            + TableColumns.ErrorKindTable.Error_Id + " TEXT,"
            + TableColumns.ErrorKindTable.Error_Name + " TEXT,"
            + TableColumns.ErrorKindTable.Error_MissionTable_Id + " TEXT,"
            + TableColumns.ErrorKindTable.Error_Parent_Id + " TEXT"
            + ")";
    private static String CREATE_MAINTAIN_TABLE = "CREATE TABLE IF NOT EXISTS " + AppConfig.MAINTAINRECORD + "("
            + TableColumns.MaintainRecordTable.Maintain_Id + " TEXT, "
            + TableColumns.MaintainRecordTable.Maintain_Description + " TEXT, "
            + TableColumns.MaintainRecordTable.Maintain_End_Time + " TEXT, "
            + TableColumns.MaintainRecordTable.Maintain_Image + " TEXT, "
            + TableColumns.MaintainRecordTable.Maintain_Start_Time + " TEXT "
            + ")";

    public DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(CREATE_MISSIONTABLE_TABLE);
        db.execSQL(CREATE_MISSIONDETAIL_TABLE);
        db.execSQL(CREATE_RECORD_TABLE);
        db.execSQL(CREATE_ERRORKIND_TABLE);
        db.execSQL(CREATE_MAINTAIN_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
