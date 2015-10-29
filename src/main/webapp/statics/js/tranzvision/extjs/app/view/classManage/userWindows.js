Ext.define('KitchenSink.view.classManage.userWindows', {
    extend: 'Ext.window.Window',
    reference: 'userWindow',
    requires: [
        'Ext.data.*',
        'Ext.grid.*',
        'Ext.util.*',
        'Ext.toolbar.Paging',
        'Ext.ux.ProgressBarPager',
        'Ext.grid.filters.Filters',
        'KitchenSink.view.classManage.userStore'
    ],
    title: Ext.tzGetResourse("TZ_GD_BJGL_COM.TZ_GD_BJJB_STD.userList","用户账号列表"),
    width: 650,
    height: 400,
    layout: 'fit',
    resizable: true,
    modal: true,
    multiSel: '',
    rowNum: 0,
    items: [
        {
            xtype: 'grid',
            columnLines: true,
            multiSelect: true,
            viewConfig: {
                enableTextSelection: true
            },
            plugins:[{
                ptype: 'gridfilters'
            }] ,
            store: {
                type: 'userStore'
            },
            selModel: {
                type: 'checkboxmodel',
                mode: 'MULTI'
            },
            columns: [{
                text:Ext.tzGetResourse("TZ_GD_BJGL_COM.TZ_GD_BJJB_STD.userName","用户姓名") ,
                dataIndex: 'userName',
                width: 100,
                filter: {
                    type: 'string'
                }
            },{
                text:Ext.tzGetResourse("TZ_GD_BJGL_COM.TZ_GD_BJJB_STD.userPhone","电话") ,
                dataIndex: 'userPhone',
                width: 120,
                filter: {
                    type: 'string'
                }
            },{
                text: Ext.tzGetResourse("TZ_GD_BJGL_COM.TZ_GD_BJJB_STD.userEmail","邮箱") ,
                dataIndex: 'userEmail',
                minWidth: 130,
                flex: 1	,
                filter: {
                    type: 'string'
                }
            }]
        }],
    buttons: [ {
        text: Ext.tzGetResourse("TZ_GD_BJGL_COM.TZ_GD_BJJB_STD.ensure","确定") ,
        iconCls:"ensure",
        handler: 'onUserChoose'
    }, {
        text: Ext.tzGetResourse("TZ_GD_BJGL_COM.TZ_GD_BJJB_STD.close","关闭") ,
        iconCls:"close",
        handler: 'onUserClose'
    }]
});
