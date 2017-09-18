var refreshTaskMgr=new Ext.util.TaskRunner();
Ext.define('KitchenSink.view.bulkEmailAndSMS.SmsGroupSends.SmsGroupSendsDefn', {
    extend: 'Ext.panel.Panel',
    requires: [
        'Ext.data.*',
        'Ext.grid.*',
        'Ext.util.*',
        'tranzvision.extension.grid.column.Link',
        'KitchenSink.view.bulkEmailAndSMS.SmsGroupSends.SmsGroupSendsDefnController',
        'KitchenSink.view.bulkEmailAndSMS.SmsGroupSends.pasteFromExcelWinSMS',
        'KitchenSink.view.bulkEmailAndSMS.ImportExcel.smsEmlImportExcelWindow',
		'KitchenSink.view.bulkEmailAndSMS.copyHistory.copyFromHistoryWin'
    ],
    xtype: 'smsGroupDet',
    title:Ext.tzGetResourse("TZ_SMSQ_COM.TZ_SMSQ_DET_STD.panelTitle","短信发送定义"),
    controller: 'SmsGroupSendsDefnController',
    bodyStyle:'overflow-y:auto;overflow-x:hidden',
    pageId:' ',
    BulkTaskId:' ',

    listeners: {
        close:function(t){
            for(var i =0;i<refreshTaskMgr.tasks.length;i++){
                if(refreshTaskMgr.tasks[i].id== t.pageId){
                    refreshTaskMgr.stop(refreshTaskMgr.tasks[i]);
                }
            }
        },
        afterrender:function(t){
            var params;

            t.pageId = Ext.getCmp('tranzvision-framework-content-panel').getActiveTab().id;

            var refreshTask = {
                id: t.pageId,
                run: function () {
                    var currentPage = Ext.getCmp('tranzvision-framework-content-panel').getActiveTab();
                    if(currentPage.id.indexOf('smsGroupDet')!=-1){
                        //console.log(t.BulkTaskId);
                        if (t.BulkTaskId!=" "){
                            params = '"smsQfId":"'+t.BulkTaskId+'"';
                            var tzParams = '{"ComID":"TZ_SMSQ_COM ","PageID":"TZ_SMSQ_DET_STD","OperateType":"getRwzxZt","comParams":{'+params+'}}';
                            Ext.Ajax.request({
                                url: Ext.tzGetGeneralURL,
                                params:{tzParams:tzParams},
                                success: function(response, opts)
                                {
                                    //返回值内容
                                    var jsonText = response.responseText;
                                    var jsonObject = Ext.util.JSON.decode(jsonText);
                                    //console.log(jsonObject.comContent.rwzxZt);
                                    if (jsonObject.comContent.rwzxZt==""||jsonObject.comContent.rwzxZt=="D"||jsonObject.comContent.rwzxZt=="E") {
                                        t.down('button[reference=saveBtn]').setDisabled(false);
                                        t.down('button[reference=sendBtn]').setDisabled(false);
                                        t.getController().pageFiledsDisControl(t.child('form'));
                                    } else {
                                        t.down('button[reference=saveBtn]').setDisabled(true);
                                        t.down('button[reference=sendBtn]').setDisabled(true);
                                        t.getController().pageReadonly(t.child('form'));
                                    }
									/*
                                    if (jsonObject.comContent.rwzxZt== "B") {
                                        t.down('button[reference=revokeBtn]').setVisible(true);
                                    }else{
                                        t.down('button[reference=revokeBtn]').setVisible(false);
                                    }
                                    if (jsonObject.comContent.rwzxZt=="C"||jsonObject.comContent.rwzxZt=="D") {
                                        t.down('button[reference=viewHisBtn]').setDisabled(false);
                                    }
									*/
									if (jsonObject.comContent.rwzxZt == "C" || jsonObject.comContent.rwzxZt == "D") {
                                        t.down('button[reference=revokeBtn]').setVisible(false);
										t.down('displayfield[name=dsfsInfo]').setVisible(false);
                                    }
									
									if (jsonObject.comContent.rwzxZt == "B" && t.down('checkbox[name=dsfsFlag]').getValue()) {
										var dsfsInfo='<span style="color:red">邮件将正在发送中...</span>';
										t.down('displayfield[name=dsfsInfo]').setValue(dsfsInfo);
										t.down('displayfield[name=dsfsInfo]').setVisible(true);
                                    }
                                }
                            });
                        }
                    }
                },
                interval: 5000,
                scopt: this
            };
            refreshTaskMgr.start(refreshTask);
        }
    },

    initComponent: function () {
        var SMSReg = /^1\d{10}$/;
        Ext.apply(Ext.form.field.VTypes, {
            phone: function(val, field) {
                var bolFlag;
                bolFlag = SMSReg.test(val);
                return bolFlag;
            },
            phoneText: '请输入手机号码'
        });
		
		Ext.apply(Ext.form.field.VTypes,{
			mobiles: function(val, field){
				var emlsReg = /^(1\d{10}\;)*(1\d{10})$/;
				return emlsReg.test(val);
			},
			mobilesText: '请输入正确手机号码，多个手机号码之间用英文分号分隔'	
		});

        Ext.util.CSS.createStyleSheet(" .readOnly-tagfield-BackgroundColor div {background:#f4f4f4;}","readOnly-tagfield-BackgroundColor");
        Ext.util.CSS.createStyleSheet(" .readOnly-combox-BackgroundColor input {background:#f4f4f4;}","readOnly-combox-BackgroundColor");
        Ext.util.CSS.createStyleSheet(" .readOnly-textarea-BackgroundColor textarea {background:#f4f4f4;}","readOnly-textarea-BackgroundColor");
		Ext.util.CSS.createStyleSheet(" .disabled-button-color span {opacity: 0.8;}","disabled-button-color");

        Ext.apply(this, {
            items: [{
                xtype: 'form',
                reference: 'SmsGroupSendsDefnForm',
                layout: {
                    type: 'vbox',      // Arrange child items vertically
                    align: 'stretch'    // 控件横向拉伸至容器大小
                },
                border: false,
                bodyPadding: 8,
                fieldDefaults: {
                    msgTarget: 'side',
                    labelWidth: 120,
                    labelStyle: 'font-weight:bold'
                },
                items: [{
                    xtype: 'textfield',
                    fieldLabel:"群发任务ID" ,
                    name: 'smsQfId',
                    hidden:true
                },{
                    xtype: 'textfield',
                    fieldLabel:Ext.tzGetResourse("TZ_SMSQ_COM.TZ_SMSQ_DET_STD.smsQfDesc","群发任务名称") ,
                    name: 'smsQfDesc',
					allowBlank: false
                },{
					layout:{
						type:'column'	
					},
					items:[{
						columnWidth:0.8,
						xtype: 'fieldset',
						border:false,
						reference:'sendModelSet',
						defaultType: 'radio', // each item will be a radio button
						layout: {
							type:'hbox',  
							padding:'10px 0 10px 0',  	
						},
						defaults: {
							hideEmptyLabel: true
						},
						style:{
							margin:'0 0 0 -10px'
						},
                   		items: [{
							xtype:'label',
							baseCls:'x-form-item-label-inner x-form-item-label-inner-default',
							width:'125px',
							style:{
								padding:'5px 0 0 0',
								'font-weight':'bold'
							},
							text: Ext.tzGetResourse("TZ_SMSQ_COM.TZ_SMSQ_DET_STD.sendModel","发送模式:")
						}, {
							//checked: true,
							boxLabel: '一般发送',
							name: 'sendModel',
							inputValue: 'NOR',
							reference:'sendModelNor',
							listeners: {
								change: 'norSend'
							}
						}, {
							xtype:'button',
							iconCls:'fa fa-question-circle',
							tooltip:Ext.tzGetResourse("TZ_SMSQ_COM.TZ_SMSQ_DET_STD.NorTip","使用邮件模板或者直接编写邮件内容发送，选择邮件模板时收件人只能添加听众"),
							border:false,
							style:{
								background:'#fff',
								padding:'5px 0 0 0'
							},
							disabled:true
						},{
							xtype:'splitter',
							width:100,
							style:{
								background:'#fff'
							}
						}, {
							checked: true,
							boxLabel: '导入Excel发送',
							name: 'sendModel',
							inputValue: 'EXC',
							reference:'sendModelExc',
							listeners: {
								change: 'excSend'
							}
						},{
							xtype:'button',
							iconCls:'fa fa-question-circle',
							tooltip:Ext.tzGetResourse("TZ_SMSQ_COM.TZ_SMSQ_DET_STD.ExcTip","导入Excel之后，可以设置Excel中对应的邮件收件人、内容等，并使用设置内容进行发送"),
							border:false,
							style:{
								background:'#fff',
								padding:'5px 0 0 0'
							},
							disabled:true
						},{
							xtype:'button',
							text:Ext.tzGetResourse("TZ_SMSQ_COM.TZ_SMSQ_DET_STD.excBtn","导入Excel"),
							tooltip:Ext.tzGetResourse("TZ_SMSQ_COM.TZ_SMSQ_DET_STD.excBtnTip","导入Excel"),
							handler:'importFromExcel',
							reference:'impExc',
							bind: {
								hidden: '{!sendModelExc.checked}'
							}
						}]
					},{
						columnWidth:0.2,
						anchor:'100%', 
						layout: {
							 type:'hbox',  
							 padding:'10px 50px 10px 0',  
							 pack:'end',  
							 align:'middle'
						},
						items:[{
							xtype: 'button',
							reference:'copyHistoryBtn',
							text: Ext.tzGetResourse("TZ_SMSQ_COM.TZ_SMSQ_DET_STD.copyHistory","复制历史任务"),
							tooltip: Ext.tzGetResourse("TZ_SMSQ_COM.TZ_SMSQ_DET_STD.copyHistoryTip","复制历史任务"),
							hidden: true,
							handler:'copyHistoryData'
						}]	
					}]
					/*
                    xtype: 'fieldset',
                    border:false,
                    reference:'sendModelSet',
                    defaultType: 'radio', // each item will be a radio button
                    layout: 'hbox',
                    defaults: {
                        hideEmptyLabel: true
                    },
                    style:{
                        margin:'0 0 0 -10px'
                    },
                    items: [{
                        xtype:'label',
                        baseCls:'x-form-item-label-inner x-form-item-label-inner-default',
                        width:'125px',
                        style:{
                            padding:'8px 0 0 0',
                            'font-weight':'bold'
                        },
                        text: Ext.tzGetResourse("TZ_SMSQ_COM.TZ_SMSQ_DET_STD.sendModel","发送模式:")
                    }, {
                        //checked: true,
                        boxLabel: '一般发送',
                        name: 'sendModel',
                        inputValue: 'NOR',
                        reference:'sendModelNor',
                        listeners: {
                            change: 'norSend'
                        }
                    }, {
                        xtype:'button',
                        iconCls:'fa fa-question-circle',
                        tooltip:Ext.tzGetResourse("TZ_SMSQ_COM.TZ_SMSQ_DET_STD.NorTip","使用邮件模板或者直接编写邮件内容发送，选择邮件模板时收件人只能添加听众"),
                        border:false,
                        style:{
                            background:'#fff',
                            padding:'8px 0 0 0'
                        },
                        disabled:true
                    },{
                        xtype:'splitter',
                        width:100,
                        style:{
                            background:'#fff'
                        }
                    }, {
						checked: true,
                        boxLabel: '导入Excel发送',
                        name: 'sendModel',
                        inputValue: 'EXC',
                        reference:'sendModelExc',
                        listeners: {
                            change: 'excSend'
                        }
                    },{
                        xtype:'button',
                        iconCls:'fa fa-question-circle',
                        tooltip:Ext.tzGetResourse("TZ_SMSQ_COM.TZ_SMSQ_DET_STD.ExcTip","导入Excel之后，可以设置Excel中对应的邮件收件人、内容等，并使用设置内容进行发送"),
                        border:false,
                        style:{
                            background:'#fff',
                            padding:'8px 0 0 0'
                        },
                        disabled:true
                    },{
                        xtype:'button',
                        text:Ext.tzGetResourse("TZ_SMSQ_COM.TZ_SMSQ_DET_STD.excBtn","导入Excel"),
                        tooltip:Ext.tzGetResourse("TZ_SMSQ_COM.TZ_SMSQ_DET_STD.excBtnTip","导入Excel"),
                        handler:'importFromExcel',
						reference:'impExc',
                        bind: {
                            hidden: '{!sendModelExc.checked}'
                        }
                    },{
						layout: {
                         type:'hbox',  
						 padding:'0 10px 10px 0',  
						 pack:'end',  
						 align:'middle'
						},
						items:[{
							xtype:'button',
							text:'<span style="color:#fff">复制历史任务</span>',
							tooltip:Ext.tzGetResourse("TZ_SMSQ_COM.TZ_SMSQ_DET_STD.copyHistory","复制历史任务"),
							width:100
						}]	
					}]
                },{
					//anchor:'100%', 
					
                    layout: {
                         type:'hbox',  
						 padding:'0 10px 10px 0',  
						 pack:'end',  
						 align:'middle'
                    },
					items:[{
						xtype:'button',
						text:'<span style="color:#fff">复制历史任务</span>',
						tooltip:Ext.tzGetResourse("TZ_SMSQ_COM.TZ_SMSQ_DET_STD.copyHistory","复制历史任务"),
						width:100
					}]*/
                },{
					xtype: 'component',
					style:{
						marginLeft: '123px'	
					},
					html: '<span style="color: #F00;">发送多个收件人时，手机号码使用半角逗号“,”隔开。</span>'	
				},{
                    xtype: 'tagfield',
                    fieldLabel: Ext.tzGetResourse("TZ_SMSQ_COM.TZ_SMSQ_DET_STD.recever","收件人"),
                    //emptyText:'可手工填写电子邮箱；也可从EXCEL中粘贴某一列的电子邮箱',
                    reference: 'receverTagField',
					name: 'recever',
                    allowBlank:false,
                    displayField: 'desc',
                    valueField: 'id',
                    createNewOnEnter: true,
                    createNewOnBlur: true,
                    filterPickList: true,
                    queryMode: 'local',
                    publishes: 'value',
					//minHeight: 200,
                    listeners: {
                        //change:'receverChange'
                    },
                    listConfig:{
                        maxHeight:1
                    }
                },{
                    xtype: 'textfield',
                    fieldLabel:"收件人" ,
                    name: 'receverOrigin',
                    hidden:true
                },{
                    layout: {
                        type:'hbox'
                    },
                    padding:'0 0 8px 0',
                    xtype:"toolbar",
					reference: 'receverToolbar',
                    items:["->",{
						xtype:'button',
                        reference:'selectStuBtn',
                        text:'<span style="color:#fff">选择考生</span>',
                        tooltip:Ext.tzGetResourse("TZ_SMSQ_COM.TZ_SMSQ_DET_STD.selectStuTip","选择考生"),
                        handler:'addStruData',
						baseCls:'x-btn x-unselectable x-column x-btn-default-small'
					},{
                        xtype:'button',
                        reference:'addAudienceBtn',
                        text:'<span style="color:#fff">添加听众</span>',
                        tooltip:Ext.tzGetResourse("TZ_SMSQ_COM.TZ_SMSQ_DET_STD.addAudienceTip","添加听众"),
                        handler:'addAudience',
                        //baseCls:'x-btn x-unselectable x-box-item x-toolbar-item x-btn-default-small x-btn-inner x-btn-inner-default-small'
						baseCls:'x-btn x-unselectable x-column x-btn-default-small'
                    },{
                        xtype:'button',
                        reference:'clearAllBtn',
                        text:'<span style="color:#fff">清除所有</span>',
                        tooltip:Ext.tzGetResourse("TZ_SMSQ_COM.TZ_SMSQ_DET_STD.clearAllTip","清除所有"),
                        handler:'clearAll',
                        //baseCls:'x-btn x-unselectable x-box-item x-toolbar-item x-btn-default-small x-btn-inner x-btn-inner-default-small'
						baseCls:'x-btn x-unselectable x-column x-btn-default-small'
                    },{
                        xtype:'button',
                        reference:'pasteFromExcelBtn',
                        iconCls:'fa fa-clipboard',
                        tooltip:Ext.tzGetResourse("TZ_SMSQ_COM.TZ_SMSQ_DET_STD.pasteFromExcel","从Excel粘贴"),
                        handler:'pasteFromExcel',
                        baseCls:'x-btn-inner x-btn-inner-default-small',
                        style:{
                            color:'#666',
							cursor:'pointer'
                        },
                        border:false
                    }]
                },{
                    xtype: 'fieldset',
                    reference:'tsfsSet',
                    border:false,
                    layout: {
                        type:'hbox'
                    },
                    style:{
                        padding:0
                    },
                    items: [{
                        xtype:'splitter',
                        width:125,
                        style:{
                            background:'#fff'
                        }
                    }, {
                        xtype:'checkbox',
                        boxLabel: Ext.tzGetResourse("TZ_SMSQ_COM.TZ_SMSQ_DET_STD.tsfsFlag","同时发送给"),
                        reference: 'tsfsFlag',
                        name: 'tsfsFlag',
                        width:125
                        //inputValue: 'cat'
                    },{
                        xtype: 'textfield',
                        name: 'tsfsPhone',
                        labelSeparator:' ',
                        width:'75%',
                        vtype: 'mobiles',
                        bind: {
                            disabled: '{!tsfsFlag.checked}'
                        }
                    }]
                },{
                    layout: {
                        type: 'column'
                    } ,
                    padding:'0 0 8px 0',
                    items:[{
                            xtype: 'combobox',
                            fieldLabel:Ext.tzGetResourse("TZ_SMSQ_COM.TZ_SMSQ_DET_STD.smsTmpId","短信模版") ,
                            name: 'smsTmpId',
                            reference:'smsTmpId',
                            queryMode: 'remote',
                            valueField: 'smstmpl',
                            displayField: 'desc',
                            editable: false,
                            columnWidth:1,
                            triggerAction: 'all',
                            triggers:{
                                clear: {
                                    cls: 'x-form-clear-trigger',
                                    handler: function(field){
                                        field.setValue("");

                                        var formdatanull={
                                            "smsCont": ""
                                        };
                                        var form = field.findParentByType('form');
                                        form.getForm().setValues(formdatanull);
                                        form.down('grid[reference=smsTmplItemGrid]').store.removeAll();
                                    }
                                }
                            },
                            listeners: {
                                change:function(t, newValue, oldValue, eOpts){
                                    if(t.findParentByType('form').down('radio[reference=sendModelNor]').checked){
                                        var SmsGroupDtFrom = t.findParentByType('form');
                                        if(newValue!=""){
                                            //SmsGroupDtFrom.child('tagfield[reference=receverTagField]').setEditable(false);
                                            //SmsGroupDtFrom.child('tagfield[reference=receverTagField]').disabled=true;
                                            //SmsGroupDtFrom.down('tagfield[reference=receverTagField]').addCls('readOnly-tagfield-BackgroundColor');
                                            SmsGroupDtFrom.child('toolbar').child('button[reference=pasteFromExcelBtn]').disabled=true;
											
											SmsGroupDtFrom.child('toolbar').child('button[reference=selectStuBtn]').disabled=true;
											SmsGroupDtFrom.child('toolbar').child('button[reference=selectStuBtn]').addCls('x-item-disabled x-btn-disabled');

                                            SmsGroupDtFrom.down('button[reference=setSmsTmpl]').setDisabled(false);
                                            SmsGroupDtFrom.down('button[reference=setSmsTmpl]').removeCls('disabled-button-color');
                                            //加载邮件模版信息
                                            //短信群发id;
                                            var smsQfId = SmsGroupDtFrom.getForm().findField("smsQfId").getValue();
                                            var tzParams = '{"ComID":"TZ_SMSQ_COM","PageID":"TZ_SMSQ_DET_STD","OperateType":"getSmsTmpInfo","comParams":{"SmsTmpId":"'+newValue+'","smsQfId":"'+smsQfId+'"}}';
                                            Ext.tzLoadAsync(tzParams,function(responseData){
                                                SmsGroupDtFrom.getForm().setValues(responseData);
                                            });
                                            var tzParams = '{"ComID":"TZ_SMSQ_COM","PageID":"TZ_SMSQ_DET_STD","OperateType":"getSmsTmpItem","comParams":{"SmsTmpId":"'+newValue+'"}}';
                                            Ext.tzLoadAsync(tzParams,function(responseData){
												var tmpItemGrid = SmsGroupDtFrom.down('grid[reference=smsTmplItemGrid]');
												Ext.suspendLayouts();
												tmpItemGrid.store.suspendEvents();
												
                                                tmpItemGrid.store.removeAll(true);
                                                tmpItemGrid.store.add(responseData['root']);
												
												tmpItemGrid.store.resumeEvents();
												tmpItemGrid.reconfigure(tmpItemGrid.store);
												Ext.resumeLayouts(true);

                                                var userAgent = navigator.userAgent;
                                                if (userAgent.indexOf("compatible") > -1 && userAgent.indexOf("MSIE") > -1) {
                                                    var copyItemsDom = document.getElementsByName("itememlCopy");
                                                    for (var i=0;i<copyItemsDom.length;i++)
                                                    {
                                                        $(copyItemsDom[i]).zclip({
                                                            beforeCopy:function(){
                                                                var itemHtml = this.parentNode.parentNode.parentNode.innerHTML;
                                                                var itemFirstCharPositon = itemHtml.indexOf("[");
                                                                var itemLastCharPositon = itemHtml.indexOf("]");
                                                                var itemPara = itemHtml.slice(itemFirstCharPositon,itemLastCharPositon+1);
                                                                SmsGroupDtFrom.down('textfield[name=copyfield]').setValue(itemPara);
                                                            },
                                                            copy:function(){
                                                                return SmsGroupDtFrom.down('textfield[name=copyfield]').getValue();
                                                            }
                                                        });
                                                    }
                                                }
                                            });
                                        }else{
                                            //SmsGroupDtFrom.child('tagfield[reference=receverTagField]').setEditable(true);
                                            //SmsGroupDtFrom.child('tagfield[reference=receverTagField]').disabled=false;
                                            //SmsGroupDtFrom.down('tagfield[reference=receverTagField]').removeCls('readOnly-tagfield-BackgroundColor');
                                            SmsGroupDtFrom.child('toolbar').child('button[reference=pasteFromExcelBtn]').disabled=false;
											
											SmsGroupDtFrom.child('toolbar').child('button[reference=selectStuBtn]').disabled=false;
											SmsGroupDtFrom.child('toolbar').child('button[reference=selectStuBtn]').removeCls('x-item-disabled x-btn-disabled');

                                            SmsGroupDtFrom.down('button[reference=setSmsTmpl]').setDisabled(true);
											SmsGroupDtFrom.down('button[reference=setSmsTmpl]').addCls('disabled-button-color');
                                        }
                                    }
                                }
                            }
                        }, {
                            xtype:'button',
                            iconCls:'set',
                            reference:'setSmsTmpl',
                            tooltip:Ext.tzGetResourse("TZ_SMSQ_COM.TZ_SMSQ_DET_STD.setSmsTmpl","设置短信模版"),
                            border:false,
                            style:{
                                background:'#fff'
                            },
                            //disabled: true,
                            handler:'setSmsTmpl',
                            width:30
                        }
                    ]
                },{
                    xtype: 'textfield',
                    fieldLabel:Ext.tzGetResourse("TZ_SMSQ_COM.TZ_SMSQ_DET_STD.smsQm","短信签名") ,
                    name: 'smsQm',
                    hidden:true
                },{
                    xtype: 'textareafield',
                    fieldLabel: Ext.tzGetResourse("TZ_SMSQ_COM.TZ_SMSQ_DET_STD.smsCont","短信内容") ,
                    //allowBlank:false,
                    name:'smsCont',
                    listeners: {
                        change:function(t,newValue, oldValue){
                            var chsReg=/[^\x00-\xff]/;
                            var num;
                            if(chsReg.test(newValue)){
                                num=70;
                            }else
                            {
                                num=160;
                            }
                            if(newValue){
                                t.findParentByType('form').down('displayfield[name=inputCount]').setVisible(true);
                                var sendCount=0;
                                if(newValue.length%num==0){
                                    sendCount=newValue.length/num;
                                }else{
                                    sendCount=parseInt(newValue.length/num)+1;
                                }
                                var msgtext = "共 <span style='color:red;'>"+newValue.length+"</span> 个字符,发送 <span style='color:red;'>"+sendCount+"</span> 条短信";
                                t.findParentByType('form').down('displayfield[name=inputCount]').setValue(msgtext);
                            }else{
                                t.findParentByType('form').down('displayfield[name=inputCount]').setVisible(false);
                            }
                        }
                    }
                },{
                    xtype: 'displayfield',
                    labelSeparator:' ',
                    margin:'-8px 8px 0 0',
                    style:'text-align:right;',
                    name: 'inputCount',
                    hidden:true
                },{
                    xtype: 'textfield',
                    fieldLabel:"参数信息项复制",
                    name: 'copyfield',
                    hidden:true
                },{
                    xtype: 'panel',
                    title: '信息项设置',
                    collapsible:true,
                    collapsed:true,
                    width:'100%',
                    flex:1,
                    padding:'0 0 0 125px',
                    //margin:'-25px 0 0 0',
                    items: [{
                        xtype: 'grid',
                        height:200,
                        border: true,
                        name: 'smsTmplItemGrid',
                        reference: 'smsTmplItemGrid',
                        columnLines: true,    //显示纵向表格线
                        viewConfig: {
                            enableTextSelection: true
                        },
                        columns: [{
                            text: Ext.tzGetResourse("TZ_SMSQ_COM.TZ_SMSQ_DET_STD.parainfoitem","信息项名称"),
                            sortable: true,
                            dataIndex: 'parainfoitem',
                            minWidth: 200,
                            flex:1
                        },{
                            xtype:'linkcolumn',
                            text: Ext.tzGetResourse("TZ_SMSQ_COM.TZ_SMSQ_DET_STD.iteminsert","插入"),
                            sortable: false,
                            width:70,
                            items:[{
                                text:"插入",
                                handler: "insertSmsitem",
                                tooltip:"插入"
                            }]
                        },{
                            text: Ext.tzGetResourse("TZ_SMSQ_COM.TZ_SMSQ_DET_STD.itememlCopy","复制"),
                            menuDisabled: true,
                            sortable: false,
                            dataIndex: 'itemcopy',
                            width:70,
                            renderer:function(){
                                return '<a href="javascript:void(0)" name="itememlCopy">复制</a>';
                            }
                        }]}]
                },{
                    xtype:'fieldset',
                    title: '发送设置',
                    collapsible: true,
                    collapsed: true,
                    defaults: {anchor: '100%'},
                    layout: 'anchor',
                    items: [{
                        xtype: 'checkbox',
                        boxLabel: Ext.tzGetResourse("TZ_SMSQ_COM.TZ_SMSQ_DET_STD.dsfsFlag","定时发送"),
                        name:'dsfsFlag',
                        reference:'dsfsFlag',
                        margin:'8px 0 0 5px',
                        inputValue:'Y',
                        uncheckedValue:'N',
                        listeners: {
                            change:function(t,newValue, oldValue){
                                if(!newValue){
                                    t.findParentByType('form').down('displayfield[name=dsfsInfo]').setVisible(false);
                                }
                            }
                        }
                    },{
                        xtype: 'datefield',
                        fieldLabel: Ext.tzGetResourse("TZ_SMSQ_COM.TZ_SMSQ_DET_STD.dsfsDate","发送日期"),
                        name: 'dsfsDate',
                        margin:'8px 0 0 25px',
                        itemId: 'dsfsDate',
                        //minValue:new Date(),
                        repeatTriggerClick:true,
                        format: 'Y-m-d',
                        labelStyle: 'font-weight:normal',
                        labelWidth: 85,
                        bind: {
                            hidden: '{!dsfsFlag.checked}'
                        }
                    },{
                        xtype: 'timefield',
                        fieldLabel:Ext.tzGetResourse("TZ_SMSQ_COM.TZ_SMSQ_DET_STD.dsfsTime","发送时间"),
                        margin:'8px 0 8px 25px',
                        name: 'dsfsTime',
                        format: 'H:i',
                        increment: 5,
                        labelStyle: 'font-weight:normal',
                        labelWidth: 85,
                        //minValue:Ext.Date.format(new Date(), 'H:i') ,
                        bind: {
                            hidden: '{!dsfsFlag.checked}'
                        }
                    }/*,{
                        xtype: 'checkbox',
                        boxLabel: Ext.tzGetResourse("TZ_SMSQ_COM.TZ_SMSQ_DET_STD.transFlag","是否转发回复信息"),
                        name:'transmitFlag',
                        reference:'transmitFlag',
                        margin:'8px 0 0 5px',
                        inputValue:'Y',
                        uncheckedValue:'N',
                        listeners: {
                           // change:function(t,newValue, oldValue){
                             //   if(!newValue){
                             //       t.findParentByType('form').down('displayfield[name=dsfsInfo]').setVisible(false);
                             //   }
                            //}
                        }
                    },{
						xtype: 'tagfield',
						fieldLabel: Ext.tzGetResourse("TZ_SMSQ_COM.TZ_SMSQ_DET_STD.transPhoneNums","转发的手机号"),
						reference: 'transPhoneNumsTagField',
						name: 'transPhoneNums',
						//allowBlank:false,
						displayField: 'desc',
						valueField: 'id',
						createNewOnEnter: true,
						createNewOnBlur: true,
						filterPickList: true,
						queryMode: 'local',
						publishes: 'value',
						labelWidth: 85,
						margin:'8px 0 8px 25px',
						labelStyle: 'font-weight:normal',
						listeners: {
							//change:'transPhoneNumsChange'
						},
						listConfig:{
							maxHeight:1
						},
						bind: {
                            hidden: '{!transmitFlag.checked}'
                        }
					},{
						xtype: 'textfield',
						fieldLabel:"转发人" ,
						name: 'transOrigin',
						hidden:true
					}*/]
                },{
                    xtype:'fieldset',
                    title: '创建人信息',
                    collapsible: true,
                    collapsed: true,
                    defaults: {anchor: '100%'},
                    layout: 'anchor',
                    items: [{
                        xtype: 'displayfield',
                        fieldLabel: Ext.tzGetResourse("TZ_SMSQ_COM.TZ_SMSQ_DET_STD.crePer","创建人"),
                        margin:'8px 0 0 8px',
                        name: 'crePer'
                    },{
                        xtype: 'displayfield',
                        fieldLabel: Ext.tzGetResourse("TZ_SMSQ_COM.TZ_SMSQ_DET_STD.dept","所属部门"),
                        margin:'8px 0 0 8px',
                        name: 'dept',
                        hidden: true
                    },{
                        xtype: 'displayfield',
                        fieldLabel: Ext.tzGetResourse("TZ_SMSQ_COM.TZ_SMSQ_DET_STD.setId","setID"),
                        margin:'8px 0 0 8px',
                        name: 'setId',
                        hidden: true
                    },{
                        xtype: 'displayfield',
                        fieldLabel: Ext.tzGetResourse("TZ_SMSQ_COM.TZ_SMSQ_DET_STD.creDt","创建时间"),
                        margin:'8px 0 0 8px',
                        name: 'creDt',
                        hidden:true
                    }]
                },{
                    xtype: 'displayfield',
                    fieldLabel: " ",
                    labelSeparator:' ',
                    margin:'0 0 0 8px',
                    name: 'dsfsInfo',
                    hidden:true
                }]
            }]
        });
        this.callParent();
    },
    buttons: [ {
        text: '保存',
        iconCls:"save",
        handler: 'onPanelSave',
        reference:"saveBtn"
    },{
        text: '预览',
        iconCls:"preview",
        handler: 'preViewSms'
    },{
        text: '发送',
        iconCls:"send",
        handler: 'sendSms',
        reference:"sendBtn"
    },{
        text: '中断发送',
        iconCls:"revoke",
        handler: 'interSend',
        reference:"revokeBtn",
        hidden:true
    },{
        text: '查看发送历史',
        iconCls:"view",
        handler: 'viewSendHistory',
        reference:"viewHisBtn",
        //disabled:true
    },{
        text: '关闭',
        iconCls:"close",
        handler: 'onPanelClose'
    }]
});
