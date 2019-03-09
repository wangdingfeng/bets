/**
 Core script to handle the entire theme and core functions
 **/
var App = function () {

    // IE mode
    var isRTL = false;
    var isIE8 = false;
    var isIE9 = false;
    var isIE10 = false;

    var resizeHandlers = [];

    var basePath = 'bets/';

    var globalImgPath = '/img/';

    var globalPluginsPath = '/plugins/';

    var globalCssPath = '/css/';

    // 判断浏览器
    var handleInit = function () {

        if ($('body').css('direction') === 'rtl') {
            isRTL = true;
        }

        isIE8 = !!navigator.userAgent.match(/MSIE 8.0/);
        isIE9 = !!navigator.userAgent.match(/MSIE 9.0/);
        isIE10 = !!navigator.userAgent.match(/MSIE 10.0/);

        if (isIE10) {
            $('html').addClass('ie10'); // detect IE10 version
        }

        if (isIE10 || isIE9 || isIE8) {
            $('html').addClass('ie'); // detect IE10 version
        }
    };

    // runs callback functions set by App.addResponsiveHandler().
    var _runResizeHandlers = function () {
        // reinitialize other subscribed elements
        for (var i = 0; i < resizeHandlers.length; i++) {
            var each = resizeHandlers[i];
            each.call();
        }
    };

    //初始化iframe内容页高度
    var handleIframeContent = function () {
        var ht = $(window).height();//获取浏览器窗口的整体高度；

        var $footer = $(".main-footer");
        var $header = $(".main-header");
        var $tabs = $(".content-tabs");

        var height = App.getViewPort().height - $footer.outerHeight() - $header.outerHeight();
        if ($tabs.is(":visible")) {
            height = height - $tabs.outerHeight();
        }

        $(".tab_iframe").css({
            height: height,
            width: "100%"
        });
    };
    //初始化内容页layout组件高度
    var handleIframeLayoutHeight = function () {

        var height = App.getViewPort().height - $('.page-footer').outerHeight() - $('.page-header').outerHeight() - $(".content-tabs").height();
        return height;
    };
    var isFullScreen = false;
    /**
     * 处理全屏
     */
    var requestFullScreen = function () {
        var de = document.documentElement;
        //W3C
        if (de.requestFullscreen) {
            de.requestFullscreen();
        }
        //FireFox
        else if (de.mozRequestFullScreen) {
            de.mozRequestFullScreen();
        }
        //Chrome等
        else if (de.webkitRequestFullScreen) {
            de.webkitRequestFullScreen();
        }
        //IE11
        else if (de.msRequestFullscreen) {
            de.msRequestFullscreen();
        } else {
            App.alert({message: "该浏览器不支持全屏！", type: "danger"});
        }

    };

    //退出全屏 判断浏览器种类
    var exitFull = function () {
        // 判断各种浏览器，找到正确的方法
        var exitMethod = document.exitFullscreen || //W3C
            document.mozCancelFullScreen ||    //Chrome等
            document.webkitExitFullscreen || //FireFox
            document.webkitExitFullscreen; //IE11
        if (exitMethod) {
            exitMethod.call(document);
        } else if (typeof window.ActiveXObject !== "undefined") {//for Internet Explorer
            var wscript = new ActiveXObject("WScript.Shell");
            if (wscript !== null) {
                wscript.SendKeys("{F11}");
            }
        }
    };
    /**
     * 使用jQuery SlimScroll插件处理可滚动内容
     */
    var handleScrollers = function () {
        App.initSlimScroll('.scroller');
    };


    // 在窗口调整大小时处理布局重新初始化
    var handleOnResize = function () {
        var resize;
        if (isIE8) {
            var currheight;
            $(window).resize(function () {
                //body窗口调整不是window窗口调整.
                if (currheight == document.documentElement.clientHeight) {
                    return;
                }
                if (resize) {
                    clearTimeout(resize);
                }
                resize = setTimeout(function () {
                    _runResizeHandlers();
                    handleIframeContent();
                }, 50); // 等待50ms完成窗口调整.
                currheight = document.documentElement.clientHeight; // 存储最后一个body窗口的大小
            });
        } else {
            $(window).resize(function () {
                if (resize) {
                    clearTimeout(resize);
                }
                resize = setTimeout(function () {
                    _runResizeHandlers();
                    handleIframeContent();
                }, 50);
            });
        }
    };

    return {
        //初始化插件
        init: function () {
            handleInit(); // initialize core variables
            handleOnResize(); // set and handle responsive
            handleScrollers();
        },
        handleFullScreen: function () {
            if (isFullScreen) {
                exitFull();
                isFullScreen = false;
            } else {
                requestFullScreen();
                isFullScreen = true;
            }
        },
        fixIframeTab: function () {
            handleIframeContent();
        },
        getIframeLayoutHeight: function () {
            return handleIframeLayoutHeight();
        },
        //重新计算高度
        fixIframeContent: function () {
            setTimeout(function () {
                handleIframeContent();
            }, 50);
        },

        // wrApper function to scroll(focus) to an element
        scrollTo: function (el, offeset) {
            var pos = (el && el.size() > 0) ? el.offset().top : 0;

            if (el) {
                if ($('body').hasClass('page-header-fixed')) {
                    pos = pos - $('.page-header').height();
                } else if ($('body').hasClass('page-header-top-fixed')) {
                    pos = pos - $('.page-header-top').height();
                } else if ($('body').hasClass('page-header-menu-fixed')) {
                    pos = pos - $('.page-header-menu').height();
                }
                pos = pos + (offeset ? offeset : -1 * el.height());
            }

            $('html,body').animate({
                scrollTop: pos
            }, 'slow');
        },

        initSlimScroll: function (el) {
            $(el).each(function () {
                if ($(this).attr("data-initialized")) {
                    return; // exit
                }

                var height;

                if ($(this).attr("data-height")) {
                    height = $(this).attr("data-height");
                } else {
                    height = $(this).css('height');
                }

                $(this).slimScroll({
                    allowPageScroll: true, // allow page scroll when the element scroll is ended
                    size: '7px',
                    color: ($(this).attr("data-handle-color") ? $(this).attr("data-handle-color") : '#bbb'),
                    wrapperClass: ($(this).attr("data-wrapper-class") ? $(this).attr("data-wrapper-class") : 'slimScrollDiv'),
                    railColor: ($(this).attr("data-rail-color") ? $(this).attr("data-rail-color") : '#eaeaea'),
                    position: isRTL ? 'left' : 'right',
                    height: height,
                    alwaysVisible: ($(this).attr("data-always-visible") == "1" ? true : false),
                    railVisible: ($(this).attr("data-rail-visible") == "1" ? true : false),
                    disableFadeOut: true
                });

                $(this).attr("data-initialized", "1");
            });
        },

        destroySlimScroll: function (el) {
            $(el).each(function () {
                if ($(this).attr("data-initialized") === "1") { // destroy existing instance before updating the height
                    $(this).removeAttr("data-initialized");
                    $(this).removeAttr("style");

                    var attrList = {};

                    // store the custom attribures so later we will reassign.
                    if ($(this).attr("data-handle-color")) {
                        attrList["data-handle-color"] = $(this).attr("data-handle-color");
                    }
                    if ($(this).attr("data-wrapper-class")) {
                        attrList["data-wrapper-class"] = $(this).attr("data-wrapper-class");
                    }
                    if ($(this).attr("data-rail-color")) {
                        attrList["data-rail-color"] = $(this).attr("data-rail-color");
                    }
                    if ($(this).attr("data-always-visible")) {
                        attrList["data-always-visible"] = $(this).attr("data-always-visible");
                    }
                    if ($(this).attr("data-rail-visible")) {
                        attrList["data-rail-visible"] = $(this).attr("data-rail-visible");
                    }

                    $(this).slimScroll({
                        wrapperClass: ($(this).attr("data-wrapper-class") ? $(this).attr("data-wrapper-class") : 'slimScrollDiv'),
                        destroy: true
                    });

                    var the = $(this);

                    // reassign custom attributes
                    $.each(attrList, function (key, value) {
                        the.attr(key, value);
                    });

                }
            });
        },

        // function to scroll to the top
        scrollTop: function () {
            App.scrollTo();
        },
        // 遮罩层
        blockUI: function (options) {
            options = $.extend(true, {}, options);
            var html = '';
            if (options.animate) {
                html = '<div class="loading-message ' + (options.boxed ? 'loading-message-boxed' : '') + '">' + '<div class="block-spinner-bar"><div class="bounce1"></div><div class="bounce2"></div><div class="bounce3"></div></div>' + '</div>';
            }else {
                html = '<div class="loading-message ' + (options.boxed ? 'loading-message-boxed' : '') + '"><img src="' + this.getGlobalImgPath() + 'loading/loading-spinner-grey.gif" align=""><span>&nbsp;&nbsp;' + (options.message ? options.message : '加载中...') + '</span></div>';
            }
            if (options.target) { // element blocking
                var el = $(options.target);
                if (el.height() <= ($(window).height())) {
                    options.cenrerY = true;
                }
                el.block({
                    message: html,
                    baseZ: options.zIndex ? options.zIndex : 1000,
                    centerY: options.cenrerY !== undefined ? options.cenrerY : false,
                    css: {
                        top: '10%',
                        border: '0',
                        padding: '0',
                        backgroundColor: 'none'
                    },
                    overlayCSS: {
                        backgroundColor: options.overlayColor ? options.overlayColor : '#555',
                        opacity: options.boxed ? 0.05 : 0.1,
                        cursor: 'wait'
                    }
                });
            } else { // page blocking
                $.blockUI({
                    message: html,
                    baseZ: options.zIndex ? options.zIndex : 1000,
                    css: {
                        border: '0',
                        padding: '0',
                        backgroundColor: 'none'
                    },
                    overlayCSS: {
                        backgroundColor: options.overlayColor ? options.overlayColor : '#555',
                        opacity: options.boxed ? 0.05 : 0.1,
                        cursor: 'wait'
                    }
                });
            }
        },

        // 关闭遮罩层
        unblockUI: function (target) {
            if (target) {
                $(target).unblock({
                    onUnblock: function () {
                        $(target).css('position', '');
                        $(target).css('zoom', '');
                    }
                });
            } else {
                $.unblockUI();
            }
        },
        // 检查设备触摸支持
        isTouchDevice: function () {
            try {
                document.createEvent("TouchEvent");
                return true;
            } catch (e) {
                return false;
            }
        },

        // To get the correct viewport width based on  http://andylangton.co.uk/articles/javascript/get-viewport-size-javascript/
        getViewPort: function () {
            var e = window,
                a = 'inner';
            if (!('innerWidth' in window)) {
                a = 'client';
                e = document.documentElement || document.body;
            }

            return {
                width: e[a + 'Width'],
                height: e[a + 'Height']
            };
        },
        //获取uuid
        getUniqueID: function (prefix) {
            return 'prefix_' + Math.floor(Math.random() * (new Date()).getTime());
        },

        // 判断是否是IE8
        isIE8: function () {
            return isIE8;
        },

        // 判断是否是IE9
        isIE9: function () {
            return isIE9;
        },

        //check RTL mode
        isRTL: function () {
            return isRTL;
        },

        // check IE8 mode
        isAngularJsApp: function () {
            return (typeof angular == 'undefined') ? false : true;
        },
        //获取项目路径
        getbasePath: function () {
            return basePath;
        },

        setbasePath: function (path) {
            basePath = path;
        },

        setGlobalImgPath: function (path) {
            globalImgPath = path;
        },
        //获取图片路径
        getGlobalImgPath: function () {
            return basePath + globalImgPath;
        },

        setGlobalPluginsPath: function (path) {
            globalPluginsPath = path;
        },

        getGlobalPluginsPath: function () {
            return basePath + globalPluginsPath;
        },

        getGlobalCssPath: function () {
            return basePath + globalCssPath;
        },

        getResponsiveBreakpoint: function (size) {
            // bootstrap responsive breakpoints
            var sizes = {
                'xs': 480,     // extra small
                'sm': 768,     // small
                'md': 992,     // medium
                'lg': 1200     // large
            };

            return sizes[size] ? sizes[size] : 0;
        }
    };

}();

