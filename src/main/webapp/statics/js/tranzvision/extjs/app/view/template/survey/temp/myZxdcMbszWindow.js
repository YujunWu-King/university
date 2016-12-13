Ext.define('KitchenSink.view.template.survey.temp.myZxdcMbszWindow', {
    extend: 'Ext.window.Window',
    xtype: 'myZxdcMbszWindow',
    title: '问卷属性',
    reference:'myZxdcMbszWindow',
    height: 500,
    width: 800,
   // y:10,
    actType: 'add',
    modal:true,
    bodyStyle:'overflow-y:auto;overflow-x:hidden',
    items: [{
        xtype: 'form',
        width: 700,
        layout: {
            type: 'vbox',
            align: 'stretch'
        },
        fieldDefaults: {
            msgTarget: 'side',
            labelWidth: 100,
            labelStyle: 'font-weight:bold'
        },
        border: false,
        bodyPadding: 10,
        items: [
            {
                xtype: 'textfield',
                fieldLabel: Ext.tzGetResourse("TZ_ZXDC_MBGL_COM.TZ_ZXDC_MBSZ_STD.TZ_APP_TPL_ID","模板ID"),
                name:'TZ_APP_TPL_ID',
                ignoreChangesFlag: true,
                hidden:true,
                value:'NEXT'
            },
            {
                fieldLabel: '模板名称',
                name: 'TZ_APP_TPL_MC',
                allowBlank: false,
                afterLabelTextTpl: [
                    '<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'
                ],
                xtype: 'textfield'
            },
            {
                xtype: 'combobox',
                fieldLabel: '状态',
                editable: false,
                emptyText: '请选择',
                queryMode: 'remote',
                name: 'TZ_EFFEXP_ZT',
                valueField: 'TValue',
                displayField: 'TSDesc',
                store: new KitchenSink.view.common.store.appTransStore("TZ_EFFEXP_ZT"),
                allowBlank: false,
                value:'Y'
            },
            {
                xtype: 'combobox',
                fieldLabel: '语言',
                editable: false,
                emptyText: '请选择',
                queryMode: 'remote',
                name: 'TZ_APP_TPL_LAN',
                valueField: 'TValue',
                displayField: 'TSDesc',
                store: new KitchenSink.view.common.store.appTransStore("TZ_APP_TPL_LAN"),
                allowBlank: false,
                readOnly:true
            },
            {  xtype: 'combobox',
                fieldLabel: '类型',
                editable:false,
                emptyText:'请选择',
                queryMode: 'remote',
                name: 'TZ_APP_TPL_LX',
                valueField: 'TValue',
                displayField: 'TSDesc',
                ignoreChangesFlag: true,
                store: new KitchenSink.view.common.store.appTransStore("TZ_APP_TPL_LX"),
                allowBlank:false,
                hidden:true,
                value:'0'
            },
            {
                fieldLabel: '卷头内容',
                xtype: 'ueditor',
                name: 'TZ_DC_JTNR'
            },
            {
                xtype: 'ueditor',
                fieldLabel: '卷尾内容',
                name: 'TZ_DC_JWNR'
            }
         ]
    }],
    buttons: [{
        text: '保存',
        iconCls:"save",
        handler: function(btn){
            //获取窗口
            var win = btn.findParentByType("window");
            win.doSave(win);
        }
    },{
        text: '确定',
        iconCls:"ensure",
        handler: function(btn){
            var win = btn.findParentByType("window");
            var form = win.child("form").getForm();
            var grid = btn.findParentByType("zxdcMbListInfo");
            if(!form.isValid()){
                return false;
            }
            var formParams = form.getValues();
            var tzParams = '{"ComID":"TZ_ZXDC_MBGL_COM","PageID":"TZ_ZXDC_MBSZ_STD","OperateType":"U","comParams":{"update":['+Ext.JSON.encode(formParams)+']}}';
            Ext.tzSubmit(tzParams,function(data){
                grid.store.reload();
                var tplId=data.id;
                win.close();
            },"",true,win);
        }
    }, {
        text: '关闭',
        iconCls:"close",
        handler: function(btn){
            var win = btn.findParentByType("window");
            win.close();
        }
    }],
    doSave:function(win){
        //保存
        var form = win.child("form").getForm();
        if(!form.isValid()){
            return false;
        }else{
            var formParams = form.getValues();
            var tzParams = '{"ComID":"TZ_ZXDC_MBGL_COM","PageID":"TZ_ZXDC_MBSZ_STD","OperateType":"U","comParams":{"update":['+Ext.JSON.encode(formParams)+']}}';
            Ext.tzSubmit(tzParams,function(response){
                /*保存后，=修改页面的ID值*/
                form.setValues({"TZ_APP_TPL_ID":response.id});
                win.findParentByType("zxdcMbListInfo").store.reload();

            },"",true,this);
        }
    }
});
