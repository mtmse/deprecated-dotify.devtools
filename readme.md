[![Build Status](https://travis-ci.org/brailleapps/dotify.devtools.svg?branch=master)](https://travis-ci.org/brailleapps/dotify.devtools)
[![Type](https://img.shields.io/badge/type-library_bundle-blue.svg)](https://github.com/brailleapps/wiki/wiki/Types)

# dotify.devtools #

In addition to the Dotify project, there is a DotifyDevtools project in the code repository. DotifyDevtools contains tools that are useful when developing for Dotify, but are not needed when running the final software.

## Main Features ##
Tools included in devtools:
  * GenerateTableEntries
  * CodePointUtility
  * SchematronRulesGenerator
  * PefFileCompareUI
  * Unbrailler
  * and more

### GenerateTableEntries ###
This tool allows a user to generate braille table entries for a span of unicode characters that are to be added to a braille table. Currently, the parameters are embedded in the code, and need to be recompiled if modified.

### CodePointUtility ###
This tool is a Swing UI that allows a user to get unicode code points for a string of characters or the other way around. In addition, it allows a user to convert braille p-notation into the corresponding unicode braille pattern (block 0x2800).

### SchematronRulesGenerator ###
This tool can be used to build schematron files using a compact java notation.

### PefFileCompareUI ###
This tool can be used for regression testing. It allows detailed comparing between two folders of pef-files, ignoring meta data.

#### Environment variables ####
`org.daisy.dotify.devtools.regression.baseline`

values: `update` or `compare` (default)

`org.daisy.dotify.devtools.regression.mode`

values: `convert` or `legacy` (default) 

### Unbrailler ###
This tool can be used to compare text differences in an xml-editor. A folder is scanned for PEF-files and the braille in each file is replaced by ascii characters for easier debugging.

## Using ##
To use the bundle<strike>, download the [latest release](http://search.maven.org/#search%7Cga%7C1%7Cg%3A%22org.daisy.dotify%22%20AND%20a%3A%22dotify.devtools%22) from maven central</strike> install it in your local maven cache with:

`gradlew install` (Windows) or `./gradlew install` (Mac/Linux)


## Building ##
Build with `gradlew build` (Windows) or `./gradlew build` (Mac/Linux)

## Testing ##
Tests are run with `gradlew test` (Windows) or `./gradlew test` (Mac/Linux)

## Javadoc ##
Javadoc for the latest Dotify Devtools is available [here](http://brailleapps.github.io/dotify.devtools/latest/javadoc/).

## More information ##
See the [common wiki](https://github.com/brailleapps/wiki/wiki) for more information.
