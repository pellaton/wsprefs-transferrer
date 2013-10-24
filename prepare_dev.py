#!/usr/bin/python
from xml.etree.ElementTree import ElementTree
import os
import prepare_lib


def _get_next_mvn_rel_version(current_mvn_dev_version):
    return current_mvn_dev_version[:-(len('-SNAPSHOT'))]


def _get_previous_mvn_rel_version(next_mvn_rel_version):
  components = next_mvn_rel_version.split('.')
  components[-1] = str(int(components[-1]) - 1)
  return '.'.join(components)


if __name__ == '__main__':
  current_mvn_dev_version = prepare_lib.get_mvn_snapshot_version_from_pom()
  next_mvn_rel_version = _get_next_mvn_rel_version(current_mvn_dev_version)
  previous_mvn_rel_version = _get_previous_mvn_rel_version(next_mvn_rel_version)
  current_osgi_dev_version = next_mvn_rel_version + '.qualifier'
  for filename in ['MANIFEST.MF', 'feature.xml', 'site.xml']:
    prepare_lib.replace(filename, previous_mvn_rel_version, current_osgi_dev_version)
