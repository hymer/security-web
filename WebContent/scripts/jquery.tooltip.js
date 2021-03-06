/**
 * @license 
 * jQuery Tools @VERSION Tooltip - UI essentials
 * 
 * NO COPYRIGHTS OR LICENSES. DO WHAT YOU LIKE.
 * 
 * http://flowplayer.org/tools/tooltip/
 *
 * Since: November 2008
 * Date: @DATE 
 */
(function($) { 

        /* 
                removed: oneInstance, lazy, 
                tip must next to the trigger 
                isShown(fully), layout, tipClass, layout
        */
        
        // static constructs
        $.tools = $.tools || {version: '@VERSION'};
        
        $.tools.tooltip = {
                
                conf: { 
                        
                        // default effect variables
                        effect: 'toggle',                       
                        fadeOutSpeed: "fast",
                        predelay: 0,
                        delay: 30,
                        opacity: 1,                     
                        tip: 0,
                        
                        // 'top', 'bottom', 'right', 'left', 'center'
                        position: ['top', 'center'], 
                        offset: [0, 0],
                        relative: false,
                        cancelDefault: true,
                        
                        // type to event mapping 
                        events: {
                                def:                    "mouseenter,mouseleave",
                                input:          "focus,blur",
                                widget:         "focus mouseenter,blur mouseleave",
                                tooltip:                "mouseenter,mouseleave"
                        },
                        
                        // 1.2
                        layout: '<div/>',
                        tipClass: 'tooltip'
                },
                
                addEffect: function(name, loadFn, hideFn) {
                        effects[name] = [loadFn, hideFn];       
                } 
        };
        
        
        var effects = { 
                toggle: [ 
                        function(done) { 
                                var conf = this.getConf(), tip = this.getTip(), o = conf.opacity;
                                if (o < 1) { tip.css({opacity: o}); }
                                tip.show();
                                done.call();
                        },
                        
                        function(done) { 
                                this.getTip().hide();
                                done.call();
                        } 
                ],
                
                fade: [
                        function(done) { 
                                var conf = this.getConf();
                                this.getTip().fadeTo(conf.fadeInSpeed, conf.opacity, done); 
                        },  
                        function(done) { 
                                this.getTip().fadeOut(this.getConf().fadeOutSpeed, done); 
                        } 
                ]               
        };   

                
        /* calculate tip position relative to the trigger */    
        function getPosition(trigger, tip, conf) {      

                
                // get origin top/left position 
                var top = conf.relative ? trigger.position().top : trigger.offset().top, 
                         left = conf.relative ? trigger.position().left : trigger.offset().left,
                         pos = conf.position[0];

                top  -= tip.outerHeight() - conf.offset[0];
                left += trigger.outerWidth() + conf.offset[1];
                
                // adjust Y             
                var height = tip.outerHeight() + trigger.outerHeight();
                if (pos == 'center')    { top += height / 2; }
                if (pos == 'bottom')    { top += height; }
                
                // adjust X
                pos = conf.position[1];         
                var width = tip.outerWidth() + trigger.outerWidth();
                if (pos == 'center')    { left -= width / 2; }
                if (pos == 'left')      { left -= width; }       
                
                return {top: top, left: left};
        }               

        
        
        function Tooltip(trigger, conf) {

                var self = this, 
                         fire = trigger.add(self),
                         tip,
                         timer = 0,
                         pretimer = 0, 
                         title = trigger.attr("title"),
                         effect = effects[conf.effect],
                         shown,
                                 
                         // get show/hide configuration
                         isInput = trigger.is(":input"), 
                         isWidget = isInput && trigger.is(":checkbox, :radio, select, :button, :submit"),                       
                         type = trigger.attr("type"),
                         evt = conf.events[type] || conf.events[isInput ? (isWidget ? 'widget' : 'input') : 'def']; 
                
                
                // check that configuration is sane
                if (!effect) { throw "Nonexistent effect \"" + conf.effect + "\""; }                                    
                
                evt = evt.split(/,\s*/); 
                if (evt.length != 2) { throw "Tooltip: bad events configuration for " + type; } 
                
                
                // trigger --> show  
                trigger.bind(evt[0], function(e) {
                        clearTimeout(timer);
                        if (conf.predelay) {
                                pretimer = setTimeout(function() { self.show(e); }, conf.predelay);     
                                
                        } else {
                                self.show(e);   
                        }
                        
                // trigger --> hide
                }).bind(evt[1], function(e)  {
                        clearTimeout(pretimer);
                        if (conf.delay)  {
                                timer = setTimeout(function() { self.hide(e); }, conf.delay);   
                                
                        } else {
                                self.hide(e);           
                        }
                        
                }); 
                
                
                // remove default title
                if (title && conf.cancelDefault) { 
                        trigger.removeAttr("title");
                        trigger.data("title", title);                   
                }               
                
                $.extend(self, {
                                
                        show: function(e) { 

                                // tip not initialized yet
                                if (!tip) {
                                        
                                        // autogenerated tooltip
                                        if (title) { 
                                                tip = $(conf.layout).addClass(conf.tipClass).appendTo(document.body)
                                                        .hide().append(title);
                                                
                                        // single tip element for all
                                        } else if (conf.tip) { 
                                                tip = $(conf.tip).eq(0);
                                                
                                        // manual tooltip
                                        } else {        
                                                tip = trigger.next();  
                                                if (!tip.length) { tip = trigger.parent().next(); }      
                                        }
                                        
                                        if (!tip.length) { throw "Cannot find tooltip for " + trigger;  }
                                } 
                                
                                if (self.isShown()) { return self; }  
                                
                                // stop previous animation
                                tip.stop(true, true);                           
                                
                                // get position
                                var pos = getPosition(trigger, tip, conf);                      
                
                                
                                // onBeforeShow
                                e = e || $.Event();
                                e.type = "onBeforeShow";
                                fire.trigger(e, [pos]);                         
                                if (e.isDefaultPrevented()) { return self; }
                
                                
                                // onBeforeShow may have altered the configuration
                                pos = getPosition(trigger, tip, conf);
                                
                                // set position
                                tip.css({position:'absolute', top: pos.top, left: pos.left});                                   
                                
                                shown = true;
                                
                                // invoke effect 
                                effect[0].call(self, function() {
                                        e.type = "onShow";
                                        shown = 'full';
                                        fire.trigger(e);                 
                                });                                     

                
                                // tooltip events       
                                var event = conf.events.tooltip.split(/,\s*/);

                                tip.bind(event[0], function() { 
                                        clearTimeout(timer);
                                        clearTimeout(pretimer);
                                });
                                
                                if (event[1] && !trigger.is("input:not(:checkbox, :radio), textarea")) {                                        
                                        tip.bind(event[1], function(e) {

                                                // being moved to the trigger element
                                                if (e.relatedTarget != trigger[0]) {
                                                        trigger.trigger(evt[1].split(" ")[0]);
                                                }
                                        }); 
                                } 
                                
                                return self;
                        },
                        
                        hide: function(e) {

                                if (!tip || !self.isShown()) { return self; }
                        
                                // onBeforeHide
                                e = e || $.Event();
                                e.type = "onBeforeHide";
                                fire.trigger(e);                                
                                if (e.isDefaultPrevented()) { return; }
        
                                shown = false;
                                
                                effects[conf.effect][1].call(self, function() {
                                        e.type = "onHide";
                                        shown = false;
                                        fire.trigger(e);                 
                                });
                                
                                return self;
                        },
                        
                        isShown: function(fully) {
                                return fully ? shown == 'full' : shown; 
                        },
                                
                        getConf: function() {
                                return conf;    
                        },
                                
                        getTip: function() {
                                return tip;     
                        },
                        
                        getTrigger: function() {
                                return trigger; 
                        }               

                });             

                // callbacks    
                $.each("onHide,onBeforeShow,onShow,onBeforeHide".split(","), function(i, name) {
                                
                        // configuration
                        if ($.isFunction(conf[name])) { 
                                $(self).bind(name, conf[name]); 
                        }

                        // API
                        self[name] = function(fn) {
                                $(self).bind(name, fn);
                                return self;
                        };
                });
                
        }
                
        
        // jQuery plugin implementation
        $.fn.tooltip = function(conf) {
                
                // return existing instance
                var api = this.data("tooltip");
                if (api) { return api; }

                conf = $.extend(true, {}, $.tools.tooltip.conf, conf);
                
                // position can also be given as string
                if (typeof conf.position == 'string') {
                        conf.position = conf.position.split(/,?\s/);    
                }
                
                // install tooltip for each entry in jQuery object
                this.each(function() {
                        api = new Tooltip($(this), conf); 
                        $(this).data("tooltip", api); 
                });
                
                return conf.api ? api: this;             
        };
                
}) (jQuery);