jQuery(document).ready(function () {
    App.init(); // init metronic core componets
});
/*
 * Context.js 右键菜单
 * Copyright Jacob Kelley
 * MIT License
 *
 * Modified by Joshua Christman
 */

context = (function () {

    var options = {
        fadeSpeed: 100,
        filter: function ($obj) {
            // Modify $obj, Do not return
        },
        above: 'auto',
        left: 'auto',
        preventDoubleContext: true,
        compress: false
    };

    function initialize(opts) {

        options = $.extend({}, options, opts);

        $(document).on('click', function () {
            $('.dropdown-context').fadeOut(options.fadeSpeed, function () {
                $('.dropdown-context').css({display: ''}).find('.drop-left').removeClass('drop-left');
            });
        });
        if (options.preventDoubleContext) {
            $(document).on('contextmenu', '.dropdown-context', function (e) {
                e.preventDefault();
            });
        }
        $(document).on('mouseenter', '.dropdown-submenu', function () {
            var $sub = $(this).find('.dropdown-context-sub:first'),
                subWidth = $sub.width(),
                subLeft = $sub.offset().left,
                collision = (subWidth + subLeft) > window.innerWidth;
            if (collision) {
                $sub.addClass('drop-left');
            }
        });

    }

    function updateOptions(opts) {
        options = $.extend({}, options, opts);
    }

    function buildMenu(data, id, subMenu) {
        var subClass = (subMenu) ? ' dropdown-context-sub' : '',
            compressed = options.compress ? ' compressed-context' : '',
            $menu = $('<ul class="dropdown-menu dropdown-context' + subClass + compressed + '" id="dropdown-' + id + '"></ul>');

        return buildMenuItems($menu, data, id, subMenu);
    }

    function buildMenuItems($menu, data, id, subMenu, addDynamicTag) {
        var linkTarget = '';
        for (var i = 0; i < data.length; i++) {
            if (typeof data[i].divider !== 'undefined') {
                var divider = '<li class="divider';
                divider += (addDynamicTag) ? ' dynamic-menu-item' : '';
                divider += '"></li>';
                $menu.append(divider);
            } else if (typeof data[i].header !== 'undefined') {
                var header = '<li class="nav-header';
                header += (addDynamicTag) ? ' dynamic-menu-item' : '';
                header += '">' + data[i].header + '</li>';
                $menu.append(header);
            } else if (typeof data[i].menu_item_src !== 'undefined') {
                var funcName;
                if (typeof data[i].menu_item_src === 'function') {
                    if (data[i].menu_item_src.name === "") { // The function is declared like "foo = function() {}"
                        for (var globalVar in window) {
                            if (data[i].menu_item_src == window[globalVar]) {
                                funcName = globalVar;
                                break;
                            }
                        }
                    } else {
                        funcName = data[i].menu_item_src.name;
                    }
                } else {
                    funcName = data[i].menu_item_src;
                }
                $menu.append('<li class="dynamic-menu-src" data-src="' + funcName + '"></li>');
            } else {
                if (typeof data[i].href == 'undefined') {
                    data[i].href = 'javascript:void(0)';
                }
                if (typeof data[i].target !== 'undefined') {
                    linkTarget = ' target="' + data[i].target + '"';
                }
                if (typeof data[i].subMenu !== 'undefined') {
                    var sub_menu = '<li class="dropdown-submenu';
                    sub_menu += (addDynamicTag) ? ' dynamic-menu-item' : '';
                    sub_menu += '"><a tabindex="-1" href="' + data[i].href + '">' + data[i].text + '</a></li>'
                    $sub = (sub_menu);
                } else {
                    var element = '<li';
                    element += (addDynamicTag) ? ' class="dynamic-menu-item"' : '';
                    element += '><a tabindex="-1" href="' + data[i].href + '"' + linkTarget + '>';
                    if (typeof data[i].icon !== 'undefined')
                        element += '<span class="glyphicon ' + data[i].icon + '"></span> ';
                    element += data[i].text + '</a></li>';
                    $sub = $(element);
                }
                if (typeof data[i].action !== 'undefined') {
                    $action = data[i].action;
                    $sub
                        .find('a')
                        .addClass('context-event')
                        .on('click', createCallback($action));
                }
                $menu.append($sub);
                if (typeof data[i].subMenu != 'undefined') {
                    var subMenuData = buildMenu(data[i].subMenu, id, true);
                    $menu.find('li:last').append(subMenuData);
                }
            }
            if (typeof options.filter == 'function') {
                options.filter($menu.find('li:last'));
            }
        }
        return $menu;
    }

    function addContext(selector, data) {
        if (typeof data.id !== 'undefined' && typeof data.data !== 'undefined') {
            var id = data.id;
            $menu = $('body').find('#dropdown-' + id)[0];
            if (typeof $menu === 'undefined') {
                $menu = buildMenu(data.data, id);
                $('body').append($menu);
            }
        } else {
            var d = new Date(),
                id = d.getTime(),
                $menu = buildMenu(data, id);
            $('body').append($menu);
        }

        //右键事件
        $(selector).on('contextmenu', function (e) {
            e.preventDefault();
            e.stopPropagation();

            rightClickEvent = e;
            currentContextSelector = $(this);

            $('.dropdown-context:not(.dropdown-context-sub)').hide();

            $dd = $('#dropdown-' + id);

            $dd.find('.dynamic-menu-item').remove(); // Destroy any old dynamic menu items
            $dd.find('.dynamic-menu-src').each(function (idx, element) {
                var menuItems = window[$(element).data('src')]($(selector));
                $parentMenu = $(element).closest('.dropdown-menu.dropdown-context');
                $parentMenu = buildMenuItems($parentMenu, menuItems, id, undefined, true);
            });

            if (typeof options.above == 'boolean' && options.above) {
                $dd.addClass('dropdown-context-up').css({
                    top: e.pageY - 20 - $('#dropdown-' + id).height(),
                    left: e.pageX - 13
                }).fadeIn(options.fadeSpeed);
            } else if (typeof options.above == 'string' && options.above == 'auto') {
                $dd.removeClass('dropdown-context-up');
                var autoH = $dd.height() + 12;
                if ((e.pageY + autoH) > $('html').height()) {
                    $dd.addClass('dropdown-context-up').css({
                        top: e.pageY - 20 - autoH,
                        left: e.pageX - 13
                    }).fadeIn(options.fadeSpeed);
                } else {
                    $dd.css({
                        top: e.pageY + 10,
                        left: e.pageX - 13
                    }).fadeIn(options.fadeSpeed);
                }
            }

            if (typeof options.left == 'boolean' && options.left) {
                $dd.addClass('dropdown-context-left').css({
                    left: e.pageX - $dd.width()
                }).fadeIn(options.fadeSpeed);
            } else if (typeof options.left == 'string' && options.left == 'auto') {
                $dd.removeClass('dropdown-context-left');
                var autoL = $dd.width() - 12;
                if ((e.pageX + autoL) > $('html').width()) {
                    $dd.addClass('dropdown-context-left').css({
                        left: e.pageX - $dd.width() + 13
                    });
                }
            }
        });
    }

    function destroyContext(selector) {
        $(document).off('contextmenu', selector).off('click', '.context-event');
    }

    return {
        init: initialize,
        settings: updateOptions,
        attach: addContext,
        destroy: destroyContext
    };
})();

