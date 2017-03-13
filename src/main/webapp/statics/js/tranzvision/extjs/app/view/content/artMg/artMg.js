Ext.define('KitchenSink.view.content.artMg.artMg', {
    extend: 'Ext.panel.Panel',
    xtype: 'artMg',
	controller: 'artTreeController',
	requires: [
	    'Ext.data.*',
		'Ext.util.*',
		'Ext.toolbar.Paging',
        'Ext.ux.ProgressBarPager',
	    'KitchenSink.view.content.artMg.artTreeController',
	    'KitchenSink.view.content.artMg.artTreeStore',
		'KitchenSink.view.content.artMg.artStore',
        'Ext.data.TreeStore'
	],
    title: '内容发布管理',
    width: 640,
    layout: 'border',
    viewModel: true,
    frame:true,
    actType: 'update',
    style:"margin:8px",
    initComponent: function() {
		me = this;
		var treeStore = new KitchenSink.view.content.artMg.artTreeStore({siteid: me.siteid});
		var gridStore = new KitchenSink.view.content.artMg.artStore('NONE');
		this.items = [
            {	
                //columnWidth: 0.3,
                //margin: "10 5 0 0",
                title: '内容发布树',
                region:'west',
                xtype: 'treepanel',
                width: 300,
                split: true,
                collapsible: true,
				// height: 400,
                autoScroll : true,
                lines: true,
				rootVisible: true,
				store: treeStore,
	        	listeners : {  
	            	itemclick: "treeItemClick"
	            },
				dockedItems: [
				{
						xtype: 'combobox',
						dock: 'top',
						margin: "2 0 0 0",
						fieldLabel: '站点',
						name:'mySites',
						labelWidth:40,
						labelStyle: 'font-weight:bold',
						style:'background:white;background-image:none',
						queryMode: 'remote',
						width:300,
						editable:false,
						valueField: 'TValue',
						displayField: 'TSDesc',
						listeners: {
							afterrender: function(tvType){
								var siteid = this.siteid;
								var tzParams = '{"ComID":"TZ_ART_MG_COM","PageID":"TZ_ART_LIST_STD","OperateType":"QF","comParams":{"typeFlag":"SITE"}}';
								Ext.tzLoad(tzParams,function(responseData){
									var store = new Ext.data.Store({
										fields: ['TValue', 'TSDesc', 'TLDesc'],
										data:responseData.TransList
									});
									
									tvType.setStore(store);
									if(siteid==undefined){
										if(store.getCount() > 0){
											tvType.setValue(store.getAt(0).get("TValue"));
										}
									}
								});
							},
							change:function(tvType,newValue,oldValue,eOpts){
								var treeStore = new KitchenSink.view.content.artMg.artTreeStore({siteid: newValue});
								tvType.findParentByType("treepanel").setStore(treeStore);
								var gridStore = new KitchenSink.view.content.artMg.artStore('NONE');
								var refs = me.getReferences(),
									dataPanel = refs.artListGridPanel,
									dataGrid = refs.artListGrid;
								dataPanel.columnId = "";
								dataPanel.setTitle("内容发布");
								dataGrid.setStore(gridStore);
								dataGrid.store.load();
								dataGrid.child("pagingtoolbar").setStore(gridStore);
							}
						}
				}]
			}, 
			{
				xtype: 'panel',
                region: 'center', 
                frame: false,
                title: '内容发布',
                reference: 'artListGridPanel',
                layout: 'fit',
                border: false,
                //bodyPadding: 10,
                //height: 400,
                bodyStyle: 'overflow-y:auto;overflow-x:hidden',
                autoScroll : true,
                fieldDefaults: {
                    msgTarget: 'side',
                    labelWidth:120,
                    labelStyle: 'font-weight:bold'
                },                
                items: [
					{
						xtype: 'grid',
						reference: 'artListGrid',
						selModel: {
							type: 'checkboxmodel'
						},
						multiSelect: true,
						viewConfig: {
							enableTextSelection: true
						},
						plugins: {
							ptype: 'cellediting',
							pluginId: 'artListCellEditing'
							//	clicksToEdit: 1
						},
						dockedItems:[{
		                    xtype:"toolbar",
		                    items:[
		                        {text:"查询",tooltip:"查询数据",iconCls:"query",handler:'cfgSearch'},"-",
								{text:"新增",tooltip:"新增数据",iconCls: 'add',handler:'addArt',name:'add'},"-",
								{text:"编辑",tooltip:"编辑数据",iconCls: 'edit',handler:'editSelArt'},"-",
								{text:"删除",tooltip:"删除选中的数据",iconCls:"remove",handler:'deleteSelList'},'-',
								{text:"复制",tooltip:"复制",iconCls:"copy",handler:'copySelList'},'-',
								{text:"发布",tooltip:"发布选中内容",iconCls:"publish",handler:'releaseSelList'},'-',
								{text:"撤销发布",tooltip:"撤销发布选中内容",iconCls:"revoke",handler:'UndoSelList'}
		                        /*
								{xtype:'splitbutton',
		                         text:'更多操作',
		                         iconCls:'list',
		                         glyph: 61,
		                         menu:
		                         [
		                                {
		                                    text:'复制',
		                                    iconCls:"switch ",
		                                    handler:'copy'
		                                }
		                         ]
		                        }*/
		                    ]}],
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
							text: "序列",
							dataIndex: 'artseq',
							width: 70,
							align: 'center',
						},{
							text: "发布时间",
							dataIndex: 'releaseTime',
							width: 145,
							//renderer: Ext.util.Format.dateRenderer('Y/n/j H:i:s'),
							//sortable: true,
							align: 'center',
							groupable: false
						},{
							text: '最后修改人',
							//sortable: false,
							align: 'center',
							dataIndex: 'lastUpdate',
							width: 140
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
							text: "置顶权重",
							dataIndex: 'artZdSeq',
							width: 70,
							align: 'center',
							editor: {
								xtype: 'numberfield'
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
						store: gridStore,
						bbar: {
							xtype: 'pagingtoolbar',
							pageSize: 50,
							store: gridStore,
							displayInfo: true,
							displayMsg: '显示{0}-{1}条，共{2}条',
							beforePageText: '第',
							afterPageText: '页/共{0}页',
							emptyMsg: '没有数据显示',
							plugins: new Ext.ux.ProgressBarPager()
						}
					}
                ]
            }
        ];
        this.callParent();
    },
    listeners : {
        afterrender: function( panel ){
			/*
        	var thisTree = panel.child("treepanel");
			var treeStore = thisTree.getStore();
	        var rootNode = treeStore.getNodeById( me.menuId );
	        thisTree.getSelectionModel().select(rootNode);
 			 
	        var form = panel.child("form").getForm();
	        form.setValues({
	            menuId: rootNode.data.id,
							menuName: rootNode.data.text,
							menuYxState: rootNode.data.menuYxState,
							comId: rootNode.data.comId,
							bigImgId: rootNode.data.bigImgId,
							smallImgId: rootNode.data.smallImgId,
							helpId: rootNode.data.helpId,
							NodeType: rootNode.data.NodeType,
							operateNode: rootNode.data.operateNode,
							rootNode: me.menuId,
							comName: rootNode.data.comName
	         });
	         form.findField("menuId").setReadOnly(true);
             form.findField("menuId").addCls('lanage_1');
			 */
        }
    },
    buttons: [{
			text: '保存',
			iconCls:"save",
			handler: 'saveContentList'
		}, {
			text: '确定',
			iconCls:"ensure",
			handler: 'saveContentList'
		}, {
			text: '关闭',
			iconCls:"close",
			handler: 'onPageClose'
		}],
		constructor: function (siteid) {
			//站点信息;
			this.siteid = siteid; 
			this.callParent();
		}
});
