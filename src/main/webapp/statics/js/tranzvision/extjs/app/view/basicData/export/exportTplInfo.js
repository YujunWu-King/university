Ext.define('KitchenSink.view.basicData.export.exportTplInfo', {
    extend: 'Ext.panel.Panel',
    alias: 'widget.exportTplInfo',
	controller: 'exportTplController',
	actType:'',
	requires: [
	    'Ext.data.*',
        'Ext.grid.*',
        'Ext.util.*',
        'Ext.toolbar.Paging'
	],
    title: '导出模板信息', 
	bodyStyle:'overflow-y:auto;overflow-x:hidden',
    initComponent:function(){

    	Ext.apply(this,{
    		items: [{
    	        xtype: 'form',
    	        reference: 'expTplForm',
    			layout: {
    	            type: 'vbox',
    	            align: 'stretch'
    	        },
    	        border: false,
    	        bodyPadding: 10,
    			bodyStyle:'overflow-y:auto;overflow-x:hidden',
    			
    	        fieldDefaults: {
    	            msgTarget: 'side',
    	            labelWidth: 140,
    	            labelStyle: 'font-weight:bold'
    	        },
    			
    	        items: [{
    	            xtype: 'textfield',
    	            fieldLabel: "导出模板编号",
    				name: 'tplId',
    	            afterLabelTextTpl: [
    	                '<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'
    	            ],
    	            allowBlank: false
    	        }, {
    	            xtype: 'textfield',
    				fieldLabel: "导出模板名称",
    				name: 'tplName',
    				afterLabelTextTpl: [
    	                '<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'
    	            ],
    				allowBlank: false
    	        },{
    	            xtype: 'textfield',
    				fieldLabel: "导出Java类",
    				name: 'javaClass'
    	        },{
    	            xtype: 'displayfield',
    				value:"此处设置的Java类必须实现接口 com.tranzvision.gd.TZUnifiedExportBundle.service.UnifiedExportBase",
    				fieldStyle:"color:#666;font-weight:bold",
    				fieldLabel:" ",
    	            labelSeparator:""
    	        }]
    	    }]
    	});
    	this.callParent();
    },
    buttons: [{
		text: '保存',
		iconCls:"save",
		name:"save",
		handler: 'infoSave'
	}, {
		text: '确定',
		iconCls:"ensure",
		name:"ensure",
		handler: 'infoSave'
	}, {
		text: '关闭',
		iconCls:"close",
		handler: function(btn){
			btn.findParentByType("exportTplInfo").close();
		}
	}]
});
