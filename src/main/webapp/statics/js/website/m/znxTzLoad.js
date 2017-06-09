var dropObj;

function getZnxList(siteid,pagenum){	
	$('#tz-viewport-contents').dropload({
		scrollArea: window,
		domUp : {  
            domClass   : 'dropload-up',  
            domRefresh : '<div class="dropload-refresh">↓下拉刷新</div>',  
            domUpdate  : '<div class="dropload-update">↑释放更新</div>',  
            domLoad    : '<div class="dropload-load"><span class="loading"></span>加载中...</div>'  
        },  
        domDown : {  
            domClass   : 'dropload-down',  
            domRefresh : '<div class="dropload-refresh">↑上拉加载更多</div>',  
            domLoad    : '<div class="dropload-load"><span class="loading"></span>加载中...</div>',  
            domNoData  : '<div class="dropload-noData">站内信已全部加载</div>'  
        },  
		loadUpFn: function(me){
			pagenum = 1;
			dropObj = me;
			$.ajax({
				type: 'GET',
				data:{
					"tzParams": '{"ComID":"TZ_M_WEB_INDEX_COM","PageID":"TZ_M_SYSINFO_STD","OperateType":"LOADZNX","comParams":{"siteId": "'+ siteid + '","pagenum": '+pagenum+'}}'
				},
				url: TzUniversityContextPath+"/dispatcher",
				dataType: 'json',
				success: function(result) {	
					pagenum = pagenum + 1;
					var resultNum = result.comContent.resultNum;
					if(resultNum > 0){
						// 插入数据到页面，放到最后面
	                	$('#tz-znx-list-container').html(result.comContent.result);

	                	initZnxListStyle();
	                	
	                	// 每次数据插入，必须重置
	                    me.resetload();
	                    me.unlock();
					}
				},
				error: function(xhr, type) {
					alert('数据加载失败!');
					me.resetload();
				}
			});
		},
		loadDownFn: function(me) {
			dropObj = me;
			$.ajax({
				type: 'GET',
				data:{
					"tzParams": '{"ComID":"TZ_M_WEB_INDEX_COM","PageID":"TZ_M_SYSINFO_STD","OperateType":"LOADZNX","comParams":{"siteId": "'+ siteid + '","pagenum": '+pagenum+'}}'
				},
				url: TzUniversityContextPath+"/dispatcher",
				dataType: 'json',
				success: function(result) {	
					pagenum=pagenum+1;
					var resultNum = result.comContent.resultNum;
					if(resultNum > 0){
						// 插入数据到页面，放到最后面
	                	$('#tz-znx-list-container').append(result.comContent.result);
	                	/*
	                	$(".slide").click(function(){
	                        $(this).children('i').toggleClass('slide_up');
	                        $(this).prev().toggleClass('slide_wz');
	                        var mailId = ($(this).attr("mailid"));
	                        var updateRecords = [{"mailId":mailId}];
	                        $.ajax({
	            				type: 'GET',
	            				url: TzUniversityContextPath+"/dispatcher",
	            				data:{
	            					"tzParams": '{"ComID":"TZ_M_WEB_INDEX_COM","PageID":"TZ_M_SYSINFO_STD","OperateType":"U","comParams":{"update": '+ JSON.stringify(updateRecords) + '}}'
	            				}
	                        });
	                    });
	                  
	                	
	                	$(".viewZnxContent").click(function(){
	                        var mailId = ($(this).attr("mailid"));
	                        var updateRecords = [{"mailId":mailId}];
	                        $.ajax({
	            				type: 'GET',
	            				url: TzUniversityContextPath+"/dispatcher",
	            				data:{
	            					"tzParams": '{"ComID":"TZ_M_WEB_INDEX_COM","PageID":"TZ_M_SYSINFO_STD","OperateType":"U","comParams":{"update": '+ JSON.stringify(updateRecords) + '}}'
	            				}
	                        });
	                    });
	                    */
	                	
	                	initZnxListStyle();
					}else{
						 // 锁定
						me.lock();
                        // 无数据
                        me.noData(0);
					}
					
					// 每次数据插入，必须重置
                    me.resetload();
				},
				error: function(xhr, type) {
					alert('数据加载失败!');
					me.resetload();
				}
			});
		}
	});
}


function showZnxDetails(el,znxMsgId){
	var contentsEl = $("#tz-details-contents");
	if(contentsEl.attr("msgId") == znxMsgId){
		$("#tz-details-container").css("display","block");
		$(".viewport-adaptive").css("display","none");
	}else{
		$.ajax({
			type: 'GET',
			data:{
				"tzParams": '{"ComID":"TZ_M_WEB_INDEX_COM","PageID":"TZ_M_ZNX_XQ_STD","OperateType":"getZnxDetailsHtml","comParams":{"znxMsgId": "'+ znxMsgId + '"}}'
			},
			url: TzUniversityContextPath+"/dispatcher",
			dataType: 'json',
			success: function(result) {	
				var resultNum = result.state.errcode;
				if(resultNum == "0"){
					//设置为已读
					var znxTitle = $(el).find(".newz_left p:first");
					if(!znxTitle.hasClass("newz_read")) znxTitle.addClass("newz_read");
					
					var detailsHtml = result.comContent.znxDetailsHtml;
					contentsEl.html(detailsHtml);
					contentsEl.attr("msgId", znxMsgId);
					$("#tz-details-container").css("display","block");
					$(".viewport-adaptive").css("display","none");
				}else{
					alert(result.state.errdesc);
				}
			},
			error: function(xhr, type) {
				alert('数据加载失败!');
			}
		});
	}
}

