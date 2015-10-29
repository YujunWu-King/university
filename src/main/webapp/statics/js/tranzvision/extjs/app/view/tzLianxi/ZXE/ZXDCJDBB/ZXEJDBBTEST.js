/**
 * Created by carmen on 2015/9/9.
 */
Ext.define('KitchenSink.view.tzLianxi.ZXE.ZXDCJDBB.ZXEJDBBTEST', {
    extend: 'Ext.grid.Panel',
   // xtype: 'jdbbInfo',
    // store: 'wcztStore',
    controller: 'jdbb',
    requires: [
        'Ext.data.*',
        'Ext.grid.*',
        'Ext.util.*',
        'Ext.toolbar.Paging',
        'Ext.ux.ProgressBarPager',
        'KitchenSink.view.tzLianxi.ZXE.ZXDCJDBB.wcztStore',
        'KitchenSink.view.tzLianxi.ZXE.ZXDCJDBB.weekStore',
        'KitchenSink.view.tzLianxi.ZXE.ZXDCJDBB.jdbbController',
        'Ext.chart.series.Column'
    ],
    initComponent:function(){
        var datas=  ['WJ01','未完成'];
            Ext.apply(this, {
                columns: [{
                    text: '规则编号',
                    dataIndex: 'onlinedcId',
                    width:100
                },{
                    text: '校验规则名称',
                    dataIndex: 'jygzName',
                    minWidth:140
                },{
                    text: '操作',
                    menuDisabled: true,
                    sortable: false,
                    width:60,
                    xtype: 'actioncolumn',
                    items:[
                        {iconCls: 'edit',tooltip: '编辑',handler:'getJDBBData'}

                    ]
                }],
                store: {
                    fields: [ 'onlinedcId', 'jygzName'],

                        data:datas,


                    autoLoad:true

                }

            });

            this.callParent();
        },
    title: '进度报表',
    bodyStyle:'overflow-y:auto;overflow-x:hidden',
    buttons: [ {
        text: '关闭',
        iconCls:"close",
        handler: 'getJDBBData'
    }]
    });