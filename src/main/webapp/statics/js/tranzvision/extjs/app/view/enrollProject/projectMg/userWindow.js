Ext.define('KitchenSink.view.enrollProject.projectMg.userWindow', {
    extend: 'Ext.window.Window',
	reference: 'userWindow',
	requires: [
        'Ext.data.*',
        'Ext.grid.*',
        'Ext.util.*',
        'Ext.toolbar.Paging',
        'Ext.ux.ProgressBarPager',
        'KitchenSink.view.enrollProject.projectMg.userStore'
    ],
	title:Ext.tzGetResourse("TZ_PRJ_PROMG_COM.TZ_PRJ_PROMG_STD.yhzhlb","用户账号列表") ,
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
            store: {
                type: 'userStore'
            },
            selModel: {
                type: 'checkboxmodel',
				mode: 'MULTI'
            },
            columns: [{
                text: Ext.tzGetResourse("TZ_PRJ_PROMG_COM.TZ_PRJ_PROMG_STD.name","姓名"),
                dataIndex: 'userName',
                width: 130
            },{
				text: Ext.tzGetResourse("TZ_PRJ_PROMG_COM.TZ_PRJ_PROMG_STD.phone","电话"),
				dataIndex: 'userPhone',
				width: 130
			},{
				text: Ext.tzGetResourse("TZ_PRJ_PROMG_COM.TZ_PRJ_PROMG_STD.email","邮箱"),
				dataIndex: 'userEmail',
				minWidth: 130,
				flex: 1	
			}],
            dockedItems:[{
                xtype:"toolbar",
                items:[
                    {text:Ext.tzGetResourse("TZ_PRJ_PROMG_COM.TZ_PRJ_PROMG_STD.query","查询"),iconCls: "query",handler:"cfgSearchAdmin"}
                ]
            }],
            bbar: {
                xtype: 'pagingtoolbar',
                pageSize: 5,
                listeners:{
                    afterrender: function(pbar){
                        var grid = pbar.findParentByType("grid");
                        pbar.setStore(grid.store);
                    }
                },
                reference: 'adminUserToolBar',
                plugins: new Ext.ux.ProgressBarPager()
            }
        }],
    buttons: [ {
        text: Ext.tzGetResourse("TZ_PRJ_PROMG_COM.TZ_PRJ_PROMG_STD.ensure","确定"),
        iconCls:"ensure",
        handler: 'onUserChooseEnsure'
    }, {
        text: Ext.tzGetResourse("TZ_PRJ_PROMG_COM.TZ_PRJ_PROMG_STD.close","关闭"),
        iconCls:"close",
        handler: 'onUserWinClose'
    }]
});
