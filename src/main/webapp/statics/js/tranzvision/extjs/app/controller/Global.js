Ext.define('KitchenSink.controller.Global', {
    extend: 'Ext.app.Controller',
    requires: [
        'KitchenSink.view.*',
        'KitchenSink.view.thumbnails.Thumbnails',
        'KitchenSink.store.Thumbnails',
        'KitchenSink.store.Navigation',
        'KitchenSink.view.common.store.appTransStore',//translateValue使用
        'KitchenSink.view.common.store.comboxStore',//下拉框
        //'KitchenSink.view.orgmgmt.initOrgInfo',
        //'KitchenSink.view.orgmgmt.orgJgInfo',
        'Ext.window.*'
    ],

    stores: [
        'Thumbnails'
        //'Companies'
    ],

    privateStore: null,

    config: {
        control: {
            'navigation-tree': {
                selectionchange: 'onTreeNavSelectionChange',
                itemclick: 'onTreeNavItemClick'
            },
            'navigation-breadcrumb breadcrumb': {
                //selectionchange: 'onBreadcrumbNavSelectionChange'
                breadcrumbmenuclick: 'onBreadcrumbNavMenuClick'
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

    constructor: function(config)
    {
      this.callParent(arguments);
      TranzvisionMeikecityAdvanced.Boot.globalController = this;
    },

    beforeHandleRoute: function(id, action)
    {
        var me = this;


        if(me.privateStore == undefined)
        { 
          me.privateStore = new KitchenSink.store.Navigation({storeId:'private-content-navigation-store',returnMenu:true});
        }


        var rNode = me.privateStore.getNodeById(id);
        //如果是返回上级菜单节点，则返回上级菜单
        if(rNode != undefined && rNode.get("returnMenu") == true)
        {
          if(rNode.parentNode && rNode.parentNode.parentNode)
          {
            me.redirectToByToken(rNode.parentNode.parentNode.getId());
          }

          return;
        }


        var node = Ext.StoreMgr.get('navigation').getNodeById(id);

        if(node)
        {
            //resume action
            action.resume();
        } else {
             Ext.Msg.alert(
                TranzvisionMeikecityAdvanced.Boot.getMessage('TZGD_FWINIT_00014'),
                TranzvisionMeikecityAdvanced.Boot.languagePackage['tz-frmwrk-lang-00011'],
                function() {
                    me.redirectToByToken(me.getApplication().getDefaultToken());
                }
            );

            //stop action
            action.stop();
        }
    },

    expandToNode: function(id)
    {
      var me = this;
      var navigationTree = me.getNavigationTree();
      var store = Ext.StoreMgr.get('navigation');

      if(navigationTree != undefined && store != undefined)
      {
        var node = store.getNodeById(id);

        if(node != undefined)
        {
          me.private_expandToNode(navigationTree,node);
        } 
      }
    },

    private_expandToNode: function(tree,node)
    {
      if(tree == undefined || node == undefined)
      {
        return;
      }

      var me = this;
      me.private_expandToNode(tree,node.parentNode);

      if(node.isExpandable() == true)
      {
        if(node.isExpanded() == false)
        {
          tree.expandNode(node);
        } 
      }
    },

    redirectToByToken: function(token)
    {
      var me = this;
      me.expandToNode(token);
 
      var currentToken = Ext.util.History.getToken();

      if(currentToken === token)
      {
        Ext.app.route.Router.onStateChange(token);
      }
      else
      {
        Ext.util.History.add(token);
      }

      return true;
    },

    handleRoute: function(id)
    {
      var me = this;


      if(me.privateStore == undefined)
      { 
        me.privateStore = new KitchenSink.store.Navigation({storeId:'private-content-navigation-store',returnMenu:true});
      }


      var rNode = me.privateStore.getNodeById(id);
      //如果是返回上级菜单节点，则返回上级菜单
      if(rNode != undefined && rNode.get("returnMenu") == true)
      {
        if(rNode.parentNode && rNode.parentNode.parentNode)
        {
          me.redirectToByToken(rNode.parentNode.parentNode.getId());
        }

        return;
      }


      var store = Ext.StoreMgr.get('navigation');
      var node = store.getNodeById(id);

      if(node.isLeaf())
      {
        //2015-03-17--------ZDQ添加---获取组件资源
        var comID = node.get("comID");
        if(comID == "" || comID == undefined)
        {
          try
          {
            var className = Ext.ClassManager.getNameByAlias('widget.' + id);
            if(className === "" || className === undefined)
            {
              className = node.get("className");
            }

            var ViewClass = Ext.ClassManager.get(className);
            var clsProto = ViewClass.prototype;

            me.private_handleRoute(id);

            return;
          }
          catch(e)
          {
            TranzvisionMeikecityAdvanced.Boot.errorMessage(TranzvisionMeikecityAdvanced.Boot.languagePackage['tz-frmwrk-lang-00009']);
            if(node.parentNode)
            {
              me.redirectToByToken(node.parentNode.getId());
            }
            else
            {
              me.redirectToByToken('all');
            }
          }
        }
        else
        {
          //等待信息
          //Ext.MessageBox.wait("加载中", "请稍候");
          var myMask = new Ext.LoadMask(
          {
            msg    : TranzvisionMeikecityAdvanced.Boot.getMessage("TZGD_FWINIT_00022"),
            target : Ext.getCmp('tranzvision-framework-content-panel')
          });

          //如果已打开该功能菜单则不显示mask；
          if( me.getContentPanel().getComponent(id)==null){
              myMask.show();
          };
          
          Ext.defer
          (
            function(winObject,handerObject,comID,id)
            {
              if(Ext.tzSetCompResourses(comID) == true)
              {
                handerObject.private_handleRoute(id);
              }
              winObject.hide();
            },
            10,
            this,
            [myMask,me,comID,id]
          );
        }
      }
      else
      {
        me.private_handleRoute(id);
      }
    },

    private_handleRoute: function(id)
    {
        var me = this;
        var navigationTree = me.getNavigationTree(),
            navigationBreadcrumb = me.getNavigationBreadcrumb(),
            store = Ext.StoreMgr.get('navigation'),
            node = store.getNodeById(id),
            contentPanel = me.getContentPanel(),
            themeName = Ext.themeName,
            thumbnails = me.getThumbnails(),
            codePreview = me.getCodePreview(),
            hasTree = navigationTree && navigationTree.isVisible(),
            cmp, className, ViewClass, clsProto, thumbnailsStore;

        if (node.isLeaf())
        {
          try
          {
            className = Ext.ClassManager.getNameByAlias('widget.' + id); 
            if(className === "" || className === undefined)
            {
              className = node.get("className");
            }

            ViewClass = Ext.ClassManager.get(className);
            clsProto = ViewClass.prototype;
          }
          catch(e)
          {
            TranzvisionMeikecityAdvanced.Boot.errorMessage(TranzvisionMeikecityAdvanced.Boot.languagePackage['tz-frmwrk-lang-00009']);
            return;
          }
        }

        Ext.suspendLayouts();

        if (node.isLeaf())
        {
            /*if (thumbnails.ownerCt) {
                contentPanel.remove(thumbnails, false); // remove thumbnail view without destroying
            } else {
                contentPanel.removeAll(true);
            }*/
            
            if (hasTree)
            {
                // Focusing explicitly brings it into rendered range, so do that first.
                if(node.isVisible == true)
                {
                  try
                  {
                    navigationTree.getView().focusNode(node);
                  }
                  catch(e)
                  {}
                }
                navigationTree.getSelectionModel().select(node);
            }
            navigationBreadcrumb.private_setSelection(node);

            contentPanel.body.addCls('kitchensink-example');

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

             /*
            *开始
            *功能描述：如果当前菜单没有对应的新开窗口，则新开一个页签窗口；
            *如果当前菜单已打开功能窗口，则只要将当前菜单对应功能窗口激活显示到前端即可
            * 修改人：叶少威
            * 时间：2015年12月22日 17:41:27
            * */
            var tmpCmpTab = contentPanel.getComponent(id);
            var tab;
            if(tmpCmpTab==null){
                cmp = new ViewClass();
                cmp.currentNodeId = id;
                cmp.itemId = id;

                tab = contentPanel.add(cmp);
                tab.on(Ext.tzTabOn(tab,cmp,cmp,me));
            }else{
                tab = cmp = tmpCmpTab;
            }
            /*结束*/

            contentPanel.setActiveTab(tab);
			 
            this.setupPreview(clsProto);

            this.updateTitle(node);
			
            //2015-03-06-禁止出现滚动条
            contentPanel.setOverflowXY("hidden","hidden");

            if (cmp.floating) {
                cmp.show();
            }
        }

        var tmpId = id;
        if(node.isLeaf() == true)
        {
          if(node.parentNode != undefined && node.parentNode != null)
          {
            tmpId = node.parentNode.getId();
          }
          else
          {
            tmpId = 'all'; 
          }
        }

        var tmpMenuNodeId = "";
        var tmpMenuTab = null;
        var tmpNode = store.getNodeById(tmpId);
        if(thumbnails.ownerCt == undefined || thumbnails.ownerCt == null || node.isLeaf() == false)
        {
          // Only attempt to select the node if the tree is visible
          if (hasTree)
          {
            if(tmpId !== 'all' && node.isLeaf() == false)
            {
              // If the node is the root (rootVisible is false), then
              // Focus the first node in the tree.
              var tmpNode1 = tmpNode.isRoot() ? store.getAt(0) : tmpNode;
              if(tmpNode1.isVisible() == true)
              {
                try
                {
                  navigationTree.ensureVisible(tmpNode1,{select: true,focus: true});
                }
                catch(e)
                {}
              }
            }
            else
            {
              navigationTree.getSelectionModel().select(tmpNode);
            }
          }
          // Ensure that non-leaf nodes are still highlighted and focused.
          if(node.isLeaf() == false)
          {
            navigationBreadcrumb.setSelection(tmpNode);
          }

          var subMenuPanel = null;
          if (!thumbnails.ownerCt)
          {
            subMenuPanel = new Ext.panel.Panel({
              items:	[thumbnails],
              id :"tranzvision-framework-subMenu-panel",
              title:TranzvisionMeikecityAdvanced.Boot.languagePackage['tz-frmwrk-lang-00010'],
              closable:false
            });
            subMenuPanel.currentNodeId = tmpId;

            tmpMenuTab = contentPanel.insert(0,subMenuPanel);
            tmpMenuTab.on(Ext.tzTabOn(tmpMenuTab,subMenuPanel,subMenuPanel,me));

            if(node.isLeaf() == false)
            {
              contentPanel.setActiveTab(tmpMenuTab);
            }
          }
          else
          {
            if(node.isLeaf() == false)
            {
              tmpMenuTab = contentPanel.getComponent('tranzvision-framework-subMenu-panel');
              tmpMenuNodeId = tmpMenuTab.currentNodeId;
              tmpMenuTab.currentNodeId = tmpId;
              contentPanel.setActiveTab(tmpMenuTab);
            }
          }

          if(node.isLeaf() == false)
          {
            contentPanel.body.removeCls('kitchensink-example');
            codePreview.removeAll();
            codePreview.add({html: tmpNode.get('description') || ''});
            codePreview.tabBar.hide();
            this.updateTitle(tmpNode);
          }
        }
        else
        {
          tmpMenuTab = contentPanel.getComponent('tranzvision-framework-subMenu-panel');
          tmpMenuNodeId = tmpMenuTab.currentNodeId;
          tmpMenuTab.currentNodeId = tmpId;
        }

        if(tmpMenuNodeId != tmpId && tmpNode != undefined && tmpNode != null)
        {
          thumbnailsStore = me.getThumbnailsStore();
          thumbnailsStore.removeAll();
          thumbnailsStore.add(me.privateStore.getNodeById(tmpNode.getId()).childNodes);
        }

        Ext.resumeLayouts(true);
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
        var me = this;

        var record = records[0];
        if(record != undefined)
        {
          me.expandToNode(record.getId());
        }
    },

    onTreeNavItemClick: function(selModel, record) {
        var navigationTree = this.getNavigationTree();
        var navigationTreeFlag = navigationTree && navigationTree.isVisible();

        navigationTreeFlag = (navigationTreeFlag == undefined ? false : navigationTreeFlag);

        if(navigationTreeFlag == true)
        {
          if (record) {
              this.redirectToByToken(record.getId());
          }
        }
    },

    onBreadcrumbNavSelectionChange: function(breadcrumb, node) {
        var navigationTree = this.getNavigationTree();
        var navigationTreeFlag = navigationTree && navigationTree.isVisible();

        navigationTreeFlag = (navigationTreeFlag == undefined ? false : navigationTreeFlag);

        if (node && navigationTreeFlag == false) {
            this.redirectToByToken(node.getId());
        }
    },

    onBreadcrumbNavMenuClick: function(breadcrumb, node) {
        var navigationTree = this.getNavigationTree();
        var navigationTreeFlag = navigationTree && navigationTree.isVisible();

        navigationTreeFlag = (navigationTreeFlag == undefined ? false : navigationTreeFlag);

        if (node && navigationTreeFlag == false) {
            this.redirectToByToken(node.getId());
        }
    },

    onThumbnailClick: function(view, node, item, index, e)
    {
        var me = this;


        //如果是返回上级菜单节点，则返回上级菜单
        if(node != undefined && node.get("returnMenu") == true)
        {
          if(node.parentNode && node.parentNode.parentNode)
          {
            me.redirectToByToken(node.parentNode.parentNode.getId());
          }

          return;
        }


        var navigationTree = me.getNavigationTree();
        var navigationTreeFlag = navigationTree && navigationTree.isVisible();
        var tNode = Ext.StoreMgr.get('navigation').getNodeById(node.getId());

        navigationTreeFlag = (navigationTreeFlag == undefined ? false : navigationTreeFlag);

        // If not using breadcrumb nav, drive the app through the tree's normal selection listener
        // This ensures that the tree is scrolled correctly with the correct node highlighted.
        if (navigationTreeFlag == true && tNode.isVisible() == true)
        {
          try
          {
            navigationTree.ensureVisible(tNode, {
                select: true,
                focus: true
            });
          }
          catch(e)
          {}
        }

        me.redirectToByToken(tNode.getId());
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
		//组件注册信息集合
		var comRegResourseSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet[comID];
		var loadOkFlag = false;

		if(comRegResourseSet == undefined)
		{
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
					  if(jsonObject.state.errcode === 0)
					  {
						  var mainContent = jsonObject.comContent;
						  
						  //默认首页页面ID
						  var defaultPageID = mainContent.defaultPageID;
						  //组件资源
						  var comResources = mainContent.comResources;
						  //组件标签资源集合
						  var cResourses = {};
						  //组件页面注册信息集合
						  var cPResources = {};
						  for(var i=0;i<comResources.length;i++)
						  {
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

						  //默认页面的JS类
						  Ext.syncRequire(cPResources[defaultPageID]["jsClassName"]);

						  TranzvisionMeikecityAdvanced.Boot.setDescResourseSet(comID,cResourses);	
						  TranzvisionMeikecityAdvanced.Boot.comRegResourseSet[comID] = cPResources;

						  loadOkFlag = true;
					  }
			      else if(jsonObject.state.timeout == true)
			      {
			        try
			        {
			          if(Ext.getCmp("tranzvision_relogin_20150626") == undefined)
			          {
			            var win = new tranzvision.view.window.ReloginWindow();
			            win.show();
			          }
			        }
			        catch(e2)
			        {}
			      }
					  else
					  {
						  TranzvisionMeikecityAdvanced.Boot.errorMessage(jsonObject.state.errdesc);
					  }
					}
					catch(e)
					{
						TranzvisionMeikecityAdvanced.Boot.errorMessage(e.toString());
					}
				},
				failure: function(response, opts)
				{
					//错误信息响应报文
					var respText = Ext.util.JSON.decode(response.responseText);
					TranzvisionMeikecityAdvanced.Boot.errorMessage(respText.error);
				}
			});
		}
		else
		{
		  loadOkFlag = true;
		}
	}
});