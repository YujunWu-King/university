    Ext.define('KitchenSink.view.security.role.roleInfoPanel', {
    extend: 'Ext.panel.Panel',
    xtype: 'roleInfo',
    controller: 'roleMg',
    requires: [
        'Ext.data.*',
        'Ext.grid.*',
        'Ext.util.*',
        'Ext.toolbar.Paging',
        'Ext.ux.ProgressBarPager',
        'KitchenSink.view.security.role.rolePlstModel',
        'KitchenSink.view.security.role.rolePlstStore'
    ],
    title: '角色定义信息',
    bodyStyle:'overflow-y:auto;overflow-x:hidden',
    actType: 'add',//默认新增
    items: [{
        xtype: 'form',
        reference: 'roleInfoForm',
        layout: {
            type: 'vbox',
            align: 'stretch'
        },
        border: false,
        bodyPadding: 10,
        //heigth: 600,
        bodyStyle:'overflow-y:auto;overflow-x:hidden',

        fieldDefaults: {
            msgTarget: 'side',
            labelWidth: 100,
            labelStyle: 'font-weight:bold'
        },

        items: [{
            xtype: 'textfield',
            fieldLabel: '角色名称',
            name: 'roleName',
            editable:true,
            allowBlank: false,
            maxLength: 30,
            afterLabelTextTpl: [
                '<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'
            ]
        }, {
            xtype: 'textfield',
            fieldLabel: '描述',
            name: 'roleDesc',
            editable:true,
            allowBlank: false,
            maxLength: 30,
            afterLabelTextTpl: [
                '<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'
            ]
        }, {
            xtype: 'textarea',
            fieldLabel: '详细描述',
            name: 'roleDescLong',
            editable:true
        }]
    },{
        xtype: 'grid',
        autoHeight:true,
        minHeight: 360,
        title: '许可权列表',
        frame: true,
        selModel: {
            type: 'checkboxmodel'
        },
        columnLines: true,
        dockedItems:{
            xtype:"toolbar",
            items:[
                {text:"新增",tooltip:"新增数据",iconCls:"add",handler:'addPermission'},"-",
                {text:"编辑",tooltip:"编辑数据",iconCls:"edit",handler:"editPermission"},"-",
                {text:"删除",tooltip:"删除选中的数据",iconCls:"remove",handler:'deletePermissions'}
            ]
        },
        reference: 'rolePlstGrid',
        style:"margin:10px",
        store: {
            type: 'rolePlstStore'
        },
        columns: [{
            text: '角色名称',
            dataIndex: 'roleName',
            hidden:true
        },{
            text: '许可权编号',
            dataIndex: 'permID',
            width: 300
        },{
            text: '许可权描述',
            dataIndex: 'permDesc',
            minWidth: 300,
            flex: 1
        },{
            menuDisabled: true,
            sortable: false,
            width:40,
            xtype: 'actioncolumn',
            items:[
                {iconCls: 'edit',tooltip: '编辑',handler:'editRoleInfoSave'},
                {iconCls: 'remove',tooltip: '删除',handler:'deleteRoleInfoSave'}
            ]
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
        text: '保存',
        iconCls:"save",
        handler: 'onRoleInfoSave'
    }, {
        text: '确定',
        iconCls:"ensure",
        handler: 'onRoleInfoEnsure'
    }, {
        text: '关闭',
        iconCls:"close",
        handler: 'onRoleInfoClose'
    }]
});