var createCallback = function (func) {
    return function (event) {
        func(event, currentContextSelector, rightClickEvent)
    };
};

var currentContextSelector = undefined;
var rightClickEvent = undefined;


//保存页面id的field
var pageIdField = "data-pageId";
//保存页面title
var pageTitleClass = "page_tab_title";

function getPageId(element) {
    if (element instanceof jQuery) {
        return element.attr(pageIdField);
    } else {
        return $(element).attr(pageIdField);
    }
}

/**
 * 通过 title获取页面
 * @param title
 * @returns {*}
 */
function findTabPanleByTitle(title) {
    var pageId = null;
    $(".page-tabs-content").find("a.menu_tab").each(function () {
        var $a = $(this);
        if ($a.find("." + pageTitleClass).text() == title) {
            pageId = $a.attr(pageIdField);
            return false;//退出循环
        }
    });
    return pageId;
}

/**
 * 根据id 获取页面title
 * @param pageId
 * @returns {*}
 */
function findTabTitle(pageId) {
    var $ele = null;
    $(".page-tabs-content").find("a.menu_tab").each(function () {
        var $a = $(this);
        if ($a.attr(pageIdField) == pageId) {
            $ele = $a;
            return false;//退出循环
        }
    });
    return $ele;
}

/**
 * 获取tab页
 * @param pageId
 * @returns {*}
 */
