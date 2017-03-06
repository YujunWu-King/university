Ext.define('KitchenSink.view.materialsReview.materialsReview.materialsReviewApplicants', {
    extend: 'Ext.panel.Panel',
    xtype: 'materialsReviewApplicants',
    controller: 'materialsReview',
    requires:[
              'Ext.data.*',
              'Ext.grid.*',
              'Ext.util.*',
              'Ext.toolbar.Paging',
              'Ext.ux.ProgressBarPager',
              'Ext.grid.filters.Filters',
              'KitchenSink.AdvancedVType',
              'tranzvision.extension.grid.column.Link',
              'KitchenSink.view.materialsReview.materialsReview.materialsReviewApplicantsStore',
              'KitchenSink.view.enrollmentManagement.applicationForm.dynamicInfo.dynamicColumn'],
    title: '材料评审考生名单',
    classID:'',
    batchID:'',
    bodyStyle:'overflow-y:auto;overflow-x:hidden',
	 constructor: function (obj){
        this.classID=obj.classID;
        this.batchID=obj.batchID;
        this.callParent();
    },
    initComponent:function(){
		var me = this;
        var classID=me.classID;
		var batchID=me.batchID;
		
        var judgeGroupStore = new KitchenSink.view.common.store.comboxStore({
            recname:'TZ_PWZDY_T',
            condition:{
                TZ_JUGTYP_STAT:{
                    value:'Y',
                    operator:'01',
                    type:'01'
                }},
            result:'TZ_PWZBH,TZ_PWZMS'
        });


		var columns=[
		    {
		        text: "考生姓名",
		        dataIndex: 'realName',
		        width:100,
		        filter: {
		            type: 'string',
		            itemDefaults: {
		                emptyText: 'Search for...'
		            }
		        }
		    },{
		        text: "面试申请号",
		        dataIndex: 'mshID',
		        width:130,
		        filter: {
		            type: 'string',
		            itemDefaults: {
		                emptyText: 'Search for...'
		            }
		        }
		    },{
		        text: "报名表编号",
		        dataIndex: 'appInsID',
		        width:130,
		        filter: {
		            type: 'string',
		            itemDefaults: {
		                emptyText: 'Search for...'
		            }
		        }
		    },{
		        text: "性别",
		        dataIndex: 'gender',
		        width:70,
		        filter: {
		            type: 'list'
		        }
		    },{
		        text: "评委",
		        dataIndex: 'judgeList',
		        flex:1,
		        renderer:function(v) {
		            if (v) {
		                return '<a class="tz_materialsReview_appJudge" href = "javaScript:void(0)" >' + v + '</a>';
		            } else {
		                return "";
		            }
		        },
		        listeners:{
		            click:'viewAppJudge'
		        }
		    },{
				text:'评委总分',
				dataIndex:'judgeTotalScore',
				width:60
			},{
		        text: "评审状态",
		        dataIndex: 'reviewStatus',
		        width:100,
		        filter: {
		            type: 'list'
		        }
		    },{
		        text: "面试资格",
		        dataIndex: 'interviewQualification',
		        width:100,
		        filter: {
		            type: 'list'
		        }
		    },{
		        menuDisabled: true,
		        text: "操作",
		        sortable: false,
		        width:80,
		        xtype: 'actioncolumn',
		        items:[
		            {iconCls:'edit',tooltip:'编辑考生信息',handler:'editCurrentApplicant'},
		            {iconCls: 'people',tooltip: '为考生指定评委',handler:'setAppJudge'},
		            {iconCls: 'remove',tooltip: '移除考生',  handler: function(view, rowIndex){
		                if( view.findParentByType("grid").findParentByType("form").getForm().findField('status').getValue()=='进行中')
		                {Ext.Msg.alert('提示','当前评审状态为进行中，不可移除考生');
		                    return ;}
		                Ext.MessageBox.confirm('确认', '您确定要删除所选记录吗?', function(btnId){
		                    if(btnId == 'yes'){
		                        var store = view.findParentByType("grid").store;
		                        store.removeAt(rowIndex);
		//                                                if (store.getAt(rowIndex).data.judgeList!=0)
		//                                                {Ext.Msg.alert('提示','该考生已被指定评委，不可删除')
		//                                                return;}else{store.removeAt(rowIndex);}
		
		                    }
		                },"",true,this);
		            } }
		        ]
		    }
		]

        Ext.define('filter', {
            extend: 'Ext.grid.filters.Filters'
        });

        Ext.apply(this,{
        	items: [{
                xtype: 'form',
                reference: 'materialsReviewApplicantsForm',
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

                items: [
                    {
                        xtype: 'textfield',
                        name: 'classID',
                        hidden:true
                    },
                    {
                        xtype: 'textfield',
                        name: 'batchID',
                        hidden:true
                    },{
                        xtype: 'textfield',
                        fieldLabel: "报考班级",
                        name: 'className',
                        fieldStyle:'background:#F4F4F4',
                        readOnly:true
                    }, {
                        xtype: 'textfield',
                        fieldLabel: "批次",
                        name: 'batchName',
                        fieldStyle:'background:#F4F4F4',
                        readOnly:true
                    }, {
                        xtype: 'numberfield',
                        fieldLabel: "报考考生数量",
                        name: 'applicantsNumber',
                        fieldStyle:'background:#F4F4F4',
                        readOnly:true
                    }, {
                        xtype: 'numberfield',
                        fieldLabel: "材料评审考生",
                        name: 'materialsReviewApplicantsNumber',
                        fieldStyle:'background:#F4F4F4',
                        readOnly:true
                    },{
                        xtype: 'textfield',
                        name: 'status',
                        hidden:true
                    }
                ]
            },
            {
                xtype: 'grid',
                title:'考生名单',
                minHeight: 260,
                name:'materialsReviewApplicantsGrid',
                reference:'materialsReviewApplicantsGrid',
                columnLines: true,
                autoHeight: true,
                frame:true,
                selModel: {
                    selType: 'checkboxmodel'
                },
                dockedItems:[{
                    xtype:"toolbar",
                    items:[
                        {text:"查询",tooltip:"查询数据",iconCls:"query",handler:'queryApplicants'},"-",
                        {text:"新增",tooltip:"添加考生参与本批次材料评审",iconCls:"add",handler:"addApplicants"},"-",
                        {text:"编辑",tooltip: "编辑选中考生", iconCls:"view", handler: 'editApplicants'},"-",
                        {text:"删除",tooltip:"从列表中移除选中的考生",iconCls:"remove",handler:"removeApplicants"},"-",
                        {text:"指定评委",tooltip:"批量指定评委",iconCls: 'people',handler:'setJudgeForSomeApps'},"->",
                        {
                            xtype:'splitbutton',
                            text:'更多操作',
                            iconCls:  'list',
                            glyph: 61,
                            menu:[
                                  	{text:"设置选中考生面试资格",tooltip:"设置选中考生面试资格",handler:"setOwnQuary"},
                                  	{text:"导出选中考生评议数据",tooltip:"导出选中考生评议数据",handler:"exportReviewData"}
                                ]
                        }]}],
                plugins: [{
                	ptype: 'cellediting'
                },
                {
                	ptype: 'gridfilters'
                }],
                store: {
                	type: 'materialsReviewApplicants'
                },
                columns: columns,
                bbar: {
                	xtype: 'pagingtoolbar',
                	pageSize: 5,
                	reference: 'materialsComToolBar',
                	listeners: {
                		afterrender: function(pbar) {
                			var grid = pbar.findParentByType("grid");
                			pbar.setStore(grid.store)
                		}
                	},
                	plugins: new Ext.ux.ProgressBarPager()
                }
            }]
        })
        this.callParent();
    },
    buttons:[
             {text:'保存',iconCls:"save",handler:'onApplicantsSave'},
             {text:'确定',iconCls:"ensure",handler:'onApplicantsEnsure'},
             {text:'关闭',iconCls:"close",handler:'onApplicantsClose'}
             ]
});
