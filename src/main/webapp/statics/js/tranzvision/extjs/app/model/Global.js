projectMgExt.define('KitchenSink.controller.Global', {
    extend: 'Ext.app.Controller',
    requires: [
        'KitchenSink.view.*',
        'KitchenSink.view.thumbnails.Thumbnails',
        'KitchenSink.store.Thumbnails',
		'KitchenSink.view.common.store.appTransStore',//translateValue使用
        'Ext.window.*'
    ],

    stores: [
        'Thumbnails',
        'Companies'
    ],

    config: {
        control: {
            'navigation-tree': {
                selectionchange: 'onTreeNavSelectionChange'
            },
            'navigation-breadcrumb breadcrumb': {
                selectionchange: 'onBreadcrumbNavSelectionChange'
            },
            'thumbnails': {
                itemclick: 'onThumbnailClick',
                itemdblclick: 'onThumbnailClick'
            },
            '#tranzvision-framework-codePreview tool[type=maximize]': {
                click: 'onMaximizeClick'
            },
            'tool[regionTool]': {
                click: 'onSetRegion'
            }
        },
        refs: {
            viewport: 'viewport',
            navigationTree: 'navigation-tree',
            navigationBreadcrumb: 'navigation-breadcrumb',
            contentPanel: 'contentPanel',
            descriptionPanel: 'descriptionPanel',
            codePreview: '#tranzvision-framework-codePreview',
            thumbnails: {
                selector: 'thumbnails',
                xtype: 'thumbnails',
                autoCreate: true
            }
        },
        routes  : {
            ':id': {
                action: 'handleRoute',
                before: 'beforeHandleRoute'
            }
        }
    },

    beforeHandleRoute: function(id, action) {
        var me = this,
            node = Ext.StoreMgr.get('navigation').getNodeById(id);

        if (node) {
            //resume action
            action.resume();
        } else {
            Ext.Msg.alert(
                'Route Failure',
                'The view for ' + id + ' could not be found. You will be taken to the application\'s start',
                function() {
                    me.redirectTo(me.getApplication().getDefaultToken());
                }
            );

            //stop action
            action.stop();
        }
    },

    handleRoute: function(id) {
        var me = this,
            navigationTree = me.getNavigationTree(),
            navigationBreadcrumb = me.getNavigationBreadcrumb(),
            store = Ext.StoreMgr.get('navigation'),
            node = store.getNodeById(id),
            contentPanel = me.getContentPanel(),
            themeName = Ext.themeName,
            thumbnails = me.getThumbnails(),
            codePreview = me.getCodePreview(),
            hasTree = navigationTree && navigationTree.isVisible(),
            cmp, className, ViewClass, clsProto, thumbnailsStore;

        Ext.suspendLayouts();

        if (node.isLeaf()) {
            /*if (thumbnails.ownerCt) {
                contentPanel.remove(thumbnails, false); // remove thumbnail view without destroying
            } else {
                contentPanel.removeAll(true);
            }*/
            
            if (hasTree) {
                // Focusing explicitly brings it into rendered range, so do that first.
                navigationTree.getView().focusNode(node);
                navigationTree.getSelectionModel().select(node);
            } else {
                navigationBreadcrumb.setSelection(node);
            }

            contentPanel.body.addCls('kitchensink-example');

            //2015-03-17--------ZDQ添加---获取组件资源
			var comID = node.get("comID");	
			if(comID == "" || comID == undefined){
				
			}else{
				//me.getCompResourses(comID);
				Ext.tzSetCompResourses(comID);
			}
			
            className = Ext.ClassManager.getNameByAlias('widget.' + id);
            ViewClass = Ext.ClassManager.get(className);
            clsProto = ViewClass.prototype;

            if (clsProto.themes) {
                clsProto.themeInfo = clsProto.themes[themeName];

                if (themeName === 'gray') {
                    clsProto.themeInfo = Ext.applyIf(clsProto.themeInfo || {}, clsProto.themes.classic);
                } else if (themeName !== 'neptune' && themeName !== 'classic') {
                    if (themeName === 'crisp-touch') {
                        clsProto.themeInfo = Ext.applyIf(clsProto.themeInfo || {}, clsProto.themes['neptune-touch']);
                    }
                    clsProto.themeInfo = Ext.applyIf(clsProto.themeInfo || {}, clsProto.themes.neptune);
                }
                // <debug warn>
                // Sometimes we forget to include allowances for other themes, so issue a warning as a reminder.
                if (!clsProto.themeInfo) {
                    Ext.log.warn ( 'Example \'' + className + '\' lacks a theme specification for the selected theme: \'' +
                        themeName + '\'. Is this intentional?');
                }
                // </debug>
            }

            cmp = new ViewClass();
            
            tab = contentPanel.add(cmp);     
			
            contentPanel.setActiveTab(tab);
			 
            this.setupPreview(clsProto);

            this.updateTitle(node);
			
            //2015-03-06-禁止出现滚动条
            contentPanel.setOverflowXY("hidden","hidden");
			
            Ext.resumeLayouts(true);

            if (cmp.floating) {
                cmp.show();
            }
        } else {
            // Only attempt to select the node if the tree is visible
            if (hasTree) {
                if (id !== 'all') {
                    // If the node is the root (rootVisible is false), then
                    // Focus the first node in the tree.
                    navigationTree.ensureVisible(node.isRoot() ? store.getAt(0) : node, {
                        select: true,
                        focus: true
                    });
                }
            }
            // Ensure that non-leaf nodes are still highlighted and focused.
            else {
                navigationBreadcrumb.setSelection(node);
            }
            thumbnailsStore = me.getThumbnailsStore();
            thumbnailsStore.removeAll();
            thumbnailsStore.add(node.childNodes);
            if (!thumbnails.ownerCt) {
				subMenuPanel = new Ext.panel.Panel({
							items:	[thumbnails],
							id :"tranzvision-framework-subMenu-panel",
							title:"子菜单内容"
						 });
				 tab = contentPanel.add(subMenuPanel);  
                 contentPanel.setActiveTab(tab);
                 //contentPanel.removeAll(true);
            }else{
				contentPanel.setActiveTab('tranzvision-framework-subMenu-panel');
			}
			
            contentPanel.body.removeCls('kitchensink-example');
            //contentPanel.add(thumbnails);
            codePreview.removeAll();
            codePreview.add({
                html: node.get('description') || ''
            });
            codePreview.tabBar.hide();
            this.updateTitle(node);
            Ext.resumeLayouts(true);
        }
    },
    
    updateTitle: function(node) {
        var text = node.get('text'),
            title = node.isLeaf() ? (node.parentNode.get('text') + ' - ' + text) : text;

        title = title === TranzvisionMeikecityAdvanced.Boot.languagePackage['tz-frmwrk-lang-00001'] ? TranzvisionMeikecityAdvanced.Boot.languagePackage['tz-frmwrk-lang-00003'] : title;
        this.getContentPanel().setTitle(title);

        document.title = document.title.split(' - ')[0] + ' - ' + text;
    },

    setupPreview: function(clsProto) {
        var me = this,
            preview = me.getCodePreview(),
            otherContent = clsProto.otherContent,
            resources = [],
            codePreviewProcessed = clsProto.codePreviewProcessed;

        if (!codePreviewProcessed) {
            resources.push({
                type: 'View',
                path: clsProto.$className.replace(/\./g, '/').replace('KitchenSink', TzUniversityContextPath + '/statics/js/tranzvision/extjs/app') + '.js'
            });

            if (otherContent) {
                resources = resources.concat(otherContent);
            }

            // Clone everything, since we're about to hook up loaders
            codePreviewProcessed = clsProto.codePreviewProcessed = [];
            Ext.each(resources, function(resource) {
                resource.xtype = 'codeContent';
                resource.rtl = false;
                resource.title = resource.type;
                //resource.tabConfig = {
                //    tooltip: resource.path
                //};
                var clone = Ext.apply({}, resource);
                codePreviewProcessed.push(clone);
                resource.loader = {
                    url: resource.path,
                    autoLoad: true,
                    rendererScope: me,
                    renderer: me.renderCodeMarkup,
                    resource: clone,
                    themeInfo: clsProto.themeInfo
                };
            });
        } else {
            resources = codePreviewProcessed;
        }

        preview.removeAll();

        preview.add(resources);
        preview.setActiveTab(0);

        // Hide the Tab Panel if there's only one resource
        preview.tabBar.setVisible(resources.length > 1);

        preview.activeView = clsProto;
    },

    exampleRe: /^\s*\/\/\s*(\<\/?example\>)\s*$/,
    themeInfoRe: /this\.themeInfo\.(\w+)/g,

    renderCodeMarkup: function(loader, response) {
        var code = this.processText(response.responseText, loader.themeInfo);
        // Passed in from the block above, we keep the proto cloned copy.
        loader.resource.html = code;
        loader.getTarget().setHtml(code);
        prettyPrint();
        return true;
    },

    processText: function (text, themeInfo) {
        var lines = text.split('\n'),
            removing = false,
            keepLines = [],
            len = lines.length,
            exampleRe = this.exampleRe,
            themeInfoRe = this.themeInfoRe,
            encodeTheme = function (text, match) {
                return Ext.encode(themeInfo[match]);
            },
            i, line, code;

        for (i = 0; i < len; ++i) {
            line = lines[i];
            if (removing) {
                if (exampleRe.test(line)) {
                    removing = false;
                }
            } else if (exampleRe.test(line)) {
                removing = true;
            } else {
                // Replace "this.themeInfo.foo" with the value of "foo" properly encoded
                // for JavaScript (otherwise strings would not be quoted).
                line = line.replace(themeInfoRe, encodeTheme);
                keepLines.push(line);
            }
        }

        code = Ext.htmlEncode(keepLines.join('\n'));
        return '<pre class="prettyprint">' + code + '</pre>';
    },

    onSetRegion: function (tool) {
        var panel = tool.toolOwner;

        var regionMenu = panel.regionMenu || (panel.regionMenu =
            Ext.widget({
                xtype: 'menu',
                items: [{
                    text: 'North',
                    checked: panel.region === 'north',
                    group: 'mainregion',
                    handler: function () {
                        panel.setBorderRegion('north');
                    }
                },{
                    text: 'South',
                    checked: panel.region === 'south',
                    group: 'mainregion',
                    handler: function () {
                        panel.setBorderRegion('south');
                    }
                },{
                    text: 'East',
                    checked: panel.region === 'east',
                    group: 'mainregion',
                    handler: function () {
                        panel.setBorderRegion('east');
                    }
                },{
                    text: 'West',
                    checked: panel.region === 'west',
                    group: 'mainregion',
                    handler: function () {
                        panel.setBorderRegion('west');
                    }
                }]
            }));

        regionMenu.showBy(tool.el);
    },

    onTreeNavSelectionChange: function(selModel, records) {
        var record = records[0];

        if (record) {
            this.redirectTo(record.getId());
        }
    },

    onBreadcrumbNavSelectionChange: function(breadcrumb, node) {
        if (node) {
            this.redirectTo(node.getId());
        }
    },

    onThumbnailClick: function(view, node, item, index, e) {
        var navigationTree = this.getNavigationTree();

        // If not using breadcrumb nav, drive the app through the tree's normal selection listener
        // This ensures that the tree is scrolled correctly with the correct node highlighted.
        if (navigationTree && navigationTree.isVisible()) {
            navigationTree.ensureVisible(node, {
                select: true,
                focus: true
            });
        } else {
            this.redirectTo(node.getId());
        }
    },

    onMaximizeClick: function(){
        var preview = this.getCodePreview();

        var w = new Ext.window.Window({
            rtl: false,
            maximized: true,
            title: TranzvisionMeikecityAdvanced.Boot.languagePackage['tz-frmwrk-lang-00004'],
            closable: false,
            layout: 'fit',
            items: {
                xtype: 'codePreview',
                tools: [],
                showTitle: false,
                items: preview.activeView.codePreviewProcessed
            },
            tools: [{
                type: 'close',
                handler: function() {
                    w.hide(preview, function() {
                        w.destroy();
                    });
                }
            }]
        });
        w.show(preview);
    },
	//获取组件资源
	getCompResourses:function(comID){
		if(comID == "" || comID == undefined){
			return;	
		}
		//组件注册信息集合
		var comRegResourseSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet[comID];		
		if(comRegResourseSet == undefined){
			var tzParams = '{"OperateType":"ZJZY","ComID":"'+comID+'"}';
			Ext.Ajax.request({
				url: Ext.tzGetGeneralURL,
				async: false,
				params: {
					tzParams: tzParams
				},
				success: function(response)
				{
					var jsonText = response.responseText;	
					try
					{
					  var jsonObject = Ext.util.JSON.decode(jsonText);				      
					  /*判断服务器是否返回了正确的信息*/
					  if(jsonObject.state.errcode === 0){
						  var mainContent = jsonObject.comContent;
						  
						  //默认首页页面ID
						  var defaultPageID = mainContent.defaultPageID;
						  //组件资源
						  var comResources = mainContent.comResources;
						  //组件标签资源集合
						  var cResourses = {};
						  //组件页面注册信息集合
						  var cPResources = {};
						  for(var i=0;i<comResources.length;i++){
							  //页面注册信息
							  var pRegResources = {};
							  //页面ID
							  var pageID = comResources[i].pageID;						  
							  //是否外部链接
							  var isExnalUrl = comResources[i].isexternalURL;
							  pRegResources["isexternalURL"] = isExnalUrl;
							  //外部url
							  var exnalUrl = comResources[i].externalURL;
							  pRegResources["externalURL"] = exnalUrl;
							  //是否新开窗口
							  var isNewWin = comResources[i].isNewWin;
							  pRegResources["isNewWin"] = isNewWin;
							  //客户端处理JS类
							  var jsClass = comResources[i].jsClassName;
							  pRegResources["jsClassName"] = jsClass;
							  //组件页面注册信息集合;
							  cPResources[pageID] = pRegResources;						  
							  //标签资源
							  var tagResources = comResources[i].tagResources;
							  //页面资源
							  var pResources = {};
							  for(var j=0;j<tagResources.length;j++){
								  //标签ID
								  var tagID = tagResources[j].tagID;
								  //标签名称
								  var tagName = tagResources[j].tagName;
								  pResources[tagID] = tagName;
							  }	
							  cResourses[pageID] = pResources;
						  }							  
						  TranzvisionMeikecityAdvanced.Boot.setDescResourseSet(comID,cResourses);	
						  TranzvisionMeikecityAdvanced.Boot.comRegResourseSet[comID] = cPResources;
						  //默认页面的JS类
						  Ext.syncRequire(cPResources[defaultPageID]["jsClassName"]);
					  }
					  else{
						  TranzvisionMeikecityAdvanced.Boot.errorMessage(jsonObject.state.errdesc);
					  }
					}
					catch(e){
						TranzvisionMeikecityAdvanced.Boot.errorMessage(e.toString());
					}
				},
				failure: function(response, opts){
					//错误信息响应报文
					var respText = Ext.util.JSON.decode(response.responseText);
					TranzvisionMeikecityAdvanced.Boot.errorMessage(respText.error);
				}
			});
		}
	}
});
