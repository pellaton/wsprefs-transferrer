#!/usr/bin/python
from xml.etree.ElementTree import ElementTree
import os
import prepare_lib


if __name__ == '__main__':
  mvn_version = prepare_lib.get_mvn_snapshot_version_from_pom()
  osgi_rel_version = mvn_version[:-(len('-SNAPSHOT'))]
  osgi_dev_version = osgi_rel_version + '.qualifier'
  for filename in ['MANIFEST.MF', 'feature.xml', 'site.xml']:
    prepare_lib.replace(filename, osgi_dev_version, osgi_rel_version)
  
  