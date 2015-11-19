Ext.define('KitchenSink.view.materialsReview.judgeAccount.judgeAccInfoWin', {
    extend: 'Ext.panel.Panel',
    requires: [
          'KitchenSink.view.materialsReview.judgeAccount.judgeAccController'
    ],
    xtype: 'judgeAccInfo',
	reference: 'judgeAccInfoWindow',
    controller: 'judgeAccController',
//	width: 650,
//    height: 430,
//    minWidth: 600,
//    minHeight: 400,

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
	
	initComponent:function(){
		var jugTypeStore = new KitchenSink.view.common.store.comboxStore({
			recname: 'TZ_JUGTYP_TBL',
			condition:{},
			result:'TZ_JUGTYP_ID,TZ_JUGTYP_NAME'
		});

		var judTypeArr = this.judType;
		var judTpItems = [];
		/*
		for(var i=1; i<judTypeArr.length; i++){
			if (i == 1){
				var tItems = {
					fieldLabel:'评委类型',
					xtype:'checkboxfield',
					boxLabel: judTypeArr[i].judgeTypeName,  
					name: 'judgeType',
					inputValue: judTypeArr[i].judgeTypeId,
					style: {
						marginRight: '20px'
					}
				};	
			}else {
				var tItems = {
					xtype:'checkboxfield',
					boxLabel: judTypeArr[i].judgeTypeName,  
					name: 'judgeType',
					inputValue: judTypeArr[i].judgeTypeId,
					style: {
						marginRight: '20px'
					}
				};	
			}
			judTpItems.push(tItems);
		}
		*/
		//for(var i=0; i<judTypeArr.length; i++){
        for(var i=0; i<2; i++){
			var tItems = {
				xtype:'checkboxfield',
				boxLabel: judTypeArr[i].judgeTypeName,  
				name: 'judgeType',
				inputValue: judTypeArr[i].judgeTypeId
			};	
			judTpItems.push(tItems);
		}
		
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
           //      bodyStyle:'overflow-y:auto;overflow-x:hidden',
				fieldDefaults: {
					msgTarget: 'side',
					labelWidth: 100,
					labelStyle: 'font-weight:bold'
				},
				
				items: [{
					xtype: 'textfield',
					fieldLabel: Ext.tzGetResourse("TZ_JUDGE_ACC_COM.TZ_JUDINFO_STD.accountNo","用户账号"),
					name: 'accountNo',
					allowBlank: false
				},{
					xtype: 'textfield',
					fieldLabel: Ext.tzGetResourse("TZ_JUDGE_ACC_COM.TZ_JUDINFO_STD.password","密码"),
					inputType: 'password',
					name: 'password',
					allowBlank: false
				},{
					xtype: 'textfield',
					fieldLabel: Ext.tzGetResourse("TZ_JUDGE_ACC_COM.TZ_JUDINFO_STD.rePassword","确认密码"),
					inputType: 'password',
					name: 'rePassword',
					allowBlank: false
				},{
					xtype: 'textfield',
					fieldLabel: Ext.tzGetResourse("TZ_JUDGE_ACC_COM.TZ_JUDINFO_STD.judgeName","用户描述"),
					name: 'judgeName'
				},/*{
					layout: {
						type: 'column',
					},	
					items:judTpItems
				},*/{
					xtype: 'checkboxgroup',
					fieldLabel: Ext.tzGetResourse("TZ_JUDGE_ACC_COM.TZ_JUDINFO_STD.judgeType","评委类型"),
					//fieldLabel: '评委类型',
					//id: 'judgeTypeCheckboxGroup',
					itemId:'judgeTypeCheckboxGroup',
					vertical: true,
					items: judTpItems,
					allowBlank: false,
					columns: 1
				},{
					layout: {
						type: 'column'
					},
					items:[{
						columnWidth:.35,
						xtype: 'displayfield',
						fieldLabel: Ext.tzGetResourse("TZ_JUDGE_ACC_COM.TZ_JUDINFO_STD.orgId","所属机构"),
						name: 'orgId'
					},{
						columnWidth:.65,
						xtype: 'displayfield',
						hideLabel: true,
						name: 'orgDesc'
					}]
				},{
					xtype: 'checkbox',
					fieldLabel: Ext.tzGetResourse("TZ_JUDGE_ACC_COM.TZ_JUDINFO_STD.clockFlag","锁定用户"),
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
