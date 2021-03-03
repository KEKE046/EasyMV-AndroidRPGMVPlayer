if(typeof require == "undefined") {
    var require = function(name){};
}

(function() {
    var greenworks = {};
    // ------ Achievement API ------

    greenworks.activateAchievement = function(){
        alert("获得成就: " + arguments[0]);
        arguments[1]();
    };
    /*
    *
    ** `achievement` String
    ** `success_callback` Function()
    ** `error_callback` Function(err)
    *
    *The `achievement` represents the unlocked achievement in your game.
    *
    */

    greenworks.indicateAchievementProgress = function(){
        alert("成就进行中: " + arguments[0] + "   " + arguments[1] + "/" + arguments[2]);
        return true;
    };
    /*
    *
    *Shows the user a pop-up notification with the current progress of an achievement.
    *Calling this function will NOT set the progress or unlock the achievement, use [SetStat](stats.md#greenworkssetstatname-value).
    *
    ** `achievement` String: API name of the achievement.
    ** `current` Number: The current progress.
    ** `max` Number: The progress required to unlock the achievement.
    *
    *Returns `true` upon success, otherwise `false`.
    *
    */

    greenworks.getAchievement = function(){arguments[1](true);};
    /*
    *
    ** `achievement` String: The achievement name in you game
    ** `success_callback` Function(is_achieved)
    *  * `is_achieved` Boolean: Whether the achievement is achieved.
    ** `error_callback` Function(err)
    *
    *Gets whether the `achievement` is achieved.
    *
    */

    greenworks.clearAchievement = function(){arguments[1]();};
    /*
    *
    ** `achievement` String - The achievement needs to be cleared
    ** `success_callback` Function()
    ** `error_callback` Function(err)
    *
    */

    greenworks.getAchievementNames = function(){return ["Achievement"];};
    /*
    *
    *Returns an `Array` represents all the achievements in the game.
    *
    */

    greenworks.getNumberOfAchievements = function(){return 1;};
    /*
    *
    *Returns an `Integer` represents the number of all achievements in the game.
    *
    */

    // ------ Authentication API ------
    // TODO: Authentication API

    // ------ Cloud API ------

    greenworks.saveTextToFile = function(){
        localStorage.setItem("fakegreenworks.file-" + arguments[0], arguments[1]);
        arguments[2]();
    };
    /*
    *
    ** `file_name` String
    ** `file_content` String
    ** `success_callback` Function()
    ** `error_callback` Function(err)
    *
    */

    greenworks.readTextFromFile = function(){
        var data = localStorage.getItem("fakegreenworks.file-" + arguments[0]);
        if(data) {
            arguments[1](data);
        }
        else {
            arguments[1]("");
        }
    };
    /*
    *
    ** `file_name` String
    ** `success_callback` Function(file_content)
    *  * `file_content` String: represents the content of `file_name` file.
    ** `error_callback` Function(err)
    *
    */

    greenworks.deleteFile = function(){
        localStorage.removeItem("fakegreenworks.file-" + arguments[0]);
        arguments[1]();
    };
    /*
    *
    ** `file_name` String
    ** `success_callback` Function()
    ** `error_callback` Function(err)
    *
    */

    greenworks.saveFilesToCloud = function(){arguments[1]();};
    /*
    *
    ** `files_path` Array of String: The files' path on local machine.
    ** `success_callback` Function()
    ** `error_callback` Function(err)
    *
    *Writes mutilple local files to Steam Cloud.
    *
    */

    greenworks.isCloudEnabledForUser = function(){return true;};
    /*
    *
    *Returns a `Boolean` indicates whether cloud is enabled in general for the
    *current Steam account.
    *
    */

    greenworks.isCloudEnabled = function(){return true;};
    /*
    *
    *Returns a `Boolean` indicates whether cloud is enabled for the current app.
    *This might return `true` independently of `greenworks.isCloudEnabledForUser()`.
    *Keep in mind that the general account setting has priority over the app specific
    *setting. So you might want to check `isCloudEnabledForUser()` first.
    *
    */

    greenworks.enableCloud = function(){};
    /*
    *
    ** `flag` Boolean
    *
    *Enables/Disables the cloud feature for the current app. Keep in mind that your
    *app won't sync anything to the user's cloud if he disabled it at top level
    *(see `greenworks.isCloudEnabledForUser()`).
    *
    */

    greenworks.getCloudQuota = function(){arguments[0](1e18, 1e18);};
    /*
    *
    ** `success_callback` Function(total_bytes, available_bytes)
    *  * `total_bytes` uint64 String: total bytes of quota
    *  * `available_bytes` uint64 String: available bytes of quota
    ** `error_callback` Function(err)
    *
    */

    greenworks.getFileCount = function(){return 0;};
    /*
    *
    *Gets the number of files on the cloud.
    *
    */

    greenworks.getFileNameAndSize = function(){
        var result = {};
        result.name = "";
        result.size = 0;
        return result;
    };
    /*
    *
    ** `index` Integer: the index of the file
    *
    *Returns an `Object`:
    *
    ** `name` String: The file name
    ** `size` Integer: The file size
    */

    // ------ DLC API ------

    greenworks.getDLCCount = function(){return 0;};
    /*
    *
    *Returns the number of DLC pieces for the current running app.
    *
    */

    greenworks.getDLCDataByIndex = function(){};
    /*
    *
    ** `index` Integer: The index of the DLC between 0 and `greenworks.getDLCCount`.
    *
    *Returns `undefined` if no DLC matching the index could be found, otherwise an object containing:
    ** `appId` Integer: The APPID of the DLC.
    ** `available` Boolean: Flag whether the DLC is currently available.
    ** `name` String: The name of the DLC.
    *
    */

    greenworks.isDLCInstalled = function(){return false;};
    /*
    *
    ** `dlc_app_id` Integer: The APPID of a DLC.
    *
    *Checks if the user owns the DLC and if the DLC is installed.
    *
    */

    greenworks.installDLC = function(){};
    /*
    *
    ** `dlc_app_id` Integer: The APPID of a DLC.
    *
    *Install a specific DLC.
    *
    */

    greenworks.uninstallDLC = function(){};
    /*
    *
    ** `dlc_app_id` Integer: The APPID of a DLC.
    *
    *Uninstall a specific DLC.
    */

    // ------ Event API ------
    // FIXME: Event API

    greenworks.on = function(){};

    // ------ Friends API ------
    // FIXME: Friends API

    // ------ Setting API ------

    greenworks.initAPI = function(){return true;};
    /*
    *
    *Returns a `Boolean` whether Steam APIs were successfully initialized or not.
    *
    *Note: When testing this, you need to launch and log in the Steam Client,
    *and create a steam_appid.txt file with your Steam APP ID
    *(or the steamworks example APP ID) under your app directory.
    *
    */


    greenworks.init = function(){return true;};
    /*
    *
    *Returns a `True` when Steam APIs were successfully initialized, otherwise throw
    *an error.
    *
    */


    greenworks.isSteamRunning = function(){return true;};
    /*
    *
    *Returns a `Boolean` whether Steam is running.
    *
    */


    greenworks.restartAppIfNecessary = function(){return true;};
    /*
    *
    ** `appId` Integer: The APP ID of your game
    *
    *If your app was not launched via Steam, this will signal Steam to launch your
    *app, and then cause your app to quit.
    *
    *There's not a moment to lose after you call `restartAppIfNecessary()`, but if
    *it returns `true`, your app is being restarted.
    *
    */


    greenworks.getAppId = function(){return 0;};  // FIXME: greenworks.getAppId
    /*
    *
    *Returns an `Integer` represents the app id of the current process.
    *
    */


    greenworks.getAppBuildId = function(){return 0;};  // FIXME: greenworks.getAppBuildId
    /*
    *
    *Returns an `Integer` representing the app's build id. May change at any time based on backend updates to the game.
    *
    */


    greenworks.getSteamId = function(){return 0;};  // FIXME: greenworks.getSteamId
    /*
    *
    *Returns an [`SteamID`](friends.md#steamid) object represents the current Steam
    *user.
    *
    */


    greenworks.getCurrentGameLanguage = function(){return "zh";};
    /*
    *
    *Returns a `String` represents the current language from Steam specifically set
    *for the game.
    *
    */


    greenworks.getCurrentUILanguage = function(){return "zh";};
    /*
    *
    *Returns a `String` represents the current language from Steam set in UI.
    *
    */


    greenworks.getCurrentGameInstallDir = function(){};
    /*
    *
    *Not implement yet.
    *
    */


    greenworks.getAppInstallDir = function(){return "";};
    /*
    *
    ** `app_id` Integer: The APP ID of your game
    *
    *Returns a `String` representing the absolute path to the app's installation directory.
    *
    */


    greenworks.getNumberOfPlayers = function(){arguments[0](1);};
    /*
    *
    ** `success_callback` Function(num_of_players)
    *  * `num_of_players` Integer: the current number of players on Steam.
    ** `error_callback` Function(err)
    *
    */


    greenworks.activateGameOverlay = function(){};
    /*
    *
    ** `option` String: valid options are `Friends`, `Community`, `Players`,
    *  `Settings`, `OfficialGameGroup`, `Stats` and `Achievements`.
    *
    *Activate the game overlay with the `option` dialog opens.
    *
    */


    greenworks.isGameOverlayEnabled = function(){return true;};
    /*
    *
    *Return `Boolean` indicates whether Steam overlay is enabled/disabled.
    *
    */


    greenworks.isSteamInBigPictureMode = function(){return true;};
    /*
    *
    *Return `Boolean` indicates whether Steam is in Big Picture mode.
    *Will always return `false` if the application is not in Steam's `game` category.
    *
    */


    greenworks.activateGameOverlayToWebPage = function(){};
    /*
    *
    ** `url` String: a full url, e.g. http://www.steamgames.com.
    *
    *Open a specified url in steam game overlay.
    *
    */


    greenworks.isSubscribedApp = function(){return true;};
    /*
    *
    ** `appId` Integer: The APP ID of your game
    *
    *Returns a `Boolean` indicates whether the user has purchased that app.
    *
    */


    greenworks.isAppInstalled = function(){return false;};
    /*
    *
    ** `appId` Integer: The APP ID of your game
    *
    *Returns a `Boolean` indicating whether the app is currently installed. The app may not actually be owned by the user.
    *
    *Only works for base applications, for DLC use `isDLCInstalled` instead.
    *
    */


    greenworks.getImageSize = function(){}; // FIXME: greenworks.getImageSize
    /*
    *
    ** `handle` Integer: The image handle
    *
    *Returns an `object` that contains image’s width and height values.
    *
    */


    greenworks.getImageRGBA = function(){}; // FIXME: greenworks.getImageRGBA
    /*
    *
    ** `handle` Integer: The image handle
    *
    *Returns a `Buffer` that contains image data in RGBA format.
    *
    *An example of saving image to `png` format:
    *
    *```js
    *var greeenworks = require('./greenworks');
    * Relies on 'jimp' module. Install it via 'npm install jimp'.
    *var Jimp = require('jimp');
    *
    *var friends = greenworks.getFriends(greenworks.FriendFlags.Immediate);
    *if (friends.length > 0) {
    *  var handle = greenworks.getSmallFriendAvatar(friends[0].getRawSteamID());
    *  if (!handle) {
    *    console.log("The user don't set small avartar");
    *    return;
    *  }
    *  var image_buffer = greenworks.getImageRGBA(handle);
    *  var size = greenworks.getImageSize(handle);
    *  if (!size.height || !size.width) {
    *    console.log("Image corrupted. Please try again");
    *    return;
    *  }
    *  console.log(size);
    *  var image = new Jimp(size.height, size.width, function (err, image) {
    *    for (var i = 0; i < size.height; ++i) {
    *      for (var j = 0; j < size.width; ++j) {
    *        var idx = 4 * (i * size.height + j);
    *        var hex = Jimp.rgbaToInt(image_buffer[idx], image_buffer[idx+1],
    *            image_buffer[idx+2], image_buffer[idx+3]);
    *        image.setPixelColor(hex, j, i);
    *      }
    *    }
    *  });
    *  image.write("/tmp/test.png");
    *}
    *```
    *
    */


    greenworks.getIPCountry = function(){return "CN";};
    /*
    *
    *Returns the 2 digit ISO 3166-1-alpha-2 format country code which client is running in, e.g "US" or "UK".
    *
    */


    greenworks.getLaunchCommandLine = function(){}; // FIXME: greenworks.getLaunchCommandLine
    /*
    *
    *Gets the command line if the game was launched via Steam URL, e.g. `steam://run/<appid>//<command line>/`. This method is preferable to launching with a command line via the operating system, which can be a security risk. In order for rich presence joins to go through this and not be placed on the OS command line, you must enable "Use launch command line" from the Installation > General page on your app.
    *
    *[Steam docs](https://partner.steamgames.com/doc/api/ISteamApps#GetLaunchCommandLine)
    */


    // ------ Stat API ------


    greenworks.getStatInt = function(){return 1;};
    /*
    *
    ** `name` String: The name of the user stat
    *
    *Returns a `Integer` represents the value of the user stat.
    *
    */


    greenworks.getStatFloat = function(){return 1;};
    /*
    *
    ** `name` String: The name of the user stat
    *
    *Returns a `Float` represents the value of the user stat.
    *
    */


    greenworks.setStat = function(){return true;};
    /*
    *
    ** `name` String: The name of the user stat
    ** `value` Integer or Float: The value of the user stat
    *
    *Returns a `Boolean` indicates whether the method succeeds.
    *
    */


    greenworks.storeStats = function(){arguments[0](0);};
    /*
    *
    ** `success_callback` Function(game_id)
    *  * `game_id`: The game which these stats are for
    ** `error_callback` Function(err)
    *
    *Stores the current user stats data on the server.
    */


    // ------ Utils API ------

    greenworks.Utils = {};

    greenworks.Utils.move = function(){arguments[2]();};
    /*
    *
    ** `source_dir` String
    ** `target_dir` String
    ** `success_callback` Function()
    ** `error_callback` Function(err)
    *
    *Moves `source_dir` to `target_dir`.
    *
    */


    greenworks.Utils.createArchive = function(){arguments[4]();};
    /*
    *
    ** `zip_file_path` String
    ** `source_dir` String
    ** `password` String: Empty represents no password
    ** `compress_level` Integer: Compress factor 0-9, store only - best compressed.
    ** `success_callback` Function()
    ** `error_callback` Function(err)
    *
    *Creates a zip archive of `source_dir`.
    *
    */


    greenworks.Utils.extractArchive = function(){arguments[3]();};
    /*
    *
    ** `zip_file_path` String
    ** `extract_dir` String
    ** `password` String: Empty represents no password
    ** `success_callback` Function()
    ** `error_callback` Function(err)
    *
    *Extracts the `zip_file_path` to the specified `extract_dir`.
    */


    // ------ Workshop API ------
    // FIXME: Workshop API

    var oldrequire = require;
    require = function(name) {
        if(name.endsWith("greenworks")) {
            return require.greenworks;
        }
        return require.oldrequire(name);
    };
    require.greenworks = greenworks;
    require.oldrequire = oldrequire;
})();