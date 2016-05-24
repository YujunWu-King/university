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
    							render: function(tzM1Lbqz){
    								tzM1Lbqz.inputEl.dom.placeholder="格式：N1,N2,…  取值范围：1-12  例如：1,2,3";
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
    						render: function(tzM1Xhqz){
    							tzM1Xhqz.inputEl.dom.placeholder="格式：M/N  取值范围：1-12  例如：5/4";
    						}
    					}
    				}]	
    			}]
        	  },{
            		title: "日/周",
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
        				boxLabel  : '按“日-月”模式循环',
        				name      : 'tzRzRyBz',
        				inputValue : '1'
        			},{
        	            xtype: 'fieldcontainer',
        	            bodyPadding: 10,
            		    layout: {
            		      type: 'vbox',
            		      align: 'stretch'
            		    },
            		    bodyStyle:'overflow-y:auto;overflow-x:hidden',
              			fieldDefaults: {
            	      		msgTarget: 'side',
            	      		labelWidth: 30,
            	      		labelStyle: 'font-weight:bold;'
              			}, 
              			style: 'margin-left:30px;',
        	            items: [{
            				xtype: 	'radio',
            				boxLabel  : '不限定，任意日期',
            				name      : 'tzDQzgz',
            				inputValue : '1'
            			},{
        					layout: {
        						type: 'column'
        					},
        					items: [{
        						columnWidth:.3,
        						xtype: 	'radio',
        						boxLabel  : '指定日期范围',
        						name      : 'tzDQzgz',
        						inputValue : '2'
        					},{
        						columnWidth:.3,
        			        	xtype: 'combobox',
        			        	labelWidth: 80,
        						fieldLabel:Ext.tzGetResourse("TZ_BATCH_XH_COM.TZ_BATCH_XHDFN_STD.tzDQsrq","起始日期"),
        						editable:false,
        						emptyText:'请选择',
        						queryMode: 'remote',
        						style:'margin-left:5px',
        						name: 'tzDQsrq',
        						valueField: 'TValue',
        						displayField: 'TSDesc',
        						store: new KitchenSink.view.common.store.appTransStore("TZ_D_QSRQ")
        			        },{
        						columnWidth:.3,
        			        	xtype: 'combobox',
        			        	labelWidth: 80,
        			        	fieldLabel:Ext.tzGetResourse("TZ_BATCH_XH_COM.TZ_BATCH_XHDFN_STD.tzDJzrq","截止日期"),
        						editable:false,
        						emptyText:'请选择',
        						queryMode: 'remote',
        						style:'margin-left:5px',
        						name: 'tzDJzrq',
        						valueField: 'TValue',
        						displayField: 'TSDesc',
        						store: new KitchenSink.view.common.store.appTransStore("TZ_D_JZRQ")
        			        }]	
        				},{
        					layout: {
        						type: 'column'
        					},
        					style:'margin-top:10px',
        					items: [{
        						columnWidth:.3,
        						xtype: 	'radio',
        						boxLabel  : '指定日期列表',
        						name      : 'tzDQzgz',
        						inputValue : '3'
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
        						columnWidth:.3,
        						xtype: 	'radio',
        						boxLabel  : '指定日期循环间隔',
        						name      : 'tzDQzgz',
        						inputValue : '4'
        					},{	
        						columnWidth:.6,
        						xtype: 'textfield',
        						//style:'margin-left:5px',
        						name: 'tzM1Lbqz',
        						listeners: {
        							render: function(tzM1Lbqz){
        								tzM1Lbqz.inputEl.dom.placeholder="格式：N1,N2,…  取值范围：1-12  例如：1,2,3";
        		                	}
        		             }
        				}]	
        			},{
        				layout: {
        					type: 'column'
        				},
        				style:'margin-top:10px',
        				items: [{
        					columnWidth:.3,
        					xtype: 	'radio',
        					boxLabel  : '指定月份循环间隔',
        					name      : 'tzDQzgz',
        					inputValue : '4'
        				},{	
        					columnWidth:.6,
        					xtype: 'textfield',
        					name: 'tzM1Xhqz',
        					listeners: {
        						render: function(tzM1Xhqz){
        							tzM1Xhqz.inputEl.dom.placeholder="格式：M/N  取值范围：1-12  例如：5/4";
        						}
        					}
        				}]	
        			},{
            				xtype: 	'radio',
            				boxLabel  : '每月最后一天',
            				name      : 'tzDQzgz',
            				inputValue : '5'
            			},{
            				xtype: 	'radio',
            				boxLabel  : '每月最后一个工作日',
            				name      : 'tzDQzgz',
            				inputValue : '6'
            			},{
            				xtype: 	'radio',
            				boxLabel  : '离指定日期最近的一个工作日',
            				name      : 'tzDQzgz',
            				inputValue : '7'
            			}]
        	        },{
        				xtype: 	'radio',
    					boxLabel  : '按“日-周”模式循环',
    					name      : 'tzRzRyBz',
    					inputValue : '2'
        			}]
              },{
                		title: "时",
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
            					boxLabel  : '不限定，任意小时',
            					name      : 'tzHQzgz',
            					inputValue : '1'
            				},{
            					layout: {
            						type: 'column'
            					},
            					items: [{
            						columnWidth:.2,
            						xtype: 	'radio',
            						boxLabel  : '指定小时范围',
            						name      : 'tzHQzgz',
            						inputValue : '2'
            					},{
            						columnWidth:.3,
            			        	xtype: 'combobox',
            			        	labelWidth: 80,
            						fieldLabel:Ext.tzGetResourse("TZ_BATCH_XH_COM.TZ_BATCH_XHDFN_STD.tzHQsxs","起始小时"),
            						editable:false,
            						emptyText:'请选择',
            						queryMode: 'remote',
            						style:'margin-left:5px',
            						name: 'tzHQsxs',
            						valueField: 'TValue',
            						displayField: 'TSDesc',
            						store: new KitchenSink.view.common.store.appTransStore("TZ_H_QSXS")
            			        },{
            						columnWidth:.3,
            			        	xtype: 'combobox',
            			        	labelWidth: 80,
            			        	fieldLabel:Ext.tzGetResourse("TZ_BATCH_XH_COM.TZ_BATCH_XHDFN_STD.tzHJzxs","截止小时"),
            						editable:false,
            						emptyText:'请选择',
            						queryMode: 'remote',
            						style:'margin-left:5px',
            						name: 'tzHJzxs',
            						valueField: 'TValue',
            						displayField: 'TSDesc',
            						store: new KitchenSink.view.common.store.appTransStore("TZ_H_JZXS")
            			        }]	
            				},{
            					layout: {
            						type: 'column'
            					},
            					style:'margin-top:10px',
            					items: [{
            						columnWidth:.2,
            						xtype: 	'radio',
            						boxLabel  : '指定小时列表',
            						name      : 'tzHQzgz',
            						inputValue : '3'
            					},{	
            						columnWidth:.6,
            						xtype: 'textfield',
            						//style:'margin-left:5px',
            						name: 'tzHLbqz',
            						listeners: {
            							render: function(tzHLbqz){
            								tzHLbqz.inputEl.dom.placeholder="格式：N1,N2,…  取值范围：0-23  例如：1,2,3";
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
            						boxLabel  : '指定小时循环间隔',
            						name      : 'tzHQzgz',
            						inputValue : '4'
            					},{	
            						columnWidth:.6,
            						xtype: 'textfield',
            						name: 'tzHXhqz',
            						listeners: {
            							render: function(tzYXhqz){
            		                		tzYXhqz.inputEl.dom.placeholder="格式：H/N  取值范围：0-23/1-23  例如：8/4";
            		                	}
            		                }
            					}]	
            				}]
                },{
            		title: "分",
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
        					boxLabel  : '不限定，任意分钟',
        					name      : 'tzM2Qzgz',
        					inputValue : '1'
        				},{
        					layout: {
        						type: 'column'
        					},
        					items: [{
        						columnWidth:.2,
        						xtype: 	'radio',
        						boxLabel  : '指定分钟范围',
        						name      : 'tzM2Qzgz',
        						inputValue : '2'
        					},{
        						columnWidth:.3,
        			        	xtype: 'combobox',
        			        	labelWidth: 80,
        						fieldLabel:Ext.tzGetResourse("TZ_BATCH_XH_COM.TZ_BATCH_XHDFN_STD.tzM2Qsfz","起始分钟"),
        						editable:false,
        						emptyText:'请选择',
        						queryMode: 'remote',
        						style:'margin-left:5px',
        						name: 'tzM2Qsfz',
        						valueField: 'TValue',
        						displayField: 'TSDesc',
        						store: new KitchenSink.view.common.store.appTransStore("TZ_M2_QSFZ")
        			        },{
        						columnWidth:.3,
        			        	xtype: 'combobox',
        			        	labelWidth: 80,
        			        	fieldLabel:Ext.tzGetResourse("TZ_BATCH_XH_COM.TZ_BATCH_XHDFN_STD.tzM2Jzfz","截止分钟"),
        						editable:false,
        						emptyText:'请选择',
        						queryMode: 'remote',
        						style:'margin-left:5px',
        						name: 'tzM2Jzfz',
        						valueField: 'TValue',
        						displayField: 'TSDesc',
        						store: new KitchenSink.view.common.store.appTransStore("TZ_M2_JZFZ")
        			        }]	
        				},{
        					layout: {
        						type: 'column'
        					},
        					style:'margin-top:10px',
        					items: [{
        						columnWidth:.2,
        						xtype: 	'radio',
        						boxLabel  : '指定分钟列表',
        						name      : 'tzM2Qzgz',
        						inputValue : '3'
        					},{	
        						columnWidth:.6,
        						xtype: 'textfield',
        						//style:'margin-left:5px',
        						name: 'tzM2Lbqz',
        						listeners: {
        							render: function(tzM2Lbqz){
        								tzM2Lbqz.inputEl.dom.placeholder="格式：N1,N2,…  取值范围：0-59  例如：0,1,2,3";
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
        						boxLabel  : '指定分钟循环间隔',
        						name      : 'tzM2Qzgz',
        						inputValue : '4'
        					},{	
        						columnWidth:.6,
        						xtype: 'textfield',
        						name: 'tzM2Xhqz',
        						listeners: {
        							render: function(tzM2Xhqz){
        								tzM2Xhqz.inputEl.dom.placeholder="格式：H/N  取值范围：0-59/1-59  例如：8/4";
        		                	}
        		                }
        					}]	
        				}]
            	  	},{
                		title: "秒",
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
            					boxLabel  : '不限定，任意秒',
            					name      : 'tzSQzgz',
            					inputValue : '1'
            				},{
            					layout: {
            						type: 'column'
            					},
            					items: [{
            						columnWidth:.2,
            						xtype: 	'radio',
            						boxLabel  : '指定秒范围',
            						name      : 'tzSQzgz',
            						inputValue : '2'
            					},{
            						columnWidth:.3,
            			        	xtype: 'combobox',
            			        	labelWidth: 80,
            						fieldLabel:Ext.tzGetResourse("TZ_BATCH_XH_COM.TZ_BATCH_XHDFN_STD.tzSQsm","起始秒"),
            						editable:false,
            						emptyText:'请选择',
            						queryMode: 'remote',
            						style:'margin-left:5px',
            						name: 'tzSQsm',
            						valueField: 'TValue',
            						displayField: 'TSDesc',
            						store: new KitchenSink.view.common.store.appTransStore("TZ_S_QSM")
            			        },{
            						columnWidth:.3,
            			        	xtype: 'combobox',
            			        	labelWidth: 80,
            			        	fieldLabel:Ext.tzGetResourse("TZ_BATCH_XH_COM.TZ_BATCH_XHDFN_STD.tzSJzm","截止秒"),
            						editable:false,
            						emptyText:'请选择',
            						queryMode: 'remote',
            						style:'margin-left:5px',
            						name: 'tzSJzm',
            						valueField: 'TValue',
            						displayField: 'TSDesc',
            						store: new KitchenSink.view.common.store.appTransStore("TZ_S_JZM")
            			        }]	
            				},{
            					layout: {
            						type: 'column'
            					},
            					style:'margin-top:10px',
            					items: [{
            						columnWidth:.2,
            						xtype: 	'radio',
            						boxLabel  : '指定秒列表',
            						name      : 'tzSQzgz',
            						inputValue : '3'
            					},{	
            						columnWidth:.6,
            						xtype: 'textfield',
            						//style:'margin-left:5px',
            						name: 'TzSLbqz',
            						listeners: {
            							render: function(TzSLbqz){
            								TzSLbqz.inputEl.dom.placeholder="格式：N1,N2,…  取值范围：0-59  例如：0,1,2,3";
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
            						boxLabel  : '指定秒循环间隔',
            						name      : 'tzSQzgz',
            						inputValue : '4'
            					},{	
            						columnWidth:.6,
            						xtype: 'textfield',
            						name: 'tzSXhqz',
            						listeners: {
            							render: function(tzSXhqz){
            								tzSXhqz.inputEl.dom.placeholder="格式：H/N  取值范围：0-59/1-59  例如：8/4";
            		                	}
            		                }
            					}]	
            				}]
                	 },{
                		title: "高级",
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
            					xtype: 	'checkbox',
            					boxLabel  : '自定义循环期间',
            					name      : 'tzZdyxhBz',
            					inputValue : 'Y'	
            			},{
            	            xtype: 'fieldcontainer',
            	            defaultType: 'textfield',
            	            bodyPadding: 10,
                		    layout: {
                		      type: 'vbox',
                		      align: 'stretch'
                		    },
                		    bodyStyle:'overflow-y:auto;overflow-x:hidden',
                  			fieldDefaults: {
                	      		msgTarget: 'side',
                	      		labelWidth: 30,
                	      		labelStyle: 'font-weight:bold;'
                  			}, 
                  			style: 'margin-left:30px;',
            	            items: [{
            	                name: 'tzXhzdY',
            	                fieldLabel:Ext.tzGetResourse("TZ_BATCH_XH_COM.TZ_BATCH_XHDFN_STD.tzXhzdY","年")
            	            },{
            	                name: 'tzXhzdW',
            	                fieldLabel:Ext.tzGetResourse("TZ_BATCH_XH_COM.TZ_BATCH_XHDFN_STD.tzXhzdW","周")
            	            },{
            	                name: 'TzXhzdM1',
            	                fieldLabel:Ext.tzGetResourse("TZ_BATCH_XH_COM.TZ_BATCH_XHDFN_STD.TzXhzdM1","月")
            	            },{
            	                name: 'TzXhzdD',
            	                fieldLabel:Ext.tzGetResourse("TZ_BATCH_XH_COM.TZ_BATCH_XHDFN_STD.TzXhzdD","日")
            	            },{
            	                name: 'tzXhzdH',
            	                fieldLabel:Ext.tzGetResourse("TZ_BATCH_XH_COM.TZ_BATCH_XHDFN_STD.tzXhzdH","时")
            	            },{
           	                	 name: 'tzXhzdM2',
        	                     fieldLabel:Ext.tzGetResourse("TZ_BATCH_XH_COM.TZ_BATCH_XHDFN_STD.tzXhzdM2","分")
        	                },{
           	                	 name: 'tzXhzdS',
        	                     fieldLabel:Ext.tzGetResourse("TZ_BATCH_XH_COM.TZ_BATCH_XHDFN_STD.tzXhzdS","秒")
        	                }]
            	        }]
                }]
        	}]
	}]
});
