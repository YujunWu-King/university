Ext.define('KitchenSink.view.callCenter.viewHisBmActWindow', {
    extend: 'Ext.window.Window',
    reference: 'viewHisBmActWindow',
    requires: [
        'Ext.data.*',
        'Ext.grid.*',
        'Ext.util.*',
        'Ext.toolbar.Paging',
        'Ext.ux.ProgressBarPager',
        'KitchenSink.view.callCenter.callActListStore'
    ],
    title: '参与活动列表',
    bodyStyle:'overflow-y:hidden;overflow-x:hidden;padding-top:10px',
    actType: 'update',
    width: 650,
    y:10,
    minWidth: 400,
    minHeight: 350,
    maxHeight: 460,
    resizable: true,
    modal:true,
    listeners:{
        resize: function(win){
            win.doLayout();
        }
    },
    viewConfig: {
        enableTextSelection: true
    },
    items: [{
        xtype: 'form',
        reference: 'historyActForm',
        layout: {
            type: 'vbox',
            align: 'stretch'
        },
        fieldDefaults: {
            msgTarget: 'side',
            labelStyle: 'font-weight:bold'
        },
        border: false,
        bodyPadding: 10,
        bodyStyle:'overflow-y:auto;overflow-x:hidden',

        fieldDefaults: {
            msgTarget: 'side',
            labelWidth: 100,
            labelStyle: 'font-weight:bold'
        },

        items: [{
            xtype: 'displayfield',
            fieldLabel: '主叫号码',
            name: 'callPhone',
            fieldStyle:'color:red;'
        },{
            xtype: 'displayfield',
            fieldLabel: '报名人',
            name: 'bmrName'
        },{
            xtype: 'displayfield',
            fieldLabel: '性别',
            name: 'bmrGender'
        }]
    },{
        xtype: 'grid',
        height: 'auto',
        //title: '历史来电记录',
        style:'margin-left:10px;margin-right:10px',
        autoHeight:true,
        minHeight:120,
        id:'historyActGrid',
        columnLines: true,
        reference: 'historyActGrid',
        store: {
            type: 'callActListStore'
        },
        columns: [{ 
            //text: '系统变量ID',
			text: '活动名称',
			draggable:false,//姓名不可拖动
            dataIndex: 'artName',
			flex:1
        },{
			text: '活动地点',
            sortable: false,				
            dataIndex: 'artAddr',
            width: 120
        },{
			text: '开始日期',
            sortable: true,				
            dataIndex: 'startDt',
            width: 90
        },{
			text: '开始时间',
            sortable: true,				
            dataIndex: 'startTime',
            width: 80
        },{
			text: '结束日期',
            sortable: true,				
            dataIndex: 'endDt',
            width: 90
        },{
			text: '结束时间',
            sortable: true,				
            dataIndex: 'endTime',
            width: 80
        }],
        bbar: {
            xtype: 'pagingtoolbar',
            pageSize: 5,
            reference: 'rolePlstToolBar',
            listeners:{
                afterrender: function(pbar){
                    var grid = pbar.findParentByType("grid");
                    pbar.setStore(grid.store);
                }
            },
            plugins: new Ext.ux.ProgressBarPager()
        }
    }],
    buttons: [{
        text: '关闭',
        iconCls:"close",
        handler: function(btn){
            //获取窗口
            var win = btn.findParentByType("window");
            var form = win.child("form").getForm();
            //关闭窗口
            win.close();
        }
    }]
});
