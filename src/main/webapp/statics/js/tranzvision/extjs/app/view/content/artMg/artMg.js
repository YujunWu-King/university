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
    actType: 'update',
    initComponent: function() {
		me = this;
		var treeStore = new KitchenSink.view.content.artMg.artTreeStore({siteId: me.siteId});
		var gridStore = new KitchenSink.view.content.artMg.artStore('1','1');
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
	            } 
			}, 
			{
				xtype: 'panel',
                region: 'center', 
                frame: true,
                title: '内容发布',
                reference: 'artListGridPanel',
                layout: {
                    type: 'vbox',
                    align: 'stretch'
                },
                border: false,
                bodyPadding: 10,
                //height: 400,
                bodyStyle: 'overflow-y:auto;overflow-x:hidden',

                fieldDefaults: {
                    msgTarget: 'side',
                    labelWidth:120,
                    labelStyle: 'font-weight:bold'
                },

                items: [{
                    xtype:"toolbar",
                    items:[
                        {text:"查询",tooltip:"查询数据",iconCls:"query",handler:'cfgSearch'},"-",
						{text:"新增",tooltip:"新增数据",iconCls: 'add',handler:'addArt'},"-",
						{text:"编辑",tooltip:"编辑数据",iconCls: 'edit',handler:'editSelArt'},"-",
						{text:"删除",tooltip:"删除选中的数据",iconCls:"remove",handler:'deleteSelList'},'-',
						{text:"发布",tooltip:"发布选中内容",iconCls:"publish",handler:'releaseSelList'},'-',
						{text:"撤销发布",tooltip:"撤销发布选中内容",iconCls:"revoke",handler:'UndoSelList'},'->',
                        {xtype:'splitbutton',
                         text:'更多操作',
                         iconCls:'list',
                         glyph: 61,
                         menu:
                         [
                                {
                                    text:'复制',
                                    iconCls:"switch ",
                                    handler:'changeLanguage'
                                },{
                                    text:'引用',
                                    iconCls:"sync",
                                    handler:'synchrLanguage'
                                }
                         ]
                        }
                    ]},
					{
						xtype: 'grid',
						reference: 'artListGrid',
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
			handler: 'onFormSave'
		}, {
			text: '确定',
			iconCls:"ensure",
			handler: 'onFormEnsure'
		}, {
			text: '关闭',
			iconCls:"close",
			handler: 'onFormClose'
		}],
		constructor: function (config) {
			//机构主菜单ID;
			//this.menuId = config.menuId;

			this.callParent();
		}
});
