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
