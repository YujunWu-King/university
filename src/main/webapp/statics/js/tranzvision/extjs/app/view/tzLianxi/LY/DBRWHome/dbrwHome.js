/**
 * Created by luyan on 2015/11/19.
 */

Ext.define('KitchenSink.view.tzLianxi.LY.DBRWHome.dbrwHome', {
    extend: 'Ext.grid.Panel',
    requires: [
        'Ext.data.*',
        'Ext.grid.*',
        'Ext.util.*',
        'Ext.toolbar.Paging',
        'Ext.ux.ProgressBarPager',
        'KitchenSink.view.tzLianxi.LY.DBRWHome.dbrwHomeController',
        'KitchenSink.view.tzLianxi.LY.DBRWHome.dbrwHomeModel',
        'KitchenSink.view.tzLianxi.LY.DBRWHome.dbrwHomeStore',
        'tranzvision.extension.grid.Exporter'
    ],
    xtype: 'dbrwHome',
    controller: 'dbrwHomeController',
    store:{
        type:'dbrwHomeStore'
    },
    columnLines: true,
    selModel: {
        type: 'checkboxmodel'
    },
    style:"margin:8px",
    multiSelect: true,
    title: '待办任务列表',
    viewConfig: {
        enableTextSelection: true
    },
    header:false,
    frame: true,
    initComponent: function () {

        this.title = "待办任务列表";

        //待办任务列表
        var store = new KitchenSink.view.tzLianxi.LY.DBRWHome.dbrwHomeStore();
        Ext.apply(this, {
            columns: [{
                text: '任务名称',
                dataIndex: 'taskName',
                width: 220
            },{
                text: '优先级',
                dataIndex: 'priority',
                minWidth: 50
            },{
                text: '步骤超时时间',
                dataIndex: 'stepTmOut',
                minWidth: 150
            },{
                text: '步骤超时',
                dataIndex: 'stepTmOutFlag',
                width: 80
            },{
                text: '流程超时时间',
                dataIndex: 'flowTmOut',
                minWidth: 150
            },{
                text:'流程超时',
                dataIndex: 'flowTmOutFlag',
                minWidth: 70
            },{
                text: '任务类型',
                dataIndex: 'taskType',
                minWidth: 80
            },{
                text: '任务产生人',
                dataIndex: 'taskCreateOprid',
                width: 100
            },{
                text: '产生日期',
                dataIndex: 'taskCreateDate',
                minWidth: 100
            },{
                text:'产生时间',
                dataIndex:'taskCreateTime',
                minWidth:100
            },{
                text: '处理时间',
                dataIndex: 'dealDttm',
                minWidth: 180
            },{
                text: '任务状态',
                dataIndex: 'taskStatus',
                minWidth: 80
            }
            ],
            store:store
        });

        this.callParent();
    }
});
