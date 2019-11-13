Ext.define('KitchenSink.view.enrollProject.userApply.userBmSchView', {
    extend : 'Ext.panel.Panel',
    xtype : 'userBmSchView',
    reference : 'userBmSchView',
    controller : 'userApplyController',
    requires : [
        'Ext.data.*',
        'Ext.grid.*',
        'Ext.util.*',
        'Ext.toolbar.Paging',
        'Ext.ux.ProgressBarPager',
    ],
    listeners : {
        resize : function(win) {
            win.doLayout();
        }
    },
    actType : '',
    title : '考生报名流程查看',
    bodyStyle : 'overflow-y:auto;overflow-x:hidden',

    items : [ {
        xtype : 'form',
        reference : 'userApplyForm',
        name:'userApplyForm',
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
        items : [{
            layout : {
                type : 'column'
            },
            items : [ {
                columnWidth : .15,
                xtype : "image",
                src : "",
                height : 193,
                width : 140,
                name : "titileImage"

            },{
                columnWidth : .85,
                bodyStyle : 'padding-left:30px',
                layout : {
                    type : 'vbox',
                    align : 'stretch',
                    readOnly : true,
                },
                items : [ {
                    xtype : 'textfield',
                    fieldLabel : '用户编号',
                    name : 'OPRID',
                    readOnly:true,
                    fieldStyle : 'background:#F4F4F4'
                },{
                    xtype : 'textfield',
                    fieldLabel : '报名表编号',
                    name : 'APPID',
                    readOnly:true,
                    hidden:true,
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
                }, {
                    xtype : 'textfield',
                    fieldLabel : '创建日期时间',
                    readOnly : true,
                    name : 'zcTime',
                    fieldStyle : 'background:#F4F4F4'
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
                	load: function(tabPanel) {
//                    	 var form = tabPanel.findParentByType('form[name=userApplyForm]').getForm();
//                    	 var oprid = form.findField('OPRID').getValue();
//                    	 var newCard =tabPanel.down('form')
//                    	 if(newCard.name =="kslcInfoForm"){
//                            var tz_params='{"ComID":"TZ_UM_USERMG_COM","PageID":"TZ_UM_BMLC_STD","OperateType":"DYITEMS","comParams":{"oprid":"'  + oprid + '"}}'
//                            Ext.tzLoad(tz_params,function(response){
//                                var formData=response.formData;
//                                var fieldsetItems=[];
//                                for(var i=0;i<formData.length;i++){
//                                    var lcname = formData[i].lcname;
//                                    var lcNamedesc = formData[i].lcNamedesc;
//                                    var table = formData[i].table;
//                                    var items=formData[i].items;
//                                    var conItems = [];
//                                    var fieldset={};
//                                    for(var fieldname in items){
//                                        var fieldname1=items[fieldname].fieldname;
//                                        var field=items[fieldname].field;
//                                        var srkLxing=items[fieldname].srkLxing;
//                                        var value =items[fieldname].value;
//                                        var typeField={};
//                                        if(srkLxing=="DROP"){
//                                            typeField = {
//                                                xtype: 'combobox',
//                                                columnWidth: 1,
//                                                autoSelect: false,
//                                                editable:false,
//                                                fieldLabel:fieldname1,
//                                                store: new KitchenSink.view.common.store.comboxStore({
//                                                    recname: 'PS_TZ_KSLC_DROP_T',
//                                                    condition:{
//                                                        TZ_TPL_ID:{
//                                                            value:lcname,
//                                                            operator:"01",
//                                                            type:"01"
//                                                        },
//                                                        TZ_FIELD:{
//                                                            value:field,
//                                                            operator:"01",
//                                                            type:"01"
//                                                        }
//                                                    },
//                                                    result:'TZ_OPT_ID,TZ_OPT_VALUE'
//                                                }),
//                                                valueField: 'TZ_OPT_ID',
//                                                displayField: 'TZ_OPT_VALUE',
//                                                queryMode: 'remote',
//                                                name:table+"_tf_"+field,
//                                                value: value,
//                                                triggers:{
//                                                    clear: {
//                                                        cls: 'x-form-clear-trigger',
//                                                        handler: function(field){
//                                                            field.setValue("");
//                                                        }
//                                                    }
//                                                }
//                                            };
//                                        }else{
//                                            typeField = {
//                                                xtype: 'textfield',
//                                                columnWidth: 1,
//                                                fieldLabel:fieldname1,
//                                                value:value,
//                                                name: table+"_tf_"+field
//                                            };
//                                        }
//                                        conItems.push(typeField);
//                                    }
//                                    fieldset={
//                                        xtype: 'fieldset',
//                                        title: lcNamedesc,
//                                        name:table,
//                                        layout:{
//                                            type:'vbox',
//                                            align:'stretch'
//                                        },
//                                        items:conItems
//                                    }
//                                    fieldsetItems.push(fieldset);
//                                    console.log("fieldset"+fieldset);
//                                }
//                                newCard.removeAll();
//                                console.log("fieldsetItems"+fieldsetItems);
//                                newCard.add(fieldsetItems);
//                                newCard.doLayout();
//                            });
//                        }
                        
                    }
                },
                items : [{
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
                }]
            } ]
        } ]
    } ],
    buttons : [ /*{
        text : '保存',
        iconCls : "save",
        handler : 'onFormSave2'
    }, {
        text : '确定',
        iconCls : "ensure",
        handler : 'onFormEnsure2'
    },*/ {
        text : '关闭',
        iconCls : "close",
        handler : 'onFormClose'
    } ]
});