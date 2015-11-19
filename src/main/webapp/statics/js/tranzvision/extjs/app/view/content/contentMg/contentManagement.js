Ext.define('KitchenSink.view.content.contentMg.contentManagement', {
    extend: 'Ext.grid.Panel',
	requires: [
        'Ext.data.*',
        'Ext.grid.*',
        'Ext.util.*',
        'Ext.toolbar.Paging',
        'Ext.ux.ProgressBarPager',
        'KitchenSink.view.content.contentMg.contentController',
        'KitchenSink.view.content.contentMg.contentStore'
    ],
    xtype: 'contentMg',
	controller: 'contentController',
    columnLines: true,
    selModel: {
        type: 'checkboxmodel'
    },
	style:"margin:8px",
    multiSelect: true,
    title: '内容管理',
    viewConfig: {
        enableTextSelection: true
    },
	tmpChannelId:'',
	header:false,
	frame: true,
    dockedItems:[{
		xtype:"toolbar",
		dock:"bottom",
		ui:"footer",
		items:['->',{minWidth:80,text:"保存",iconCls:"save",handler:"saveContentList",name:'save'},
            {minWidth:80,text:"确定",iconCls:"ensure",handler:"saveContentList",name:'ensure'},
            {minWidth:80,text:"关闭",iconCls:"close",handler:"onComRegClose"}
			]
		},{
		xtype:"toolbar",
		name:'funToolbar',
		items:[
			{text:"查询",tooltip:"查询数据",iconCls:"query",handler:'cfgSearch'},"-",
			{text:"新增",tooltip:"新增数据",iconCls: 'add',handler:'addArt'},"-",
			{text:"编辑",tooltip:"编辑数据",iconCls: 'edit',handler:'editSelArt'},"-",
			{text:"删除",tooltip:"删除选中的数据",iconCls:"remove",handler:'deleteSelList'},'-',
			{text:"发布",tooltip:"发布选中内容",iconCls:"publish",handler:'releaseSelList'},'-',
			{text:"撤销发布",tooltip:"撤销发布选中内容",iconCls:"revoke",handler:'UndoSelList'},'->',
			{
	            xtype: 'combobox',
	            fieldLabel: '栏目',
				name:'myChannels',
				//id:'myChannels201507271129',
	            labelWidth:40,
	            labelStyle: 'font-weight:bold',
	            queryMode: 'remote',
	            width:230,
	            editable:false,
				valueField: 'TValue',
	    		displayField: 'TSDesc',
				listeners: {
				  	afterrender: function(tvType){
						var tzParams = '{"ComID":"TZ_CONTENT_MG_COM","PageID":"TZ_CONTENT_STD","OperateType":"QF","comParams":"{}"}';
						Ext.tzLoad(tzParams,function(responseData){
							var store = new Ext.data.Store({
								fields: ['TValue', 'TSDesc', 'TLDesc'],
								data:responseData.TransList
							});
							/*
							if (this.channelStore==null){
								this.channelStore=store;
							}*/
							tvType.setStore(store);
							if(store.getCount() > 0){
								tvType.setValue(store.getAt(0).get("TValue"));
							}
						});
					},
					change:function(tvType,newValue,oldValue,eOpts){
						this.tmpChannelId=newValue;
						var store = new KitchenSink.view.content.contentMg.contentStore(newValue);
						if (newValue==''){
							//store.removeAll();
							//tvType.findParentByType("grid").getStore.removeAll();
							//tvType.findParentByType("grid").child("pagingtoolbar").getStore.removeAll();
							tvType.findParentByType("grid").store.load();
							tvType.findParentByType("grid").child("pagingtoolbar").store.load();
						}else{

							tvType.findParentByType("grid").setStore(store);
							tvType.findParentByType("grid").child("pagingtoolbar").setStore(store);
						}
					}
				}
			},
			/*{
				xtype:'splitbutton',
				text:'更多操作',
				iconCls:  'list',
				glyph: 61,
				menu:[{
					text:'设置栏目',
					handler:'setChannel'
				}]
			}*/
			{text:"设置栏目",tooltip:"设置栏目",iconCls:"",handler:'setChannel'}
		]
	}],
    initComponent: function () { 
		//console.log(this);	
		//var _channelId=this.down('combobox');//down('combobox[name=myChannels]').getvalue();//.findParentByType("contentMg");//
		//console.log(_channelId);
		var store = new KitchenSink.view.content.contentMg.contentStore('1');
        Ext.apply(this, {
            columns: [{
                text: '站点编号',
                hidden: true,
                dataIndex: 'siteId'
            },{
                text: '栏目编号',
                hidden: true,
                dataIndex: 'columnId'
            },{
                text: '内容编号',
                hidden: true,
                dataIndex: 'articleId'
            },{
                text: 'classId',
                hidden: true,
                dataIndex: 'classId'
            },{
                text: '文章标题',
                //sortable: true,
                dataIndex: 'articleTitle',
								flex: 1,
								renderer: function(v,c) {
									var tzGetGeneralURL = Ext.tzGetGeneralURL();
									var siteId = c.record.data.siteId;
									var columnId = c.record.data.columnId;
									var artId = c.record.data.articleId;
									var classId = c.record.data.classId;
									
	                var url = encodeURI( tzGetGeneralURL + "?classid=" +classId+ "&operatetype=HTML&siteId=" + siteId + "&columnId=" + columnId + "&artId=" + artId + "&oprate=R");
	                
	                return '<a target="_blank" href="' + url + '">' + v + '</a>';
	            	}
            },{
	            text: "发布时间",
	            dataIndex: 'releaseTime',
	            width: 165,
	            //renderer: Ext.util.Format.dateRenderer('Y/n/j H:i:s'),
	            //sortable: true,
	            align: 'center',
	            groupable: false
	        },{
                text: '最后修改人',
                //sortable: false,
                align: 'center',
                dataIndex: 'lastUpdate',
                width: 160
            },{
	            text: '发布/撤销',
	            dataIndex: 'releaseOrUndo',
	            width: 100,
	            align: 'center',
	            groupable: false,
	            renderer: function(v) {
	                if(v == "Y"){
	                	//return '<a href="javascript:void(0)">撤销</a>';
						return '<img src="data:image/gif;base64,R0lGODlhAQABAID/AMDAwAAAACH5BAEAAAAALAAAAAABAAEAAAICRAEAOw==" class="x-action-col-icon x-action-col-0  revoke" />';
	                }else{
	                	//return '<a href="javascript:void(0)">发布</a>';
						return '<img src="data:image/gif;base64,R0lGODlhAQABAID/AMDAwAAAACH5BAEAAAAALAAAAAABAAEAAAICRAEAOw==" class="x-action-col-icon x-action-col-0  publish" />';
	                }
	            },
	            listeners:{
	            	click:'releaseOrUndo'
	            }
	        },{
	            text: '置顶/撤销',
	            dataIndex: 'topOrUndo',
	            width: 100,
	            align: 'center',
	            groupable: false,
	            renderer: function(v) {
	            	if(v != "0"){
	                	//return '<a href="javascript:void(0)">撤销</a>';
						return '<img src="data:image/gif;base64,R0lGODlhAQABAID/AMDAwAAAACH5BAEAAAAALAAAAAABAAEAAAICRAEAOw==" class="x-action-col-icon x-action-col-0  revoke" />';
	                }else{
	                	//return '<a href="javascript:void(0)">置顶</a>';
						return '<img src="data:image/gif;base64,R0lGODlhAQABAID/AMDAwAAAACH5BAEAAAAALAAAAAABAAEAAAICRAEAOw==" class="x-action-col-icon x-action-col-0  top" />';
	                }
	            },
	            listeners:{
	            	click:'topOrUndo'
	            }
	        },{
	           text: '操作',
               menuDisabled: true,
               sortable: false,
               //align: 'center',
               width:60,
			   			 xtype: 'actioncolumn',
						   items:[{iconCls: 'edit',tooltip: '编辑',handler:'editArt'},
								  {iconCls: 'remove',tooltip: '删除',handler:'deleteArt'}
						   ]
            }],
			store: store,
            bbar: {
                xtype: 'pagingtoolbar',
                pageSize: 50,
                /*
				listeners:{
					afterrender: function(pbar){
						var grid = pbar.findParentByType("grid");
						pbar.setStore(grid.store);
					}
				},*/
				store: store,
				
                displayInfo: true,
				displayMsg: '显示{0}-{1}条，共{2}条',
				beforePageText: '第',
                afterPageText: '页/共{0}页',
				emptyMsg: '没有数据显示',
                plugins: new Ext.ux.ProgressBarPager()
            }	
        });
        this.callParent();
    }
});