function findTabPanel(pageId) {
    var $ele = null;
    $("#tab-content").find("div.tab-pane").each(function () {
        var $div = $(this);
        if ($div.attr(pageIdField) == pageId) {
            $ele = $div;
            return false;//退出循环
        }
    });
    return $ele;
}

/**
 * 获取iframe
 * @param pageId
 * @returns {*}
 */
function findIframeById(pageId) {
    return findTabPanel(pageId).children("iframe");
}

/**
 * 获取选择的tabId
 * @returns {*}
 */
function getActivePageId() {
    var $a = $('.page-tabs-content').find('.active');
    return getPageId($a);
}

/**
 * 移除tab
 * @param pageId
 * @returns {boolean}
 */
function canRemoveTab(pageId) {
    return findTabTitle(pageId).find('.fa-remove').size() > 0;
}

//添加tab
var addTabs = function (options) {
    var defaultTabOptions = {
        id: Math.random() * 200,
        urlType: "relative",
        title: "新页面"
    };
    options = $.extend(true, defaultTabOptions, options);
    if (options.urlType === "relative") {
        options.url = App.getbasePath() + options.url;
    }

    var pageId = options.id;

    //判断这个id的tab是否已经存在,不存在就新建一个
    if (findTabPanel(pageId) === null) {

        //创建新TAB的title

        var $title = $('<a href="javascript:void(0);"></a>').attr(pageIdField, pageId).addClass("menu_tab");

        var $text = $("<span class='page_tab_title'></span>").text(options.title).appendTo($title);

        //是否允许关闭
        if (options.close) {
            $("<i class='fa fa-times fa-remove' style='cursor: pointer;margin-left:4px' onclick='closeTab(this);'></i>").attr(pageIdField, pageId).appendTo($title);
        }

        //加入TABS
        $(".page-tabs-content").append($title);

        var $tabPanel = $('<div role="tabpanel" class="tab-pane"></div>').attr(pageIdField, pageId);

        if (options.content) {
            //是否指定TAB内容
            $tabPanel.append(options.content);
        } else {
            //没有内容，使用IFRAME打开链接
            //table 刷新会营销 加载显示 所以 列表页的加载方式 修改为 table的加载
            if(options.url.search("list") == -1){
                App.blockUI({
                    target: '#tab-content',
                    boxed: true,
                    message: '加载中......'
                });
            }

            var $iframe = $("<iframe></iframe>").attr("src", options.url).css("width", "100%").attr("frameborder", "no").attr("id", "iframe_" + pageId).addClass("tab_iframe").attr(pageIdField, pageId);
            //iframe 加载完成事件
            $iframe.load(function () {
                if(options.url.search("list") == -1){
                    //没有内容，使用IFRAME打开链接
                    App.unblockUI("#tab-content");
                }
                App.fixIframeContent();//修正高度
            });

            $tabPanel.append($iframe);

        }
        $("#tab-content").append($tabPanel);

    }
    activeTabByPageId(pageId)
};

