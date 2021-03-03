delete localStorage;
delete window.localStorage;
localStorage = function() {};
localStorage.data = function() {};
localStorage.cached = function() {};
localStorage.getItem = function(key) {
    if(!localStorage.cached[key]) {
        localStorage.data[key] = __fix_localStorage.getItem(key);
        localStorage.cached[key] = true;
    }
    if(!localStorage.data[key]) {
        return null;
    }
    return localStorage.data[key];
};
localStorage.setItem = function(key, value) {
    var oldValue = localStorage.data[key];
    if(oldValue != value) {
        localStorage.data[key] = value;
        localStorage.cached[key] = true;
        setTimeout(function() {
            __fix_localStorage.setItem(key, localStorage.data[key]);
        }, 0);
    }
};
localStorage.clear = function() {
    localStorage.data = function() {}
    setTimeout(function() {
        __fix_localStorage.clear();
    }, 0);
};
localStorage.removeItem = function(key) {
    if(localStorage.cached[key]) {
        delete localStorage.data[key];
    }
    else {
        localStorage.cached[key] = true;
    }
    setTimeout(function() {
        __fix_localStorage.removeItem(key);
    }, 0);
};
window.localStorage = localStorage;
