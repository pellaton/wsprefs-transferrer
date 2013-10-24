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
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;

import org.eclipse.core.runtime.IPath;

/**
 * Utility class to deal with the {@code .wsprefs} file used to transfer the
 * preferences between the workspaces.
 */
public final class WSPrefsFileUtil {

  /**
   * File name of the preference file.
   */
  public static final String FILENAME = ".wsprefs";


  /**
   * Private constructor to avoid instantiation.
   */
  private WSPrefsFileUtil() {
    throw new AssertionError("Not instantiable");
  }


  /**
   * Gets the output stream of the file to export the preferences into based on
   * the target workspace root directory passed as argument. If this target
   * workspace root does not exist, an attempt to create it is done.
   *
   * @param targetWorkspaceRootPath the target workspace root directory
   * @return the file to export the preferences into
   * @throws FileNotFoundException if the file cannot be created
   */
  static final OutputStream getExportFileFromPath(IPath targetWorkspaceRootPath)  throws FileNotFoundException {
    File targetWorkspaceRootFile = new File(targetWorkspaceRootPath.toOSString());
    if (!targetWorkspaceRootFile.exists()) {
      if (!targetWorkspaceRootFile.mkdirs()) {
        return null;
      }
    }
    return new FileOutputStream(new File(targetWorkspaceRootFile, FILENAME));
  }
}
