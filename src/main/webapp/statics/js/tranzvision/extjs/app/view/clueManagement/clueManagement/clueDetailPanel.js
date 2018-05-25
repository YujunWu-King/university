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
            companyNameData = me.companyNameData;

        /*
        var clueDetailFormPanel = {
            //姓名
            customerNameStore : new KitchenSink.view.common.store.comboxStore({
                pageSize: 0,
                recname: 'TZ_XS_CUSNM_V',
                condition: {
					TZ_JG_ID: {
                        value:Ext.tzOrgID,
                        operator: '01',
                        type: '01'
                    },
                    TZ_REALNAME: {
                        value: ' ',
                        operator: '02',
                        type: '01'
                    }
                },
                result: 'TZ_KH_OPRID,TZ_REALNAME,TZ_DESCR_254'
            }),
            //公司
            companyNameStore : new KitchenSink.view.common.store.comboxStore({
                pageSize: 0,
                recname: 'TZ_XS_COMNM_V',
                condition: {
                    TZ_JG_ID: {
                        value:Ext.tzOrgID,
                        operator: '01',
                        type: '01'
                    }
                },
                result: 'TZ_COMP_CNAME'
            })
        };
        */


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
            //clueDetailFormPanel:clueDetailFormPanel,
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
                        flex:1
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
                    readOnly:true,
                    fieldStyle:'background:#F4F4F4'
                }, {
                    xtype:'textfield',
                    name:'cusOprid',
                    hidden:true
                },{
                    xtype: 'combo',
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
                    xtype:'textfield',
                    name:'localId',
                    hidden:true
                },{
                    xtype: 'textfield',
                    fieldLabel: '常住地',
                    name: 'localAddress',
                    editable: false,
                    triggers: {
                        clear: {
                            cls: 'x-form-clear-trigger',
                            handler: 'clearLocal'
                        },
                        search: {
                            cls: 'x-form-search-trigger',
                            handler: "searchLocal"
                        }
                    }
                }, {
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
                    xtype: 'textareafield',
                    grow: true,
                    fieldLabel: '备注',
                    name: 'memo'
                }
                ]
            },{
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
                    text:'线索编号',
                    dataIndex:'clueId',
                    hidden:true
                },{
                    text:'报名表编号',
                    dataIndex:'bmbId',
                    width:110
                },{
                    text:'提交状态',
                    dataIndex:'tjStatus',
                    width:100,
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
                    width:120,
                    sortable:false
                },{
                    text:'邮箱',
                    dataIndex:'bmrEmail',
                    width:180,
                    sortable:false
                },{
                    text:'班级名称',
                    dataIndex:'bmrClassName',
                    sortable:false,
                    flex:1
                },{
                    text:'初审状态',
                    dataIndex:'bmrCsStatus',
                    sortable:false,
                    width:105
                },{
                    text:'收费状态',
                    dataIndex:'bmrsfStatus',
                    sortable:false,
                    width:105
                },{
                    text:'录取状态',
                    dataIndex:'bmrLqStatus',
                    sortable:false,
                    width:105
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
        this.fromType = config.fromType;
        this.callParent();
    }
});
