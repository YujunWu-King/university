﻿Ext.define('KitchenSink.view.enrollProject.userMg.userMgList', {
    extend: 'Ext.grid.Panel',
	requires: [
        'Ext.data.*',
        'Ext.grid.*',
        'Ext.util.*',
        'Ext.toolbar.Paging',
        'Ext.ux.ProgressBarPager',
        'KitchenSink.view.enrollProject.userMg.userMgController',
        'KitchenSink.view.enrollProject.userMg.userMgStore',
        'KitchenSink.view.enrollProject.userMg.userMgModel'
    ],
    xtype: 'userMgGL',//不能变
    controller: 'userMgController',
    columnLines: true,
    selModel: {
        type: 'checkboxmodel'
    },
	style:"margin:8px",
    multiSelect: true,
    title: '申请用户管理',
    viewConfig: {
        enableTextSelection: true
    },
	header:false,
	frame: true,
    dockedItems:[{
		xtype:"toolbar",
		dock:"bottom",
		ui:"footer",
		items:['->',{minWidth:80,text:"关闭",iconCls:"close",handler:"onListClose"}]
		},{
		xtype:"toolbar",
		items:[
			{text:"查询",tooltip:"查询数据",iconCls: "query",handler:'queryUser'},"-",
			{text:"查看",tooltip:"查看数据",iconCls: 'view',handler:'viewUserByBtn'},'->',
			{
				xtype:'splitbutton',
				text:'更多操作',
				iconCls:  'list',
				glyph: 61,
				menu:[{
					text:'重置密码',
					handler:'resetPassword'
				},{
					text:'关闭账号',
					handler:'deleteUser'
				},{
					text:'加入黑名单',
					handler:'addHmd'
				},/*{
					text:'邮件发送历史',
					iconCls: 'mail',
					handler:'viewMailHistory'	
				},*/{
					text:'选中申请人另存为听众',
					handler:'saveAsStaAud'	
				},{
					text:'搜索结果另存为听众',
					handler:'saveAsDynAud'	
				}/*,{
					text:'另存为动态听众',
					handler:'saveAsDynAud'	
				}*/,{
					text:'添加到已有听众',
					handler:'saveToStaAud'	
				},
				{
					text:Ext.tzGetResourse("TZ_UM_USERMG_COM.TZ_UM_USERMG_STD.exportApplicantsInfo","导出选中人员信息到Excel"),
					name:'exportExcel',
					handler:'exportExcelOrDownload'
				},{
					text:Ext.tzGetResourse("TZ_UM_USERMG_COM.TZ_UM_USERMG_STD.exportSearchResultInfo","导出搜索结果人员信息到Excel"),
					name:'exportSearchResultExcel',
					handler:'exportSearchResultExcel'
				},
				{
					text:Ext.tzGetResourse("TZ_UM_USERMG_COM.TZ_UM_USERMG_STD.downloadExcel","查看导出结果并下载"),
					name:'downloadExcel',
					handler:'exportExcelOrDownload'
				},{
					text:Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.sendEmailSelectedPerson","给选中人发送邮件"),
					glyph:'xf1d8@FontAwesome',
					handler:'sendEmlSelPers'
                },{
                    text:Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.viewSmsHistory","查看短信发送历史"),
                    glyph:'xf1da@FontAwesome',
                    handler:'viewSmsHistory'
                },{
					text:Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.sendSnselectedPerson","给选中人发送短信"),
					glyph:'xf003@FontAwesome',
					handler:'sendSmsSelPers'
                },{
					text:Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.sendEmlSelPersAll","给搜索结果发送邮件"),
					glyph:'xf1d8@FontAwesome',
					handler:'sendEmlSelPersAll'
                },{
					text:Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.sendSmsSelPersAll","给搜索结果发送短信"),
					glyph:'xf003@FontAwesome',
					handler:'sendSmsSelPersAll'
                },{
					text:Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.exportApplyInfoExcel","导出未报名人信息"),
					iconCls:'export',
					handler:'exportApplyInfoExcel'
                }]
			}
		]
	}],
    initComponent: function () {    
    	var store = new KitchenSink.view.enrollProject.userMg.userMgStore();
    	//性别
    	//var sexStore = new KitchenSink.view.common.store.appTransStore("TZ_GENDER");
    	//console.log(sexStore);
    	//账户激活状态
    	var jihuoStore = new KitchenSink.view.common.store.appTransStore("TZ_JIHUO_ZT");
    	//锁定状态
    	//var acctLockStore = new KitchenSink.view.common.store.appTransStore("ACCTLOCK");
    	//黑名单用户
    	//var isYnStore = new KitchenSink.view.common.store.appTransStore("TZ_SF_SALE");
    	
        Ext.apply(this, {
            columns: [{
				text: '机构ID',
				dataIndex: 'TZ_JG_ID',
				width: 20,
				hidden:true
			},{
                text: '用户ID',
                dataIndex: 'OPRID',
				width: 20,
				hidden:true
            },{
                text: '姓名',
                sortable: true,
                dataIndex: 'TZ_REALNAME',
                width: 75
            },/*{
                text: '班级号',
                sortable: true,
                dataIndex: 'classID',
                hidden:true,
                width: 75
            },{
                text: '报名表编号',
                sortable: true,
                dataIndex: 'appInsID',
                hidden:true,
                width: 75
            },*/{
                text: '面试申请号',
                sortable: true,
                dataIndex: 'TZ_MSH_ID',
                width: 95,
                flex:1
            },/*{
                text: '性别',
               sortable: true,
				dataIndex: 'userSex',
                width: 55,
            	hidden:true,
                renderer:function(value,metadata,record){
    				if(value == null || value==""){
    					return "";	
    				}
    				if (value =='M') {
    					return "男";
    				} else {
    					return "女";
    				}
                }
            },*/{
                text: '手机',
                sortable: true,
                dataIndex: 'TZ_MOBILE',
                width: 100
            },{
                text: '电子邮箱',
                sortable: true,
                dataIndex: 'TZ_EMAIL',
                width: 180
            },/*{
            	text:'批次名称',
            	sortable: true,
                dataIndex: 'bitch',
                width: 180
            },
            {
                text: '报考信息',
                sortable: true,
                dataIndex: 'applyInfo',
                flex:1,
                width: 200
            },{
                text: '填写比例',
                sortable: true,
                dataIndex: 'fillProportion',
        //        dataIndex:'nationId',
                width: 100
            },*/{
                text: '账号激活状态',
                sortable: true,
                dataIndex: 'TZ_JIHUO_ZT',
                width: 100,
                renderer:function(value,metadata,record){
    				if(value == null || value==""){
    					return "";	
    				}
    				if (value =='Y') {
    					return "已激活";
    				} else {
    					return "未激活";
    				}
    				//var index = jihuoStore.find('TValue',value);   
    				//if(index!=-1){   
    				//	   return jihuoStore.getAt(index).data.TSDesc;   
    				//}   
    				//return value;     				 
    			}
            },{
                text: '创建日期时间',
                sortable: true,
                dataIndex: 'TZ_ZHCE_DT',
                width: 130
            },/*{
                text: '锁定状态',
                sortable: true,
                dataIndex: 'acctlock',
                width: 80,
            	hidden:true,
                renderer:function(value,metadata,record){
    	   		if(value == null || value==""){
    	   				return "";	
    	   			}
    				if (value =='0') {
    					return "未锁定";
    				} else {
    					return "已锁定";
    				}
    				//var index = acctLockStore.find('TValue',value);   
    				//if(index!=-1){   
    				//	   return acctLockStore.getAt(index).data.TSDesc;   
    				//}   
    				//return value;     				 
    			}
            },{
                text: '提交状态',
                sortable: true,
                dataIndex: 'tj_zt',
                width: 80,
                renderer:function(value,metadata,record){
	    	   		if(value == null || value==""){
	    	   				return "";	
	    	   			}
    				if (value =='S') {
    					return "保存";
    				} 
    				if(value=='U') {
    					return "提交";
    				}
    			}
            },{
                text: '面试结果',
                sortable: true,
                dataIndex: 'ms_result',
                width: 130
            },{
                text: '备注',
                sortable: true,
                dataIndex: 'remark',
                width: 130
            },{
                text: '黑名单',
                sortable: true,
                dataIndex: 'hmdUser',
                width: 60,
            	hidden:true,
                renderer:function(value,metadata,record){
    				if(value == null || value==""){
    					return "否";	
    				}
    				if (value =='N') {
    					return "否";
    				} else {
    					return "是";
    				}
    				//var index = isYnStore.find('TValue',value);   
    				//if(index!=-1){   
    				//	   return isYnStore.getAt(index).data.TSDesc;   
    				//}   
    	//			//return value;     				 
                }
            },*/
            {
			   xtype: 'actioncolumn',
			   text: '操作',	
               menuDisabled: true,
			   menuText: '操作',
               sortable: false,
			   width:50,
			   align: 'center',
			   			 items:[
			   			  {text: '查看',iconCls: 'view',tooltip: '查看',handler:'viewUserByRow'},
			   			  {text: '查看报名表',iconCls: 'preview',tooltip: '查看报名表',handler:'viewApplicationForm1'}
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
