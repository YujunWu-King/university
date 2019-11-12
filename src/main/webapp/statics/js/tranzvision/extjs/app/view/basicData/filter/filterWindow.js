Ext.define('KitchenSink.view.basicData.filter.filterWindow', {
    extend: 'Ext.window.Window',
    xtype: 'filterWindow', 
    title: '新增可配置搜索', 
	reference: 'filterWindow',
    width: 400,
    height: 240,
    minWidth: 200,
    minHeight: 100,
    layout: 'fit',
    resizable: true,
    modal: true,
    closeAction: 'hide',
	actType: 'add',
	
	items: [{
		xtype: 'form',	
		layout: {
			type: 'vbox',
			align: 'stretch'
		},
		border: false,
		bodyPadding: 10,
		//heigth: 600,
	
		fieldDefaults: {
			msgTarget: 'side',
			labelWidth: 80,
			labelStyle: 'font-weight:bold'
		},
		items: [
            {
                bodyStyle:'padding:0 0 10px 0',
                xtype: 'textfield',
                fieldLabel: Ext.tzGetResourse("TZ_GD_FILTER_COM.TZ_GD_FILTERTZ_STD.ComID","组件编号"),
                name: 'ComID',
                editable: false,
                triggers: {
                    search: {
                        cls: 'x-form-search-trigger',
                        handler: "pmtSearchComIDTmp"
                    }
                }
        },{
                bodyStyle:'padding:0 0 10px 0',
                columnWidth:.8,
                xtype: 'textfield',
                fieldLabel: Ext.tzGetResourse("TZ_GD_FILTER_COM.TZ_GD_FILTERTZ_STD.PageID","页面编号"),
                name: 'PageID',
                editable: false,
                triggers: {
                    search: {
                        cls: 'x-form-search-trigger',
                        handler: "pmtSearchPageIDTmp"
                    }
                }
            },
            {
			xtype: 'textfield',
			fieldLabel: '视图名称',
			name: 'ViewMc',
			maxLength: 18,
			allowBlank: false
		},{
			xtype: 'textfield',
			fieldLabel: '程序类名称',
			name: 'appClassMc',
			maxLength: 18,
			allowBlank: true,
			hidden:true
		},{
			xtype: 'combobox',
			fieldLabel: '类型名称',
			name: 'typeName',
			maxLength: 18,
			allowBlank: false,
			editable:false,
			value: '0',
	        queryMode: 'remote',
	    	valueField: 'TValue',
    		displayField: 'TSDesc',
    		store: new KitchenSink.view.common.store.appTransStore("TZ_SEARCH_TYPE"),
    		listeners:{
				change:function(ts, newvalue, oldvalue){
					var typeName = ts.findParentByType("form").down('textfield[name=typeName]').getValue();
					if(typeName == 0){
						ts.findParentByType("form").down('textfield[name=ViewMc]').allowBlank = false;
						ts.findParentByType("form").down('textfield[name=appClassMc]').allowBlank = true;
						ts.findParentByType("form").down('textfield[name=ViewMc]').setHidden(false);
						ts.findParentByType("form").down('textfield[name=appClassMc]').setHidden(true);
					}
					if(typeName == 1){
						ts.findParentByType("form").down('textfield[name=appClassMc]').allowBlank = false;
						ts.findParentByType("form").down('textfield[name=ViewMc]').allowBlank = true;
						ts.findParentByType("form").down('textfield[name=appClassMc]').setHidden(false);
						ts.findParentByType("form").down('textfield[name=ViewMc]').setHidden(true);
					}
				}
			}
		}]
	}],
    buttons: [{
		text: '确定',
		iconCls:"ensure",
		handler: 'onFilterWinEnsure'
	}, {
		text: '取消',
		iconCls:"close",
		handler: 'onFilterWinClose'
	}]
});
