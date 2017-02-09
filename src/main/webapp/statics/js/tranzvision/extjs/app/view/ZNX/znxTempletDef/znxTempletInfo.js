
Ext.define('KitchenSink.view.ZNX.znxTempletDef.znxTempletInfo', {
    extend: 'Ext.panel.Panel',
    xtype: 'znxTempletInfo', 
	controller: 'znxTempletInfoMth',
	requires: [
	    'Ext.data.*',
        'Ext.grid.*',
        'Ext.util.*',
		'Ext.grid.plugin.Clipboard',
        'Ext.toolbar.Paging',
        'Ext.ux.ProgressBarPager',
		//'Ext.ux.Ueditor',
		'KitchenSink.view.ZNX.znxTempletDef.znxTempletInfoMth',
		'KitchenSink.view.ZNX.znxTempletDef.znxTempletItemModel',
		'KitchenSink.view.ZNX.znxTempletDef.znxTempletItemStore',
	    'tranzvision.extension.grid.column.Link'
	],

    title: '站内信模板定义', 
	bodyStyle:'overflow-y:auto;overflow-x:hidden',
	actType: 'add',//默认新增
	constructor: function (){
        this.callParent();
    },
	initComponent: function(){
		var znxTempletInfoPanel = {
            znxServStore: new KitchenSink.view.common.store.comboxStore({
				recname: 'TZ_EMLS_DEF_TBL',
				condition:{
					TZ_JG_ID:{
						value:'',
						operator:"01",
						type:"01"
					}
				},
				result:'TZ_EMLSERV_ID,TZ_EML_ADDR100'
			}),
		    resTmplStore: new KitchenSink.view.common.store.comboxStore({
				recname: 'TZ_TMP_DEFN_TBL',
				condition:{
					TZ_JG_ID:{
						value:'',
						operator:"01",
						type:"01"
					}
				},
				result:'TZ_YMB_ID,TZ_YMB_NAME'
			})
        };
		
		Ext.apply(this,{
            znxTempletInfoPanel:znxTempletInfoPanel,
			items: [{
				xtype: 'form',
				reference: 'znxTempletForm',
				layout: {
					type: 'vbox',       // Arrange child items vertically
					align: 'stretch'    // 控件横向拉伸至容器大小
				},
				actType:"add",
				border: false,
				bodyPadding: 10,
				bodyStyle:'overflow-y:auto;overflow-x:hidden',
				
				fieldDefaults: {
					msgTarget: 'side',
					labelWidth: 120,
					labelStyle: 'font-weight:bold'
				},
				
				items: [{
					xtype: 'textfield',
					fieldLabel: Ext.tzGetResourse("TZ_ZNX_TMPL_MG_COM.TZ_ZNX_TMPL_STD.znxtempid","站内信模板编号"),
					name: 'znxtempid',
					maxLength: 15,
					allowBlank: false,
					afterLabelTextTpl: [
						'<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'
					]
				},{
					xtype: 'textfield',
					fieldLabel: '',
					name: 'znxContentMode',
					hidden:true
				},{
					xtype: 'textfield',
					fieldLabel: Ext.tzGetResourse("TZ_ZNX_TMPL_MG_COM.TZ_ZNX_TMPL_STD.znxtempname","站内信模板名称"),
					name: 'znxtempname',
					maxLength: 60,
					afterLabelTextTpl: [
						'<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'
					],
					allowBlank: false
				},{
					xtype: 'combobox',
					fieldLabel: Ext.tzGetResourse("TZ_ZNX_TMPL_MG_COM.TZ_ZNX_TMPL_STD.znxtemporg","机构"),
					forceSelection: true,
					editable: false,
					store: new KitchenSink.view.common.store.comboxStore({
						recname: 'TZ_JG_BASE_T',
						condition:{
							TZ_JG_EFF_STA:{
								value:"Y",
								operator:"01",
								type:"01"
							}
						},
						result:'TZ_JG_ID,TZ_JG_NAME'
					}),
					value:Ext.tzOrgID,
					valueField: 'TZ_JG_ID',
					displayField: 'TZ_JG_NAME',
					//typeAhead: true,
					queryMode: 'local',
					name: 'znxtemporg',
					afterLabelTextTpl: [
						'<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'
					],
					listeners:{
						select: 'changeOrg',
						afterrender: function(combox){
							//当前登录人机构id
							if( (combox.value).toUpperCase() != 'ADMIN')
							{
								combox.readOnly = true;
							}
							//console.log(combox.findRecordByValue(value));
						}
					},
					allowBlank: false
				},{
					xtype: 'tabpanel',
                    height:'auto',
					listeners:{
						tabchange: function(tabPanel,newCard){
							if (newCard.title == "内容编辑")
							{
								
								var form = tabPanel.findParentByType('form').getForm();
								
								//var znxContentMode = form.findField("znxContentMode");
								//var znxContent = form.findField("znxtmpcontent");
								//var znxContentHtml = form.findField("znxtmpcontentHtml");
								/*
								if(znxContentMode.value != 'ueditor')
								{
									znxContentHtml.setValue(znxContent.value);
									znxContentMode.setValue('ueditor');
								};
								*/
								
								var copyItemsDom = document.getElementsByName("itemznxCopy");
								
								for (var i=0;i<copyItemsDom.length;i++)
								{
									$(copyItemsDom[i]).zclip({
										beforeCopy:function(){
											var itemHtml = this.parentNode.parentNode.parentNode.innerHTML;
											var itemFirstCharPositon = itemHtml.indexOf("["); 
											var itemLastCharPositon = itemHtml.indexOf("]"); 
											var itemPara = itemHtml.slice(itemFirstCharPositon,itemLastCharPositon+1);
											form.findField("copyfield").setValue(itemPara);
										},
										copy:function(){
											return form.findField("copyfield").getValue();
										}
									});
								}
								
							}									
						}
					},
					frame: true,
					items:[{
							title:'基本信息',
							items:[{
								layout: {
									type: 'vbox',       // Arrange child items vertically
									align: 'stretch'    // 控件横向拉伸至容器大小
								},
                                border: false,
								bodyPadding: 10,
								bodyStyle:'overflow-y:auto;overflow-x:hidden',
								fieldDefaults: {
									msgTarget: 'side',
									labelWidth: 120,
									labelStyle: 'font-weight:bold'
								},
								items:[
                                  {
                                    xtype:'checkbox',
                                    fieldLabel: Ext.tzGetResourse('TZ_ZNX_TMPL_MG_COM.TZ_ZNX_TMPL_STD.isuse','是否启用'),
                                    name:'isuse',
                                    inputValue:'Y',
                                    uncheckedValue:'N',
                                    afterLabelTextTpl: [
                                      '<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'
                                  ]
                                  },{
                                    xtype:'checkbox',
                                    fieldLabel:Ext.tzGetResourse('TZ_ZNX_TMPL_MG_COM.TZ_ZNX_TMPL_STD.ifRpt','是否判重'),
                                    name:'ifRpt',
                                    inputValue:'Y',
                                    uncheckedValue:'N'
                                    },{
									xtype: 'combobox',
									fieldLabel: Ext.tzGetResourse("TZ_ZNX_TMPL_MG_COM.TZ_ZNX_TMPL_STD.restempid","模版类型"),
									forceSelection: true,
									editable: false,
									/**/
									store: new KitchenSink.view.common.store.comboxStore({
										recname: 'TZ_TMP_DEFN_TBL',
										condition:{
										},
										result:'TZ_YMB_ID,TZ_YMB_NAME'
									}),
									valueField: 'TZ_YMB_ID',
									displayField: 'TZ_YMB_NAME',
									queryMode: 'local',
									name: 'restempid',
									listeners:{
										select: 'changeResTmpl',
										focus: function (combo,event, eOpts) {
											var form = this.findParentByType("form");
											var znxtemporg = form.getForm().findField("znxtemporg").getValue();
										   
											var tzStoreParams ='{"OperateType":"COMBOX","recname":"TZ_TMP_DEFN_TBL","condition":{"TZ_JG_ID":{"value":"'+znxtemporg+'","operator":"01","type":"01"},"TZ_USE_FLAG":{"value":"'+'Y'+'","operator":"01","type":"01"}},"result":"TZ_YMB_ID,TZ_YMB_NAME"}';
											
											var arrayData;
											
											arrayData = new Array();
											
											znxTempletInfoPanel.resTmplStore.tzStoreParams=tzStoreParams;
											
											znxTempletInfoPanel.resTmplStore.load({
												scope: this,
												callback: function(records, operation, success) {
													//console.log(records);
													for(var i=0;i<records.length;i++){
														arrayData.push(records[i].data);
													}
												combo.store.loadData(arrayData);
												}
											} );
										}
									},
									afterLabelTextTpl: [
										'<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'
									],
									allowBlank: false
								},{
									xtype: 'textarea',
									fieldLabel: Ext.tzGetResourse("TZ_ZNX_TMPL_MG_COM.TZ_ZNX_TMPL_STD.znxtmpdesc","描述"),
									name:'znxtmpdesc'
								}]
							}]
						},{
							title:'内容编辑',
							items:[{
                                xtype:'form',
								layout: {
									type: 'vbox',       // Arrange child items vertically
									align: 'stretch'    // 控件横向拉伸至容器大小
								},
                                border: false,
								bodyStyle:'overflow-y:auto;overflow-x:hidden',
								fieldDefaults: {
									msgTarget: 'side',
									labelWidth: 120,
									labelStyle: 'font-weight:bold'
								},
								items:[{
									xtype: 'textfield',
									fieldLabel: Ext.tzGetResourse("TZ_ZNX_TMPL_MG_COM.TZ_ZNX_TMPL_STD.znxtmpsubject","站内信主题"),
									maxLength: 254,
									name: 'znxtmpsubject',
                                    margin:10
								},{
									xtype: 'checkbox',
									boxLabel: Ext.tzGetResourse("TZ_ZNX_TMPL_MG_COM.TZ_ZNX_TMPL_STD.ispersonalize","站内信内容不包含个性化动态合并内容"),
									name:'ispersonalize',
                                    margin:10,
									inputValue:'Y',
									uncheckedValue:'N',
									style:{
										marginLeft:'123px'
									}

								},{
									xtype: 'checkbox',
									boxLabel: Ext.tzGetResourse("TZ_ZNX_TMPL_MG_COM.TZ_ZNX_TMPL_STD.ishtml","以HTML格式发送邮件"),
									name:'ishtml',
                                    margin:10,
									inputValue:'Y',
									checked:true,
									uncheckedValue:'N',
									style:{
										marginLeft:'123px'
									},
									handler:'htmlSwitch',
									afterRender:function()
									{
										var form = this.findParentByType("form").getForm();
										if(this.checked){
											var znxContent = form.findField("znxtmpcontent");
											var znxContentHtml = form.findField("znxtmpcontentHtml");
											znxContent.setHidden(true);
											znxContentHtml.setHidden(false);
										}else
										{
											var znxContent = form.findField("znxtmpcontent");
											var znxContentHtml = form.findField("znxtmpcontentHtml");
											znxContent.setHidden(false);
											znxContentHtml.setHidden(true);
										}
									}
								},{
									xtype: 'textarea',
									fieldLabel: Ext.tzGetResourse("TZ_ZNX_TMPL_MG_COM.TZ_ZNX_TMPL_STD.znxtmpcontent","站内信内容"),
									name:'znxtmpcontent',
                                    margin:10
								},{
									//xtype: 'htmleditor',
									xtype: 'ueditor',
									fieldLabel: Ext.tzGetResourse("TZ_ZNX_TMPL_MG_COM.TZ_ZNX_TMPL_STD.znxtmpcontentHtml","站内信内容"),
									height: 300,
									zIndex: 900,
									name:'znxtmpcontentHtml',
                                    margin:10,
                                    panelXtype: 'znxTempletInfo'  
								}
								,{
									xtype: 'textfield',
									fieldLabel: Ext.tzGetResourse("TZ_ZNX_TMPL_MG_COM.TZ_ZNX_TMPL_STD.copyfield","参数信息项复制"),
									name: 'copyfield',
									hidden:true
								},
								{
									xtype: 'grid',
                                    height:200,
                                    border: false,
                                    title:'信息项列表',
									name: 'znxTmplItemGrid',
									reference: 'znxTmplItemGrid',
									/*
									selModel: {
										type: 'spreadsheet',
										columnSelect: false  // replaces click-to-sort on header
									},
									plugins: 'clipboard',
									*/
									store: {
												type: 'znxTempletItemStore'
										   },
									columnLines: true,    //显示纵向表格线	
									columns: [{ 
										text: Ext.tzGetResourse("TZ_ZNX_TMPL_MG_COM.TZ_ZNX_TMPL_STD.parainfoitem","信息项名称"),
										sortable: true,
										dataIndex: 'parainfoitem',
										minWidth: 200,
										flex:1
									},{
										xtype:'linkcolumn',
										text: Ext.tzGetResourse("TZ_ZNX_TMPL_MG_COM.TZ_ZNX_TMPL_STD.iteminsert","插入"),
										sortable: false,	
										width:70,
										items:[{
											text:"插入",
											handler: "insertznxitem",
											tooltip:"插入"
										}]
									},{
										text: Ext.tzGetResourse("TZ_ZNX_TMPL_MG_COM.TZ_ZNX_TMPL_STD.itemznxCopy","复制"),
										menuDisabled: true,
										sortable: false,
										dataIndex: 'itemcopy',
										width:70,
										renderer:function(){
										   return '<a href="javascript:void(0)" name="itemznxCopy">复制</a>';
										}
									}]
								}]
							}]
						  }
						]
				}]
			}]
		});
		this.callParent();
	},
    buttons: [{
		text: '保存',
		iconCls:"save",
		handler: 'onFormSave'
	}, {
		text: '确定',
		iconCls:"ensure",
		handler: 'onFormEnsure'
	}, {
		text: '关闭',
		iconCls:"close",
		handler: 'onFormClose'
	}]
});