//关闭tab
var closeTab = function (item) {
    //item可以是a标签,也可以是i标签
    //它们都有data-id属性,获取完成之后就没事了
    var pageId = getPageId(item);
    closeTabByPageId(pageId);
};

/**
 * 通过title刷新页面
 * @param title
 */
function refreshTabByTitle(title) {
    var pageId = findTabPanleByTitle(title);
    try {
        refreshTabById(pageId);
    } catch (e) {
        console.error("没有此title");
    }
}

/**
 * 关闭tab
 * @param pageId
 */
function closeTabByPageId(pageId) {
    var $title = findTabTitle(pageId);//有tab的标题
    var $tabPanel = findTabPanel(pageId);//装有iframe

    if ($title.hasClass("active")) {
        //要关闭的tab处于活动状态
        //要把active class传递给其它tab

        //优先传递给后面的tab,没有的话就传递给前一个
        var $nextTitle = $title.next();
        var activePageId;
        if ($nextTitle.size() > 0) {
            activePageId = getPageId($nextTitle);
        } else {
            activePageId = getPageId($title.prev());
        }

        setTimeout(function () {
            //某种bug，需要延迟执行
            activeTabByPageId(activePageId);
        }, 100);

    } else {
        //要关闭的tab不处于活动状态
        //直接移除就可以了,不用传active class

    }

    $title.remove();
    $tabPanel.remove();

}

