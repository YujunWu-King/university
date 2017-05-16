Ext.define('KitchenSink.view.enrollmentManagement.interviewReview.interviewReviewAppJudgeWindow', {
    extend: 'Ext.window.Window',
    xtype: 'interviewReviewAppJug',
    controller: 'interviewReview',
    requires: [
        'Ext.data.*',
        'Ext.grid.*',
        'Ext.util.*',
        'KitchenSink.view.enrollmentManagement.interviewReview.interviewReviewAppJudgeStore'
    ],
    title: '查看考生评审得分',
    width: 1000,
    height: 400,
    modal:true,
    layout: {
        type: 'fit'
    },
    scoreFields:'',
    buttons: [{
        text: '关闭', iconCls: 'close', handler: function (btn) {
            btn.findParentByType('window').close();
        }
    }],
    constructor:function(fields){    	
        this.scoreFields = fields;        
        this.callParent();
    },
    initComponent:function() {
        var columns = [
            {
                dataIndex: 'classID',
                hidden: true
            }, {
                dataIndex: 'batchID',
                hidden: true
            }, {
                text: "评委姓名",
                dataIndex: 'judgeRealName',
                align: 'center',
                minWidth: 100                
            }, {
                text: "报名表编号",
                dataIndex: 'appInsID',
                align: 'center',
                minWidth: 100
            }
            , {
                text: "考生姓名",
                dataIndex: 'studentRealName',
                align: 'center',
                minWidth: 100
            }
        ];
        
        for(var x =0;x<this.scoreFields.length;x++){
        	var minWidth = 100;
        	if(x>=10){
        		//评语列
        		minWidth = 200;
        	}
            columns.push({
                text:this.scoreFields[x].name,
                dataIndex:x,
                align:'center',
                minWidth:minWidth
            })
        }
        Ext.apply(this, {
            items: [{
                xtype: 'grid',
                autoHeight: true,
                columnLines: true,
                frame: true,
                style: 'border:0',
                columns:columns ,

            }]
        });
        this.callParent();
    }
});