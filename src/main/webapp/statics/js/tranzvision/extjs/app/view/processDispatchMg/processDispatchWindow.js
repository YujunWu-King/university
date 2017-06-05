Ext.define('KitchenSink.view.processDispatchMg.processDispatchWindow', {
    extend: 'Ext.window.Window',
    xtype: 'processDispatchWindow',
    controller: 'processDispatchCon',
    requires: [
        'Ext.data.*',
        'Ext.grid.*',
        'Ext.util.*',
        'Ext.toolbar.Paging',
        'Ext.ux.ProgressBarPager',
        'tranzvision.extension.grid.column.Link',
        'KitchenSink.view.orgmgmt.orgListStore',
        'KitchenSink.view.processDispatchMg.processDispatchController'
    ],
    title: '进程调度',
    reference: 'processDispatchWindow',
    id:'runDispatchWin',
    width: 600,
    height: 400,
    minWidth: 200,
    minHeight: 100,
    layout: 'fit',
    resizable: true,
    modal: true,
    actType: 'update',

    items: [{
        xtype: 'form',
        id: 'runInfoForm',
        layout: {
            type: 'vbox',
            align: 'stretch'
        },
        border: false,
        bodyPadding: 10,

        fieldDefaults: {
            msgTarget: 'side',
            labelWidth: 100,
            labelStyle: 'font-weight:bold;font-size:12px'
        },
        items: [
            {
                xtype: 'textfield',
                name:'orgId',
                value:Ext.tzOrgID,
                hidden:true
            },
            {
                xtype: 'textfield',
                name:'jcName',
                hidden:true
            },
            {
                xtype: 'fieldcontainer',
                layout: 'column',
                style: 'margin-left:50px',
                items: [
                    {
                        xtype: 'textfield',
                        columnWidth: .4,
                        width : 100,
                        readOnly:true,
                        name: 'user',
                        labelWidth: 70,
                        fieldLabel: "用户"
                    },
                    {
                        xtype: 'textfield',
                        columnWidth: .6,
                        readOnly:true,
                        style: 'margin-left:20px',
                        name: 'runCntlId',
                        labelWidth: 80,
                        fieldLabel: "运行控制ID"
                    }
                ]
            },
            {
                xtype: 'combobox',
                editable:false,
                style: 'margin-top:20px;margin-left:50px',
                fieldLabel: '服务器名称',
                labelWidth: 70,
                forceSelection: true,
                valueField: 'processName',
                displayField: 'processName',
                store: new KitchenSink.view.processServer.processServerStore(),
                queryMode: 'local',
                name: 'processName',
                emptyText:'任意',
                allowBlank:true
            },{
                xtype: 'radio',
                name:'loopYear',
                style:'margin-top:30px;margin-left:50px',
                inputValue:"dateTime",
                boxLabel: "指定调度日期时间"
            },{
                xtype: 'fieldcontainer',
                layout: 'column',
                style:'margin-top:10px;margin-left:50px',
                items:[{
                    xtype: 'datefield',
                    fieldLabel: '运行日期',
                    columnWidth:.4,
                    name:'runDate',
                    format: 'Y-m-d',
                    labelWidth: 60,
                    editable: false,
                    style:'margin-left:30px'
                },{
                    xtype: 'timefield',
                    fieldLabel: '运行时间',
                    columnWidth:.4,
                    name:'runTime',
                    format: 'H:i:s',
                    labelWidth: 60,
                    editable: false,
                    style:'margin-left:40px'
                }]
            },{
                xtype: 'radio',
                name:'loopYear',
                style:'margin-top:30px;margin-left:50px',
                inputValue:"dateTime",
                boxLabel: "指定调度日期时间"
            },{
                bodyStyle:'padding:0 0 10px 0',
                style:'margin-left:50px',
                xtype: 'textfield',
                fieldLabel: '循环',
                labelWidth: 40,
                name: 'cycleExpression',
                editable: false,
                triggers: {
                    search: {
                        cls: 'x-form-search-trigger',
                        handler: "pmtSearchCycleTmp"
                    }
                },
                style:'margin-top:10px;margin-left:80px'
            }
        ],
    }],
    buttons: [{
        text: '确定',
        iconCls:"ensure",
        handler: function (btn) {
            //获取窗口
            var win = btn.findParentByType("window");
            //页面注册信息表单
            var form = win.child("form").getForm();
            win.doSave(win);
            win.close()
        }
    }, {
        text: '关闭',
        iconCls:"close",
        handler: function(btn){
            var win = btn.findParentByType("window");
            var form = win.child("form").getForm();
            win.close();
        }
    }],
    doSave:function(win){
        //保存
        var form = win.child("form").getForm();
        if(!form.isValid()){
            return false;
        }
        var formParams = form.getValues();
        var tzParams = '{"ComID":"TZ_JC_DISPATCH_COM","PageID":"TZ_DISPATCH_INFO","OperateType":"U","comParams":{"update":['+Ext.JSON.encode(formParams)+']}}';
        Ext.tzSubmit(tzParams,function(response){
            var attrValue=response.attrValue;
            form.setValues({"attrValue":attrValue});
        },"",true,this);

    }
});
