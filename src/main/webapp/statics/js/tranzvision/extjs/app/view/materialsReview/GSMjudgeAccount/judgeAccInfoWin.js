Ext.define('KitchenSink.view.materialsReview.GSMjudgeAccount.judgeAccInfoWin', {
    extend: 'Ext.panel.Panel',
    requires: [
        'KitchenSink.view.materialsReview.GSMjudgeAccount.judgeAccController'
    ],
    xtype: 'GSMjudgeAccInfo',
    reference: 'judgeAccInfoWindow',
    controller: 'GSMjudgeAccController',

    bodyStyle:'overflow-y:auto;overflow-x:hidden',
    resizable: true,
    modal: true,
    closeAction:'destroy',
    title: '创建评委账号',
    actType: 'add',
    judType: [],
    pGrid: {},


    listeners:{
        close:function(panel, eOpts){
            //panel.pGrid.getEl().unmask();
        }
    },

    constructor: function(judTypeArr){
        this.judType = judTypeArr;
        this.callParent();
    },
    listening:function(field){  var form = field.findParentByType("form");},

    initComponent:function(){

        var jugTypeStore = new KitchenSink.view.common.store.comboxStore({
            recname: 'TZ_JUGTYP_TBL',
            condition:{},
            result:'TZ_JUGTYP_ID,TZ_JUGTYP_NAME'
        });

        var judTypeArr = this.judType;
        var judTpItems = [];

        for(var i=0; i<judTypeArr.length; i++){
            var tItems = {
                xtype:'checkboxfield',
                boxLabel: judTypeArr[i].judgeTypeName,
                name: 'judgeType',
                shuxing:judTypeArr[i].judgeTypeId,
                inputValue: judTypeArr[i].judgeTypeId

            };


            judTpItems.push(tItems);
        }
console.log(judTpItems);
        Ext.apply(this,{
            items: [{
                xtype: 'form',
                layout: {
                    type: 'vbox',
                    align: 'stretch'
                },

                border: false,
                autoScroll : true,
                bodyStyle : 'overflow-x:visible; overflow-y:scroll',
                bodyPadding: 10,
                fieldDefaults: {
                    msgTarget: 'side',
                    labelWidth: 100,
                    labelStyle: 'font-weight:bold'
                },

                items: [{
                    xtype: 'textfield',
                    fieldLabel: Ext.tzGetResourse("TZ_GSM_JUDGE_ACC_COM.TZ_GSM_JUDINFO_STD.accountNo","用户账号"),
                    name: 'accountNo',
                    allowBlank: false
                },{
                    xtype: 'textfield',
                    fieldLabel: Ext.tzGetResourse("TZ_GSM_JUDGE_ACC_COM.TZ_GSM_JUDINFO_STD.password","密码"),
                    inputType: 'password',
                    name: 'password',
                    allowBlank: false
                },{
                    xtype: 'textfield',
                    fieldLabel: Ext.tzGetResourse("TZ_GSM_JUDGE_ACC_COM.TZ_GSM_JUDINFO_STD.rePassword","确认密码"),
                    inputType: 'password',
                    name: 'rePassword',
                    allowBlank: false
                },{
                    xtype: 'textfield',
                    fieldLabel: Ext.tzGetResourse("TZ_GSM_JUDGE_ACC_COM.TZ_GSM_JUDINFO_STD.judgeName","用户描述"),
                    name: 'judgeName'
                },{
                    xtype: 'checkboxgroup',
                    fieldLabel: Ext.tzGetResourse("TZ_JUDGE_ACC_COM.TZ_JUDINFO_STD.judgeType","评委类型"),
                  //  id: 'judgeTypeCheckboxGroup',
                    itemId:'judgeTypeCheckboxGroup',
                    vertical: true,
                    items: judTpItems,
                    allowBlank: false,
                    columns: 1,
                    listeners:{
                                 change:  function (field, newValue, oldValue) {
                                         var form = field.findParentByType("form");
                                         var CLPW = form.down("checkboxfield[shuxing='001']").getValue();
                                         var XZR = form.down("checkboxfield[shuxing='003']").getValue();

                                     var oldstring = oldValue instanceof Array ? oldValue.judgeType.join(''):String(oldValue.judgeType);
                                 //    console.log(oldstring);
                                     console.log(arguments)
                                           if (CLPW == true  && oldstring.indexOf('003')>=0) {
                                               this.removeListener('change',arguments.callee);
                                             form.down("checkboxfield[shuxing='003']").setValue(false);
                                               this.addListener('change',arguments.callee);
                                         }
                                         if (XZR == true && oldstring.indexOf('001')>=0) {
                                             this.removeListener('change',arguments.callee);
                                             form.down("checkboxfield[shuxing='001']").setValue(false);
                                             this.addListener('change',arguments.callee);
                                         }
                                     }
                                        }

                },
//                    {
//                    xtype: 'textfield',
//                    name: 'classID',
//                    hidden:true,
//                    listeners:{
//                        change:function(  newValue, oldValue, eOpts ){
//
//                            console.log(oldValue);
//
//var classID=oldValue;
//          var params = '{"ComID":"TZ_GSM_JUDGE_ACC_COM","PageID":"TZ_GSM_JUDINFO_STD","OperateType":"QFdepart","comParams":{"type":"QFdepart","classID":"' + classID + '"}}';
//                            Ext.tzLoad(params, function(resp){});
//                        }
//                    }
//
//                },
//                    {
//                    xtype: 'textfield',
//                    fieldLabel: "所属班级",
//                    name: 'className',
//                    triggers: {
//                        search: {
//                            cls: 'x-form-search-trigger',
//                            handler:'selectClassName'
//                        }
//                    }
//
//                },
                    {
                    xtype: 'textfield',
                    fieldLabel: "手机",
                    name: 'judgePhoneNumber'
                },{
                    xtype: 'textfield',
                    fieldLabel: "邮箱",
                    name: 'judgeEmail'
                },{
                    xtype: 'textfield',
                    fieldLabel: "OA系统账号",
                    name: 'judgeOAID'
               },{
                    xtype: 'textfield',
                    fieldLabel: "所属系",
                    name: 'judgeDepart'
                },
// {
//                    xtype: 'textfield',
//                    fieldLabel: "所属系",
//                    name: 'judgeDepart',
//
//                    triggers: {
//                        search: {
//                            cls: 'x-form-search-trigger',
//                            handler:'selectJudgeDepart'
//                        }
//                    }
//                },
                   {
                    layout: {
                        type: 'column'
                    },
                    items:[{
                        columnWidth:.35,
                        xtype: 'displayfield',
                        fieldLabel: Ext.tzGetResourse("TZ_GSM_JUDGE_ACC_COM.TZ_GSM_JUDINFO_STD.orgId","所属机构"),
                        name: 'orgId'
                    },{
                        columnWidth:.65,
                        xtype: 'displayfield',
                        hideLabel: true,
                        name: 'orgDesc'
                    }]
                },{
                    xtype: 'checkbox',
                    fieldLabel: Ext.tzGetResourse("TZ_GSM_JUDGE_ACC_COM.TZ_GSM_JUDINFO_STD.clockFlag","锁定用户"),
                    name: 'clockFlag',
                    inputValue: "Y"
                }]
            }]
        })
        this.callParent();
    },
    buttons: [{
        text: '保存',
        iconCls:"save",
        handler: 'onJudgeAccSave'
    }, {
        text: '确定',
        iconCls:"ensure",
        handler: 'onJudgeAccEnsure'
    }, {
        text: '关闭',
        iconCls:"close",
        handler: 'onWinClose'
    }]

});
