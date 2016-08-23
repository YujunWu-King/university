Ext.define('KitchenSink.view.content.artMg.artTreeStore', {
    extend: 'Ext.data.TreeStore',
    alias: 'store.artTreeStore',
    rootVisible: true,
    constructor: function(config) {
        var me = this;
        //var items;
        var root;
		var tzParams ='{"ComID":"TZ_ART_MG_COM","PageID":"TZ_ART_LIST_STD","OperateType":"QF","comParams":{"typeFlag":"TREE","siteid":"'+ config.siteid+'"}}';
		Ext.tzLoadAsync(tzParams,function(responseData){
			root = responseData.root;
		});
		items = root.children;
		me.callParent([Ext.apply({
			root: {
				nodeId: root.nodeId,
				id: root.nodeId,
				text: root.text,
				rootNode: 'ROOT123',
				expanded: root.expanded,
				children: items
			}
		}, config)]);	
    }
});
