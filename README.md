#![Screenshot](/img/logo.png) Eclipse wsprefs-transferrer

The Eclipse Workspace Preferences Transferrer extends [Eclipse's Switch Workspace feature](http://help.eclipse.org/indigo/index.jsp?topic=%2Forg.eclipse.platform.doc.user%2Freference%2Fref-workspaceswitch.htm) to copy all preferences from the current workspace to another one. 

## Quick Start
### Installation

Issue #2 - TODO

### Usage
1. ```File → Switch Workspace → Other…```
1. Select the target workspace
1. Check the option <i>Preferences</i>
1. Hit OK 

![Screenshot](/img/wsprefs.png)

## Internals
This Eclipse plug-in does exactly the same as if the user did a preferences export into a file in 
the current workspace and a preferences import in the other one. The functionality is split into two steps:

### Export Step
- The preferences are exported into the file ```$NEW_WORKSPACE/.wsprefs```
- This functionality is based on
  - [org.eclipse.ui.preferenceTransfer](http://help.eclipse.org/indigo/topic/org.eclipse.platform.doc.isv/reference/extension-points/org_eclipse_ui_preferenceTransfer.html?resultof=%22%6f%72%67%2e%65%63%6c%69%70%73%65%2e%75%69%2e%70%72%65%66%65%72%65%6e%63%65%54%72%61%6e%73%66%65%72%22%20)
  - [SettingsTransfer](http://help.eclipse.org/indigo/index.jsp?topic=%2Forg.eclipse.platform.doc.isv%2Freference%2Fapi%2Forg%2Feclipse%2Fui%2Fpreferences%2FSettingsTransfer.html)
  - [IPreferencesService#exportPreferences()](http://help.eclipse.org/indigo/index.jsp?topic=%2Forg.eclipse.platform.doc.isv%2Freference%2Fapi%2Forg%2Feclipse%2Fcore%2Fruntime%2Fpreferences%2FIPreferencesService.html)


### Import Step
- If the file ```$NEW_WORKSPACE/.wsprefs``` exists, it is imported during the workspace startup.
- The file is deleted after the import
- This functionality is based on
  - [org.eclipse.ui.startup](http://help.eclipse.org/indigo/topic/org.eclipse.platform.doc.isv/reference/extension-points/org_eclipse_ui_startup.html?resultof=%22%49%53%74%61%72%74%75%70%22%20%22%69%73%74%61%72%74%75%70%22%20)
  - [IStartup](http://help.eclipse.org/indigo/topic/org.eclipse.platform.doc.isv/reference/api/org/eclipse/ui/IStartup.html)
  - [IPreferencesService#applyPreferences()](http://help.eclipse.org/indigo/index.jsp?topic=%2Forg.eclipse.platform.doc.isv%2Freference%2Fapi%2Forg%2Feclipse%2Fcore%2Fruntime%2Fpreferences%2FIPreferencesService.html)