/**
 * @license 
 * jQuery Tools @VERSION / Tooltip Slide Effect
 * 
 * NO COPYRIGHTS OR LICENSES. DO WHAT YOU LIKE.
 * 
 * http://flowplayer.org/tools/tooltip/slide.html
 *
 * Since: September 2009
 * Date: @DATE 
 */
(function($) { 

        // version number
        var t = $.tools.tooltip;
                
        // extend global configuragion with effect specific defaults
        $.extend(t.conf, { 
                direction: 'up', // down, left, right 
                bounce: false,
                slideOffset: 10,
                slideInSpeed: 200,
                slideOutSpeed: 200, 
                slideFade: !$.browser.msie
        });                     
        
        // directions for slide effect
        var dirs = {
                up: ['-', 'top'],
                down: ['+', 'top'],
                left: ['-', 'left'],
                right: ['+', 'left']
        };
        
        /* default effect: "slide"  */
        t.addEffect("slide", 
                
                // show effect
                function(done) { 

                        // variables
                        var conf = this.getConf(), 
                                 tip = this.getTip(),
                                 params = conf.slideFade ? {opacity: conf.opacity} : {}, 
                                 dir = dirs[conf.direction] || dirs.up;

                        // direction                    
                        params[dir[1]] = dir[0] +'='+ conf.slideOffset;
                        
                        // perform animation
                        if (conf.slideFade) { tip.css({opacity:0}); }
                        tip.show().animate(params, conf.slideInSpeed, done); 
                }, 
                
                // hide effect
                function(done) {
                        
                        // variables
                        var conf = this.getConf(), 
                                 offset = conf.slideOffset,
                                 params = conf.slideFade ? {opacity: 0} : {}, 
                                 dir = dirs[conf.direction] || dirs.up;
                        
                        // direction
                        var sign = "" + dir[0];
                        if (conf.bounce) { sign = sign == '+' ? '-' : '+'; }                    
                        params[dir[1]] = sign +'='+ offset;                     
                        
                        // perform animation
                        this.getTip().animate(params, conf.slideOutSpeed, function()  {
                                $(this).hide();
                                done.call();            
                        });
                }
        );  
        
})(jQuery);

