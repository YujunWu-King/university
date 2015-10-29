Ext.define('KitchenSink.view.enrollmentManagement.exportTemplate.exportFieldSetWindow', {
    extend: 'Ext.window.Window',
    xtype:'exportFieldSetWindow',
	reference: 'exportFieldSetWindow',
	requires: [
        'Ext.data.*',
        'Ext.grid.*',
        'Ext.util.*',
        'Ext.toolbar.Paging'
    ],
    title: '导出字段设置',
	width: 900,
    y:10,
    height:450,
    ignoreChangesFlag: true,
    bodyStyle:'overflow-y:auto;overflow-x:hidden;',
    resizable: true,
    modalID:'',/*报名表模板ID*/
    record:'',
    listeners:{
        resize: function(win){
            win.doLayout();
        }
    },
    modal: true,
	items: [{
        xtype: 'form',
        layout: {
            type: 'vbox',
            align: 'stretch'
        },
        fieldDefaults: {
            msgTarget: 'side',
            labelStyle: 'font-weight:bold'
        },
        border: false,
        bodyPadding: 10,
        bodyStyle:'overflow-y:auto;overflow-x:hidden',

        fieldDefaults: {
            msgTarget: 'side',
            labelWidth: 100,
            labelStyle: 'font-weight:bold'
        },        items: [{
            xtype: 'textfield',
            fieldLabel: '导出字段名称',
            readOnly:true,
            cls:'lanage_1',
            name: 'fieldName'
        },{
            xtype: 'textfield',
            fieldLabel: '数据分隔符',
            name: 'separator'
        },{
            xtype: 'fieldset',
            title: '<span style="font-weight: bold;" class="themeColor">导出字段属性设置</span>',
            name:'fieldAttribute',
            defaults: {
                anchor: '100%',
                layout: {
                    type: 'hbox',
                    defaultMargins: {top: 0, right: 5, bottom: 0, left: 0}
                }
            },
            items:[
                {
                    xtype: 'numberfield',
                    fieldLabel: '列宽',
                    minValue:100,
                    allowDecimals:false,
                    name: 'columnWidth'
                },{
                    xtype: 'combobox',
                    fieldLabel: '筛选类型',
                    displayField:'filterName',
                    valueField:'filter',
                    editable:false,
                    name: 'filter',
                    store:{
                        fields:['filter','filterName'],
                        data:[
                            {filter:'string',filterName:'字符串'},
                            {filter:'list',filterName:'列表'},
                            {filter:'number',filterName:'数字'},
                            {filter:'boolean',filterName:'布尔型（不为空/为空）'},
                            {filter:'date',filterName:'日期'}
                        ]
                    }
                }
            ]
        }
        ]
    },{
		xtype: 'grid',
        title:'报名表字段',
        height:250,
        bodyStyle:'overflow-y:auto;overflow-x:hidden;',
		viewConfig: {
			enableTextSelection: true
		},
		columnLines: true,
	    dockedItems:[{
		   xtype:"toolbar",
		   items:[
			{text:"新增",tooltip:"新增数据",iconCls:"add",handler:'addAppFormField'}
		   ]
	    }], 
		
		plugins: {
							ptype: 'cellediting',
							pluginId: 'dataCellediting',
							clicksToEdit: 1
				 },
		
		viewConfig: {
			plugins: {
				ptype: 'gridviewdragdrop',
				containerScroll: true,
				dragGroup: this,
				dropGroup: this
			}
		},
	
        columns: [
            {   xtype:'rownumberer',
                dataIndex: 'appFormFieldSeq',
                text:'序号',
                width: 50
            },{
                text: "报名表字段值",
                dataIndex: 'appFormField',
                sortable:false,
                width: 150,
                editor: {
                    xtype:'textfield',
                    editable:false,
                    triggers: {
                        search: {
                            cls: 'x-form-search-trigger',
                            handler: "selectAppFormField"
                        }
                    }
                }
		},{
                text: "报名表字段名称",
                dataIndex: 'appFormFieldName',
                flex:1,
                sortable:false,
                minWidth: 120
		},{
                text: "码表类别",
                dataIndex: 'codeTable',
                sortable:false,
                width: 150,
                editor: {
                    xtype:'textfield',
                    editable:false,
                    triggers: {
                        search: {
                            cls: 'x-form-search-trigger',
                            handler: "selectAppFormCodeTable"
                        }
                    }
                }
        },{
		    text: "描述",
			dataIndex: 'codeTableName',
			minWidth: 80,
            sortable: false,
            flex:1
		},{
						menuDisabled: true,
						sortable: false,
						width:70,
						xtype: 'actioncolumn',
						text: '操作',
						align: 'left',
						items:[
                            '-',
							{iconCls: 'remove',tooltip: '删除', handler: 'removeField'}
						]
		}],	//columns-end
		store: {
            fields:['appFormFieldSeq','appFormField','appFormFieldName','codeTable','codeTableName'],
            pageSize:100
        }
	}],		  

    buttons: [{
		text: '确定',
		iconCls:"ensure",
        handler:function(btn){
            var win = btn.findParentByType("window");
            var rec = btn.findParentByType('exportFieldSetWindow').record;
            var appFormFieldForm = win.child("form").getForm();
            var appFormFieldStore =win.child("grid").getStore();

            var appFormFieldArray = [];
            appFormFieldStore.each(function(record){
                record.set('appFormFieldSeq',appFormFieldStore.indexOf(record)+1);
                appFormFieldArray.push(record.data);
            });
            rec.set('separator',appFormFieldForm.findField('separator').getValue());
            rec.set('columnWidth',appFormFieldForm.findField('columnWidth').getValue());
            rec.set('filter',appFormFieldForm.findField('filter').getValue());
            rec.set('appFormField',appFormFieldArray);
            appFormFieldStore.commitChanges();
            btn.findParentByType('window').close();
        }
	}, {
		text: '关闭',
		iconCls:"close",
        handler:function(btn){
            btn.findParentByType("window").close();
        }
	}]
});
