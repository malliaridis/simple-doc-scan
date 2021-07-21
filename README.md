# simple-doc-scan
A simple scan app for scanning documents and sending them via email.

## Technologies
This project is a simple demonstration of how to integrate and use the [GeniusSDK library](https://geniusscansdk.com/) for scanning and cropping documents. It uses some up-to-date (at the moment of writing) architecture components.

## How to setup
The project can be used straight-forward, clone and execute the app.

It is limited however due to licensing reasons to 60 seconds from the moment of accessing the scan step (`DocScanFragment`). If you want to remove this limitation you have to provide a license key of the GeniusSDK in the `secrets.properties` file and uncomment the first line in  `DocScanFragment.startCamera()` called `initializeGeniusScanSDK()`.

## Possible Optimizations
This project can be optimized in the following aspects:
- Replace GeniusSDK-Library completely because
  - it is outdated (makes use of deprecated libraries)
  - its is badly documented
  - it costs
- File names stored in cache can contain a generated UUID to uniquely be identified and not cause a conflict with previous snapshots
- Images can also be stored locally
- Image rotation options could be provided in sending step for rotating image manually before sending
- The message title date is locally formatted and may be standardized
- Progress onResume could be abstracted in a fragment, however the `DocScanFragment` does not implement the `Fragment` and instead uses the `ScanFragment`, which does not allow the merge of progress actions
- The permissions handling can be better solved by providing a button to navigate directly to the apps settings for granting camera permissions
- A loading bar can be displayed between snapshot taken and navigation to the next step
- Camera dependencies version is alpha (not stable) and could be changed
- Further instructions could be provided
- The theme colors are the default colors (by template) and could be changed
- Some steps does not provide information what to do (e.g. scan process) and could be optimized
- The top app bar could be updated according to the current step the use is executing

## Known Bugs and Limitations
This project has a couple of limitations or issues if you want to call it that way:
- Image is not freezing instantly when document detection trigger is triggered -> This is because of the refocus (can be disabled with `ScanFragment.takePicture(file, false)`)
- App crashes on back navigation after taking image if the fragment is updating the user guidance and gets detached in the meantime
- If the top-level domain is invalid (e.g. ".y") it is not recognized as an issue with the current validation and therefore the email service may ignore it completely (tested with Gmail)
- Error handling when license is expired or not set is not properly handled
- Image dimensions are not validated yet
- This project does not have any tests implemented because it started as an empty project and was developed without any classes, functions or structure. It also had some time limitations. For further development it is strongly recommended to add tests
- Some issues and app crashes occur on device rotation. It was developed on portrait mode and not validated yet for different orientations.

## License
This project may be subject of third-party licenses.