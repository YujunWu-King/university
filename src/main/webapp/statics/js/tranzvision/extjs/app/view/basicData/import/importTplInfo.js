Ext.define('KitchenSink.view.basicData.import.importTplInfo', {
    extend: 'Ext.panel.Panel',
    alias: 'widget.importTplInfo', 
	controller: 'importTplController',
	actType:'',
	requires: [
	    'Ext.data.*',
        'Ext.grid.*',
        'Ext.util.*',
        'Ext.toolbar.Paging'
	],
    title: '导入模板信息', 
	bodyStyle:'overflow-y:auto;overflow-x:hidden',
	listeners:{
        resize:function( panel, width, height, oldWidth, oldHeight, eOpts ){
            var buttonHeight = 36,/*button height plus panel body padding*/
            	formHeight = panel.lookupReference('impTplForm').getHeight(),
            	formPadding = 10,
            	tab = panel.child('tabpanel');
            
            tab.setMinHeight( height- formHeight -buttonHeight-formPadding);
        }
    },
    items: [{
        xtype: 'form',
        reference: 'impTplForm',
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
            fieldLabel: "导入模板编号",
			name: 'tplId',
            afterLabelTextTpl: [
                '<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'
            ],
            allowBlank: false
        }, {
            xtype: 'textfield',
			fieldLabel: "导入模板名称",
			name: 'appClassDesc',
			afterLabelTextTpl: [
                '<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'
            ],
			allowBlank: false
        }, {
            xtype: 'textfield',
			fieldLabel: "目标表名称",
			name: 'targetTbl',
			afterLabelTextTpl: [
                '<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'
            ],
			allowBlank: false
        }, {
            xtype: 'textfield',
			fieldLabel: "导入Java类",
			name: 'javaClass'
        },{
            xtype: 'displayfield',
			value:"此处设置的Java类必须实现接口com.tranzvision.import.UnifiedImportBase",
			fieldStyle:"color:#666;font-weight:bold",
			fieldLabel:" ",
            labelSeparator:""
        }, {
            xtype: 'filefield',
			fieldLabel: "上传Excel模板",
			name: 'excelTpl',
			afterLabelTextTpl: [
                '<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'
            ],
			allowBlank: false,
			triggers:{
                clear: {
                    cls: 'x-form-clear-trigger',
                    handler: function(field){
                        field.setValue("");
                    }
                }
            }
        },{
            xtype: 'checkboxfield',
			fieldLabel: "允许用户调整映射关系",
			name: 'enableMapping',
			inputValue: 'Y',
        }]
    },{
    	xtype:'tabpanel',
    	frame: true,
        plain:false,
        resizeTabs:true,
        defaults :{
            autoScroll: false
        },
    	margin:'0 8',
    	header:false,
    	listeners:{
            tabchange:function(tabPanel, newCard, oldCard){
            	tabPanel.updateLayout();
            }
        },
    	items:[{
    		xtype:'grid',
    		title:'加载字段',
    		store:{
    			fields:["seq"],
    			data:[{seq:1},{seq:2},{seq:3},{seq:4},{seq:5},{seq:6}]
    		},
    		columns:[{
                text: '序号',
                dataIndex: 'seq',
                width: 80
            },{
                text: '字段',
                dataIndex: 'field',
                width: 200
            },{
                text: '字段描述',
                dataIndex: 'fieldName',
                flex:1
            },{
                text: '是否必填',
                xtype:'checkcolumn',
                dataIndex: 'required',
                width: 100
            },{
                text: '是否覆盖',
                xtype:'checkcolumn',
                dataIndex: 'cover',
                width: 100
            },{
                text: '是否前台展示',
                xtype:'checkcolumn',
                dataIndex: 'display',
                width: 120
            }],
            dockedItems:[{
            	xtype:'toolbar',
            	items:[{
            		text:'（重新）加载目标表字段',
            		glyph:"xf265@FontAwesome",
            	}]
            }]
    	},{
    		xtype:'grid',
    		title:'映射关系',
    		store:{
    			fields:["seq"],
    			data:[{seq:123}]
    		},
    		columns:[{
                text: '导入模板列标题',
                dataIndex: 'columnTitle',
                flex:1
            },{
                text: '业务数据目标表字段',
                dataIndex: 'fieldName',
                flex:1
            }]
    	}]
    }],
    buttons: [{
		text: '保存',
		iconCls:"save",
		handler: 'infoSave'
	}, {
		text: '确定',
		iconCls:"ensure",
		handler: 'infoSave'
	}, {
		text: '关闭',
		iconCls:"close",
		handler: function(btn){
			btn.findParentByType("importTplInfo").close();
		}
	}]
});
