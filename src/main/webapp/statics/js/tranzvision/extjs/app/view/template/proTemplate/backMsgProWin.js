Ext.define('KitchenSink.view.template.proTemplate.backMsgProWin', {
    //extend: 'Ext.window.Window',
    extend: 'Ext.panel.Panel',
	reference: 'bmlcmbbackMsgWindow',
    controller: 'proTemplate',
	requires: [
        'Ext.data.*',
        'Ext.grid.*',
        'Ext.util.*',
        'Ext.toolbar.Paging',
        'Ext.ux.colorpick.Field',
        'Ext.ux.ProgressBarPager',
		'KitchenSink.view.template.proTemplate.probackMsgStore'

    ],

	actType : "update",
    title:Ext.tzGetResourse("TZ_PM_BMLCMBGL_COM.TZ_PM_BMLCMB_STD.cyhfdxsz","常用回复短语设置"),
    //frame: true,

    TZ_APPPRO_TMP_ID:'',
    TZ_APPPRO_ID:'',
    resizable: true,
    modal: true,
	multiSel: '',
	rowNum: 0,
    bodyStyle:'overflow-y:auto;overflow-x:hidden',
	items: [{
		xtype: 'grid',

		height: 430,
		viewConfig: {
			enableTextSelection: true
		},
        autoHeight:true,
		columnLines: true,
		reference:'probackMsgGrid',
		style:"margin:8px",
        header:false,
        frame: true,
        selModel: {
            type: 'checkboxmodel'
        },
	    dockedItems:[{
		   xtype:"toolbar",
		   items:[
			{text:Ext.tzGetResourse("TZ_PM_BMLCMBGL_COM.TZ_PM_BMLCMB_STD.add","新增"),iconCls:"add",handler:'addDatabBackInfo'},'-',
               {text:Ext.tzGetResourse("TZ_PM_BMLCMBGL_COM.TZ_PM_BMLCMB_STD.edit","编辑"),iconCls:"edit",handler:'editDatabBackInfo'}
               ,'-', {text:Ext.tzGetResourse("TZ_PM_BMLCMBGL_COM.TZ_PM_BMLCMB_STD.delete","删除"),iconCls:"remove",handler:'deleteDatabBackInfo'}
               ,'->'
		   ]
	    }],

        columns: [{
            text: Ext.tzGetResourse("TZ_PM_BMLCMBGL_COM.TZ_PM_BMLCMB_STD.color","颜色"),
            width:200,
            bind: '{color}',
            editable:false,
            dataIndex: 'TZ_APPPRO_COLOR',
            flex:1,
            /*editor: {
             xtype: 'colorfield',
             allowBlank: false
             },*/
            renderer:function(value){
                return "<div class='x-colorpicker-field-swatch-inner'' style='width:80%;height:50%;background-color: #"+value+"'></div>"+value;
            }
        },{
		    text: Ext.tzGetResourse("TZ_PM_BMLCMBGL_COM.TZ_GD_BACKMSG_STD.TZ_CLS_RESULT","结果"),
			dataIndex: 'TZ_CLS_RESULT',
			width: 200,
            flex:1,

			editor: {
                 xtype:'textfield'
            }
		},{			    menuDisabled: true,
						sortable: false,
						width:60,
						xtype: 'actioncolumn',
						align: 'center',

						items:[
							//{iconCls: 'add',tooltip: '添加', handler: 'addBackMsg'},
                        {iconCls: 'edit',tooltip: Ext.tzGetResourse("TZ_PM_BMLCMBGL_COM.TZ_PM_BMLCMB_STD.edit","编辑"),handler:'editBackMsgDataInfo'},
						{iconCls: 'remove',tooltip: Ext.tzGetResourse("TZ_PM_BMLCMBGL_COM.TZ_PM_BMLCMB_STD.delete","删除"), handler: 'deleteBackMsg'}
						]
		}],	//columns-end
		store: {
					type: 'probackMsgStore'
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
				reference: 'probackMsgToolBar',
				plugins: new Ext.ux.ProgressBarPager()
		}	
	}],		  

    buttons: [{
		text: Ext.tzGetResourse("TZ_PM_BMLCMBGL_COM.TZ_PM_BMLCMB_STD.save","保存"),
		iconCls:"save",
		handler: 'onBackMsgSave'
	},{
        text: Ext.tzGetResourse("TZ_PM_BMLCMBGL_COM.TZ_PM_BMLCMB_STD.ensure","确定"),
        iconCls:"ensure",
        handler: 'onBackMsgSure'
    }, {
		text:Ext.tzGetResourse("TZ_PM_BMLCMBGL_COM.TZ_PM_BMLCMB_STD.close","关闭"),
		iconCls:"close",
		handler: 'onBackMsgClose'
	}]
});
