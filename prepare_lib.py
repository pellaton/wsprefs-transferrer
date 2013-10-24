#!/usr/bin/python
from xml.etree.ElementTree import ElementTree
import os


def _escape(version):
  return version.replace('.', '\\.').replace('-', '\\-')


def get_mvn_snapshot_version_from_pom():
  pom_file = 'pom.xml'
  if not os.path.exists(pom_file):
    raise Exception('no pom.xml in current working directory')

  pom = ElementTree(file=pom_file)
  mvn_version = pom.find('{http://maven.apache.org/POM/4.0.0}version').text
  if not mvn_version.endswith('-SNAPSHOT'):
    raise Exception('not a snapshot version')
  return mvn_version


def replace(filename, old_version, new_version):
  command = "find . -name {filename} | xargs sed -e 's/{oldversion}/{newversion}/g' -i ''"
  os.popen(command.format(filename=filename, oldversion=_escape(old_version), newversion=_escape(new_version)))

