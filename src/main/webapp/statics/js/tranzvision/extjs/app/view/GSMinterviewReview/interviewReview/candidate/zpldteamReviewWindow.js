Ext.define('KitchenSink.view.GSMinterviewReview.interviewReview.candidate.zpldteamReviewWindow', {
    extend: 'Ext.window.Window',
    //extend: 'Ext.panel.Panel',
	reference: 'zpldteamReviewWindow',
    controller: 'candidateController',
	requires: [
        'Ext.data.*',
        'Ext.grid.*',
        'Ext.util.*',
        'Ext.toolbar.Paging',
        'Ext.ux.ProgressBarPager',
		'KitchenSink.view.GSMinterviewReview.interviewReview.candidate.zpldteamReviewWindowStore'

    ],

	actType : "update",//默认add
    whichColnums:"",
    title:"招聘领导小组审议",
    //frame: true,
    width: 800,
    height: 500,
    modal:true,
    layout:{
        type:'fit'
    },
    constructor:function(store,fields,flag){
        this.scoreStore = store;
        var columns = [{
            text: '小组成员',
            minWidth: 150,
            dataIndex: 'pwName',
            flex:1
        },{
            text: '是否投票',
            minWidth: 100,
            dataIndex: 'ifVote',
            defaultvalue: '已投票',
            flex:1
        }];
        var backcolums = [{
            header: '退回',
            xtype: 'linkcolumn',
            sortable: false,
            flex: 2,
            value: '退回',
            label: '退回',
            minWidth: 75,
            handler: 'viewCancel'
            },/*{
            header: '提交',
            xtype: 'linkcolumn',

            sortable: false,
            flex: 2,
            value: '提交',
            label: '提交',
            minWidth: 75,
            handler: 'submitMS'
            },*/{
            text: "操作",
            menuDisabled: true,
            sortable: false,
            align: 'center',
            minWidth: 75,
            xtype: 'actioncolumn',
            items: [
                {iconCls: 'edit', tooltip: '编辑申请人', handler: 'editApplicant23'}
                ]
            }];
        for(var x = 0;x<fields.length;x++){
            if(fields[x].type==='N'){
                columns.push({
                    text:fields[x].name,
                    dataIndex:fields[x].ID,
                    minWidth:100,
                    align:'center',
                    flex:1,
                    renderer:function(v){
                        return "<span title='"+v+"'>"+v+"</span>";
                    }
                });
            }else{
                this.whichColnums = fields[x].ID;
                columns.push({
                    text:fields[x].name,
                    dataIndex:fields[x].ID,
                    minWidth:100,
                    align:'center',
                    flex:1,
                    renderer:function(v){
                        var x;
                        if((x = store.findExact('TValue',v))>=0){
                            return "<span title='"+store.getAt(x).data.TSDesc+"'>"+store.getAt(x).data.TSDesc+"</span>";
                        }else{
                            return "<span title='"+v+"'>"+v+"</span>";
                        }
                    }
                });
            }
        }
        if(flag){
            columns = columns.concat(backcolums);
        }
        Ext.apply(this,{
           items: [
            {
            xtype: 'form',
                reference: 'zpldForm',
                fieldDefaults: {
                    msgTarget: 'side',
                    labelWidth: 100,
                    labelStyle: 'font-weight:bold'
                },
                items: [
                    {
                        xtype: 'textfield',
                        name: 'classID',
                        hidden: true

                    },
                    {
                        xtype: 'textfield',
                        name: 'batchID',
                        hidden: true

                    },
                    {
                        xtype: 'displayfield',
                        fieldLabel: '申请人',
                        style: "margin:8px",
                        name: 'realName'

                    },
                    {
                        xtype: 'displayfield',
                        fieldLabel: '审议通过率',
                        style: "margin:8px",
                        name: 'zpldxzsytgl'

                    },{
                        xtype: 'grid',
                        columnLines: true,
                        autoHeight:true,
                        reference: 'zpldteamReviewGrid',
                        name: 'zpldteamReviewGrid',

                        bodyStyle: 'overflow-y:auto;overflow-x:hidden',
                        columns:columns,
                        //columns-end
                        
                    }

                ]
        }],
                });
                this.callParent();
        },

    buttons: [{
		text: '保存',
		iconCls:"save",
		//handler: 'onteamReviewMsgSave'
        handler:function(btn){
            var store = btn.findParentByType('window').down('grid[name=zpldteamReviewGrid]').store;
            var removedData = store.getRemovedRecords(),
                updateData = store.getModifiedRecords(),
                comParas,JSONData={},
                classID = btn.findParentByType('window').child('form').getForm().findField('classID').getValue(),
                batchID = btn.findParentByType('window').child('form').getForm().findField('batchID').getValue();
            //新增数据
           /* if(newData.length !== 0) {
                JSONData.add = [];
                for (var x = newData.length - 1; x >= 0; x--) {
                    JSONData.add[x] = {};
                    JSONData.add[x].appINSID = newData[x].data.appINSID;
                    JSONData.add[x].classID = classID;
                    JSONData.add[x].batchID = batchID;
                }
            }*/
            //删除数据
           /* if(removedData.length !== 0){
                JSONData.delete = [];
                for(var x =removedData.length-1;x>=0;x--){
                    JSONData.delete[x] = {};
                    JSONData.delete[x].appINSID = removedData[x].data.appINSID;
                    JSONData.delete[x].classID = classID;
                    JSONData.delete[x].batchID = batchID;
                }
            }*/
            //更新数据
            if(updateData.length !== 0){
                JSONData.update =[];
                for(var x = updateData.length-1;x>=0;x--){
                    JSONData.update[x] ={};
                    delete updateData[x].data.id;
                    JSONData.update[x].classID = classID;
                    JSONData.update[x].batchID = updateData[x].data.batchID;
                    JSONData.update[x].appInsID = updateData[x].data.appInsID;
                    JSONData.update[x].pwID = updateData[x].data.pwID;
                    for(var n in updateData[x].data){
                        if(updateData[x].isModified(n)){
                            JSONData.update[x].ReviewResult = updateData[x].data[n];
                        }
                    }
                    //JSONData.update[x].reviewStatus = updateData[x].data.LUQUZT;
                }
            }
            //表单中的数据
            comParas=Ext.JSON.encode(JSONData);
            //提交参数
            var tzParams = '{"ComID":"TZ_GSM_CANDIDATE_COM","PageID":"TZ_ZPLDTEAM_STD","OperateType":"U","comParams":' + comParas + '}';
            Ext.tzSubmit(tzParams,function(responseData){
                var thisValue = responseData.zpldxzsytgl||btn.findParentByType('panel').child('form').getForm().findField('zpldxzsytgl').getValue();
                btn.findParentByType('panel').child('form').getForm().findField('zpldxzsytgl').setValue(thisValue);
                var grid = btn.findParentByType('panel').down("grid[name=zpldteamReviewGrid]");
                var store = grid.getStore();
                if(tzParams.indexOf("add")>-1||tzParams.indexOf("delete")>-1||tzParams.indexOf("update")){
                    store.reload();
                }
            },"",true,this);
        }
	},{
        text: '确定',
        iconCls:"ensure",
       // handler: 'onteamReviewSure'
        handler:function(btn){
            var store = btn.findParentByType('window').down('grid[name=zpldteamReviewGrid]').store,
                newData = store.getNewRecords(),
                removedData = store.getRemovedRecords(),
                updateData = store.getModifiedRecords(),
                comParas,JSONData={},
                classID = btn.findParentByType('window').child('form').getForm().findField('classID').getValue(),
                batchID = btn.findParentByType('window').child('form').getForm().findField('batchID').getValue();
            //新增数据
            /* if(newData.length !== 0) {
             JSONData.add = [];
             for (var x = newData.length - 1; x >= 0; x--) {
             JSONData.add[x] = {};
             JSONData.add[x].appINSID = newData[x].data.appINSID;
             JSONData.add[x].classID = classID;
             JSONData.add[x].batchID = batchID;
             }
             }*/
            //删除数据
            /* if(removedData.length !== 0){
             JSONData.delete = [];
             for(var x =removedData.length-1;x>=0;x--){
             JSONData.delete[x] = {};
             JSONData.delete[x].appINSID = removedData[x].data.appINSID;
             JSONData.delete[x].classID = classID;
             JSONData.delete[x].batchID = batchID;
             }
             }*/
            //更新数据

            if(updateData.length !== 0){
                JSONData.update =[];
                for(var x = updateData.length-1;x>=0;x--){
                    JSONData.update[x] ={};
                    delete updateData[x].data.id;
                    JSONData.update[x].classID = classID;
                    JSONData.update[x].batchID = updateData[x].data.batchID;
                    JSONData.update[x].appInsID = updateData[x].data.appInsID;
                    JSONData.update[x].pwID = updateData[x].data.pwID;
                    for(var n in updateData[x].data){
                        if(updateData[x].isModified(n)){
                            JSONData.update[x].ReviewResult = updateData[x].data[n];
                        }
                    }
                    //JSONData.update[x].reviewStatus = updateData[x].data.LUQUZT;
                }
            }

            comParas=Ext.JSON.encode(JSONData);
            //表单中的数据
            //提交参数
            var tzParams = '{"ComID":"TZ_GSM_CANDIDATE_COM","PageID":"TZ_ZPLDTEAM_STD","OperateType":"U","comParams":' + comParas + '}';
            Ext.tzSubmit(tzParams,function(responseData){
               var grid = btn.findParentByType('panel').down("grid[name=zpldteamReviewGrid]");
               grid.store.commitChanges();
                btn.findParentByType('panel').close();

                //var interviewMgrPanel=Ext.ComponentQuery.query("panel[reference=candidateStudentGrid]");
                //var store=interviewMgrPanel[0].getStore();
                //var  store11 = interviewMgrPanel[0].getStore();

                var activeTab = Ext.getCmp('tranzvision-framework-content-panel').getActiveTab();
                var  store11  = activeTab.down("grid[name=candidateStudentGrid]").getStore();
                store11.reload();
                //grid11.getStore().reload();

            },"",true,this);
        }
    }, {
		text: '关闭',
		iconCls:"close",
		//handler: 'onteamReviewClose'
        handler:function(btn){
            btn.findParentByType('window').close();
            var activeTab = Ext.getCmp('tranzvision-framework-content-panel').getActiveTab();
                var  store11  = activeTab.down("grid[name=candidateStudentGrid]").getStore();
                store11.reload();
            //this.up('panel').close();
            //this.getView().close();
        }
	}]
});
