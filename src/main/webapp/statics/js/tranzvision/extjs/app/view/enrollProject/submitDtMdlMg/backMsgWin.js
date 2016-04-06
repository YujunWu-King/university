Ext.define('KitchenSink.view.enrollProject.submitDtMdlMg.backMsgWin', {
    extend: 'Ext.window.Window',
	reference: 'backMsgWindow',
	requires: [
        'Ext.data.*',
        'Ext.grid.*',
        'Ext.util.*',
        'Ext.toolbar.Paging',
        'Ext.ux.ProgressBarPager',
		'KitchenSink.view.enrollProject.submitDtMdlMg.backMsgStore'
    ],
    title: Ext.tzGetResourse("TZ_GD_SMTDTMDL_COM.TZ_GD_SMTDTSET_STD.cyhfdysz","常用回复短语设置"),
	actType : "add",//默认add
	width: 610,
    height: 400,
    layout: 'fit',
	smtDtTmpID:'',
	smtDtID:'',
    resizable: true,
    modal: true,
	multiSel: '',
	rowNum: 0,
	items: [{
		xtype: 'grid',
		height: 315, 
		viewConfig: {
			enableTextSelection: true
		},
		columnLines: true,
		reference:'backMsgGrid',
	    dockedItems:[{
		   xtype:"toolbar",
		   items:[
			{text:Ext.tzGetResourse("TZ_GD_SMTDTMDL_COM.TZ_GD_SMTDTSET_STD.add","新增"),iconCls:"add",handler:'addLastBackMsg'},"-"
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
		    text: Ext.tzGetResourse("TZ_GD_SMTDTMDL_COM.TZ_GD_BAKEMSG_STD.smtDtTmpID","递交资料模型ID"),
			dataIndex: 'smtDtTmpID',
			hidden: true
		},{ 
		    text: Ext.tzGetResourse("TZ_GD_SMTDTMDL_COM.TZ_GD_BAKEMSG_STD.smtDtID","递交资料ID"),
			dataIndex: 'smtDtID',
			minWidth: 100,
			hidden: true
		},{
		    text: Ext.tzGetResourse("TZ_GD_SMTDTMDL_COM.TZ_GD_BAKEMSG_STD.msgId","回复短语编号"),
			dataIndex: 'msgId',
			width: 110,
			hidden: true
        },{
		    text: Ext.tzGetResourse("TZ_GD_SMTDTMDL_COM.TZ_GD_BAKEMSG_STD.msgContent","回复短语内容"),
			dataIndex: 'msgContent',
			minWidth: 300,
            flex:1,
			editor: {
                 xtype:'textfield',
				 allowBlank: false,
				 afterLabelTextTpl: [
					'<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'
				 ]
            }
		},{
						menuDisabled: true,
						sortable: false,
						width:60,
						xtype: 'actioncolumn',
						align: 'center',
						items:[
							{iconCls: 'remove',tooltip: Ext.tzGetResourse("TZ_GD_SMTDTMDL_COM.TZ_GD_SMTDTSET_STD.delete","删除"), handler: 'deleteBackMsg'}
						]
		}],	//columns-end
		store: {
					type: 'backMsgStore'
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
				reference: 'backMsgToolBar',
				plugins: new Ext.ux.ProgressBarPager()
		}	
	}],		  

    buttons: [{
		text: Ext.tzGetResourse("TZ_GD_SMTDTMDL_COM.TZ_GD_SMTDTSET_STD.save","保存"),
		iconCls:"save",
		handler: 'onBackMsgSave'
	},{
        text: Ext.tzGetResourse("TZ_GD_SMTDTMDL_COM.TZ_GD_SMTDTSET_STD.ensure","确定"),
        iconCls:"ensure",
        handler: 'onBackMsgSure1'
    }, {
		text: Ext.tzGetResourse("TZ_GD_SMTDTMDL_COM.TZ_GD_SMTDTSET_STD.close","关闭"),
		iconCls:"close",
		handler: 'onBackMsgClose'
	}]
});
