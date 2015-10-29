/**
 * Created by admin on 2015/9/7.
 */
Ext.define('KitchenSink.view.template.bmb.myRoleSetWindow', {
    extend: 'Ext.window.Window',
    xtype: 'myRoleSetWindow',
    requires: [
        'Ext.data.*',
        'Ext.grid.*',
        'Ext.util.*',
        'Ext.toolbar.Paging',
        'Ext.ux.ProgressBarPager',
        'KitchenSink.view.template.bmb.myBmbController',
        'KitchenSink.view.template.bmb.myRoleSetModel',
        'KitchenSink.view.template.bmb.myRoleSetStore'
    ],
    reference: 'myRoleSetWindow',
    title: '模板角色配置',
    width: 600,
    minWidth: 300,
    minHeight: 200,
    resizable: true,
    bodyStyle: 'overflow-y:auto;overflow-x:hidden',
    modal: true,
    closeAction: 'hide',
    closable: true,
    autoScroll: true,
    items: [{
        xtype: 'grid',
        title: '模板角色配置',
        columnLines: true,
        selModel: {
            type: 'checkboxmodel'
        },
        multiSelect: true,
        viewConfig: {
            enableTextSelection: true
        },
        header: false,
        store: {
            type: 'myRoleSetStore'
        },
        columns: [
            {
                text: '机构ID',
                dataIndex: 'jgid',
                hidden: true
            },
            {
                text: '模板编号',
                dataIndex: 'tplid',
                hidden: true
            },
            {
                text: '角色名称',
                dataIndex: 'rolename',
                width: 160,
                flex: 1
            },
            {
                text: '角色描述',
                dataIndex: 'roledesc',
                width: 100,
                flex: 1
            },
            {
                xtype: 'checkcolumn',
                text: '是否启用',
                dataIndex: 'isChecked',
                width: 100
            }]
    }],
    buttons: [
        {
            text: '保存',
            iconCls: "save",
            handler: 'onRoleSetSave'
        },
        {
            text: '确定',
            iconCls: "ensure",
            handler: 'onRoleSetEnsure'
        },
        {
            text: '关闭',
            iconCls: "close",
            handler: 'onRoleSetClose'
        }]
});