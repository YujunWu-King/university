Ext.define('KitchenSink.view.GSMinterviewReview.interviewReview.candidate.xswyhReviewWindow', {
    extend: 'Ext.window.Window',
    //extend: 'Ext.panel.Panel',
	reference: 'xswyhReviewWindow',
    name:'xswyhReviewWindow',
    controller: 'candidateController',
	requires: [
        'Ext.data.*',
        'Ext.grid.*',
        'Ext.util.*',
        'Ext.toolbar.Paging',
        'Ext.ux.ProgressBarPager',
		'KitchenSink.view.GSMinterviewReview.interviewReview.candidate.xswyhReviewWindowStore'

    ],

	actType : "update",//默认add
    title:"学术委员会审议",
    //frame: true,

    width: 800,
    height: 500,
    modal:true,
    bodyStyle:'overflow-y:auto;overflow-x:hidden',
	items: [

        {
            xtype: 'form',
            reference: 'zpldForm',
            layout: {
                type: 'vbox',
                align: 'stretch'
            },
            border: false,
            //bodyPadding: 10,
            style:"margin:8px",
            //heigth: 300,
            bodyStyle:'overflow-y:auto;overflow-x:hidden',

            fieldDefaults: {
                msgTarget: 'side',
                labelWidth: 100,
                labelStyle: 'font-weight:bold'
            },
           items: [
                {
                    xtype: 'textfield',
                    fieldLabel: '班级ID',
                    name: 'classID',
                    hidden:true

                },
                {
                    xtype: 'textfield',
                    fieldLabel: '批次ID',
                    name: 'batchID',
                    hidden:true

                },
                {
                    xtype: 'displayfield',
                    fieldLabel: '申请人',
                    name: 'realName'

                },
                {
                    xtype: 'displayfield',
                    fieldLabel: '审议通过率',
                    name: 'xswyhsytgl'

                }

            ]
        }

      ,{
		xtype: 'grid',

		height: 280,
		viewConfig: {
			enableTextSelection: true
		},
        autoHeight:true,
		columnLines: true,
		reference:'xswyhReviewGrid',
        name:'xswyhReviewGrid',
		style:"margin:8px",
        header:false,
        frame: true,

        columns: [{
			//xtype: 'rownumberer',
			text: '班级ID',
			dataIndex: 'classID',
			//width:100,
			hidden: true

		},{
            text: '批次ID',
            dataIndex: 'batchID',
			hidden: true
		},{
            text: '小组成员ID',
            dataIndex: 'pwID',
            hidden:true,
            width:100

        },{
            text: '小组成员',
            width:200,
            dataIndex: 'pwName'

		},{
            text: '报名表实例ID',
            dataIndex: 'appInsID'

        },{
            text: '是否投票',
            width:100,
            dataIndex: 'ifVote'
            //width: 110,

        },{
            text: '审议结果',
            width:100,
            dataIndex: 'ReviewResult',

            renderer:function(v){
                var x=v;
                if(x=="1"){
                    return "通过";
                }
                if (x=="2"){
                    return "未通过";
                }
                if (x=="3"){
                    return "未审议";
                }
            }

        },{
            text: '备注',
            dataIndex: 'remark',
            flex:1
        },{
            header: '退回',
            xtype:'linkcolumn',
            sortable: false,
            flex:1,
            value:'退回',
            label:'退回',
            minWidth: 50,
            handler:'viewCancel'
        },{
            header: '提交',
            xtype:'linkcolumn',
            sortable: false,
            flex:1,
            value:'提交',
            label:'提交',
            minWidth: 50,
            handler:'viewCandidateSubmit'
        }, {
            text:"操作",
            menuDisabled: true,
            sortable: false,
            align:'center',
            minWidth:75,
            xtype: 'actioncolumn',
            items:[
                {iconCls:'edit',tooltip:'编辑考生',handler:'editApplicant24'}
            ]
        }],	//columns-end
		store: {
					type: 'xswyhReviewWindowStore'
			   }

	}],		  

    buttons: [{
		text: '保存',
		iconCls:"save",
		//handler: 'onxswyhReviewMsgSave'
        handler:function(btn){
            var store = btn.findParentByType('window').down('grid[name=xswyhReviewGrid]').store,
                newData = store.getNewRecords(),
                removedData = store.getRemovedRecords(),
                updateData = store.getModifiedRecords(),
                comParas,JSONData={},
                classID = btn.findParentByType('window').child('form').getForm().findField('classID').getValue(),
                batchID = btn.findParentByType('window').child('form').getForm().findField('batchID').getValue();


            if(updateData.length !== 0){
                JSONData.update =[];
                for(var x = updateData.length-1;x>=0;x--){
                    JSONData.update[x] ={};
                    delete updateData[x].data.id;
                    JSONData.update[x].classID = classID;
                    JSONData.update[x].batchID = updateData[x].data.batchID;
                    JSONData.update[x].appInsID = updateData[x].data.appInsID;
                    JSONData.update[x].pwID = updateData[x].data.pwID;

                    JSONData.update[x].ReviewResult = updateData[x].data.ReviewResult;
                    //console.log(updateData[x].data.ReviewResult);
                    //JSONData.update[x].reviewStatus = updateData[x].data.LUQUZT;
                }
            }
            comParas=Ext.JSON.encode(JSONData);
            //表单中的数据
            comParas=Ext.JSON.encode(JSONData);
            //提交参数
            var tzParams = '{"ComID":"TZ_GSM_CANDIDATE_COM","PageID":"TZ_XSWYH_STD","OperateType":"U","comParams":' + comParas + '}';
            Ext.tzSubmit(tzParams,function(responseData){
                var thisValue = responseData.xswyhsytgl||btn.findParentByType('panel').child('form').getForm().findField('xswyhsytgl').getValue();
                btn.findParentByType('panel').child('form').getForm().findField('xswyhsytgl').setValue(thisValue);
                var grid = btn.findParentByType('panel').down("grid[name=xswyhReviewGrid]");
                var store = grid.getStore();
                if(tzParams.indexOf("add")>-1||tzParams.indexOf("delete")>-1||tzParams.indexOf("update")){
                    store.reload();
                }
            },"",true,this);
        }
	},{
        text: '确定',
        iconCls:"ensure",
        //handler: 'onxswyhReviewSure'
        handler:function(btn){
            var store = btn.findParentByType('window').down('grid[name=xswyhReviewGrid]').store,
                newData = store.getNewRecords(),
                removedData = store.getRemovedRecords(),
                updateData = store.getModifiedRecords(),
                comParas,JSONData={},
                classID = btn.findParentByType('window').child('form').getForm().findField('classID').getValue(),
                batchID = btn.findParentByType('window').child('form').getForm().findField('batchID').getValue();


            if(updateData.length !== 0){
                JSONData.update =[];
                for(var x = updateData.length-1;x>=0;x--){
                    JSONData.update[x] ={};
                    delete updateData[x].data.id;
                    JSONData.update[x].classID = classID;
                    JSONData.update[x].batchID = updateData[x].data.batchID;
                    JSONData.update[x].appInsID = updateData[x].data.appInsID;
                    JSONData.update[x].pwID = updateData[x].data.pwID;

                    JSONData.update[x].ReviewResult = updateData[x].data.ReviewResult;
                    console.log(updateData[x].data.ReviewResult);
                    //JSONData.update[x].reviewStatus = updateData[x].data.LUQUZT;
                }
            }
            comParas=Ext.JSON.encode(JSONData);
            //表单中的数据
            comParas=Ext.JSON.encode(JSONData);
            //提交参数
            var tzParams = '{"ComID":"TZ_GSM_CANDIDATE_COM","PageID":"TZ_XSWYH_STD","OperateType":"U","comParams":' + comParas + '}';
            Ext.tzSubmit(tzParams,function(responseData){
                var thisValue = responseData.xswyhsytgl||btn.findParentByType('panel').child('form').getForm().findField('xswyhsytgl').getValue();
                btn.findParentByType('panel').child('form').getForm().findField('xswyhsytgl').setValue(thisValue);
                var grid = btn.findParentByType('window').down("grid[name=xswyhReviewGrid]");
                var store = grid.getStore();
                if(tzParams.indexOf("add")>-1||tzParams.indexOf("delete")>-1||tzParams.indexOf("update")){
                    store.reload();
                }
                btn.findParentByType('panel').close();

                //var interviewMgrPanel=Ext.ComponentQuery.query("panel[reference=candidateStudentGrid]");
                //var store=interviewMgrPanel[0].getStore();
                //var  store11 = interviewMgrPanel[0].getStore();
                 var activeTab = Ext.getCmp('tranzvision-framework-content-panel').getActiveTab();
                 var  store11  = activeTab.down("grid[name=candidateStudentGrid]").getStore();

                var tzStoreParams ='{"classID":"'+classID+'","batchID":"'+batchID+'"}';
                store11.tzStoreParams = tzStoreParams;
                store11.load();
            },"",true,this);
        }
    }, {
		text: '关闭',
		iconCls:"close",
		//handler: 'onteamReviewClose'
        handler:function(btn){
            btn.findParentByType('window').close();
            //this.up('panel').close();
            //this.getView().close();
        }
	}]
});
