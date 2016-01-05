/*
 创景咨询教育行业高端产品（Meikecity Advanced） 1.0.0.0

 Copyright (c) 2009-2015 Tranzvision
 */
/**
 * 该程序用于引导加载美课城高端产品主程序
 */
/*创建美课城高端产品主程序加载器对象*/
var TranzvisionMeikecityAdvanced = TranzvisionMeikecityAdvanced || {};


/*创建美课城高端产品主程序加载器主体程序，启动执行*/
TranzvisionMeikecityAdvanced.Boot = TranzvisionMeikecityAdvanced.Boot || (function()
{
    var boot =
    {
        /*保存当前网页对应的文档对象*/
        doc : document,
        mainapp_interaction_url : tz_gdcp_interaction_url_20150612184830,
        //mainapp_cfg_url : '/tranzvision/main/mainapp_config.json',
        //mainmenu_cfg_url : '/tranzvision/main/mainmenu_config.json',
        language : tz_gdcp_language_cd_20150612184830 || 'ZHS',
        languagePackage : {},
        mainMenu : {},
        descResourseSet : {},
        comRegResourseSet:{},
        loginUserId: "",
        orgID: "",
        firstName:"",
        globalController: undefined,
        error :
        {
            failed : false,
            description : ''
        },
        theme : tz_gdcp_theme_id_20150612184830 || 'tranzvision',
        errdesc : tz_gdcp_frmwrk_init_msgset_20150612184830,

        getMessage: function(msgID)
        {
            return this.errdesc[this.getLanguage()][msgID];
        },

        getTitle: function()
        {
            return  this.getMessage("TZGD_FWINIT_00003");
        },
        getUserinfo:function()
        {
            return this.getMessage("TZGD_FWINIT_00024") + " " + this.firstName;
        },

        getTheme: function()
        {
            var theme = location.href.match(/theme=([\w-]+)/);

            return  this.theme || (theme && theme[1]) || 'tranzvision';
        },

        getLanguage: function()
        {
            var language = location.href.match(/language=([\w-]+)/);

            return  (this.language || (language && language[1]) || 'ZHS').toUpperCase();
        },

        errorMessage: function(err)
        {
            /*Ext.Msg.show(
             {
             title: this.getMessage("TZGD_FWINIT_00001"),
             message: err,
             modal: true,
             buttons: Ext.Msg.OK,
             icon: Ext.Msg.ERROR
             });*/
            Ext.MessageBox.show(
                {
                    title: this.getMessage("TZGD_FWINIT_00001"),
                    //width: 500,
                    //height:160,
                    //y: 200,
                    msg: err,
                    buttons: Ext.Msg.OK,
                    icon: Ext.Msg.ERROR
                });
        },

        init: function()
        {
            this.language = this.getLanguage();
            this.theme = this.getTheme();

            Ext.beforeLoad = function(tags)
            {
                var theme = location.href.match(/theme=([\w-]+)/),
                    language = location.href.match(/language=([\w-]+)/);

                theme  = (theme && theme[1]) || (tags.desktop ? 'tranzvision' : 'yenching');
                language = ((language && language[1]) || 'ZHS').toUpperCase();

                Ext.manifest = theme + "-" + language;
                tags.test = /testMode=true/.test(location.search);
                Ext.microloaderTags = tags;
            };

            /*创建美课城高端产品主程序加载器对象*/
            TranzvisionMeikecityAdvanced.frameworkLoader =
            {
                createMeikecityAdvancedFramework : function()
                {
                    Ext.platformTags = {};

                    if(Ext.beforeLoad)
                    {
                        Ext.beforeLoad(Ext.platformTags);
                    }

                    /*设置动态js文件加载地址*/
                    Ext.Loader.setConfig({
                        enabled:true,
                        paths:{
                            'KitchenSink': TzUniversityContextPath + '/statics/js/tranzvision/extjs/app',
                            'Ext.ux': TzUniversityContextPath + '/statics/js/lib/extjs/ux',
                            'tranzvision.extension': TzUniversityContextPath + '/statics/js/tranzvision/extjs/extension',
                            'Ext.lib': TzUniversityContextPath + '/statics/js/lib'
                        }
                    });

                    Ext.application(
                        {
                            extend: 'KitchenSink.Application',
                            name: 'KitchenSink',
                            autoCreateViewport: 'KitchenSink.view.main.Main',
                            paths:
                            {
                                'KitchenSink': TzUniversityContextPath + '/statics/js/tranzvision/extjs/app'
                            }
                        });

                    /*隐藏欢迎登录信息*/
                    var defaultObject = document.getElementById("tranzvision_mkc_advanced_default_table");
                    if(defaultObject != null)
                    {
                        defaultObject.style.display = "none";
                    }
                }
            };
        },

        loadFrameworkResource: function(resources)
        {
            /*动态加载CSS资源和JS程序*/
            var resource_array = resources[boot.theme];
            for(var j=0; j < resource_array.length; j++)
            {
                /*动态加载CSS资源*/
                var cssheader = '<link rel="stylesheet" type="text/css" href="';
                var csstailer = '"/>';
                try
                {
                    var css_araay = resource_array[j].content.css;
                    for(var i = 0; i < css_araay.length; i++)
                    {
                        var css_item = css_araay[i];
                        var css_path = css_item.path;
                        var css_files = css_item.name;
                        if(css_path != null)
                        {
                            for(var k = 0; k < css_files.length; k++)
                            {
                                if(css_files[k] != null)
                                {
                                    boot.doc.write(cssheader + TzUniversityContextPath + css_path + "/" + css_files[k] + csstailer);
                                }
                            }
                        }
                    }
                }
                catch(e)
                {
                    boot.error.failed = true;
                    boot.error.description = boot.getMessage("TZGD_FWINIT_00008");
                }

                /*动态加载JS程序*/
                var jsheader = '<script type="text/javascript" charset="UTF-8" src="';
                var jstailer = '"></script>';
                try
                {
                    var js_araay = resource_array[j].content.js;
                    for(var i = 0; i < js_araay.length; i++)
                    {
                        var js_item = js_araay[i];
                        var js_path = js_item.path;
                        var js_files = js_item.name;
                        if(js_path != null)
                        {
                            for(var k = 0; k < js_files.length; k++)
                            {
                                if(js_files[k] != null)
                                {
                                    boot.doc.write(jsheader + TzUniversityContextPath + js_path + "/" + js_files[k] + jstailer);
                                }
                            }
                        }
                    }
                }
                catch(e)
                {
                    boot.error.failed = true;
                    boot.error.description = boot.getMessage("TZGD_FWINIT_00009");
                }
            }
        },

        getMainmenuTree: function()
        {
            var loadStatus = true;
            var errorDesc = "";

            /*加载主菜单树*/
            try
            {
                Ext.Ajax.request(
                    {
                        //url: this.mainmenu_cfg_url,
                        url: this.mainapp_interaction_url,
                        params:{tzParams: '{"OperateType":"ZCD"}'},
                        async: false,
                        success: function(response)
                        {
                            var jsonText = response.responseText;

                            try
                            {
                                var jsonObject = Ext.util.JSON.decode(jsonText);

                                /*判断服务器是否返回了正确的信息*/
                                if(jsonObject.state.errcode === 0)
                                {/*服务器正确返回美课城应用主菜单数据*/
                                    /*设置主菜单JSON对象*/
                                    //boot.mainMenu = jsonObject[boot.language].mainMenu;
                                    var comContent = jsonObject.comContent;
                                    if(comContent[boot.language] != undefined)
                                    {
                                        boot.mainMenu = comContent[boot.language].mainMenu;
                                    }
                                    else
                                    {
                                        boot.mainMenu = comContent['ZHS'].mainMenu;
                                    }
                                }
                                else
                                {
                                    loadStatus = false;
                                    boot.error.failed = true;
                                    boot.error.description = jsonObject.state.errdesc;
                                }
                            }
                            catch(e)
                            {
                                loadStatus = false;
                                boot.error.failed = true;
                                boot.error.description = boot.getMessage("TZGD_FWINIT_00011");
                            }
                        },
                        failure: function(response, opts)
                        {
                            loadStatus = false;
                            boot.error.failed = true;
                            boot.error.description = boot.getMessage("TZGD_FWINIT_00012");
                        }
                    });
            }
            catch(e)
            {
                loadStatus = false;
                this.error.failed = true;
                this.error.description = this.getMessage("TZGD_FWINIT_00002") + e.toString();
            }

            return loadStatus;
        },

        getFrameworkResourceList: function()
        {
            var loadStatus = true;
            var errorDesc = "";

            /*加载主框架资源*/
            try
            {
                Ext.Ajax.request(
                    {
                        //url: this.mainapp_cfg_url,
                        url: this.mainapp_interaction_url,
                        params:{tzParams: '{"themeID":"'+this.theme+'","OperateType":"KJZY"}'},
                        async: false,
                        success: function(response)
                        {
                            var jsonText = response.responseText;

                            try
                            {
                                var jsonObject = Ext.util.JSON.decode(jsonText);

                                /*判断服务器是否返回了正确的信息*/
                                if(jsonObject.state.errcode === 0)
                                {/*服务器正确返回美课城框架程序资源列表*/
                                    /*资源内容*/
                                    var comContent = jsonObject.comContent;

                                    /*当前登录用户账号*/
                                    boot.loginUserId = comContent.loginUserID;

                                    /*当前用户的机构编号*/
                                    boot.orgID = comContent.curOrgID;

                                    /*当前用户姓名*/
                                    boot.firstName = comContent.firstName;

                                    /*设置当前会话语言环境代码*/
                                    boot.language = (comContent.languageCode || 'ZHS').toUpperCase();

                                    /*设置语言包*/
                                    if(comContent[boot.language] != undefined)
                                    {
                                        boot.languagePackage = comContent[boot.language].languagePackage;
                                    }
                                    else
                                    {
                                        boot.languagePackage = comContent['ZHS'].languagePackage;
                                    }

                                    /*加载主框架资源*/
                                    boot.loadFrameworkResource(comContent.resources);
                                }
                                else
                                {
                                    loadStatus = false;
                                    boot.error.failed = true;
                                    boot.error.description = jsonObject.state.errdesc;
                                }
                            }
                            catch(e)
                            {
                                loadStatus = false;
                                boot.error.failed = true;
                                boot.error.description = boot.getMessage("TZGD_FWINIT_00007");
                            }
                        },
                        failure: function(response, opts)
                        {
                            loadStatus = false;
                            boot.error.failed = true;
                            boot.error.description = boot.getMessage("TZGD_FWINIT_00004");
                        }
                    });
            }
            catch(e)
            {
                loadStatus = false;
                this.error.failed = true;
                this.error.description = this.getMessage("TZGD_FWINIT_00002") + e.toString();
            }

            return loadStatus;
        },

        //获取与客户端与服务端统一交互URL的方法
        getGeneralURL: function()
        {
            return boot.mainapp_interaction_url;
        },

        //设置代理
        listProxy: function()
        {
            var proxy = new Ext.data.proxy.Ajax(
                {
                    url: boot.mainapp_interaction_url,
                    reader:
                    {
                        type: 'json',
                        totalProperty: 'comContent.total',
                        rootProperty: 'comContent.root',
                        messageProperty: 'state.errdesc'
                    }
                });

            return proxy;
        },

        //负责客户端与服务端统一交互的方法
        submit: function(params,callback,msg,saveFlag,saveTarget,failureFn)
        {
            if(msg == "" || msg == undefined)
            {
                //msg = "保存成功";
                msg = TranzvisionMeikecityAdvanced.Boot.getMessage("TZGD_FWINIT_00025");
            }

            //等待信息
            //Ext.MessageBox.wait("加载中", "请稍候");
            var myMask = new Ext.LoadMask(
                {
                    msg    : boot.getMessage("TZGD_FWINIT_00022"),
                    target : Ext.getCmp('tranzvision-framework-content-panel')
                });

            myMask.show();

            var theFailureFn;
            try
            {
                theFailureFn = eval(failureFn);
            }
            catch(e)
            { }

            try
            {
                Ext.Ajax.request(
                    {
                        url: boot.mainapp_interaction_url,
                        params:{tzParams: params},
                        timeout: 60000,
                        async: false,
                        success: function(response, opts)
                        {
                            //返回值内容
                            var jsonText = response.responseText;
                            try
                            {
                                var jsonObject = Ext.util.JSON.decode(jsonText);
                                /*判断服务器是否返回了正确的信息*/
                                if(jsonObject.state.errcode == 0)
                                {
                                    boot.showToast(msg);

                                    //提交Form表单修改，重置表单修改状态
                                    if(saveFlag === true)
                                    {
                                        var savedObject = saveTarget && saveTarget.getView && (typeof saveTarget.getView === "function") && saveTarget.getView();
                                        savedObject = savedObject || saveTarget;
                                        if(savedObject && savedObject.commitChanges && (typeof savedObject.commitChanges === "function"))
                                        {
                                            savedObject.commitChanges(savedObject);
                                        }
                                    }

                                    var theFunc = eval(callback);
                                    if (typeof(theFunc) == "function")
                                    {
                                        theFunc(jsonObject.comContent);
                                    }
                                }
                                else if(jsonObject.state.timeout == true)
                                {
                                    try
                                    {
                                        if (typeof(theFailureFn) == "function")
                                        {
                                            theFailureFn(jsonObject.state);
                                        }

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
                                    if (typeof(theFailureFn) == "function")
                                    {
                                        theFailureFn(jsonObject.state);
                                    }
                                    TranzvisionMeikecityAdvanced.Boot.errorMessage(jsonObject.state.errdesc);
                                }
                            }
                            catch(e)
                            {
                                if (typeof(failureFn) == "function")
                                {
                                    failureFn(jsonText);
                                }
                                TranzvisionMeikecityAdvanced.Boot.errorMessage(e.toString());
                            }
                        },
                        failure: function(response, opts)
                        {
                            //错误信息响应报文
                            try
                            {
                                var respText = Ext.util.JSON.decode(response.responseText);
                                if (typeof(failureFn) == "function")
                                {
                                    failureFn(respText);
                                }
                                TranzvisionMeikecityAdvanced.Boot.errorMessage(respText.error);
                            }
                            catch(e2)
                            {
                                if (typeof(failureFn) == "function")
                                {
                                    failureFn(response.responseText);
                                }
                                if(response.responseText == undefined || response.responseText == "")
                                {
                                    TranzvisionMeikecityAdvanced.Boot.errorMessage(boot.getMessage("TZGD_FWINIT_00027"));
                                }
                                else
                                {
                                    TranzvisionMeikecityAdvanced.Boot.errorMessage(response.responseText);
                                }
                            }
                        },
                        callback: function(opts,success,response)
                        {
                            myMask.hide();
                        }
                    });
            }
            catch(e1)
            {
                boot.errorMessage(boot.getMessage("TZGD_FWINIT_00027"));
                myMask.hide();
            }
        },

        //加载数据
        load: function(params,callback,target)
        {
            //等待信息
            //Ext.MessageBox.wait("加载中", "请稍候");
            var myMask = new Ext.LoadMask(
                {
                    msg    : boot.getMessage("TZGD_FWINIT_00022"),
                    target : target!=undefined?target:Ext.getCmp('tranzvision-framework-content-panel')
                });

            myMask.show();

            try
            {
                //获取数据
                Ext.Ajax.request(
                    {
                        url: boot.mainapp_interaction_url,
                        params:{tzParams: params},
                        success: function(response, opts)
                        {
                            //返回值内容
                            var jsonText = response.responseText;
                            try
                            {
                                var jsonObject = Ext.util.JSON.decode(jsonText);
                                /*判断服务器是否返回了正确的信息*/
                                if(jsonObject.state.errcode == 0)
                                {
                                    var theFunc = eval(callback);
                                    if (typeof(theFunc) == "function")
                                    {
                                        theFunc(jsonObject.comContent);
                                    }
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
                            try
                            {
                                var respText = Ext.util.JSON.decode(response.responseText);
                                TranzvisionMeikecityAdvanced.Boot.errorMessage(respText.error);
                            }
                            catch(e2)
                            {
                                if(response.responseText == undefined || response.responseText == "")
                                {
                                    TranzvisionMeikecityAdvanced.Boot.errorMessage(boot.getMessage("TZGD_FWINIT_00026"));
                                }
                                else
                                {
                                    TranzvisionMeikecityAdvanced.Boot.errorMessage(response.responseText);
                                }
                            }
                        },
                        callback: function(opts,success,response)
                        {
                            myMask.hide();
                        }
                    });
            }
            catch(e1)
            {
                boot.errorMessage(boot.getMessage("TZGD_FWINIT_00026"));
                myMask.hide();
            }
        },

        loadAsync:function(params,callback)
        {
            //等待信息
            //Ext.MessageBox.wait("加载中", "请稍候");
            var myMask = new Ext.LoadMask(
                {
                    msg    : boot.getMessage("TZGD_FWINIT_00022"),
                    target : Ext.getCmp('tranzvision-framework-content-panel')
                });

            myMask.show();

            try
            {
                //获取数据
                Ext.Ajax.request(
                    {
                        url: boot.mainapp_interaction_url,
                        async: false,
                        params:{tzParams: params},
                        success: function(response, opts)
                        {
                            //返回值内容
                            var jsonText = response.responseText;
                            try
                            {
                                var jsonObject = Ext.util.JSON.decode(jsonText);
                                /*判断服务器是否返回了正确的信息*/
                                if(jsonObject.state.errcode == 0)
                                {
                                    var theFunc = eval(callback);
                                    if (typeof(theFunc) == "function")
                                    {
                                        theFunc(jsonObject.comContent);
                                    }
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
                            try
                            {
                                var respText = Ext.util.JSON.decode(response.responseText);
                                TranzvisionMeikecityAdvanced.Boot.errorMessage(respText.error);
                            }
                            catch(e2)
                            {
                                if(response.responseText == "")
                                {
                                    TranzvisionMeikecityAdvanced.Boot.errorMessage(boot.getMessage("TZGD_FWINIT_00026"));
                                }
                                else
                                {
                                    TranzvisionMeikecityAdvanced.Boot.errorMessage(response.responseText);
                                }
                            }
                        },
                        callback: function(opts,success,response)
                        {
                            myMask.hide();
                        }
                    });
            }
            catch(e1)
            {
                boot.errorMessage(boot.getMessage("TZGD_FWINIT_00026"));
                myMask.hide();
            }
        },

        //从服务器获取指定的组件描述资源集合
        getResourseFromServer: function(cid,pid,rid,defaultText)
        {
            //返回值
            var ret = defaultText;
            //标签ID
            var resId = cid+"."+pid+"."+rid;
            //传入参数
            var tzParams = '{"OperateType":"BQ","CID":"'+resId+'","DText":"'+defaultText+'"}';

            try
            {
                Ext.Ajax.request(
                    {
                        url: this.mainapp_interaction_url,
                        async: false,
                        params:{tzParams: tzParams},
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
                                    var languagePackage = null;
                                    if(mainContent[boot.language] != undefined)
                                    {
                                        languagePackage = mainContent[boot.language].languagePackage;
                                    }
                                    else
                                    {
                                        languagePackage = mainContent['ZHS'].languagePackage;
                                    }
                                    //描述
                                    ret = languagePackage[resId];
                                    //组件信息集合
                                    var cResourses = {};
                                    //页面资源
                                    var pResources = {};
                                    for(var itm in languagePackage)
                                    {
                                      var tmp1 = itm.split(".");
                                      var tmp2 = tmp1[2];
                                      if(tmp2 !== undefined)
                                      {
                                        pResources[tmp2] = languagePackage[itm];
                                      }
                                    }
                                    cResourses[pid] = pResources;
                                    boot.setDescResourseSet(cid,cResourses);
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
                                    boot.errorMessage(jsonObject.state.errdesc);
                                }
                            }
                            catch(e)
                            {
                                boot.errorMessage(e.toString());
                            }
                        },
                        failure: function(response, opts)
                        {
                            //错误信息响应报文
                            try
                            {
                                var respText = Ext.util.JSON.decode(response.responseText);
                                TranzvisionMeikecityAdvanced.Boot.errorMessage(respText.error);
                            }
                            catch(e2)
                            {
                                if(response.responseText == "")
                                {
                                    TranzvisionMeikecityAdvanced.Boot.errorMessage(boot.getMessage("TZGD_FWINIT_00027"));
                                }
                                else
                                {
                                    TranzvisionMeikecityAdvanced.Boot.errorMessage(response.responseText);
                                }
                            }
                        }
                    });
            }
            catch(e1)
            {
                boot.errorMessage(boot.getMessage("TZGD_FWINIT_00027"));
            }

            return ret;
        },

        //返回页beforeactivate事件监听方法的方法
        tabOn: function(tab,sCmpView,tCmpView,callerObject)
        {
            var tabOnActiveHandler = {
                beforeactivate: function(tab)
                {
                    ;
                }
            };

            if(sCmpView.isComponent && tCmpView.isComponent && sCmpView.currentNodeId)
            {
                tab.currentNodeId = sCmpView.currentNodeId;
                tCmpView.currentNodeId = sCmpView.currentNodeId;

                tabOnActiveHandler = {
                    beforeactivate: function(tab)
                    {
                        if(tab.currentNodeId != undefined && tab.currentNodeId != null)
                        {
                            var store = Ext.StoreMgr.get('navigation');
                            var node =  store.getNodeById(tab.currentNodeId);
                            var navigationBreadcrumb = Ext.getCmp('tranzvision-framework-navigation-breadcrumb');

                            if(node != undefined && node != null)
                            {
                                var navigationTree = node.getOwnerTree();
                                var hasTree = navigationTree && navigationTree.isVisible();

                                if(hasTree && node.isVisible() == true)
                                {
                                    try
                                    {
                                        navigationTree.getView().focusNode(node);
                                    }
                                    catch(e)
                                    {}
                                }

                                if(navigationTree)
                                {
                                    navigationTree.getSelectionModel().select(node);
                                }

                                navigationBreadcrumb.private_setSelection(node);

                                if(callerObject && typeof callerObject.updateTitle === "function")
                                {
                                    callerObject.updateTitle(node);
                                }
                                else
                                {
                                    var tmpController = TranzvisionMeikecityAdvanced.Boot.globalController;
                                    if(tmpController && typeof tmpController.updateTitle === "function")
                                    {
                                        tmpController.updateTitle(node);
                                    }
                                }
                            }
                        }
                    }
                };
            }

            return tabOnActiveHandler;
        },

        //导入Excel、粘贴Excel数据并解析的方法
        importExcel : function(config)
        {
            var className = 'KitchenSink.view.common.importExcel.importExcelWindow';
            if(!Ext.ClassManager.isCreated(className)){
                Ext.syncRequire(className);
            }
            var ViewClass = Ext.ClassManager.get(className);
            var win = new ViewClass(config);
            win.show();
        },

        //从服务器获取指定hardcode点的值
        getHardcodeValue: function(hardvodeName)
        {
            //hardcode值
            var hardvodeValue="";
            var tzParams = '{"OperateType":"HARDCODE","hardcodeName":"'+hardvodeName+'"}';

            try
            {
                Ext.Ajax.request(
                    {
                        url: boot.mainapp_interaction_url,
                        async: false,
                        params:{tzParams: tzParams},
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
                                    if(typeof mainContent=='string'){
                                        hardvodeValue = mainContent
                                    }
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
                                    boot.errorMessage(jsonObject.state.errdesc);
                                }
                            }
                            catch(e)
                            {
                                boot.errorMessage(e.toString());
                            }
                        },
                        failure: function(response, opts)
                        {
                            //错误信息响应报文
                            try
                            {
                                var respText = Ext.util.JSON.decode(response.responseText);
                                TranzvisionMeikecityAdvanced.Boot.errorMessage(respText.error);
                            }
                            catch(e2)
                            {
                                if(response.responseText == "")
                                {
                                    TranzvisionMeikecityAdvanced.Boot.errorMessage(boot.getMessage("TZGD_FWINIT_00027"));
                                }
                                else
                                {
                                    TranzvisionMeikecityAdvanced.Boot.errorMessage(response.responseText);
                                }
                            }
                        }
                    });
            }
            catch(e1)
            {
                boot.errorMessage(boot.getMessage("TZGD_FWINIT_00027"));
            }

            return hardvodeValue;
        },

        //将指定组件描述资源信息集合对象放入到tzResourseSet对象中
        setDescResourseSet: function(cid,cResourses)
        {
            //boot.descResourseSet[cid] = boot.descResourseSet[cid] || descResourseSet;
            if(boot.descResourseSet[cid] === undefined)
            {
              boot.descResourseSet[cid] = cResourses;
            }
            else
            {
              var dest = boot.descResourseSet[cid];
              for(var itm in cResourses)
              {
                dest[itm] = dest[itm] || {};
                Ext.apply(dest[itm],cResourses[itm]);
              }
            }
        },

        //获取指定资源描述ID对应的描述信息的方法
        getDescResourse: function(resId,defaultText)
        {
            var ret = "";
            var cid = "";//虚拟组件ID
            var pid = "";//虚拟页面ID
            var rid = "";//资源字段ID

            try
            {
                //解析资源ID
                var tmpArray = resId.split(".");

                if(tmpArray.length >= 3)
                {
                    cid = tmpArray[0];
                    pid = tmpArray[1];
                    rid = tmpArray[2];

                    //获取指定资源描述信息
                    if(boot.descResourseSet != undefined)
                    {
                        var cset = boot.descResourseSet[cid];
                        if(cset != undefined)
                        {
                            var pset = cset[pid];
                            if(pset != undefined)
                            {
                                res = pset[rid];
                                if(res != undefined)
                                {
                                    ret = res;
                                }
                            }
                        }
                    }
                    if(ret == ""){
                        //当前缓存中不存在指定的描述资源信息，则向服务器请求指定的描述资源信息
                        var tmpRet = boot.getResourseFromServer(cid,pid,rid,defaultText);
                        if(tmpRet != undefined)
                        {
                            ret = tmpRet;
                        }else{
                            ret = defaultText;
                        }
                    }
                }
            }
            catch(e1)
            {
                //不做任何处理
            }

            return ret;
        },

        //负责可配置搜索弹窗的方法
        showCFGSearch: function(configuration)
        {
            var className = 'KitchenSink.view.common.cfgSearchWindow';
            if(!Ext.ClassManager.isCreated(className)){
                Ext.syncRequire(className);
            }
            var ViewClass = Ext.ClassManager.get(className);
            //新建类
            var win = new ViewClass(configuration);
            win.show();
        },

        //发送邮件;
        sendEmail: function(configuration)
        {
            var contentPanel,cmp, className, ViewClass;

            contentPanel = Ext.getCmp('tranzvision-framework-content-panel');
            contentPanel.body.addCls('kitchensink-example');

            className = 'KitchenSink.view.common.commonEmailPanel';
            if(!Ext.ClassManager.isCreated(className)){
                Ext.syncRequire(className);
            }
            ViewClass = Ext.ClassManager.get(className);

            cmp = new ViewClass(configuration);

            tab = contentPanel.add(cmp);

            contentPanel.setActiveTab(tab);

            Ext.resumeLayouts(true);

            if (cmp.floating) {
                cmp.show();
            }
        },

        //发送短信;
        sendSms: function(configuration)
        {
            var contentPanel,cmp, className, ViewClass;

            contentPanel = Ext.getCmp('tranzvision-framework-content-panel');
            contentPanel.body.addCls('kitchensink-example');

            className = 'KitchenSink.view.common.commonSmsPanel';
            if(!Ext.ClassManager.isCreated(className)){
                Ext.syncRequire(className);
            }
            ViewClass = Ext.ClassManager.get(className);

            cmp = new ViewClass(configuration);

            tab = contentPanel.add(cmp);

            contentPanel.setActiveTab(tab);

            Ext.resumeLayouts(true);

            if (cmp.floating) {
                cmp.show();
            }
        },

        //负责Prompt弹窗的方法
        showPromptSearch: function(configuration)
        {
            var className = 'KitchenSink.view.common.promptSearchWindow';
            if(!Ext.ClassManager.isCreated(className)){
                Ext.syncRequire(className);
            }
            var ViewClass = Ext.ClassManager.get(className);
            //新建类
            var win = new ViewClass(configuration);
            //win.show();
        },

        //反馈信息
        showToast: function(html,title,align,bgcolor)
        {
            //标题
            if(title == undefined)
            {
                title = "";
            }
            //从哪个方向显示
            if(align == undefined)
            {
                align = "t";
            }
            //背景色
            if(bgcolor == undefined)
            {
                bgcolor = "white";
            }
            Ext.toast(
                {
                    html: html,
                    closable: false,
                    align: align,
                    style: "background-color:"+bgcolor,
                    slideInDuration: 400,
                    minWidth: 400
                });
        },

        //获取组件资源并加载默认页面
        setCompResourses:function(comID)
        {
            var loadOkFlag = false;

            if(comID == "" || comID == undefined)
            {
                return loadOkFlag;
            }

            var dIndex = comID.indexOf("$");
            var pComID = comID;
            if(dIndex >= 0)
            {
                pComID = comID.substring(0,dIndex);
            }

            //组件注册信息集合
            var comRegResourseSet = boot.comRegResourseSet[pComID];
            if(comRegResourseSet == undefined)
            {
                var tzParams = '{"OperateType":"ZJZY","ComID":"'+comID+'"}';
                try
                {
                    Ext.Ajax.request(
                        {
                            url: boot.mainapp_interaction_url,
                            async: false,
                            params:{tzParams: tzParams},
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
                                            for(var j=0;j<tagResources.length;j++)
                                            {
                                                //标签ID
                                                var tagIdArray = tagResources[j].tagID.split(".");
                                                var tagID = tagIdArray[2] || tagResources[j].tagID;
                                                //标签名称
                                                var tagName = tagResources[j].tagName;
                                                pResources[tagID] = tagName;
                                            }
                                            cResourses[pageID] = pResources;
                                        }

                                        //默认页面的JS类
                                        if(cPResources[defaultPageID] != "" && cPResources[defaultPageID] != undefined)
                                        {
                                            Ext.syncRequire(cPResources[defaultPageID]["jsClassName"]);
                                        }

                                        boot.setDescResourseSet(pComID,cResourses);
                                        boot.comRegResourseSet[pComID] = cPResources;

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
                                        boot.errorMessage(jsonObject.state.errdesc);
                                    }
                                }
                                catch(e)
                                {
                                    boot.errorMessage(e.toString());
                                }
                            },
                            failure: function(response, opts)
                            {
                                //错误信息响应报文
                                try
                                {
                                    var respText = Ext.util.JSON.decode(response.responseText);
                                    TranzvisionMeikecityAdvanced.Boot.errorMessage(respText.error);
                                }
                                catch(e2)
                                {
                                    if(response.responseText == "")
                                    {
                                        TranzvisionMeikecityAdvanced.Boot.errorMessage(boot.getMessage("TZGD_FWINIT_00015"));
                                    }
                                    else
                                    {
                                        TranzvisionMeikecityAdvanced.Boot.errorMessage(response.responseText);
                                    }
                                }
                            },
                            callback: function(opts,success,response)
                            {
                                //myMask.hide();
                            }
                        });
                }
                catch(e1)
                {
                    boot.errorMessage(boot.getMessage("TZGD_FWINIT_00015"));
                    //myMask.hide();
                }
            }
            else
            {
                loadOkFlag = true;
            }

            return loadOkFlag;
        },

        //获取验证码图片URL的方法
        getValidCodeImageURL: function()
        {
			var _captchaURL = TzUniversityContextPath + "/captcha";
			return _captchaURL + "?" + Math.random();
        },
		
		//根据邮箱查看邮件发送历史;
        searchMailHistory: function(configuration)
        {
            var contentPanel,cmp, className, ViewClass;

            contentPanel = Ext.getCmp('tranzvision-framework-content-panel');
            contentPanel.body.addCls('kitchensink-example');

            className = 'KitchenSink.view.common.searchMailHistoryPanel';
            if(!Ext.ClassManager.isCreated(className)){
                Ext.syncRequire(className);
            }
            ViewClass = Ext.ClassManager.get(className);

            cmp = new ViewClass(configuration);

            tab = contentPanel.add(cmp);

            contentPanel.setActiveTab(tab);

            Ext.resumeLayouts(true);

            if (cmp.floating) {
                cmp.show();
            }
        }
    };

    return boot;
})();

