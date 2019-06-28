Ext.define('KitchenSink.view.clueManagement.clueManagement.clueDetailPanel',{
    extend:'Ext.panel.Panel',
    xtype:'clueDetailPanel',
    reference:'clueDetailPanel',
    requires:[
        'Ext.data.*',
        'Ext.grid.*',
        'Ext.util.*',
        'Ext.toolbar.Paging',
        'KitchenSink.view.common.store.comboxStore',
        'KitchenSink.view.clueManagement.clueManagement.clueBmbStore',
        'KitchenSink.view.clueManagement.clueManagement.clueBmbActStore',
        'KitchenSink.view.clueManagement.clueManagement.clueDetailController'
    ],
    controller:'clueDetailController',
    actType:"",
    fromType:"",
    title:'线索详情',
    bodyStyle:'overflow-y:auto;overflow-x:hidden',
    initComponent:function() {
        var me=this;
        var backReasonData = me.backReasonData,
            closeReasonData = me.closeReasonData,
            colorSortData = me.colorSortData,
            customerNameData = me.customerNameData,
            companyNameData = me.companyNameData,
            clueTagStore = me.clueTagStore,
            otherZrrStore = me.otherZrrStore;


        var dynamicStore = {
            //退回原因
            backReasonStore: Ext.create("Ext.data.Store", {
                fields: [
                    'TZ_THYY_ID,TZ_LABEL_NAME'
                ],
                data: backReasonData
            }),
            //关闭原因
            closeReasonStore:Ext.create("Ext.data.Store", {
                fields: [
                    'TZ_GBYY_ID,TZ_LABEL_NAME'
                ],
                data: closeReasonData
            }),
            //线索类别
            colorSortStore:Ext.create("Ext.data.Store", {
                fields: [
                    "TZ_COLOUR_SORT_ID", "TZ_COLOUR_NAME", "TZ_COLOUR_CODE"
                ],
                data: colorSortData
            }),
            //姓名
            customerNameStore:Ext.create("Ext.data.Store",{
                fields:[
                    "TZ_KH_OPRID","TZ_REALNAME","TZ_DESCR_254"
                ],
                data:customerNameData
            }),
            //公司
            companyNameStore:Ext.create("Ext.data.Store",{
                fields:[
                    "TZ_COMP_CNAME"
                ],
                data:companyNameData
            })
        };

        Ext.apply(this,{
            items: [{
                xtype: 'form',
                reference: 'clueDetailForm',
                layout: {
                    type: 'vbox',
                    align: 'stretch'
                },
                border: false,
                bodyPadding: 10,
                bodyStyle: 'overflow-y:auto;overflow-x:hidden',
                fieldDefaults: {
                    msgTarget: 'side',
                    labelWidth: 110,
                    labelStyle: 'font-weight:bold'
                },
                items: [{
                    xtype:'hiddenfield',
                    fieldLabel:'线索ID',
                    name:'clueId'
                },{
                    xtype:'hiddenfield',
                    name:'fromType',
                    value:this.fromType
                },{
                    xtype:'fieldcontainer',
                    name:'quickDealWith',
                    layout: 'hbox',
                    items:[{
                        xtype: 'combobox',
                        fieldLabel: "状态",
                        forceSelection: true,
                        valueField: 'TValue',
                        displayField: 'TSDesc',
                        name:'clueState',
                        store: new KitchenSink.view.common.store.appTransStore("TZ_LEAD_STATUS"),
                        editable : false,
                        readOnly:true,
                        queryMode: 'local',
                        fieldStyle:'background:#F4F4F4',
                        value: 'C',
                        flex: 1
                    },{
                        width:120,
                        xtype:'button',
                        text:'快速处理',
                        margin:'0 0 0 5',
                        menu:{
                            items:[
                                {
                                    text:'过往状态',
                                    handler:'viewClueOldState'
                                },{
                                    text:'退回',
                                    handler:'dealWithBack'
                                },{
                                    text:'关闭',
                                    handler:'dealWithClose'
                                },{
                                    text:'转交',
                                    handler:'dealWithGive'
                                },{
                                    text:'延迟联系',
                                    handler:'dealWithDelayContact'
                                }
                            ]
                        }
                    }]
                },{
                    xtype:'combobox',
                    fieldLabel:'退回原因',
                    afterLabelTextTpl: ['<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'],
                    name:'backReasonId',
                    store: dynamicStore.backReasonStore,
                    autoShow: true,
                    valueField: 'TZ_THYY_ID',
                    displayField: 'TZ_LABEL_NAME',
                    queryMode: 'local',
                    editable: false
                },{
                    xtype:'combobox',
                    fieldLabel:'关闭原因',
                    afterLabelTextTpl: ['<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'],
                    name:'closeReasonId',
                    store: dynamicStore.closeReasonStore,
                    autoShow: true,
                    valueField: 'TZ_GBYY_ID',
                    displayField: 'TZ_LABEL_NAME',
                    queryMode: 'local',
                    editable: false
                },{
                    xtype:'datefield',
                    fieldLabel:'建议跟进日期',
                    afterLabelTextTpl: ['<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'],
                    name: 'contactDate',
                    format:'Y-m-d'
                },{
                    xtype: 'textfield',
                    fieldLabel: '责任人ID',
                    name: 'chargeOprid',
                    hidden:true
                },{
                    xtype: 'textfield',
                    fieldLabel: '责任人',
                    name: 'chargeName',
                    editable:false,
                    readOnly: me.zrrEditFalg == 'Y' ? false : true,
                    fieldStyle: me.zrrEditFalg == 'Y' ? '' : 'background:#F4F4F4',
                    triggers: {
                        clear: {
                            cls: 'x-form-clear-trigger',
                            hidden: true,
                            handler: function(field){
                            	field.setValue("");
                            	field.findParentByType('form').getForm().findField('chargeOprid').setValue("");
                                field.getTrigger('clear').hide();
                            }
                        },
                        search: {
                            cls: 'x-form-search-trigger',
                            handler: "searchCharge"
                        }
                    },
                    listeners: {
						change: function(field,newValue,oldValue){
							if(me.zrrEditFalg == 'Y'){
								field.getTrigger('clear')[(newValue.length > 0) ? 'show' : 'hide']();
								
								//如果已有责任人，责任人不可编辑
								if(newValue && newValue.length > 0){
									field.getTrigger('clear').hide();
									var chargeNameField = field.findParentByType('form').getForm().findField('chargeName')
									chargeNameField.setReadOnly(true);
									chargeNameField.setFieldStyle('background:#F4F4F4');
								}
							}
						}
					}
                },{
                	xtype: 'tagfield',
                    fieldLabel: "其他责任人",
                    name: 'otherCharge',
                    store: otherZrrStore,
                    valueField: 'TZ_ZRR_OPRID',
                    displayField: 'TZ_REALNAME',
                    filterPickList: false,
                    createNewOnEnter: false,
                    createNewOnBlur: false,
                    queryMode: 'local',
                    triggers: {
                        clear: {
                            cls: 'x-form-clear-trigger',
                            hidden: true,
                            handler: function(field){
                            	field.setValue("");
                                field.getTrigger('clear').hide();
                            }
                        },
                        search: {
                            cls: 'x-form-search-trigger',
                            handler: "searchOtherCharge"
                        }
                    },
                    listeners: {
						change: function(field,newValue,oldValue){
							field.getTrigger('clear')[(newValue.length > 0) ? 'show' : 'hide']();
						}
					}
                },{
                    xtype:'textfield',
                    name:'cusOprid',
                    hidden:true
                },{
                    xtype: 'combo',
                    id : 'myCombo',
                    fieldLabel: '客户姓名',
                    maxLength: 30,
                    name: 'cusName',
                    afterLabelTextTpl: ['<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'],
                    store: dynamicStore.customerNameStore,
                    typeAhead: true,
                    autoShow: true,
                    valueField: 'TZ_REALNAME',
                    displayField: 'TZ_DESCR_254',
                    queryMode: 'local',
                    allowBlank: false,
                    displayTpl: Ext.create('Ext.XTemplate',
                        '<tpl for=".">',
                        '{TZ_REALNAME}',
                        '</tpl>'
                    ),
                    listeners:{
                    	load:function(){
                            Ext.getCmp('myCombo').setValue("小名");
                         },
                        select:function(combo,record){
                            var form = this.findParentByType('form').getForm();

                            //显示值为姓名
                            var selectValue=this.getValue();
                            var selectDisplay = this.getRawValue();
                            this.setRawValue(selectValue);

                            var cusOprid = record.get("TZ_KH_OPRID");

                            //带入相关信息
                            var tzParamsHis = '{"ComID":"TZ_XSXS_INFO_COM","PageID":"TZ_XSXS_DETAIL_STD","OperateType":"tzGetCustomerInfo","comParams":{"cusOprid":"'+cusOprid+'","cusName":"'+selectValue+'","nameCompany":"'+selectDisplay+'"}}';
                            Ext.tzLoad(tzParamsHis,function(responseData){
                                form.findField('cusMobile').setValue(responseData.mobile);
                                form.findField('companyName').setRawValue(responseData.company);
                                form.findField('position').setValue(responseData.position);
                                form.findField('phone').setValue(responseData.phone);
                            });
                        }
                    }
                },{
                    xtype: 'textfield',
                    fieldLabel: '手机',
                    maxLength: 25,
                    //afterLabelTextTpl: ['<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'],
                    //allowBlank:false,
                    name: 'cusMobile'
                },{
                	xtype: 'textfield',
                    fieldLabel: '邮箱',
                    maxLength: 100,
                    name: 'cusEmail'
                },{
                    xtype: 'combo',
                    fieldLabel: '公司',
                    maxLength: 50,
                    name: 'companyName',
                    store: dynamicStore.companyNameStore,
                    typeAhead: true,
                    autoShow: true,
                    valueField: 'TZ_COMP_CNAME',
                    displayField: 'TZ_COMP_CNAME',
                    queryMode: 'local',
                    displayTpl: Ext.create('Ext.XTemplate',
                        '<tpl for=".">',
                        '{TZ_COMP_CNAME}',
                        '</tpl>'
                    )
                },{
                    xtype: 'textfield',
                    fieldLabel: '职务',
                    maxLength: 50,
                    name: 'position'
                },{
                    xtype: 'textfield',
                    fieldLabel: '电话',
                    maxLength: 24,
                    name: 'phone'
                },{
                    xtype:'combobox',
                    fieldLabel:'类别',
                    name:'colorType',
                    emptyText:'请选择...',
                    queryModel:'local',
                    valueField:'TZ_COLOUR_SORT_ID',
                    displayField:'TZ_COLOUR_NAME',
                    triggerAction:'all',
                    editable:false,
                    fieldStyle:"padding-left:46px;",
                    store:dynamicStore.colorSortStore,
                    tpl:Ext.create('Ext.XTemplate',
                        '<tpl for=".">',
                        '<div class="x-boundlist-item"><div class="x-colorpicker-field-swatch-inner" style="margin-top:6px;width:30px;height:50%;background-color: #{TZ_COLOUR_CODE}"></div><div style="margin-left:40px;display: block;  overflow:  hidden; white-space: nowrap; -o-text-overflow: ellipsis; text-overflow:  ellipsis;"> {TZ_COLOUR_NAME}</div></div>',
                        '</tpl>',{}
                    ),
                    displayTpl:Ext.create('Ext.XTemplate',
                        '<tpl for=".">',
                        '{TZ_COLOUR_NAME}',
                        '</tpl>'
                    ),
                    beforeBodyEl:[
                            '<div class="' + Ext.baseCSSPrefix + 'colorpicker-field-swatch" style="margin-left:120px;width:30px;height:50%">' +
                            '<div id="{id}-colorEl" data-ref="colorEl" class="' + Ext.baseCSSPrefix +
                            'colorpicker-field-swatch-inner"></div>' +
                            '</div>'
                    ],
                    childEls: [
                        'colorEl'
                    ],
                    listeners:{
                        change:function(combo, newValue, oldValue, eOpts){
                            var colorEl = this.colorEl;
                            if(newValue){
                                var rec = dynamicStore.colorSortStore.find('TZ_COLOUR_SORT_ID',newValue,0,false,false,true);
                                if(rec>-1){
                                    var color = dynamicStore.colorSortStore.getAt(rec).get("TZ_COLOUR_CODE");

                                    if (colorEl) {
                                        var tpl = Ext.create('Ext.XTemplate',
                                            'background: #{alpha};'
                                        );
                                        var data = {
                                            alpha: color
                                        };
                                        var bgStyle = tpl.apply(data);
                                        colorEl.applyStyles(bgStyle);
                                    }
                                }
                            }else{
                                colorEl.applyStyles( 'background: none');
                            }
                            combo.getTrigger('clear')[(newValue==null || newValue.length == 0) ? 'hide' : 'show']();
                        }
                    },
                    triggers: {
                        clear: {
                            cls: 'x-form-clear-trigger',
                            hidden: true,
							handler: function(field){
								field.setValue();
								field.getTrigger('clear').hide();
							}
                        }
                    }
                },{
                	xtype: 'tagfield',
                    fieldLabel: "标签",
                    name: 'clueTags',
                    store: clueTagStore,
                    valueField: 'TZ_LABEL_ID',
                    displayField: 'TZ_LABEL_NAME',
                    filterPickList:true,
                    createNewOnEnter: true,
                    createNewOnBlur: true,
                    queryMode: 'local',
                    listeners:{
                        select: function(combo,record,index,eOpts){//匹配下拉值之后置空输入文字
                            var me = this;
                            me.inputEl.dom.value = "";
                        }
                    }
                },{
                    xtype:'fieldcontainer',
                    name:'relBmb',
                    layout: 'hbox',
                    items:[{
                        xtype: 'combobox',
                        fieldLabel: "报考状态",
                        forceSelection: true,
                        valueField: 'TValue',
                        displayField: 'TSDesc',
                        name:'bkStatus',
                        store: new KitchenSink.view.common.store.appTransStore("TZ_LEAD_BMB_STATUS"),
                        editable : false,
                        readOnly:true,
                        queryMode: 'local',
                        fieldStyle:'background:#F4F4F4',
                        value:'A',
                        flex:1
                    },{
                        width:120,
                        xtype:'button',
                        text:'关联报名表',
                        margin:'0 0 0 5',
                        name:'glBmbBut',
                        handler:'clueRelBmb'
                    }]
                },/*{
                    xtype: 'textfield',
                    fieldLabel: '推荐人姓名',
                    name: 'refereeName'
                }, */{
                    xtype: 'numberfield',
                    fieldLabel: '年龄',
                    name: 'age'
                },{
            		xtype: 'combobox',
                    fieldLabel: '性别',
                    editable:false,
                    emptyText:'请选择',
                    queryMode: 'remote',
            	    	name: 'sex',
            	    	valueField: 'TValue',
                		displayField: 'TSDesc',
                		store: new KitchenSink.view.common.store.appTransStore("TZ_GENDER")
                },{
                    xtype: 'textfield',
                    fieldLabel: '推荐人',
                    name: 'tjr'
                },{
                    xtype: 'textfield',
                    fieldLabel: '辅导班',
                    name: 'fdb'
                },{
                	xtype: 'combobox',
                    fieldLabel: '最高学历',
                    editable:false,
                    emptyText:'请选择',
                    queryMode: 'remote',
            	    	name: 'zgxl',
            	    	valueField: 'TValue',
                		displayField: 'TSDesc',
                		store: new KitchenSink.view.common.store.appTransStore("TZ_XL")
                },{
                	xtype: 'combobox',
                    fieldLabel: '工作年限',
                    editable:false,
                    emptyText:'请选择',
                    queryMode: 'remote',
            	    	name: 'gznx',
            	    	valueField: 'TValue',
                		displayField: 'TSDesc',
                		store: new KitchenSink.view.common.store.appTransStore("TZ_NX")
                },{
                	xtype: 'combobox',
                    fieldLabel: '管理年限',
                    editable:false,
                    emptyText:'请选择',
                    queryMode: 'remote',
            	    	name: 'glnx',
            	    	valueField: 'TValue',
                		displayField: 'TSDesc',
                		store: new KitchenSink.view.common.store.appTransStore("TZ_NX")
                },{
                    xtype: 'textareafield',
                    grow: true,
                    fieldLabel: '备注',
                    name: 'memo'
                }]
            },{
            	xtype:'tabpanel',
                frame:true,
                activeTab:0,
                plain:false,
                style:'margin:0 10px',
                resizeTabs:true,
                defaults:{
                    autoScroll:false
                },
                listeners:{
                    tabchange:function(tabPanel,newCard,oldCard) {

						this.doLayout();
                    }
                },
                items:[{
	            	title:'报名信息',
	                xtype:'grid',
	                columnLines:true,
	                minHeight:170,
	                name:'bmbInfoGrid',
	                reference:'bmbInfoGrid',
	                store:{
	                    type:'clueBmbStore'
	                },
	                columns:[{
	                    text:'报名表编号',
	                    dataIndex:'bmbId',
	                    width:100
	                },{
	                    text:'提交状态',
	                    dataIndex:'tjStatus',
	                    width:90,
	                    sortable:false
	                },{
	                    text:'姓名',
	                    dataIndex:'bmrName',
	                    width:90,
	                    sortable:false
	                },{
	                    text:'操作',
	                    sortable:false,
	                    menuDisabled:true,
	                    width:50,
	                    xtype:'actioncolumn',
	                    align:'center',
	                    items:[
	                        {iconCls:'preview',tooltip:'查看报名表',handler:'viewApplication'},
	                        {iconCls:'audit',tooltip:'审核信息结果',handler:'viewAuditApplication'},
	                        {iconCls:'delete',tooltip:'删除',handler:'deleteBmb'}
	                    ]
	                },{
	                    text:'手机',
	                    dataIndex:'bmrMobile',
	                    width:110,
	                    sortable:false
	                },{
	                    text:'邮箱',
	                    dataIndex:'bmrEmail',
	                    width:140,
	                    sortable:false
	                },{
	                    text:'班级名称',
	                    dataIndex:'bmrClassName',
	                    sortable:false,
	                    minWidth:120,
	                    width: 130,
	                    flex:1
	                },{
	                    text:'初审状态',
	                    dataIndex:'bmrCsStatus',
	                    sortable:false,
	                    width:100
	                },{
	                    text:'录取状态',
	                    dataIndex:'bmrLqStatus',
	                    sortable:false,
	                    width:100
	                }]
                },{
                	title:'联系报告',
                    name:"connectReport",
                    listeners:{
                        afterrender:function(panel){
							var clueID = panel.findParentByType("tabpanel").findParentByType("panel").clueID;
                            var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_XSXS_INFO_COM"]["TZ_CONNECT_RPT_STD"];
                            if( pageResSet == "" || pageResSet == undefined){
                                Ext.MessageBox.alert('提示', '您没有修改数据的权限');
                                return;
                            }
                            //该功能对应的JS类
                            var className = pageResSet["jsClassName"];
                            if(className == "" || className == undefined){
                                Ext.MessageBox.alert('提示', '未找到该功能页面对应的JS类，页面ID为：TZ_CONNECT_RPT_STD，请检查配置。');
                                return;
                            }
                            var contentPanel, cmp, ViewClass, clsProto;

                            contentPanel = Ext.getCmp('tranzvision-framework-content-panel');
                            contentPanel.body.addCls('kitchensink-example');

                            if(!Ext.ClassManager.isCreated(className)){
                                Ext.syncRequire(className);
                            }
                            ViewClass = Ext.ClassManager.get(className);
							
                            panel.add(new ViewClass({
                                TZ_LEAD_ID:clueID
                            }));
                        }
                    },
                },{
	            	title:'活动列表',
	                xtype:'grid',
	                columnLines:true,
	                minHeight:170,
	                name:'bmbActGrid',
	                reference:'bmbActGrid',
	                listeners:{
	                	beforerender:function(panel){
	                		//线索ID
							var clueId = panel.findParentByType("tabpanel").previousSibling().getForm().findField("clueId").getValue();
							var tzParams = '{"ComID":"TZ_XSXS_INFO_COM","PageID":"TZ_XSXS_DETAIL_STD","OperateType":"queryAct","comParams":{"clueId":"'+clueId+'"}}';
							panel.store.proxy.extraParams.tzParams = tzParams;
							panel.store.reload();
	                	}/*,
                        afterrender:function(panel){
                        	//线索ID
							var clueId = panel.findParentByType("tabpanel").previousSibling().getForm().findField("clueId").getValue();
							var tzParams = '{"ComID":"TZ_XSXS_INFO_COM","PageID":"TZ_XSXS_DETAIL_STD","OperateType":"queryAct","comParams":{"clueId":"'+clueId+'"}}';
							panel.store.proxy.extraParams.tzParams = tzParams;
							panel.store.reload();
                        }*/
	                },
	                store:{
	                    type:'clueBmbActStore'
	                },
	                columns:[{
	                    text:'活动ID',
	                    dataIndex:'TZ_ART_ID',
	                    width:100,
	                    hidden:true,
	                    flex:1
	                },{
	                    text:'活动名称',
	                    dataIndex:'TZ_NACT_NAME',
	                    width:100,
	                    flex:1
	                },{
	                    text:'报名时间',
	                    dataIndex:'TZ_REG_TIME',
	                    width:90,
	                    sortable:false,
	                    flex:1,
	                    renderer:function(v){
	                    	return Ext.Date.format(new Date(v),"Y-m-d H:i:s");
	                    }
	                    
	                },{
	                    text:'报名渠道',
	                    dataIndex:'TZ_ZXBM_LY',
	                    width:90,
	                    sortable:false,
	                    flex:1,
	                    renderer:function(v){
	                    	if(v == 'A'){
	                    		return '手机'
	                    	}else if(v == 'B'){
	                    		return '网站'
	                    	}else if(v == 'C'){
	                    		return '短信'
	                    	}else if(v == 'D'){
	                    		return '邮件'
	                    	}else{
	                    		return v
	                    	}
	                    }
	                },{
	                    text:'签到状态',
	                    dataIndex:'TZ_BMCY_ZT',
	                    width:110,
	                    sortable:false,
	                    flex:1,
	                    renderer:function(v){
	                    	if(v == 'A'){
	                    		return '已参与'
	                    	}else if(v == 'B'){
	                    		return '其他原因为参与'
	                    	}else if(v == 'C'){
	                    		return '无故缺席'
	                    	}else if(v == 'D'){
	                    		return '请假'
	                    	}else{
	                    		return v
	                    	}
	                    }
	                },{
	                    text:'报名状态',
	                    dataIndex:'TZ_NREG_STAT',
	                    width:110,
	                    sortable:false,
	                    flex:1,
	                    renderer:function(v){
	                    	if(v == '1'){
	                    		return '已报名'
	                    	}else if(v == '3'){
	                    		return '已撤销'
	                    	}else if(v == '4'){
	                    		return '等待'
	                    	}else{
	                    		return v
	                    	}
	                    }
                    }
	                ]/*,
	                bbar:{
	                    xtype:'pagingtoolbar',
	                    pageSize:10,
	                    store:{
		                    type:'clueBmbActStore'
		                },
	                    plugins: new Ext.ux.ProgressBarPager()
	                }*/
                }]
            }],
            buttons: [{
                text: '保存',
                iconCls: "save",
                name: 'saveBtn',
                handler: 'saveClue'
            },{
                text: '确定',
                iconCls: "ensure",
                name: 'ensureBtn',
                handler: 'saveClue'
            },  {
                text: '关闭',
                iconCls: "close",
                handler: 'closeClue'
            }]
        });
        this.callParent();
    },
    constructor:function(config){
        this.backReasonData = config.backReasonData;
        this.closeReasonData = config.closeReasonData;
        this.colorSortData = config.colorSortData;
        this.customerNameData = config.customerNameData;
        this.companyNameData = config.companyNameData;
        this.clueTagStore = config.clueTagStore;
        this.otherZrrStore = config.otherZrrStore;
        this.fromType = config.fromType;
        this.clueID = config.clueID;
        this.zrrEditFalg = config.zrrEditFalg;
        
        this.callParent();
    }
});
