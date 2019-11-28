﻿Ext.define('KitchenSink.view.enrollmentManagement.applicationForm.classManagement', {
    extend: 'Ext.grid.Panel',
    requires: [
        'Ext.data.*',
        'Ext.grid.*',
        'Ext.util.*',
        'Ext.toolbar.Paging',
        'Ext.ux.ProgressBarPager',
        'KitchenSink.view.enrollmentManagement.applicationForm.classStore',
        'KitchenSink.view.enrollmentManagement.applicationForm.interviewCheckStore',
        'KitchenSink.view.enrollmentManagement.applicationForm.classController',
        'tranzvision.extension.grid.column.Link'
    ],
    xtype: 'appFormAuditing',
    columnLines: true,
    controller: 'appFormClass',
    name:'appFormClass',
    /*style:"margin:8px",*/
    multiSelect: true,
    title: Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_CLASS_STD.applicationFormAuditing","报名表审核"),
    viewConfig: {
        enableTextSelection: true
    },
    header:false,
    frame: true,
    dockedItems:[
        {
        xtype:"toolbar",
        items:[
            {
                text:Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_CLASS_STD.query","查询"),
                iconCls:"query",
                handler:'queryClass'
            }
        ]},
        {
        xtype:"toolbar",
        dock:"bottom",
        ui:"footer",
        items:['->',
            {
                minWidth:80,
                text:Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_CLASS_STD.close","关闭"),
                iconCls:"close",
                handler: 'onComRegClose'
            }]
        }
    ],
    constructor:function(){
    	this.callParent();
    	var panel = this;
    	panel.columns[4].setVisible(true);
		panel.columns[5].setVisible(true);
		panel.columns[6].setVisible(true);
    	//如果当前登录人为苏州管理员，则隐藏相关列
		var tzParams = '{"ComID":"TZ_BMGL_BMBSH_COM","PageID":"TZ_BMGL_CLASS_STD","OperateType":"getRoleList","comParams":{}}';
        Ext.tzLoad(tzParams,function(respData){
        	var roleList = respData.roleList;
        	if(roleList != null && roleList.length > 0){
        		for(var i = 0; i < roleList.length; i++){
        			var roleID = roleList[i].roleID;
        			var isRole = roleList[i].isRole;
        			//console.log(roleID.lastIndexOf("SZGD_JG_GLY"));
        			if(roleID.lastIndexOf("SZGD_JG_GLY") != -1 && isRole){
        				console.log(111)
        				panel.columns[4].setVisible(false);
        				panel.columns[5].setVisible(false);
        				panel.columns[6].setVisible(false);
        			}
        		}
        	}
        })
    },
    initComponent: function () {
		var store = new KitchenSink.view.enrollmentManagement.applicationForm.classStore(),
			applyStatusStore = new KitchenSink.view.common.store.appTransStore("TZ_BMGL_APPLY_STATUS");
		applyStatusStore.on('load',function(){
			store.load();
			
			
		});
        
        Ext.apply(this, {
            columns: [{
                xtype: 'rownumberer',
                width:50
            }/*,{
                text:Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_CLASS_STD.applyDirectionId","报考方向编号"),
                dataIndex: 'classID',
                minWidth:180,
                flex:1
            }*/,{
                text:Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_CLASS_STD.applyDirection","班级名称"),
                minWidth:180,
                dataIndex:'className',
                flex:1
            },{
                text:Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_CLASS_STD.applyBatch","批次名称"),
                dataIndex: 'batchName',
                width:110
            }/*,{
                text:Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_CLASS_STD.applyChioce","申请志愿"),
                dataIndex: 'className',
                width:110
            }*/,{
                text:Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_CLASS_STD.admissionDate","入学日期"),
                dataIndex: 'admissionDate',
                width:110
            },{
                text:Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_CLASS_STD.applicantingNumber","已报名人数"),
                dataIndex: 'applicantingNumber',
                width:110
            },{
                text:Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_CLASS_STD.submittedNumber","已提交人数"),
                dataIndex: 'submittedNumber',
                width:110
            },{
                text:Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_CLASS_STD.passNumber","初审通过人数"),
                dataIndex: 'passNumber',
                width:110
            }/*,{
                text:Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_CLASS_STD.firstChoiceNumber","第一志愿申请人数"),
                dataIndex: 'firstChoiceNumber',
                width:140
            }*/,{
                xtype:'linkcolumn',
                text:Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_CLASS_STD.viewApplicants","查看报考学生"),
                menuDisabled:true,
                hideable:false,
                items:[{
                    getText:function(v, meta, rec) {
                        return this.text;
                    },
                    handler: "viewApplicants"
                }],
                width:130
            },/*{
                xtype:'linkcolumn',
                text:Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_CLASS_STD.publishResult","报名流程结果公布"),
                width:160,
                menuDisabled:true,
                hideable:false,
                items:[{
                    getText:function(v, meta, rec) {
                        return this.text;
                    },
                    handler: "publishResult"
                }]
            },{
                menuDisabled: true,
                sortable: false,
                width:150,
                text: "材料评审",
                xtype: 'actioncolumn',
                align:'center',
                items:[
                	{iconCls: 'import',tooltip: Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_CLASS_STD.autoScreen","自动初筛"),handler:'automaticScreen'},"-",
                    {iconCls: 'set',tooltip: Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_CLASS_STD.clmspsSet","设置评审规则"),handler:'setMaterialReviewRule'},"-",
                    {iconCls: 'people',tooltip: Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_CLASS_STD.clmspsPeople","查看考生名单"),handler:'viewMaterialStuApplicants'},"-",
                    {iconCls: 'schedule',tooltip: Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_CLASS_STD.clmspsSchedule","评审进度管理"),handler:'clReviewScheduleMg'}
                ]
            },*/{
                menuDisabled: true,
                sortable: false,
                width:150,
                text: "材料评审",
                xtype: 'actioncolumn',
                align:'center',
                items:[
                    //{iconCls: 'import',tooltip: Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_CLASS_STD.autoScreen","自动初筛"),handler:'automaticScreen'},"-",
                    {iconCls: 'set',tooltip: Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_CLASS_STD.clmspsSet","设置评审规则"),handler:'setMaterialReviewRule'},"-",
                    {iconCls: 'people',tooltip: Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_CLASS_STD.clmspsPeople","查看考生名单"),handler:'viewMaterialStuApplicants'},"-",
                    {iconCls: 'schedule',tooltip: Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_CLASS_STD.clmspsSchedule","评审进度管理"),handler:'clReviewScheduleMg'}
                ]
            },{
                menuDisabled: true,
                sortable: false,
                width:150,
                text: "面试评审",
                xtype: 'actioncolumn',
                align:'center',
                items:[
                    {iconCls: 'set',tooltip: Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_CLASS_STD.clmspsSet","设置评审规则"),handler:'setInterviewReviewRule'},"-",
                    {iconCls: 'people',tooltip: Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_CLASS_STD.clmspsPeople","查看考生名单"),handler:'viewInterviewStuApplicants'},"-",
                    {iconCls: 'schedule',tooltip: Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_CLASS_STD.clmspsSchedule","评审进度管理"),handler:'msReviewScheduleMg'},"-",
                    {iconCls: 'view',tooltip: Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_CLASS_STD.interviewCheck","查看面试情况"),handler:'interviewCheck'}
                ]
            },{
                menuDisabled: true,
                sortable: false,
                width:150,
                text: "分数统计",
                xtype: 'actioncolumn',
                align:'center',
                items:[
                	{iconCls: 'import',tooltip: Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_CLASS_STD.autoScreenMS","面试打分"),handler:'automaticScreen'},"-",
                	{iconCls: 'import',tooltip: Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_CLASS_STD.autoScreenM","自动筛选"),handler:'automaticScreenBase'}
                ]
            }],
            store: store,
            bbar: {
                xtype: 'pagingtoolbar',
                pageSize: 10,
                store: store,
                plugins: new Ext.ux.ProgressBarPager()
            }
        });

        this.callParent();
    }
});

