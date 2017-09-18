Ext.define('KitchenSink.view.callCenter.viewHistoryCallWindow', {
    extend: 'Ext.window.Window',
    reference: 'historyCallWindow',
    requires: [
        'Ext.data.*',
        'Ext.grid.*',
        'Ext.util.*',
        'Ext.toolbar.Paging',
        'Ext.ux.ProgressBarPager',
        'KitchenSink.view.callCenter.callHistoryStore'
    ],
    title: '历史来电记录',
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
        reference: 'historyCallForm',
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
        id:'historyCallGrid',
        columnLines: true,
        reference: 'historyCallGrid',
        store: {
            type: 'callHistoryStore'
        },
        columns: [{ 
            //text: '系统变量ID',
			text: '系统编号',
			draggable:false,//姓名不可拖动
            dataIndex: 'receiveId',
			width: 80
        },{
			text: '来电号码',
            sortable: true,				
            dataIndex: 'callPhone',
            width: 100
        },{
			text: '来电时间',
            sortable: true,				
            dataIndex: 'callDTime',
            width: 140
        },{
			text: '处理状态',
            sortable: true,				
            dataIndex: 'dealWithZT',
            width: 80,
            renderer:function(value){
                if(value=='A'){
                    return '无需处理';
                }else if(value=='C'){
                	return '已处理';
                }else{
                	return '未处理';
                }                    
            }
        },{
            text: '备注',
            sortable: true,
            dataIndex: 'callDesc',
            flex:1
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
