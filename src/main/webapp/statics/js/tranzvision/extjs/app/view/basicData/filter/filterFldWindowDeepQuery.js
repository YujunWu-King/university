Ext.define('KitchenSink.view.basicData.filter.filterFldWindowDeepQuery', {
    extend: 'Ext.window.Window',
    requires: [
	    'Ext.data.*',
        'Ext.grid.*',
        'Ext.util.*',
        'Ext.toolbar.Paging',
        'Ext.ux.ProgressBarPager',
		'KitchenSink.view.basicData.filter.FldStore'
	],
    //xtype: 'filterWindow', 
    title: '可配置搜索配置-选择搜索条件字段', 
	reference: 'filterFldWindowDeepQuery',
    y:20,
    width: 740,
    maxHeight: 500,
    modal:true,
    layout: 'fit',
    resizable: true,
	items: [{
		xtype: 'form',	
		layout: {
			type: 'vbox',
			align: 'stretch'
		},
		border: false,
		defaults: {
			msgTarget: 'side',
			labelWidth: 90,
			labelStyle: 'font-weight:bold'
		},
		items: [{
			xtype: 'hiddenfield',
			fieldLabel: '可配置搜索组件编号',
			name: 'ComID'
		}, {
			xtype: 'hiddenfield',
			fieldLabel: '可配置搜索页面编号',
			name: 'PageID'
		}, {
			xtype: 'hiddenfield',
			fieldLabel: '可配置搜索视图名称',
			name: 'ViewMc',
			padding:10
		}, {
			xtype: 'textfield',
			fieldLabel: '视图名称',
			name: 'FieldView',
			padding:10
		},{
			xtype: 'textfield',
			fieldLabel: '字段名称',
			name: 'FieldMc',
            padding:10
		},{
			xtype: 'container',
			layout: {
			    type: 'hbox'
			},
			items:[{
				xtype:'button',
				margin:'0 20 0 105',
				width:100,
				text:'搜索',
				tooltip:'搜索',
				handler:function(btn) {
					var form = btn.findParentByType("form");
					var formParams = form.getForm().getValues();
					var ComID = formParams['ComID'];
			   	 	var PageID = formParams['PageID'];
			   	 	var ViewMc = formParams['ViewMc'];
			   	 	var FieldMc = formParams['FieldMc'];
			   	 	
			   	 	var FieldView=formParams['FieldView'];
			   	 	
			   	 	var grid = form.child("grid");
			   	 	var tzStoreParams = '{"ComID":"' + ComID + '","PageID":"' + PageID + '","ViewMc":"' + ViewMc +'","FieldView":"' + FieldView + '","FieldMc":"' + FieldMc + '"}';
			        grid.store.tzStoreParams = tzStoreParams;
			        grid.store.loadPage(1);
			    }
			},{
				xtype:'button',
				text:'清除',
				width:100,
				tooltip:'清除',
				handler: function(btn) {
			        var win = btn.findParentByType("window");
					var form = win.child("form").getForm();
					form.findField("FieldMc").setValue("");
			    }
			}]
		},{
			xtype: 'grid',
			height: 'auto',
            title:'字段列表',
            minHeight:200,
            maxHeight:300,
            style:'margin-top:10px',
			columnLines: true,
			store: {
				type: 'FldStore'
			},
			columns: [{
	            text: '序号',
	            dataIndex: 'orderNum',
	            width: 70
	        },{
	            text: "字段名称",
	            dataIndex: 'FieldMc',
	            width: 250,
	            renderer: function(value){
	            	return '<a href="javascript:void(0)">' + value + '</a>'
	            },
	            listeners:{
	            	click:'addOneFld'
	            }
	        },{
	            text: '字段描述',
	            dataIndex: 'fieldDesc',
	            flex:1
	        }],
			bbar: {
				xtype: 'pagingtoolbar',
				pageSize: 8,
	            listeners:{
	                afterrender: function(pbar){
	                    var grid = pbar.findParentByType("grid");
	                    pbar.setStore(grid.store);
	                }
	            },
				plugins: new Ext.ux.ProgressBarPager()
			}
		}]
	}],
    buttons: [{
		text: '关闭',
		iconCls:"close",
		handler: 'onFilterFldWinClose'
	}]
});
