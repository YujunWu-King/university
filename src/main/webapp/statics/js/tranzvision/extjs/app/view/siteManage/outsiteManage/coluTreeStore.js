Ext.define('KitchenSink.view.siteManage.outsiteManage.coluTreeStore',{
	extend : 'Ext.data.TreeStore',
	alias : 'store.coluTreeStore',
	rootVisible : true,
	constructor : function(config) {
		var me = this;
		var items;
		var root;
		var tzParams = '{"ComID":"TZ_GD_WWZDGL_COM","PageID":"TZ_GD_WWLMGL_STD","OperateType":"QF","comParams":{"typeFlag":"TREE","siteId":"'+ config.siteId + '"}}';
		Ext.tzLoadAsync(tzParams, function(responseData) {
			root = responseData.root;
		});
		items = root.children;
		me.callParent([ Ext.apply({
			root : {
				text : root.text,
				siteId : root.siteId,
				nodeId : root.nodeId,
				id : root.id,
				coluState : root.coluState,
				coluPath : root.coluPath,
				coluTempletId : root.coluTempletId,
				contentTypeId : root.contentTypeId,
				coluTempletName : root.coluTempletName,
				contentTypeName : root.contentTypeName,
				coluType : root.coluType,
				coluUrl : root.coluUrl,
				NodeType : root.NodeType,
				coluAbout : root.coluAbout,
				operateNode : root.operateNode,
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
