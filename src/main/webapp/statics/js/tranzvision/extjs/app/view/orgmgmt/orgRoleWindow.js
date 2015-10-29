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
            readOnly:true
        }, {
            xtype: 'combo',
            fieldLabel: '角色类型',
            name: 'roleType',
            queryMode:'local',
            editable:false,
            valueField:'FIELDVALUE',
            displayField:'XLATLONGNAME',
            store:new KitchenSink.view.common.store.comboxStore({
                recname:'PSXITMMNT_VW',
                condition:{FIELDNAME:{
                    value:'TZ_ROLE_TYPE',
                    operator:'01',
                    type:'01'
                }
                },
                result:'XLATLONGNAME,FIELDVALUE'
            })
        }, {
            xtype: 'textfield',
            fieldLabel: '角色描述',
            name: 'roleDesc',
            readOnly:true
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
