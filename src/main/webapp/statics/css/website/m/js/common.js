$(document).ready(function(){
   
   // 切换
     tab(".zhaos .list",".zhaos .tabNote","list_on","mousedown");
    //关闭弹窗
     $("#pop_btn").click(function(){
       $(".Shade").hide();
       $(".pop").hide();
  
    });

});
$(window).load(function(){
  initStyles();
});

$(window).resize(function(){
  initStyles();
});


function tab(nav,content,on,type)
      {
        $(nav).children().bind(type,(function(){
          var $tab=$(this);
          var tab_index=$tab.prevAll().length;
          var $content = $(content).children();
          $(nav).children().removeClass(on);
          $tab.addClass(on);
          $content.hide();
          $content.eq(tab_index).show();
        }));
      }



function initStyles() {
  $("#body_bg").css("height",$("#bg_img").height()+"px");
}