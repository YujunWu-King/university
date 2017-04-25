Ext.define('KitchenSink.view.enrollProject.userMg.userMgInfoPanel', {
    extend : 'Ext.panel.Panel',
    xtype : 'userMgInfoPanel',
    controller : 'userMgController',
    requires : [
        'Ext.data.*',
        'Ext.grid.*',
        'Ext.util.*',
        'Ext.toolbar.Paging',
        'Ext.ux.ProgressBarPager',
        'KitchenSink.view.enrollProject.userMg.stuAppStore'
    ],
    listeners : {
        resize : function(win) {
            win.doLayout();
        }
    },
    actType : '',
    title : '考生信息',
    bodyStyle : 'overflow-y:auto;overflow-x:hidden',

    items : [ {
        xtype : 'form',
        reference : 'userMgForm',
        name:'userMgForm',
        layout : {
            type : 'vbox',
            align : 'stretch'
        },
        border : false,
        bodyPadding : 10,
        bodyStyle : 'overflow-y:auto;overflow-x:hidden',

        fieldDefaults : {
            msgTarget : 'side',
            labelWidth : 110,
            labelStyle : 'font-weight:bold'
        },
        items : [ {
            layout : {
                type : 'column'
            },
            items : [ {
                columnWidth : .2,
                xtype : "image",
                src : "",
                height : 300,
                width : 217,
                name : "titileImage"

            },
                {
                    columnWidth : .8,
                    bodyStyle : 'padding-left:30px',
                    layout : {
                        type : 'vbox',
                        align : 'stretch'
                    },
                    items : [ {
                        xtype : 'textfield',
                        readOnly : true,
                        fieldLabel : '用户编号',
                        name : 'OPRID',
                        fieldStyle : 'background:#F4F4F4'
                    }, {
                        xtype : 'textfield',
                        fieldLabel : '面试申请号',
                        readOnly : true,
                        name : 'msSqh',
                        fieldStyle : 'background:#F4F4F4'
                    }, {
                        xtype : 'textfield',
                        fieldLabel : '姓名',
                        readOnly : true,
                        name : 'userName',
                        fieldStyle : 'background:#F4F4F4'
                    }, {
                        xtype : 'textfield',
                        fieldLabel : '性别',
                        readOnly : true,
                        name : 'userSex',
                        fieldStyle : 'background:#F4F4F4'
                    }, {
                        xtype : 'textfield',
                        fieldLabel : '邮箱',
                        readOnly : true,
                        name : 'userEmail',
                        fieldStyle : 'background:#F4F4F4'
                    }, {
                        xtype : 'textfield',
                        fieldLabel : '手机',
                        readOnly : true,
                        name : 'userPhone',
                        fieldStyle : 'background:#F4F4F4'
                    },
                        {
                            xtype : 'combo',
                            fieldLabel : '账号激活状态',
                            name : 'jihuoZt',
                            emptyText : '请选择',
                            queryMode : 'remote',
                            editable : false,
                            valueField : 'TZ_ZHZ_ID',
                            displayField : 'TZ_ZHZ_DMS',
                            store : new KitchenSink.view.common.store.comboxStore({
                                recname : 'TZ_PT_ZHZXX_TBL',
                                condition : {
                                    TZ_ZHZJH_ID : {
                                        value : "TZ_JIHUO_ZT",
                                        operator : "01",
                                        type : "01"
                                    },
                                    TZ_EFF_STATUS : {
                                        value : "A",
                                        operator : "01",
                                        type : "01"
                                    }
                                },
                                result : 'TZ_ZHZ_ID,TZ_ZHZ_DMS'
                            })
                        },
                        {
                            xtype : 'combo',
                            fieldLabel : '账号锁定状态',
                            name : 'acctlock',
                            emptyText : '请选择',
                            queryMode : 'remote',
                            editable : false,
                            valueField : 'TZ_ZHZ_ID',
                            displayField : 'TZ_ZHZ_DMS',
                            store : new KitchenSink.view.common.store.comboxStore({
                                recname : 'TZ_PT_ZHZXX_TBL',
                                condition : {
                                    TZ_ZHZJH_ID : {
                                        value : "ACCTLOCK",
                                        operator : "01",
                                        type : "01"
                                    },
                                    TZ_EFF_STATUS : {
                                        value : "A",
                                        operator : "01",
                                        type : "01"
                                    }
                                },
                                result : 'TZ_ZHZ_ID,TZ_ZHZ_DMS'
                            })
                        }, {
                            xtype : 'textfield',
                            fieldLabel : '创建日期时间',
                            readOnly : true,
                            name : 'zcTime',
                            fieldStyle : 'background:#F4F4F4'
                        }, {
                            xtype : 'radiogroup',
                            fieldLabel : '黑名单用户',
                            // readOnly:true,
                            name : 'blackNames',
                            fieldStyle : 'background:#F4F4F4',
                            columns : 6,
                            items : [ {
                                boxLabel : "是",
                                name : "blackName",
                                inputValue : "Y",
                                // readOnly : true
                            }, {
                                boxLabel : "否",
                                name : "blackName",
                                inputValue : "N",
                                // readOnly : true
                            } ]
                        }, {
                            xtype : 'radiogroup',
                            fieldLabel : '允许继续申请',
                            // readOnly:true,
                            name : 'allowApplys',
                            fieldStyle : 'background:#F4F4F4',
                            columns : 6,
                            items : [ {
                                boxLabel : "是",
                                name : "allowApply",
                                inputValue : "Y",
                                // readOnly : true
                            }, {
                                boxLabel : "否",
                                name : "allowApply",
                                inputValue : "N",
                                // readOnly : true
                            } ]
                        }, {
                            xtype : 'textarea',
                            fieldLabel : '备注',
                            name : 'beizhu',
                            // fieldStyle:'background:#F4F4F4'
                        }, {
                            xtype : 'hiddenfield',
                            name : 'titleImageUrl'
                        } ]
                }, {
                    xtype : 'tabpanel',
                    frame : true,
                    columnWidth : 1,
                    bodyStyle : 'padding-top:10px',
                    layout : {
                        type : 'vbox',
                        align : 'stretch'
                    },
                    listeners: {
                        tabchange: function(tabPanel, newCard, oldCard) {
                            if (newCard.name == "appClInfoForm") {

                                var form = tabPanel.findParentByType('form[name=userMgForm]').getForm();
                                var store = newCard.child('grid[name=viewAppGrid]').store;

                                var oprid = form.findField('OPRID').getValue();
                                if (store.isLoaded() == false) {
                                    var tzParams = '{"ComID":"TZ_UM_USERMG_COM","PageID":"TZ_UM_STUAPPL_STD","OperateType":"QG",' + '"comParams":{"oprid":"' + oprid + '"}}';
                                    Ext.tzLoad(tzParams,function(respData) {
                                        store.loadData(respData.root);
                                    });

                                    this.doLayout();
                                }
                            }
                        }
                    },
                    items : [ {
                        title : '个人信息',
                        xtype : 'form',
                        name : 'userInfoForm',
                        layout : {
                            type : 'vbox',
                            align : 'stretch'
                        },
                        // frame: true,
                        border : false,
                        bodyPadding : 10,
                        // margin:10,
                        bodyStyle : 'overflow-y:auto;overflow-x:hidden',
                        fieldDefaults : {
                            msgTarget : 'side',
                            labelWidth : 120,
                            readOnly:true,
                            fieldStyle : 'background:#F4F4F4',
                            labelStyle : 'font-weight:bold'
                        },
                        items : [
                            {
                                xtype : 'textfield',
                                fieldLabel : '考生编号',
                                readOnly:true,
                                fieldStyle : 'background:#F4F4F4',
                                name : 'kshNo'
                            },
                            {
                                xtype : 'datefield',
                                fieldLabel : '出生日期',
                                name : 'birthdate',
                                readOnly:true,
                                fieldStyle : 'background:#F4F4F4',
                                columnWidth: 1,
                                hideEmptyLabel: true,

                                format: 'Y-m-d'
                            },
                            {
                                xtype : 'textfield',
                                fieldLabel : '联系电话',
                                name : 'zyPhone',
                                readOnly : true
                            },
                            /*{
                                xtype : 'combobox',
                                fieldLabel : '证件类型',
                                name : 'nationType',
                                readOnly:true,
                                fieldStyle : 'background:#F4F4F4',
                                editable:false,
                                valueField: 'TValue',
                                displayField: 'TLDesc',
                                store: new KitchenSink.view.common.store.appTransStore("TZ_NATION_ID_TYPE"),
                                queryMode: 'local',
                            },*/
                            {
                                xtype : 'textfield',
                                fieldLabel : '证件号码',
                                readOnly:true,
                                fieldStyle : 'background:#F4F4F4',
                                name : 'nationId',
                            },
                            {
                                layout: {
                                    type: 'column',
                                    align: 'stretch'
                                },
                                items:[{
                                    xtype: 'textfield',
                                    columnWidth:1,
                                    fieldLabel: '常驻州省',
                                    name: 'lenProvince',
                                    editable:false,
                                    readOnly:true,
                                    fieldStyle : 'background:#F4F4F4',
                                    allowBlank: true,
                                    style:'margin-bottom:4.5px',
                                    promptValidation:{
                                        recName:'PS_STATE_TBL',
                                        presetFields:{
                                        },
                                        valueOfResult:'descr',
                                        displayOfResult:'descr',
                                        errorMsg:''
                                    },
                                    triggers: {
                                        search: {
                                            cls: 'x-form-search-trigger',
                                            handler: "selectProvince"
                                        }
                                    }
                                }]
                            },
                            {
                                xtype : 'combobox',
                                fieldLabel : '是否有海外学历',
                                editable:false,
                                readOnly:true,
                                fieldStyle : 'background:#F4F4F4',
                                valueField: 'TValue',
                                displayField: 'TLDesc',
                                store: new KitchenSink.view.common.store.appTransStore("TZ_SF_SALE"),
                                name : 'isHaiwXuel',
                            },
                            {
                                xtype : 'combobox',
                                fieldLabel : '申请的专业',
                                name : 'appMajor',
                                readOnly:true,
                                fieldStyle : 'background:#F4F4F4',
                                valueField: 'TValue',
                                editable:false,
                                displayField: 'TLDesc',
                                store: new KitchenSink.view.common.store.appTransStore("TZ_MBAX_ZHUANY"),
                                queryMode: 'local',
                            },
                            {
                                xtype : 'textfield',
                                fieldLabel : '紧急联系人',
                                readOnly:true,
                                fieldStyle : 'background:#F4F4F4',
                                name : 'jjlxr',
                            },
                            {
                                xtype : 'combobox',
                                fieldLabel : '紧急联系人性别',
                                editable:false,
                                readOnly:true,
                                fieldStyle : 'background:#F4F4F4',
                                valueField: 'TValue',
                                displayField: 'TLDesc',
                                store: new KitchenSink.view.common.store.appTransStore("TZ_GENDER"),
                                name : 'jjlxrSex',
                            },
                            {
                                xtype : 'textfield',
                                fieldLabel : '紧急联系人电话',
                                readOnly:true,
                                fieldStyle : 'background:#F4F4F4',
                                name : 'jjlxrPhone'
                            }
                        ]
                    },{
                        title:'录取流程信息',
                        xtype:'form',
                        name:'processInfoForm',
                        layout:{
                            type:'vbox',
                            align:'stretch'
                        },
                        border:false,
                        bodyPadding : 10,
                        // margin:10,
                        bodyStyle : 'overflow-y:auto;overflow-x:hidden',
                        fieldDefaults : {
                            msgTarget : 'side',
                            labelWidth : 150,
                            readOnly:true,
                            //fieldStyle : 'background:#F4F4F4',
                            labelStyle : 'font-weight:bold'
                        },
                        items:[{
                            xtype:'textfield',
                            fieldLabel:'面试名次',
                            name:'mspsmc'
                        },{
                            xtype : 'combobox',
                            fieldLabel : '面试批次',
                            editable:false,
                            valueField: 'TValue',
                            displayField: 'TLDesc',
                            store: new KitchenSink.view.common.store.appTransStore("TZ_MSPS_PC"),
                            name : 'mspspc',
                        },{
                            xtype:'combobox',
                            fieldLabel:'条件录取资格（面试结果）',
                            editable:false,
                            valueField:'TValue',
                            displayField:'TLDesc',
                            store:new KitchenSink.view.common.store.appTransStore("TZ_TJLQZG"),
                            name:'tjlqzg'
                        },{
                            xtype:'combobox',
                            fieldLabel:'条件录取资格项目（面试结果）',
                            editable:false,
                            valueField:'TValue',
                            displayField:'TLDesc',
                            store:new KitchenSink.view.common.store.appTransStore("TZ_LQZG_XM"),
                            name:'tjlqzgxm'
                        },{
                            xtype:'combobox',
                            fieldLabel:'获取面试结果批次（面试结果）',
                            editable:false,
                            valueField:'TValue',
                            displayField:'TLDesc',
                            store:new KitchenSink.view.common.store.appTransStore("TZ_MSPS_PC"),
                            name:'msjgpc'
                        },{
                            xtype:'textfield',
                            fieldLabel:'联考报名（是/否）',
                            maxLength:4,
		name:'lkbm'
		},{
		    xtype:'textfield',
		        fieldLabel:'联考前调整情况',
		        name:'lqlzqk'
		},{
		    xtype:'textfield',
		        fieldLabel:'调整原因备注',
		        name:'tzyyNotes'
		},{
		    xtype:'combobox',
		        fieldLabel:'联考前条件录取资格',
		        editable:false,
		        valueField:'TValue',
		        displayField:'TLDesc',
		        store:new KitchenSink.view.common.store.appTransStore("TZ_LKQ_TJLQZG"),
		        name:'lkqtjluzg'
		},{
		    xtype:'combobox',
		        fieldLabel:'联考前条件录取资格项目',
		        editable:false,
		        valueField:'TValue',
		        displayField:'TLDesc',
		        store:new KitchenSink.view.common.store.appTransStore("TZ_LQZG_XM"),
		        name:'lkqtjluzgxm'
		},{
		    xtype:'textfield',
		        fieldLabel:'联考实考（是/否）',
		        maxLength:4,
		        name:'lksk'
		},{
		    xtype:'textfield',
		        fieldLabel:'英语听力过线（是/否）',
		        maxLength:4,
		        name:'yytlgx'
		},{
		    xtype:'textfield',
		        fieldLabel:'政治过线（是/否）',
		        maxLength:4,
		        name:'zzgx'
		},{
		    xtype:'textfield',
		        fieldLabel:'联考过线（是/否）',
		        maxLength:4,
		        name:'lkgx'
		},{
		    xtype:'textfield',
		        fieldLabel:'预录取前调整情况',
		        name:'ylqzzqk'
		},{
		    xtype:'textfield',
		        fieldLabel:'调整原因备注2',
		        name:'tzyyNotes2'
		},{
		    xtype:'combobox',
		        fieldLabel:'预录取资格',
		        editable:false,
		        valueField:'TValue',
		        displayField:'TLDesc',
		        store:new KitchenSink.view.common.store.appTransStore("TZ_YLQ_ZG"),
		        name:'ylqzg'
		},{
		    xtype:'combobox',
		        fieldLabel:'预录取资格项目',
		        editable:false,
		        valueField:'TValue',
		        displayField:'TLDesc',
		        store:new KitchenSink.view.common.store.appTransStore("TZ_LQZG_XM"),
		        name:'ylqzgxm'
		},{
		    xtype:'textfield',
		        fieldLabel:'正式录取前调整情况',
		        name:'zslqzzqk'
		},{
		    xtype:'textfield',
		        fieldLabel:'调整原因备注3',
		        name:'tzyyNotes3'
		},{
		    xtype:'combobox',
		        fieldLabel:'正式录取资格',
		        editable:false,
		        valueField:'TValue',
		        displayField:'TLDesc',
		        store:new KitchenSink.view.common.store.appTransStore("TZ_ZSLQ_ZG"),
		        name:'zslqzg'
		},{
		    xtype:'combobox',
		        fieldLabel:'正式录取资格项目',
		        editable:false,
		        valueField:'TValue',
		        displayField:'TLDesc',
		        store:new KitchenSink.view.common.store.appTransStore("TZ_LQZG_XM"),
		        name:'zslqzgxm'
		},{
		    xtype:'textfield',
		        fieldLabel:'入学前前调整情况',
		        name:'rxqzzqk'
		},{
		    xtype:'textfield',
		        fieldLabel:'调整原因备注4',
		        name:'tzyyNotes4'
		},{
		    xtype:'combobox',
		        fieldLabel:'入学情况',
		        editable:false,
		        valueField:'TValue',
		        displayField:'TLDesc',
		        store:new KitchenSink.view.common.store.appTransStore("TZ_RX_QK"),
		        name:'rxqk'
		},{
		    xtype:'combobox',
		        fieldLabel:'入学项目',
		        editable:false,
		        valueField:'TValue',
		        displayField:'TLDesc',
		        store:new KitchenSink.view.common.store.appTransStore("TZ_LQZG_XM"),
		        name:'rxxm'
		}]
		},{
		    title : '申请材料',
		        xtype : 'form',
		        name : 'appClInfoForm',
		        layout : {
		        type : 'vbox',
		            align : 'stretch'
		    },
		    // frame: true,
		    border : false,
		        bodyPadding : '0 10 10 10',
		        // margin:10,
		        bodyStyle : 'overflow-y:auto;overflow-x:hidden',
		        fieldDefaults : {
		        msgTarget : 'side',
		            labelWidth : 120,
		            labelStyle : 'font-weight:bold'
		    },
		    items : [
		        {
		            xtype: 'grid',
		            height: 180,
		            frame: true,
		            columnLines: false,
		            name: 'viewAppGrid',
		            reference: 'viewAppGrid',
		            style:"margin-top:10px",
		            store: {
		                type: 'stuAppStore'
		            },
		            viewConfig: {
		                enableTextSelection: true
		            },
		            plugins: [{
		                ptype: 'cellediting'
		            }],
		            columns: [
		                {
		                    text: '班级编号',
		                    dataIndex: 'classId',
		                    sortable: false,
		                    hidden:true
		                },
		                {
		                    text: '批次编号',
		                    dataIndex: 'batchId',
		                    sortable: false,
		                    hidden:true
		                },
		                {
		                    text: '报考方向',
		                    dataIndex: 'appInfo',
		                    flex:1,
		                    sortable: false
		                },
		                {
		                    text: '批次名称',
		                    dataIndex: 'batchName',
		                    width:300,
		                    sortable: false
		                },
		                {
		                    text: '申请单编号',
		                    dataIndex: 'appInsId',
		                    sortable: false
		                },
		                {
		                    text: '提交状态',
		                    dataIndex: 'appSubStatus',
		                    sortable: false,
		                    editor: {
		                        xtype: 'combobox',
		                        store: new KitchenSink.view.common.store.appTransStore("TZ_APPFORM_STATE"),
		                        displayField: 'TLDesc',
		                        valueField: 'TValue',
		                        editable: false
		                    },
		                    width:'10%',
		                    renderer:function(value,metadata,record){
		                        console.log(value);
		                        if(value=="U"){
		                            return "已提交";
		                        }else if(value=="OUT"){
		                            return "撤销";
		                        }else if(value=="BACK"){
		                            return "退回修改";
		                        }else{
		                            return "新建";
		                        }
		                    }
		                },
		                {
		                    xtype: 'actioncolumn',
		                    text: '操作',
		                    menuDisabled: true,
		                    menuText: '操作',
		                    sortable: false,
		                    align: 'center',
		                    items:[
		                        {text: '查看报名表',iconCls: 'preview',tooltip: '查看报名表',handler:'viewApplicationForm'},
		                        {text: '打印报名表',iconCls: 'print',tooltip: '打印报名表',handler:'printAppForm'},
		                        {text: '报名流程',iconCls: 'edit',tooltip: '报名流程',handler:'viewBmSch'},
		                        {text: '申请材料查看',iconCls: 'view',tooltip: '申请材料查看',handler:'auditApplicationForm'}
		                    ]
		                }]
		
		        }
		    ]
		}]
		} ]
		} ]
} ],
buttons : [ {
    text : '保存',
    iconCls : "save",
    handler : 'onFormSave'
}, {
    text : '确定',
    iconCls : "ensure",
    handler : 'onFormEnsure'
}, {
    text : '关闭',
    iconCls : "close",
    handler : 'onFormClose'
} ]
});