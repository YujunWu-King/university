Ext.define('KitchenSink.view.siteManage.outsiteManage.menuTreeStore',{
	extend : 'Ext.data.TreeStore',
	alias : 'store.menuTreeStore',
	rootVisible : true,
	constructor : function(config) {
		var me = this;
		var items;
		var root;
		var tzParams = '{"ComID":"TZ_GD_WWZDGL_COM","PageID":"TZ_GD_WWCDGL_STD","OperateType":"QF","comParams":{"typeFlag":"TREE","siteId":"'+ config.siteId + '"}}';
		Ext.tzLoadAsync(tzParams, function(responseData) {
			root = responseData.root;
		});
		items = root.children;
		me.callParent([ Ext.apply({
			root : {
				text : root.text,
				nodeId : root.nodeId,
				id : root.id,
				siteId : root.siteId,
				menuState : root.menuState,
				menuPath : root.menuPath,
				menuTempletId : root.menuTempletId,
				menuTempletName : root.menuTempletName,
				menuPageName : root.menuPageName,
				menuType : root.menuType,
				isDefault : root.isDefault,
				defaultPage : root.defaultPage,
				menuXH : root.menuXH,
				menuStyle : root.menuStyle,
				saveImageAccessUrl : root.saveImageAccessUrl,
				
				titleImageTitle: root.titleImageTitle,
				titleImageDesc: root.titleImageDesc,
				titleImageUrl:root.titleImageUrl,
				
				NodeType : root.NodeType,
				operateNode : root.operateNode,
				menuShow:root.menuShow,
				rootNode : root.id,
				expanded : root.expanded,
				children : me.getChartNavItems(items)
			}
		}, config) ]);
	},

	addIconClasses : function(items) {
		for (var item, i = items.length; i-- > 0;) {
			item = items[i];

			if (!('iconCls' in item)) {
				item.iconCls = 'icon-' + item.largeIcon;
			}
			if (!('glyph' in item)) {
				item.glyph = '32@Sencha-Examples';
			}
			if (item.children) {
				this.addIconClasses(item.children);
			}
		}
		return items;
	},
	
	getChartNavItems : function(items) {
		return this.addIconClasses(items);
	},
});
