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

import java.io.IOException;
import java.io.OutputStream;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.core.runtime.preferences.IPreferencesService;
import org.eclipse.core.runtime.preferences.InstanceScope;
import org.eclipse.ui.preferences.SettingsTransfer;
import org.osgi.framework.FrameworkUtil;

/**
 * {@link SettingsTransfer} implementation that exports all preferences into
 * the file {@code .wsprefs} located in the target workspace's root directory.
 */
public class PreferencesSettingsTransfer extends SettingsTransfer {

  /** {@inheritDoc} */
  @Override
  public IStatus transferSettings(IPath targetWorkspaceRootPath) {
    IPreferencesService service = Platform.getPreferencesService();
    IEclipsePreferences preferenceNode = (IEclipsePreferences) service.getRootNode().node(InstanceScope.SCOPE);
    OutputStream outputStream = null;
    IStatus status = Status.OK_STATUS;
    try {
      outputStream = WSPrefsFileUtil.getExportFileFromPath(targetWorkspaceRootPath);
      status = service.exportPreferences(preferenceNode, outputStream, null);
      outputStream.flush();
    } catch (CoreException e) {
      status = convertToErrorStatus(e);
    } catch (IOException e) {
      status = convertToErrorStatus(e);
    } finally {
      if (outputStream != null) {
        try {
          outputStream.close();
        } catch (IOException e) {
          // Well, nothing useful to do...
        }
      }
    }
    return status;
  }

  /** {@inheritDoc} */
  @Override
  public String getName() {
    return WSPrefsFileUtil.FILENAME;
  }

  private IStatus convertToErrorStatus(Exception exception) {
    String message = "There was an error while exporting the workspace preferences.";
    return new Status(IStatus.ERROR, FrameworkUtil.getBundle(this.getClass()).getSymbolicName(), message, exception);
  }
}