/**
 * @license 
 * jQuery Tools @VERSION / Tooltip Dynamic Positioning
 * 
 * NO COPYRIGHTS OR LICENSES. DO WHAT YOU LIKE.
 * 
 * http://flowplayer.org/tools/tooltip/dynamic.html
 *
 * Since: July 2009
 * Date: @DATE 
 */
(function($) { 

        // version number
        var t = $.tools.tooltip;
        
        t.dynamic = {
                conf: {
                        classNames: "top right bottom left"
                }
        };
                
        /* 
         * See if element is on the viewport. Returns an boolean array specifying which
         * edges are hidden. Edges are in following order:
         * 
         * [top, right, bottom, left]
         * 
         * For example following return value means that top and right edges are hidden
         * 
         * [true, true, false, false]
         * 
         */
        function getCropping(el) {
                
                var w = $(window); 
                var right = w.width() + w.scrollLeft();
                var bottom = w.height() + w.scrollTop();                
                
                return [
                        el.offset().top <= w.scrollTop(),                                               // top
                        right <= el.offset().left + el.width(),                         // right
                        bottom <= el.offset().top + el.height(),                        // bottom
                        w.scrollLeft() >= el.offset().left                                      // left
                ]; 
        }
        
        /*
                Returns true if all edges of an element are on viewport. false if not
                
                @param crop the cropping array returned by getCropping function
         */
        function isVisible(crop) {
                var i = crop.length;
                while (i--) {
                        if (crop[i]) { return false; }  
                }
                return true;
        }
        
        // dynamic plugin
        $.fn.dynamic = function(conf) {
                
                if (typeof conf == 'number') { conf = {speed: conf}; }
                
                conf = $.extend({}, t.dynamic.conf, conf);
                
                var cls = conf.classNames.split(/\s/), orig;    
                        
                this.each(function() {          
                                
                        var api = $(this).tooltip().onBeforeShow(function(e, pos) {                             

                                // get nessessary variables
                                var tip = this.getTip(), tipConf = this.getConf();  

                                /*
                                        We store the original configuration and use it to restore back to the original state.
                                */                                      
                                if (!orig) {
                                        orig = [
                                                tipConf.position[0], 
                                                tipConf.position[1], 
                                                tipConf.offset[0], 
                                                tipConf.offset[1], 
                                                $.extend({}, tipConf)
                                        ];
                                }
                                
                                /*
                                        display tip in it's default position and by setting visibility to hidden.
                                        this way we can check whether it will be on the viewport
                                */
                                $.extend(tipConf, orig[4]);
                                tipConf.position = [orig[0], orig[1]];
                                tipConf.offset = [orig[2], orig[3]];

                                tip.css({
                                        visibility: 'hidden',
                                        position: 'absolute',
                                        top: pos.top,
                                        left: pos.left 
                                }).show(); 
                                
                                // now let's see for hidden edges
                                var crop = getCropping(tip);            
                                                                
                                // possibly alter the configuration
                                if (!isVisible(crop)) {
                                        
                                        // change the position and add class
                                        if (crop[2]) { $.extend(tipConf, conf.top);             tipConf.position[0] = 'top';            tip.addClass(cls[0]); }
                                        if (crop[3]) { $.extend(tipConf, conf.right);   tipConf.position[1] = 'right';  tip.addClass(cls[1]); }                                 
                                        if (crop[0]) { $.extend(tipConf, conf.bottom);  tipConf.position[0] = 'bottom'; tip.addClass(cls[2]); } 
                                        if (crop[1]) { $.extend(tipConf, conf.left);            tipConf.position[1] = 'left';   tip.addClass(cls[3]); }                                 
                                        
                                        // vertical offset
                                        if (crop[0] || crop[2]) { tipConf.offset[0] *= -1; }
                                        
                                        // horizontal offset
                                        if (crop[1] || crop[3]) { tipConf.offset[1] *= -1; }
                                }  
                                
                                tip.css({visibility: 'visible'}).hide();
                
                        });
                        
                        // restore positioning as soon as possible
                        api.onBeforeShow(function() {
                                var c = this.getConf(), tip = this.getTip();             
                                setTimeout(function() { 
                                        c.position = [orig[0], orig[1]];
                                        c.offset = [orig[2], orig[3]];
                                }, 0);
                        });
                        
                        // remove custom class names and restore original effect
                        api.onHide(function() {
                                var tip = this.getTip(); 
                                tip.removeClass(conf.classNames);
                        });
                                
                        ret = api;
                        
                });
                
                return conf.api ? ret : this;
        };      
        
}) (jQuery);