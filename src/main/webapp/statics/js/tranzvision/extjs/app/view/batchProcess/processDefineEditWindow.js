Ext.define('KitchenSink.view.batchProcess.processDefineEditWindow', {
    extend: 'Ext.window.Window',
    xtype: 'processDefineEditWindow',
    controller: 'processDefineCon',
    requires: [
        'Ext.data.*',
        'Ext.grid.*',
        'Ext.util.*',
        'Ext.toolbar.Paging',
        'Ext.ux.ProgressBarPager',
        'KitchenSink.view.batchProcess.processDefineController'
    ],
    title: '进程定义编辑',
    reference: 'processDefineEditWindow',
    width: 700,
    height: 350,
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
            labelWidth: 120,
            labelStyle: 'font-weight:bold'
        },
        items: [
            {
                xtype: 'combobox',
                editable:false,
                fieldLabel: '归属机构',
                forceSelection: true,
                readOnly:true,
                valueField: 'orgId',
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
                fieldLabel: '进程名称',
                readOnly:true,
                name: 'processName',
                afterLabelTextTpl: [
                    '<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'
                ],
                allowBlank:false
            },{
                xtype: 'textfield',
                fieldLabel: '进程描述',
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
                bodyStyle:'padding:0 0 10px 0',
                xtype: 'textfield',
                fieldLabel: '注册到组件',
                name: 'ComID',
                editable: false,
                triggers: {
                    search: {
                        cls: 'x-form-search-trigger',
                        handler: "pmtSearchComIDTmp"
                    }
                },
                afterLabelTextTpl: [
                    '<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'
                ],
                allowBlank:false
            }, {
                xtype: 'textfield',
                fieldLabel: 'JavaClass类路径',
                name: 'className',
                afterLabelTextTpl: [
                    '<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'
                ],
                allowBlank:false
            },{
                xtype: 'textarea',
                fieldLabel: '备注信息',
                name: 'remark'
            }
        ]
    }],
    buttons: [{
        text: '保存',
        iconCls:"save",
        handler: function(btn){
            //获取窗口
            var win = btn.findParentByType("window");
            var form = win.child("form").getForm();
            win.doSave(win);
        },
    }, {
        text: '确定',
        iconCls:"ensure",
        handler: function(btn){
            //获取窗口
            var win = btn.findParentByType("window");
            //页面注册信息表单
            var form = win.child("form").getForm();
            var gridStore = 	btn.findParentByType("grid").store;
            var formParams = form.getValues();
            if (form.isValid()) {
                var tzParams = '{"ComID":"TZ_PROCESS_DF_COM","PageID":"TZ_PROCESS_EDIT","OperateType":"U","comParams":{"update":['+Ext.JSON.encode(formParams)+']}}'
                Ext.tzSubmit(tzParams,function(){
                    gridStore.load();
                    win.close();
                },"",true,this);
            }
        },
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
        var tzParams = '{"ComID":"TZ_PROCESS_DF_COM","PageID":"TZ_PROCESS_EDIT","OperateType":"U","comParams":{"update":['+Ext.JSON.encode(formParams)+']}}';
        Ext.tzSubmit(tzParams,function(response){
            var attrValue=response.attrValue;
            form.setValues({"attrValue":attrValue});
           win.findParentByType("grid").store.reload();
        },"",true,this);

    }
});