function backToZnxList(el){
	$("#tz-details-container").css("display","none");
	$(".viewport-adaptive").css("display","block");
}


function deleteZnx(el, znxMsgId){
	var $removeEl = $(el).closest(".line-wrapper");
	
	$.ajax({
		type: 'GET',
		data:{
			"tzParams": '{"ComID":"TZ_WEBSITE_ZNX_COM","PageID":"TZ_WEBSITE_ZNX_STD","OperateType":"U","comParams":{"update":[{"mailId":"'+ znxMsgId +'"}]}}'
		},
		url: TzUniversityContextPath+"/dispatcher",
		dataType: 'json',
		async: false,
		success: function(result) {	
			var resultNum = result.state.errcode;
			if(resultNum == "0"){
				$removeEl.slideUp("slow", function() {
					$removeEl.remove();

					dropObj.resetload();
			    });
			}else{
				alert("删除失败，"+result.state.errdesc);
			}
		},
		error: function(xhr, type) {
			alert('数据删除失败!');
		}
	});
}


function initZnxListStyle(){
	// 设定每一行的宽度=屏幕宽度+按钮宽度
    $(".line-scroll-wrapper").width($(".line-wrapper").width() + $(".line-btn-delete").width());
    // 设定常规信息区域宽度=屏幕宽度
    $(".line-normal-wrapper").width($(".line-wrapper").width());

    // 获取所有行，对每一行设置监听 
    var lines = $(".line-normal-wrapper");
    var len = lines.length; 
    var lastX, lastXForMobile;

    // 用于记录被按下的对象
    var pressedObj;  // 当前左滑的对象
    var lastLeftObj; // 上一个左滑的对象

    // 用于记录按下的点
    var start;

    // 网页在移动端运行时的监听
    for (var i = 0; i < len; ++i) {
        lines[i].addEventListener('touchstart', function(e){
            lastXForMobile = e.changedTouches[0].pageX;
            pressedObj = this; // 记录被按下的对象 

            // 记录开始按下时的点
            var touches = event.touches[0];
            start = { 
                x: touches.pageX, // 横坐标
                y: touches.pageY  // 纵坐标
            };
        });

        lines[i].addEventListener('touchmove',function(e){
            // 计算划动过程中x和y的变化量
            var touches = event.touches[0];
            delta = {
                x: touches.pageX - start.x,
                y: touches.pageY - start.y
            };

            // 横向位移大于纵向位移，阻止纵向滚动
            if (Math.abs(delta.x) > Math.abs(delta.y)) {
                event.preventDefault();
            }
        });

        lines[i].addEventListener('touchend', function(e){
            if (lastLeftObj && pressedObj != lastLeftObj) { // 点击除当前左滑对象之外的任意其他位置
                $(lastLeftObj).animate({marginLeft:"0"}, 500); // 右滑
                lastLeftObj = null; // 清空上一个左滑的对象
            }
            var diffX = e.changedTouches[0].pageX - lastXForMobile;
            if (diffX < -150) {
                $(pressedObj).animate({marginLeft:"-80px"}, 500); // 左滑
                lastLeftObj && lastLeftObj != pressedObj && 
                    $(lastLeftObj).animate({marginLeft:"0"}, 500); // 已经左滑状态的按钮右滑
                lastLeftObj = pressedObj; // 记录上一个左滑的对象
            } else if (diffX > 150) {
              if (pressedObj == lastLeftObj) {
                $(pressedObj).animate({marginLeft:"0"}, 500); // 右滑
                lastLeftObj = null; // 清空上一个左滑的对象
              }
            }
        });
    }

    // 网页在PC浏览器中运行时的监听
    for (var i = 0; i < len; ++i) {
        $(lines[i]).bind('mousedown', function(e){
            lastX = e.clientX;
            pressedObj = this; // 记录被按下的对象
        });

        $(lines[i]).bind('mouseup', function(e){
            if (lastLeftObj && pressedObj != lastLeftObj) { // 点击除当前左滑对象之外的任意其他位置
                $(lastLeftObj).animate({marginLeft:"0"}, 500); // 右滑
                lastLeftObj = null; // 清空上一个左滑的对象
            }
            var diffX = e.clientX - lastX;
            if (diffX < -150) {
                $(pressedObj).animate({marginLeft:"-80px"}, 500); // 左滑
                lastLeftObj && lastLeftObj != pressedObj && 
                    $(lastLeftObj).animate({marginLeft:"0"}, 500); // 已经左滑状态的按钮右滑
                lastLeftObj = pressedObj; // 记录上一个左滑的对象
            } else if (diffX > 150) {
              if (pressedObj == lastLeftObj) {
                $(pressedObj).animate({marginLeft:"0"}, 500); // 右滑
                lastLeftObj = null; // 清空上一个左滑的对象
              }
            }
        });
    }
}