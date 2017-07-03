Ext.define('KitchenSink.view.callCenter.updatePswWindow', {
    extend: 'Ext.window.Window',
    reference: 'updatePswWindow',
    requires: [
        'Ext.data.*',
        'Ext.grid.*',
        'Ext.util.*',
        'Ext.toolbar.Paging',
        'Ext.ux.ProgressBarPager'
    ],
    title: '修改密码',
    bodyStyle:'overflow-y:hidden;overflow-x:hidden;padding-top:10px',
    ignoreChangesFlag:true,
    width: 650,
    y:10,
    minWidth: 400,
    minHeight: 300,
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
        reference: 'updatePswForm',
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
        	xtype:'textfield',
        	name:'oprId',
        	hidden:true
        },{
        	xtype:'textfield',
        	name:'receiveId',
        	hidden:true
        },{
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
        },{
        	xtype: 'textfield',
        	fieldLabel:'新密码',
        	maxLength: 32,
            name: 'password',
            allowBlank:false,
            inputType: 'password',
        }]
    }],
    buttons: [{
        text: '确认修改密码',
        iconCls:"ensure",
        handler: 'confirmUpdatePsw'
    },{
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
