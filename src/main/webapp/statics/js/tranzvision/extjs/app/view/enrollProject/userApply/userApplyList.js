Ext.define('KitchenSink.view.enrollProject.userApply.userApplyList', {
    extend: 'Ext.grid.Panel',
	requires: [
        'Ext.data.*',
        'Ext.grid.*',
        'Ext.util.*',
        'Ext.toolbar.Paging',
        'Ext.ux.ProgressBarPager',
        'KitchenSink.view.enrollProject.userApply.userApplyController',
        'KitchenSink.view.enrollProject.userApply.userApplyStore',
        'KitchenSink.view.enrollProject.userApply.userApplyModel',
        'KitchenSink.view.enrollProject.userApply.AdmissionAndPayInfoWindow'
    ],
    xtype: 'userApplyGL',//不能变
    controller: 'userApplyController',
    listeners: {
        afterrender: function(grid){
            grid.getStore().addListener("refresh",
                function (store) {
                    grid.getView().getSelectionModel().deselectAll();
                }, this);

            var normalView = grid.getView().normalView;
            var wheelIncrement=88; //鼠标滚动步长（像素）

            normalView&&grid.getView().el.on('mousewheel',function(e){
                var delta=e.getWheelDelta();
                var scrollTop=normalView.el.getScrollTop()+(delta>0?-wheelIncrement:wheelIncrement);
                normalView.el.setScrollTop(scrollTop);
            });
        }
    },
    columnLines: true,
    selModel: {
        type: 'checkboxmodel'
    },
	style:"margin:8px",
    multiSelect: true,
    title: '报名表查询',
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
			{text:"查询",tooltip:"查询数据",iconCls: "query",handler:'queryUser'},"->",
			/*{text:"查看",tooltip:"查看数据",iconCls: 'view',handler:'viewUserByBtn'},'->',*/
			{
				xtype:'splitbutton',
				text:'更多操作',
				iconCls:  'list',
				glyph: 61,
				menu:[/*{
					text:'重置密码',
					handler:'resetPassword'
				},{
					text:'关闭账号',
					handler:'deleteUser'
				},{
					text:'加入黑名单',
					handler:'addHmd'
				},/!*{
					text:'邮件发送历史',
					iconCls: 'mail',
					handler:'viewMailHistory'	
				},*!/{
					text:'选中申请人另存为听众',
					handler:'saveAsStaAud'	
				},{
					text:'搜索结果另存为听众',
					handler:'saveAsDynAud'	
				}/!*,{
					text:'另存为动态听众',
					handler:'saveAsDynAud'	
				}*!/,{
					text:'添加到已有听众',
					handler:'saveToStaAud'	
				},*/
				{
					text:Ext.tzGetResourse("TZ_UM_USERAPPLY_COM.TZ_UM_USERMG_STD.exportApplicantsInfo","导出选中人员信息到Excel"),
					name:'exportExcel',
                    iconCls:"excel",
					handler:'exportExcelOrDownload'
				},{
					text:Ext.tzGetResourse("TZ_UM_USERAPPLY_COM.TZ_UM_USERMG_STD.exportSearchResultInfo","导出搜索结果人员信息到Excel"),
					name:'exportSearchResultExcel',
                    iconCls:"excel",
					handler:'exportSearchResultExcel'
				},
				{
					text:Ext.tzGetResourse("TZ_UM_USERAPPLY_COM.TZ_UM_USERMG_STD.downloadExcel","查看导出结果并下载"),
					name:'downloadExcel',
                    iconCls:"download",
					handler:'exportExcelOrDownload'
				},{
					text:Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.sendEmailSelectedPerson","给选中人发送邮件"),
					glyph:'xf1d8@FontAwesome',
					handler:'sendEmlSelPers'
                },{
					text:Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.sendSnselectedPerson","给选中人发送短信"),
					glyph:'xf003@FontAwesome',
					handler:'sendSmsSelPers'
                }/*,{
					text:Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.exportApplyInfoExcel","导出未报名人信息"),
					iconCls:'export',
					handler:'exportApplyInfoExcel'
                }*/]
			}
		]
	}],
    initComponent: function () {    
    	var store = new KitchenSink.view.enrollProject.userApply.userApplyStore();
    	//性别
    	//var sexStore = new KitchenSink.view.common.store.appTransStore("TZ_GENDER");
    	//账户激活状态
    	//var jihuoStore = new KitchenSink.view.common.store.appTransStore("TZ_JIHUO_ZT");
    	//锁定状态
    	//var acctLockStore = new KitchenSink.view.common.store.appTransStore("ACCTLOCK");
    	//黑名单用户
    	//var isYnStore = new KitchenSink.view.common.store.appTransStore("TZ_SF_SALE");
    	
    	var appStateStore = new KitchenSink.view.common.store.appTransStore("TZ_APP_FORM_STA");
        //var mszgStore = new KitchenSink.view.common.store.appTransStore("TZ_IS_MSZG");
    	
        Ext.apply(this, {
            columns: [{
                xtype: 'actioncolumn',
                text: '操作',
                menuDisabled: true,
                menuText: '操作',
                sortable: false,
                "with":50,
                align: 'center',
                locked: true,
                items:[
                    {text: '查看',iconCls: 'edit',tooltip: '查看',handler:'viewUserByRow'},
                    {text: '查看报名表',iconCls: 'preview',tooltip: '查看报名表',handler:'viewApplicationForm1'},
                    /*{text: '查看',iconCls: 'view',tooltip: '查看缴费计划',handler:'viewPlanDetails'},*/
                    {text: '打印报名表',iconCls: 'print',tooltip: '打印报名表',handler:'printAppForm'}
                ]
            },{
                text: '用户ID',
                dataIndex: 'OPRID',
				hidden:true
            },{
                text: '姓名',
                sortable: true,
                dataIndex: 'TZ_REALNAME',
                locked: true,
                width: 90
            },{
                text: '面试申请号',
                sortable: true,
                dataIndex: 'TZ_MSH_ID',
                width: 100
            },{
                text: '报考信息',
                sortable: true,
                dataIndex: 'TZ_CLASS_NAME',
                width: 200
            },{
                text:'批次名称',
                sortable: true,
                dataIndex: 'TZ_BATCH_NAME',
                width: 180
            },{
                text:'报名表填写百分比',
                sortable: true,
                dataIndex: 'TZ_FILL_PROPORTION',
                width: 180
            },{
                text:'已提交推荐信的数量',
                sortable: true,
                dataIndex: 'TZ_TJX_NUM',
                width: 150
            },{
                text: '提交状态',
                sortable: true,
                dataIndex: 'TZ_APP_FORM_STA',
                width: 80,
                renderer:function(v,metadata,record){
                    if(v!=null && v!=""){
                        for(var i=0 ;i<appStateStore.data.items.length;i++){
                            if(appStateStore.data.items[i].data.TValue==v){
                                return appStateStore.data.items[i].data.TLDesc;
                            }
                        }
                    }else{
                        return "";
                    }
                }
            },{
                text:'最后修改时间',
                sortable: true,
                dataIndex: 'TZ_APP_SUB_DTTM',
                width: 160
            },/*{
                text:'缴费状态',
                sortable: true,
                dataIndex: 'TZ_JF_PLAN',
                width: 90
            },{
                text:'公司',
                sortable: true,
                dataIndex: 'TZ_COMPANY_NAME',
                width: 200
            },{
                text:'职位',
                sortable: true,
                dataIndex: 'TZ_COMMENT1',
                width: 90
            },{
                text:'主要负责人',
                sortable: true,
                dataIndex: 'TZ_MAIN_REALNAME',
                width: 90
            },*/{
                text: '手机',
                sortable: true,
                dataIndex: 'TZ_MOBILE',
                width: 120
            },{
                text: '邮箱',
                sortable: true,
                dataIndex: 'TZ_EMAIL',
                width: 180
            },{
                text:'面试资格',
                sortable: true,
                dataIndex: 'TZ_MSZG',
                width: 90,
                /*renderer:function(v){
                    if(v){
                        var index = mszgStore.find('TValue',v,0,false,true,true);
                        if(index>-1){
                            return mszgStore.getAt(index).get("TSDesc");
                        }else{
                            index = mszgStore.find('TSDesc',v,0,false,true,true);
                            if(index>-1){
                                return mszgStore.getAt(index).get("TSDesc");
                            }
                        }
                        return "";
                    }
                }*/
            },/*{
                text:'面试笔试成绩',
                sortable: true,
                dataIndex: 'TZ_MSBS_CJ',
                width: 90
            },*/{
                text: '面试结果',
                sortable: true,
                dataIndex: 'TZ_MSJG',
                width: 130
            },/*{
                text:'拟录取结果',
                sortable: true,
                dataIndex: 'TZ_LQ_RESULT',
                width: 90
            },*//*{
                text: '身份证号',
                sortable: true,
                dataIndex: 'NATIONAL_ID',
                width: 160,
                hidden:true
            },{
                text: '性别',
                sortable: true,
                dataIndex: 'TZ_GENDER',
                width: 55,
                renderer:function(value,metadata,record){
                    if(value == null || value==""){
                        return "";
                    }
                    if (value =='M') {
                        return "男";
                    } else {
                        return "女";
                    }
                },
                hidden:true
            },{
                text:'主要负责人ID',
                hidden: true,
                dataIndex: 'MAIN_OPRID',
                width: 90
            },{
                text:'班级id',
                hidden: true,
                dataIndex: 'TZ_CLASS_ID',
                width: 90
            },{
                text:'OPRID',
                hidden: true,
                dataIndex: 'OPRID',
                width: 90
            },*/{
                text: '报名表编号',
                sortable: true,
                dataIndex: 'TZ_APP_INS_ID',
                hidden:true,
                width: 75
            },/*{
                text: '填写比例',
                sortable: true,
                dataIndex: 'fillProportion',
                width: 100
            },{
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

    			}
            },{
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
                }
            },*/
            ],
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
