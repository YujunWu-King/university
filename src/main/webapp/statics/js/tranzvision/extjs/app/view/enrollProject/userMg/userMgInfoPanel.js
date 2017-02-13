Ext.define('KitchenSink.view.enrollProject.userMg.userMgInfoPanel', {
    extend: 'Ext.panel.Panel',
    xtype: 'userMgInfoPanel',
    controller: 'userMgController',
    requires: [
        'Ext.data.*',
        'Ext.grid.*',
        'Ext.util.*',
        'Ext.toolbar.Paging',
        'Ext.ux.ProgressBarPager',
    ],
    listeners:{
        resize: function(win){
            win.doLayout();
        }
    },
    actType: '',
    title: '会员用户信息',
    bodyStyle:'overflow-y:auto;overflow-x:hidden',
    items: [{
        xtype: 'form',
        reference: 'userMgForm',
        layout: {
            type: 'vbox',
            align: 'stretch'
        },
        border: false,
        bodyPadding: 10,
        bodyStyle:'overflow-y:auto;overflow-x:hidden',

        fieldDefaults: {
            msgTarget: 'side',
            labelWidth: 110,
            labelStyle: 'font-weight:bold'
        },
        items: [{
            layout: {
                type: 'column'
            },
            items:[{
                columnWidth:.2,
                xtype: "image",
                src: "",
                height:300,
                width:217,
                name: "titileImage"

            },{
                columnWidth:.8,
                bodyStyle:'padding-left:30px',
                layout: {
                    type: 'vbox',
                    align: 'stretch'
                },
                items: [{
                    xtype: 'textfield',
                    readOnly:true,
                    fieldLabel: '用户编号',
                    name: 'OPRID',
                    fieldStyle:'background:#F4F4F4'
                },{
                    xtype: 'textfield',
                    fieldLabel: '面试申请号',
                    readOnly:true,
                    name: 'msSqh',
                    fieldStyle:'background:#F4F4F4'
                },{
                    xtype: 'textfield',
                    fieldLabel: '姓名',
                    readOnly:true,
                    name: 'userName',
                    fieldStyle:'background:#F4F4F4'
                },{
                    xtype: 'textfield',
                    fieldLabel: '性别',
                    readOnly:true,
                    name: 'userSex',
                    fieldStyle:'background:#F4F4F4'
                },{
                    xtype: 'textfield',
                    fieldLabel: '邮箱',
                    readOnly:true,
                    name: 'userEmail',
                    fieldStyle:'background:#F4F4F4'
                },{
                    xtype: 'textfield',
                    fieldLabel: '手机',
                    readOnly:true,
                    name: 'userPhone',
                    fieldStyle:'background:#F4F4F4'
                }/*,{
                    xtype: 'textfield',
                    fieldLabel: '账号激活状态',
                    readOnly:true,
                    name: 'jihuoZt',
                    fieldStyle:'background:#F4F4F4'
                }*/,{
                    xtype: 'combo',
                    fieldLabel: '账号激活状态',
                    name: 'jihuoZt',
                    emptyText:'请选择',
                    queryMode: 'remote',
                    editable:false,
                    valueField: 'TZ_ZHZ_ID',
                    displayField: 'TZ_ZHZ_DMS',
                    store:new KitchenSink.view.common.store.comboxStore({
                        recname: 'TZ_PT_ZHZXX_TBL',
                        condition:{
                            TZ_ZHZJH_ID:{
                                value: "TZ_JIHUO_ZT",
                                operator:"01",
                                type:"01"
                            },
                            TZ_EFF_STATUS:{
                                value: "A",
                                operator:"01",
                                type:"01"
                            }
                        },
                        result:'TZ_ZHZ_ID,TZ_ZHZ_DMS'
                    })
                },{
                    xtype: 'combo',
                    fieldLabel: '账号锁定状态',
                    name: 'acctlock',
                    emptyText:'请选择',
                    queryMode: 'remote',
                    editable:false,
                    valueField: 'TZ_ZHZ_ID',
                    displayField: 'TZ_ZHZ_DMS',
                    store:new KitchenSink.view.common.store.comboxStore({
                        recname: 'TZ_PT_ZHZXX_TBL',
                        condition:{
                            TZ_ZHZJH_ID:{
                                value: "ACCTLOCK",
                                operator:"01",
                                type:"01"
                            },
                            TZ_EFF_STATUS:{
                                value: "A",
                                operator:"01",
                                type:"01"
                            }
                        },
                        result:'TZ_ZHZ_ID,TZ_ZHZ_DMS'
                    })
                }
                    ,{
                        xtype: 'textfield',
                        fieldLabel: '创建日期时间',
                        readOnly:true,
                        name: 'zcTime',
                        fieldStyle:'background:#F4F4F4'
                    },{
                        xtype: 'radiogroup',
                        fieldLabel: '黑名单用户',
                        readOnly:true,
                        name: 'blackNames',
                        fieldStyle:'background:#F4F4F4',
                        columns: 6,
                        items:[{
                        	boxLabel: "是",
                            name: "blackName",
                            inputValue: "Y",
                            readOnly : true
                        },{
                        	boxLabel: "否",
                            name: "blackName",
                            inputValue: "N",
                            readOnly : true
                        }]  
                    },{
                        xtype: 'radiogroup',
                        fieldLabel: '允许继续申请',
                        readOnly:true,
                        name: 'allowApplys',
                        fieldStyle:'background:#F4F4F4',
                        columns: 6,
                        items:[{
                        	boxLabel: "是",
                            name: "allowApply",
                            inputValue: "Y",
                            readOnly : true
                        },{
                        	boxLabel: "否",
                            name: "allowApply",
                            inputValue: "N",
                            readOnly : true
                        }]
                    },{
                        xtype: 'textarea',
                        fieldLabel: '备注',
                        name: 'beizhu',
                        //fieldStyle:'background:#F4F4F4'
                    },{
                        xtype: 'hiddenfield',
                        name: 'titleImageUrl'
                    }]
            },{
                columnWidth:1,
                bodyStyle:'padding-top:10px',
                layout: {
                    type: 'vbox',
                    align: 'stretch'
                },
                items: [
                    {
                        title: '个人信息',
                        xtype: 'form',
                        name: 'userInfoForm',
                        layout: {
                            type: 'vbox',
                            align: 'stretch'
                        },
                        frame: true,
                        border: false,
                        bodyPadding: 10,
                        //margin:10,
                        bodyStyle:'overflow-y:auto;overflow-x:hidden',
                        fieldDefaults: {
                            msgTarget: 'side',
                            labelWidth: 100,
                            labelStyle: 'font-weight:bold'
                        },
                        items: []
                    }
                ]}
            ]
        }]
    }],
    buttons: [
        {
            text: '保存',
            iconCls:"save",
            handler: 'onFormSave'
        }, {
            text: '确定',
            iconCls:"ensure",
            handler: 'onFormEnsure'
        }, {
            text: '关闭',
            iconCls:"close",
            handler: 'onFormClose'
        }]
});