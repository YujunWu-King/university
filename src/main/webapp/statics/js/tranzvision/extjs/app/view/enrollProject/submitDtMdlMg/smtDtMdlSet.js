Ext.define('KitchenSink.view.enrollProject.submitDtMdlMg.smtDtMdlSet', {
    extend: 'Ext.panel.Panel',
	xtype: 'smtDtMdlSet',
   // reference:'smtDtMdlSet',
	controller: 'submitDataModelMg',
	actType:'add',//默认
	requires: [
	    'Ext.data.*',
        'Ext.grid.*',
        'Ext.util.*',
        'Ext.toolbar.Paging',
        'Ext.ux.ProgressBarPager',
        'KitchenSink.view.enrollProject.submitDtMdlMg.smtDtMdlSetStore',
		'KitchenSink.view.enrollProject.submitDtMdlMg.smtDtMdlController',
		'tranzvision.extension.grid.column.Link'
	],
    title:Ext.tzGetResourse("TZ_GD_SMTDTMDL_COM.TZ_GD_SMTDTSET_STD.djzlmxsz","递交资料模型设置") ,
    bodyStyle:'overflow-y:auto;overflow-x:hidden',
	items: [{
        xtype: 'form',
        reference: 'smtDtMdlSetForm',
		layout: {
            type: 'vbox',
            align: 'stretch'
        },
        border: false,
        bodyPadding: 10,
		//heigth: 600,
		bodyStyle:'overflow-y:auto;overflow-x:hidden',
		
        fieldDefaults: {
            msgTarget: 'side',
            labelWidth: 100,
            labelStyle: 'font-weight:bold'
        },  
	    items: [{
            xtype: 'textfield',
			fieldLabel: Ext.tzGetResourse("TZ_GD_SMTDTMDL_COM.TZ_GD_SMTDTSET_STD.smtDtTmpID","递交资料模型ID"),
			labelWidth: 140,
			hidden: true,
			name: 'smtDtTmpID'
        },{
            xtype: 'textfield',
			fieldLabel: Ext.tzGetResourse("TZ_GD_SMTDTMDL_COM.TZ_GD_SMTDTSET_STD.smtDtName","递交资料模型名称"),
			labelWidth: 140,
			name: 'smtDtName',
            afterLabelTextTpl: [
                '<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'
            ],
            allowBlank: false
        },{
            xtype: 'combobox',
            fieldLabel: Ext.tzGetResourse("TZ_GD_SMTDTMDL_COM.TZ_GD_SMTDTSET_STD.smtDtStatus","状态"),
            forceSelection: true,
            valueField: 'TValue',
            displayField: 'TSDesc',
            labelWidth:140,
            store: new KitchenSink.view.common.store.appTransStore("TZ_SBMINF_STATUS"),
            typeAhead: true,
            queryMode: 'local',
            name: 'smtDtStatus',
            value:'Y'
        }]
    },{
		xtype: 'grid', 
		title: Ext.tzGetResourse("TZ_GD_SMTDTMDL_COM.TZ_GD_SMTDTSET_STD.djzl","递交资料"),
        height:340,
		frame: true,
		columnLines: true,
		reference:'smtDtMdlSetGrid',
		style:"margin:0 10px 10px 10px",
		bodyStyle:'overflow-y:auto;overflow-x:hidden',
	    dockedItems:[{
		   xtype:"toolbar",
		   items:[
			//{text:"新增",tooltip:"新增数据",iconCls:"add",handler:'addDataInfo'},"-"
               {text: Ext.tzGetResourse("TZ_GD_SMTDTMDL_COM.TZ_GD_SMTDTSET_STD.add","新增"),iconCls:"add",handler:'addSmtDataInfo'},"-",
               {text: Ext.tzGetResourse("TZ_GD_SMTDTMDL_COM.TZ_GD_SMTDTSET_STD.edit","编辑"),iconCls:'edit',handler:'editSmtDataInfo'},"-",
               {text: Ext.tzGetResourse("TZ_GD_SMTDTMDL_COM.TZ_GD_SMTDTSET_STD.delete","删除"),iconCls:'remove',handler:'removeSmtDataInfo'},"->"
		   ]
	    }],
        selModel: {
            type: 'checkboxmodel'
        },
		//不允许行编辑
	/*	plugins: {
							ptype: 'cellediting',
							pluginId: 'dataCellediting',
							clicksToEdit: 1
				 },
				 */
		viewConfig: {
			plugins: {
				ptype: 'gridviewdragdrop',
				containerScroll: true,
				dragGroup: this,
				dropGroup: this
			},
			listeners: {
				drop: function(node, data, dropRec, dropPosition) {
					data.view.store.beginUpdate();
					var items = data.view.store.data.items;
					for(var i = 0;i< items.length;i++){
						items[i].set('order',i+1);
					}
					data.view.store.endUpdate();
				}
			}
		},
	
        columns: [{
		    text: Ext.tzGetResourse("TZ_GD_SMTDTMDL_COM.TZ_GD_SMTDTSET_STD.content","内容简介"),
			dataIndex: 'content',
			minWidth: 120,
            flex:1,
			editor: {
                 xtype:'textfield'
            }
		},{ 
			text: Ext.tzGetResourse("TZ_GD_SMTDTMDL_COM.TZ_GD_SMTDTSET_STD.remark","备注"),
			dataIndex: 'remark',
            minWidth: 120,
            flex:1,
			editor: {
                 xtype:'textfield'
            }
		},{
            	xtype:'linkcolumn',               
                sortable: false,
                width: 155,
                text: Ext.tzGetResourse("TZ_GD_SMTDTMDL_COM.TZ_GD_SMTDTSET_STD.cyhfdysz","常用回复短语设置"),
                items:[{
                	text:Ext.tzGetResourse("TZ_GD_SMTDTMDL_COM.TZ_GD_SMTDTSET_STD.cyhfdysz","常用回复短语设置"),
					handler:'editBackMsg'
                }]
    },{
            menuDisabled: true,
            sortable: false,
            width:80,
            xtype: 'actioncolumn',
            align: 'center',
            items:[
                {iconCls:'edit',tooltip: Ext.tzGetResourse("TZ_GD_SMTDTMDL_COM.TZ_GD_SMTDTSET_STD.edit","编辑"),handler:'editData'},
                {iconCls: 'remove',tooltip:  Ext.tzGetResourse("TZ_GD_SMTDTMDL_COM.TZ_GD_SMTDTSET_STD.delete","删除"), handler: 'deleteData'}

            ]
		}],	//columns-end
		store: {
					type: 'smtDtMdlSetStore'
			   },
		bbar: {
				xtype: 'pagingtoolbar',
				pageSize: 10,
				listeners:{
				afterrender: function(pbar){
					var grid = pbar.findParentByType("grid");
					pbar.setStore(grid.store);
				    }
			    },
				reference: 'smtDtMdlSetToolBar',
				plugins: new Ext.ux.ProgressBarPager()
		}	
	}],
	
	buttons: [{
		text:  Ext.tzGetResourse("TZ_GD_SMTDTMDL_COM.TZ_GD_SMTDTSET_STD.save","保存"),
		iconCls:"save",
		handler: 'onDtMdlSetSave'
	}, {
		text:  Ext.tzGetResourse("TZ_GD_SMTDTMDL_COM.TZ_GD_SMTDTSET_STD.ensure","确定"),
		iconCls:"ensure",
		handler: 'onDtMdlSetEnsure'
	}, {
		text: Ext.tzGetResourse("TZ_GD_SMTDTMDL_COM.TZ_GD_SMTDTSET_STD.close","关闭"),
		iconCls:"close",
		handler: 'onDtMdlSetClose'
	}]
});