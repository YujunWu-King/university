Ext.define('KitchenSink.view.scoreModelManagement.scoreModelTreeManager', {
    extend: 'Ext.tree.Panel',
    xtype: 'scoreModelTreeManager',
    controller: 'scoreModelTreeManagerController',
    requires: [
        'Ext.data.*',
        'Ext.util.*',
        'KitchenSink.view.scoreModelManagement.scoreModelTreeManagerController',
        'KitchenSink.view.scoreModelManagement.scoreModelTreeStore',
        'Ext.data.TreeStore'
    ],
    title: '成绩模型树管理',
    split: true,
    collapsible: true,
    autoScroll : true,
    lines: true,
    rootVisible: true,
    collapseFirst: false,
    
    /*
    layout: {
		type: 'vbox',
		align: 'stretch'
    },
	*/
    viewConfig: {  
        plugins: {  
            ptype: 'treeviewdragdrop'  
        },  
        listeners: {  
            drop: function(node, data, dropRec, dropPosition) {  
                store.sync();  
            }  
        }  
    },  
    
    initComponent: function() {
        var me = this;
        
        var treeStore = new KitchenSink.view.scoreModelManagement.scoreModelTreeStore({
        	treeName: me.treeName
        });
        
        Ext.apply(this, {
        	store: treeStore,
        	dockedItems:[{
        		xtype: 'toolbar',
        		dock : 'top', 
        		style:'background-color: #dfeaf2;',
        		items: ['->',{
	        			iconCls : 'add',
	        			reference: 'addButton',
	        			text: '插入同级节点',
	        			tooltip: '插入同级节点',
	        			handler : "insertBrotherNode"
                    },{
                    	iconCls : 'add',
                    	text: '插入子节点',
                    	tooltip: '插入子节点',
                    	handler : "insertChildNode" 
                    },{
                    	iconCls : 'edit',
                    	text: '编辑',
                    	tooltip: '编辑',
                        handler : "editNode",
                    },{
                    	iconCls : 'delete',
                    	reference: 'deleteButton',
                    	text: '删除',
                    	tooltip: '删除',
                        handler : "removeNode",
                    }
                ]
        	}]
        	
        	
        });
        this.callParent();
    },
    listeners : {
        itemclick: function(view , record, item, index, e, eOpts ){
        	var treeManager = view.findParentByType("scoreModelTreeManager");
        	treeManager.selectTreeNode = record.data;
        	
        	var addButton = treeManager.down('toolbar').child('button[reference=addButton]');
        	var deleteButton = treeManager.down('toolbar').child('button[reference=deleteButton]');

        	if(record.data.root){
        		addButton.setHidden(true);
        		deleteButton.setHidden(true);
        	}else{
        		addButton.setHidden(false);
        		deleteButton.setHidden(false);
        	}
        }
    },
    constructor: function (config) {
        this.treeName = config.treeName;
        this.callParent();
    }
});
