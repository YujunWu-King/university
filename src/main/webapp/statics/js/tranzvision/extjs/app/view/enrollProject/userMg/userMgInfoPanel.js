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

    items : [
        {
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
            items : [
                {
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
                                name : 'userName'
                            }, {
                                xtype : 'combo',
                                fieldLabel : '性别',
                                name : 'userSex',
                                valueField: 'TValue',
                                editable:false,
                                displayField: 'TLDesc',
                                store: new KitchenSink.view.common.store.appTransStore("TZ_GENDER"),
                                queryMode: 'local'
                            },
                                {
                                    xtype : 'textfield',
                                    fieldLabel : '绑定邮箱',
                                    name : 'userEmail'
                                },
                                {
                                    xtype : 'checkboxfield',
                                    fieldLabel : '邮箱绑定标志',
                                    inputValue:'Y',
                                    uncheckedValue:'N',
                                    name : 'bindEmailFlg'
                                },
                                {
                                    xtype : 'textfield',
                                    fieldLabel : '绑定手机',
                                    name : 'userPhone'
                                },
                                {
                                    xtype : 'checkboxfield',
                                    fieldLabel : '手机绑定标志',
                                    inputValue:'Y',
                                    uncheckedValue:'N',
                                    name : 'bindPhoneFlg'
                                },
                                {
                                    xtype : 'combo',
                                    fieldLabel : '账号激活状态',
                                    name : 'jihuoZt',
                                    emptyText : '请选择',
                                    //queryMode : 'remote',
                                    editable : false,
                                    valueField: 'TValue',
                                    editable:false,
                                    displayField: 'TLDesc',
                                    store: new KitchenSink.view.common.store.appTransStore("TZ_JIHUO_ZT"),
                                    queryMode: 'local'
                                },
                                {
                                    xtype : 'combo',
                                    fieldLabel : '账号锁定状态',
                                    name : 'acctlock',
                                    emptyText : '请选择',
                                    //queryMode : 'remote',
                                    editable : false,
                                    valueField: 'TValue',
                                    editable:false,
                                    displayField: 'TLDesc',
                                    store: new KitchenSink.view.common.store.appTransStore("ACCTLOCK"),
                                    queryMode: 'local'
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
                                	 var form = tabPanel.findParentByType('form[name=userMgForm]').getForm();
                                	 var oprid = form.findField('OPRID').getValue();

                                	 
                                    if (newCard.name == "appClInfoForm") {
                                       
                                        var store = newCard.child('grid[name=viewAppGrid]').store;
                                       
                                        if (store.isLoaded() == false) {
                                            var tzParams = '{"ComID":"TZ_UM_USERMG_COM","PageID":"TZ_UM_STUAPPL_STD","OperateType":"QG",' + '"comParams":{"oprid":"' + oprid + '"}}';
                                            Ext.tzLoad(tzParams,function(respData) {
                                                store.loadData(respData.root);
                                            });

                                            this.doLayout();
                                        }
                                    }else if(newCard.name =="kslcInfoForm"){
                                        var tz_params='{"ComID":"TZ_UM_USERMG_COM","PageID":"TZ_UM_BMLC_STD","OperateType":"DYITEMS","comParams":{"oprid":"'  + oprid + '"}}'
                                        Ext.tzLoad(tz_params,function(response){
                                            var formData=response.formData;
                                            var fieldsetItems=[];
                                            for(var i=0;i<formData.length;i++){
                                                var lcname = formData[i].lcname;
                                                var lcNamedesc = formData[i].lcNamedesc;
                                                var table = formData[i].table;
                                                var items=formData[i].items;
                                                var conItems = [];
                                                var fieldset={};
                                                for(var fieldname in items){
                                                    var fieldname1=items[fieldname].fieldname;
                                                    var field=items[fieldname].field;
                                                    var srkLxing=items[fieldname].srkLxing;
                                                    var value =items[fieldname].value;
                                                    var typeField={};
                                                    if(srkLxing=="DROP"){
                                                        typeField = {
                                                            xtype: 'combobox',
                                                            columnWidth: 1,
                                                            autoSelect: false,
                                                            editable:false,
                                                            fieldLabel:fieldname1,
                                                            store: new KitchenSink.view.common.store.comboxStore({
                                                                recname: 'PS_TZ_KSLC_DROP_T',
                                                                condition:{
                                                                    TZ_TPL_ID:{
                                                                        value:lcname,
                                                                        operator:"01",
                                                                        type:"01"
                                                                    },
                                                                    TZ_FIELD:{
                                                                        value:field,
                                                                        operator:"01",
                                                                        type:"01"
                                                                    }
                                                                },
                                                                result:'TZ_OPT_ID,TZ_OPT_VALUE'
                                                            }),
                                                            valueField: 'TZ_OPT_ID',
                                                            displayField: 'TZ_OPT_VALUE',
                                                            queryMode: 'remote',
                                                            name:table+"_tf_"+field,
                                                            value: value,
                                                            triggers:{
                                                                clear: {
                                                                    cls: 'x-form-clear-trigger',
                                                                    handler: function(field){
                                                                        field.setValue("");
                                                                    }
                                                                }
                                                            }
                                                        };
                                                    }else{
                                                        typeField = {
                                                            xtype: 'textfield',
                                                            columnWidth: 1,
                                                            fieldLabel:fieldname1,
                                                            value:value,
                                                            name: table+"_tf_"+field
                                                        };
                                                    }
                                                    conItems.push(typeField);
                                                }
                                                fieldset={
                                                    xtype: 'fieldset',
                                                    title: lcNamedesc,
                                                    name:table,
                                                    layout:{
                                                        type:'vbox',
                                                        align:'stretch'
                                                    },
                                                    items:conItems
                                                }
                                                fieldsetItems.push(fieldset);
                                                console.log("fieldset"+fieldset);
                                            }
                                            newCard.removeAll();
                                            console.log("fieldsetItems"+fieldsetItems);
                                            newCard.add(fieldsetItems);
                                            newCard.doLayout();
                                        });
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
                                    labelStyle : 'font-weight:bold'
                                },
                                items : [
                                    /*{
                                        xtype : 'textfield',
                                        fieldLabel : '考生编号',
                                        name : 'kshNo'
                                    },*/
//                                    {
//                                        xtype : 'textfield',
//                                        fieldLabel : '联系电话',
//                                        name : 'zyPhone'
//                                    },
                                    {
                                        xtype : 'textfield',
                                        fieldLabel : '真实姓名',
                                        name : 'realname'
                                    },
//                                    {
//                                        xtype : 'datefield',
//                                        fieldLabel : '出生日期',
//                                        name : 'birthdate',
//                                        columnWidth: 1,
//                                        hideEmptyLabel: true,
//                                        format: 'Y-m-d'
//                                    },
                                    {
                                        xtype : 'textfield',
                                        fieldLabel : '本科院校',
                                        name : 'shcoolcname'
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
                                            allowBlank: true,
                                            style:'margin-bottom:4.5px',
                                            triggers: {
                                                search: {
                                                    cls: 'x-form-search-trigger',
                                                    handler: "selectProvince"
                                                }
                                            }
                                        }]
                                    },
                                    {
                                        xtype : 'textfield',
                                        fieldLabel : '工作单位',
                                        name : 'companyname',
                                    },
                                    {           
                                        fieldLabel : '行业类别',
                                        name : 'copmindustry',
                                        xtype : 'combo',
                                        emptyText : '请选择',
                                        //queryMode : 'remote',
                                        editable : false,
                                        valueField: 'TValue',
                                        editable:false,
                                        displayField: 'TLDesc',
                                        store: new KitchenSink.view.common.store.appTransStore("TZ_MBA_ZS_HYLB2"),
                                        queryMode: 'local'
                                    }
                                ]
                            },{
            					title : '流程状态',
            					xtype : 'form',
            					name : 'kslcInfoForm',
            					reference:'kslcInfoForm',
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
            						labelWidth : 210,
            						labelStyle : 'font-weight:bold'
            					},
            					items : []
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
                                                    if(value=="U"){
                                                        return "已提交";
                                                    }else if(value=="OUT"){
                                                        return "撤销";
                                                    }else if(value=="BACK"){
                                                        return "退回修改";
                                                    }else if(value=="P"){
                                                        return "预提交"
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
        name: 'save',
        iconCls : "save",
        handler : 'onFormSave'
    }, {
        text : '确定',
        name: 'ensure',
        iconCls : "ensure",
        handler : 'onFormEnsure'
    }, {
        text : '关闭',
        iconCls : "close",
        handler : 'onFormClose'
    } ]
});