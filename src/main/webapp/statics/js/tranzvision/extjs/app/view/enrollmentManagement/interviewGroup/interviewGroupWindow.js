Ext.define('KitchenSink.view.enrollmentManagement.interviewGroup.interviewGroupWindow', {
    extend: 'Ext.window.Window',
    xtype: 'zldbWindow0',
	controller: 'appFormInterview',
	requires: [
	    'Ext.data.*',
        'Ext.grid.*',
        'Ext.util.*',
        'Ext.toolbar.Paging',
        'Ext.ux.ProgressBarPager',
        'KitchenSink.view.enrollmentManagement.applicationForm.dynamicInfo.dynamicAttributeForm',
        'KitchenSink.view.enrollmentManagement.interviewGroup.intervieweeGroupStore',
        'KitchenSink.view.enrollmentManagement.interviewGroup.interviewGroupStore',
        'KitchenSink.view.enrollmentManagement.interviewGroup.interviewController'
	],
	title:'评委组及面试组选择',
    modal:true,//背景遮罩
    header:'面试组',
    autoWidth: true, 
    width:300,
    height:400,
    autoHeight: false, 
    autoscroll:true,
    resizable:false,
    bodyStyle:'1px 2px 3px 4px',
    actType: 'update',//默认更新
    x:400,
    y:30,
    
    initComponent: function(){
    	var me = this;
		//评委组store
		var intervieweeGroupStore = new KitchenSink.view.enrollmentManagement.interviewGroup.intervieweeGroupStore();
		
		var data=[
				{"tz_group_id":1,"tz_group_name":"1组","interviewers":0},
				{"tz_group_id":2,"tz_group_name":"2组","interviewers":0},
				{"tz_group_id":3,"tz_group_name":"3组","interviewers":0},
				{"tz_group_id":4,"tz_group_name":"4组","interviewers":0},
				{"tz_group_id":5,"tz_group_name":"5组","interviewers":0},
				{"tz_group_id":6,"tz_group_name":"6组","interviewers":0},
				{"tz_group_id":7,"tz_group_name":"7组","interviewers":0},
				{"tz_group_id":8,"tz_group_name":"8组","interviewers":0},
				{"tz_group_id":9,"tz_group_name":"9组","interviewers":0},
				{"tz_group_id":10,"tz_group_name":"10组","interviewers":0},
				{"tz_group_id":11,"tz_group_name":"11组","interviewers":0},
				{"tz_group_id":12,"tz_group_name":"12组","interviewers":0},
				{"tz_group_id":13,"tz_group_name":"13组","interviewers":0},
				{"tz_group_id":14,"tz_group_name":"14组","interviewers":0},
				{"tz_group_id":15,"tz_group_name":"15组","interviewers":0},
				{"tz_group_id":16,"tz_group_name":"16组","interviewers":0},
				{"tz_group_id":17,"tz_group_name":"17组","interviewers":0},
				{"tz_group_id":18,"tz_group_name":"18组","interviewers":0},
				{"tz_group_id":19,"tz_group_name":"19组","interviewers":0},
				{"tz_group_id":20,"tz_group_name":"20组","interviewers":0}
		      ];                                                          
		
		 var store = Ext.create('Ext.data.Store', {
				    storeId: 'store',
				    fields:[ 'tz_group_id', 'tz_group_name', 'interviewers'],
				    data: data
			});
		 
		 store.load();//要执行一次，以对数据初始化，很重要

		 Ext.apply(this,{
        	
        	 items:[
        		 {
                     xtype: 'textarea',
                     fieldLabel: 'classID',
                     labelSeparator: ':',
                     value:'1',
                     editable:false,
                     hidden:true
                 },
                 {
                     xtype: 'textarea',
                     fieldLabel: 'tz_app_ins_id',
                     labelSeparator: ':',
                     value:'',
                     editable:false,
                     hidden:true
                 },
        		 {
                     xtype: 'combobox',
                     fieldLabel: Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_AUDIT_STD.auditStates","评委组"),
                     name: 'jugGroupName',
                     editable:false,
                     emptyText:Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_AUDIT_STD.pleaseSelect","请选择..."),
                     valueField: 'jugGroupId',
                     displayField: 'jugGroupName',
                     store: intervieweeGroupStore,
                     queryMode: 'local',
                     triggerAction: 'all',
                     layout: 'column',
                     listeners: {	// select监听函数  
                         select : function(combo, record, index){
	                        	var myCombo = this;
	                        	var v = this.getValue();
                        		var tzParams = '{"ComID":"TZ_MSXCFZ_COM","PageID":"TZ_MSGL_MSFZ_STD","OperateType":"queryGroups","comParams":{"jugGroupId":"'+v+'"}}';
                        		// 加载数据
                 				Ext.tzLoad(tzParams,function(responseData) {
                 					//设置安排人数之前，先将所有安排的人数归零
                 					for(var j = 0; j < store.getCount(); j++){
                 						var rec = store.getAt(j);
                 						rec.set('interviewers',0);
                 					}
                 					//设置安排的人数
                 					for(var i=0;i<responseData.total;i++){
                 						var rec = store.getAt(i);
                 						var interviewers = responseData.root[i].interviewers;
                 						rec.set('interviewers',interviewers);
                 						//store.load();
                 					}
                 				});
                        	 }   
                     } 
                 },
                 
                 {
                     xtype: 'grid',
                     autoHeight: false,
                     //stripeRows : true, //是否有斑马线（好看）
                     border:true,
                     height:300,
                     bodyStyle: 'overflow-x:hidden; overflow-y:hidden',
                     multiSelect: true,
                     emptyMsg: '没有记录',
                     store: store,
                     selModel: {
                         type: 'checkboxmodel',
                         mode:"SINGLE"
                     },

                     columns: [
	                         { 
	                        	 dataIndex: 'tz_group_id', 
	                        	 hidden:true
	                         },{  
	                             text : "面试组",  
	                             dataIndex : 'tz_group_name',  
	                             width : 120, 
	                             flex:1,
	                             align : 'center'  
	                         },{  
	                             text : "已安排人数",  
	                             dataIndex : 'interviewers',  
	                             width : 100,
	                             flex:1,
	                             align : 'center'  
	                         }
                     ],
                     dockedItems: [
                         {
                         xtype:"toolbar",
                         dock:"bottom",
                         ui:"footer",
                         items:['->',
                             {minWidth:80,text:Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.esure","确定"),name:'update',handler:'update'}
                             ,{minWidth:80,text:Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.closes","取消"),handler:'onStuInfoClose'}]
                     }],
                     /*bbar: {
                         xtype: 'pagingtoolbar',
                         pageSize: 5,
                         listeners: {
                             afterrender: function (pbar) {
                                 var grid = pbar.findParentByType("grid");
                                 pbar.setStore(grid.store);
                             }
                         },
                         plugins: new Ext.ux.ProgressBarPager(),
                         emptyMsg:'没有数据显示'
                     }*/
                 },
                 ]
        });
        this.callParent();
    }
});