function closeTabOnly(pageId) {
    var $title = findTabTitle(pageId);//有tab的标题
    var $tabPanel = findTabPanel(pageId);//装有iframe
    $title.remove();
    $tabPanel.remove();
}

var closeCurrentTab = function () {
    var pageId = getActivePageId();
    if (canRemoveTab(pageId)) {
        closeTabByPageId(pageId);
    }
};
//关闭页面 并刷新表格
var closeCurrentTabTable = function () {
    var pageId = getActivePageId();
    //获取上一个pageId
    var lastPageId =  $('.page-tabs-content').find('.active').prev().attr(pageIdField);
    if (canRemoveTab(pageId)) {
        closeTabByPageId(pageId);
        //刷新上个页面table
        $("#iframe_" + lastPageId)[0].contentWindow.dataGrid.refresh();
    }
};

function refreshTabById(pageId) {
    var $iframe = findIframeById(pageId);
    var url = $iframe.attr('src');

    if (url.indexOf(top.document.domain) < 0) {
        $iframe.attr("src", url);// 跨域状况下，重新设置url
    } else {
        $iframe[0].contentWindow.location.reload(true);//带参数刷新
    }
    if(url.search("list") == -1){
        App.blockUI({
            target: '#tab-content',
            boxed: true,
            message: '加载中......'
        });
    }
}

var refreshTab = function () {
    //刷新当前tab
    var pageId = getActivePageId();
    refreshTabById(pageId);
};

function getTabUrlById(pageId) {
    var $iframe = findIframeById(pageId);
    return $iframe[0].contentWindow.location.href;
}

function getTabUrl(element) {
    var pageId = getPageId(element);
    getTabUrlById(pageId);
}


/**
 * 编辑tab的标题
 * @param pageId
 * @param title
 */
function editTabTitle(pageId, title) {
    var $title = findTabTitle(pageId);//有tab的标题
    var $span = $title.children("span.page_tab_title");
    $span.text(title);
}

