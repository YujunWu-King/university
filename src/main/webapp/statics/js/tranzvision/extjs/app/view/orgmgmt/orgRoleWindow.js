Ext.define('KitchenSink.view.orgmgmt.orgRoleWindow', {
    extend: 'Ext.window.Window',
    reference: 'orgRoleWindow',
    requires: [
        'Ext.data.*',
        'Ext.grid.*',
        'Ext.util.*',
        'Ext.toolbar.Paging'
    ],
    title: '机构角色信息',
    bodyStyle:'overflow-y:auto;overflow-x:hidden;padding-top:10px',
    actType: 'update',
    closeAction:'destroy',
    width: 500,
    minHeight: 240,
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
        reference: 'orgRoleForm',
        layout: {
            type: 'vbox',
            align: 'stretch'
        },
        border: false,
        bodyPadding: 10,
        bodyStyle:'overflow-y:auto;overflow-x:hidden',
        fieldDefaults: {
            msgTarget: 'side',
            labelWidth: 100,
            labelStyle: 'font-weight:bold'
        },

        items: [
            {
                xtype: 'hiddenfield',
                name: 'orgId',
                allowBlank:false
            },
            {
            xtype: 'textfield',
            fieldLabel: '角色名称',
            name: 'roleName',
            readOnly:true,
                fieldStyle:'background:#F4F4F4'
        }, {
            xtype: 'combo',
            fieldLabel: '角色类型',
            name: 'roleType',
            queryMode:'local',
            editable:false,
            valueField:'TZ_ZHZ_ID',
            displayField:'TZ_ZHZ_CMS',
            store:new KitchenSink.view.common.store.comboxStore({
                recname:'PS_TZ_TRANS_ITEM_VW',
                condition:{
					"TZ_ZHZJH_ID":{
						value:'TZ_ROLE_TYPE',
						operator:'01',
						type:'01'
					},
					"TZ_LANGUAGE_ID":{
						value:'ZHS',
						operator:'01',
						type:'01'
					}
                },
                result:'TZ_ZHZ_CMS,TZ_ZHZ_ID'
            })
        }, {
            xtype: 'textfield',
            fieldLabel: '角色描述',
            name: 'roleDesc',
            readOnly:true,
                fieldStyle:'background:#F4F4F4'
        }]
    }],
    buttons: [{
        text: '保存',
        iconCls:"save",
        name:'save',
        handler: 'onOrgRoleFormSave'
    }, {
        text: '确定',
        iconCls:"ensure",
        name:"ensure",
        handler: 'onOrgRoleFormSave'
    },{
        text: '关闭',
        iconCls:"close",
        handler: function(btn){
            btn.findParentByType("window").close();
        }
    }]
});
