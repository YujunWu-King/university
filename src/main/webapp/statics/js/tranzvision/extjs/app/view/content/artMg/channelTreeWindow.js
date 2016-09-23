Ext.define('KitchenSink.view.content.artMg.channelTreeWindow', {
    extend: 'Ext.window.Window',
    xtype: 'channelTreeWindow', 
	controller: 'artTreeController',
	requires: [
	    'Ext.data.*',
        'Ext.grid.*',
        'Ext.util.*',
        'KitchenSink.view.content.artMg.artTreeController',
	    'KitchenSink.view.content.artMg.artTreeStore',
        'Ext.data.TreeStore'
	],
    title: '站点栏目选择', 
    reference:'channelTreeWindow',
    width:600,
	modal:true,
	artRecs:[],
	channelId:'',
	bodyStyle:'overflow-y:auto;overflow-x:hidden',
	actType: 'add',//默认新增
    items: [{
				xtype: 'combobox',
				dock: 'top',
				margin: "5 0 5 0",
				fieldLabel: '站点',
				name:'mySites',
				labelWidth:40,
				labelStyle: 'font-weight:bold',
				style:'background:white;background-image:none',
				queryMode: 'remote',
				editable:false,
				valueField: 'TValue',
				displayField: 'TSDesc',
				listeners: {
					afterrender: function(tvType){
						var siteid = this.siteid;
						var tzParams = '{"ComID":"TZ_ART_MG_COM","PageID":"TZ_ART_LIST_STD","OperateType":"QF","comParams":{"typeFlag":"SITE"}}';
						Ext.tzLoad(tzParams,function(responseData){
							var store = new Ext.data.Store({
								fields: ['TValue', 'TSDesc', 'TLDesc'],
								data:responseData.TransList
							});
							
							tvType.setStore(store);
							if(siteid==undefined){
								if(store.getCount() > 0){
									tvType.setValue(store.getAt(0).get("TValue"));
								}
							}
						});
					},
					change:function(tvType,newValue,oldValue,eOpts){
						var treeStore = new KitchenSink.view.content.artMg.artTreeStore({siteid: newValue});
						tvType.findParentByType("panel").child("treepanel").setStore(treeStore);
					}
				}
			},{
					region:'south',
					xtype: 'treepanel',
					width: 550,
					split: true,
					//collapsible: true,
					height: 400,
					autoScroll : true,
					//lines: true,
					rootVisible: true,
					store: new KitchenSink.view.content.artMg.artTreeStore({siteid: "NONE"}),
					listeners : {  
						itemclick: "channelTreeItemClick"
					}
			}],
    buttons: [{
		text: '确定',
		iconCls:"ensure",
		handler: 'onShooseChannelEnsure'
	}, {
		text: '关闭',
		iconCls:"close",
		handler: 'onShooseChannelClose'
	}]
});