//计算多个jq对象的宽度和
var calSumWidth = function (element) {
    var width = 0;
    $(element).each(function () {
        width += $(this).outerWidth(true);
    });
    return width;
};
//滚动到指定选项卡
var scrollToTab = function (element) {
    //element是tab(a标签),装有标题那个
    //div.content-tabs > div.page-tabs-content
    var marginLeftVal = calSumWidth($(element).prevAll()),//前面所有tab的总宽度
        marginRightVal = calSumWidth($(element).nextAll());//后面所有tab的总宽度
    //一些按钮(向左,向右滑动)的总宽度
    var tabOuterWidth = calSumWidth($(".content-tabs").children().not(".menuTabs"));
    // tab(a标签)显示区域的总宽度
    var visibleWidth = $(".content-tabs").outerWidth(true) - tabOuterWidth;
    //将要滚动的长度
    var scrollVal = 0;
    if ($(".page-tabs-content").outerWidth() < visibleWidth) {
        //所有的tab都可以显示的情况
        scrollVal = 0;
    } else if (marginRightVal <= (visibleWidth - $(element).outerWidth(true) - $(element).next().outerWidth(true))) {
        //向右滚动
        //marginRightVal(后面所有tab的总宽度)小于可视区域-(当前tab和下一个tab的宽度)
        if ((visibleWidth - $(element).next().outerWidth(true)) > marginRightVal) {
            scrollVal = marginLeftVal;
            var tabElement = element;
            while ((scrollVal - $(tabElement).outerWidth()) > ($(".page-tabs-content").outerWidth() - visibleWidth)) {
                scrollVal -= $(tabElement).prev().outerWidth();
                tabElement = $(tabElement).prev();
            }
        }
    } else if (marginLeftVal > (visibleWidth - $(element).outerWidth(true) - $(element).prev().outerWidth(true))) {
        //向左滚动
        scrollVal = marginLeftVal - $(element).prev().outerWidth(true);
    }
    //执行动画
    $('.page-tabs-content').animate({
        marginLeft: 0 - scrollVal + 'px'
    }, "fast");
};
//滚动条滚动到左边
var scrollTabLeft = function () {
    var marginLeftVal = Math.abs(parseInt($('.page-tabs-content').css('margin-left')));
    var tabOuterWidth = calSumWidth($(".content-tabs").children().not(".menuTabs"));
    var visibleWidth = $(".content-tabs").outerWidth(true) - tabOuterWidth;
    var scrollVal = 0;
    if ($(".page-tabs-content").width() < visibleWidth) {
        return false;
    } else {
        var tabElement = $(".menu_tab:first");
        var offsetVal = 0;
        while ((offsetVal + $(tabElement).outerWidth(true)) <= marginLeftVal) {
            offsetVal += $(tabElement).outerWidth(true);
            tabElement = $(tabElement).next();
        }
        offsetVal = 0;
        if (calSumWidth($(tabElement).prevAll()) > visibleWidth) {
            while ((offsetVal + $(tabElement).outerWidth(true)) < (visibleWidth) && tabElement.length > 0) {
                offsetVal += $(tabElement).outerWidth(true);
                tabElement = $(tabElement).prev();
            }
            scrollVal = calSumWidth($(tabElement).prevAll());
        }
    }
    $('.page-tabs-content').animate({
        marginLeft: 0 - scrollVal + 'px'
    }, "fast");
};
//滚动条滚动到右边
var scrollTabRight = function () {
    var marginLeftVal = Math.abs(parseInt($('.page-tabs-content').css('margin-left')));
    var tabOuterWidth = calSumWidth($(".content-tabs").children().not(".menuTabs"));
    var visibleWidth = $(".content-tabs").outerWidth(true) - tabOuterWidth;
    var scrollVal = 0;
    if ($(".page-tabs-content").width() < visibleWidth) {
        return false;
    } else {
        var tabElement = $(".menu_tab:first");
        var offsetVal = 0;
        while ((offsetVal + $(tabElement).outerWidth(true)) <= marginLeftVal) {
            offsetVal += $(tabElement).outerWidth(true);
            tabElement = $(tabElement).next();
        }
        offsetVal = 0;
        while ((offsetVal + $(tabElement).outerWidth(true)) < (visibleWidth) && tabElement.length > 0) {
            offsetVal += $(tabElement).outerWidth(true);
            tabElement = $(tabElement).next();
        }
        scrollVal = calSumWidth($(tabElement).prevAll());
        if (scrollVal > 0) {
            $('.page-tabs-content').animate({
                marginLeft: 0 - scrollVal + 'px'
            }, "fast");
        }
    }
};

//关闭其他选项卡
var closeOtherTabs = function (isAll) {
    if (isAll) {
        //关闭全部
        $('.page-tabs-content').children("[" + pageIdField + "]").find('.fa-remove').parents('a').each(function () {
            var $a = $(this);
            var pageId = getPageId($a);
            closeTabOnly(pageId);
        });
        var firstChild = $(".page-tabs-content").children().eq(0); //选中那些删不掉的第一个菜单
        if (firstChild) {
            //激活这个选项卡
            activeTabByPageId(getPageId(firstChild));

        }
    } else {
        //除此之外全部删除
        $('.page-tabs-content').children("[" + pageIdField + "]").find('.fa-remove').parents('a').not(".active").each(function () {
            var $a = $(this);
            var pageId = getPageId($a);
            closeTabOnly(pageId);
        });

    }
};

//激活Tab,通过id
function activeTabByPageId(pageId) {
    $(".menu_tab").removeClass("active");
    $("#tab-content").find(".active").removeClass("active");
    //激活TAB
    var $title = findTabTitle(pageId).addClass('active');
    findTabPanel(pageId).addClass("active");
    scrollToTab($title[0]);
}

