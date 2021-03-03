//=============================================================================
// UCHU_MobileOperation.js
//----------------------------------------------------------------------------
// Copyright (c) 2015 uchuzine
// Released under the MIT license
// http://opensource.org/licenses/mit-license.php
//=============================================================================

var UCHU_MobileOperation = {};
(function() {
	//-----------------------------------------------------------------------------
	// Setup
    console.log("gamepad");
	var PRM = PRM || {};

	PRM.url=[];
	PRM.visible=[];
	PRM.size=[];
	PRM.pos=[];
	PRM.spot=[];
	PRM.pcBtn = false; //Boolean(Parameters["PC BtnDisplay"] === 'true' || false);
	PRM.pcExt = true; //Boolean(Parameters["PC TouchExtend"] === 'true' || false);
    PRM.url[0] = "./EasyMV/DirPad.png";//String(Parameters["DPad Image"]);
    PRM.url[1] = "./EasyMV/ActionBtn.png";//String(Parameters["ActionBtn Image"]);
    PRM.url[2] = "./EasyMV/CancelButton.png";//String(Parameters["CancelBtn Image"]);
	PRM.opacity = 0.7; //Number(Parameters["Button Opacity"]);
	PRM.vZoom = 1.7; //Number(Parameters["Vertical BtnZoom"]);
	PRM.tabZoom = 0.8; //Number(Parameters["Tablet BtnZoom"]);
	PRM.tabvZoom = 1.1; //Number(Parameters["TabVertical BtnZoom"]);
	PRM.hideBtn = true; //Boolean(Parameters["HideButton OnMessage"] === 'true' || false);
	PRM.visible[0] = true; //Boolean(Parameters["DPad Visible"] === 'true' || false);
	PRM.size[0] = 200; //Number(Parameters["DPad Size"]);
	PRM.pos[0] = ["10", "10"];//Parameters["DPad Margin"].split(";");
	PRM.spot[0] = ["left", "bottom"];//Parameters["DPad Orientation"].split(";");
	PRM.pad_scale = 1.3;//Number(Parameters["DPad OpelationRange"]);
	PRM.pad_dia = 0.7;;//Math.max(0,Math.min(1,(1-Number(Parameters["DPad DiagonalRange"]))));
	PRM.visible[1] = true;//Boolean(Parameters["ActionBtn Visible"] === 'true' || false);
	PRM.size[1] = 100;//Number(Parameters["ActionBtn Size"]);
	PRM.pos[1] = ["25", "100"];//Parameters["ActionBtn Margin"].split(";");
	PRM.spot[1] = ["right", "bottom"];//Parameters["ActionBtn Orientation"].split(";");
	PRM.visible[2] = true;//Boolean(Parameters["CancelBtn Visible"] === 'true' || false);
	PRM.size[2] = 100;//Number(Parameters["CancelBtn Size"]);
	PRM.pos[2] = ["100", "25"];//Parameters["CancelBtn Margin"].split(";");
	PRM.spot[2] = ["right", "bottom"];//Parameters["CancelBtn Orientation"].split(";");
	PRM.flickpage = true;//Boolean(Parameters["Flick PageUp-PageDown"] === 'true' || false);
	PRM.holdaction = true;//Boolean(Parameters["HoldCanvas ActionBtn"] === 'true' || false);
	PRM.outcansel = true;//Boolean(Parameters["OutCanvas CancelBtn"] === 'true' || false);
	PRM.outaction = true;//Boolean(Parameters["OutCanvas ActionBtn"] === 'true' || false);
	PRM.analogmove = false; //Boolean(Parameters["Analog Move"] === 'true' || false);
	PRM.sensitivity = 1.8; //Number(Parameters["Analog Sensitivity"]);

	var btn_id=["DirPad","ok","escape"];
	var current_zoom=1;
	var st_x = 0;
	var st_y = 0;
	var pad_range=PRM.size[0]*PRM.pad_scale;
	var pad_size=pad_range*current_zoom/2;
	var Btn_ready=false;
	var Btn_hide=false;
	var PressBtn=false;
	var dirx=0;
	var diry=0;
	var touchx=0;
	var touchy=0;
	var autofire=false;
	var hvzoom=[1, PRM.vZoom];
	var ua = (function(u){
	  return {
	    Tablet:(u.indexOf("windows") != -1 && u.indexOf("touch") != -1) || u.indexOf("ipad") != -1 || (u.indexOf("android") != -1 && u.indexOf("mobile") == -1) || (u.indexOf("firefox") != -1 && u.indexOf("tablet") != -1) || u.indexOf("kindle") != -1 || u.indexOf("silk") != -1 || u.indexOf("playbook") != -1
	  };
	})(window.navigator.userAgent.toLowerCase());

	if(ua.Tablet){
		hvzoom=[PRM.tabZoom, PRM.tabvZoom];
	}
	if (!Utils.isMobileDevice() && !PRM.pcBtn) {PRM.visible[0]=PRM.visible[1]=PRM.visible[2]=false;}

	//-----------------------------------------------------------------------------
	// Locate_DirPad

	function Locate_DirPad() {
		this.initialize.apply(this, arguments);
	}


	Locate_DirPad.prototype.initialize = function() {
        console.log("Locate_DirPadinitialize");
		var img = new Image();
		var url = PRM.url[0];
		img.onerror = function() {Graphics.printError('DirPad Image was Not Found:',url);};
		img.src = url;
		img = null;
		this.Div = document.createElement("div");
		this.Div.id = 'Dirpad';
		this.Div.style.position = 'fixed';
		this.Div.style[PRM.spot[0][0].replace(/\s+/g, "")] = String(PRM.pos[0][0]-(pad_range-PRM.size[0])/2)+'px';
		this.Div.style[PRM.spot[0][1].replace(/\s+/g, "")] = String(PRM.pos[0][1]-(pad_range-PRM.size[0])/2)+'px';
		this.Div.style.width = pad_range+'px';
		this.Div.style.height = pad_range+'px';
		this.Div.style.opacity = PRM.opacity;
		this.Div.style.zIndex = '11';
		this.Div.style.userSelect="none";
		this.Div.style["-webkit-tap-highlight-color"]="rgba(0,0,0,0)";
		this.Div.style.background = 'url('+PRM.url[0]+') 50% 50% / '+String(Math.round(PRM.size[0]/pad_range*100))+'% no-repeat';

		if(!Utils.isMobileDevice() && PRM.pcBtn){
			this.Div.addEventListener('mousedown', function(e) {
			  if (!SceneManager.isSceneChanging()){dirope(e.layerX,e.layerY,true);PressBtn=true;}
			}, false);
			this.Div.addEventListener('mousemove', function(e) {
			  if(PressBtn && !SceneManager.isSceneChanging()){dirope(e.layerX,e.layerY,false);}
			}, false);
			this.Div.addEventListener('mouseup', function() {
				disope();PressBtn=false;
			}, false);
			this.Div.addEventListener('mouseout', function() {
			    disope();PressBtn=false;
			}, false);
		}
		this.Div.addEventListener('touchstart', function(e) {
			PressBtn=true;
			if (!SceneManager.isSceneChanging()){dirope(e.touches[0].clientX-dirx, e.touches[0].clientY-diry,true)};
		}, false);
		this.Div.addEventListener('touchmove', function(e) {
			if (!SceneManager.isSceneChanging()){dirope(e.touches[0].clientX-dirx, e.touches[0].clientY-diry,false)};
			PressBtn=true;
		}, false);
		this.Div.addEventListener('touchend', function() {
			disope();PressBtn=false;
		}, false);
			document.body.appendChild(this.Div);
	};

	function dirope(xx,yy,st) {
		touchx=(xx-pad_size)/pad_size;
		touchy=(yy-pad_size)/pad_size;
		if(st && Math.sqrt(touchx*touchx+touchy*touchy)>1){
			disope();
		}else{
			if(touchx>Math.abs(touchy)*PRM.pad_dia){Input._currentState['right']=true;Input._currentState['left']=false;}
			else if(touchx<-Math.abs(touchy)*PRM.pad_dia){Input._currentState['left']=true;Input._currentState['right']=false;}
			else{Input._currentState['left']=false;Input._currentState['right']=false;}
			if(touchy>Math.abs(touchx)*PRM.pad_dia){Input._currentState['down']=true;Input._currentState['up']=false;}
			else if(touchy<-Math.abs(touchx)*PRM.pad_dia){Input._currentState['up']=true;Input._currentState['down']=false;}
			else{Input._currentState['up']=false;Input._currentState['down']=false;}
		}
	}
	function disope() {
		touchx=0; touchy=0;
		Input._currentState['up']=false;
		Input._currentState['down']=false;
		Input._currentState['left']=false;
		Input._currentState['right']=false;
	}

	//-----------------------------------------------------------------------------
	// Locate_Button

	function Locate_Button() {
		this.initialize.apply(this, arguments);
	}
	Locate_Button.prototype.initialize = function(type) {
        console.log("Locate_Buttoninitialize");
		var img = new Image();
		var url = PRM.url[type];
		img.onerror = function() {Graphics.printError('Button Image was Not Found:',url);};
		img.src = url;
		img = null;
		this.Div = document.createElement("div");
		this.Div.id = btn_id[type]+'Btn';
		this.Div.style.position = 'fixed';
		this.Div.style[PRM.spot[type][0].replace(/\s+/g, "")] = PRM.pos[type][0]+'px';
		this.Div.style[PRM.spot[type][1].replace(/\s+/g, "")] = PRM.pos[type][1]+'px';
		this.Div.style.width = PRM.size[type]+'px';
		this.Div.style.height = PRM.size[type]+'px';
		this.Div.style.opacity = PRM.opacity;
		this.Div.style.zIndex = '11';
		this.Div.style.userSelect="none";
		this.Div.style.background = 'url('+PRM.url[type]+') 0 0 / cover no-repeat';

		if(!Utils.isMobileDevice() && PRM.pcBtn){
			this.Div.addEventListener('mousedown', function() {
				Input._currentState[btn_id[type]] = true;PressBtn=true;
			}, false);
			this.Div.addEventListener('mouseover', function() {
			  if(TouchInput.isPressed()){Input._currentState[btn_id[type]] = true;PressBtn=true;return false;}
			}, false);
			this.Div.addEventListener('mouseup', function() {
			  Input._currentState[btn_id[type]] = false;PressBtn=false;
			}, false);
			this.Div.addEventListener('mouseout', function() {
			  Input._currentState[btn_id[type]] = false;PressBtn=false;
			}, false);
		}

		this.Div.addEventListener('touchstart', function() {
			if (!SceneManager.isSceneChanging()){Input._currentState[btn_id[type]] = true;PressBtn=true;}
		}, false);
		this.Div.addEventListener('touchend', function() {
			Input._currentState[btn_id[type]] = false;PressBtn=false;
		}, false);

		document.body.appendChild(this.Div);
	};

	//-----------------------------------------------------------------------------
	// Replace function

	var Scene_Base_start = Scene_Base.prototype.start;
	Scene_Base.prototype.start = function() {
		Scene_Base_start.call(this);
	    if (Utils.isMobileDevice() || PRM.pcBtn) {
			if(!Btn_ready){
				Btn_ready=true;
				if(PRM.visible[0]){this.DirPad = new Locate_DirPad();}
				if(PRM.visible[1]){this.okButton = new Locate_Button(1);}
				if(PRM.visible[2]){this.canselButton = new Locate_Button(2);}
				Graphics._updateRealScale();
				document.documentElement.style["-webkit-user-select"]="none";
				document.addEventListener("touchmove", function(evt) {evt.preventDefault();}, false);
			}
		}
	};

	if(PRM.visible[0] || PRM.visible[1] || PRM.visible[2]){

		var Game_Temp_setDestination = Game_Temp.prototype.setDestination;
		Game_Temp.prototype.setDestination = function(x, y) {
			Game_Temp_setDestination.apply(this, arguments);
			if(PressBtn){
				this._destinationX = null;
				this._destinationY = null;
			}
		};

		var Graphics_updateRealScale = Graphics._updateRealScale;
		Graphics._updateRealScale = function() {
			Graphics_updateRealScale.call(this);
			if (this._stretchEnabled) {
				if(document.getElementById("Dirpad")){
				if(window.innerWidth<window.innerHeight){current_zoom=hvzoom[1];}else{current_zoom=hvzoom[0];}
					pad_size=pad_range*current_zoom/2;
					if(PRM.visible[0]){
						document.getElementById("Dirpad").style.zoom=current_zoom;
						dirx=document.getElementById("Dirpad").offsetLeft*current_zoom;
						diry=document.getElementById("Dirpad").offsetTop*current_zoom;
					}
					if(PRM.visible[1]){document.getElementById("okBtn").style.zoom=current_zoom;}
					if(PRM.visible[2]){document.getElementById("escapeBtn").style.zoom=current_zoom;}
				}
			}
		};
	}

	//-----------------------------------------------------------------------------
	// Option

	if(PRM.hideBtn){
		Scene_Base.prototype.hideUserInterface = function() {
			if (Utils.isMobileDevice() || PRM.pcBtn) {Btn_hide=true;
				if(PRM.visible[0]){document.getElementById("Dirpad").style.zIndex = '0';}
				if(PRM.visible[1]){document.getElementById("okBtn").style.zIndex = '0';}
				if(PRM.visible[2]){document.getElementById("escapeBtn").style.zIndex = '0';}
			}
		};
		Scene_Base.prototype.showUserInterface = function() {
			if (Utils.isMobileDevice() && !Btn_hide || PRM.pcBtn && !Btn_hide) {
				if(PRM.visible[0]){document.getElementById("Dirpad").style.zIndex = '11';}
				if(PRM.visible[1]){document.getElementById("okBtn").style.zIndex = '11';}
				if(PRM.visible[2]){document.getElementById("escapeBtn").style.zIndex = '11';}
			}
		};

		var Scene_Map_createMessageWindows = Scene_Map.prototype.createMessageWindow;
		var Scene_Map_processMapTouch = Scene_Map.prototype.processMapTouch;
		var Scene_Map_terminate = Scene_Map.prototype.terminate;

		//fixed by liply
		var Window_Message_startMessage = Window_Message.prototype.startMessage;
		Window_Message.prototype.startMessage = function(){
			Window_Message_startMessage.apply(this, arguments);
			if($gameMessage.positionType()==2){
				SceneManager._scene.hideUserInterface();
			}
		};

		var Window_Message_terminateMessage = Window_Message.prototype.terminateMessage;
		Window_Message.prototype.terminateMessage = function() {
			Window_Message_terminateMessage.apply(this, arguments);
			Btn_hide=false;
			setTimeout("Scene_Base.prototype.showUserInterface();", 200);
		};

        //
		// Scene_Map.prototype.createMessageWindow = function() {
		// 	Scene_Map_createMessageWindows.call(this);
		// 	var oldStartMessage = this._messageWindow.startMessage;
		// 	var oldTerminateMessage = this._messageWindow.terminateMessage;
		// 	var scene = this;
		//
		// 	this._messageWindow.startMessage = function() {
		// 		oldStartMessage.apply(this, arguments);
		// 		if($gameMessage.positionType()==2){
		// 			scene.hideUserInterface();
		// 		}
		// 	};
		// 	Window_Message.prototype.terminateMessage = function() {
		// 		oldTerminateMessage.apply(this, arguments);
		// 		Btn_hide=false;
		// 		setTimeout("Scene_Base.prototype.showUserInterface();", 200);
		// 	};
		// };

		var Scene_Battle_createMessageWindow = Scene_Battle.prototype.createMessageWindow;
		Scene_Battle.prototype.createMessageWindow = function() {
			Scene_Battle_createMessageWindow.call(this);
			var oldStartMessage = this._messageWindow.startMessage;
			var oldTerminateMessage = this._messageWindow.terminateMessage;
			var scene = this;
			this._messageWindow.startMessage = function() {
				oldStartMessage.apply(this, arguments);
				if($gameMessage.positionType()==2){
					scene.hideUserInterface();
				}
			};
			Window_Message.prototype.terminateMessage = function() {
				oldTerminateMessage.apply(this, arguments);
				Btn_hide=false;
				setTimeout("Scene_Base.prototype.showUserInterface();", 200);
			};
		};
	}

	if(Utils.isMobileDevice() || PRM.pcExt){
		if(PRM.holdaction){
			var TouchInput_update = TouchInput.update;
			TouchInput.update = function() {
				TouchInput_update.call(this);
				if (!PressBtn && TouchInput.isLongPressed()) {
					Input._currentState['ok']=true;autofire=true;
				}
				if(!TouchInput.isPressed() && autofire){
					Input._currentState['ok']=false;autofire=false;
				}
			};
		}

		if(PRM.flickpage || PRM.outcansel || PRM.outaction){
			TouchInput._endRequest= function(type) {
				Input._currentState[type]=false;
			}
			if(Utils.isMobileDevice()){
				var TouchInput_onTouchStart = TouchInput._onTouchStart;
				TouchInput._onTouchStart = function(event) {
				    TouchInput_onTouchStart.apply(this, arguments);
					var touch = event.changedTouches[0];
					if(!PressBtn){
						st_x = Graphics.pageToCanvasX(touch.pageX);
						st_y = Graphics.pageToCanvasY(touch.pageY);
						if(st_x<0 || st_y<0 || st_x>Graphics.boxWidth || st_y>Graphics.boxHeight){
							if(PRM.outcansel){Input._currentState['escape']=true;setTimeout("TouchInput._endRequest('escape');", 100);}
							if(PRM.outaction){Input._currentState['ok']=true;setTimeout("TouchInput._endRequest('ok');", 100);}
						}
					}
				};
			}else{
				var TouchInput_onLeftButtonDown = TouchInput._onLeftButtonDown;
				TouchInput._onLeftButtonDown = function(event) {
					TouchInput_onLeftButtonDown.apply(this, arguments);
					if(!PressBtn){
						st_x = Graphics.pageToCanvasX(event.pageX);
						st_y = Graphics.pageToCanvasY(event.pageY);
						if(st_x<0 || st_y<0 || st_x>Graphics.boxWidth || st_y>Graphics.boxHeight){
							if(PRM.outcansel){Input._currentState['escape']=true;setTimeout("TouchInput._endRequest('escape');", 100);}
							if(PRM.outaction){Input._currentState['ok']=true;setTimeout("TouchInput._endRequest('ok');", 100);}
						}
					}
				};
			}
		}

		if(PRM.flickpage){
			var TouchInput_onMove = TouchInput._onMove;
			TouchInput._onMove = function(x, y) {
				TouchInput_onMove.apply(this, arguments);
				if(!PressBtn){
					if((st_x-x)<-50 && Math.abs(st_y-y)<100){st_y=9999;Input._currentState['pageup']=true;setTimeout("TouchInput._endRequest('pageup');", 100);}
					if((st_x-x)>50 && Math.abs(st_y-y)<100){st_y=9999;Input._currentState['pagedown']=true;setTimeout("TouchInput._endRequest('pagedown');", 100);}
				}
			}
		}
	}

	//AnalogMove.js
	if(PRM.analogmove && Utils.isMobileDevice() || PRM.analogmove && PRM.pcBtn){
		Input.leftStick = function() {
			var threshold = 0.1;
			var x = touchx;
			var y = touchy;
			var tilt = Math.min(1,Math.sqrt(touchx*touchx+touchy*touchy)*PRM.sensitivity);
			var direction = 0.0;
			if (x === 0.0) {
				direction = (-y > 0 ? Math.PI * 0.0 : Math.PI * 1.0);
			} else if (y === 0.0) {
				direction = (-x > 0 ? Math.PI * 0.5 : Math.PI * 1.5);
			} else {
				direction = Math.atan2(-x, -y);
			}
			return {tilt: tilt, direction: direction};
		};
	}
})(UCHU_MobileOperation);