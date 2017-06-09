package com.junova.ms.statistics;

import com.junova.ms.base.BaseActivity;

public class ErrorActivity extends BaseActivity {
//    @Bind(R.id.error_bt_choseDate)
//    Button btDate;
//    @Bind(R.id.error_rv_error)
//    RecyclerView rvError;
//    String strStartDate="";
//    String strEndDate="";
//
//    ErrorAdapter adapter;
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_error);
//        ButterKnife.bind(this);
//        initTitlebar();
//        Date date = new Date();
//        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
//        String strDate = format.format(date);
//
//        adapter = new ErrorAdapter();
//        strStartDate = strDate+" 00:00:00";
//        strEndDate=strDate+" 23:59:59";
//        rvError.setLayoutManager(new LinearLayoutManager(this));
//        rvError.setAdapter(adapter);
//        getErrorList();
//    }
//    void getErrorList(){
//        HashMap<String,String> params = new HashMap<>();
//        params.put("startDate",strStartDate);
//        params.put("endDate",strEndDate);
//        Logger.d(params);
//        MsApi.getInstance().getErrorHistory(params).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<List<ErrorHistoryModel>>() {
//            @Override
//            public void call(List<ErrorHistoryModel> errorHistories) {
//                    adapter.addList(errorHistories);
//            }
//        }, new Action1<Throwable>() {
//            @Override
//            public void call(Throwable throwable) {
//
//            }
//        });
//    }
//
//    @OnClick(R.id.error_bt_choseDate)
//    void choseDate() {
//        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
//        View v = View.inflate(this, R.layout.dialog_choose_date, null);
//
//
//        final DatePicker startPicker = (DatePicker) v.findViewById(R.id.dialog_choosedate_dpstart);
//        final DatePicker endPicker = (DatePicker) v.findViewById(R.id.dialog_choosedate_dpend);
//        Boolean a = startPicker == null;
//
//
//        startPicker.setCalendarViewShown(false);
//        endPicker.setCalendarViewShown(false);
//        builder.setView(v);
//
//
//        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
//                Date startDate = new Date(startPicker.getYear()-1900,startPicker.getMonth(),startPicker.getDayOfMonth());
//                Date endDate = new Date(endPicker.getYear()-1900,endPicker.getMonth(),endPicker.getDayOfMonth());
//                strStartDate=dateFormat.format(startDate);
//                strEndDate=dateFormat.format(endDate);
//                strStartDate = strStartDate+" 00:00:00";
//                strEndDate=strEndDate+" 23:59:59";
//                Logger.d("start "+strStartDate+"        end "+strEndDate);
//                getErrorList();
//                dialog.cancel();
//            }
//        });
//        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                dialog.cancel();
//            }
//        });
//        AlertDialog dialog = builder.create();
//        dialog.show();
//    }
//    void initTitlebar(){
//        titleBar.setTitle("异常统计");
//        titleBar.setLeftImageResource(R.drawable.back_white);
//        titleBar.setLeftText("返回");
//        titleBar.setLeftTextColor(Color.WHITE);
//        titleBar.setLeftClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                finish();
//            }
//        });
//        titleBar.setActionTextColor(Color.WHITE);
////        titleBar.addAction(new TitleBar.TextAction("提交") {
////            @Override
////            public void performAction(View view) {
////                Toast.makeText(MissionTableActivity.this, "点击了发布", Toast.LENGTH_SHORT).show();
////                uploadRecord();
////            }
////        });
//    }

}