(function()
{
    /*调用美课城高端产品主程序加载器初始化*/
    try
    {
        /*初始化美课城高端产品主框架程序加载器*/
        TranzvisionMeikecityAdvanced.Boot.init();

        /*加载美课城高端产品主框架资源*/
        TranzvisionMeikecityAdvanced.Boot.getFrameworkResourceList();

        /*构建美课城高端产品主应用框架*/
        Ext.onReady(function ()
        {
            document.title = TranzvisionMeikecityAdvanced.Boot.getTitle();

            /*为Ext对象新增扩展接口方法*/
            if(Ext.tzGetGeneralURL == undefined) Ext.tzGetGeneralURL = TranzvisionMeikecityAdvanced.Boot.getGeneralURL;
            if(Ext.tzSubmit == undefined) Ext.tzSubmit = TranzvisionMeikecityAdvanced.Boot.submit;
            if(Ext.tzGetResourse == undefined) Ext.tzGetResourse = TranzvisionMeikecityAdvanced.Boot.getDescResourse;
            if(Ext.tzShowCFGSearch == undefined) Ext.tzShowCFGSearch = TranzvisionMeikecityAdvanced.Boot.showCFGSearch;
            if(Ext.tzSendEmail == undefined) Ext.tzSendEmail = TranzvisionMeikecityAdvanced.Boot.sendEmail;
            if(Ext.tzSendSms == undefined) Ext.tzSendSms = TranzvisionMeikecityAdvanced.Boot.sendSms;
            if(Ext.tzShowPromptSearch == undefined) Ext.tzShowPromptSearch = TranzvisionMeikecityAdvanced.Boot.showPromptSearch;
            if(Ext.tzShowToast == undefined) Ext.tzShowToast = TranzvisionMeikecityAdvanced.Boot.showToast;
            if(Ext.tzLoad == undefined) Ext.tzLoad = TranzvisionMeikecityAdvanced.Boot.load;
            if(Ext.tzLoadAsync == undefined) Ext.tzLoadAsync = TranzvisionMeikecityAdvanced.Boot.loadAsync;
            if(Ext.tzListProxy == undefined) Ext.tzListProxy = TranzvisionMeikecityAdvanced.Boot.listProxy;
            if(Ext.tzSetCompResourses == undefined) Ext.tzSetCompResourses = TranzvisionMeikecityAdvanced.Boot.setCompResourses;
            if(Ext.tzOrgID == undefined) Ext.tzOrgID = TranzvisionMeikecityAdvanced.Boot.orgID;
            if(Ext.tzGetResourseFromServer == undefined) Ext.tzGetResourseFromServer = TranzvisionMeikecityAdvanced.Boot.getResourseFromServer;
            if(Ext.tzTabOn == undefined) Ext.tzTabOn = TranzvisionMeikecityAdvanced.Boot.tabOn;
            if(Ext.tzImport == undefined) Ext.tzImport = TranzvisionMeikecityAdvanced.Boot.importExcel;
            if(Ext.tzGetHardcodeValue == undefined) Ext.tzGetHardcodeValue = TranzvisionMeikecityAdvanced.Boot.getHardcodeValue;
			if(Ext.tzSearchMailHistory == undefined) Ext.tzSearchMailHistory = TranzvisionMeikecityAdvanced.Boot.searchMailHistory;


            /*加载重验证码校验相关的JS代码*/
            Ext.apply(Ext.form.field.VTypes,
                {
                    codeValidator: function(value)
                    {
                        var flag = false;
                        var tzYzmParams ='{"ComID":"TZ_PT_LOGIN_COM","PageID":"TZ_PT_LOGIN_PAGE","OperateType":"HTML","comParams":{"validateType":"IamgeCodeValidate","yzm":'+ Ext.JSON.encodeString(value) +'}}';

                        Ext.Ajax.request
                        ({
                            async: false,
                            url: Ext.tzGetGeneralURL(),
                            params:
                            {
                                "tzParams": tzYzmParams
                            },
                            success: function(response)
                            {
                                var responseText = eval( "(" + response.responseText + ")" );
                                if(responseText.success == "true")
                                {
                                    flag = true;
                                }
                            }
                        });

                        return flag;
                    },
                    codeValidatorText: TranzvisionMeikecityAdvanced.Boot.getMessage("TZGD_FWINIT_00040")
                });


            /*加载重新登录框的JS代码*/
            Ext.define('tranzvision.view.window.ReloginWindow',
                {
                    extend: 'Ext.window.Window',
                    xtype: 'relogin-window',
                    height: 270,
                    width: TranzvisionMeikecityAdvanced.Boot.language === 'ZHS' ? 420 : 460,
                    title: TranzvisionMeikecityAdvanced.Boot.getMessage("TZGD_FWINIT_00041"),
                    layout: 'fit',
                    resizable: false,
                    modal: true,
                    closeAction: 'destroy',
                    padding: 20,
                    ignoreChangesFlag: true,
                    id: 'tranzvision_relogin_20150626',
                    listeners:
                    {
                        show: function(object, eOpts)
                        {
                            Ext.getCmp("tranzvision_password_relogin_20150626").focus(false,100);
                        }
                    },
                    items:
                        [
                            {
                                xtype: 'form',
                                layout:
                                {
                                    type: 'vbox',
                                    align: 'stretch'
                                },
                                border: false,
                                ignoreLabelWidth: true,
                                fieldDefaults:
                                {
                                    msgTarget: 'side',
                                    labelWidth: TranzvisionMeikecityAdvanced.Boot.language === 'ZHS' ? 80 : 140,
                                    labelStyle: 'font-weight:bold'
                                },
                                items:
                                    [
                                        {
                                            xtype: 'textfield',
                                            fieldLabel: TranzvisionMeikecityAdvanced.Boot.getMessage("TZGD_FWINIT_00042"),
                                            name: 'userName',
                                            border: false,
                                            preSubTpl:
                                                [
                                                    '<div id="{cmpId}-triggerWrap" data-ref="triggerWrap" class="{triggerWrapCls} {triggerWrapCls}-{ui}" style="border:0">',
                                                    '<div id={cmpId}-inputWrap data-ref="inputWrap" class="{inputWrapCls} {inputWrapCls}-{ui}">'
                                                ],
                                            readOnly: true,
                                            value: TranzvisionMeikecityAdvanced.Boot.loginUserId
                                        },
                                        {
                                            xtype: 'textfield',
                                            fieldLabel: TranzvisionMeikecityAdvanced.Boot.getMessage("TZGD_FWINIT_00043"),
                                            name: 'tranzvision_password_relogin_20150626',
                                            id: 'tranzvision_password_relogin_20150626',
                                            inputType: 'password',
                                            allowBlank: false,
                                            listeners:
                                            {
                                                specialkey: function(textfield, e)
                                                {
                                                    if(e.getKey() == Ext.EventObject.ENTER)
                                                    {
                                                        Ext.getCmp("tranzvision_ensure_btn_relogin_20150626")["handler"]();
                                                    }
                                                }
                                            }
                                        },
                                        {
                                            layout:
                                            {
                                                type: 'hbox',
                                                align: 'stretch'
                                            },
                                            border: false,
                                            items:
                                                [
                                                    {
                                                        flex: 1,
                                                        xtype: 'textfield',
                                                        fieldLabel: TranzvisionMeikecityAdvanced.Boot.getMessage("TZGD_FWINIT_00044"),
                                                        name: 'yzm_20150626',
                                                        allowBlank: false,
                                                        validateOnChange: false,
                                                        validateOnBlur: true,
                                                        vtype: 'codeValidator',
                                                        listeners:
                                                        {
                                                            specialkey: function(textfield, e)
                                                            {
                                                                if(e.getKey() == Ext.EventObject.ENTER)
                                                                {
                                                                    Ext.getCmp("tranzvision_ensure_btn_relogin_20150626")["handler"]();
                                                                }
                                                            }
                                                        }
                                                    },
                                                    {
                                                        width: 95,
                                                        xtype: "image",
                                                        src: '',
                                                        name: "yzmpic_20150626",
                                                        id:  "yzmpic_20150626",
                                                        listeners:
                                                        {
                                                            el:
                                                            {
                                                                click: function()
                                                                {
                                                                    this.component.setSrc(TranzvisionMeikecityAdvanced.Boot.getValidCodeImageURL());
                                                                }
                                                            }
                                                        },
                                                        initComponent: function()
                                                        {
                                                            this.src = TranzvisionMeikecityAdvanced.Boot.getValidCodeImageURL();
                                                            this.callParent();
                                                        }
                                                    }
                                                ]
                                        },
                                        {
                                            margin: '0 0 0 0',
                                            xtype: 'component',
                                            html: '<center><font color="red">' + TranzvisionMeikecityAdvanced.Boot.getMessage("TZGD_FWINIT_00048") + '</font></center>'
                                        },
                                        {
                                            layout:
                                            {
                                                type: 'hbox',
                                                pack: 'end',
                                                align: 'stretch'
                                            },
                                            items:
                                                [
                                                    {
                                                        xtype: 'button',
                                                        id: 'tranzvision_ensure_btn_relogin_20150626',
                                                        text: TranzvisionMeikecityAdvanced.Boot.getMessage("TZGD_FWINIT_00016"),
                                                        iconCls:"ensure",
                                                        margin: '15 20 0 0',
                                                        handler: function()
                                                        {
                                                            var loginSuccess = false;
                                                            var bt = Ext.getCmp("btSubmit");
                                                            var form = this.findParentByType("form").getForm();

                                                            if (form.isValid())
                                                            {
                                                                var orgId = TranzvisionMeikecityAdvanced.Boot.orgID;
                                                                var userName = TranzvisionMeikecityAdvanced.Boot.loginUserId;
                                                                var password = form.findField("tranzvision_password_relogin_20150626").getValue();
                                                                var yzm = form.findField("yzm_20150626").getValue();

                                                                var tzLoginParams = {};
                                                                tzLoginParams.ComID = "TZ_PT_LOGIN_COM";
                                                                tzLoginParams.PageID = "TZ_PT_LOGIN_PAGE";
                                                                tzLoginParams.OperateType = "HTML";
                                                                tzLoginParams.comParams = {};
                                                                tzLoginParams.comParams.validateType = "JgLogin";
                                                                tzLoginParams.comParams.orgId = orgId;
                                                                tzLoginParams.comParams.userName = userName;
                                                                tzLoginParams.comParams.password = password;
                                                                tzLoginParams.comParams.yzm = yzm;

                                                                try
                                                                {
                                                                    Ext.Ajax.request
                                                                    ({
                                                                        async: false,
                                                                        url: Ext.tzGetGeneralURL(),
                                                                        params:
                                                                        {
                                                                            "tzParams": Ext.JSON.encode(tzLoginParams)
                                                                        },
                                                                        success: function(response)
                                                                        {
                                                                            var responseText = eval( "(" + response.responseText + ")" );

                                                                            if(responseText.success == "success")
                                                                            {
                                                                                var cpr = new Ext.state.CookieProvider();
                                                                                Ext.state.Manager.setProvider(cpr);
                                                                                loginSuccess = true;
                                                                            }
                                                                            else
                                                                            {
                                                                                var myBoot = TranzvisionMeikecityAdvanced.Boot;
                                                                                myBoot.errorMessage(myBoot.getMessage("TZGD_FWINIT_00010") + "\n" + responseText.error);
                                                                            }
                                                                        }
                                                                    });
                                                                }
                                                                catch(e1)
                                                                {
                                                                    var myBoot = TranzvisionMeikecityAdvanced.Boot;
                                                                    myBoot.errorMessage(myBoot.getMessage("TZGD_FWINIT_00010") + "\n" + e1.toString());
                                                                }
                                                            }
                                                            else
                                                            {
                                                                var myBoot = TranzvisionMeikecityAdvanced.Boot;
                                                                myBoot.errorMessage(myBoot.getMessage("TZGD_FWINIT_00045"));
                                                            }


                                                            if(loginSuccess == true)
                                                            {
                                                                Ext.tzShowToast(TranzvisionMeikecityAdvanced.Boot.getMessage("TZGD_FWINIT_00046"));
                                                                var win = this.findParentByType("window");
                                                                win.close();
                                                            }
                                                        }
                                                    },
                                                    {
                                                        xtype: 'button',
                                                        text: TranzvisionMeikecityAdvanced.Boot.getMessage("TZGD_FWINIT_00047"),
                                                        iconCls:"close",
                                                        margin: '15 0 0 0',
                                                        handler: function()
                                                        {
                                                            var win = this.findParentByType("window");
                                                            win.close();
                                                        }
                                                    }
                                                ]
                                        }
                                    ]
                            }
                        ]
                });


            /*重载Ext.Boot的Request对象，主要添加“resetClassLoadedStatus”方法*/
            var tmpReloadRequest =
            {
                /*重置JavaScript类加载状态或者提示第一个JavaScript类加载失败信息的方法*/
                resetClassLoadedStatus: function(showMessageOnly)
                {
                    var me = this;


                    if(typeof me._classNames == undefined)
                    {
                        return ;
                    }


                    var className = null;
                    var classNameList = me._classNames;
                    for(var j=0;j<classNameList.length;j++)
                    {
                        className = classNameList[j];


                        if(className == undefined)
                        {
                            continue;
                        }


                        /*判断当前指定的类是否已正确加载，如果已正确加载，则跳过*/
                        if(Ext.ClassManager.get(className) != undefined)
                        {
                            continue;
                        }


                        var tmpClassName = Ext.ClassManager.resolveName(className);
                        if(tmpClassName == undefined)
                        {
                            continue;
                        }


                        var tmpURL1 = Ext.Loader.getPath(tmpClassName);
                        var tmpEntries = me.getEntries();
                        var tmpEntry = null;
                        var tmpURL2 = "";
                        if(tmpURL1 != undefined)
                        {
                            for(var i=0;i<tmpEntries.length; i++)
                            {
                                tmpEntry = tmpEntries[i];
                                tmpURL2 = tmpEntry.getLoadUrl();

                                if(tmpURL2 != undefined)
                                {
                                    if(tmpURL2.indexOf(tmpURL1) >= 0)
                                    {
                                        if(tmpEntry.loaded == true && tmpEntry.error != undefined)
                                        {
                                            if(showMessageOnly == false)
                                            {
                                                /*重置JavaScript类的加载状态，以便可以重新加载该类*/
                                                tmpEntry.loaded = false;
                                                tmpEntry.loading = false;
                                                tmpEntry.evaluated = false;
                                                tmpEntry.evaluating = false;
                                                tmpEntry.error = false;
                                            }
                                            else
                                            {
                                                var errorMsg = TranzvisionMeikecityAdvanced.Boot.getMessage("TZGD_FWINIT_00015");
                                                TranzvisionMeikecityAdvanced.Boot.errorMessage(errorMsg);
                                                return;
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            };
            Ext.Boot.Request.prototype = Ext.apply(Ext.Boot.Request.prototype,tmpReloadRequest);

            /*重载Ext.Boot对象的“processRequest”方法*/
            var tmpExtBoot =
            {
                /*重载“processRequest”方法，在加载JavaScript类之前先根据状态判断是否要重置JavaScript类的加载状态*/
                processRequest: function(request, sync)
                {
                    /*有条件重置JavaScript类的加载状态*/
                    request.resetClassLoadedStatus(false);

                    /*执行原“processRequest”方法的代码*/
                    request.loadEntries(sync);

                    /*在同步加载的情况下，如果加载失败，则提示第一个加载错误*/
                    request.resetClassLoadedStatus(true);
                }
            };
            Ext.Boot = Ext.apply(Ext.Boot,tmpExtBoot);

            Ext.define('Altus.overrides.form.field.ComboBox',
                {
                    override: 'Ext.form.field.ComboBox',

                    /**
                    * Override to make all IEs as terrible as each other - Ext have an additional IE9 or less check.
                    * This sorts out field mutation firing on setValue, and setRawValue causing a selection in the combo.
                    *
                    * From here: http://www.sencha.com/forum/showthread.php?295443-IE-11-Combobox-doDelegatedEvent-double-call-setValue&p=1078651&viewfull=1#post1078651
                    */
                    checkChangeEvents: Ext.isIE ? ['change', 'propertychange', 'keyup'] : ['change', 'input', 'textInput', 'keyup', 'dragdrop']
                });

            /*排序*覆盖Ext.util.Sorter类的sortFn方法，以支持中文按拼音排序*/
            Ext.define('tranzvision.Ext.util.Sorter',
                {
                    override: 'Ext.util.Sorter',
                    sortFn: function(item1, item2)
                    {
                        var me = this,
                            transform = me._transform,
                            root = me._root,
                            property = me._property,
                            lhs, rhs;

                        if(root)
                        {
                            item1 = item1[root];
                            item2 = item2[root];
                        }

                        lhs = item1[property];
                        rhs = item2[property];

                        if(transform)
                        {
                            lhs = transform(lhs);
                            rhs = transform(rhs);
                        }

                        if(isNaN(lhs) === true || isNaN(rhs) === true)
                        {
                            /*其中一个比较量不是数字*/
                            var tmpLhs = Date.parse(lhs.replace(/[./\-]/g,"/"));
                            var tmpRhs = Date.parse(rhs.replace(/[./\-]/g,"/"));

                            if(isNaN(tmpLhs) === true || isNaN(tmpRhs) === true || lhs.length >= 31 || rhs.length >= 31)
                            {
                                /*两个比较量即不是数字，也不是日期，则使用localeCompare方法比较*/
                                return lhs.localeCompare(rhs);
                            }
                            else
                            {
                                /*两个比较量是日期*/
                                return (tmpLhs > tmpRhs) ? 1 : (tmpLhs < tmpRhs ? -1 : 0);
                            }
                        }
                        else
                        {
                            /*如果两个比较量均是数字，则转换成数字后再比较大小*/
                            lhs = 1.0 * lhs;
                            rhs = 1.0 * rhs;
                            return (lhs > rhs) ? 1 : (lhs < rhs ? -1 : 0);
                        }
                        //return (lhs > rhs) ? 1 : (lhs < rhs ? -1 : 0);
                    }
                });

            /*覆盖Ext.window.MessageBox类的buttonText属性和titleText属性*/
            Ext.define('tranzvision.Ext.window.MessageBox',
                {
                    override: 'Ext.window.MessageBox',
                    buttonText:
                    {
                        ok: TranzvisionMeikecityAdvanced.Boot.getMessage("TZGD_FWINIT_00016"),
                        yes: TranzvisionMeikecityAdvanced.Boot.getMessage("TZGD_FWINIT_00017"),
                        no: TranzvisionMeikecityAdvanced.Boot.getMessage("TZGD_FWINIT_00018"),
                        cancel: TranzvisionMeikecityAdvanced.Boot.getMessage("TZGD_FWINIT_00019")
                    },
                    titleText:
                    {
                        confirm: TranzvisionMeikecityAdvanced.Boot.getMessage("TZGD_FWINIT_00020"),
                        prompt: TranzvisionMeikecityAdvanced.Boot.getMessage("TZGD_FWINIT_00021"),
                        wait: TranzvisionMeikecityAdvanced.Boot.getMessage("TZGD_FWINIT_00022"),
                        alert: TranzvisionMeikecityAdvanced.Boot.getMessage("TZGD_FWINIT_00023")
                    }
                });

            /*覆盖翻页控件Ext.toolbar.Paging提示信息属性*/
            Ext.define('tranzvision.Ext.toolbar.Paging',
                {
                    override:'Ext.toolbar.Paging',
                    displayInfo:true,
                    displayMsg:TranzvisionMeikecityAdvanced.Boot.getMessage("TZGD_FWINIT_00028"),
                    emptyMsg:TranzvisionMeikecityAdvanced.Boot.getMessage("TZGD_FWINIT_00029"),
                    beforePageText:TranzvisionMeikecityAdvanced.Boot.getMessage("TZGD_FWINIT_00030"),
                    afterPageText:TranzvisionMeikecityAdvanced.Boot.getMessage("TZGD_FWINIT_00031"),
                    firstText:TranzvisionMeikecityAdvanced.Boot.getMessage("TZGD_FWINIT_00032"),
                    prevText:TranzvisionMeikecityAdvanced.Boot.getMessage("TZGD_FWINIT_00033"),
                    nextText:TranzvisionMeikecityAdvanced.Boot.getMessage("TZGD_FWINIT_00034"),
                    lastText:TranzvisionMeikecityAdvanced.Boot.getMessage("TZGD_FWINIT_00035"),
                    refreshText:TranzvisionMeikecityAdvanced.Boot.getMessage("TZGD_FWINIT_00036")
                });

            /*覆盖抽象视图Ext.view.AbstractView的提示信息属性*/
            Ext.define('tranzvision.Ext.view.AbstractView',
                {
                    override:'Ext.view.AbstractView',
                    loadingText:TranzvisionMeikecityAdvanced.Boot.getMessage("TZGD_FWINIT_00022")
                });

            /**编辑状态检查*覆盖类Ext.form.field.Base的isDirty方法*/
            Ext.define('tranzvision.Ext.form.field.Base',
                {
                    override:'Ext.form.field.Base',
                    ignoreChangesFlag: false,
                    isDirty: function()
                    {
                        var dirtyFlag = false;
                        var me = this;

                        if(me.ignoreChangesFlag == true)
                        {
                            dirtyFlag = false;
                        }
                        else
                        {
                            dirtyFlag = me.callParent(arguments);
                        }

                        return dirtyFlag;
                    }
                });

            /**HTML标签转义*覆盖类Ext.grid.column.Column的initComponent方法*/
            Ext.define('tranzvision.Ext.grid.column.Column',
                {
                    override:'Ext.grid.column.Column',
                    tzRenderer: function(value, metaData)
                    {
                      return Ext.String.htmlEncode(value);;
                    },
                    initComponent: function()
                    {
                      var me = this;


                      if(!me.renderer && !me.defaultRenderer)
                      {
                        me.renderer = me.tzRenderer;
                      }


                      me.callParent(arguments);
                    }
                }); 

            /**表单校验*覆盖类Ext.form.Basic的initialize、isValid方法*/
            Ext.define('tranzvision.Ext.form.Basic',
                {
                    override:'Ext.form.Basic',
                    constructor: function(owner, config)
                    {
                        this.callParent(arguments);
                        this.trackResetOnLoad = true;
                    },
                    isValid: function()
                    {
                        var me = this,invalid;

                        Ext.suspendLayouts();
                        var showTipInfo = true;
                        invalid = me.getFields().filterBy(function(field)
                        {
                            var tmpValidateResult = field.validate()&&(field.validState==undefined?true:field.validState);

                            if(tmpValidateResult == false && showTipInfo == true)
                            {
                                try
                                {
                                    field.focus();
                                    showTipInfo = false;
                                    Ext.tzShowToast(TranzvisionMeikecityAdvanced.Boot.getMessage("TZGD_FWINIT_00072"));
                                }
                                catch(e)
                                {}
                            }

                            return !tmpValidateResult;
                        });
                        Ext.resumeLayouts(true);
                        return invalid.length < 1;
                    }
                });

            /**新增、修改、删除保存提醒*覆盖类Ext.data.Store的loadPage方法*/
            Ext.define('tranzvision.Ext.data.Store',
                {
                    override:'Ext.data.Store',
                    loadPage: function()
                    {
                        var me = this;
                        var parameters = arguments;


                        if(this.isChanged() == true)
                        {
                            //获取当前方法对应的被覆盖前的原型方法
                            var method = this.loadPage;
                            var superMethod = method && (method.$previous || ((method = method.$owner ? method : method.caller) && method.$owner.superclass[method.$name]));

                            Ext.MessageBox.show(
                                {
                                    title: TranzvisionMeikecityAdvanced.Boot.getMessage("TZGD_FWINIT_00014"),
                                    width: 500,
                                    //height:160,
                                    //y: 200,
                                    msg: TranzvisionMeikecityAdvanced.Boot.getMessage("TZGD_FWINIT_00073"),
                                    buttons: Ext.Msg.YESNO,
                                    buttonText:
                                    {
                                        yes: TranzvisionMeikecityAdvanced.Boot.getMessage("TZGD_FWINIT_00074"),
                                        no: TranzvisionMeikecityAdvanced.Boot.getMessage("TZGD_FWINIT_00019")
                                    },
                                    icon: Ext.Msg.WARNING,
                                    fn: function(btnClicked)
                                    {
                                        if(btnClicked === "yes")
                                        {
                                            //调用loadPage被重写前的原型方法
                                            superMethod.apply(me,parameters);
                                        }
                                    }
                                });
                        }
                        else
                        {
                            me.callParent(parameters);
                        }
                    },
                    isChanged: function()
                    {
                        var me = this;
                        var newCount = 0;
                        var modCount = 0;
                        var delCount = 0;
                        var chgTotal = 0;


                        newCount = me.getNewRecords().length >= 1 ? 1 : 0;
                        modCount = me.getModifiedRecords().length >= 1 ? 1 : 0;
                        delCount = me.getRemovedRecords().length >= 1 ? 1 : 0;
                        chgTotal = newCount + modCount + delCount;


                        return chgTotal >= 1;
                    }
                });

            /**新增、修改、删除保存提醒*覆盖类Ext.tab.Tab的onCloseClick方法*/
            Ext.define('tranzvision.Ext.tab.Tab',
                {
                    override:'Ext.tab.Tab',
                    isChangesCommited: function()
                    {
                        var me = this;


                        //利用beforeclose事件先判断是否有尚未保存的编辑数据
                        if(me.fireEvent('beforeclose',me,true) !== false)
                        {
                            if(me.tabBar && me.card)
                            {
                                if(me.card.fireEvent('beforeclose',me.card,true) === false)
                                {
                                    //尚有未保存的数据，返回false
                                    return false;
                                }
                            }
                        }
                        else
                        {
                            //尚有未保存的数据，返回false
                            return false;
                        }


                        return true;
                    },
                    onCloseClick: function()
                    {
                        var me = this;
                        var parameters = arguments;


                        //先判断是否有尚未保存的编辑数据，如果有尚未保存的数据，则根据用户的选择决定是否继续执行关闭操作
                        if(me.isChangesCommited() === false)
                        {
                            //获取当前方法对应的被覆盖前的原型方法
                            var method = this.onCloseClick;
                            var superMethod = method && (method.$previous || ((method = method.$owner ? method : method.caller) && method.$owner.superclass[method.$name]));

                            Ext.MessageBox.show(
                                {
                                    title: TranzvisionMeikecityAdvanced.Boot.getMessage("TZGD_FWINIT_00014"),
                                    width: 500,
                                    //height:160,
                                    //y: 200,
                                    msg: TranzvisionMeikecityAdvanced.Boot.getMessage("TZGD_FWINIT_00073"),
                                    buttons: Ext.Msg.YESNO,
                                    buttonText:
                                    {
                                        yes: TranzvisionMeikecityAdvanced.Boot.getMessage("TZGD_FWINIT_00074"),
                                        no: TranzvisionMeikecityAdvanced.Boot.getMessage("TZGD_FWINIT_00019")
                                    },
                                    icon: Ext.Msg.WARNING,
                                    fn: function(btnClicked)
                                    {
                                        if(btnClicked === "yes")
                                        {
                                            //重新调用调用onCloseClick被重写前的原型方法
                                            superMethod.apply(me,parameters);
                                        }
                                    }
                                });

                            return false;
                        }
                        else
                        {
                            me.callParent(arguments);
                        }
                    }
                });

            /**新增、修改、删除保存提醒*监听类Ext.tab.Tab的beforeclose事件*/
            Ext.define('tranzvision.Ext.panel.Panel',
                {
                    override:'Ext.panel.Panel',
                    initComponent: function()
                    {
                        var me = this;

                        me.callParent(arguments);

                        if(me.hasListener("beforeclose") === false)
                        {
                            me.on({beforeclose: me.beforecloseHandler, scope: me});
                        }
                    },
                    isChangesCommited: function(cmpObject)
                    {
                        //先判断cmpObject对象是否有被编辑，如果被编辑，是否有已提交保存编辑的数据
                        if(Ext.ComponentQuery.is(cmpObject,"form") && cmpObject.isDirty() == true)
                        {
                            return false;
                        }

                        if(Ext.ComponentQuery.is(cmpObject,"grid") && cmpObject.getStore().isChanged() == true)
                        {
                            return false;
                        }


                        var me = this;
                        var savedFlag = true;
                        if((typeof cmpObject.getRefItems == "function") && cmpObject.isComponent)
                        {
                            var objects = [] && cmpObject.getRefItems();

                            for(var i=0;i<objects.length;i++)
                            {
                                savedFlag = me.isChangesCommited(objects[i]);
                                if(savedFlag === false)
                                {
                                    return false;
                                }
                            }
                        }


                        return true;
                    },
                    commitChanges: function(cmpObject)
                    {
                        //先判断cmpObject对象是否有被编辑
                        if(Ext.ComponentQuery.is(cmpObject,"form") && cmpObject.isDirty() == true)
                        {
                            cmpObject.getForm().getFields().each
                            (function(field)
                                {
                                    field.resetOriginalValue();
                                },
                                me
                            );
                        }

                        if(Ext.ComponentQuery.is(cmpObject,"grid") && cmpObject.getStore().isChanged() == true)
                        {
                            cmpObject.getStore().commitChanges();
                        }


                        var me = this;
                        if((typeof cmpObject.getRefItems == "function") && cmpObject.isComponent)
                        {
                            var objects = [] && cmpObject.getRefItems();

                            for(var i=0;i<objects.length;i++)
                            {
                                me.commitChanges(objects[i]);
                            }
                        }
                    },
                    ignoreChangesFlag: false,
                    ignoreChanges: function(flag)
                    {
                        this.ignoreChangesFlag = (flag === true) ? true : false;
                    },
                    close: function()
                    {
                        var me = this;
                        var parameters = arguments;


                        //先判断是否有尚未保存的编辑数据，如果有尚未保存的数据，则根据用户的选择决定是否继续执行关闭操作
                        if(me.isChangesCommited(me) === false && me.ignoreChangesFlag !== true)
                        {
                            //获取当前方法对应的被覆盖前的原型方法
                            var method = this.close;
                            var superMethod = method && (method.$previous || ((method = method.$owner ? method : method.caller) && method.$owner.superclass[method.$name]));

                            Ext.MessageBox.show(
                                {
                                    title: TranzvisionMeikecityAdvanced.Boot.getMessage("TZGD_FWINIT_00014"),
                                    width: 500,
                                    //height:160,
                                    //y: 200,
                                    msg: TranzvisionMeikecityAdvanced.Boot.getMessage("TZGD_FWINIT_00073"),
                                    buttons: Ext.Msg.YESNO,
                                    buttonText:
                                    {
                                        yes: TranzvisionMeikecityAdvanced.Boot.getMessage("TZGD_FWINIT_00074"),
                                        no: TranzvisionMeikecityAdvanced.Boot.getMessage("TZGD_FWINIT_00019")
                                    },
                                    icon: Ext.Msg.WARNING,
                                    fn: function(btnClicked)
                                    {
                                        if(btnClicked === "yes")
                                        {
                                            //重新调用调用close被重写前的原型方法
                                            superMethod.apply(me,parameters);
                                        }
                                    }
                                });

                            return false;
                        }
                        else
                        {
                            me.callParent(arguments);
                        }
                    },
                    beforecloseHandler: function(panelObject,checkChanges,eOpts)
                    {
                        //监听事件beforeclose
                        var me = this;


                        if(checkChanges === true)
                        {
                            if(me.findParentByType("contentPanel"))
                            {
                                return me.isChangesCommited(me);
                            }
                        }


                        return true;
                    }
                });

            /**表单校验*覆盖类Ext.form.field.Base的提示信息属性*/
            Ext.define('tranzvision.Ext.form.field.Base',
                {
                    override:'Ext.form.field.Base',
                    invalidText:TranzvisionMeikecityAdvanced.Boot.getMessage("TZGD_FWINIT_00051")
                });

            /**表单校验*覆盖类Ext.form.field.Text的提示信息属性*/
            Ext.define('tranzvision.Ext.form.field.Text',
                {
                    override:'Ext.form.field.Text',
                    minLengthText:TranzvisionMeikecityAdvanced.Boot.getMessage("TZGD_FWINIT_00052"),
                    maxLengthText:TranzvisionMeikecityAdvanced.Boot.getMessage("TZGD_FWINIT_00053"),
                    blankText:TranzvisionMeikecityAdvanced.Boot.getMessage("TZGD_FWINIT_00054"),
                    onBlur: function(e) {
                        var me = this;

                        me.callParent(arguments);

                        me.removeCls(me.fieldFocusCls);
                        me.triggerWrap.removeCls(me.triggerWrapFocusCls);
                        me.inputWrap.removeCls(me.inputWrapFocusCls);
                        me.invokeTriggers('onFieldBlur', [e]);

                        var value=me.value;
                        var valueExist = false;
                        var promptValidation = me.promptValidation;

                        if(promptValidation&&me.value.replace(/(^\s*)|(\s*$)/g, "")!=""&&me.isValid()){
                            eval("promptValidation.presetFields."+promptValidation.valueOfResult+"={value:'"+value+"',type:'01'}");
                            var condition = {presetFields:promptValidation.presetFields};

                            var results =promptValidation.valueOfResult;
                            if(promptValidation.displayOfResult!=undefined){
                                results=results  +','+promptValidation.displayOfResult;
                            }

                            Ext.Ajax.request({
                                async: false,
                                url: Ext.tzGetGeneralURL,
                                params: {
                                    tzParams: '{"OperateType":"PROMPT","maxRow":"1","recname":"'+promptValidation.recName+'","condition":'+Ext.JSON.encode(condition)+',"result":"'+results+'"}'
                                },
                                success: function(response){
                                    var jsonText = response.responseText;
                                    try{
                                        var responseText = Ext.util.JSON.decode(jsonText);
                                        var resultArray =responseText.comContent.root;
                                        if(resultArray&&resultArray.length>0){
                                            var displayOfResult =  eval("resultArray[0]."+promptValidation.displayOfResult);
                                            try{
                                                if(promptValidation.displayField!=undefined){
                                                    me.findParentByType('form').getForm().findField(promptValidation.displayField).setValue(displayOfResult);
                                                }
                                            }catch(e1){}
                                            valueExist=true
                                        }else{
                                            valueExist=false;
                                            try{
                                                if(promptValidation.displayField!=undefined){
                                                    me.findParentByType('form').getForm().findField(promptValidation.displayField).setValue();
                                                }
                                            }catch(e2){}
                                        }
                                    }catch(e){
                                        valueExist=false;
                                        try{
                                            if(promptValidation.displayField!=undefined){
                                                me.findParentByType('form').getForm().findField(promptValidation.displayField).setValue();
                                            }
                                        }catch(e3){}
                                    }
                                }
                            });
                            if(!valueExist){
                                me.markInvalid(promptValidation.errorMsg||'The value is not exist!');
                                me.validState=false;
                            }else{
                                me.validState=true;
                            }
                        }
                    }
                });

            /**表单校验*覆盖类Ext.form.field.Number的提示信息属性*/
            Ext.define('tranzvision.Ext.form.field.Number',
                {
                    override:'Ext.form.field.Number',
                    minText:TranzvisionMeikecityAdvanced.Boot.getMessage("TZGD_FWINIT_00055"),
                    maxText:TranzvisionMeikecityAdvanced.Boot.getMessage("TZGD_FWINIT_00056"),
                    nanText:TranzvisionMeikecityAdvanced.Boot.getMessage("TZGD_FWINIT_00057"),
                    negativeText:TranzvisionMeikecityAdvanced.Boot.getMessage("TZGD_FWINIT_00058")
                });

            /**表单校验*覆盖类Ext.form.field.Date的提示信息属性*/
            Ext.define('tranzvision.Ext.form.field.Date',
                {
                    override:'Ext.form.field.Date',
                    minText:TranzvisionMeikecityAdvanced.Boot.getMessage("TZGD_FWINIT_00059"),
                    maxText:TranzvisionMeikecityAdvanced.Boot.getMessage("TZGD_FWINIT_00060"),
                    invalidText:TranzvisionMeikecityAdvanced.Boot.getMessage("TZGD_FWINIT_00061")
                });

            /**表单校验*覆盖类Ext.form.field.Time的提示信息属性*/
            Ext.define('tranzvision.Ext.form.field.Time',
                {
                    override:'Ext.form.field.Time',
                    minText:TranzvisionMeikecityAdvanced.Boot.getMessage("TZGD_FWINIT_00062"),
                    maxText:TranzvisionMeikecityAdvanced.Boot.getMessage("TZGD_FWINIT_00063"),
                    invalidText:TranzvisionMeikecityAdvanced.Boot.getMessage("TZGD_FWINIT_00064")
                });

            /**表单校验*覆盖类Ext.form.field.HtmlEditor的提示信息属性*/
            Ext.define('tranzvision.Ext.form.field.HtmlEditor',
                {
                    override:'Ext.form.field.HtmlEditor',
                    createLinkText:TranzvisionMeikecityAdvanced.Boot.getMessage("TZGD_FWINIT_00065")
                });

            /**表单校验*覆盖类Ext.form.CheckboxGroup的提示信息属性*/
            Ext.define('tranzvision.Ext.form.CheckboxGroup',
                {
                    override:'Ext.form.CheckboxGroup',
                    blankText:TranzvisionMeikecityAdvanced.Boot.getMessage("TZGD_FWINIT_00070")
                });

            /**表单校验*覆盖类Ext.form.RadioGroup的提示信息属性*/
            Ext.define('tranzvision.Ext.form.RadioGroup',
                {
                    override:'Ext.form.RadioGroup',
                    blankText:TranzvisionMeikecityAdvanced.Boot.getMessage("TZGD_FWINIT_00071")
                });

            /**表单校验*覆盖类Ext.form.field.VTypes的提示信息属性*/
            Ext.define('tranzvision.Ext.form.field.VTypes',
                {
                    override:'Ext.form.field.VTypes',
                    'emailText':TranzvisionMeikecityAdvanced.Boot.getMessage("TZGD_FWINIT_00066"),
                    'urlText':TranzvisionMeikecityAdvanced.Boot.getMessage("TZGD_FWINIT_00067"),
                    'alphaText':TranzvisionMeikecityAdvanced.Boot.getMessage("TZGD_FWINIT_00068"),
                    'alphanumText':TranzvisionMeikecityAdvanced.Boot.getMessage("TZGD_FWINIT_00069")
                });

            /*覆盖Grid操作列类Ext.grid.column.Action的defaultRenderer方法*/
            Ext.define('tranzvision.Ext.grid.column.Action',
                {
                    override:'Ext.grid.column.Action',
                    constructor: function(config)
                    {
                        var me = this;

                        var items = (config && config.items) || me.items || [];
                        var tmpWidth = 30 + 22 * (items ? items.length : 0);

                        if(TranzvisionMeikecityAdvanced.Boot.getLanguage() !== "ZHS")
                        {
                            tmpWidth = tmpWidth >= 100 ? tmpWidth : 100;
                        }
                        else
                        {
                            tmpWidth = tmpWidth >= 60 ? tmpWidth : 60;
                        }

                        var cfg = Ext.applyIf(
                            {
                                menuDisabled: true,
                                sortable: false,
                                width: tmpWidth,
                                align: 'center'
                            }, config)

                        if(config.text)
                        {
                            cfg.text = config.text;
                        }
                        else
                        {
                            cfg.text = TranzvisionMeikecityAdvanced.Boot.getMessage("TZGD_FWINIT_00075");
                        }

                        me.callParent([cfg]);
                    },
                    defaultRenderer: function(v, cellValues, record, rowIdx, colIdx, store, view)
                    {
                        var me = this,
                            prefix = Ext.baseCSSPrefix,
                            scope = me.origScope || me,
                            items = me.items,
                            len = items.length,
                            i = 0,
                            item, ret, disabled, tooltip;

                        // Allow a configured renderer to create initial value (And set the other values in the "metadata" argument!)
                        // Assign a new variable here, since if we modify "v" it will also modify the arguments collection, meaning
                        // we will pass an incorrect value to getClass/getTip
                        ret = Ext.isFunction(me.origRenderer) ? me.origRenderer.apply(scope, arguments) || '' : '';

                        cellValues.tdCls += ' ' + Ext.baseCSSPrefix + 'action-col-cell';
                        for(; i < len; i++)
                        {
                            item = items[i];

                            disabled = item.disabled || (item.isDisabled ? item.isDisabled.call(item.scope || scope, view, rowIdx, colIdx, item, record) : false);
                            tooltip = disabled ? null : (item.tooltip || (item.getTip ? item.getTip.apply(item.scope || scope, arguments) : null));

                            // Only process the item action setup once.
                            if (!item.hasActionConfiguration)
                            {
                                // Apply our documented default to all items
                                item.stopSelection = me.stopSelection;
                                item.disable = Ext.Function.bind(me.disableAction, me, [i], 0);
                                item.enable = Ext.Function.bind(me.enableAction, me, [i], 0);
                                item.hasActionConfiguration = true;
                            }

                            ret += '<img role="button" alt="' + (item.altText || me.altText) + '" src="' + (item.icon || Ext.BLANK_IMAGE_URL) +
                                '" class="' + me.actionIconCls + ' ' + prefix + 'action-col-' + String(i) + ' ' + (disabled ? prefix + 'item-disabled' : ' ') +
                                (Ext.isFunction(item.getClass) ? item.getClass.apply(item.scope || scope, arguments) : (item.iconCls || me.iconCls || '')) + '"' +
                                (tooltip ? ' data-qtip="' + tooltip + '"' : '') + 'style="margin: 0px 2px 0px 2px" />';
                        }

                        return ret;
                    }
                });

            /*将用户个性化设置的Grid显示隐藏列存入Cookie并且在下次渲染之后记住上次的选择*/
            Ext.define('tranzvision.Ext.grid.Panel',
                {
                    override:'Ext.grid.Panel',
                    initComponent: function()
                    {
                        var me = this;

                        if(me.columns && me.columns.length >= 1 && me.columns[me.columns.length - 1].xtype === 'actioncolumn')
                        {
                            Ext.apply(me.columns[me.columns.length - 1],{margin: '0 20 0 0'});
                        }

                        me.callParent(arguments);
                    },

                    listeners:
                    {
                        columnschanged:function(ct, eOpts )
                        {
                            var grid = ct.findParentBy(function(container,ct)
                            {
                                if(Ext.ComponentQuery.is(container,"grid")&&container.name)
                                {
                                    return true;
                                }
                            });

                            if(grid)
                            {
                                var gridName = grid.name;
                                var columns = grid.columns;
                                var cookie = Ext.state.Manager.getProvider();
                                var hiddenGridColumns = cookie.get('TZGD_HIDDEN_GRIDCOLUMNS');

                                var tempHiddenColumns="",hiddenColumns="";

                                for(var i=0;i<columns.length;i++)
                                {
                                    if(columns[i].dataIndex&&columns[i].isHidden())
                                    {
                                        if(hiddenColumns=="")
                                        {
                                            hiddenColumns = columns[i].dataIndex;
                                        }
                                        else
                                        {
                                            hiddenColumns = hiddenColumns+"|"+columns[i].dataIndex;
                                        }
                                    }
                                }

                                if(hiddenColumns!="")
                                {
                                    hiddenColumns=gridName+":"+hiddenColumns;
                                }

                                cookie = new Ext.state.CookieProvider();
                                Ext.state.Manager.setProvider(cookie);

                                if(hiddenGridColumns)
                                {
                                    var tempArray = hiddenGridColumns.split(gridName+":");

                                    if(tempArray.length>1)
                                    {
                                        tempHiddenColumns = gridName+":"+(tempArray[1].split('-')[0]);
                                    }

                                    if(tempHiddenColumns=="")
                                    {
                                        cookie.set('TZGD_HIDDEN_GRIDCOLUMNS',hiddenGridColumns+"-"+hiddenColumns);
                                    }
                                    else
                                    {
                                        cookie.set('TZGD_HIDDEN_GRIDCOLUMNS',hiddenGridColumns.replace(tempHiddenColumns,hiddenColumns));
                                    }
                                }
                                else
                                {
                                    cookie.set('TZGD_HIDDEN_GRIDCOLUMNS',hiddenColumns);
                                }
                            }
                        },

                        afterrender:function(grid, eOpts )
                        {
                            var columns = grid.columns;
                            var gridName = grid.name;
                            if(gridName)
                            {
                                var cookie = Ext.state.Manager.getProvider();
                                var hiddenGridColumns = cookie.get('TZGD_HIDDEN_GRIDCOLUMNS');

                                if(hiddenGridColumns)
                                {
                                    var tempArray = hiddenGridColumns.split(gridName+":");
                                    if(tempArray.length>1)
                                    {
                                        var gridHiddenColumns = tempArray[1].split('-')[0],
                                            gridHiddenColumnsArray = gridHiddenColumns.split('|');
                                    }

                                    /*根据cookie自定义显示和隐藏columns*/
                                    for(var i=0 ; i<columns.length;i++)
                                    {
                                        if(columns[i].dataIndex)
                                        {
                                            var visible=true;
                                            if(gridHiddenColumnsArray)
                                            {
                                                for(j=0;j<gridHiddenColumnsArray.length;j++)
                                                {
                                                    if(columns[i].dataIndex==gridHiddenColumnsArray[j])
                                                    {
                                                        visible=false;
                                                        break;
                                                    }
                                                }
                                            }
                                            grid.suspendEvent( 'columnschanged' );
                                            columns[i].setVisible(visible);
                                            grid.resumeEvent( 'columnschanged' );
                                        }
                                    }
                                }
                            }
                        }
                    }
                });

            /*统一处理store*/
            Ext.apply(Ext.data.Store,
                {
                    listeners:
                    {
                        'load': function(store, records, successful, eOpts)
                        {
                            if(Ext.ClassManager.getName(store.proxy) != "Ext.data.proxy.Ajax")
                            {
                                return false;
                            }

                            try
                            {
                                //返回值
                                var responseText = eOpts.getResponse().responseText;

                                //返回值json对象
                                var jsonObject = Ext.util.JSON.decode(responseText);

                                //获取数据失败
                                if(jsonObject.state.errcode != 0)
                                {
                                    if(jsonObject.state.timeout == true)
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
                            }
                            catch(e)
                            {
                                var errorMsg = "";

                                if(eOpts.exception == true && eOpts.sucess == undefined && eOpts.error.status == 0)
                                {
                                    errorMsg = TranzvisionMeikecityAdvanced.Boot.getMessage("TZGD_FWINIT_00026");
                                }
                                else
                                {
                                    errorMsg = TranzvisionMeikecityAdvanced.Boot.getMessage("TZGD_FWINIT_00013");
                                }

                                TranzvisionMeikecityAdvanced.Boot.errorMessage(errorMsg);
                            }
                        },
                        'beforeload': function(store, operation, eOpts)
                        {
                            var comID = store.comID;
                            var pageID = store.pageID;
                            var tzStoreParams = store.tzStoreParams;
                            if(tzStoreParams == "" || tzStoreParams == undefined){
                                tzStoreParams = "{}";
                            }
                            //参数
                            var tzParams = "";
                            //组件ID和页面ID不为空
                            if(comID != "" && comID != undefined && pageID != "" && pageID != undefined){
                                tzParams = '{"ComID":"'+comID+'","PageID":"'+pageID+'","OperateType":"QG","comParams":'+tzStoreParams+'}';
								//console.log(tzParams);
                            }

                            if(store.tzType != undefined && store.tzType != ""){
                                tzParams = tzStoreParams;
                            }

                            if(Ext.ClassManager.getName(store.proxy)=="Ext.data.proxy.Ajax" && tzParams != "")
                            {
                                store.proxy.setExtraParams({tzParams:tzParams});
                            }

                            if(store.isFiltered( ))
                            {
                                store.clearFilter(true);
                            }
                        }
                    }
                });

            /*如果主框架资源加载成功，则启动美课城高端产品主框架程序，否则显示错误信息*/
            if(TranzvisionMeikecityAdvanced.Boot.error.failed === true)
            {/*主框架资源加载失败*/
                document.getElementById("tranzvision_mkc_advanced_default_text").innerHTML = TranzvisionMeikecityAdvanced.Boot.getMessage("TZGD_FWINIT_00010") + TranzvisionMeikecityAdvanced.Boot.error.description;
            }
            else
            {/*主框架资源加载成功*/
                Ext.get("tranzvision_mkc_advanced_default_text").setHtml(TranzvisionMeikecityAdvanced.Boot.getTitle());

                /*加载主菜单数据*/
                TranzvisionMeikecityAdvanced.Boot.getMainmenuTree();
                if(TranzvisionMeikecityAdvanced.Boot.error.failed === true)
                {
                    TranzvisionMeikecityAdvanced.Boot.errorMessage(TranzvisionMeikecityAdvanced.Boot.error.description);
                }
                else
                {
                    TranzvisionMeikecityAdvanced.frameworkLoader.createMeikecityAdvancedFramework();
                }
            }
        });
    }
    catch(e)
    {
        document.title = TranzvisionMeikecityAdvanced.Boot.getTitle();
        alert(TranzvisionMeikecityAdvanced.Boot.error.description);
    }
})();