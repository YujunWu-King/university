Ext.define('KitchenSink.view.batch.circulate.batchCirculateDefnPanel', {
    extend: 'Ext.panel.Panel',
    xtype: 'batchCirculateDefnPanel', 
	controller: 'batchCirculateDefnMngController',
	requires: [
	    'Ext.data.*',
        'Ext.grid.*',
        'Ext.util.*',
        'Ext.toolbar.Paging',
	    'KitchenSink.view.batch.circulate.batchCirculateDefnMngController'
	],
	title: '进程循环定义', 
	bodyStyle:'overflow-y:auto;overflow-x:hidden',
	actType: 'add',//默认新增
	items: [{
		xtype: 'form',
		reference: 'batchCirculateDefnForm',
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
            labelWidth: 120,
            labelStyle: 'font-weight:bold'
        },
        items: [{
        	xtype: 'combobox',
			fieldLabel:Ext.tzGetResourse("TZ_BATCH_XH_COM.TZ_BATCH_XHDFN_STD.orgId","归属机构"),
			forceSelection: true,
			editable: false,
			store: new KitchenSink.view.common.store.comboxStore({
				recname: 'TZ_JG_BASE_T',
				condition:{
					TZ_JG_EFF_STA:{
						value:"Y",
						operator:"01",
						type:"01"
					}
				},
				result:'TZ_JG_ID,TZ_JG_NAME'
			}),
            valueField: 'TZ_JG_ID',
            displayField: 'TZ_JG_NAME',
            //typeAhead: true,
            queryMode: 'remote',
			name: 'orgId',
			afterLabelTextTpl: [
                '<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'
            ],
            allowBlank: false
         },{
        	xtype: 'textfield',
        	fieldLabel:Ext.tzGetResourse("TZ_BATCH_XH_COM.TZ_BATCH_XHDFN_STD.batchCirculateName","循环名称"),
        	afterLabelTextTpl: [
        	                    '<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'
        	],
        	allowBlank: false
         },{
        	xtype: 'textfield',
        	fieldLabel:Ext.tzGetResourse("TZ_BATCH_XH_COM.TZ_BATCH_XHDFN_STD.batchCirculateDecs","循环描述"),
  			name: 'batchCirculateDecs'
         },{
        	xtype: 'combobox',
			fieldLabel:Ext.tzGetResourse("TZ_BATCH_XH_COM.TZ_BATCH_XHDFN_STD.tzEeBz","是否有效"),
			editable:false,
			emptyText:'请选择',
			queryMode: 'remote',
			name: 'tzEeBz',
			valueField: 'TValue',
			displayField: 'TSDesc',
			store: new KitchenSink.view.common.store.appTransStore("TZ_EE_BZ")
        },{
      	  xtype: 'tabpanel',
    	  frame: true,
    	  items:[{
    		title: "年份",
    		bodyPadding: 10,
		    layout: {
		      type: 'vbox',
		      align: 'stretch'
		    },
		    bodyStyle:'overflow-y:auto;overflow-x:hidden',
  			fieldDefaults: {
	      		msgTarget: 'side',
	      		labelWidth: 100,
	      		labelStyle: 'font-weight:bold'
  			}, 
  			items: [
  			    {
  			    	xtype: 'fieldcontainer',
  			    	items:[{
  			    		xtype: 	'radio',
						boxLabel  : '不限定，任意年份',
                		name      : 'tzYQzgz',
                		inputValue : '1'
					},{
						xtype: 	'radio',
						boxLabel  : '指定年份范围',
                		name      : 'tzYQzgz',
                		inputValue : '2'
					},{
						xtype: 	'radio',
						boxLabel  : '指定年份列表',
                		name      : 'tzYQzgz',
                		inputValue : '3'
					},{
						xtype: 	'radio',
						boxLabel  : '指定年份循环间隔',
                		name      : 'tzYQzgz',
                		inputValue : '4'
					}]
  			    }
  			]
    	  }]
        }]
	}]
});
