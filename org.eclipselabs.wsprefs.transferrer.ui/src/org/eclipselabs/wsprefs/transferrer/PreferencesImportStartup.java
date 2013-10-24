/*
 * *******************************************************************************************************************
 * Copyright (c) 2011 Michael Pellaton and others. All rights reserved.
 * This program and the accompanying materials are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Michael Pellaton
 * *******************************************************************************************************************
 */
package org.eclipselabs.wsprefs.transferrer;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.preferences.ConfigurationScope;
import org.eclipse.core.runtime.preferences.IPreferenceFilter;
import org.eclipse.core.runtime.preferences.IPreferencesService;
import org.eclipse.core.runtime.preferences.InstanceScope;
import org.eclipse.ui.IStartup;
import org.eclipse.ui.PlatformUI;
import org.osgi.framework.Bundle;
import org.osgi.framework.FrameworkUtil;

/**
 * {@link IStartup} implementation that imports all preferences from the
 * {@code .wsprefs} file located in the workspace's root directory. After
 * the successful import, the file is discarded to avoid a second import.
 */
public class PreferencesImportStartup implements IStartup {

  /** {@inheritDoc} */
  @Override
  public void earlyStartup() {
    IPath workspaceRootPath = ResourcesPlugin.getWorkspace().getRoot().getLocation();
    final File importFile = workspaceRootPath.append(WSPrefsFileUtil.FILENAME).toFile();

    if (importFile.canRead()) {

      // Import from file in a worker thread, not the UI thread.
      PlatformUI.getWorkbench().getDisplay().asyncExec(new Runnable() {

        /** {@inheritDoc} */
        @Override
        public void run() {
          Bundle bundle = FrameworkUtil.getBundle(getClass());
          IStatus status = Status.OK_STATUS;

          InputStream inputStream = null;
          try {
            inputStream = new FileInputStream(importFile);
            importConfigurationFromStream(inputStream);
          } catch (CoreException e) {
            status = convertToErrorStatus(bundle , e);
          } catch (IOException e) {
            status = convertToErrorStatus(bundle, e);
          } finally {
            if (inputStream != null) {
              try {
                inputStream.close();
              } catch (IOException e) {
                // Well, nothing useful to do...
              }
            }
          }
          status = deleteFile(bundle, status, importFile);
          handleStatus(bundle, status);
        }
      });
    }
  }


  private void importConfigurationFromStream(InputStream inputStream) throws CoreException {
    IPreferencesService service = Platform.getPreferencesService();
    service.applyPreferences(service.readPreferences(inputStream), getPreferenceImportFilters());
  }


  private IPreferenceFilter[] getPreferenceImportFilters() {
    IPreferenceFilter filter = new IPreferenceFilter() {

      /** {@inheritDoc} */
      @Override
      public String[] getScopes() {
        return new String[] {ConfigurationScope.SCOPE, InstanceScope.SCOPE};
      }

      /** {@inheritDoc} */
      @Override
      public Map<?, ?> getMapping(String scope) {
        return null;
      }
    };

    return new IPreferenceFilter[] {filter};
  }


  private IStatus deleteFile(Bundle bundle, IStatus status, File file) {
    if (!file.delete()) {
      String message = "The preference file could not be deleted: " + file.getName();
      return new Status(IStatus.WARNING, bundle.getSymbolicName(), message );
    }
    return status;
  }


  private IStatus convertToErrorStatus(Bundle bundle, Exception exception) {
    String message = "There was an error while importing the workspace preferences.";
    return new Status(IStatus.ERROR, bundle.getSymbolicName(), message, exception);
  }


  private void handleStatus(Bundle bundle, IStatus status) {
    if (!status.isOK()) {
      Platform.getLog(bundle).log(status);
    }
  }
}