$(function () {
    var $tabs = $(".menuTabs");
    //点击选项卡的时候就激活tab
    $tabs.on("click", ".menu_tab", function () {
        var pageId = getPageId(this);
        activeTabByPageId(pageId);
    });

    //双击选项卡刷新页面
    $tabs.on("dblclick", ".menu_tab", function () {
        var pageId = getPageId(this);
        refreshTabById(pageId);
    });

    //选项卡右键菜单
    function findTabElement(target) {
        var $ele = $(target);
        if (!$ele.is("a")) {
            $ele = $ele.parents("a.menu_tab");
        }
        return $ele;
    }

    context.init({
        preventDoubleContext: false,//不禁用原始右键菜单
        compress: true//元素更少的padding
    });
    context.attach('.page-tabs-content', [
//            {header: 'Options'},
        {
            text: '刷新',
            action: function (e, $selector, rightClickEvent) {
                var pageId = getPageId(findTabElement(rightClickEvent.target));
                refreshTabById(pageId);

            }
        },
        {
            text: "新窗口打开",
            action: function (e, $selector, rightClickEvent) {
                var pageId = getPageId(findTabElement(rightClickEvent.target));
                var url = getTabUrlById(pageId);
                window.open(url);

            }
        }
    ]);

});
(function ($) {
    $.fn.sidebarMenu = function (options) {
        options = $.extend({}, $.fn.sidebarMenu.defaults, options || {});
        var $menu_ul = $(this);
        var level = 0;
        if (options.data) {
            init($menu_ul, options.data, level);
        } else {
            if (!options.url) return;
            $.getJSON(options.url, options.param, function (data) {
                init($menu_ul, data, level);
            });
        }

        function init($menu_ul, data, level) {
            $.each(data, function (i, item) {
                //如果不是header
                var li = $('<li class="treeview " data-level="' + level + '"></li>');

                //a标签
                var $a;
                if (level > 0) {
                    $a = $('<a style="padding-left:' + (level * 20) + 'px"></a>');
                } else {
                    $a = $('<a></a>');
                }
                //图标
                var $icon = $('<i></i>');
                $icon.addClass(item.icon);

                //标题
                var $title = $('<span class="title"></span>');
                $title.addClass('menu-text').text(item.text);

                $a.append($icon);
                $a.append($title);
                $a.attr("href", "javascript:void(0)");

                var isOpen = item.isOpen;

                if (isOpen === true) {
                    li.addClass("active");
                }
                if (item.children && item.children.length > 0) {
                    var pullSpan = $('<span class="pull-right-container"></span>');
                    var pullIcon = $('<i class="fa fa-angle-left pull-right"></i>');
                    pullSpan.append(pullIcon);
                    $a.append(pullSpan);
                    li.append($a);

                    var menus = $('<ul></ul>');
                    menus.addClass('treeview-menu');
                    if (isOpen === true) {
                        menus.css("display", "block");
                        menus.addClass("menu-open");
                    } else {
                        menus.css("display", "none");
                    }
                    init(menus, item.children, level + 1);
                    li.append(menus);
                } else {

                    if (item.targetType != null && item.targetType === "blank") //代表打开新页面
                    {
                        $a.attr("href", item.url);
                        $a.attr("target", "_blank");
                    } else if (item.targetType != null && item.targetType === "ajax") { //代表ajax方式打开页面
                        $a.attr("href", item.url);
                        $a.addClass("ajaxify");
                    } else if (item.targetType != null && item.targetType === "iframe-tab") {
                        item.urlType = item.urlType ? item.urlType : 'relative';
                        var href = 'addTabs({id:\'' + item.id + '\',title: \'' + item.text + '\',close: true,url: \'' + item.url + '\',urlType: \'' + item.urlType + '\'});';
                        $a.attr('onclick', href);
                    } else if (item.targetType != null && item.targetType === "iframe") { //代表单iframe页面
                        $a.attr("href", item.url);
                        $a.addClass("iframeOpen");
                        $("#iframe-main").addClass("tab_iframe");
                    } else {
                        $a.attr("href", item.url);
                        $a.addClass("iframeOpen");
                        $("#iframe-main").addClass("tab_iframe");
                    }
                    $a.addClass("nav-link");
                    var badge = $("<span></span>");
                    // <span class="badge badge-success">1</span>
                    if (item.tip != null && item.tip > 0) {
                        badge.addClass("label").addClass("label-success").text(item.tip);
                    }
                    $a.append(badge);
                    li.append($a);
                }
                $menu_ul.append(li);
            });
        }

        //另外绑定菜单被点击事件,做其它动作
        $menu_ul.on("click", "li.treeview a", function () {
            var $a = $(this);

            if ($a.next().size() == 0) {//如果size>0,就认为它是可以展开的
                if ($(window).width() < $.AdminLTE.options.screenSizes.sm) {//小屏幕
                    //触发左边菜单栏按钮点击事件,关闭菜单栏
                    $($.AdminLTE.options.sidebarToggleSelector).click();
                }
            }
        });
    };

    $.fn.sidebarMenu.defaults = {
        url: null,
        param: null,
        data: null,
        isHeader: false
    };
})(jQuery);

//sidebar - menu组件封装
//在页面上面直接调用sidebar - menu的方法