Ext.define('KitchenSink.view.siteManage.siteManage.skin.skinInfo', {
 extend: 'Ext.panel.Panel',
 xtype: 'skininfo', 
 controller: 'siteSkinInfo',
 	requires: [
	    'Ext.data.*',
        'Ext.grid.*',
        'Ext.util.*',
        'Ext.toolbar.Paging',
        'Ext.ux.ProgressBarPager',
	    'KitchenSink.view.template.sitetemplate.skin.siteSkinController',//保存
		'KitchenSink.view.template.sitetemplate.skin.skinRoleModel',//图片列表json格式
		'KitchenSink.view.template.sitetemplate.skin.skinstate',
        'KitchenSink.view.template.sitetemplate.skin.skinPictureStore'//图片列表json路径
	],
    title: '站点皮肤设置', 
	bodyStyle:'overflow-y:auto;overflow-x:hidden',
	actType: 'add',//默认新增
	items:[
	{
        xtype: 'form',
        reference: 'skinAccountForm',//查看表单时cmp.on引用
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
            items:[{
            xtype: 'hiddenfield',
            readOnly:true,
            fieldLabel: '站点模板编号',
			name: 'siteId'
        	},{
            xtype: 'hiddenfield',
            readOnly:true,
            fieldLabel: '皮肤编号',
			name: 'skinId',
			value: ''
        	},{
            xtype: 'combobox',
            typeAhead: true,
            queryMode: 'local',
            forceSelection: true,
            store: {
                type: 'skinstate'
            },
            fieldLabel: '状态',
           // triggerAction:'all',
            valueField: 'id',
            displayField:'state',
			name: 'skinStatus'
            },{
            xtype: 'textfield',
            fieldLabel: '皮肤名称',
			name: 'skinName'
            },{
            xtype: 'textarea',
            fieldLabel: '皮肤样式源码',
            labelSeparator:':',//分隔符
            labelWindth:300,
            height: 200,
			name: 'skinCode'
            }],
	},{
		xtype: 'grid',
		height: 315, 
		title: '皮肤效果图片集',
		frame: true,
		columnLines: true,
		reference: 'skinRoleGrid',
		style:"margin:10px",
		store: {
			type: 'skinPictureStore'
		},
		columns: [{ 
			text: '图片ID',
			dataIndex: 'pictureID',
            hidden: true
		},{
			text: '图片顺序',
			dataIndex: 'pictureRanking',
			minWidth: 80,
			flex: 1
		},{
			text: '图片名称',
			dataIndex: 'pictureName',
			minWidth: 80,
			flex: 1
		},{
			text: '图片访问地址',
			dataIndex: 'pictureUrl',
			minWidth: 80,
			flex: 1
		}],
		bbar: {
			xtype: 'pagingtoolbar',
			pageSize: 5,
			reference: 'skinRoleToolBar',
			store: {
				type: 'skinPictureStore'
			},
			displayInfo: true,
			displayMsg: '显示{0}-{1}条，共{2}条',
			beforePageText: '第',
			afterPageText: '页/共{0}页',
			emptyMsg: '没有数据显示',
			plugins: new Ext.ux.ProgressBarPager()
		}
        }],//页面itemend
        
	    buttons: [{
		text: '保存',
		iconCls:"save",
		handler: 'onFormSave'
	    }, {
		text: '确定',
		iconCls:"ensure",
		handler: 'onFormEnsure'
	    }, {
		text: '关闭',
		iconCls:"close",
		handler: 'onFormClose'
	    }]
});