Ext.define('KitchenSink.view.processServer.processServerEditWindow', {
    extend: 'Ext.window.Window',
    xtype: 'processServerEditWindow',
    controller: 'processServerCon',
    requires: [
        'Ext.data.*',
        'Ext.grid.*',
        'Ext.util.*',
        'Ext.toolbar.Paging',
        'Ext.ux.ProgressBarPager',
        'KitchenSink.view.processServer.processServerController'
    ],
    title: '进程服务器编辑',
    reference: 'processServerEditWindow',
    width: 700,
    height: 450,
    minWidth: 200,
    minHeight: 100,
    layout: 'fit',
    resizable: true,
    modal: true,
    actType: 'add',

    items: [{
        xtype: 'form',
        layout: {
            type: 'vbox',
            align: 'stretch'
        },
        border: false,
        bodyPadding: 10,
        //heigth: 600,

        fieldDefaults: {
            msgTarget: 'side',
            labelWidth: 150,
            labelStyle: 'font-weight:bold'
        },
        items: [
            {
                xtype: 'combobox',
                editable:false,
                fieldLabel: '归属机构',
                forceSelection: true,
                valueField: 'orgId',
                readOnly:true,
                displayField: 'orgName',
                store: new KitchenSink.view.orgmgmt.orgListStore(),
                queryMode: 'local',
                name: 'orgId',
                emptyText:'请选择机构',
                afterLabelTextTpl: [
                    '<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'
                ],
                allowBlank:false
            },{
                xtype: 'textfield',
                fieldLabel: '进程服务器名称',
                readOnly:true,
                name: 'processName',
                afterLabelTextTpl: [
                    '<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'
                ],
                allowBlank:false
            },{
                xtype: 'textfield',
                fieldLabel: '服务器IP地址',
                name: 'serverIP',
                afterLabelTextTpl: [
                    '<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'
                ],
                allowBlank:false
            },{
                xtype: 'textfield',
                fieldLabel: '进程服务器描述',
                name: 'processDesc',
                afterLabelTextTpl: [
                    '<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'
                ],
                allowBlank:false
            },{
                xtype: 'combobox',
                editable:false,
                fieldLabel: '运行平台类型',
                forceSelection: true,
                valueField: 'TValue',
                displayField: 'TSDesc',
                store: new KitchenSink.view.common.store.appTransStore("TZ_PLAT_TYPE"),
                queryMode: 'remote',
                name: 'runPlatType',
                emptyText:'Windows/Unix/其他',
                afterLabelTextTpl: [
                    '<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'
                ],
                allowBlank:false
            },{
                xtype: 'textfield',
                fieldLabel: '任务循环读取间隔时间',
                name: 'intervalTime',
                afterLabelTextTpl: [
                    '<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'
                ],
                allowBlank:false
            }, {
                xtype: 'textfield',
                fieldLabel: '最大并行任务数',
                name: 'parallelNum',
                afterLabelTextTpl: [
                    '<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'
                ],
                allowBlank:false
            },                	
            {
        		xtype: 'datefield',
                fieldLabel: '最近心跳日期',
                editable:false,
                format : 'Y-m-d',
                name: 'palpitationDate',
                afterLabelTextTpl: [
                    '<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'
                ],
                allowBlank:false
        	},                	
        	{
        		xtype: 'timefield',
                fieldLabel: '最近心跳时间',
                editable:false,
                format : 'H:i:s',
                name: 'palpitationTime',
                afterLabelTextTpl: [
                    '<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'
                ],
                allowBlank:false
        	},{
                xtype: 'textarea',
                fieldLabel: '备注信息',
                name: 'remark'
            },
            {
               xtype:'fieldcontainer',
               layout:'hbox',
                items:[
                    {
                        xtype:"displayfield" ,
                        width:'40%',
                        fieldLabel:"运行状态" ,
                        name:'status'
                    },{
                        xtype:"button",
                        glyph:"xf144@FontAwesome",
                        text:"开始",
                        handler:'startProcessWin'
                    },{
                        xtype:"button" ,
                        style:'margin-left:50px',
                        glyph:"xf0c8@FontAwesome",
                        icon:"",
                        text:"停止",
                        handler:'stopProcessWin'
                    }
                ]
            },

        ],
    }],
    buttons: [{
        text: '保存',
        iconCls:"save",
        handler: function (btn) {
            //获取窗口
            var win = btn.findParentByType("window");
            var form = win.child("form").getForm();
            win.doSave(win);
        }
    }, {
        text: '确定',
        iconCls: "ensure",
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
        handler: function (btn) {
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
        var tzParams = '{"ComID":"TZ_PROCESS_FW_COM","PageID":"TZ_PROCESS_FW_EDIT","OperateType":"U","comParams":{"update":['+Ext.JSON.encode(formParams)+']}}';
        Ext.tzSubmit(tzParams,function(response){
            var attrValue=response.attrValue;
            form.setValues({"attrValue":attrValue});
            win.findParentByType("grid").store.reload();
        },"",true,this);

    }
});
