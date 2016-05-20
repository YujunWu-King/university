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
  			items: [{
					xtype: 	'radio',
					boxLabel  : '不限定，任意年份',
					name      : 'tzYQzgz',
					inputValue : '1'
				},{
					layout: {
						type: 'column'
					},
					items: [{
						columnWidth:.2,
						xtype: 	'radio',
						boxLabel  : '指定年份范围',
						name      : 'tzYQzgz',
						inputValue : '2'
					},{
						columnWidth:.3,
			        	xtype: 'combobox',
			        	labelWidth: 80,
						fieldLabel:Ext.tzGetResourse("TZ_BATCH_XH_COM.TZ_BATCH_XHDFN_STD.tzYQsnf","起始年份"),
						editable:false,
						emptyText:'请选择',
						queryMode: 'remote',
						style:'margin-left:5px',
						name: 'tzYQsnf',
						valueField: 'TValue',
						displayField: 'TSDesc',
						store: new KitchenSink.view.common.store.appTransStore("TZ_Y_QSNF")
			        },{
						columnWidth:.3,
			        	xtype: 'combobox',
			        	labelWidth: 80,
			        	fieldLabel:Ext.tzGetResourse("TZ_BATCH_XH_COM.TZ_BATCH_XHDFN_STD.tzYJznf","截止年份"),
						editable:false,
						emptyText:'请选择',
						queryMode: 'remote',
						style:'margin-left:5px',
						name: 'tzYJznf',
						valueField: 'TValue',
						displayField: 'TSDesc',
						store: new KitchenSink.view.common.store.appTransStore("TZ_Y_JZNF")
			        }]	
				},{
					layout: {
						type: 'column'
					},
					style:'margin-top:10px',
					items: [{
						columnWidth:.2,
						xtype: 	'radio',
						boxLabel  : '指定年份列表',
						name      : 'tzYQzgz',
						inputValue : '3'
					},{	
						columnWidth:.6,
						xtype: 'textfield',
						//style:'margin-left:5px',
						name: 'tzYLbqz',
						listeners: {
							render: function(tzYLbqz){
								tzYLbqz.inputEl.dom.placeholder="格式：YYYY,…  取值范围：1970-2099  例如：2001,2002";
		                	}
		                }
					}]	
				},{
					layout: {
						type: 'column'
					},
					style:'margin-top:10px',
					items: [{
						columnWidth:.2,
						xtype: 	'radio',
						boxLabel  : '指定年份循环间隔',
						name      : 'tzYQzgz',
						inputValue : '4'
					},{	
						columnWidth:.6,
						xtype: 'textfield',
						name: 'tzYXhqz',
						listeners: {
							render: function(tzYXhqz){
		                		tzYXhqz.inputEl.dom.placeholder="格式：YYYY/N  取值范围：1970-2099  例如：2001/4";
		                	}
		                }
					}]	
				}]
    	  	},{
        		title: "月份",
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
      			items: [{
    					xtype: 	'radio',
    					boxLabel  : '不限定，任意月份',
    					name      : 'tzM1Qzgz',
    					inputValue : '1'
    				},{
    					layout: {
    						type: 'column'
    					},
    					items: [{
    						columnWidth:.2,
    						xtype: 	'radio',
    						boxLabel  : '指定月份范围',
    						name      : 'tzM1Qzgz',
    						inputValue : '2'
    					},{
    						columnWidth:.3,
    			        	xtype: 'combobox',
    			        	labelWidth: 80,
    						fieldLabel:Ext.tzGetResourse("TZ_BATCH_XH_COM.TZ_BATCH_XHDFN_STD.tzM1Qsyf","起始月份"),
    						editable:false,
    						emptyText:'请选择',
    						queryMode: 'remote',
    						style:'margin-left:5px',
    						name: 'tzM1Qsyf',
    						valueField: 'TValue',
    						displayField: 'TSDesc',
    						store: new KitchenSink.view.common.store.appTransStore("TZ_M1_QSYF")
    			        },{
    						columnWidth:.3,
    			        	xtype: 'combobox',
    			        	labelWidth: 80,
    			        	fieldLabel:Ext.tzGetResourse("TZ_BATCH_XH_COM.TZ_BATCH_XHDFN_STD.tzM1Jzyf","截止月份"),
    						editable:false,
    						emptyText:'请选择',
    						queryMode: 'remote',
    						style:'margin-left:5px',
    						name: 'tzM1Jzyf',
    						valueField: 'TValue',
    						displayField: 'TSDesc',
    						store: new KitchenSink.view.common.store.appTransStore("TZ_M1_JZYF")
    			        }]	
    				},{
    					layout: {
    						type: 'column'
    					},
    					style:'margin-top:10px',
    					items: [{
    						columnWidth:.2,
    						xtype: 	'radio',
    						boxLabel  : '指定月份列表',
    						name      : 'tzM1Qzgz',
    						inputValue : '3'
    					},{	
    						columnWidth:.6,
    						xtype: 'textfield',
    						//style:'margin-left:5px',
    						name: 'tzM1Lbqz',
    						listeners: {
    							render: function(tzYLbqz){
    								tzYLbqz.inputEl.dom.placeholder="格式：N1,N2,…  取值范围：1-12  例如：1,2,3";
    		                	}
    		                }
    					}]	
    				},{
    					layout: {
    						type: 'column'
    					},
    					style:'margin-top:10px',
    					items: [{
    						columnWidth:.2,
    						xtype: 	'radio',
    						boxLabel  : '指定月份循环间隔',
    						name      : 'tzM1Qzgz',
    						inputValue : '4'
    					},{	
    						columnWidth:.6,
    						xtype: 'textfield',
    						name: 'tzM1Xhqz',
    						listeners: {
    							render: function(tzYXhqz){
    		                		tzYXhqz.inputEl.dom.placeholder="格式：M/N  取值范围：1-12  例如：5/4";
    		                	}
    		                }
    					}]	
    				}]
        	  	}]
        }]
	}]
});
