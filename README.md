<picture>
    <source media="(prefers-color-scheme: dark)" srcset="./.github/images/logo-wordmark-dark.svg">
    <img alt="FixMyLinks" src="./.github/images//logo-wordmark-light.svg" height="96">
</picture>

**🔗🛠️ Replace domain names and remove unwanted tracking data from links shared on Android**

---

# How do I use FixMyLinks?

## Replace domain names

Bring back **twitter.com**! With FixMyLinks, you can turn **x.com** links back into **twitter.com** links and remove all tracking data ([URL parameters](#remove-unwanted-url-parameters)) from these links while you're at it.

You can also set up [custom rules](#custom-rules) to replace domain names in any other links you wish to modify.

## Remove unwanted URL parameters

Content links generated by apps via their "share" functionality often contain URL parameters used for tracking purposes. FixMyLinks makes it super easy to ensure these links don't contain tracking parameters by removing them for you.

> [!NOTE]
> URL parameters can also be used for necessary purposes that affect the link's destination and/or the content you see on screen. Therefore, it's important to avoid removing such parameters from links when creating [custom rules](#custom-rules).

<small>
    Find out more about tracking parameters <a href="https://support.google.com/google-ads/answer/6277564?hl=en-GB#:~:text=on%20your%20website.-,Tracking%20parameters,-pass%20information%20about">here</a> (external link)
</small>

## Installation

Currently, the only way to install FixMyLinks is to build from source. Other installation options may be added in the future.

### Build from source

<details>
<summary>
    <b>Prerequisites</b>
</summary>

-   [Git](https://git-scm.com/)
-   [Android Studio](https://d.android.com/studio)
-   [Android Debug Bridge (ADB)](https://developer.android.com/tools/adb) (should be bundled with Android Studio)
-   A [physical](https://developer.android.com/studio/run/device) or [virtual](https://developer.android.com/studio/run/managing-avds) Android device connected to ADB
</details>

<details>
<summary>
  <b>Instructions</b>
</summary>

1. Clone the repository and enter the project directory
    ```sh
    git clone https://github.com/suvanl/FixMyLinks.git FixMyLinks
    ```
    ```sh
    cd FixMyLinks
    ```
2. Create a **local.properties** file in the project root containing the path to the Android SDK:
    <details>
       <summary>
       <b>Windows</b>
       </summary>

    ```sh
    echo "sdk.dir=C\:\\Users\\<YOUR-USERNAME>\\AppData\\Local\\Android\\Sdk" >> local.properties
    ```

    </details>

    <details>
       <summary>
       <b>macOS</b>
       </summary>

    ```sh
    echo "sdk.dir = /Users/<YOUR-USERNAME>/Library/Android/sdk" >> local.properties
    ```

    </details>

    <details>
       <summary>
       <b>Linux</b>
       </summary>

    ```sh
    echo "sdk.dir = /home/<YOUR-USERNAME>/Android/sdk" >> local.properties
    ```

    </details>

3. Build the app
    <details>
       <summary>
       <b>Windows</b>
       </summary>

    ```sh
    .\gradlew assemble
    ```

    </details>

    <details>
       <summary>
       <b>macOS and Linux</b>
       </summary>

    ```sh
    ./gradlew assemble
    ```

    > You may need to grant execute permissions to `gradlew`:
    >
    > ```sh
    > chmod +x gradlew
    > ```

    </details>

4. Install the built APK on a connected device
    ```sh
    adb install app/build/outputs/apk/debug/app-debug.apk
    ```
5. Open the app using the device's launcher. Alternatively, use the following command to run the app:
    ```sh
    adb shell am start -n "com.suvanl.fixmylinks/com.suvanl.fixmylinks.MainActivity" -a android.intent.action.MAIN -c android.intent.category.LAUNCHER
    ```
    > Optionally, you may use the `--splashscreen-show-icon` flag.

</details>

## Usage

The following usage examples assume that FixMyLinks is installed on a supported Android device.

### Example 1: modifying links when sharing content

1. Share content from an app that a [built-in](#built-in-rules) or [custom](#custom-rules) rule exists for, such as Twitter/X.
    1. On a piece of shareable content, such as a Tweet/Post, tap the share button. This may open a custom share sheet provided by the app or may open the native share sheet.
    2. If a custom share sheet is displayed, tap the option to open the native share sheet (i.e., "Share via..." in the Twitter/X app).
2. In the native share sheet, select the FixMyLinks "Share to.." option
3. This will recreate the share sheet, but for a modified link as per the rule that was matched.
4. Select any share option you wish to use. The share target will receive the modified link.

### Example 2: using built-in rules

✨ **Coming soon**

### Example 3: using custom rules

✨ **Coming soon**

## Built-in rules

FixMyLinks currently contains the following rules out of the box:

-   Twitter/X\*\*
-   Instagram\*
-   Spotify\*

<small>
    * remove tracking parameters 
    <br>
    ** replace domain name and remove tracking parameters
</small>

<br>
<br>

More built-in rules will be added in future releases.

## Custom rules

✨ **Coming soon** &mdash; custom rule support will be implemented in a future release.

# License

FixMyLinks is licensed under the [GNU General Public License v3.0 (GPL-3.0)](https://github.com/suvanl/FixMyLinks/blob/main/LICENSE).
