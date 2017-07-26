Ext.define('KitchenSink.view.basicData.filter.filterDataSetCondWindow', {
    extend: 'Ext.window.Window',
    requires: [
	    'Ext.data.*',
        'Ext.grid.*',
        'Ext.util.*',
        'Ext.toolbar.Paging',
        'Ext.ux.ProgressBarPager',
		'KitchenSink.view.basicData.filter.FldStore'
	],
    xtype: 'filterWindow', 
    title: '查询条件', 
	reference: 'filterDataSetCondWindow',
    y:20,
    width: 740,
    //maxHeight: 500,
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
        bodyPadding: 10,
		//heigth: 600,
		bodyStyle:'overflow-y:auto;overflow-x:hidden',
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
			name: 'ViewMc'
		},{
			xtype: 'hiddenfield',
			fieldLabel: '字段序号',
			name: 'fieldOrder'
		},{
			xtype: 'hiddenfield',
			fieldLabel: '条件序号',
			name: 'orderCond'
		},{
			xtype: 'textfield',
			fieldLabel: '搜索记录名称',
			name: 'searchRec',
			readOnly:true,
			cls:"lanage_1"
		},{
			xtype: 'textfield',
			fieldLabel: '数据集描述',
			name: 'fldDstDesc',
			readOnly:true,
			cls:"lanage_1"
		},{
        	 xtype: 'combobox',
        	 fieldLabel: '条件与/或',
        	 valueField: 'TValue',
             displayField: 'TLDesc',
             store: new KitchenSink.view.common.store.appTransStore("TZ_FLTDST_AND_OR"),
             queryMode: 'local',
    		 name: 'dstAndOr'
    	},{
        	 xtype: 'combobox',
        	 fieldLabel: '左括号',
        	 valueField: 'TValue',
             displayField: 'TLDesc',
             store: new KitchenSink.view.common.store.appTransStore("TZ_FLTDST_L_PAREN"),
             queryMode: 'local',
    		 name: 'leftParen'
    	},{								
			xtype: 'textfield',
			fieldLabel: "条件字段",
			name: 'dstCondFld',
			triggers: {
			  search: {
				 cls: 'x-form-search-trigger',
				 handler: "searchDataSetCondFld"
			  }
			}				
		},{
        	 xtype: 'combobox',
        	 fieldLabel: '操作符',
        	 valueField: 'TValue',
             displayField: 'TLDesc',
             store: new KitchenSink.view.common.store.appTransStore("TZ_FLTDST_OPERATOR"),
             queryMode: 'local',
    		 name: 'dstOperator'
    	},{
        	xtype:'radiogroup',
        	fieldLabel:'字段取值类型',
        	items: [{
        		name: 'dstCondValueType',
        		inputValue: 'A',
        		boxLabel: '当前登录用户',
        		listeners:{
        			"change":function(el,checked){
        				if(checked){
        					el.findParentByType("form").getForm().findField('dstCondFldValue').setValue("%userid");
							el.findParentByType("form").getForm().findField('dstCondFldValue').setReadOnly(true);
							el.findParentByType("form").getForm().findField('dstCondFldValue').setFieldStyle('background:#F4F4F4');
        				}
        			}
        		}
        	},{
        		name: 'dstCondValueType',
        		inputValue: 'B',
        		boxLabel: '当前系统时间',
        			listeners:{
            			"change":function(el,checked){
            				if(checked){
								el.findParentByType("form").getForm().findField('dstCondFldValue').setValue("%datetime");
								el.findParentByType("form").getForm().findField('dstCondFldValue').setReadOnly(true);
								el.findParentByType("form").getForm().findField('dstCondFldValue').setFieldStyle('background:#F4F4F4');
            				}
            			}
            		}
        	},{
        		name: 'dstCondValueType',
        		inputValue: 'C',
				checked: true,
        		boxLabel: '常量',
        			listeners:{
            			"change":function(el,checked){
            				if(checked){
            					el.findParentByType("form").getForm().findField('dstCondFldValue').setValue("");
								el.findParentByType("form").getForm().findField('dstCondFldValue').setReadOnly(false);
								el.findParentByType("form").getForm().findField('dstCondFldValue').setFieldStyle('background:#FFFFFF');
            				}
            			}
            		}
        	}]
        },{
			xtype: 'textfield',
			fieldLabel: '字段取值',
			name: 'dstCondFldValue'
		},{
        	 xtype: 'combobox',
        	 fieldLabel: '右括号',
        	 valueField: 'TValue',
             displayField: 'TLDesc',
             store: new KitchenSink.view.common.store.appTransStore("TZ_FLTDST_R_PAREN"),
             queryMode: 'local',
    		 name: 'rightParen'
    	},{
       	 xtype: 'combobox',
    	 fieldLabel: '搜索字段不区分大小写',
    	 valueField: 'TValue',
         displayField: 'TLDesc',
         store: new KitchenSink.view.common.store.appTransStore("TZ_NO_UPORLOW"),
         queryMode: 'local',
		 name: 'fltFldNoUpperLower'
	 }]
	}],
    buttons: [{
		text: '确定',
		iconCls:"ensure",
		handler: 'onFilterDataSetCondWinEnsure'
	},{
		text: '关闭',
		iconCls:"close",
		handler: 'onFilterDataSetCondWinClose'
	}]
});
